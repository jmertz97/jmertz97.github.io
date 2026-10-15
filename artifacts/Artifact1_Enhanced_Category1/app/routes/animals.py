from flask import Blueprint, jsonify, render_template, request
from pymongo.errors import *
from dotenv import load_dotenv
import os
import sys

from app.models import AnimalShelter
from app.data.rescue_animal_categories import QUERIES_BY_CATEGORY


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
	return render_template('dashboard.html')

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
		animal_records = shelterDB.read(query, {"_id": 0, "index": 0})
	except TypeError:
		# "Bad Request" - raised by AnimalShelter
		return jsonify({'error': "The query format is invalid"}), 400
	except OperationFailure:
		# "Bad Request" - raised by DB
		return jsonify({'error': "The query was rejected by the database"}), 400
	except ConnectionFailure:
		# "Service Unavailable"
		return jsonify({'error': "Unable to connect to the database"}), 503
	except:
		# "Internal Server Error" - any other error
		return jsonify({'error': "Unexpected error occurred"}), 500

	return jsonify(animal_records)
