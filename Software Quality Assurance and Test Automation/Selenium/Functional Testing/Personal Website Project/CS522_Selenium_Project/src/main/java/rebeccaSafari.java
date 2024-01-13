import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class rebeccaSafari {
    WebDriver driver;
    Actions actions;
    int sleepTime = 10;
    WebDriverWait wait;
    TakesScreenshot screenshot;
    String urlRE = "https://www.rererebeccawong.com";
    String filePath = "/Users/hiki/IdeaProjects/CS522_Selenium_Project/rebecca_safari_test_data.xlsx";
    String section_1 = "WORK";
    String section_2 = "ABOUT";
    String section_3 = "HOME";
    String homePageName = "Product designer | Rebecca Wong design portfolio";
    String workPageName = "Product designer | Rebecca Wong design portfolio";
    JavascriptExecutor executor;
    private static final Logger logger = LogManager.getLogger(rebeccaSafari.class);

    public rebeccaSafari() {
    }

    @BeforeTest
    public void setUp() {
        driver = new SafariDriver();
        executor = (JavascriptExecutor)driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(sleepTime));
        actions = new Actions(driver);
        screenshot = (TakesScreenshot)driver;
        driver.manage().timeouts().implicitlyWait(sleepTime, TimeUnit.SECONDS);
        driver.get(urlRE);
        driver.manage().window().maximize();
        logger.info("Setting up the test...");
    }

    @Test(priority = 1, enabled = true)
    public void checkWork() {
        menuWork();
        takeScreenshot("checkWork");
    }

    @Test(dependsOnMethods = "checkWork", priority = 2, enabled = true)
    public void SpaceDesign() {
        String cssS_1 = search("SpaceDesign_1");//get CSS Selector from xlsx sheet
        WebElement restaurantButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_1)));
        //use JavaScript to click the restaurant button for reliability
        executor.executeScript("arguments[0].click();", restaurantButton);
        logger.info("Checking SpaceDesign...");
        String originalWindow = driver.getWindowHandle();//store the original tab
        takeScreenshot("SpaceDesign_1");
        String cssS_2 = search("SpaceDesign_2");//get CSS Selector from xlsx sheet
        //wait WebElement button tobe clickable
        WebElement yelpButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_2)));
        //use JavaScript to click the yelp button for reliability
        executor.executeScript("arguments[0].click();", yelpButton);
        logger.info("View restaurant on Yelp...");
        Set<String> windowHandles = driver.getWindowHandles();//store the current tab

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(originalWindow)) {//check if the current tab is the original tab
                driver.switchTo().window(windowHandle);//select the current tab
                takeScreenshot("SpaceDesign_2");
                driver.close();//close tab
                logger.info("Yelp close...");
                break;
            }
        }
        driver.switchTo().window(originalWindow);
        String cssS_3 = search("SpaceDesign_viewMore");
        WebElement viewMore = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_3)));
        //use JavaScript to click the view more button for reliability
        executor.executeScript("arguments[0].click();", viewMore);
        logger.info("Go back to the WORK...");
    }

    @Test(dependsOnMethods = "checkWork", priority = 3, enabled = true)
    public void MapUp() {
        String cssS_1 = search("MapUp");
        WebElement mapupButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_1)));
        executor.executeScript("arguments[0].click();", mapupButton);
        logger.info("Checking MapUp...");
        takeScreenshot("MapUp");
        String cssS_2 = search("MapUp_viewMore");
        WebElement viewMore = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_2)));
        executor.executeScript("arguments[0].click();", viewMore);
        logger.info("Go back to the WORK...");
    }

    @Test(dependsOnMethods = "checkWork", priority = 4, enabled = true)
    public void OfficialWeb() {
        String cssS_1 = search("OfficialWeb");
        WebElement officialButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_1)));
        executor.executeScript("arguments[0].click();", officialButton);
        logger.info("Checking OfficialWeb...");
        takeScreenshot("OfficialWeb_1");
        String originalWindow = driver.getWindowHandle();
        String cssS_2 = search("OfficialWeb_2");
        WebElement feiyueButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_2)));
        executor.executeScript("arguments[0].click();", feiyueButton);
        logger.info("View Feiyue.Cloud website...");
        Set<String> windowHandles = driver.getWindowHandles();

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                takeScreenshot("OfficialWeb_2");
                driver.close();
                logger.info("New tab close...");
                break;
            }
        }

        driver.switchTo().window(originalWindow);
        String cssS_3 = search("OfficialWeb_viewMore");
        WebElement viewMore = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_3)));
        executor.executeScript("arguments[0].click();", viewMore);
        logger.info("Go back to the WORK...");
    }

    @Test(priority = 5, enabled = true)
    public void Resume() throws InterruptedException {
        menuAbout();
        takeScreenshot("Resume");
        String originalWindow = driver.getWindowHandle();
        String cssS_1 = search("Resume_1");
        WebElement resumeButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssS_1)));
        executor.executeScript("arguments[0].click();", resumeButton);
        logger.info("Viewing resume...");
        Set<String> windowHandles = driver.getWindowHandles();

        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                Thread.sleep(1000L);
                takeScreenshot("Resume");
                driver.close();
                logger.info("New tab close...");
                break;
            }
        }

        driver.switchTo().window(originalWindow);
        menuHome(); //test menu HOME
    }

    @AfterTest
    public void closeExit() {
        logger.info("Closing the test...");
        driver.quit();
    }

    private String search(String methodName) {
        String cssSelector = null;
        try {
            FileInputStream file = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet("Sheet1");//Data in Sheet 1

            for (Row row : sheet) {
                //First column is method name
                Cell methodNameCell = row.getCell(0);
                //Second column is CSS Selector
                Cell cssSelectorCell = row.getCell(1);
                if (methodNameCell != null && methodNameCell.getStringCellValue().equals(methodName)) {
                    cssSelector = cssSelectorCell.getStringCellValue();
                    break;
                }
            }
            workbook.close();
        } catch (IOException var10) {
            var10.printStackTrace();
        }
        logger.info("CSS selector: " + cssSelector);
        return cssSelector;
    }

    void menuWork(){
        driver.findElement(By.linkText(section_1)).click();
        logger.info("Page is in " + section_1);
        driver.getTitle();
        logger.info("Page title is " + driver.getTitle());
        if (driver.getTitle().contains(workPageName)){
            logger.info("Work Page Test Passed!");
        } else {
            logger.error("Page title is NOT " + driver.getTitle());
        }
    }

    void menuHome(){
        driver.findElement(By.linkText(section_3)).click();
        logger.info("Page is in " + section_3);
        driver.getTitle();
        logger.info("Page title is " + driver.getTitle());
        if (driver.getTitle().contains(homePageName)){
            logger.info("Home Page Test Passed!");
        } else {
            logger.error("Page title is NOT " + driver.getTitle());
        }
    }

    void menuAbout(){
        driver.findElement(By.linkText(section_2)).click();
        logger.info("Page is in " + section_2);
    }

    void takeScreenshot(String methodName) {
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        String timeStamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        String filePath = "/Users/hiki/Desktop/TestRe/" + methodName + "_" + timeStamp + ".png";
        Path destinationPath = Paths.get(filePath);

        try {
            Files.move(srcFile.toPath(), destinationPath);
            logger.info("Screenshot saved at: " + destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
