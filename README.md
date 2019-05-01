# cca-group-project

### To Build the Container
    docker build -t ccaproject .

### To Run the Container
This will run the container, forwarding port 8080 on the docker machine to port 8080 on your host machine. 

    docker run -p 8080:8080 -it ccaproject bin/bash

### To Run the Backend
To test the backend, you can hit endpoints on localhost:8080

    start-hbase.sh
    cd backend
    mvn spring-boot:run
