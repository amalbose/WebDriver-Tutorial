package com.axatrikx.imagecomparison;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ImageComparisonTest {

	WebDriver driver;
	ImageComparator imgComparator;

	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		imgComparator = new ImageComparator();
	}

	@Test
	public void testCompareWithSavedImage() {
		// Perform required operation on test environment
		performRequiredOperation("http://test/application/url");

		// take screenshot
		WebElement actualElementToCompare = driver.findElement(By.id("locator"));
		File actualImage = takeScreenShot(actualElementToCompare);
		
		File baseImage = new File("//location/to/base/image");
		
		// compare screenshot
		boolean res = imgComparator.compareImages(baseImage, actualImage, 10);
		Assert.assertTrue(res, "Element not displayed correctly");

		String diffImage = imgComparator.getDiffFileName();
		// Write the diffImage to report
	}

	@Test
	public void testCompareWithBaseline() {
		// Perform required operation on baseline environment
		performRequiredOperation("http://baseline/application/url");

		// take screenshot
		WebElement baseElementToCompare = driver.findElement(By.id("locator"));
		File baseImage = takeScreenShot(baseElementToCompare);

		// Perform required operation on test environment
		performRequiredOperation("http://test/application/url");

		// take screenshot
		WebElement actualElementToCompare = driver.findElement(By.id("locator"));
		File actualImage = takeScreenShot(actualElementToCompare);

		// compare screenshot
		boolean res = imgComparator.compareImages(baseImage, actualImage, 10);
		Assert.assertTrue(res, "Element not displayed correctly");

		String diffImage = imgComparator.getDiffFileName();
		// Write the diffImage to report
	}
	
	@Test
	public void testFullScreenCompareWithSavedImage() {
		// Perform required operation on test environment
		performRequiredOperation("http://test/application/url");

		// take screenshot
		File actualImage = takeScreenShot();
		
		File baseImage = new File("//location/to/base/image");
		
		// compare screenshot
		boolean res = imgComparator.compareImages(baseImage, actualImage, 10);
		Assert.assertTrue(res, "Element not displayed correctly");

		String diffImage = imgComparator.getDiffFileName();
		// Write the diffImage to report
	}
	
	@AfterClass
	public void tearDown(){
		driver.quit();
	}

	private void performRequiredOperation(String string) {
		// navigate to required page
	}

	/**
	 * Takes screenshot and returns fileName
	 * 
	 * @return
	 */
	private File takeScreenShot() {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}

	/**
	 * Takes screenshot of the element
	 * 
	 * @param element
	 * @return
	 */
	private File takeScreenShot(WebElement element) {
		File screenShotImage = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		// Element we require the screenshot for which can be a div, table etc.
		Point p = element.getLocation();
		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();

		BufferedImage img;
		try {
			img = ImageIO.read(screenShotImage);
			BufferedImage dest = img.getSubimage(p.getX(), p.getY(), width, height);
			ImageIO.write(dest, "png", screenShotImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return screenShotImage;
	}

}
