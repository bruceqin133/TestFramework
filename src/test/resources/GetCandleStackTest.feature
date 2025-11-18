Feature: Test for API get candle-stack api

  Scenario: happy flow for get candle-stack api
    Given user send request and get response by the file "happyFlowHttp.properties"
    When user get jsonpath file by "get-candlestick.properties"
    Then User verify status code is 200
    And user verify method is "method"
    And user verify parameters is correct in the response body
    And user verify interval among each item in response is correct


  Scenario Outline: happy flow for different interval inputs
    Given user send request and get response by the file "<request_file>"
    When user get jsonpath file by "<jsonpath_file>"
    And user verify method is "method"
    Then user verify parameters is correct in the response body
    And user verify interval among each item in response is correct
    Examples:
      | request_file                         | jsonpath_file              |
      | happyFlowHttpForDay.properties       | get-candlestick.properties |
      | happyFlowHttpForHour.properties      | get-candlestick.properties |
      | happyFlowHttpForDay2.properties      | get-candlestick.properties |
      | happyFlowHttpReverseParam.properties | get-candlestick.properties |


  Scenario Outline: unhappy flow for wrong input
    Given user send request and get response by the file "<request_file>"
    When user get jsonpath file by "get-candlestick.properties"
    Then User verify status code is <statusCode>
    And user verify error code "<errCode>"
    And user verify error message "<errMsg>"
    Examples:
      | request_file                       | statusCode | errCode | errMsg                          |
      | wrongInstrumentName.properties     | 400        | 40004   | Unsupported instrument exchange |
      | wrongInstrumentNullName.properties | 400        | 40004   | Invalid instrument_name         |
      | wrongIntervalName.properties       | 400        | 40003   | Invalid request                 |
      | wrongIntervalNameNull.properties   | 400        | 40003   | Invalid request                 |





