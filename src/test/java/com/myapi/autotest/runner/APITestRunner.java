package com.myapi.autotest.runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features={"src/test/resources/",
},
        glue ="com.myapi.autotest.steps",monochrome = true )
public class APITestRunner extends AbstractTestNGCucumberTests {


}
