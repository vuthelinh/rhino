package com.rhino.keyword;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.rhino.utility.Const;

public class Keywords{
	    /*
		 * Author: vuthelinh@gmail.com
		 * */
	public static Logger APP_LOGS = Logger.getLogger("devpinoyLogger");
	
	public static WebDriver _driver;
	
	public static Properties OBJECT;
	public static Properties CONFIG;
	

	File directory = new File(".");
	
	public Keywords() {

		System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.Jdk14Logger");
		
	}
	

	public static void scrollBrowser(String object, String data) {
		/*
		 * Scroll the document window to the horizontal position x-axis and vertical y-axis
		 * */
		String Data[] = data.split(",");
		APP_LOGS.debug("[info] Executing: |Scroll browser to the horizontal position " + Data[0] + " and vertical " + Data[1] + " | ");
		System.out.println("[info] Executing: |Scroll browser to the horizontal position " + Data[0] + " and vertical " + Data[1] + " | ");
				
		JavascriptExecutor js = (JavascriptExecutor) _driver;
		js.executeScript("window.scrollTo(" + Data[0] + "," + Data[1] + ")");
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// ----------------------- Chuan --------------------------
	public static String addSelection(String object, String data) {
		/*
		 * Add a selection to the set of selected options in a multi-select
		 * element using an option locator.
		 */
		APP_LOGS.debug("[info] Executing: |addSelection | " + OBJECT.getProperty(object) + " | label={" + data + "} |");
		System.out.println("[info] Executing: |addSelection | " + OBJECT.getProperty(object) + " | label={" + data + "} |");
		try{
		WebElement select = _driver.findElement(formats(OBJECT.getProperty(object)));
		java.util.List<WebElement> list = select.findElements(By.tagName("option"));
		String Data[] = data.split(",");
		// For loop to split and take single data from excel sheet
		for (int K = 0; K < Data.length; K++) {
			int j = 0;
			for (int i = 0; i < list.size(); i++)// For loop to select a data from listbox
			{
				j++;
				String str = list.get(i).getText();
				// System.out.println("Value in the list is "+str);
				// System.out.println("Value From the excel is "+Data[K]);
				if (str.equalsIgnoreCase(Data[K])) {
					j--;
					select.sendKeys(Keys.CONTROL);
					list.get(i).click(); //${str} is selected");
					break;
				}

				if (j == list.size()) {
					APP_LOGS.debug("[>>>> ERROR <<<] Option with label '" + Data[K] + "' not found");
					System.out.println("[>>>> ERROR <<<] Option with label '" + Data[K] + "' not found");
					return Const.FAIL + "[>>>> ERROR <<<] Option with label '" + Data[K] + "' not found";
				}
			}
		} 
		return Const.PASS;
		}catch(WebDriverException e) {
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
		}
	} 
	
	public static String chooseCancelOnNextConfirmation(String object, String data) {
		APP_LOGS.debug("[info] Executing: |chooseCancelOnNextConfirmation|");
		System.out.println("[info] Executing: |chooseCancelOnNextConfirmation|");

		try {
			Alert myAlert = _driver.switchTo().alert();
			myAlert.dismiss(); // cancel the alert - equivalent of pressing CANCEL
			return Const.PASS;
		} catch (NoAlertPresentException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] There were no alerts" + e.getMessage());
			System.out.println("[>>>> ERROR <<<] There were no alerts"	+ e.getMessage());
			return Const.FAIL + ": There were no alerts";
		}
	}
		
	public static String chooseOkOnNextConfirmation(String object, String data) {
		APP_LOGS.debug("[info] Executing: |chooseOkOnNextConfirmation |");
		System.out.println("[info] Executing: |chooseOkOnNextConfirmation |");
		try {
			Alert myAlert = _driver.switchTo().alert(); // Switch to alert pop-up
			myAlert.accept(); // accept the alert - equivalent of pressing OK
			return Const.PASS;
		} catch (NoAlertPresentException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] There were no alerts" + e.getMessage());
			System.out.println("[>>>> ERROR <<<] There were no alerts"	+ e.getMessage());
			return Const.FAIL + ": There were no alerts";
		}
	}
	
	public static String answerOnNextPrompt(String object, String data) {
		APP_LOGS.debug("[info] Executing: |answerOnNextPrompt |");
		System.out.println("[info] Executing: |answerOnNextPrompt |");
		try {
			Alert myAlert = _driver.switchTo().alert();
			myAlert.sendKeys(data); // Write text inside the prompt
			myAlert.accept();
			return Const.PASS;
		} catch (NoAlertPresentException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] There were no alerts" + e.getMessage());
			System.out.println("[>>>> ERROR <<<] There were no alerts"	+ e.getMessage());
			return Const.FAIL + ": There were no alerts";
		}
	}
	
	public static String windowMaximize(String object, String data) {
		APP_LOGS.debug("[info] Executing: |maximize |");
		System.out.println("[info] Executing: |maximize |");
		
		try{
			_driver.manage().window().maximize();
			return Const.PASS;
		}catch (WebDriverException e) {
			return Const.FAIL + e.getMessage();
		}
	}
	
	public static String verifyElementPresent(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyElementPresent | " + OBJECT.getProperty(object) + " |");
		System.out.println("[info] Executing: |verifyElementPresent | " + OBJECT.getProperty(object) + " |");
		
		int counter = storeXpathCount(object);
			
		if (counter > 0) {
			return Const.PASS;
		} else {
			APP_LOGS.debug("[>>>> ERROR <<<] False");
			System.out.println("[>>>> ERROR <<<] False");
			return Const.FAIL;
		}
	}
	
	public static String verifyText(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyText | " + OBJECT.getProperty(object) + " | "	+ data + " |");
		System.out.println("[info] Executing: |verifyText | " + OBJECT.getProperty(object) + " | "	+ data + " |");

		String text = storeText(object);

		if (text.equals(data)) {
				return Const.PASS;
		} else {
			APP_LOGS.debug("[>>>> ERROR <<<] Actual text '" + text + "' didn't match '" + data + "'");
			System.out.println("[>>>> ERROR <<<] Actual text '" + text	+ "' didn't match '" + data + "'");
			return Const.FAIL + ": Actual text '" + text	+ "' didn't match '" + data + "'";
		}
	}
	
	public static String close(String object, String data) {
		APP_LOGS.debug("[info] Executing: |close |");
		System.out.println("[info] Executing: |close |");
		
		try {
			//_driver.close();
			_driver.quit();
			System.gc();	
			return Const.PASS;
		} catch (Exception e) {	return Const.FAIL;}
	}
	
	public static String pause(String object, String data) {
		APP_LOGS.debug("[info] Executing: |pause | " + data + " (s) |");
		System.out.println("[info] Executing: |pause | " + data + " (s) |");
		
		long datas = Long.parseLong(data);
		try {
			Thread.sleep(datas);
			return Const.PASS;
		} catch (Exception e) {return Const.FAIL + e.getMessage();}
	}
	
	public static String type(String object, String data) {
		clear(object, data);
		return sendKey(object, data);
	}
	
	public static String clear(String object, String data) {
		try {
			_driver.findElement(formats(OBJECT.getProperty(object))).clear();
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			System.out.println("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL+ ": Element " + OBJECT.getProperty(object) + " not found !";}
	}
	
	public static String waitForElementPresent(String object, String data) {
		APP_LOGS.debug("[info] Executing: |waitForElementPresent | " + OBJECT.getProperty(object) + " |");
		System.out.println("[info] Executing: |waitForElementPresent | " + OBJECT.getProperty(object) + " |");
		try {
			WebDriverWait wait = new WebDriverWait(_driver, Long.valueOf(CONFIG.getProperty("selenium.timeout")));
			wait.until(ExpectedConditions.visibilityOfElementLocated(formats(OBJECT.getProperty(object))));
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n" + e.getMessage());
			System.out.println("[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n" + e.getMessage());
			return Const.FAIL;
		}
	}
	
	public static String open(String object, String data) {
		
		try {
			_driver = openBrowser(CONFIG.getProperty("selenium.browser"));
			
			APP_LOGS.debug("[info] Executing: |open application url | " + CONFIG.getProperty("selenium.host") + " |");
			System.out.println("[info] Executing: |open application url | " + CONFIG.getProperty("selenium.host") + " |");
			_driver.get(CONFIG.getProperty("selenium.host"));
			
			return Const.PASS;
		} catch(Exception e){
			return Const.FAIL;}
	}
	
	public static String captureEntirePageScreenshot(String object, String data) {
		 try {
			 
			 File scrFile = ((TakesScreenshot)_driver).getScreenshotAs(OutputType.FILE);
			 FileUtils.copyFile(scrFile, new File(CONFIG.getProperty("selenium.report")+ Const.IMAGES + data +".png"));
			 return Const.PASS;			
		} catch (IOException e) { 
			return Const.FAIL + e.getMessage();
		}
}

	private static WebDriver openBrowser(String browserType) {
		
		APP_LOGS.debug("[info] Executing: |Launch " + browserType + " browser|");
		System.out.println("[info] Executing: |Launch " + browserType + " browser|");
		
		WebDriver driver = null;
		
		try {

			switch (browserType) {

			case "firefox":

				driver = new FirefoxDriver();	break;

			case "iexplorer":

				System.setProperty("webdriver.ie.driver", CONFIG.getProperty("webdriver_ie"));

				DesiredCapabilities capab = DesiredCapabilities.internetExplorer();
				capab.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);

				driver = new InternetExplorerDriver(capab);	break;

			case "googlechrome":

				System.setProperty("webdriver.chrome.driver", CONFIG.getProperty("webdriver.chrome"));

				driver = new ChromeDriver(); break;
			}

		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<]  Are you sure " + browserType + " browser or WebDriver server installed?\n" + e.getMessage());
			System.out.println("[>>>> ERROR <<<]  Are you sure " + browserType + " browser or WebDriver server installed?\n" + e.getMessage());
		}

		return driver;
	}
	
	private static By formats(String locator) {
		By by = null;

		if (locator.startsWith("id=")) {

			locator = locator.substring(3);
			by = By.id(locator);

		} else if (locator.startsWith("name=")) {

			locator = locator.substring(5);
			by = By.name(locator);

		} else if (locator.startsWith("css=")) {

			locator = locator.substring(4);
			by = By.cssSelector(locator);

		} else if (locator.startsWith("link=")) {

			locator = locator.substring(5);
			by = By.linkText(locator);

		} else if (locator.startsWith("//")) {
			by = By.xpath(locator);
		} else {
			APP_LOGS.debug("[>>>> ERROR <<<] Executing: |This xpath format does not support| |");
		}

		return by;
	}
	
	private static WebElement getElementBy(String object) {
		
		return _driver.findElement(formats(OBJECT.getProperty(object)));
	}

	private static String storeText(String object) {
		try {
			return _driver.findElement(formats(OBJECT.getProperty(object))).getText();
		} catch (WebDriverException e) {
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
		}
	}

	private static int storeXpathCount(String object) {
		List<WebElement> elements=null;
		
		try {
				elements = getElementsBy(object);
		}catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found \n " + e.getMessage());
			System.out.println("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found \n " + e.getMessage());
		}
		
		return elements.size();
	}
	
	private static List<WebElement> getElementsBy(String object) {
		return _driver.findElements(formats(OBJECT.getProperty(object)));
	}

	public static String sendKey(String object, String data) {
		APP_LOGS.debug("[info] Executing: |sendKey | " + OBJECT.getProperty(object) + " | " + data + " | ");
		System.out.println("[info] Executing: |sendKey | " + OBJECT.getProperty(object) + " | " + data + " | ");

		try {

			WebElement elements = getElementBy(object);
			elements.sendKeys(data);
			
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			System.out.println("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
		}
	}
	
	public static String click(String object, String data) {
		APP_LOGS.debug("[info] Executing: |click | " + OBJECT.getProperty(object) + " | ");
		System.out.println("[info] Executing: |click | " + OBJECT.getProperty(object) + " | ");
		
		try {
			WebElement elements = getElementBy(object);
			elements.click();
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			System.out.println("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
}