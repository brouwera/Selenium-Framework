package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TablePage extends BasePage {

    // ============================================================
    // Locators
    // ============================================================

    // Language radio buttons
    private final By langAny = By.cssSelector("input[name='lang'][value='Any']");
    private final By langJava = By.cssSelector("input[name='lang'][value='Java']");
    private final By langPython = By.cssSelector("input[name='lang'][value='Python']");

    // Level checkboxes
    private final By levelBeginner = By.cssSelector("input[name='level'][value='Beginner']");
    private final By levelIntermediate = By.cssSelector("input[name='level'][value='Intermediate']");
    private final By levelAdvanced = By.cssSelector("input[name='level'][value='Advanced']");

    // Min Enrollments dropdown
    private final By minEnrollDropdownButton = By.cssSelector("#enrollDropdown .dropdown-button");
    private By minEnrollOption(String value) {
        return By.cssSelector("#enrollDropdown .dropdown-menu li[data-value='" + value + "']");
    }

    // Sort dropdown
    private final By sortDropdown = By.id("sortBy");

    // Reset button
    private final By resetButton = By.id("resetFilters");

    // Table rows
    private final By tableRows = By.cssSelector("tbody tr");

    // No results message
    private final By noResultsMessage = By.id("noData");

    // First language cell
    private final By firstLanguageCell = By.cssSelector("tbody tr td[data-col='language']");

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
        driver.get("https://practicetestautomation.com/practice-test-tables/");
        return this;
    }

    // ============================================================
    // Filter Interactions
    // ============================================================
    @Step("Select Language: {language}")
    public TablePage selectLanguage(String language) {
        switch (language.toLowerCase()) {
            case "any":
                click(langAny);
                waitForTableToUpdate();
                break;
            case "java":
                click(langJava);
                waitForLanguageToBe("Java");
                break;
            case "python":
                click(langPython);
                waitForLanguageToBe("Python");
                break;
        }
        return this;
    }

    @Step("Set Beginner = {checked}")
    public TablePage setBeginner(boolean checked) {
        setCheckbox(levelBeginner, checked);
        waitForTableToUpdate();
        return this;
    }

    @Step("Set Intermediate = {checked}")
    public TablePage setIntermediate(boolean checked) {
        setCheckbox(levelIntermediate, checked);
        waitForTableToUpdate();
        return this;
    }

    @Step("Set Advanced = {checked}")
    public TablePage setAdvanced(boolean checked) {
        setCheckbox(levelAdvanced, checked);
        waitForTableToUpdate();
        return this;
    }

    private void setCheckbox(By locator, boolean checked) {
        WebElement box = find(locator);
        if (box.isSelected() != checked) {
            box.click();
        }
    }

    @Step("Select Min Enrollments: {value}")
    public TablePage setMinEnrollments(String value) {
        click(minEnrollDropdownButton);
        click(minEnrollOption(value));
        waitForTableToUpdate();
        return this;
    }

    @Step("Sort by: {option}")
    public TablePage sortBy(String option) {
        WebElement dropdown = find(sortDropdown);
        dropdown.sendKeys(option);
        waitForTableToUpdate();
        return this;
    }

    @Step("Click Reset button")
    public TablePage clickReset() {
        click(resetButton);
        waitForTableToUpdate();
        return this;
    }

    // ============================================================
    // Visibility Checks
    // ============================================================
    @Step("Check if 'No matching courses' message is visible")
    public boolean isNoResultsMessageVisible() {
        return isDisplayed(noResultsMessage);
    }

    @Step("Check if Reset button is visible")
    public boolean isResetButtonVisible() {
        return isDisplayed(resetButton);
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
    private void waitForTableToUpdate() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRows));
    }

    private void waitForLanguageToBe(String expected) {
        wait.until(driver -> {
            WebElement cell = driver.findElement(firstLanguageCell);
            return cell.getText().trim().equalsIgnoreCase(expected);
        });
    }
}