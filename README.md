# loans
Loan Projections

How to run.

1. Clone the application
2. Ensure all POM dependancies are resolved.
3. Application uses default port 8080.

Code Coverage
1. PLugin used is Jacoco
2. I have included the coverage report, but you can generate another one.
3. Current coverage is 91%.


Code analysis
1. This code has been anylyzed by Sonarlint a SonarQube library and passed.

Api Endpoints
   1. Fee Projection
   Endpoint: http://localhost:8080/api/fee/projection
   Method: POST
   Payload: {
           "duration": 13,
           "startDate":"01/06/2023",
            "principalAmount":3000,
           "frequency":"m"
           }

   2. Installment Projection
   Endpoint: localhost:8080/api/installment/projection
   Method: POST
   Payload: {
             "duration": 3,
             "startDate":"01/06/2023",
             "principalAmount":3000,
             "frequency":"m"
             }
   
