import com.microsoft.playwright.*;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EmulatorSample {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass(alwaysRun = true)
    protected void launchBrowser() {
        playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        HashMap<String, Object> capabilitiesObject = new HashMap<>();
        capabilitiesObject.put("browser", "playwright-webkit");
        capabilitiesObject.put("browser_version", "latest");
        capabilitiesObject.put("name", "Test on Playwright emulated Devices");
        capabilitiesObject.put("build", "playwright-java-4");
        capabilitiesObject.put("browserstack.username", "salomelicheli_8HlXDQ");
        capabilitiesObject.put("browserstack.accessKey", "AP7sxHYTsZVEpLGFhqYJ");
//        capabilitiesObject.put("os", "ios");
//        capabilitiesObject.put("os_version", "15");
//        capabilitiesObject.put("device", "iPhone 13");
//        capabilitiesObject.put("real_mobile", "true");
        String capabilities = null;
        JSONObject jsonCaps = new JSONObject(capabilitiesObject);
        try {
            capabilities = URLEncoder.encode(jsonCaps.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String wsEndpoint = "wss://cdp.browserstack.com/playwright?caps=" + capabilities;
        browser = browserType.connect(wsEndpoint);
        context = browser.newContext(new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0 Mobile/15E148 Safari/604.1")
                .setViewportSize(375, 812)
                .setScreenSize(1920, 1080)
                .setDeviceScaleFactor(3)
                .setIsMobile(true)
                .setHasTouch(true));
        page = context.newPage();
    }

    @Test
    public void testName() {
        page.navigate("https://tbcganvadeba.ge/");
        page.waitForTimeout(4000);
        Locator button = page.locator("//app-cookies//button[contains(text(),'თანხმობა')]");
        assertThat(button).isVisible();
        button.click();
        page.locator("a[href='/offers'] div").click();
        assertThat(page.locator("app-offers div.offer-list")).isVisible();
    }
}
