from bson import ObjectId
from bson.errors import InvalidId
from flask import Blueprint, jsonify, render_template, request
from pymongo.errors import *
from dotenv import load_dotenv
import os
import sys

from app.models import AnimalShelter
from app.data.rescue_animal_categories import QUERIES_BY_CATEGORY
from app.data.valid_db_vals import VALID_OUTCOME_TYPES, VALID_OUTCOME_SUBTYPES


animals_blueprint = Blueprint('animals', __name__)

load_dotenv()
try:
	# obtain DB username, password from .env
	shelterDB = AnimalShelter(os.getenv("MONGO_USER"), os.getenv("MONGO_PASS"))
except Exception as e:
	# abort program if failed to connect successfully to DB
	print(e)  # substitute for more robust error logging
	sys.exit(1)


# dashboard main route
@animals_blueprint.route('/')
def dashboard():
	return render_template('dashboard.html', valid_outcomes=VALID_OUTCOME_TYPES)


# api route called during initial page load, and after changing selected dropdown option
@animals_blueprint.route("/api/animals", methods=['GET'])
def get_animals():
	category = request.args.get('category')  # category passed in via query parameters
	
	if category is None:  # default case - no query parameter
		query = {}
	elif category in QUERIES_BY_CATEGORY:  # valid query parameter present
		query = QUERIES_BY_CATEGORY[category]
	else:  # invalid query parameter present
		return jsonify({'error': f"Unknown category: {category}"}), 400

	try:  # submit query to DB; projection filters out id and index columns
		animal_records = shelterDB.read(query)
	except TypeError:
		# "Bad Request" - raised by AnimalShelter
		return jsonify({'error': "The query format is invalid"}), 400
	except OperationFailure:
		# "Bad Request" - raised by DB
		return jsonify({'error': "The query was rejected by the database"}), 400
	except ConnectionFailure:
		# "Service Unavailable"
		return jsonify({'error': "Unable to connect to the database"}), 503
	except Exception:
		# "Internal Server Error" - any other error
		return jsonify({'error': "Unexpected error occurred"}), 500

	reformat_result(animal_records)  # mutates animal_records in place
	return jsonify(animal_records)


# update
@animals_blueprint.route("/api/animals/<entry_id>", methods=['PUT'])
def update_animal(entry_id):
	newValues = request.get_json(silent=True)
	if not isinstance(newValues, dict):
		return jsonify({'error': "Request body must be a JSON object"}), 400
	
	outcome_type = newValues.get('outcome_type')
	outcome_subtype = newValues.get('outcome_subtype') or ''  # convert none/null to empty string

	error = validate_outcome(outcome_type, outcome_subtype)
	if error:
		return jsonify({'error': error}), 400


	updates = {'$set': {'outcome_type': outcome_type}}
	if outcome_subtype:
		updates.get('$set').update({'outcome_subtype': outcome_subtype})
	else:
		updates.update({'$unset': {'outcome_subtype': ''}})

	try:
		oid = ObjectId(entry_id)
	except InvalidId:
		return jsonify({'error': "Invalid record id"}), 400

	try:
		updated_entry = shelterDB.update({'_id': oid}, updates)
		if updated_entry is not None:
			reformat_result([updated_entry])  # wrapped in [] because reformat_result expects a list
			return jsonify(updated_entry)
		else:
			return jsonify({'error': "No such animal record"}), 404					
	except OperationFailure as e:
		if e.code == 121:
			# mongodb rejects the query
			return jsonify({'error': "The query was rejected by the database"}), 400
		else:
			return jsonify({'error': "Unexpected error occurred"}), 500
	except ConnectionFailure:
		return jsonify({'error': "Unable to connect to the database"}), 503


def validate_outcome(outcome_type, outcome_subtype):
	# validate outcome_type / outcome_subtype changes before sending data to the DB
	if not outcome_type:
		return "outcome_type is required"
	elif not isinstance(outcome_type, str) or not isinstance(outcome_subtype, str):
		return "outcome_type and outcome_subtype must be strings"
	elif outcome_type not in VALID_OUTCOME_TYPES:
		return f"Unknown outcome_type: {outcome_type}"
	elif outcome_subtype not in VALID_OUTCOME_TYPES[outcome_type]:
		return f"Invalid outcome_subtype for {outcome_type}: {outcome_subtype}"
	return None


def reformat_result(result):
	# converts DB datatime objects to iso formatting for easier handling by the JS
	for entry in result:
		if '_id' in entry:
			entry['_id'] = str(entry['_id'])
		for field in entry:
			if field in ('datetime', 'date_of_birth'):
				entry[field] = entry[field].isoformat()
