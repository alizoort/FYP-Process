# Description

This project is the FYP Process modeled using BPMN Camunda. The idea is that a user (student or professor) needs to register to the FYP Process. If the user logs in as a student, he can add a suggestion of a project. The project will be saved in a database. If he is a professor, he will study the suggested project and either accept the project or reject it.

# Installation Manual

Use this guide to install this project

## Required Frameworks and Applications
These are the needed frameworks and applications needed to install this project
- Camunda Modeler
- Camunda bpm run
- postgresql
- java
- maven

Make sure you have the corresponding versions

## Configuration
the Camunda configurations are in the application.yaml file in the src/main/ressources
camunda.bpm.admin-user:
id: demo
password: demo

## Installation

First, clone the project to your laptop

```bash
git clone https://github.com/alizoort/FYP-Process.git
```

Then, you need to install all the dependencies

```bash
mvn clean install
```

# User Manual
After the installation, to start the process here are the steps to take:
- build the project
- run the camunda run server and the database
- enter the url:http://localhost:8080/camunda-welcome/index.html

The BPMN is now running. You can start the process and monitor the flow.



# References
[Camunda Documentation](https://docs.camunda.org/manual/7.16/)