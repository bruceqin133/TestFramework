Feature: Test for API get book api

  Scenario: happy flow for get book api
    Given user get input parameters
    When user send socket request and get response by the file "happyFlowSockParameterized.properties"
