# Spring Boot Microservice Best Practices - Service
This project provide guidelines for development team to follow while developing microservices in spring boot. Basically developed code should be general and bug free and deployable to any environment without much huddles.
##
### Prerequisites
- JDK 1.8
- Maven
- Mysql
- AWS Access or LocalStack

## Quick Start

### Clone source
```
git clone https://github.com/vinodvpillai/springboot-microservice-best-practices.git
cd springboot-microservice-best-practices
```

```
MySQL START
```

```
docker-compose -f localstack-compose.yml up
```

### Build
```
mvn clean package
```

### Run
```
java -javaagent:/home/agent/skywalking-agent.jar  -jar  /home/agent/springboot-microservice-best-practices.jar
```

### Endpoint details:

##### 1. CustomerController - Add new customer (CURL Request):

```
curl -X POST \
  http://localhost:8082/v1/customers \
  -H 'Accept-Language: en' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: e6238e62-efc5-4042-8352-2990b05a7bec' \
  -H 'cache-control: no-cache' \
  -d '{
	"name": "Vinod One",
	"emailId": "vinod1@yopmail.com",
	"address": "Gujarat"
}'
```
##### 2. CustomerController - Get customer (CURL Request):

```
curl -X GET \
  http://localhost:8082/v1/customers/vinod1@yopmail.com \
  -H 'Postman-Token: 354da13a-4231-4272-a12d-b4531314ef21' \
  -H 'cache-control: no-cache'
```

##### 3. CustomerController - Update customer (CURL Request):
```
curl -X PUT \
  http://localhost:8082/v1/customers/vinod1@yopmail.com \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 5a722939-e40c-4b35-96f2-d0dad428e3a4' \
  -H 'cache-control: no-cache' \
  -d '{
	"name": "Vinod Pillai",
	"address": "Gujarat"
}'
```

##### 4. CustomerController - Delete customer (CURL Request):
```
curl -X DELETE \
  http://localhost:8082/v1/customers/vinod1@yopmail.com \
  -H 'Postman-Token: e482297c-eea9-4e0a-ae80-96302ff84efd' \
  -H 'cache-control: no-cache'
```

