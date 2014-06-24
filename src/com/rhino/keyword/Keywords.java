package com.rhino.keyword;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.api.robot.Key;
import org.sikuli.script.Button;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import com.rhino.utility.Const;
import com.rhino.utility.Excel_Reader;

public class Keywords{
	    /*
		 * Author: vuthelinh@gmail.com
		 * */
	public static Logger APP_LOGS = Logger.getLogger("devpinoyLogger");
	
	public static WebDriver _driver;
	protected static Screen _screen = new Screen();
	
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
		int x = Integer.parseInt(Data[0]);
		int y = Integer.parseInt(Data[1]);
		APP_LOGS.debug("[info] Executing: |Scroll browser to the horizontal position " + Data[0] + " and vertical " + Data[1] + " | ");

		JavascriptExecutor js = (JavascriptExecutor) _driver;
		js.executeScript("window.scrollTo(" + x + "," + y + ")");
		//js.executeScript("window.scrollBy(0," + data + ")");
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String iScroller(String object, String data) throws FindFailed,AWTException {
		APP_LOGS.debug("[info] Executing: |iScroller  | scroll browser to up/down :" + data + "|");
	
		try{
		_screen.mouseMove(object);
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		int x = (int) b.getX();
		int y = (int) b.getY();
		int dy = Integer.parseInt(data);
		
		Robot r = new Robot();
		_screen.mouseDown(Button.LEFT);
		r.mouseMove(x, y + dy);
		_screen.mouseUp(Button.LEFT);
		return  Const.PASS;
		}catch (Exception e) {
			e.printStackTrace();
			return Const.FAIL;
		}
}

	public static String addSelection(String object, String data) {
		/*
		 * Add a selection to the set of selected options in a multi-select
		 * element using an option locator.
		 */
		APP_LOGS.debug("[info] Executing: |addSelection | " + OBJECT.getProperty(object) + " | label={" + data + "} |");

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
				// s.println("Value in the list is "+str);
				// s.println("Value From the excel is "+Data[K]);
				if (str.equalsIgnoreCase(Data[K])) {
					j--;
					select.sendKeys(Keys.CONTROL);
					list.get(i).click(); //${str} is selected");
					break;
				}

				if (j == list.size()) {
					APP_LOGS.debug("[>>>> ERROR <<<] Option with label '" + Data[K] + "' not found");
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

		try {
			Alert myAlert = _driver.switchTo().alert();
			myAlert.dismiss(); // cancel the alert - equivalent of pressing CANCEL
			return Const.PASS;
		} catch (NoAlertPresentException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] There were no alerts" + e.getMessage());
			return Const.FAIL + ": There were no alerts";
		}
	}
		
	public static String chooseOkOnNextConfirmation(String object, String data) {
		APP_LOGS.debug("[info] Executing: |chooseOkOnNextConfirmation |");

		try {
			Alert myAlert = _driver.switchTo().alert(); // Switch to alert pop-up
			myAlert.accept(); // accept the alert - equivalent of pressing OK
			return Const.PASS;
		} catch (NoAlertPresentException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] There were no alerts" + e.getMessage());
		
			return Const.FAIL + ": There were no alerts";
		}
	}
	
	public static String answerOnNextPrompt(String object, String data) {
		APP_LOGS.debug("[info] Executing: |answerOnNextPrompt |");
		
		try {
			Alert myAlert = _driver.switchTo().alert();
			myAlert.sendKeys(data); // Write text inside the prompt
			myAlert.accept();
			return Const.PASS;
		} catch (NoAlertPresentException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] There were no alerts" + e.getMessage());
		
			return Const.FAIL + ": There were no alerts";
		}
	}
	
	public static String windowMaximize(String object, String data) {
		APP_LOGS.debug("[info] Executing: |maximize |");
				
		try{
			_driver.manage().window().maximize();
			return Const.PASS;
		}catch (WebDriverException e) {
			return Const.FAIL + e.getMessage();
		}
	}
	
	public static String verifyElementPresent(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyElementPresent | " + OBJECT.getProperty(object) + " |");
				
		int counter = storeXpathCount(object);
			
		if (counter > 0) {
			return Const.PASS;
		} else {
			APP_LOGS.debug("[>>>> ERROR <<<] False");
			return Const.FAIL;
		}
	}
	
	public static String verifyNotElementPresent(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyNotElementPresent | " + OBJECT.getProperty(object) + " |");
				
		int counter = storeXpathCount(object);
		if (counter == 0) {
			return Const.PASS;
		} else {
			APP_LOGS.debug("[>>>> ERROR <<<] False");
			return Const.FAIL;
		}
	}
		
	public static String verifyLocation(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyLocation | " + data + " |");
				
		String currentUrl = _driver.getCurrentUrl();
		
		if (currentUrl.equals(data)) {
			return Const.PASS;
		}else{
			APP_LOGS.debug("[>>>> ERROR <<<] False");
			return Const.FAIL;
	}

	}
	
	public static String verifyChecked(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyChecked | " + OBJECT.getProperty(object) + " |");
			
					WebElement elements = getElementBy(object);
					String getChecked = elements.getAttribute("checked");

					if (getChecked != null) {
						return Const.PASS;
					}else{
						APP_LOGS.debug("[>>>> ERROR <<<] False");
		
						return Const.FAIL;
				}
	}
	
	public static String verifyNotChecked(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyNotChecked | " + OBJECT.getProperty(object) + " |");
			
					WebElement elements = getElementBy(object);
					String getChecked = elements.getAttribute("checked");

					if (getChecked == null) {
						return Const.PASS;
					}else{
						APP_LOGS.debug("[>>>> ERROR <<<] False");
		
						return Const.FAIL;
				}
	}
	
	public static String verifyText(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyText | " + OBJECT.getProperty(object) + " | "	+ data + " |");
		
		String text = storeText(object);

		if (text.equals(data)) {
				return Const.PASS;
		} else {
			APP_LOGS.debug("[>>>> ERROR <<<] Actual text '" + text + "' didn't match '" + data + "'");
			return Const.FAIL + ": Actual text '" + text	+ "' didn't match '" + data + "'";
		}
	}
	
	public static String verifyValue(String object, String data) {
		APP_LOGS.debug("[info] Executing: |verifyValue | " + OBJECT.getProperty(object) + " | "	+ data + " |");
		
		String text = storeValue(object);

		if (text.equals(data)) {
				return Const.PASS;
		} else {
			APP_LOGS.debug("[>>>> ERROR <<<] Actual value '" + text + "' didn't match '" + data + "'");
			return Const.FAIL + ": Actual value '" + text	+ "' didn't match '" + data + "'";
		}
	}
	
	public static String close(String object, String data) {
		APP_LOGS.debug("[info] Executing: |close |\n");
		
		try {
			_driver.quit();
			System.gc();	
			return Const.PASS;
		} catch (Exception e) {	return Const.FAIL;}
	}
	
	public static String pause(String object, String data) {
		APP_LOGS.debug("[info] Executing: |pause | " + data + " (s) |");
		
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
			return Const.FAIL+ ": Element " + OBJECT.getProperty(object) + " not found !";}
	}
	
	public static String waitForVisible(String object, String data) {
		APP_LOGS.debug("[info] Executing: |waitForVisible | " + OBJECT.getProperty(object) + " |");
		try {
			WebDriverWait wait = new WebDriverWait(_driver, Long.valueOf(CONFIG.getProperty("selenium.timeout")));
			wait.until(ExpectedConditions.visibilityOfElementLocated(formats(OBJECT.getProperty(object))));
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n" + e.getMessage());
			return Const.FAIL;
		}
	}
	
	public static String waitForNotVisible(String object, String data) {
		APP_LOGS.debug("[info] Executing: |waitForNotVisible | " + OBJECT.getProperty(object) + " |");
		try {
			WebDriverWait wait = new WebDriverWait(_driver, Long.valueOf(CONFIG.getProperty("selenium.timeout")));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(formats(OBJECT.getProperty(object))));
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n" + e.getMessage());
			return Const.FAIL;
		}
	}
	
	public static String waitForElementPresent(String object, String data) {
		APP_LOGS.debug("[info] Executing: |waitForElementPresent | " + OBJECT.getProperty(object) + " |");
		try {
			WebDriverWait wait = new WebDriverWait(_driver, Long.valueOf(CONFIG.getProperty("selenium.timeout")));
			wait.until(ExpectedConditions.presenceOfElementLocated(formats(OBJECT.getProperty(object))));
			return Const.PASS;
		} catch (NoSuchElementException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n" + e.getMessage());
			return Const.FAIL;
		}
	}
	
	public static String waitForAlertPresent(String object, String data) {
		APP_LOGS.debug("[info] Executing: |waitForAlertPresent | " + OBJECT.getProperty(object) + " |");
		try {
		 WebDriverWait wait = new WebDriverWait(_driver, Long.valueOf(CONFIG.getProperty("selenium.timeout")));
		 wait.until(ExpectedConditions.alertIsPresent());
		 return Const.PASS;
		} catch (NoSuchElementException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n" + e.getMessage());
			return Const.FAIL;
		}
	}
	
	public static String open(String object, String data) {
		
		try {
			_driver = openBrowser(CONFIG.getProperty("selenium.browser"));
			
			APP_LOGS.debug("[info] Executing: |open application url | " + CONFIG.getProperty("selenium.host") + " |");
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
		}

		 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
	
	private static String storeValue(String object) {
		try {
			return _driver.findElement(formats(OBJECT.getProperty(object))).getAttribute("value");
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
		}
		
		return elements.size();
	}
	
	private static List<WebElement> getElementsBy(String object) {
		return _driver.findElements(formats(OBJECT.getProperty(object)));
	}

	public static String sendKey(String object, String data) {
		APP_LOGS.debug("[info] Executing: |sendKey | " + OBJECT.getProperty(object) + " | " + data + " | ");

		try {

			WebElement elements = getElementBy(object);
			elements.sendKeys(data);
			
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
		}
	}
	
	public static String click(String object, String data) {
		APP_LOGS.debug("[info] Executing: |click | " + OBJECT.getProperty(object) + " | ");
		
		try {
			WebElement elements = getElementBy(object);
			elements.click();
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
		
	public static String clicks(String object, String data) {
		APP_LOGS.debug("[info] Executing: |clicks | " + OBJECT.getProperty(object) + " | ");
		int times = Integer.parseInt(data);
		try {
			WebElement elements = getElementBy(object);
		
			for(int i=0;i<times;i++){
				elements.click();
			}
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String clickAt(String object, String data) {
		APP_LOGS.debug("[info] Executing: |clickAt | " + OBJECT.getProperty(object) + " | ");
		/*
		 * data - specifies the x,y position (i.e. - 10,20) of the mouse event relative to the element returned by the locator.
		 * */
		String Data[] = data.split(",");
		int x =   Integer.parseInt(Data[0]);
		int y =   Integer.parseInt(Data[1]);

		try {
				WebElement elements = getElementBy(object);
				Actions builder = new Actions(_driver);
				builder.moveByOffset(elements.getLocation().getX() + x, elements.getLocation().getY() + y).click();
				builder.perform();
				return Const.PASS;
				
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String mouseDown(String object, String data) {
		APP_LOGS.debug("[info] Executing: |mouseDown | " + OBJECT.getProperty(object) + " | ");
		/*
		 * Simulates a user pressing the left mouse button (without releasing it yet) at the specified location.
		 * */
		
		try {
				WebElement elements = getElementBy(object);
				Actions action = new Actions(_driver);
				action.clickAndHold(elements);
				action.perform();
				return Const.PASS;
				
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String mouseMove(String object, String data) {
		APP_LOGS.debug("[info] Executing: |mouseDown | " + OBJECT.getProperty(object) + " | ");
		/*
		 * Simulates a user pressing the left mouse button (without releasing it yet) at the specified location.
		 * */
		
		try {
				WebElement elements = getElementBy(object);
				Actions action = new Actions(_driver);
				action.release(elements);
				action.perform();
				return Const.PASS;
				
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String mouseDownAt(String object, String data) {
		APP_LOGS.debug("[info] Executing: |mouseDownAt | " + OBJECT.getProperty(object) + " | ");
		/*
		 * Simulates a user pressing the left mouse button (without releasing it yet) at the specified location.
		 * */
		String Data[] = data.split(",");
		int x =   Integer.parseInt(Data[0]);
		int y =   Integer.parseInt(Data[1]);

		try {
				WebElement elements = getElementBy(object);
				Actions action = new Actions(_driver);
				action.moveByOffset(elements.getLocation().getX() + x, elements.getLocation().getY() + y).clickAndHold();
				action.perform();
				return Const.PASS;
				
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String mouseMoveAt(String object, String data) {
		APP_LOGS.debug("[info] Executing: |mouseDown | " + OBJECT.getProperty(object) + " | ");
		/*
		 * Simulates a user pressing the left mouse button (without releasing it yet) at the specified location.
		 * */
		
		String Data[] = data.split(",");
		int x =   Integer.parseInt(Data[0]);
		int y =   Integer.parseInt(Data[1]);
		
		try {
				WebElement elements = getElementBy(object);
				Actions action = new Actions(_driver);
				action.moveByOffset(elements.getLocation().getX() + x, elements.getLocation().getY() + y).release();
				action.perform();
				return Const.PASS;
				
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String doubleClick(String object, String data) {
		APP_LOGS.debug("[info] Executing: |doubleClick | " + OBJECT.getProperty(object) + " | ");
		
		try {
				WebElement elements = getElementBy(object);
				Actions action = new Actions(_driver);	
				action.doubleClick(elements).perform();
				return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String contextMenu(String object, String data) {
		APP_LOGS.debug("[info] Executing: |contextMenu | " + OBJECT.getProperty(object) + " | ");

		try {
				WebElement elements = getElementBy(object);
				Actions action = new Actions(_driver);	
				action.contextClick(elements).perform(); //Right Click
				return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String mouseOver(String object, String data) {
		APP_LOGS.debug("[info] Executing: |mouseOver | " + OBJECT.getProperty(object) + " | ");
				
		try {
				WebElement elements = getElementBy(object);
				Actions action = new Actions(_driver);
				action.moveToElement(elements).perform();
				return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
		
	public static String dragAndDrop(String object, String data) {
	   /**
		 * Offset in pixels from the current location to which the element should be moved, e.g., "+70,-300"
		 * */
		APP_LOGS.debug("[info] Executing: |dragAndDrop | " + OBJECT.getProperty(object) + " | ");

		
		String Data[] = data.split(",");
		
		int x =   Integer.parseInt(Data[0]);
		int y =   Integer.parseInt(Data[1]);

		try {
				WebElement elements = getElementBy(object);
				Actions action = new Actions(_driver);
				action.clickAndHold(elements).moveByOffset(x, y).release().perform();
				return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String navigate(String object, String data) {
		APP_LOGS.debug("[info] Executing: |navigate | " + data + " | ");
		
		try {
				_driver.navigate().to(data);			
			return Const.PASS;
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Page " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Page " + OBJECT.getProperty(object) + " not found !";
			}
	}
		
	public static String check(String object, String data) {
		APP_LOGS.debug("[info] Executing: |check | " + OBJECT.getProperty(object) + " | ");

		try {
			WebElement elements = getElementBy(object);
			String getChecked = elements.getAttribute("checked");

			if (getChecked == null) {
				elements.click();
			}
			
			return Const.PASS;
			
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
		}
	}
	
	public static String selectByVisibleText(String object, String data) {
		APP_LOGS.debug("[info] Executing: |selectByVisibleText | " + OBJECT.getProperty(object) + " | " + data + "|");

		try{
			WebElement select = _driver.findElement(formats(OBJECT.getProperty(object)));
			Select option = new Select(select);
			java.util.List<WebElement> list = select.findElements(By.tagName("option"));
			
			// For loop to split and take single data from excel sheet
				int j = 0;
				for (int i = 0; i < list.size(); i++)// For loop to select a data from listbox
				{
					
					j++;
					String str = list.get(i).getText();
					if (str.equalsIgnoreCase(data)) {
						j--;
						list.get(i).click(); //${str} is selected");
						option.selectByVisibleText(data);
//						option.selectByValue("4");
//						option.selectByIndex(1);
						break;
					}

					if (j == list.size()) {
						APP_LOGS.debug("[>>>> ERROR <<<] Option with label '" + data + "' not found");
						return Const.FAIL + "[>>>> ERROR <<<] Option with label '" + data + "' not found";
					}
				}
			 
			return Const.PASS;
			}catch(WebDriverException e) {
				return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String selectByValue(String object, String data) {
		APP_LOGS.debug("[info] Executing: |selectByValue | " + OBJECT.getProperty(object) + " | " + data + "|");

		try{
			WebElement select = _driver.findElement(formats(OBJECT.getProperty(object)));
			
			Select option = new Select(select);
			option.deselectAll();
			 option.selectByValue(data);
			 return Const.PASS;
			}catch(WebDriverException e) {
				return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
	
	public static String selectByIndex(String object, String data) {
		APP_LOGS.debug("[info] Executing: |selectByIndex | " + OBJECT.getProperty(object) + " | " + data + "|");

		try{
			WebElement select = _driver.findElement(formats(OBJECT.getProperty(object)));
			
			Select option = new Select(select);
			 option.selectByIndex(2);
			 return Const.PASS;
			}catch(WebDriverException e) {
				return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
			}
	}
		
	public static String uncheck(String object, String data) {
		APP_LOGS.debug("[info] Executing: |check | " + OBJECT.getProperty(object) + " | ");
		try {
			WebElement elements = getElementBy(object);
			String getChecked = elements.getAttribute("checked");

			if (getChecked != null) {
				elements.click();
			}
			
			return Const.PASS;
			
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
		}
	}
		
	/*
	 * Sikuli
	 * */
	
	public static String iWait(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iWait | " + OBJECT.getProperty(object) + " | ");
		
			try { //Integer.parseInt(data)
				_screen.wait(OBJECT.getProperty(object), Integer.parseInt(CONFIG.getProperty("selenium.timeout")));

				return Const.PASS;
			} catch (FindFailed e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n");
				e.printStackTrace();
				return "[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n";
			}
		}
	
	public static String iClick(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iClick | " + OBJECT.getProperty(object) + " | ");
			try {
					_screen.click(OBJECT.getProperty(object));
					return Const.PASS;
					
			} catch (FindFailed e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !");
				e.printStackTrace();
				return Const.FAIL + ": [>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !";
			}
		}
	
	public static String iClickAt(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iClickAt | " + OBJECT.getProperty(object) + " | (" + data + ")");
		Pattern pattern = new Pattern(OBJECT.getProperty(object));
		
		String Data[] = data.split(",");
		
		int x = Integer.parseInt(Data[0]);
		int y = Integer.parseInt(Data[1]);
		
		try {
				_screen.click(pattern.targetOffset(x, y));
				return Const.PASS;
		} catch (FindFailed e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !");
			e.printStackTrace();
			return Const.FAIL + ": [>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !";
		}
	}
	
	public static String iClear(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iClean | " + OBJECT.getProperty(object) + " | ");
			try {
					_screen.type("a", Key.CTRL);
					_screen.type(Key.BACKSPACE);
					return Const.PASS;
					
			} catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !");
				e.printStackTrace();
				return Const.FAIL + ": [>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !";
			}
		}
	
	public static String iClearkAt(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iClearkAt | " + OBJECT.getProperty(object) + " | (" + data + ")");
		Pattern pattern = new Pattern(OBJECT.getProperty(object));
		
		String Data[] = data.split(",");
		
		int x = Integer.parseInt(Data[0]);
		int y = Integer.parseInt(Data[1]);
		
		try {
				_screen.click(pattern.targetOffset(x, y));
				_screen.type("a", Key.CTRL);
				_screen.type(Key.BACKSPACE);
				
				return Const.PASS;
		} catch (FindFailed e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !");
			e.printStackTrace();
			return Const.FAIL + ": [>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !";
		}
	}
	
	public static String pressTAB(String object, String data) {
		APP_LOGS.debug("[info] Executing: |pressTAB| | ");
		try {
			_screen.type(Key.TAB);
			return Const.PASS;
		} catch (Exception e){e.getMessage();
			return Const.FAIL;
		}
	}
	
	public static String iSendKey(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iSendKey | " + data + " | ");
			try {
					_screen.type(data);
					return Const.PASS;
			} catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !");
				e.printStackTrace();
				return Const.FAIL + ": [>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !";
			}
		}
	
	public static String iType(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iType | " + OBJECT.getProperty(object) + " | ");
			try {
					_screen.type(OBJECT.getProperty(object),data);
					return Const.PASS;
					
			} catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !");
				e.printStackTrace();
				return Const.FAIL + ": [>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !";
			}
		}
	
	public static String iMouseMove(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iMouseMove | " + OBJECT.getProperty(object) + " | ");
		try {
			_screen.mouseMove(OBJECT.getProperty(object));
			return Const.PASS;
		} catch (FindFailed e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not on screen !";
		}
	}
	
	public static String iMouseDown(String object, String data) {
		APP_LOGS.debug("[info] Executing: |iMouseDown |");
		try {
			_screen.mouseDown(Button.LEFT);
			return Const.PASS;
		} catch (Exception e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not on screen !";
		}
	}
	
	public static String iMouseMoveAt(String object, String data) {
		
		APP_LOGS.debug("[info] Executing: |iMouseMoveAt | " + OBJECT.getProperty(object) + "(" + data + ") | ");
		Pattern pattern = new Pattern(OBJECT.getProperty(object));
		String Data[] = data.split(",");
		int x =   Integer.parseInt(Data[0]);
		int y =   Integer.parseInt(Data[1]);
		
		try {
			_screen.mouseMove(pattern.targetOffset(x, y));
			return Const.PASS;
		} catch (FindFailed e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + object + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + object + " not on screen !";
		}
	}
	
	public static String iMouseDownAt(String object, String data) {
		
		APP_LOGS.debug("[info] Executing: |iMouseDownAt | " + object + "(" + data + ") | ");
		Pattern pattern = new Pattern(OBJECT.getProperty(object));
		String Data[] = data.split(",");
		int x =   Integer.parseInt(Data[0]);
		int y =   Integer.parseInt(Data[1]);
		
		try {
			_screen.hover(pattern.targetOffset(x, y));
			_screen.mouseDown(Button.LEFT);
			return Const.PASS;
		} catch (FindFailed e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not found !" + e.getMessage());
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not on screen !";
		}
	}
	
	public static String iMouseHover(String object, String data) {
		
		APP_LOGS.debug("[info] Executing: |iMouseHover | " + OBJECT.getProperty(object) + " | ");
			
			try{
				_screen.hover(OBJECT.getProperty(object));
				return Const.PASS;
			}catch (FindFailed e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !" + e.getMessage());
				return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not on screen !";
			}
	}

	public static String iMouseHoverAt(String object, String data) {
		Pattern pattern = new Pattern(OBJECT.getProperty(object));
		String Data[] = data.split(",");
		int x =   Integer.parseInt(Data[0]);
		int y =   Integer.parseInt(Data[1]);
		APP_LOGS.debug("[info] Executing: |iMouseHoverAt | " + OBJECT.getProperty(object) + "(" + data + ") | ");
			
			try{
				_screen.hover(pattern.targetOffset(x, y));
				return Const.PASS;
			}catch (FindFailed e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + OBJECT.getProperty(object) + " not on screen !" + e.getMessage());
				return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not on screen !";
			}
	}

	public static String iMouseUp(String object, String data) {
	
		APP_LOGS.debug("[info] Executing: |iMouseUp |");
			
			try{
				_screen.mouseUp(Button.LEFT);
				return Const.PASS;
			}catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + object + " not on screen !" + e.getMessage());
				return Const.FAIL + ": Element " + object + " not on screen !";
			}
	}

	public static String iMouseRightUp(String object, String data) {
	
		APP_LOGS.debug("[info] Executing: |iMouseRightUp |");
			
			try{
				_screen.mouseUp(Button.RIGHT);
				return Const.PASS;
			}catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + object + " not on screen !" + e.getMessage());
				return Const.FAIL + ": Element " + object + " not on screen !";
			}
	}

	public static String iMouseRightDown(String object, String data) {
	
		APP_LOGS.debug("[info] Executing: |iMouseRightDown | " + OBJECT.getProperty(object) + " | ");
			
			try{
				_screen.mouseDown(Button.RIGHT);
				return Const.PASS;
			}catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + object + " not on screen !" + e.getMessage());
				return Const.FAIL + ": Element " + object + " not on screen !";
			}
	}

	public static String iRightClick(String object, String data) {
	
		APP_LOGS.debug("[info] Executing: |iRightClick | " + OBJECT.getProperty(object) + " | ");
			
			try{
				_screen.rightClick(OBJECT.getProperty(object));
				return Const.PASS;
			}catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + object + " not on screen !" + e.getMessage());
				return Const.FAIL + ": Element " + object + " not on screen !";
			}
	}

	public static String iRightClickAt(String object, String data) {

		APP_LOGS.debug("[info] Executing: |iRightClickAt | " + OBJECT.getProperty(object) + "(" + data + ") | ");
		
			Pattern pattern = new Pattern(OBJECT.getProperty(object));
			String Data[] = data.split(",");
			int x =   Integer.parseInt(Data[0]);
			int y =   Integer.parseInt(Data[1]);
				
			try{
				_screen.rightClick(pattern.targetOffset(x, y));
				return Const.PASS;
			}catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + object + " not on screen !" + e.getMessage());
				return Const.FAIL + ": Element " + object + " not on screen !";
			}
	}

	public static String verifyImageOnScreen(String object, String data) {

		APP_LOGS.debug("[info] Executing: |verifyImageOnScreen " + OBJECT.getProperty(object) + " image on screen.|\n");
		
		Pattern pattern = new Pattern(OBJECT.getProperty(object));
		Match result = _screen.exists(pattern.similar((float)0.70));
			
			try{
				if (result!= null) {
					return Const.PASS;
				}
				return Const.FAIL + ": Element " + object + " not on screen !";
			}catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + object + " not on screen !" + e.getMessage());
				return Const.FAIL + ": Element " + object + " not on screen !";
			}
	}
	
	public static String verifyFileExist(String object, String data) {

		APP_LOGS.debug("[info] Executing: |verifyFileExist: " + data + " exist on system.|\n");
		
		String fileName = System.getProperty("user.home") + "\\Downloads\\" + data;
		Excel_Reader templateFile = new Excel_Reader(fileName);
		try{
				if (templateFile.getCellData("Sheet1", 0, 1).equals("Depth") && templateFile.getCellData("Sheet1", 1, 1).equals("Pore pressuree")) {
				return Const.PASS;
			}
				return Const.FAIL + ": Can't download " + object + " not on screen !";
			}catch (Exception e) {
				APP_LOGS.debug("[>>>> ERROR <<<] Element " + data + " not on system !" + e.getMessage());
				return Const.FAIL + ": Element " + object + " not on screen !";
			}
	}

	/**
	 * special keyword for Grid on PS page
	 * */
	public static String type_column(String object, String data) {
		
		APP_LOGS.debug("[info] Executing: |Enter value to cloumn in the pressure plot grid |");
		String Data[] = data.split(",");
		
		try{
			for (int K = 0; K < Data.length; K++) {
				_driver.findElement(formats(OBJECT.getProperty(object))).sendKeys(Data[K]);
				_driver.findElement(formats(OBJECT.getProperty(object))).sendKeys(Key.ENTER);
			} 
			return Const.PASS;
		}catch(WebDriverException e) {
			return Const.FAIL + ": Element " + OBJECT.getProperty(object) + " not found !";
		}
	} 

}