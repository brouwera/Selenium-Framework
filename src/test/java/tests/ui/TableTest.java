package tests.ui;

import base.BaseTest;
import config.ConfigManager;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TablePage;
import utils.AiDataGenerator;
import utils.AiScenarioGenerator;
import utils.TableScenario;
import utils.TableUtils;

import java.util.List;

@Epic("Table")
@Feature("Course Table Filtering and Sorting")
@Owner("Adam Brouwer")
public class TableTest extends BaseTest {

    // ============================================================
    // Navigation Helper
    // ============================================================
    @Step("Navigate to Course Table Page")
    private TablePage navigateToTablePage() {
        return new HomePage(getDriver(), getWait())
                .open()
                .goToTablePage();
    }

    // ============================================================
    // Supported Languages (UI Reality)
    // ============================================================
    private static final List<String> SUPPORTED_LANGUAGES = List.of("Any", "Java", "Python");

    private String normalizeLanguage(String lang) {
        if (!SUPPORTED_LANGUAGES.contains(lang)) {
            return "Any";
        }
        return lang;
    }

    // ============================================================
    // AI-Driven Dynamic Sorting & Filtering Scenarios
    // ============================================================
    @Story("AI-generated dynamic table scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates table sorting and filtering using AI-generated instructions.")
    @Test(enabled = true)
    public void aiGeneratedTableScenario() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        if (!ConfigManager.isAiDataEnabled()) {
            Allure.step("AI data generation disabled — skipping AI-driven table scenario.");
            return;
        }

        TablePage tablePage = navigateToTablePage();

        TableScenario scenario = AiDataGenerator.generateTableScenario();

        Allure.addAttachment("AI Scenario Instructions", scenario.getInstruction());
        Allure.addAttachment("AI Scenario Actions", scenario.getActions().toString());

        scenario.applyTo(tablePage);

        // ============================================================
        // Behavior-Driven Assertions (Professional Approach)
        // ============================================================

        // 1. Language filter
        if (scenario.getActions().containsKey("language")) {

            String expectedLang = normalizeLanguage(
                    (String) scenario.getActions().get("language")
            );

            List<String> languages = tablePage.getColumnValues("language");

            if (languages.isEmpty()) {
                AssertionHelper.assertTrue(
                        tablePage.isNoResultsMessageVisible(),
                        "No results message should be visible when filtering by Language = " + expectedLang
                );
            } else if (!expectedLang.equals("Any")) {
                AssertionHelper.assertTrue(
                        TableUtils.allEqual(languages, expectedLang),
                        "All visible courses should have Language = " + expectedLang
                );
            }
        }

        // 2. Level filters
        boolean beginner = (boolean) scenario.getActions().getOrDefault("beginner", true);
        boolean intermediate = (boolean) scenario.getActions().getOrDefault("intermediate", true);
        boolean advanced = (boolean) scenario.getActions().getOrDefault("advanced", true);

        List<String> levels = tablePage.getColumnValues("level");

        if (!beginner && !intermediate && !advanced) {
            AssertionHelper.assertTrue(
                    tablePage.isNoResultsMessageVisible(),
                    "No results message should be visible when all levels are disabled"
            );
        } else {
            if (!beginner) {
                AssertionHelper.assertFalse(
                        levels.contains("Beginner"),
                        "Beginner courses should not be visible"
                );
            }
            if (!intermediate) {
                AssertionHelper.assertFalse(
                        levels.contains("Intermediate"),
                        "Intermediate courses should not be visible"
                );
            }
            if (!advanced) {
                AssertionHelper.assertFalse(
                        levels.contains("Advanced"),
                        "Advanced courses should not be visible"
                );
            }
        }

        // 3. Min enrollments
        if (scenario.getActions().containsKey("minEnrollments")) {
            int min = (int) scenario.getActions().get("minEnrollments");
            List<Integer> enrollments = tablePage.getNumericColumnValues("enrollments");

            if (enrollments.isEmpty()) {
                AssertionHelper.assertTrue(
                        tablePage.isNoResultsMessageVisible(),
                        "No results message should be visible when filtering by minEnrollments = " + min
                );
            } else {
                AssertionHelper.assertTrue(
                        TableUtils.allGreaterOrEqual(enrollments, min),
                        "All visible courses should have enrollments >= " + min
                );
            }
        }

        // 4. Sorting
        if (scenario.getActions().containsKey("sortBy")) {
            String sortColumn = (String) scenario.getActions().get("sortBy");

            if (sortColumn.equalsIgnoreCase("Enrollments")) {
                List<Integer> enrollments = tablePage.getNumericColumnValues("enrollments");

                if (!enrollments.isEmpty()) {
                    AssertionHelper.assertTrue(
                            TableUtils.isSortedAscending(enrollments),
                            "Enrollments should be sorted in ascending numeric order"
                    );
                }
            }

            if (sortColumn.equalsIgnoreCase("Course Name")) {
                List<String> names = tablePage.getColumnValues("course");

                if (!names.isEmpty()) {
                    AssertionHelper.assertTrue(
                            TableUtils.isSortedAlphabetically(names),
                            "Course names should be sorted alphabetically A→Z"
                    );
                }
            }
        }

        Allure.step("AI-driven table scenario validated successfully.");
    }

    // ============================================================
    // Test Case 1: Language filter → Java
    // ============================================================
    @Test
    public void filterByLanguageJava() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        TablePage table = navigateToTablePage()
                .selectLanguage("Java");

        List<String> languages = table.getColumnValues("language");

        AssertionHelper.assertTrue(
                TableUtils.allEqual(languages, "Java"),
                "All visible courses should have Language = Java"
        );
    }

    // ============================================================
    // Test Case 2: Level filter → Beginner only
    // ============================================================
    @Test
    public void filterByLevelBeginner() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        TablePage table = navigateToTablePage()
                .setIntermediate(false)
                .setAdvanced(false);

        List<String> levels = table.getColumnValues("level");

        AssertionHelper.assertTrue(
                TableUtils.allEqual(levels, "Beginner"),
                "All visible courses should have Level = Beginner"
        );
    }

    // ============================================================
    // Test Case 3: Min enrollments → 10,000+
    // ============================================================
    @Test
    public void filterByMinEnrollments() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        TablePage table = navigateToTablePage()
                .setMinEnrollments("10000");

        List<Integer> enrollments = table.getNumericColumnValues("enrollments");

        AssertionHelper.assertTrue(
                TableUtils.allGreaterOrEqual(enrollments, 10000),
                "All visible courses should have enrollments >= 10,000"
        );
    }

    // ============================================================
    // Test Case 4: Combined filters
    // ============================================================
    @Test
    public void combinedFilters() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        TablePage table = navigateToTablePage()
                .selectLanguage("Python")
                .setIntermediate(false)
                .setAdvanced(false)
                .setMinEnrollments("10000");

        List<String> languages = table.getColumnValues("language");
        List<String> levels = table.getColumnValues("level");
        List<Integer> enrollments = table.getNumericColumnValues("enrollments");

        AssertionHelper.assertTrue(
                TableUtils.allEqual(languages, "Python"),
                "All visible courses should have Language = Python"
        );

        AssertionHelper.assertTrue(
                TableUtils.allEqual(levels, "Beginner"),
                "All visible courses should have Level = Beginner"
        );

        AssertionHelper.assertTrue(
                TableUtils.allGreaterOrEqual(enrollments, 10000),
                "All visible courses should have enrollments >= 10,000"
        );
    }

    // ============================================================
    // Test Case 5: No results state
    // ============================================================
    @Test
    public void noResultsState() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        TablePage table = navigateToTablePage()
                .selectLanguage("Python")
                .setBeginner(false)
                .setIntermediate(false)
                .setAdvanced(false);

        AssertionHelper.assertTrue(
                table.isNoResultsMessageVisible(),
                "No matching courses message should be visible"
        );
    }

    // ============================================================
    // Test Case 6: Reset button behavior
    // ============================================================
    @Test
    public void resetButtonBehavior() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        TablePage table = navigateToTablePage()
                .selectLanguage("Java");

        AssertionHelper.assertTrue(
                table.isResetButtonVisible(),
                "Reset button should be visible after changing filters"
        );

        table.clickReset();

        AssertionHelper.assertFalse(
                table.isResetButtonVisible(),
                "Reset button should be hidden after clicking Reset"
        );

        List<String> languages = table.getColumnValues("language");

        AssertionHelper.assertTrue(
                languages.size() > 0,
                "All rows should be visible after Reset"
        );
    }

    // ============================================================
    // Test Case 7: Sort by Enrollments
    // ============================================================
    @Test
    public void sortByEnrollmentsAscending() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        TablePage table = navigateToTablePage()
                .sortBy("Enrollments");

        List<Integer> enrollments = table.getNumericColumnValues("enrollments");

        AssertionHelper.assertTrue(
                TableUtils.isSortedAscending(enrollments),
                "Enrollments should be sorted in ascending numeric order"
        );
    }

    // ============================================================
    // Test Case 8: Sort by Course Name
    // ============================================================
    @Test
    public void sortByCourseName() {

        AiScenarioGenerator.attachSuggestedScenarios("Table Page");

        TablePage table = navigateToTablePage()
                .sortBy("Course Name");

        List<String> names = table.getColumnValues("course");

        AssertionHelper.assertTrue(
                TableUtils.isSortedAlphabetically(names),
                "Course names should be sorted alphabetically A→Z"
        );
    }
}