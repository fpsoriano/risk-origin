# Instructions

### Requirements

* JDK 11
* Maven
* Docker
* Docker-compose


http://localhost:8082/risk/swagger.html

### Run Application + tests
#### `java`

```shell
cd risk
mvn clean install
java -jar ./target/risk-0.0.1-SNAPSHOT.jar
open http://localhost:8082/risk/swagger.html
```

*Note: I recommend open via incognito tab, if the swagger does not load

#### `docker`

```shell
cd risk
mvn clean install
docker build --tag risk-docker -f Dockerfile .
docker-compose up -d
open http://localhost:8082/risk/swagger.html
```

*Note: I recommend open via incognito tab, if the swagger does not load


# technical decisions
- I have broken the project in different packages according to their responsibilities
- In order to make the service class clean, I created a helper to assistant it
- I have created a validation method for each business requirement, so I could use when some insurance had the same rule
- I have created classes to input/output (DTO)  and class to be handled via domain (in this case, service layer) (model)
- For API documentation, I am using Swagger
- Exception handler
- Communication between layers via interface
