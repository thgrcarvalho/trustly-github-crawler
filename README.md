# trustly-github-crawler

## Before Installation

* You'll need java jdk 11 and maven installed on your machine for compiling the project.
* You'll need docker and docker-compose for running the app inside a container.

## Installation

1. On the root project folder, compile the application with the following command: `mvn clean install`
1. Run the following docker-compose command to build the image and deploy a container with the app (from also the root project folder): `docker-compose -f docker/docker-compose up -d`

Obs: If you want to run the application without docker, after step 1, run the following command (from also the root project folder): `java -Djdk.tls.client.protocols=TLSv1.2 -jar target/GithubCrawler-0.0.1-SNAPSHOT.jar`

## Using the API:

After the application is installed and running, you can make HTTP GET request to the following endpoint: `http://SERVER_URL/8080/crawler/github-repository?url=GITHUB_REPO&branch=REPO_BRANCH`

The URL variable are as follows:
* SERVER_URL: The server where is app is hosted;
* GITHUB_REPO: The URL of the repository main page;
* REPO_BRANCH: The branch which will be used to retrieve the files.
