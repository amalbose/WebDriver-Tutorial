package com.axatrikx;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tutorial_1 {

	// Webdriver variable
	private WebDriver driver;

	/**
	 * Initializes the chromedriver object
	 */
	private void initDriver() {
		// Setting the path to chromedriver.exe file
		System.setProperty("webdriver.chrome.driver", "res\\chromedriver.exe");

		// Initializes ChromeDriver
		driver = new ChromeDriver(); // Window opens up after this step, but
										// doesnt load any URL
		// Maximizes the window
		driver.manage().window().maximize(); // Browser maximizes
	}

	/**
	 * Loads the url and perform search and clicks on selenium downloads link
	 */
	private void performSearch() {
		// loads the URL
		driver.get("http://google.com"); // DONT FORGET THE PROTOCOL (http://)

		// Gets the search box
		driver.findElement(By.id("lst-ib")).sendKeys("Selenium" + Keys.ENTER);
		// Types Selenium and presses ENTER

		// Wait till the link is displayed
		WebDriverWait wait = new WebDriverWait(driver, 5000);
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Downloads - Selenium")));

		// click on the first link with link text 'Downloads - Selenium'
		driver.findElement(By.linkText("Downloads - Selenium")).click();
	}

	private void verifyResultPage() {
		String pageTitle = driver.getTitle();
		String expectedTitle = "Downloads";
		if (expectedTitle.equals(pageTitle)) {
			System.out.println("Pass");
		} else {
			System.out.println("Fail : Page title was : " + pageTitle);
		}
	}

	private void close() {
		driver.quit();
	}

	public static void main(String[] args) {
		Tutorial_1 t = new Tutorial_1();
		t.initDriver();
		t.performSearch();
		t.verifyResultPage();
		t.close();
	}
}
