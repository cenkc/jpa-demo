# jpa-demo application readme
spring boot with jpa demo using h2 database

# running the application thru IntelliJ IDEA with Maven
run "mvn spring-boot:run" command inside of a IntelliJ's built-in terminal

# running the application thru IntelliJ IDEA with Maven Wrapper
run "mvnw spring-boot:run" command inside of a IntelliJ's built-in terminal

# h2 database ui
http://localhost:8181/h2-console
(use default jdbc url, which is = "jdbc:h2:mem:testdb")

# Swagger 
http://localhost:8181/v2/api-docs

http://localhost:8181/swagger-ui.html

NOTE : I'm not going to provide Postman exports or curl commands. One can invoke account operation end-points via 'swagger-ui', 
