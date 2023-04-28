# Smart Mat Backend README

---

## Table of Contents
1. Prerequisites
2. Installation
3. Running the Application
4. Running the tests
5. Docker Commands

---

### 1. Prerequisites
Before starting, make sure that the following tools and software are installed on your local machine:


Docker --- [Download Docker](https://www.docker.com/products/docker-desktop/)

Maven --- [Download Maven](https://maven.apache.org/install.html)


---

### 2. Installation

Download the source code from the repository

---

### 3. Running the Application
1. To run the application, make sure you have docker installed and running on your local machine
2. Open a terminal and navigate to the root directory of the project
3. Run the following command to build and run the docker containers: `docker-compose up --build`

This should start the application, if using docker desktop, you can check the status of the containers by clicking on the docker icon in the taskbar and selecting the containers tab.

---

### 4. Running the tests
To run the tests for the application, make sure the `test-db` image is running in your docker container.
You will also need to have maven installed on your local machine and added to your path.

Open a terminal and navigate to the root directory of the project, from here you can type the command: `mvn test -Plocal-tests`

---

### 5. Docker Commands
Here is a list of commands you can use in docker to manage the containers. These commands should be run from the root directory of the project.



#### Build and run Docker containers (On first time running and after changes)
> docker-compose up --build

#### Run Docker container (After first time running)
>docker-compose up

#### Stop and remove Docker container
>docker-compose down

#### Stop Docker container
>docker-compose stop

---



