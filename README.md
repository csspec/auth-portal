# Centralized Authorization Portal for PEC University of Technology

## AIM
The main aim of this project is *automation* and *digitalization* of day-to-day 
work of college, which are at present done manually. We aim to build a central 
authorization and database service which could be used later as open-end APIs 
for scaling and development of various other modules to be made under PEC 
Automation and Digitalization initiative.

This repository contains code for Authorization portal.

We plan to build a scalable project. This is the reason, we plan to make a central 
authorization system for our project. Central authorization could later help other 
applications like hostel automation, registration system etc. to authorize the user from 
services provided by our project.

Thus we will build secure RESTful web services which other applications can use.
[Documentation about APIs will be uploaded soon]

## TECHNOLOGY 
The server side of the project will be implemented in JAVA's
[Spring](https://projects.spring.io/spring-framework/) Platform. We will use various 
other frameworks like [LomBok](https://projectlombok.org/), which will help us to simplify the code and add flexibility.
The databases will be managed using [MongoDB](https://www.mongodb.com).

We are using gradle build system.
More information can read at Docs/

## STRUCTURE
Project provides following package structure:
```
	+ auth.api.*	--	Implementation of API
	+ auth.config.* -- 	Configuration of Spring and other modules
	+ auth.db.*	--	Implementation of Database
```

