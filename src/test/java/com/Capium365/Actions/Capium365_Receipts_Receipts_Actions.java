package com.Capium365.Actions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Capium.Utilites.HelperClass;
import com.Capium.Utilites.Log;
import com.Capium365.Locators.Capium365_Receipts_Receipts_Locators;

public class Capium365_Receipts_Receipts_Actions {

	Capium365_Receipts_Receipts_Locators receiptLocator = null;

	public Capium365_Receipts_Receipts_Actions() {
		this.receiptLocator = new Capium365_Receipts_Receipts_Locators();
		PageFactory.initElements(HelperClass.getDriver(), receiptLocator);
	}

	WebDriver driver = HelperClass.getDriver();
	WebDriverWait wait = HelperClass.getWait();
	
	String FilePath="D:\\Files\\Receipt 2.pdf";

	public void ClickOnReceipts() {
		By receiptsTab = By.xpath("//span[text()='Receipts']//parent::a");
		wait.until(ExpectedConditions.visibilityOfElementLocated(receiptsTab));
		WebElement element = HelperClass.waitUntilClickable(receiptsTab);
		HelperClass.safeClick(element);
	}

	public void clickSidebarExpandArrow() {
		try {
			By arrowButton = By.xpath(
					"//div[contains(@class,'toggle-menu')]/mat-icon[normalize-space(text())='keyboard_arrow_right']");
			WebElement element = HelperClass.waitUntilClickable(arrowButton);
			HelperClass.scrollToElement(element);
			((JavascriptExecutor) HelperClass.getDriver()).executeScript("arguments[0].click();", element);
			Log.info("Clicked sidebar expand arrow.");

		} catch (Exception e) {
			Log.error("Failed to click sidebar expand arrow: ");
			e.printStackTrace();
		}
	}

	public void ClickOnReceiptsInsideSidebar() {
		By ReceiptsInsideSidebar = By.xpath("//a[@mattooltip='Receipts']");
		HelperClass.isElementPresent(ReceiptsInsideSidebar);
		HelperClass.clickUsingJS(ReceiptsInsideSidebar);
		HelperClass.sleep1(3000);

	}

	// Purchase Invoices under Receipts

	public void ClickOn_PurchaseInvoices() {
		By PurchaseInvoice = By.xpath("//a[@mattooltip='Purchase Invoices']");

		HelperClass.isElementPresent(PurchaseInvoice);
		HelperClass.clickUsingJS(PurchaseInvoice);
	}

	// Receipts under Receipts
	public void ClickOnUploadPurchaseInvoices() {
		By ClickOnAddUploadPurchaseInvoices = By.xpath("//span[normalize-space()='Upload Receipts']");
		HelperClass.isElementPresent(ClickOnAddUploadPurchaseInvoices);
		HelperClass.waitUntilClickable(ClickOnAddUploadPurchaseInvoices);
		HelperClass.clickUsingJS(ClickOnAddUploadPurchaseInvoices);
	}

	public void SearchBar() {
		By SearchBar = By.xpath("//input[@formcontrolname='search']");
		HelperClass.isElementPresent(SearchBar);
		HelperClass.waitUntilClickable(SearchBar);
		HelperClass.safeSendKeys(SearchBar, "Test");
	}

	public void ClickTimeFilerDropdownOnReceiptsTab(String visibleText) {
		receiptLocator = PageFactory.initElements(HelperClass.getDriver(), Capium365_Receipts_Receipts_Locators.class);
		WebDriver driver = HelperClass.getDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		By dropdownTrigger = By
				.xpath("//ng-select[contains(@class,'w-170')]//div[contains(@class,'ng-select-container')]");
		By optionLocator = By.xpath("//div[@role='option']/span/span[normalize-space(text())='" + visibleText + "']");

		int retryCount = 0;
		while (retryCount < 2) {
			try {
				WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(dropdownTrigger));
				HelperClass.sleep1(3000);
				trigger.click();
				HelperClass.sleep1(3000);
				wait.until(ExpectedConditions.presenceOfElementLocated(optionLocator));
				wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
				wait.until(ExpectedConditions.elementToBeClickable(optionLocator));

				WebElement option = driver.findElement(optionLocator);
				js.executeScript("arguments[0].scrollIntoView(true);", option);
				js.executeScript("arguments[0].click();", option);
				HelperClass.sleep1(2000);
				Log.info("Successfully selected time filter: " + visibleText);
				return;

			} catch (StaleElementReferenceException sere) {
				Log.warn("Stale element detected. Retrying... (" + (retryCount + 1) + ")");
			} catch (TimeoutException te) {
				Log.error("Timeout waiting for dropdown option: " + visibleText);
				break;
			} catch (Exception e) {
				Log.error("Dropdown selection failed: " + e.getMessage());
				e.printStackTrace();
				break;
			}

			retryCount++;
			HelperClass.sleep1(1000);
		}
	}

	public void selectAllTimeDropdownOptionsAndReset(String specificOption) {
		WebDriver driver = HelperClass.getDriver();
		WebDriverWait wait = HelperClass.getWait();

		By dropdownToggle = By
				.xpath("//ng-select[contains(@class,'w-170')]//div[contains(@class,'ng-select-container')]");
		By dropdownOptionsLocator = By.xpath(
				"//ng-dropdown-panel[@class='ng-dropdown-panel ng-star-inserted ng-select-bottom']//div[@role='option']");
		By resultsRowLocator = By.xpath("//table//tr[contains(@class, 'ng-star-inserted')]");
		By noResultsLocator = By.xpath("//div[contains(text(),'No results found')]");
		By statusColumnCells = By.xpath("//table//tr[contains(@class, 'ng-star-inserted')]/td[10]");

		HelperClass.safeClick(dropdownToggle);
		HelperClass.sleep1(500);

		List<WebElement> options = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptionsLocator));
		if (options.isEmpty()) {
			System.out.println("No options found in 'All Time' dropdown.");
			return;
		}
		String defaultOption = options.get(0).getText().trim();
		for (int i = 0; i < options.size(); i++) {
			HelperClass.safeClick(dropdownToggle);
			HelperClass.sleep1(500);
			List<WebElement> freshOptions = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptionsLocator));
			if (i >= freshOptions.size())
				break;
			WebElement option = freshOptions.get(i);
			String optionText = option.getText().trim();
			if (optionText.isEmpty())
				continue;
			if (specificOption != null && !optionText.equalsIgnoreCase(specificOption)) {
				continue;
			}

			System.out.println("Selecting time filter: " + optionText);
			Log.info("Selecting: " + optionText);
			HelperClass.scrollToElement(option);
			option.click();

			try {
				wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(resultsRowLocator),
						ExpectedConditions.presenceOfElementLocated(noResultsLocator)));
			} catch (Exception e) {
				System.out.println("Timeout waiting for results for: " + optionText);
				continue;
			}
			HelperClass.sleep1(800);
			List<WebElement> rows = driver.findElements(resultsRowLocator);
			if (rows.isEmpty()) {
				System.out.println("No data to display for: " + optionText);
			} else {
				List<WebElement> statusCells = driver.findElements(statusColumnCells);
				if (statusCells.isEmpty()) {
					System.out.println("Status column not found for: " + optionText);
				} else {
					System.out.println("Status values for " + optionText + ":");
					for (WebElement cell : statusCells) {
						String statusText = cell.getText().trim();
						System.out.println(" - " + statusText);
					}
				}
			}

			if (specificOption != null)
				break;
		}

		HelperClass.safeClick(dropdownToggle);
		HelperClass.sleep1(500);

		List<WebElement> finalOptions = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptionsLocator));
		for (WebElement option : finalOptions) {
			if (option.getText().trim().equalsIgnoreCase(defaultOption)) {
				HelperClass.scrollToElement(option);
				option.click();
				System.out.println("Reset to default option: " + defaultOption);
				break;
			}
		}
	}

	public void selectAllStatusOptions() {
		WebDriver driver = HelperClass.getDriver();
		WebDriverWait wait = HelperClass.getWait();

		By dropdownToggle = By
				.xpath("//ng-select[@formcontrolname='statusChange']//div[contains(@class, 'ng-select-container')]");
		By dropdownOptionsLocator = By.xpath("//div[contains(@class,'ng-dropdown-panel')]//div[@role='option']");
		By resultsTableRow = By.xpath("//table//tr[contains(@class, 'ng-star-inserted')]");
		By noResultsMessage = By.xpath("//div[contains(text(), 'No results found')]");

		HelperClass.safeClick(dropdownToggle);
		HelperClass.sleep1(1000);

		List<WebElement> initialOptions = HelperClass.retryUntilNotEmpty(
				() -> wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptionsLocator)));

		if (initialOptions.isEmpty()) {
			System.out.println("No dropdown options found.");
			return;
		}

		String defaultOptionText = initialOptions.get(0).getText().trim();

		for (int i = 0; i < initialOptions.size(); i++) {
			// Reopen dropdown each time
			HelperClass.safeClick(dropdownToggle);
			HelperClass.sleep1(400);

			List<WebElement> currentOptions = HelperClass.retryUntilNotEmpty(
					() -> wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptionsLocator)));

			if (i >= currentOptions.size())
				break;

			WebElement option = currentOptions.get(i);
			String selectedStatus = option.getText().trim();
			if (selectedStatus.isEmpty())
				continue;

			System.out.println("Selecting Status: " + selectedStatus);
			HelperClass.scrollToElement(option);
			option.click();

			wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(resultsTableRow),
					ExpectedConditions.presenceOfElementLocated(noResultsMessage)));

			HelperClass.sleep1(700);

			int statusColIndex = getStatusColumnIndex();
			String statusCellXPath = "//table//tr[contains(@class, 'ng-star-inserted')]/td[" + statusColIndex + "]";

			List<WebElement> statusCells = driver.findElements(By.xpath(statusCellXPath));

			if (statusCells.isEmpty()) {
				System.out.println("No data rows for: " + selectedStatus);
			} else {
				boolean allMatch = true;
				for (WebElement cell : statusCells) {
					String cellText = cell.getText().trim();
					if (!cellText.equalsIgnoreCase(selectedStatus)) {
						System.out.println("Mismatch. Expected: " + selectedStatus + " but found: " + cellText);
						allMatch = false;
					}
				}
				if (allMatch) {
					System.out.println("All rows matched status: " + selectedStatus);
				}
			}
		}

		HelperClass.safeClick(dropdownToggle);
		HelperClass.sleep1(1000);

		List<WebElement> resetOptions = HelperClass.retryUntilNotEmpty(
				() -> wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptionsLocator)));

		for (WebElement option : resetOptions) {
			if (option.getText().trim().equalsIgnoreCase(defaultOptionText)) {
				HelperClass.scrollToElement(option);
				option.click();
				System.out.println("Reset to default option: " + defaultOptionText);
				break;
			}
		}
	}

	private int getStatusColumnIndex() {
		List<WebElement> headers = HelperClass.getDriver().findElements(By.xpath("//table//thead//th"));
		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i).getText().trim().equalsIgnoreCase("Status")) {
				return i + 1; // XPath index is 1-based
			}
		}
		throw new RuntimeException("'Status' column not found in table header.");
	}

	public void ClickonThreeDots() {
		HelperClass.clickWithRetry(receiptLocator.ThreedotsinReceipts, HelperClass.getDriver(), HelperClass.getWait());
	}

	public void ClickOnExportToExcel() {
		HelperClass.clickWithRetry(receiptLocator.ExportToExcel, driver, wait);
	}

	public void ClickOnExportToCSV() {
		HelperClass.clickWithRetry(receiptLocator.ExportToCSV, driver, wait);
	}

	public void ClickOnExportToPDF() {
		HelperClass.clickWithRetry(receiptLocator.ExportToPDF, driver, wait);
	}

	public void ClickOnChoseAfile() throws IOException, AWTException {
		//String ReceipteUploadPath1 = "D:\\Autoit\\UploadReceipt.exe";
		HelperClass.clickWithRetry(receiptLocator.ChooseAfile, driver, wait);
		HelperClass.sleep1(2000);
		Robot robot = new Robot();

		StringSelection selection = new StringSelection(FilePath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);

		HelperClass.sleep1(2000);

		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		HelperClass.sleep1(2000);
	}

	public void ClickOnUploadReceiptsbutton() {
		HelperClass.clickWithRetry(receiptLocator.UploadReceipts, driver, wait);
		HelperClass.sleep1(6000);
	}

	public void RedirectToReceiptDashboard() {
		By ClickOnAddUploadPurchaseInvoices = By.xpath("//span[normalize-space()='Upload Receipts']");
		if (HelperClass.isElementVisible(ClickOnAddUploadPurchaseInvoices, 5)) {
			Log.info("Successfully navigated to Receipts Dashboard");
		} else {
			Log.error("Dashboard page not loaded");
		}
		HelperClass.sleep1(15000);
	}

	public void ValidateSupplierCreatedorNot() {
		By locate = By.xpath("//tbody/tr[1]/td[4]/div/span[normalize-space()='Atco Electrical Distributors Ltd']");
		WebElement element = driver.findElement(locate);
		String Actual = "Atco Electrical Distributors Ltd";
		String Expected = HelperClass
				.getText(By.xpath("//tr[1]/td/span/span[normalize-space()='Atco Electrical Distributors Ltd']"));

		if (Actual.equals(Expected)) {
			System.out.println("Receipt uploaded successfully");
		} else {
			System.out.println("Uploaded receipt not found");
		}
		HelperClass.highlightElement(element);
	}

	public void SelctToReviewOption() {
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//ng-select[@formcontrolname='statusChange']")));
		By dropdown = By.xpath("//ng-select[@formcontrolname='statusChange']");
		WebElement element = driver.findElement(dropdown);
		HelperClass.selectDivOptionDropdownValue(element, "To Review");
	}
	public void ClickOnFirstCheckbox() {
		
		HelperClass.sleep1(2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//table[@aria-describedby='Receipt Table']/tbody/tr[1]/td/div/mat-checkbox/label/span[1]")));
		wait.until(ExpectedConditions.elementToBeClickable(receiptLocator.ClickOnPurchaseInvoiceCheckbox));
		HelperClass.clickWithRetry(receiptLocator.ClickOnPurchaseInvoiceCheckbox, driver, wait);
		beforeCopyText = HelperClass
				.getText(By.xpath("//table[@aria-describedby='Receipt Table']/tbody/tr[1]/td[2]/div/a"));
	}

	public void ValidateAllOptionsAfterClickingonCheckbox() {
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'action-list')]/div/p")));
		String actualText = HelperClass.getText(By.xpath("//div[contains(@class,'action-list')]/div/p"));
		String[] expectedText = { "Edit", "Export", "Options" };

		String[] actualWords = actualText.trim().split("\\s+");

		if (Arrays.equals(actualWords, expectedText)) {
			System.out.println("Text matches exactly with expected array.");
			// HelperClass.clickUsingJS(By.xpath("//mat-icon[normalize-space()='highlight_off']//parent::span//parent::button"));
		} else {
			System.out.println("Text does NOT match.");
		}
	}

	public void ClickOnBulkEdit() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//p[normalize-space()='Bulk Edit']//parent::span//parent::button")));
		HelperClass.clickUsingJS(receiptLocator.ClickOnBulkEditOption);
	}

	public void VerifyBulkEditPopUpAppearOrNot() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Bulk Edit']")));
		String ExpectedText = HelperClass.getText(By.xpath("//h2[normalize-space()='Bulk Edit']"));
		String ActualText = "Bulk Edit";
		if (ActualText.equals(ExpectedText)) {
			System.out.println("Bulk Edit Popup page Matched");
		} else {
			System.out.println("Bulk Edit Popup page Not Matched");
		}
	}

	public void selecSupplerDropdownValueinBulkEdit(String visibleText) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			// STEP 1: Click the dropdown trigger once
			WebElement trigger = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//ng-select[@formcontrolname='supplier']")));
			trigger.click();

			// STEP 2: Wait for dropdown options to appear
			By optionLocator = By.xpath("//div[@role='option']//span[normalize-space(text())='" + visibleText + "']");
			wait.until(ExpectedConditions.presenceOfElementLocated(optionLocator));
			wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
			wait.until(ExpectedConditions.elementToBeClickable(optionLocator));

			// STEP 3: Click on the desired option
			WebElement option = driver.findElement(optionLocator);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);

			HelperClass.sleep1(1000); // Small wait after click to ensure selection registers
		} catch (TimeoutException e) {
			System.out.println("Option '" + visibleText + "' not found.");
		} catch (Exception e) {
			System.out.println("Dropdown selection failed: " + e.getMessage());
		}
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[text()='Save']//parent::span//parent::button")));
		HelperClass.clickUsingJS(By.xpath("//span[text()='Save']//parent::span//parent::button"));
		HelperClass.sleep1(3000);
	}

	public void ClickOnCopyOption() {
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//p[normalize-space()='Copy']//parent::span//parent::button")));
		HelperClass.clickUsingJS(By.xpath("//p[normalize-space()='Copy']//parent::span//parent::button"));
	}

	String beforeCopyText;

	public void VerifyBeforeClickingOnCopyOption() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//table[@aria-describedby='Receipt Table']/tbody/tr[1]/td[3]/div/div/a")));
		beforeCopyText = HelperClass
				.getText(By.xpath("//table[@aria-describedby='Receipt Table']/tbody/tr[1]/td[3]/div/div/a"));
		System.out.println("Before Copy Text: " + beforeCopyText);
	}

	public void VerifyAfterClickingOnCopyOption() {
		wait.until(ExpectedConditions
				.numberOfElementsToBeMoreThan(By.xpath("//table[@aria-describedby='Purchase Table']/tbody/tr"), 3));

		String afterCopyText = HelperClass
				.getText(By.xpath("//table[@aria-describedby='Receipt Table']/tbody/tr[1]/td[3]/div/div/a"));
		System.out.println("After Copy Text: " + afterCopyText);

		String beforeTextWithoutNumbers = beforeCopyText.replaceAll("\\d+", "").trim();
		String afterTextWithoutNumbers = afterCopyText.replaceAll("\\d+", "").trim();

		String beforeNumber = beforeCopyText.replaceAll("\\D+", ""); 
		String afterNumber = afterCopyText.replaceAll("\\D+", "");

		if (beforeTextWithoutNumbers.equals(afterTextWithoutNumbers) && !beforeNumber.equals(afterNumber)) {
			System.out.println("Copy successful: Content matches except number field.");
		} else {
			System.out.println("Copy failed: Content mismatch or number not updated.");
		}
	}

	public void ClickOnDeleteOption() {
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//p[normalize-space()='Delete']//parent::span//parent::button")));
		HelperClass.clickUsingJS(By.xpath("//p[normalize-space()='Delete']//parent::span//parent::button"));

	}

	public void ConfirmDeletepopUp() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Confirm Delete']")));
		String ActualText = "Confirm Delete";
		String ExpectedText = HelperClass.getText(By.xpath("//h2[normalize-space()='Confirm Delete']"));
		if (ActualText.equals(ExpectedText)) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//span[normalize-space(text())='Delete by selecting the box']//parent::label")));
			HelperClass.clickUsingJS(
					By.xpath("//span[normalize-space(text())='Delete by selecting the box']//parent::label"));
			System.out.println("Delete Confirmation pop-up matched");
			HelperClass.sleep1(2000);
			HelperClass.clickUsingJS(By.xpath("//span[normalize-space()='Yes']//parent::button"));
		} else {
			System.out.println("Delete Confirmation pop-up not matched");
		}
		HelperClass.sleep1(2000);
	}

	public void VerifyAfterDeletionofInvoice() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='search']")));
		HelperClass.safeSendKeys(By.xpath("//input[@formcontrolname='search']"), beforeCopyText + Keys.ENTER);
	}

	public void ClickOnExportOptions(String VisibleText) {

		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//p[normalize-space()='Export']//parent::div")));

		HelperClass
				.clickUsingJS(By.xpath("//p[normalize-space()='" + VisibleText + "']//parent::span//parent::button"));
		HelperClass.sleep1(4000);
	}

	public void ClickonArchived() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//p[normalize-space()='Archive']//parent::span//parent::button")));
		HelperClass.clickUsingJS(By.xpath("//p[normalize-space()='Archive']//parent::span//parent::button"));
		HelperClass.sleep1(2000);
	}

	public void VerifyArchivedPopup(String VisibleText) {
		receiptLocator = PageFactory.initElements(driver, Capium365_Receipts_Receipts_Locators.class);
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Confirm Archive']")));
		HelperClass.sleep1(2000);
		HelperClass.clickUsingJS(By.xpath("//span[normalize-space()='Yes']//parent::button"));
		HelperClass.sleep1(4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='search']")));
		HelperClass.safeSendKeys(By.xpath("//input[@formcontrolname='search']"), beforeCopyText + Keys.ENTER);
		HelperClass.sleep1(3000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//table[@aria-describedby='Receipt Table']/tbody/tr[1]/td[6]/div")));
		String ActualText = HelperClass.getText(
				By.xpath("//table[@aria-describedby='Receipt Table']/tbody/tr[1]/td[6]/div/span[normalize-space()='"
						+ VisibleText + "']"));
		String ExpectedText = "Archived";
		if (ActualText.equals(ExpectedText)) {
			System.out.println("Invoice Successfully Archived");
		} else {
			System.out.println("Invoice Not Archived");
		}

	}

	public void SelectArchivedInvoice() {
		HelperClass.sleep1(2000);
		HelperClass.clickDropdown(receiptLocator.ClickOnStatusChangeDropdown);
		HelperClass.clickUsingJS(By.xpath(
				"//ng-dropdown-panel[@aria-label='Options list']/div/div/div/span[normalize-space()='Archived']"));
	}

	public void ClickOnRestoreOption() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//p[normalize-space()='Restore']//parent::span//parent::button")));
		HelperClass.clickUsingJS(By.xpath("//p[normalize-space()='Restore']//parent::span//parent::button"));
	}

	public void VerifyConfirmRestore() {
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Confirm Restore']")));
		HelperClass.sleep1(2000);
		HelperClass.clickUsingJS(By.xpath("//span[normalize-space()='Yes']//parent::button"));
		HelperClass.sleep1(2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='search']")));
		HelperClass.safeSendKeys(By.xpath("//input[@formcontrolname='search']"), beforeCopyText + Keys.ENTER);
		HelperClass.sleep1(3000);

	}

	public void MouseHovertheElement() {
		WebElement element = driver.findElement(By.xpath("//table[@aria-describedby='Receipt Table']/tbody/tr[1]"));
		HelperClass.hoverOverElement(element);
	}

	public void VerifyElements() {
		if (HelperClass.isElementVisible(By.xpath("//tr[1]//mat-icon[normalize-space()='edit']"), 2)) {
			System.out.println("After Mouse hover Elements Visible Successfully");
		} else {
			System.out.println("After Mouse hover Elements are Not Showing:");
		}
	}

	public void ClickOnEditbutton() {
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[1]//mat-icon[normalize-space()='edit']")));
		HelperClass.clickUsingJS(By.xpath("//tr[1]//mat-icon[normalize-space()='edit']"));
	}

	public void VerifyEditPage() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Edit Receipt']")));
		String ExpectedText = HelperClass.getText(By.xpath("//h2[normalize-space()='Edit Receipt']"));
		String ActualText = "Edit Receipt";
		if (ExpectedText.equals(ActualText)) {
			System.out.println("Edit Page " + ExpectedText + " displayed successfully");
		}
		System.out.println("Edit page not Displayed");
	}

	public void ClickOnDeletebutton() {
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//tr[1]//mat-icon[normalize-space()='delete_outline']")));
		HelperClass.clickUsingJS(By.xpath("//tr[1]//mat-icon[normalize-space()='delete_outline']"));

	}

	public void VerifyDeletepopUp() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Confirm Delete']")));
		if (HelperClass.isElementPresent(By.xpath("//h2[normalize-space()='Confirm Delete']"))) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"//span[normalize-space(text())='Delete by selecting the box']//parent::label//parent::mat-checkbox")));
			HelperClass.sleep1(2000);
			HelperClass.clickUsingJS(By.xpath(
					"//span[normalize-space(text())='Delete by selecting the box']//parent::label//parent::mat-checkbox"));
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Yes']//parent::button")));
			HelperClass.clickUsingJS(By.xpath("//span[normalize-space()='Yes']//parent::button"));
			HelperClass.sleep1(2000);
			System.out.println("Receord delete successfully");
		} else {
			System.out.println("Delete pop-up not displayed");
		}
	}

	public void ClickOnArchivebutton() {
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//tr[1]//mat-icon[normalize-space()='archive']")));
		HelperClass.clickUsingJS(By.xpath("//tr[1]//mat-icon[normalize-space()='archive']"));
	}

	public void VerifyArchivepopUp() {
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Confirm Archive']")));
		if (HelperClass.isElementPresent(By.xpath("//h2[normalize-space()='Confirm Archive']"))) {
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Yes']//parent::button")));
			HelperClass.clickUsingJS(By.xpath("//span[normalize-space()='Yes']//parent::button"));
			System.out.println("Successfully Archived");
		} else {
			System.out.println("Not Archived");
		}
	}
	public void ClickOnThreedots() {
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//tr[1]//mat-icon[normalize-space()='more_vert']")));
		HelperClass.clickUsingJS(By.xpath("//tr[1]//mat-icon[normalize-space()='more_vert']"));

	}

	public void verifyPreviewpage() {
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Preview']//parent::button")));
		HelperClass.clickUsingJS(By.xpath("//span[normalize-space()='Preview']//parent::button"));
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Preview Receipts']//parent::h2")));

		if (HelperClass.isElementPresent(By.xpath("//span[normalize-space()='Preview Receipts']//parent::h2"))) {
			System.out.println("Preview Receipts page is visible");
		} else {
			System.out.println("Preview Receipts page is not visible");
		}
	}

	public void ClickOnHistoryandVerify() {

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'History')]")));
		if (HelperClass.isElementPresent(By.xpath("//span[contains(text(),'History')]"))) {
			System.out.println("History page verified");
		} else {
			System.out.println("History page not verified");
		}
	}
	
	

}
