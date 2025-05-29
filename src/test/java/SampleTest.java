import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.WaitForSelectorState;
import ge.configs.BstackRunner;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SampleTest extends BstackRunner {

    @Test(priority = 1)
    public void firstStep() {
        page.navigate("https://tbcganvadeba.ge/");
        page.waitForTimeout(4000);
        Locator button = page.locator("//app-cookies//button[contains(text(),'თანხმობა')]");
        assertThat(button).isVisible();
        button.click();
        page.locator("a[href='/offers'] div").click();
        assertThat(page.locator("app-offers div.offer-list")).isVisible();
    }

    @Test(priority = 2)
    public void secondStep() {
        page.waitForTimeout(5000);
        assertThat(page.locator("tbcx-pw-card").all().get(0)).isVisible();
        page.locator("tbcx-pw-card").all().get(0).click();
        assertThat(page.locator("//app-offer-details//div[@class='merchant-details']")).isVisible();
    }
}
