package com.Capium365.StepDefination;


import com.Capium.Utilites.HelperClass;
import com.Capium.Utilites.Log;
import com.Capium.Utilites.StepTracker;
import com.Capium365.Actions.LoginActions;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.service.ExtentService;
import io.cucumber.java.*;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Hooks {

	private static ExtentReports extent = ExtentService.getInstance();
	private static ExtentTest scenarioTest;
	//private static ThreadLocal<ExtentTest> scenarioTest1 = new ThreadLocal<>();

	@BeforeAll
	public static void setup() {
		HelperClass.setUpDriver();
		Log.info("Driver setup successfully");
		System.out.println("Starting the Test Execution...");

	}

	@Before
	public void beforeScenario(Scenario scenario) {
	//HelperClass.setUpDriver();
		scenarioTest = extent.createTest("Scenario: " + scenario.getName());
		System.out.println("Starting Scenario: " + scenario.getName());
		Log.info("Scenario:" + scenario.getName());
	}

	@AfterStep
	public void afterStep(Scenario scenario) {
	    WebDriver driver = HelperClass.getDriver();
	    String stepName = StepTracker.getCurrentStep();

	    if (stepName == null || stepName.isEmpty()) {
	        stepName = "Unnamed Step";
	    }

	    try {
	        String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

	        if (scenario.isFailed()) {
	            scenario.attach(Base64.getDecoder().decode(base64Screenshot), "image/png", stepName);
	            scenarioTest.log(Status.FAIL, stepName,
	                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
	            Log.info("Captured screenshot for failed step: " + stepName);
	        } else {
//	            scenarioTest.log(Status.PASS, stepName,
//	                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
	        	scenarioTest.log(Status.PASS, stepName);
	            Log.info("Captured screenshot for passed step: " + stepName);
	        }

	    } catch (Exception e) {
	        Log.error("Failed to capture screenshot for step: " + stepName + " | Error: " + e.getMessage());
	    }
	}

	@After
	public void afterScenario(Scenario scenario) {
		WebDriver driver = HelperClass.getDriver();

		
		try {
			if (scenario.isFailed()) {
				scenarioTest.log(Status.FAIL, "Scenario Failed.");
				Log.info("Scenario Failed.");
			} else {
				scenarioTest.log(Status.PASS, "Scenario Passed.");
				Log.info("Scenario Passed.");
			}
			LoginActions loginPage = PageFactory.initElements(driver, LoginActions.class);
			loginPage.Logout();
		Log.info("Successfully logged out and redirected to login page.");
		} catch (Exception e) {
			Log.error("Logout failed: ");
			System.out.println("Logout failed: " + e.getMessage());
		}
		
		
		try {
			HelperClass.tearDown(); 
			Log.info("Browser closed.");
		} catch (Exception e) {
			Log.error("Error during browser teardown: ");
		}

		try {
			extent.flush(); // flush report
			System.out.println("Extent report flushed.");
		} catch (Exception e) {
			System.out.println("Error flushing extent report: " + e.getMessage());
		}
	}

	@AfterAll
	public static void tearDown() {
		System.out.println("Ending Test Execution...");
//		try {
//			HelperClass.tearDown(); 
//			Log.info("Browser closed.");
//		} catch (Exception e) {
//			Log.error("Error during browser teardown: ");
//		}
//
//		try {
//			extent.flush(); // flush report
//			System.out.println("Extent report flushed.");
//		} catch (Exception e) {
//			System.out.println("Error flushing extent report: " + e.getMessage());
//		}
	}


	public static ExtentTest getScenarioTest() {
		return scenarioTest;
	}

	public static void captureScreenshotBase64(WebDriver driver, ExtentTest scenarioTest, String message) {
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			String base64Screenshot = ts.getScreenshotAs(OutputType.BASE64);
			scenarioTest.log(Status.INFO, "Screenshot: " + message,
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
			Log.info("Successfully captured screenshot: " + message);
		} catch (Exception e) {
			Log.error("Failed to capture screenshot: " + message + " | Error: " + e.getMessage());
		}
	}
	
	 public static void captureScreenshot(String screenshotName, Scenario scenario, ExtentTest scenarioTest) {
	        try {
	            WebDriver driver = HelperClass.getDriver();
	            TakesScreenshot ts = (TakesScreenshot) driver;

	            byte[] screenshotBytes = ts.getScreenshotAs(OutputType.BYTES);
	            scenario.attach(screenshotBytes, "image/png", screenshotName);

	            String base64Screenshot = ts.getScreenshotAs(OutputType.BASE64);
	            scenarioTest.log(Status.INFO, "Screenshot: " + screenshotName,
	                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());

	            File screenshotFile = ts.getScreenshotAs(OutputType.FILE);
	            String filePath = System.getProperty("user.dir") + "/screenshots/" + screenshotName + "_" + System.currentTimeMillis() + ".png";
	            FileUtils.copyFile(screenshotFile, new File(filePath));

	            Log.info("Saved screenshot: " + filePath);

	        } catch (Exception e) {
	            Log.error("Failed to capture screenshot: " + e.getMessage());
	            System.out.println("Screenshot error: " + e.getMessage());
	        }
	    }

	private static Map<String, Object> scenarioContext = new HashMap<>();

	public static void setScenarioContext(String key, Object value) {
		scenarioContext.put(key, value);
	}

	public static Object getScenarioContext(String key) {
		return scenarioContext.get(key);
	}

	public static void clearScenarioContext() {
		scenarioContext.clear();
	}

}
