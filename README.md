API tests for the Stellar Burgers app.

Used technologies: Java 11.0.26, Maven 3.9.9, JUnit 4.13.2, REST Assured 5.5.5, Allure 2.29.1, Datafaker 1.8.0, Gson 2.13.1.

Run tests:
````
mvn clean test 
````

Generate Allure report:
````
allure generate target/allure-results --clean -o target/allure-report
````