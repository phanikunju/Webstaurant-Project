package com.clark.automation_webstaurant;

import java.util.Set;

import org.openqa.selenium.WebDriver;

/**
 * Hello world!
 *
 */
public class WebstaurantWindowHandler
{
    public void waitForPopup(WebDriver driver) {
        int attempts = 0;
        while (driver.getWindowHandles().size() == 1 && attempts < 10) {
            try {
                Thread.sleep(1000); // Adjust the wait time as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            attempts++;
        }
    }
    
    public void switchToPopupWindow(WebDriver driver) {
        Set<String> windowHandles = driver.getWindowHandles();
        System.out.println("Size: " + windowHandles.size());
        for (String handle : windowHandles) {
        	System.out.println("Handle " + handle);
            if (!handle.equals(driver.getWindowHandle())) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
