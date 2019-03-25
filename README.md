# cca-group-project

###To Build the Container
    docker build -t ccaproject

###To Run the Container
    docker run -p 8080:8080 -it ccaproject bin/bash

###To Run the Backend
    cd backend
    mvn spring-boot:run