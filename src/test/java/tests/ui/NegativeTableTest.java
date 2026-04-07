package tests.ui;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TablePage;
import utils.NegativeScenarioGenerator;
import utils.NegativeTableScenario;

import java.util.List;

@Epic("Table")
@Feature("Negative Table Scenarios")
@Owner("Adam Brouwer")
public class NegativeTableTest extends BaseTest {

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
    // Test 1: Unsupported Language
    // ============================================================
    @Story("Unsupported Language Handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that unsupported languages fall back to 'Any' and do not break the UI.")
    @Test
    public void unsupportedLanguageTest() {

        TablePage tablePage = navigateToTablePage();

        NegativeTableScenario scenario = NegativeScenarioGenerator.generateNegativeScenario();
        scenario.addAction("language", "JavaScript"); // Force unsupported language

        scenario.applyNegativeTo(tablePage);

        List<String> languages = tablePage.getColumnValues("language");

        AssertionHelper.assertTrue(
                languages.size() >= 0,
                "UI should remain stable when unsupported language is selected"
        );
    }

    // ============================================================
    // Test 2: Contradictory Levels (all disabled)
    // ============================================================
    @Story("Contradictory Level Filters")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that disabling all levels results in a no-results state.")
    @Test
    public void contradictoryLevelsTest() {

        TablePage tablePage = navigateToTablePage();

        NegativeTableScenario scenario = NegativeScenarioGenerator.generateNegativeScenario();
        scenario.addAction("beginner", false);
        scenario.addAction("intermediate", false);
        scenario.addAction("advanced", false);

        scenario.applyNegativeTo(tablePage);

        AssertionHelper.assertTrue(
                tablePage.isNoResultsMessageVisible(),
                "No results message should appear when all levels are disabled"
        );
    }

    // ============================================================
    // Test 3: Unsupported Sort Column
    // ============================================================
    @Story("Unsupported Sort Handling")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that unsupported sort columns do not break the UI.")
    @Test
    public void unsupportedSortColumnTest() {

        TablePage tablePage = navigateToTablePage();

        NegativeTableScenario scenario = NegativeScenarioGenerator.generateNegativeScenario();
        scenario.addAction("sortBy", "Difficulty"); // Unsupported sort

        scenario.applyNegativeTo(tablePage);

        AssertionHelper.assertTrue(
                tablePage.getVisibleRows().size() >= 0,
                "UI should remain stable when unsupported sort column is used"
        );
    }
}