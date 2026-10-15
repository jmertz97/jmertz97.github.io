# separate script to simplify user installation
# creates an empty db with proper schema typing, then populates it with data from aac_shelter_outcomes.csv
import csv
import os

from datetime import datetime
from dotenv import load_dotenv
from pymongo import MongoClient  # noqa
from pymongo.errors import BulkWriteError  # noqa
from pathlib import Path
from app.data.valid_db_vals import VALID_OUTCOME_TYPES, VALID_OUTCOME_SUBTYPES, VALID_SEXES_UPON_OUTCOME



csv_path = Path(__file__).resolve().parent / "src" / "aac_shelter_outcomes.csv"
float_fields = {"location_lat", "location_long", "age_upon_outcome_in_weeks"}
date_fields = {"datetime", "date_of_birth"}

animal_id_pattern = '^A[0-9]{6}$'


def build_anyof_branches():
	# builds the anyOf branches of the validator $jsonSchema, using data obtained from data/valid_db_vals.py
	anyOf = []

	for outcome_type, outcome_subtypes in VALID_OUTCOME_TYPES.items():
		valid_outcome_subtypes = [subtype for subtype in outcome_subtypes if subtype]
		outcome_branch = {'properties': {'outcome_type': {'enum': [outcome_type]}}}
		if valid_outcome_subtypes:
			outcome_branch['properties']['outcome_subtype'] = {'enum': valid_outcome_subtypes}
			if '' not in outcome_subtypes:
				outcome_branch['required'] = ['outcome_subtype']  # noqa
		else:
			outcome_branch['not'] = {'required': ['outcome_subtype']}  # noqa
		anyOf.append(outcome_branch)

	return anyOf


def build_validator():
	# builds and returns the validation schema
	return {
		'$jsonSchema': {
			'bsonType': 'object',
			'title': "Animal Object Validation",
			'anyOf': build_anyof_branches(), 
			'dependencies': {'outcome_subtype': ['outcome_type']},  # ensures subtype accompanied by type
			'required': [ 'animal_id' , 'location_lat', 'location_long', 'animal_type', 'datetime', 'sex_upon_outcome' ],
			'properties': {
				'animal_id': {
					'bsonType': 'string',
					'pattern': animal_id_pattern,
					'description': "'animal_id' [required] must be a string of A and a six-digit number from 000000 to 999999"
				},
				"name": {
					'bsonType': 'string',
					'description': "'name' must be a string if the field exists"
				},
				'animal_type': {
					'bsonType': 'string',
					'description': "'animal_type' is required and must be a string"
				},
				'breed': {
					'bsonType': 'string',
					'description': "'breed' must be a string if the field exists"
				},
				'color': {
					'bsonType': 'string',
					'description': "'color' must be a string if the field exists"
				},
				'outcome_type': {
					'bsonType': 'string',
					'enum': sorted(VALID_OUTCOME_TYPES),
					'description': "'outcome_type' must be a known outcome type (string) if the field exists"
				},
				'outcome_subtype': {
					'bsonType': 'string',
					'enum': sorted(VALID_OUTCOME_SUBTYPES),
					'description': "'outcome_subtype' must be a known outcome subtype (string) if the field exists"
				},
				'sex_upon_outcome': {
					'bsonType': 'string',
					'enum': sorted(VALID_SEXES_UPON_OUTCOME),
					'description': "'sex_upon_outcome' is required and must be a known outcome sex (string)"
				},
				'datetime': {
					'bsonType': 'date',
					'description': "'datetime' is required and must be a valid date"
				},
				'date_of_birth': {
					'bsonType': 'date',
					'description': "'date_of_birth' must be a date if the field exists"
				},
				'location_lat': {
					'bsonType': 'double',
					'minimum': -90.0,
					'maximum': 90.0,
					'description': "'location_lat' is required and must be a double"
				},
				'location_long': {
					'bsonType': 'double',
					'minimum': -180.0,
					'maximum': 180.0,
					'description': "'location_long' is required and must be a double"
				},
				'age_upon_outcome_in_weeks': {
					'bsonType': 'double',
					'minimum': 0.0,
					'description': "'age_upon_outcome_in_weeks' must be a double if the field exists"
				}
			}
		}
	}

def apply_typing(csv_doc):
	# traverse the csv to convert data types before populating the db
	out_csv = []
	for csv_row in csv_doc:
		if not any(csv_row.values()):
			continue  # skip blank rows
		out_row = {}
		for cell in csv_row:
			if csv_row[cell]:
				if cell in float_fields:
					out_row[cell] = float(csv_row[cell])
				elif cell in date_fields:
					out_row[cell] = datetime.fromisoformat(csv_row[cell])
				else:
					out_row[cell] = csv_row[cell]
		out_csv.append(out_row)
	return out_csv


def main():
	load_dotenv()
	# raise error if unable to access .env
	mongoUsername = os.environ['MONGO_USER']
	mongoPassword = os.environ['MONGO_PASS']
	mongoHost = os.environ['MONGO_HOST']
	mongoPort = os.environ['MONGO_PORT']

	# AnimalShelter CRUD module is for runtime, not setup, therefore not used here
	db = MongoClient(mongoHost, int(mongoPort), username=mongoUsername, password=mongoPassword)['aac']

	with open(csv_path, 'r', newline='', encoding='utf-8') as csvfile:
		reader = csv.DictReader(csvfile)
		out_csv = apply_typing(reader)

	db.animals.drop()
	db.create_collection('animals', validator=build_validator())

	try:
		result = db.animals.insert_many(out_csv, ordered=False)
		print(f"Inserted {len(result.inserted_ids)} of {len(out_csv)}")
	except BulkWriteError as e:
		print(f"Inserted {e.details['nInserted']} of {len(out_csv)}")
		print(f"Rejected {len(e.details['writeErrors'])} of {len(out_csv)}")
		for err in e.details['writeErrors'][:6]:
			print(err['index'], err.get('errInfo'))

if __name__ == '__main__':
	main()
