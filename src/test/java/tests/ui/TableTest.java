package tests.ui;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TablePage;
import utils.TableUtils;

import java.util.List;

@Epic("Table")
@Feature("Course Table Filtering and Sorting")
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
    // Test Case 1: Language filter → Java
    // ============================================================
    @Story("Filter by Language: Java")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Adam Brouwer")
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
    @Owner("Adam Brouwer")
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
    @Owner("Adam Brouwer")
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
    @Owner("Adam Brouwer")
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
    @Owner("Adam Brouwer")
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
    @Owner("Adam Brouwer")
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
    @Owner("Adam Brouwer")
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
    @Owner("Adam Brouwer")
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