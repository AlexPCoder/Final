import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class UITestsFireFox {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.manage().window().maximize();
    }

    @Test
    public void FillTheForm() {
        driver.get("https://demoqa.com/elements");
        WebElement TextBoxBtn = driver.findElement(By.xpath("//div[@class='element-list collapse show']/ul[@class='menu-list']/li[@id='item-0']"));
        TextBoxBtn.click();

        WebElement FullNameField = driver.findElement(By.xpath("//*[@id='userName']"));
        FullNameField.sendKeys("Dart Weider");

        WebElement UserEmailField = driver.findElement(By.xpath("//*[@id='userEmail']"));
        UserEmailField.sendKeys("DartWeider@email.com");

        WebElement CurrentAddressField = driver.findElement(By.xpath("//*[@id='currentAddress']"));
        CurrentAddressField.sendKeys("Current Address");

        WebElement PermanentAddressField = driver.findElement(By.xpath("//*[@id='permanentAddress']"));
        PermanentAddressField.sendKeys("Permanent Address");

        WebElement SubmitBtn = driver.findElement(By.xpath("//*[@id='submit']"));
        //new Actions(driver).scrollToElement(SubmitBtn).perform();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        SubmitBtn.click();

        WebElement Output = driver.findElement(By.xpath("//*[@id='output']"));
        boolean isOutputDisplayed = Output.isDisplayed();
        Assert.assertTrue(isOutputDisplayed, "Output is not displayed");

        String OutPutText = Output.getText();
        Assert.assertTrue(OutPutText.contains("Name:Dart Weider"), "Name is not displayed");
        Assert.assertTrue(OutPutText.contains("Email:DartWeider@email.com"), "Email is not displayed");
        Assert.assertTrue(OutPutText.contains("Current Address :Current Address"), "Current Address is not displayed");
        Assert.assertTrue(OutPutText.contains("Permananet Address :Permanent Address"), "Permanent Address is not displayed");
    }

    @Test
    public void UseCheckBoxes() {
        driver.get("https://demoqa.com/elements");
        WebElement TextBoxBtn = driver.findElement(By.xpath("//div[@class='element-list collapse show']/ul[@class='menu-list']/li[@id='item-1']"));
        TextBoxBtn.click();

        WebElement ExpandHomeButton = driver.findElement(By.xpath("//*[@id=\"tree-node\"]/ol/li/span/button"));
        ExpandHomeButton.click();

        WebElement ExpandDownloadsButton = driver.findElement(By.xpath("//*[@id=\"tree-node\"]/ol/li/ol/li[3]/span/button"));
        ExpandDownloadsButton.click();

        WebElement CheckBoxExcelFile = driver.findElement(By.xpath("//*[@id=\"tree-node\"]/ol/li/ol/li[3]/ol/li[2]/span/label/span[1]"));
        CheckBoxExcelFile.click();

        WebElement Output = driver.findElement(By.xpath("//*[@id=\"result\"]"));
        boolean isOutputDisplayed = Output.isDisplayed();
        Assert.assertTrue(isOutputDisplayed, "Output is not displayed");

        String OutPutText = Output.getText();
        Assert.assertTrue(OutPutText.contains("excelFile"), "File name is not displayed");

    }

    @Test
    public void CreateUser() throws InterruptedException {
        driver.get("https://demoqa.com/elements");
        WebElement WebTablesButton = driver.findElement(By.xpath("//div[@class='element-list collapse show']/ul[@class='menu-list']/li[@id='item-3']"));
        WebTablesButton.click();

        WebElement AddButton = driver.findElement(By.xpath("//*[@id=\"addNewRecordButton\"]"));
        AddButton.click();

        WebElement FirstNameField = driver.findElement(By.xpath("//*[@id=\"firstName\"]"));
        FirstNameField.sendKeys("Dart Weider");

        WebElement LastNameField = driver.findElement(By.xpath("//*[@id=\"lastName\"]"));
        LastNameField.sendKeys("Dart Weider");

        WebElement UserEmailField = driver.findElement(By.xpath("//*[@id=\"userEmail\"]"));
        UserEmailField.sendKeys("DartWeider@email.com");

        WebElement AgeField = driver.findElement(By.xpath("//*[@id=\"age\"]"));
        AgeField.sendKeys("17");

        WebElement SalaryField = driver.findElement(By.xpath("//*[@id=\"salary\"]"));
        SalaryField.sendKeys("10000");

        WebElement DepartmentField = driver.findElement(By.xpath("//*[@id=\"department\"]"));
        DepartmentField.sendKeys("Engineering");

        WebElement SubmitButton = driver.findElement(By.xpath("//*[@id=\"submit\"]"));
        SubmitButton.click();

        List<WebElement> cells = driver.findElements(By.xpath("//div[@class=\"rt-tbody\"]//div[@class=\"rt-td\"]"));

        List<WebElement> filteredElements = cells.stream()
                .filter(element -> element.getText().equalsIgnoreCase("DartWeider@email.com"))
                .collect(Collectors.toList());
        int Size = filteredElements.size();

        Assert.assertTrue(Size == 1, "Created user not found");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
