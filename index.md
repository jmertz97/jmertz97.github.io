---
title: Professional Self-Assessment
nav_order: 1
---

# **Introduction**

Hello! My name is Joshua Mertz. I began my Computer Science education in 2018 at University of North Carolina Wilmington (UNCW), and continued it from 2025 onwards at Southern New Hampshire University (SNHU). I am on track to complete my Bachelor's in Computer Science by the end of 2026.
This GitHub Pages site hosts and presents my ePortfolio, initially drafted for the CS-499 Capstone course.

- [My GitHub](https://github.com/jmertz97)
- Contact (email): jmertz97@gmail.com

# **Professional Self-Assessment**

In late 2017, I was informally introduced to programming when I took my friend up on his offer for me to join his modding project for the video game Hearts of Iron IV. I helped him by developing rudimentary scripts and sharing them with him on an unfamiliar website called GitHub. Though I only spent a few months as an active contributor, it grabbed my interest strongly enough for me to consider pursuing a career in Computer Science. Roughly a year after first picking up the hobby, I began my formal education in Computer Science by writing hello_world.py on my first day of class at UNCW.

At UNCW, I learned the basic foundations of programming, familiarizing myself with common data structures and algorithms, and later learning how to evaluate them for complexity and efficiency. I also gained familiarity with security, studying and applying skills such as implementing default-deny policies and sanitizing user-provided input.

The most noteworthy course I took was Software Engineering at UNCW, in which I was assigned to a team with four other classmates; we had to brainstorm the semester project by the end of that same session of class. We chose to build a full-stack Flask web application, designed to be a social media site for collectors. The project resided in a GitHub repository with a formal pull request process and was hosted through Heroku. My contribution consisted of developing two front-end modules, which had to correctly interface with other team members’ modules.

Our team followed an Agile-inspired development framework. A fellow team member took leadership of the project and oversaw other developers’ pull requests; clear milestones were set for each two-week-long sprint; and in-person Scrum meetings were held regularly. The professor acted as the stakeholder, providing feedback on our concept, proposing plausible customer requirements for the project to meet, and evaluating a live, in-person presentation and demonstration of the final deliverable.

At the start of 2025, I transferred to SNHU, where I developed skills that I hadn’t yet covered at UNCW. I learned to design, build, and query relational (SQL) databases in Intro to Structured Database Environments; I later learned to work with NoSQL (MongoDB) in Client/Server Development, from which I drew one of my artifacts. The latter course also introduced me to client-server architecture, and was followed by Full-Stack Development, where I expanded upon that skill by building Travlr Getaways, a MEAN-stack web application with a public-facing website and an SPA admin portal.

I also continued to reinforce and expand upon the skills I had learned and applied at UNCW. In Software Development Lifecycle, I worked through a simulated Agile project where I individually rotated through the Agile roles of Product Owner, Scrum Master, developer, and tester throughout the six sprints. Overall, while I gained in-person experience with software development as a team in the UNCW program, I stepped into a wider range of roles via SNHU’s simulated, structured project environments.

Having reached the conclusion of my studies at SNHU, I have a far better understanding of my career goals. I have resolved to pursue a career in web application development, as a back-end or full-stack developer. Whether my professional work will be concentrated on one end of the stack, or span it end-to-end, I hope to develop my skills in whatever direction is needed to support my peers and to establish myself as a reliable team member, just as I sought to do when I first helped my friend with his modding project long ago.

----

This ePortfolio is the culmination of the skills I have built up throughout my studies at both institutions. Both artifacts presented here are iterations of the final project delivered for their respective courses. As they were each built only to meet assignment requirements, the two artifacts were ripe for improvement; thus, they were selected for enhancement to jointly demonstrate proficiency in three areas: Software Design & Engineering, Algorithms & Data Structures, and Databases. I first documented these artifacts in detail in my code review video, where I walked through their functionality and flaws and communicated my early enhancement plans.

The first artifact is the Austin Animal Center (AAC) dashboard, from Client/Server Development (CS340). It initially consisted of a dashboard implemented in a Jupyter notebook using Dash, and a CRUD module implemented as a Python class named AnimalShelter. The artifact was originally intended to run only within a course-specific virtual environment, which also contained the database of records to be viewed through the dashboard. 

To enhance the artifact with respect to Software Design & Engineering, I rebuilt the AAC dashboard as a full-stack Flask application. In the process, I eliminated all dependencies on Dash, replacing its reactive dashboard components with a series of JavaScript functions. I also reworked the CRUD module to raise errors for handling by the new Flask route file, instead of swallowing them as in the original iteration. Additionally, the original program stored hardcoded connection and authentication credentials for the database; I refactored the program to obtain these variables from a local .env file to remove the clear security risk.

I then enhanced the same artifact with respect to Databases. The original version lacked a validation schema, allowing faulty data such as negative age values to surface in the database records, so I designed one for the enhancement to constrain certain fields to valid data. Because the database lives on the user's MongoDB server rather than in the project files, I chose to add a setup script to build the database with the intended schema validation and populate it with data from the included CSV file. I also authored a README file to instruct the user on when and how to run the script. As the original artifact only ever invoked the `.read()` method of the CRUD module, I expanded the dashboard functionality to allow users to alter a selected record's values in two fields - outcome_type and outcome_subtype - thereby demonstrating the functionality of the CRUD module's `.update()` method. Since the constraints on outcome_subtype are defined by the value of outcome_type, I chose to derive both the UI constraints on user input and the validation schema from a common source. 

The second artifact is the Rescue Animal Intake program, from Foundation in Application Development (IT145). It consists of a console program built in Java using four class files. The program allows users to provide input into a main menu, where they may choose to intake a new animal, reserve an animal by type and country of service, or print a list of animals stored by the program. The program's animal data was previously stored in one ArrayList each for Dog and Monkey objects, linearly traversed by the intake method to find duplicate animals (by matching name) before allowing a new animal to be added, and by the reserve method to locate an animal to reserve by its reserved status and its country of service.

I enhanced this artifact with respect to Algorithms & Data Structures by reworking the ArrayList into a pair of HashMaps for each animal type. One stores the animal objects directly and is optimized for the intake method, while the other stores lists of animals grouped by country of service and is optimized for the reserve method – with the intake method storing the same object in both collections. By using HashMaps of animals in place of the original animal lists, I reduced the lookup complexity of the intake method's duplicate check from O(n) to O(1) on average; I also used this opportunity to key the map on a concatenation of the name and intake date, so that a matching name alone wouldn't cause a distinct new animal to be rejected. For the reserve method, I designed the nested collection of animals by country to narrow its traversal from every animal of that type to just a list of animals of that type in the desired country; the outer collection is also a HashMap, allowing the search to immediately jump to the intended country.

The Rescue Animal Intake program differs from the AAC dashboard in that its data is contained within the program during runtime, rather than relying on an external database. This, as well as the program being designed around functions performing searches for individual matches in a collection, contributed to my choice to use this artifact to demonstrate my own ability to compare the benefits and drawbacks of different optimization approaches, and to implement the selected approach. This is intended to complement my enhancement work for the AAC dashboard, where I demonstrate my ability to rebuild a streamlined program as a full-stack application with proper client-server separation, and to improve upon its database integration with more robust validation.

---

### Links
- [Enhancement One - Software Design & Engineering]({% link enhancement1.md %}): AAC dashboard, part 1 (rebuilding as a Flask app)
- [Enhancement Two - Algorithms & Data Structures]({% link enhancement2.md %}): Rescue Animal Intake program
- [Enhancement Three - Databases]({% link enhancement3.md %}): AAC dashboard, part 2 (implementing schema validation, update functionality)
- [Code Review Video]({% link codereview.md %})
- [View repository on GitHub](https://github.com/jmertz97/jmertz97.github.io)
