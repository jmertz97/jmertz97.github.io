---
title: Enhancement 3 - Databases
nav_order: 4
---

### _Briefly describe the artifact. What is it? When was it created?_

This artifact is a dashboard that allows the user to view information about animals in the Austin Animal Center’s database. The dashboard was created as a Python notebook (using JupyterDash) and was designed to interface with a MongoDB database via a CRUD module implemented in Python, with the database itself generated from the `aac_shelter_outcomes.csv` file. The artifact was created during March and April 2026 for the CS340 course. This version of the artifact is a further developed iteration of the one turned in for the Category 1 milestone.

### _Justify the inclusion of the artifact in your ePortfolio…_

For the third enhancement category (Databases), I chose to iterate on an existing program for which I had already established the basic interface. The same CS340 artifact I selected for Category 1 had a basic CRUD module built, but without database schema and with only the `.read()` method used by the dashboard.
Most of the work for this enhancement went towards building `import_db_data.py`, a script which codifies the construction of the database with the appropriate `$jsonSchema` architecture. The script is only intended to be run once to automate the configuration of the database but ensures that the program runs on a correctly structured DB with schema validation. Since the DB resides on the user’s machine, and previous iterations of the dashboard have relied on the user pre-initializing their MongoDB instance, I decided that a script to create the DB was a necessary component for ensuring the DB would have a uniform structure and schema across different machines.

Additionally, since the original artifact and its Category 1 milestone iteration each only made use of AnimalShelter’s `.read()` method, I chose to add new functionality to the dashboard which would make use of the `.update()` method and conduct update operations on the DB. I implemented this in the form of a streamlined “mini-menu”, which allows the user to alter the outcome type and subtype values for individual selected records. The user input is constrained to dropdown menu options, and the feature is built off the existing row selection architecture used to update the map. With the continued additions to `dashboard_table.js` making the file quite large, I chose to refactor it into four separate JavaScript files – `table.js`, `update_minimenu.js`, `piechart.js`, and `map.js`.

In the data, there is only a limited set of outcome types, and each outcome type has only a limited set of outcome subtypes. This data was used to determine the acceptable values for those fields in the database, as well as which values to display in the dropdowns in the mini-menu; thus, I made sure both the mini-menu and import scripts derive their data from the same source, a new file (`valid_db_vals.py`) in the data folder.

Finally, with the introduction of a separate script to handle importing the database, I chose to add a README file to provide instructions for running the script. While the README would have already been ideal to include in the artifact, the addition of an import script which runs independently of the main program is what made it fully necessary to include, since the user must be informed that the script is intended to be run on its own during setup.

### _Did you meet the course outcomes you planned to meet…_

I believe I have met the fifth course outcome, “Develop a security mindset that anticipates…”, through my work to build the database schema. The constraints on user input built into the design of the mini-menu, combined with the DB’s own enforcement of its schema, provide multiple layers of defense against bad or malicious input that can be provided through the `.update()` method which is now in use. This aspect of defensive programming builds upon the improvements to error handling which I began in my earlier pass of the artifact for the Category 1 milestone.

Additionally, I believe I have met the second course outcome (“Design, develop, and deliver professional-quality communications…”) in my continued work on this artifact, in the form of the new README file, which concisely provides the user with instructions for setting up the artifact, not just the newly added scripts. I did not originally plan to cover this outcome in this way here, but the necessity to provide a “professional-quality written communication” emerged as I brainstormed the implementation of schema typing in the DB itself.

### _Reflect on the process of enhancing and modifying the artifact._

The code review process emphasized the importance of using specific and consistent data over one-off magic-number constants in my code, which translated to me being diligent about ensuring certain variables in my Category 2 artifact work derived from a common source. I found myself applying this principle at a much larger scale with the use of a nested dictionary to store the outcome types and subtypes – while I could have hard-coded the accepted values into the schema definition in the import script, I realized it would be more consistent to have it retrieve the values from the same source as where I had already planned to retrieve the dropdown menu options from. This change helped to ensure that these two separate layers of input constraint and validation would remain consistent.

One challenge I faced was learning the syntax for building the schema validation rules. While I had covered PyMongo queries and schema previously, the validation for the outcome_subtype column proved to be much more complicated than for the other columns, given that the acceptable string values for cells in that column were dependent on which value was present in the outcome_type column. I had to delve deeper into the MongoDB `$jsonSchema` documentation to understand how to format the schema using `anyOf`, before I could implement the `build_valid_outcomes()` function, which builds the `anyOf` branch from `VALID_OUTCOMES` in `valid_db_vals.py`.

One other challenge I faced was verifying that the README file’s instructions were accurate. I expected this to be straightforward, but writing down the instructions and testing them in a standalone terminal helped me uncover where I had made assumptions based on my own setup, such as the project’s `.venv` being preconfigured to allow me to run the import script from within PyCharm, but not being similarly configured for VS Code or a basic Windows terminal.
