package ge.configs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.internal.TestResult;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BstackRunner {
    private static Map<String, Machine> browserStackJSonMap;
    protected Playwright playwright;
    protected Browser browser;
    BrowserContext context;
    protected Page page;

    public BstackRunner() {
        File file = new File("Settings.json");
        browserStackJSonMap = convertJsonFileToMap(file, new HashMap<>());
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser"})
    protected void launchBrowser(@Optional  String browserName) {
        playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        HashMap<String, Object> capabilitiesObject = new HashMap<>();
        Machine machine;
        if(browserName != null) {
            machine = browserStackJSonMap.get(browserName);
        } else {
            machine = browserStackJSonMap.get("chrome");
        }
        capabilitiesObject.put("browser", machine.getBrowser());
        capabilitiesObject.put("browser_version", machine.getBrowserVersion());
        capabilitiesObject.put("os", machine.getOs());
        capabilitiesObject.put("os_version", machine.getOsVersion());
        capabilitiesObject.put("name", machine.getName());
//        capabilitiesObject.put("build", machine.getBuild());
        capabilitiesObject.put("build", System.getenv("BROWSERSTACK_BUILD_NAME"));
        capabilitiesObject.put("browserstack.username", "salomelicheli_8HlXDQ");
        capabilitiesObject.put("browserstack.accessKey", "AP7sxHYTsZVEpLGFhqYJ");
        String capabilities = null;
        JSONObject jsonCaps = new JSONObject(capabilitiesObject);
        try {
            capabilities = URLEncoder.encode(jsonCaps.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String wsEndpoint = "wss://cdp.browserstack.com/playwright?caps=" + capabilities;
        browser = browserType.connect(wsEndpoint);
        context = browser.newContext(new Browser
                .NewContextOptions()
                .setViewportSize(1920, 1080));
        page = context.newPage();
        page.evaluate("window.moveTo(0, 0); window.resizeTo(screen.width, screen.height);");
    }

    @AfterClass(alwaysRun = true)
    protected void closeContext() {
        if(page != null) {
            page.close();
        }
        if(context != null) {
            context.close();
        }
        if(browser != null) {
            browser.close();
        }
        if(playwright != null) {
            playwright.close();
        }
    }

    @AfterClass(alwaysRun = true)
    protected void adjustStatus(ITestContext result) {
        if(!result.getFailedTests().getAllResults().isEmpty()) {
            changeStatus("failed");
        }
        else {
            changeStatus("passed");
        }
    }

    private void changeStatus(String status) {
        page.evaluate("_ => {}", "browserstack_executor: { \"action\": \"setSessionStatus\", \"arguments\": { \"status\": \"" + status + "\"}}");
    }

    private Map<String, Machine> convertJsonFileToMap(File jsonFile, Map<String, Machine> map) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Machine> machines = mapper.readValue(jsonFile, new TypeReference<List<Machine>>() {});
            map = new HashMap<>();
            for (Machine machine : machines) {
                map.put(machine.getBrowser(), machine);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Malformed Settings.json file - %s.", e));
        }
        return map;
    }
}
