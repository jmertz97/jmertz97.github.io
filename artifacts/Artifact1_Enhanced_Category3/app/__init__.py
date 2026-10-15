from flask import Flask
from app.routes import animals_blueprint

def create_app():
	# factory pattern for Flask app
	app = Flask(__name__)
	app.register_blueprint(animals_blueprint)  # only blueprint so far
	return app
