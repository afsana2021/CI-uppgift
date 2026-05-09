
import io.github.bonigarcia.wdm.WebDriverManager;
        import org.junit.jupiter.api.*;
        import org.openqa.selenium.By;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.chrome.ChromeDriver;
        import org.openqa.selenium.chrome.ChromeOptions;

        import static org.junit.jupiter.api.Assertions.*;

public class SeleniumTest {

    WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // required for CI
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.get("https://www.saucedemo.com/");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // 1. Valid login
    @Test
    void testValidLogin() {
        login("standard_user", "secret_sauce");
        assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    // 2. Invalid password
    @Test
    void testInvalidPassword() {
        login("standard_user", "wrong_password");

        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        assertTrue(error.contains("Username and password do not match"));
    }

    // 3. Locked user
    @Test
    void testLockedUser() {
        login("locked_out_user", "secret_sauce");

        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        assertTrue(error.contains("locked out"));
    }

    //  4. Empty login
    @Test
    void testEmptyLogin() {
        driver.findElement(By.id("login-button")).click();

        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        assertTrue(error.contains("Username is required"));
    }

    // Reusable login method (IMPORTANT)
    void login(String username, String password) {
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }
}