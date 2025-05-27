import com.microsoft.playwright.Locator;
import ge.configs.BstackRunner;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SampleTest extends BstackRunner {

    @Test
    public void testName() {
        page.navigate("https://tbcganvadeba.ge/");
        page.waitForTimeout(4000);
        Locator button = page.locator("//app-cookies//button[contains(text(),'თანხმობა')]");
        assertThat(button).isVisible();
        button.click();
        page.locator("a[href='/offers'] div").click();
        assertThat(page.locator("app-offers div.offer-list")).isVisible();
        System.out.println("Title: " + page.title());
    }
}
