# ContactAssessment
This API runs using SQLite. I have included a create table script for the initial set up of the DB. Once the table is created it will need to be saved as C:/Contacts/contactsDB. This can be done using the SQLite window and the .save functionality. Also, a second table will need to be created for the "test" environment. This will be saved as C:/Contacts/testcontactsDB. The code will look for these accordingly whether it is running the test cases or the full application.

This code contains the Controller class which handles the five requests required, as well as a Service and DAO class that handle the functionality and accessing the database.
