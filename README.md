# **Use Description**:
**How to use** 

1. Environment switch and run test\
There are 2 different environments, prod and uat. 
Test should run through maven command: 
- mvn test -Pprod (for prod env)
- mvn test -Puat (for uat)

2. Test Data for request
This framework support 2 kinds of APIs (REST and WebSocket).
User can set some informations below:
- requestType: http, restAssured, webSocket (for REST APIs, user can use both "http" and "restAssured")
- url: This is the request url (support for both REST and webSocket urls)
- method: This is only for REST such as "GET", "POST".If the requestType is "restAssured", it could also be "PUT","PATCH","DELETE",etc.
- body: This is the request body which support both webSocket requests and http post requests.
- timeForWait: This is only for webSocket request. User need to wait for response and maybe he will get the response with empty data if the time is too short. 

Examples:
- REST API properties:\
   requestType=http \
   url=https://${httpPath}/exchange/v1/public/get-candlestick?instrument_name=BTCUSD-PERP&timeframe=M5
   method=GET \
   body=none 


- WebSock API Proprties:\
   requestType=webSocket \
   url=wss://${websocketPath}/exchange/v1/market \
   body={  "id": 1,  "method": "subscribe",  "params": {    "channels": ["book.BTCUSD-PERP.10"]  }} \
   timeForWait=30 

  - Select classes what you want to run (BDD or TDD):
  please have a look at testng.xml
   


**Framework Structure** \
All code and functions are stored in /src/main 
- baseObjects: basic objects
- common: different request senders and listener for websocket and all senders extends the parent class RequestSender and implement the common functions in interfaces
- core: Factories such as factory which manufactures the senders and make them run
- interfaces: abstraction for different kinds of objects
- utils: file operation
- steps: steps only for cucumber sections
- main/resource/jsonPath: All jsonpath values which are used to get values from response data are stored in this folder. One file for one API
- main/resource/testCaseData: All test data are stored in this folder. One scenario one file.
- Testcases are defined under src/test path
- You can get the reports under /target/sunfire-reports
- pom.xml: defines the dependencies, env settings, plugins, properties settings.
- testng.xml: The range of test cases which the user want to run.
- test/resources: .features example file for BDD mode cucumber
- RestAssuredTest and WebSocketTest under /src/test are test files only for testng TDD mode.
- src/test/runner: A runner class which is integrated by both testng and cucumber
**Advantages:**
1. Support different kinds of APIs: \
This framework supports both request for REST and websocket APIs by Factory Mode in Java. Even sometimes user can implement inter-cooperation between these 2 kinds of APIs in some scenarios.
2. Isolation between test data and code: \
If the data structure in response has been updated, user just need to update the jsonpath in .properties file instead of in the code and similar for test data.
3. Data driven: \
Currently in test case, user can implement different scenarios by a single test case through data provider.
4. Flexible for environment switch: \
Different environment settings are set in pom.xml and can be parameterized in .properties file. User just need to set the value in running command.
5. Integrated cucumber and testng so user can run test by both TDD and BDD mode
6. The next step can use parameters returned from the last step (see GetBook.feature).

**What needs to be optimized?** \
Because of time, something still need to be extended and optimized.
- logger instead of system.out.print
- test report
- The structure for testing data still need to be optimized. The websocket request and http request could be separated.
- User can only get and return the last webSocket API during the time expiration.

_(Note: Please reset or close proxy settings in pom.xml if needed.)_
