# Elasticsearch Jest Client Demo

This is a demo project to learn and understand the use of Jest Java library for communicating with Elasticsearch. 

We will be using Spring Boot AutoConfiguration for Jest.

See and run the test cases to understand more about the usage.

Another goal of this project is to add as many usages of Jest Client for Elasticsearch and use the project to test compatibility with any Elasticsearch core version.

## Usage and setup (Local machine)

Clone simple-starter-build-tools: <br>
git clone https://github.com/heartin/simple-starter-build-tools.git <br>
Run './mvnw clean install' or 'mvn clean install'

Clone simple-starter-parent-java: <br>
git clone https://github.com/heartin/simple-starter-parent-java.git <br>
Run './mvnw clean install' or 'mvn clean install'

### Configuring and running elasticsearch-jest-client-demo

Clone this repo (elasticsearch-jest-client-demo): <br>
git clone https://github.com/heartin/elasticsearch-jest-client-demo.git <br>
Run './mvnw clean compile' or 'mvn clean compile'

Configure following properties in your application.properties file as required by JestAutoConfiguration in Spring: <br>
spring.elasticsearch.jest.username <br>
spring.elasticsearch.jest.password <br>
spring.elasticsearch.jest.uris <br>

Notes: 
1. username is your username, password is your password and uris should refer to your endpoint. 
1. If you are using Amazon Web Services (AWS) as given in the links at references section, you might not be requiring an username / password. 
1. Please refer to additional references section below for more details on how to setup an account on the Elastic Cloud or Amazon Web Services (AWS).
1. Having passwords in application.properties may result in accidental checkin of passwords. So you may also use any of the other Spring Boot externalized configuration options instead of application.properties. 

Run './mvnw clean install' or 'mvn clean install'


## Additional References (from  my blog)
1. https://cloudmaterials.com/en/book/elasticsearch-essentials
1. https://cloudmaterials.com/en/book/elasticsearch-java-clients-jest-low-level-high-level
1. https://cloudmaterials.com/en/book/amazon-elasticsearch-service-essentials
1. https://javajee.com/externalized-configuration-with-spring-boot