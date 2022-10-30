# SpringBoot-database
SpringBoot application with PostgreSQL database set up on docker compose.

# Starting the application

**Prerequisite**

Docker must be installed and running to start application. 
Application wont start locally without database connection.

## Start

First make new clean build

```
make clean
```
Copy new jar file to root direcory for dockerfile access

```
cp target/demo-0.0.1-SNAPSHOT.jar ./
```

At project root folder run 
```
make start
```
Or
```
docker compose start
```

Other commands and shortcuts available at "Makefile"

**Application**

Open **localhost:8080** on browser

* Get values - Returns all values from database
* Post values - Inserts all values from "values.json" to database. 
NB - every new insertion rewrites old values.
* Delete values - Deletes all values from database

## Database

To view/use database open **localhost:5050** on browser

**Login:**

* Username: admin@admin.com
* Password: root

**Open database**

"Add New Server"

Fill in neccessary fields to make connection

* Name: db
* Host: db
* Username: postgres
* Password: postgres

From servers menu (left side)

Name/Databases/db/Schemas/public/Tables/value

Right click and "View/Edit Data"