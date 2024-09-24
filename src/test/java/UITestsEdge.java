import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UITestsEdge {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {

        // Automatic WebDriver management using WebDriverManager
        //WebDriverManager.edgedriver().setup();

        // EdgeOptions initialization
        EdgeOptions options = new EdgeOptions();

        // Initialize Edge driver
        driver = new EdgeDriver(options);

        // Set timeouts and maximize the window
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.manage().window().maximize();

        // Initialize WebDriverWait
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    @Test
    public void FillTheForm() {
        driver.get("https://demoqa.com/elements");
        WebElement TextBoxBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='element-list collapse show']/ul[@class='menu-list']/li[@id='item-0']")));
        TextBoxBtn.click();

        WebElement FullNameField = driver.findElement(By.id("userName"));
        FullNameField.sendKeys("Dart Weider");

        WebElement UserEmailField = driver.findElement(By.id("userEmail"));
        UserEmailField.sendKeys("DartWeider@email.com");

        WebElement CurrentAddressField = driver.findElement(By.id("currentAddress"));
        CurrentAddressField.sendKeys("Current Address");

        WebElement PermanentAddressField = driver.findElement(By.id("permanentAddress"));
        PermanentAddressField.sendKeys("Permanent Address");

        WebElement SubmitBtn = driver.findElement(By.id("submit"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        SubmitBtn.click();

        WebElement Output = driver.findElement(By.id("output"));
        Assert.assertTrue(Output.isDisplayed(), "Output is not displayed");

        String OutPutText = Output.getText();
        Assert.assertTrue(OutPutText.contains("Name:Dart Weider"), "Name is not displayed");
        Assert.assertTrue(OutPutText.contains("Email:DartWeider@email.com"), "Email is not displayed");
        Assert.assertTrue(OutPutText.contains("Current Address :Current Address"), "Current Address is not displayed");
        Assert.assertTrue(OutPutText.contains("Permananet Address :Permanent Address"), "Permanent Address is not displayed");;
    }

    @Test
    public void UseCheckBoxes() {
        driver.get("https://demoqa.com/elements");
        WebElement TextBoxBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='element-list collapse show']/ul[@class='menu-list']/li[@id='item-1']")));
        TextBoxBtn.click();

        WebElement ExpandHomeButton = driver.findElement(By.xpath("//*[@id=\"tree-node\"]/ol/li/span/button"));
        ExpandHomeButton.click();

        WebElement ExpandDownloadsButton = driver.findElement(By.xpath("//*[@id=\"tree-node\"]/ol/li/ol/li[3]/span/button"));
        ExpandDownloadsButton.click();

        WebElement CheckBoxExcelFile = driver.findElement(By.xpath("//*[@id=\"tree-node\"]/ol/li/ol/li[3]/ol/li[2]/span/label/span[1]"));
        CheckBoxExcelFile.click();

        WebElement Output = driver.findElement(By.id("result"));
        Assert.assertTrue(Output.isDisplayed(), "Output is not displayed");

        String OutPutText = Output.getText();
        Assert.assertTrue(OutPutText.contains("excelFile"), "File name is not displayed");
    }

    @Test
    public void CreateUser() {
        driver.get("https://demoqa.com/elements");
        WebElement WebTablesButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='element-list collapse show']/ul[@class='menu-list']/li[@id='item-3']")));
        WebTablesButton.click();

        WebElement AddButton = driver.findElement(By.id("addNewRecordButton"));
        AddButton.click();

        WebElement FirstNameField = driver.findElement(By.id("firstName"));
        FirstNameField.sendKeys("Dart Weider");

        WebElement LastNameField = driver.findElement(By.id("lastName"));
        LastNameField.sendKeys("Dart Weider");

        WebElement UserEmailField = driver.findElement(By.id("userEmail"));
        UserEmailField.sendKeys("DartWeider@email.com");

        WebElement AgeField = driver.findElement(By.id("age"));
        AgeField.sendKeys("17");

        WebElement SalaryField = driver.findElement(By.id("salary"));
        SalaryField.sendKeys("10000");

        WebElement DepartmentField = driver.findElement(By.id("department"));
        DepartmentField.sendKeys("Engineering");

        WebElement SubmitButton = driver.findElement(By.id("submit"));
        SubmitButton.click();

        List<WebElement> cells = driver.findElements(By.xpath("//div[@class='rt-tbody']//div[@class='rt-td']"));

        List<WebElement> filteredElements = cells.stream()
                .filter(element -> element.getText().equalsIgnoreCase("DartWeider@email.com"))
                .collect(Collectors.toList());

        Assert.assertTrue(filteredElements.size() == 1, "Created user not found");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
