# Austin Animal Center Dashboard

This is a Flask web app that allows users to view and make constrained edits to a table of database records within a dashboard.

## Requirements
- Python 3 (program was developed on 3.14)
- MongoDB running locally

## Setup
1) Install the dependencies listed in `requirements.txt`.
	```
	pip install -r requirements.txt
	```
2) Ensure a MongoDB instance is active and that a user with read/write access has been configured. From within `mongosh`:
	```
	use admin
	db.createUser({ user: "your_username", pwd: "your_password", roles: [{ role: "readWrite", db: "aac" }] })
	```
3) Copy `.env.example` to create a new `.env` file. Ensure the values for `MONGO_USER` and `MONGO_PASS` match the database's configured user credentials, and that the `MONGO_HOST` and `MONGO_PORT` values match.
4) Build the database by running `python -m db_import.import_db_data` from the project root. The script will use the user credentials in `.env` to access MongoDB and populate the `animals` collection within the `aac` database with the records obtained from `db_import/src/aac_shelter_outcomes.csv`. If the `animals` collection within the `aac` database already exists, the script will overwrite its contents entirely.
	```
	python -m db_import.import_db_data
	```
	Note: Of the 10,000 records in the `aac_shelter_outcomes.csv`, one should be rejected by the DB schema for having a negative age value. The first line of terminal output should display `Inserted 9999 of 10000`.

## Running
Run the app from the project root via terminal:
   ```
   flask run
   ```
The terminal will then display the URL where the project may be accessed in a web browser (typically http://127.0.0.1:5000).
