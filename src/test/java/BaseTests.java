import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;

public class BaseTests {
    private WebDriver driver;
    public void setUp(){
        try{
            System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
            driver = new ChromeDriver();
            driver.get("https://subscribe.stctv.com/");
            driver.manage().window().maximize();
            validateSubscriptionPackages(driver, "SA", 1);// SA, KW or BHR
            validateSubscriptionPackages(driver, "KW", 2);
            validateSubscriptionPackages(driver, "BHR", 3);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
    private static void validateSubscriptionPackages(WebDriver driver, String countryCode, Integer countryOrder) {
        // Select the country
        WebElement countriesButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("country")));
        if(countryOrder == 1) {
            countriesButton.click();
        }
        boolean elementExists = checkIfElementExists(driver, countryCode);
        Assert.assertEquals(elementExists, true);
        WebElement chosenCountry = driver.findElement(By.id(countryCode.toLowerCase(Locale.ROOT)));
        chosenCountry.click();
        String actualURL = driver.getCurrentUrl();
        String expectedURL = "https://subscribe.jawwy.tv/" + countryCode.toLowerCase(Locale.ROOT)+"-ar";
        Assert.assertEquals("The URL does not match the expected value.", expectedURL, actualURL);
        // Validate the subscription packages - Types Exist
        elementExists = checkIfElementExists(driver, "name-لايت");
        Assert.assertEquals(elementExists, true);
        elementExists = checkIfElementExists(driver, "لايت-selection");
        Assert.assertEquals(elementExists, true);
        //currency-لايت
        elementExists = checkIfElementExists(driver, "name-الأساسية");
        Assert.assertEquals(elementExists, true);
        elementExists = checkIfElementExists(driver, "الأساسية-selection");
        Assert.assertEquals(elementExists, true);
        elementExists = checkIfElementExists(driver, "name-بريميوم");
        Assert.assertEquals(elementExists, true);
        elementExists = checkIfElementExists(driver, "selection-بريميوم");
        Assert.assertEquals(elementExists, true);
        //Validate the Currency
        elementExists = checkIfElementExists(driver, "currency-لايت");
        Assert.assertEquals(elementExists, true);
        elementExists = checkIfElementExists(driver, "currency-بريميوم");
        Assert.assertEquals(elementExists, true);
        elementExists = checkIfElementExists(driver, "currency-الأساسية");
        Assert.assertEquals(elementExists, true);
    }
    public static boolean checkIfElementExists(WebDriver driver, String elementString){
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id(elementString.toLowerCase(Locale.ROOT))));
            if (element.isDisplayed()) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            System.out.println("Element does not exist. "+ e);
            return false;
        }
    }
    public static void main(String args[]){
        BaseTests test = new BaseTests();
        test.setUp();
    }
}
