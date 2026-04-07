package tests.ui;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.DragAndDropPage;
import pages.HomePage;
import utils.AiScenarioGenerator;

@Epic("Drag and Drop")
@Feature("HTML5 Drag and Drop Behavior")
@Owner("Adam Brouwer")
public class DragAndDropTest extends BaseTest {

    // ============================================================
    // Navigation Helper
    // ============================================================
    @Step("Navigate to Drag and Drop Page")
    private DragAndDropPage navigateToDragAndDropPage() {
        return new HomePage(getDriver(), getWait())
                .open()
                .goToDragAndDropPage()
                .waitForPageReady();
    }

    // ============================================================
    // Test Case 1: Drag A → B
    // ============================================================
    @Story("Drag Column A to Column B")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that Column A can be dragged to Column B using HTML5 drag-and-drop simulation.")
    @Test
    public void testDragAtoB() {

        AiScenarioGenerator.attachSuggestedScenarios("Drag and Drop Page");

        DragAndDropPage page = navigateToDragAndDropPage();

        page.dragAtoB();

        AssertionHelper.assertTrue(
                page.isColumnAHeader("B"),
                "Column A should now contain header 'B' after drag"
        );

        AssertionHelper.assertTrue(
                page.isColumnBHeader("A"),
                "Column B should now contain header 'A' after drag"
        );
    }

    // ============================================================
    // Test Case 2: Drag B → A
    // ============================================================
    @Story("Drag Column B to Column A")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that Column B can be dragged to Column A using HTML5 drag-and-drop simulation.")
    @Test
    public void testDragBtoA() {

        AiScenarioGenerator.attachSuggestedScenarios("Drag and Drop Page");

        DragAndDropPage page = navigateToDragAndDropPage();

        page.dragBtoA();

        AssertionHelper.assertTrue(
                page.isColumnAHeader("B"),
                "Column A should contain header 'B' after drag"
        );

        AssertionHelper.assertTrue(
                page.isColumnBHeader("A"),
                "Column B should contain header 'A' after drag"
        );
    }

    // ============================================================
    // Test Case 3: Actions API Fallback (Optional)
    // ============================================================
    @Story("Drag and Drop using Actions API fallback")
    @Severity(SeverityLevel.MINOR)
    @Description("Validates that the Actions API fallback can perform drag-and-drop (may be less reliable on HTML5).")
    @Test
    public void testDragUsingActionsFallback() {

        AiScenarioGenerator.attachSuggestedScenarios("Drag and Drop Page");

        DragAndDropPage page = navigateToDragAndDropPage();

        page.dragAtoBWithActions();

        AssertionHelper.assertTrue(
                page.isColumnAHeader("B"),
                "Column A should contain header 'B' after fallback drag"
        );

        AssertionHelper.assertTrue(
                page.isColumnBHeader("A"),
                "Column B should contain header 'A' after fallback drag"
        );
    }
}