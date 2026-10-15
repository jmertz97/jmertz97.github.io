# Project 2 CRUD module
# Joshua Mertz

from pymongo import MongoClient 
from bson.objectid import ObjectId 

class AnimalShelter(object): 
    """ CRUD operations for Animal collection in MongoDB """ 

    def __init__(self, username, password): 
        # Initializing the MongoClient. This helps to access the MongoDB 
        # databases and collections. This is hard-wired to use the aac 
        # database, the animals collection, and the aac user.
        # 
        # Connection Variables 
        # 
        USER = username
        PASS = password
        HOST = 'localhost'
        PORT = 27017
        DB = 'aac' 
        COL = 'animals' 
        # 
        # Initialize Connection 
        # 
        self.client = MongoClient('mongodb://%s:%s@%s:%d' % (USER,PASS,HOST,PORT)) 
        self.database = self.client['%s' % (DB)] 
        self.collection = self.database['%s' % (COL)] 

    def create(self, data):
        """ Add a record to the collection """
        if type(data) != dict:
            # return immediately if data is the wrong type
            return False
        # otherwise proceed with the create operation
        try:
            self.collection.insert_one(data)  # data should be a dictionary
            return True
        except Exception as e:
            return False

    def read(self, data):
        """ Find matching records in the collection """
        if type(data) != dict:
            # return immediately if data is the wrong type
            return []
        # otherwise proceed with the read operation
        try:
            return list(self.collection.find(data))  # convert to list before returning
        except Exception as e:
            return []

    def update(self, query, new_values):
        """ Update records in the collection which match the query with new values """
        if type(query) != dict or type(new_values) != dict:
            # return immediately if data is the wrong type
            return 0
        # otherwise proceed with the update operation
        try:
            # update_many returns an UpdateResult object, whose modified_count property reports how many records were affected
            return self.collection.update_many(query, new_values).modified_count
        except Exception as e:
            return 0

    def delete(self, data):
        """ Delete a matching record(s) from the collection """
        if type(data) != dict:
            # return immediately if data is the wrong type
            return 0
        # otherwise proceed with the delete operation
        try:
            # delete_many returns a DeletedResult object, whose deleted_count property reports how many records were affected
            return self.collection.delete_many(data).deleted_count
        except Exception as e:
            return 0
