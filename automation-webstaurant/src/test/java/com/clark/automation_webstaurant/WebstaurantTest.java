package com.clark.automation_webstaurant;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebstaurantTest {

    private WebDriver driver;
    //private WebstaurantWindowHandler handler;
    List<String> listofSearchResultsNotContainingTable;
    List<WebElement> searchResults;
    final int implicitWaitTimeSeconds = 10;
    WebDriverWait wait;

    @Before
    public void setUp() {
        // Set up WebDriver
    	ChromeOptions options = new ChromeOptions();
    	options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        System.setProperty("webdriver.chrome.driver", "/Users/tejakunju/eclipse-workspace/Webstaurant Project/automation-webstaurant/chromedriver");
    	driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 20);

        driver.manage().timeouts().implicitlyWait(implicitWaitTimeSeconds, TimeUnit.SECONDS);
    	//handler = new WebstaurantWindowHandler();
    }

    @After
    public void tearDown() {
        // Quit WebDriver
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSearchAndAddToCart() {
        // 1. Go to https://www.webstaurantstore.com/
        driver.get("https://www.webstaurantstore.com/");

        // 2. Search for 'stainless work table'.
        //WebElement searchBox = driver.findElement(By.id("searchval"));
        WebElement searchBox = driver.findElement(By.xpath("//div[contains(@class,'hidden flex-1 lt:flex')]//input[@id='searchval']"));
        searchBox.sendKeys("stainless work table");
        searchBox.submit();
 
        // 3. Check the search result ensuring every product has the word 'Table' in its title.
        searchResults = driver.findElements(By.xpath("(//div[@class='ag-item p-2 xs:p-4'])"));
        if(searchResults != null && searchResults.size() > 0) {
        	listofSearchResultsNotContainingTable = new ArrayList<String>();
        }
        
        for (WebElement result : searchResults) {
            String title = result.getText();
            //System.out.println(title);
            if (!title.contains("Table")) {
            	listofSearchResultsNotContainingTable.add(title);
            	//System.out.println("Not Found");
            } else {
            	//System.out.println("Found");
            }
        }
        if(listofSearchResultsNotContainingTable != null && listofSearchResultsNotContainingTable.size() > 0) {
        	System.out.println("Following titles do not contain 'Table'");
        	for(String listofResults: listofSearchResultsNotContainingTable) {
        		System.out.println(listofResults);
        	}
        }

        
        // Re-wrote the below code again as the requirement mentioned about search results 
        // but not sure if that included pagination portion too
        // 3. Check the search result ensuring every product has the word 'Table' in its title.
        // Performed this for 1st page of search results. Can loop this till the last page and 
        // do the same check for 'Table' keyword in all the results.
        if(searchResults != null && searchResults.size() > 0) {
        	listofSearchResultsNotContainingTable = new ArrayList<String>();
        }
        searchResults = driver.findElements(By.xpath("(//div[@id='ProductBoxContainer'])"));
        for (WebElement result : searchResults) {
            String title = result.getText();
            System.out.println("Title:");
            System.out.println(title);
            if (!title.contains("Table")) {
            	listofSearchResultsNotContainingTable.add(title);
            	//System.out.println("Not Found");
            } else {
            	//System.out.println("Found");
            }
        }
        if(listofSearchResultsNotContainingTable != null && listofSearchResultsNotContainingTable.size() > 0) {
        	System.out.println("Following titles do not contain 'Table'");
        	for(String listofResults: listofSearchResultsNotContainingTable) {
        		//System.out.println(listofResults);
        	}
        }
 

        // 4. Add the last item from the search results on the last page to the Cart.
        String xpathExpression = "//a[contains(@aria-label, 'go to page 2')]";
        WebElement traversePageButton = driver.findElement(By.xpath(xpathExpression));
        traversePageButton.click();
        while(true) {
	        try {
	            xpathExpression = "(//*[name()='svg'][@class='h-3 w-3'])[2]";
	        	traversePageButton = driver.findElement(By.xpath(xpathExpression));
	            traversePageButton.click();
	            //System.out.println("Reached this page ");
	        } catch(NoSuchElementException e) {
	        	System.out.println("Reached the last page ");
	        	break;
	        }
//	        Actions actions = new Actions(driver);
//	        actions.sendKeys(org.openqa.selenium.Keys.END).perform();
	        long scrollHeight = (Long) ((JavascriptExecutor) driver).executeScript("return Math.ceil(document.documentElement.scrollHeight * 0.8);");
	        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, arguments[0]);", scrollHeight);

	        //driver.manage().timeouts().implicitlyWait(implicitWaitTimeSeconds, TimeUnit.SECONDS);
    	}
        searchResults = driver.findElements(By.xpath("(//div[@id='ProductBoxContainer'])"));
        System.out.println(searchResults.size());
 
        // adding the last item to the cart (last item is out of stock so going with n-1)
        String xPathForAddtoCart //= "(//input[@name='addToCartButton'])[59]";
         = "(//input[@name='addToCartButton'])["+ (searchResults.size() - 1) +"]";
        System.out.println(xPathForAddtoCart);
        WebElement addLastItemToCart = driver.findElement(By.xpath(xPathForAddtoCart));
        addLastItemToCart.click();

        WebElement viewCartElement = driver.findElement(By.xpath("(//a[normalize-space()='View Cart'])[1]"));
        viewCartElement.click();
 
        // 5. Empty Cart.
        // Switch to the pop up handle window
        //String mainWindowHandle = driver.getWindowHandle();
        ////*[name()='path' and contains(@d,'M247.244,1')]
        // (//*[name()='path'])[84]
        //WebElement emptyCartElement = driver.findElement(By.xpath("//button[normalize-space()='Empty Cart']"));
        WebElement emptyCartElement = driver.findElement(By.xpath("(//*[name()='path'])[84]"));
        //WebElement emptyCartElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("path")));  
        		//By.xpath("//*[name()='path' and contains(@d,'M247.244,1')]")));

        emptyCartElement.click();
 
        //wait for the popup
        //handler.waitForPopup(driver);

        // switch to pop up window
        //handler.switchToPopupWindow(driver);
        
        // Confirmation Screen
        //emptyCartElement = driver.findElement(By.xpath("//button[contains(text(),'Empty Cart')]"));
//        emptyCartElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Empty Cart')]")));
//        emptyCartElement.click();
//        driver.manage().timeouts().implicitlyWait(implicitWaitTimeSeconds, TimeUnit.SECONDS);
        
        // Switch back to the main window
        //driver.switchTo().window(mainWindowHandle);
        
        WebElement emptyCartMsgConfirmation = driver.findElement(By.xpath("(//p[@class='header-1'])[1]"));
        //System.out.println("Msg: " + emptyCartMsgConfirmation.getText());
        assertEquals("Your cart is empty.", emptyCartMsgConfirmation.getText());
     
        driver.manage().timeouts().implicitlyWait(implicitWaitTimeSeconds, TimeUnit.SECONDS);
 
    }
    


}
