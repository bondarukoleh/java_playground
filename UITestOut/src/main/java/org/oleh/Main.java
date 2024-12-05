package org.oleh;

import com.microsoft.playwright.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

    }

    private void createPWFromBinary() {
            // Create a Playwright instance
            Playwright playwright = Playwright.create();

            // Get the path to the Chrome driver
            String workingDirectory = System.getProperty("user.dir");
            // I had to copy everything in C:\Users\myUserName\AppData\Local\ms-playwright\chromium-1060\chrome-win to ArchivedPlaywrightDrivers
            Path driverPathOfChromium = Paths.get(workingDirectory, "chromium-1148\\chrome-win\\chrome.exe");
            // Set the path to the Chrome driver executable

            System.out.println(driverPathOfChromium);

//            // Launch the Chrome browser using the custom driver path, with none-headless mode.
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setExecutablePath(driverPathOfChromium).setHeadless(false).setSlowMo(50));
//            // Create a new page
            Page page = browser.newPage();
//            // Open a web page
            page.navigate("https://www.google.com");
//            Thread.sleep(25000);
//            // Close the browser
            browser.close();
//            // Close the Playwright instance
            playwright.close();
    }
}