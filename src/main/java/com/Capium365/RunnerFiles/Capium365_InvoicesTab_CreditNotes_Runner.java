package com.Capium365.RunnerFiles;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
	    features = "src\\test\\resources\\Capium365FeatureFiles\\Capium365_InvoicesTab_CreditNotes.feature",
	    glue = "com.Capium365.StepDefination",
	    tags = "@CreditNotes_Invoice", 
	    plugin = {
	        "pretty",
	        "html:target/cucumber-reports/CucumberReport.html",
	        "json:target/cucumber-reports/CucumberReport.json",
	        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
	    },
	    monochrome = true
	)
public class Capium365_InvoicesTab_CreditNotes_Runner extends AbstractTestNGCucumberTests{

}
