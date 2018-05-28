# Elasticsearch Jest Client Usages

This project provides essential usages of various Elasticsearch APIs using the Jest client. 
This project uses Spring Boot AutoConfiguration (JestAutoConfiguration) for configuring Jest.
Contributors are welcome to add more usages through pull requests. 

Primary objectives of this project are:
1. Learn and understand the use of Jest Java library for communicating with Elasticsearch. See and run the test cases to understand more about the usage.
1. Use the test cases within project to test compatibility with any Elasticsearch core version as soon as Elasticsearch is upgraded.


## Usage and setup (Local machine)

Clone simple-starter-build-tools: <br>
git clone https://github.com/heartin/simple-starter-build-tools.git <br>
Run './mvnw clean install' or 'mvn clean install'

Clone simple-starter-parent-java: <br>
git clone https://github.com/heartin/simple-starter-parent-java.git <br>
Run './mvnw clean install' or 'mvn clean install'

### Configuring and running elasticsearch-jest-client-usages

Clone this repo (elasticsearch-jest-client-usages): <br>
git clone https://github.com/heartin/elasticsearch-jest-client-usages.git <br>
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