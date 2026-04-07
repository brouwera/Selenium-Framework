package tests.ui;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.openqa.selenium.ElementNotInteractableException;
import org.testng.annotations.Test;
import pages.ExceptionsPage;
import pages.HomePage;
import utils.AiScenarioGenerator;

@Epic("Practice Test Automation")
@Feature("Exceptions Module")
@Owner("Adam Brouwer")
public class ExceptionsTest extends BaseTest {

    // ============================================================
    // Navigation Helper
    // ============================================================
    @Step("Navigate to Exceptions Page")
    private ExceptionsPage navigateToExceptionsPage() {
        return new HomePage(getDriver(), getWait())
                .open()
                .goToExceptionsPage();
    }

    // ============================================================
    // Test Case 1: NoSuchElementException (Row 2 appears late)
    // ============================================================
    @Story("Row 2 appears after delay")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that clicking Add eventually reveals Row 2 input field.")
    @Test
    public void testRow2AppearsAfterDelay() {

        AiScenarioGenerator.attachSuggestedScenarios("Exceptions Page");

        ExceptionsPage page = navigateToExceptionsPage();

        page.clickAddButton()
                .waitForRow2ToAppear();

        AssertionHelper.assertTrue(
                page.isRow2InputVisible(),
                "Row 2 input should be visible after clicking Add"
        );
    }

    // ============================================================
    // Test Case 2: ElementNotInteractableException (Invisible Save)
    // ============================================================
    @Story("Save text in Row 2")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that saving text in Row 2 works and avoids clicking invisible Save in Row 1.")
    @Test
    public void testSaveTextRow2() {

        AiScenarioGenerator.attachSuggestedScenarios("Exceptions Page");

        ExceptionsPage page = navigateToExceptionsPage();

        page.clickAddButton()
                .enterTextRow2("Adam Test")
                .clickRow2Save();

        AssertionHelper.assertEquals(
                page.getConfirmationMessage(),
                "Row 2 was saved",
                "Confirmation message should match expected text"
        );
    }

    @Story("Demonstrate ElementNotInteractableException")
    @Severity(SeverityLevel.MINOR)
    @Description("Intentionally click the invisible Save button to show the exception behavior.")
    @Test
    public void testInvisibleSaveThrowsException() {

        AiScenarioGenerator.attachSuggestedScenarios("Exceptions Page");

        ExceptionsPage page = navigateToExceptionsPage();

        AssertionHelper.assertThrows(
                ElementNotInteractableException.class,
                page::clickInvisibleSaveButton
        );
    }

    // ============================================================
    // Test Case 3: InvalidElementStateException (Row 1 disabled)
    // ============================================================
    @Story("Row 1 disabled state")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Row 1 is disabled before clicking Edit.")
    @Test
    public void testRow1IsDisabledInitially() {

        AiScenarioGenerator.attachSuggestedScenarios("Exceptions Page");

        ExceptionsPage page = navigateToExceptionsPage();

        AssertionHelper.assertFalse(
                page.isRow1InputEnabled(),
                "Row 1 input should be disabled initially"
        );
    }

    @Story("Edit Row 1 input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that editing Row 1 requires clicking Edit first.")
    @Test
    public void testEditRow1() {

        AiScenarioGenerator.attachSuggestedScenarios("Exceptions Page");

        ExceptionsPage page = navigateToExceptionsPage();

        page.clickRow1Edit()
                .waitForRow1ToAppear()
                .clearRow1Input()
                .enterTextRow1("Updated");

        AssertionHelper.assertEquals(
                page.getRow1InputValue(),
                "Updated",
                "Row 1 input should contain updated text"
        );
    }

    // ============================================================
    // Test Case 4: StaleElementReferenceException
    // ============================================================
    @Story("Instructions disappear after Add")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that instructions text disappears after clicking Add.")
    @Test
    public void testInstructionsDisappear() {

        AiScenarioGenerator.attachSuggestedScenarios("Exceptions Page");

        ExceptionsPage page = navigateToExceptionsPage();

        AssertionHelper.assertTrue(
                page.isInstructionsDisplayed(),
                "Instructions should be visible initially"
        );

        page.clickAddButton()
                .waitForInstructionsToDisappear();

        AssertionHelper.assertFalse(
                page.isInstructionsDisplayed(),
                "Instructions should disappear after clicking Add"
        );
    }

    @Story("Instructions removed from DOM")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify instructions element is removed from DOM after clicking Add.")
    @Test
    public void testInstructionsRemovedFromDOM() {

        AiScenarioGenerator.attachSuggestedScenarios("Exceptions Page");

        ExceptionsPage page = navigateToExceptionsPage();

        page.clickAddButton()
                .waitForInstructionsToDisappear();

        AssertionHelper.assertFalse(
                page.isInstructionsPresentInDOM(),
                "Instructions element should be removed from DOM"
        );
    }

    // ============================================================
    // Test Case 5: TimeoutException
    // ============================================================
    @Story("Short timeout causes failure")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that a 3-second wait fails because Row 2 appears after ~5 seconds.")
    @Test
    public void testShortTimeoutFails() {

        AiScenarioGenerator.attachSuggestedScenarios("Exceptions Page");

        ExceptionsPage page = navigateToExceptionsPage();

        boolean appeared = page.waitForRow2InputShortTimeout();

        AssertionHelper.assertFalse(
                appeared,
                "Row 2 should NOT appear within 3 seconds"
        );
    }
}