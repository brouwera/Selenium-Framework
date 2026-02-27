package tests;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.ExceptionsPage;
import pages.HomePage;

@Epic("Practice Test Automation")
@Feature("Exceptions Module")
public class ExceptionsTest extends BaseTest {

    // ============================================================
    // Navigation Helper
    // ============================================================

    private ExceptionsPage navigateToExceptionsPage() {
        return new HomePage(this).clickTestExceptionsLink();
    }

    // ============================================================
    // Test Case 1: NoSuchElementException (Row 2 appears late)
    // ============================================================

    @Story("Row 2 appears after delay")
    @Description("Verify that clicking Add eventually reveals Row 2 input field.")
    @Test
    public void testRow2AppearsAfterDelay() {

        ExceptionsPage page = navigateToExceptionsPage();

        page.clickAddButton(); // waits internally for Row 2

        AssertionHelper.assertTrue(
                page.isRow2InputVisible(),
                "Row 2 input should be visible after clicking Add"
        );
    }

    // ============================================================
    // Test Case 2: ElementNotInteractableException (Invisible Save)
    // ============================================================

    @Story("Save text in Row 2")
    @Description("Verify that saving text in Row 2 works and avoids clicking invisible Save in Row 1.")
    @Test
    public void testSaveTextRow2() {

        ExceptionsPage page = navigateToExceptionsPage();

        page.clickAddButton()
                .enterTextRow2("Adam Test")
                .clickRow2Save();

        String message = page.getConfirmationMessage();

        AssertionHelper.assertEquals(
                message,
                "Row 2 was saved",
                "Confirmation message should match"
        );
    }

    // ============================================================
    // Test Case 3: InvalidElementStateException (Row 1 disabled)
    // ============================================================

    @Story("Edit Row 1 input")
    @Description("Verify that editing Row 1 requires clicking Edit first.")
    @Test
    public void testEditRow1() {

        ExceptionsPage page = navigateToExceptionsPage();

        page.clickRow1Edit()
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
    @Description("Verify that instructions text disappears after clicking Add.")
    @Test
    public void testInstructionsDisappear() {

        ExceptionsPage page = navigateToExceptionsPage();

        AssertionHelper.assertTrue(
                page.isInstructionsDisplayed(),
                "Instructions should be visible initially"
        );

        page.clickAddButton(); // instructions removed from DOM

        AssertionHelper.assertFalse(
                page.isInstructionsDisplayed(),
                "Instructions should disappear after clicking Add"
        );
    }

    // ============================================================
    // Test Case 5: TimeoutException
    // ============================================================

    @Story("Short timeout causes failure")
    @Description("Verify that a 3-second wait fails because Row 2 appears after ~5 seconds.")
    @Test
    public void testShortTimeoutFails() {

        ExceptionsPage page = navigateToExceptionsPage();

        boolean appeared = page.waitForRow2InputShortTimeout();

        AssertionHelper.assertFalse(
                appeared,
                "Row 2 should NOT appear within 3 seconds"
        );
    }
}