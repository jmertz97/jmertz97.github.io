QUERIES_BY_CATEGORY = {
	'water': {
        'animal_type': "Dog",
        'breed': {'$in': ["Labrador Retriever Mix", "Chesapeake Bay Retriever", "Newfoundland"]},
        'sex_upon_outcome': "Intact Female",
        'age_upon_outcome_in_weeks': { "$gt": 26, "$lt": 156 }
    },
	'mountain': {
        'animal_type': "Dog",
        'breed': {'$in': ["German Shepherd", "Alaskan Malamute", "Old English Sheepdog", "Siberian Husky", "Rottweiler"]},
        'sex_upon_outcome': "Intact Male",
        'age_upon_outcome_in_weeks': { '$gt': 26, '$lt': 156 }
    },
	'disaster': {
        'animal_type': "Dog",
        'breed': {'$in': ["Doberman Pinscher", "German Shepherd", "Golden Retriever", "Bloodhound", "Rottweiler"]},
        'sex_upon_outcome': "Intact Male",
        'age_upon_outcome_in_weeks': { '$gt': 20, '$lt': 300 }
    }
}

# rescue categories with intentionally identical requirements to others already defined in QUERIES_BY_CATEGORY
ALIASED_CATEGORIES = {
	'wilderness': QUERIES_BY_CATEGORY['mountain'],
	'individual_tracking': QUERIES_BY_CATEGORY['disaster']
}

# merging aliased categories into main dict
QUERIES_BY_CATEGORY.update(ALIASED_CATEGORIES)
