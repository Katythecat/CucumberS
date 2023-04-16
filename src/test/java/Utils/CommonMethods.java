package Utils;

import StepDefinitions.PageInitializer;
import io.cucumber.java.eo.Se;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;


public class CommonMethods extends PageInitializer {
    public static WebDriver driver;
    public static void openBrowserAndLaunchApplication() {
        ConfigReader.readProperties();

        String browserType=ConfigReader.getPropertyValue("browserType");
        switch (browserType){
            case "Chrome":
                driver=new ChromeDriver(); break;
            case "Firefox" :
                driver=new FirefoxDriver(); break;
            case "Edge" :
                driver=new EdgeDriver(); break;
        }
        driver.manage().window().maximize();
        driver.get(ConfigReader.getPropertyValue("url"));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(Constants.WAIT_TIME));
        initializePageObjects();
        // this will initialize all the pages we have in our page
        // PageInitializer class along with the launching of application
    }

    public static void closeBrowser(){
        driver.close();
    }

    public static void doClick(WebElement element){
        element.click();
    }

    public static void sendText(WebElement element, String text){
        element.clear();
        element.sendKeys(text);
    }

    public static Select clickOnDropdown(WebElement element){
        Select select=new Select(element);
        return select;

    }

    public static void selectByValue(WebElement element,String value){
        clickOnDropdown(element).selectByValue(value);

    }
    public static void selectByText(WebElement element, String text){
        clickOnDropdown(element).selectByVisibleText(text);
    }
    public static void selectByIndex(WebElement element, int index){
        clickOnDropdown(element).selectByIndex(index);
    }

    public static void selectByOptions(WebElement element,String text){
        List<WebElement> options = clickOnDropdown(element).getOptions();
        for(WebElement option:options){
            String ddlOptionText=option.getText();
            if(ddlOptionText.equalsIgnoreCase(text)){
                option.click();
            }
        }

    }

    public static byte[] takeScreenshot(String imageName){
        // remote webdriver is a class implement TakeScreenshot interface
        TakesScreenshot ts=(TakesScreenshot) driver; //type casting

        //This capture the screenshot and store it as byte array
        byte[] picBytes = ts.getScreenshotAs(OutputType.BYTES);
        //This capture the screenshot and stores it as a file in the sourceFile variable
        File sourceFile = ts.getScreenshotAs(OutputType.FILE);

        try { //The "FileUtils.copyFile" method is called to copy the "File" object to a new file location.
            FileUtils.copyFile(sourceFile,
                    new File(Constants.SCREENSHOT_FILEPATH+ imageName+" "+
                            getTimeStamp("yyyy-MM-dd-HH-mm-ss")+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return picBytes;
    }

    public static String getTimeStamp(String pattern){
        Date date=new Date();
        SimpleDateFormat sdm=new SimpleDateFormat(pattern);

        return sdm.format(date);
    }

}
