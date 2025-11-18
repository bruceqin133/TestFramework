package com.crypto.autotest.runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features={"src/test/resources/GetCandleStackTest.feature",
},
        glue ="com.crypto.autotest.steps",monochrome = true )
public class APITestRunner extends AbstractTestNGCucumberTests {


}
