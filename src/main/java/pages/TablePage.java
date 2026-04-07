package pages;

import config.ConfigManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

public class TablePage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================
    private final By langAny = By.cssSelector("input[name='lang'][value='Any']");
    private final By langJava = By.cssSelector("input[name='lang'][value='Java']");
    private final By langPython = By.cssSelector("input[name='lang'][value='Python']");

    private final By levelBeginner = By.cssSelector("input[name='level'][value='Beginner']");
    private final By levelIntermediate = By.cssSelector("input[name='level'][value='Intermediate']");
    private final By levelAdvanced = By.cssSelector("input[name='level'][value='Advanced']");

    private final By minEnrollDropdownButton = By.cssSelector("#enrollDropdown .dropdown-button");
    private final By minEnrollMenu = By.cssSelector("#enrollDropdown .dropdown-menu");

    private By minEnrollOption(String value) {
        return By.cssSelector("#enrollDropdown .dropdown-menu li[data-value='" + value + "']");
    }

    private final By sortDropdown = By.id("sortBy");
    private final By resetButton = By.id("resetFilters");

    private final By tableRows = By.cssSelector("tbody tr");
    private final By noResultsMessage = By.id("noData");

    // ============================================================
    // Supported Values
    // ============================================================
    private static final Set<String> SUPPORTED_LANGUAGES =
            Set.of("any", "java", "python");

    private static final Set<String> SUPPORTED_MIN_ENROLLMENTS =
            Set.of("Any", "0", "1000", "5000", "10000", "20000");

    private static final Set<String> SUPPORTED_SORT_OPTIONS =
            Set.of("Course Name", "Enrollments");

    // ============================================================
    // Constructor
    // ============================================================
    public TablePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ============================================================
    // Navigation
    // ============================================================
    @Step("Open Test Table page")
    public TablePage open() {
        String base = ConfigManager.getPracticeBaseUrl().replaceAll("/+$", "");
        String url = base + "/practice-test-tables/";

        navigateTo(url);
        waitForPageLoad();
        waitForTableToSettle();
        return this;
    }

    // ============================================================
    // Language Filter (with fallback)
    // ============================================================
    @Step("Select Language: {language}")
    public TablePage selectLanguage(String language) {

        String normalized = language == null ? "" : language.trim().toLowerCase();

        if (!SUPPORTED_LANGUAGES.contains(normalized)) {
            Allure.addAttachment("Unsupported Language Fallback",
                    "Requested: " + language + "\nFalling back to: Any");
            log.warn("Unsupported language '{}', falling back to 'Any'", language);
            click(langAny);
        } else {
            switch (normalized) {
                case "java" -> click(langJava);
                case "python" -> click(langPython);
                default -> click(langAny);
            }
        }

        waitForTableToSettle();
        return this;
    }

    // ============================================================
    // Level Filters
    // ============================================================
    @Step("Set Beginner = {checked}")
    public TablePage setBeginner(boolean checked) {
        setCheckbox(levelBeginner, checked);
        waitForTableToSettle();
        return this;
    }

    @Step("Set Intermediate = {checked}")
    public TablePage setIntermediate(boolean checked) {
        setCheckbox(levelIntermediate, checked);
        waitForTableToSettle();
        return this;
    }

    @Step("Set Advanced = {checked}")
    public TablePage setAdvanced(boolean checked) {
        setCheckbox(levelAdvanced, checked);
        waitForTableToSettle();
        return this;
    }

    private void setCheckbox(By locator, boolean checked) {
        try {
            WebElement box = find(locator);
            if (box.isSelected() != checked) {
                click(locator);
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            Allure.addAttachment("Invalid Level Checkbox",
                    "Checkbox not found for locator: " + locator);
            log.warn("Attempted to toggle invalid level checkbox: {}", locator);
        }
    }

    // ============================================================
    // Min Enrollments (FULL FIX)
    // ============================================================
    @Step("Select Min Enrollments: {value}")
    public TablePage setMinEnrollments(String value) {

        String normalized = value == null ? "" : value.trim();

        // FULL PAGE SCROLL — this is the missing fix
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 400);");
        safeSleep(200);

        // Hover to open menu
        try {
            actions.moveToElement(find(minEnrollDropdownButton)).perform();
            Thread.sleep(150);
        } catch (Exception e) {
            log.warn("Hover failed on Min Enrollments dropdown, using JS click");
            jsClick(minEnrollDropdownButton);
        }

        // Click to ensure menu opens
        try {
            click(minEnrollDropdownButton);
        } catch (Exception e) {
            log.warn("Normal click failed on Min Enrollments dropdown, using JS click");
            jsClick(minEnrollDropdownButton);
        }

        // Wait for menu
        waitForPresence(minEnrollMenu);

        // Fallback
        if (!SUPPORTED_MIN_ENROLLMENTS.contains(normalized)) {
            Allure.addAttachment("Unsupported Min Enrollments Fallback",
                    "Requested: " + value + "\nFalling back to: Any");
            log.warn("Unsupported min enrollment '{}', falling back to 'Any'", value);
            normalized = "Any";
        }

        By option = minEnrollOption(normalized);

        // Scroll option into view
        scrollIntoView(option);

        // Click option
        try {
            click(option);
        } catch (Exception e) {
            log.warn("Option '{}' not clickable, retrying with JS click", normalized);
            jsClick(option);
        }

        waitForTableToSettle();
        return this;
    }

    // ============================================================
    // Sorting (with fallback)
    // ============================================================
    @Step("Sort by: {option}")
    public TablePage sortBy(String option) {

        String normalized = option == null ? "" : option.trim();

        WebElement dropdown = find(sortDropdown);
        Select select = new Select(dropdown);

        if (!SUPPORTED_SORT_OPTIONS.contains(normalized)) {
            Allure.addAttachment("Unsupported Sort Fallback",
                    "Requested: " + option + "\nFalling back to: Course Name");
            log.warn("Unsupported sort option '{}', falling back to 'Course Name'", option);

            select.selectByVisibleText("Course Name");
        } else {
            select.selectByVisibleText(normalized);
        }

        waitForTableToSettle();
        return this;
    }

    // ============================================================
    // Reset
    // ============================================================
    @Step("Click Reset button")
    public TablePage clickReset() {
        click(resetButton);
        waitForTableToSettle();
        return this;
    }

    // ============================================================
    // Visibility Checks
    // ============================================================
    @Step("Check if 'No matching courses' message is visible")
    public boolean isNoResultsMessageVisible() {
        return isElementVisible(noResultsMessage);
    }

    @Step("Check if Reset button is visible")
    public boolean isResetButtonVisible() {
        return isElementVisible(resetButton);
    }

    // ============================================================
    // Table Parsing Utilities
    // ============================================================
    @Step("Get all visible table rows")
    public List<WebElement> getVisibleRows() {
        return findAll(tableRows);
    }

    @Step("Get values from column: {colName}")
    public List<String> getColumnValues(String colName) {
        List<String> values = new ArrayList<>();

        for (WebElement row : getVisibleRows()) {
            WebElement cell = row.findElement(By.cssSelector("td[data-col='" + colName + "']"));
            String text = cell.getText().trim();
            if (!text.isEmpty()) {
                values.add(text);
            }
        }
        return values;
    }

    @Step("Get numeric values from column: {colName}")
    public List<Integer> getNumericColumnValues(String colName) {
        List<Integer> values = new ArrayList<>();

        for (WebElement row : getVisibleRows()) {
            WebElement cell = row.findElement(By.cssSelector("td[data-col='" + colName + "']"));
            String text = cell.getText().trim();
            if (!text.isEmpty()) {
                values.add(parseEnrollment(text));
            }
        }
        return values;
    }

    @Step("Find row containing text: {text}")
    public WebElement findRowByText(String text) {
        for (WebElement row : getVisibleRows()) {
            if (row.getText().contains(text)) {
                return row;
            }
        }
        return null;
    }

    // ============================================================
    // NEW: Get All Rows as Maps
    // ============================================================
    @Step("Get all visible table rows as key-value maps")
    public List<Map<String, String>> getAllRowsAsMaps() {

        List<Map<String, String>> rows = new ArrayList<>();

        for (WebElement row : getVisibleRows()) {
            Map<String, String> map = new HashMap<>();

            List<WebElement> cells = row.findElements(By.cssSelector("td[data-col]"));
            for (WebElement cell : cells) {
                String key = cell.getAttribute("data-col").trim();
                String value = cell.getText().trim();
                map.put(key, value);
            }

            rows.add(map);
        }

        return rows;
    }

    // ============================================================
    // Sorting Helpers
    // ============================================================
    @Step("Check if numeric list is sorted ascending")
    public boolean isSortedAscending(List<Integer> values) {
        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        return sorted.equals(values);
    }

    @Step("Check if string list is sorted alphabetically (A→Z)")
    public boolean isSortedAlphabetically(List<String> values) {
        List<String> sorted = new ArrayList<>(values);
        sorted.sort(String.CASE_INSENSITIVE_ORDER);
        return sorted.equals(values);
    }

    // ============================================================
    // Helper: Parse Enrollment Numbers
    // ============================================================
    @Step("Parse enrollment value: {text}")
    public int parseEnrollment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(text.replace(",", "").trim());
    }

    // ============================================================
    // Table Refresh Waits
    // ============================================================
    @Step("Wait for table to settle")
    public void waitForTableToSettle() {

        waitForCondition(driver -> {
            boolean hasRows = !driver.findElements(tableRows).isEmpty();
            boolean hasNoResults = !driver.findElements(noResultsMessage).isEmpty();
            return hasRows || hasNoResults;
        });

        waitForCondition(driver -> {
            int first = driver.findElements(tableRows).size();
            try { Thread.sleep(150); } catch (Exception ignored) {}
            int second = driver.findElements(tableRows).size();
            return first == second;
        });
    }
}