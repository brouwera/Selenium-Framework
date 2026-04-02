package tests.ui;

import base.BaseTest;
import config.ConfigManager;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TablePage;
import utils.AiDataGenerator;
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
    // AI-Driven Dynamic Sorting & Filtering Scenarios
    // ============================================================
    @Story("AI-generated dynamic table scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates table sorting and filtering using AI-generated instructions.")
    @Test(enabled = true, groups = {"regression"})
    public void aiGeneratedTableScenario() {

        // Skip if AI data is disabled
        if (!ConfigManager.isAiDataEnabled()) {
            Allure.step("AI data generation disabled — skipping AI-driven table scenario.");
            return;
        }

        // Arrange
        TablePage tablePage = navigateToTablePage();

        // Generate AI-driven scenario
        TableScenario scenario = AiDataGenerator.generateTableScenario();

        // Attach scenario details to Allure
        Allure.addAttachment("AI Scenario Instructions", scenario.getInstruction());
        Allure.addAttachment("AI Scenario Actions", scenario.getActions().toString());

        // Act — Apply filters/sorting based on AI instructions
        scenario.applyTo(tablePage);

        // ============================================================
        // Behavior-Driven Assertions (Professional Approach)
        // ============================================================

        // 1. Language filter
        if (scenario.getActions().containsKey("language")) {
            String expectedLang = (String) scenario.getActions().get("language");
            List<String> languages = tablePage.getColumnValues("language");

            // If no rows exist → assert "No results" instead of failing
            if (languages.isEmpty()) {
                AssertionHelper.assertTrue(
                        tablePage.isNoResultsMessageVisible(),
                        "No results message should be visible when filtering by Language = " + expectedLang
                );
            } else {
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
    @Story("Filter by Language: Java")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that selecting Language = Java shows only Java courses.")
    @Test(groups = {"regression"})
    public void filterByLanguageJava() {

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
    @Story("Filter by Level: Beginner")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that only Beginner courses are visible when Intermediate and Advanced are unchecked.")
    @Test(groups = {"regression"})
    public void filterByLevelBeginner() {

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
    @Story("Filter by Min Enrollments: 10,000+")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all visible courses have enrollments >= 10,000.")
    @Test(groups = {"regression"})
    public void filterByMinEnrollments() {

        TablePage table = navigateToTablePage()
                .setMinEnrollments("10000");

        List<Integer> enrollments = table.getNumericColumnValues("enrollments");

        AssertionHelper.assertTrue(
                TableUtils.allGreaterOrEqual(enrollments, 10000),
                "All visible courses should have enrollments >= 10,000"
        );
    }

    // ============================================================
    // Test Case 4: Combined filters → Python + Beginner + 10,000+
    // ============================================================
    @Story("Combined Filters: Python + Beginner + 10,000+")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify combined filtering logic for Language, Level, and Min Enrollments.")
    @Test(groups = {"regression"})
    public void combinedFilters() {

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
    @Story("No Results State")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that no results message appears when filters yield no matches.")
    @Test(groups = {"regression"})
    public void noResultsState() {

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
    // Test Case 6: Reset button visibility and behavior
    // ============================================================
    @Story("Reset Button Behavior")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Reset button visibility and that it restores default filter values.")
    @Test(groups = {"regression"})
    public void resetButtonBehavior() {

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
    // Test Case 7: Sort by Enrollments (ascending, numeric)
    // ============================================================
    @Story("Sort by Enrollments (Ascending)")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify numeric sorting of enrollments in ascending order.")
    @Test(groups = {"regression"})
    public void sortByEnrollmentsAscending() {

        TablePage table = navigateToTablePage()
                .sortBy("Enrollments");

        List<Integer> enrollments = table.getNumericColumnValues("enrollments");

        AssertionHelper.assertTrue(
                TableUtils.isSortedAscending(enrollments),
                "Enrollments should be sorted in ascending numeric order"
        );
    }

    // ============================================================
    // Test Case 8: Sort by Course Name (alphabetical)
    // ============================================================
    @Story("Sort by Course Name (A→Z)")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify alphabetical sorting of course names.")
    @Test(groups = {"regression"})
    public void sortByCourseName() {

        TablePage table = navigateToTablePage()
                .sortBy("Course Name");

        List<String> names = table.getColumnValues("course");

        AssertionHelper.assertTrue(
                TableUtils.isSortedAlphabetically(names),
                "Course names should be sorted alphabetically A→Z"
        );
    }
}