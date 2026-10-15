from pymongo import MongoClient  # noqa
from pymongo import ReturnDocument
import os

class AnimalShelter(object): 
    """ CRUD operations for Animal collection in MongoDB """ 

    def __init__(self, mongoUsername=None, mongoPassword=None):
        mongoHost = os.getenv('MONGO_HOST', 'localhost')  # fallback values
        mongoPort = os.getenv('MONGO_PORT', '27017')

        # program mandates a collection 'animals' in db 'aac'
        DB = 'aac'
        COL = 'animals'

        # auth string excludes username & password if either are still null
        mongoAuth = '' if None in (mongoUsername, mongoPassword) else (mongoUsername + ':' + mongoPassword + '@')
        self.client = MongoClient(f"mongodb://{mongoAuth}{mongoHost}:{mongoPort}")
        self.database = self.client['%s' % DB]
        self.collection = self.database['%s' % COL]

        # basic find query to verify proper connection
        self.collection.find_one({})


    def read(self, query, projection=None):
        """ Find matching records in the collection """
        # optional projection parameter for the MongoDB query

        # raise TypeError if the query or projection are of the wrong type
        if type(query) != dict:
            raise TypeError("Query must be a dict")
        if projection is not None and type(projection) != dict:
            raise TypeError("Projection must be a dict")

        # no further error handling here: exceptions handled at route layer for conversion to HTTP responses
        return list(self.collection.find(query, projection))  


    def create(self, query):
        """ Add a record to the collection """
        
        if type(query) != dict:
            raise TypeError("Query must be a dict")

        self.collection.insert_one(query)
        return True


    def update(self, query, new_values):
        """ Update a single matching record from the collection, and return the updated record """
        # reworked to update and return a single record to fit the new use case for the previously unused update functionality
        
        if type(query) != dict:
            raise TypeError("Query must be a dict")
        if type(new_values) != dict:
            # could also check for presence of operator, but MongoDB will check this and raise OperationFailure
            raise TypeError("New values must be a dict")
        
        return self.collection.find_one_and_update(query, new_values, return_document=ReturnDocument.AFTER)


    def delete(self, query):
        """ Delete a matching record(s) from the collection """
        
        if type(query) != dict:
            raise TypeError("Query must be a dict")
        
        return self.collection.delete_many(query).deleted_count  
