package tests;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.DynamicControlsPage;

@Epic("Dynamic Controls")
@Feature("Dynamic UI Behavior")
public class DynamicControlsTest extends BaseTest {

    // ============================================================
    // Navigation Helper
    // ============================================================
    @Step("Navigate to Dynamic Controls Page")
    private DynamicControlsPage navigateToDynamicControlsPage() {
        return new DynamicControlsPage(getDriver(), getWait()).open();
    }

    // ============================================================
    // Test Case 1: Remove and Add Checkbox
    // ============================================================
    @Story("Checkbox can be removed and added back")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify checkbox can be removed and added back with correct messages.")
    @Test(groups = {"regression"})
    public void testRemoveAndAddCheckbox() {

        DynamicControlsPage page = navigateToDynamicControlsPage();

        // Remove checkbox
        page.clickRemoveOrAdd()
                .waitForLoadingToDisappear()
                .waitForCheckboxToDisappear();

        AssertionHelper.assertFalse(
                page.isCheckboxPresent(),
                "Checkbox should be removed"
        );

        AssertionHelper.assertEquals(
                page.getMessageText(),
                "It's gone!",
                "Removal message should match expected text"
        );

        // Add checkbox back
        page.clickRemoveOrAdd()
                .waitForLoadingToDisappear()
                .waitForCheckboxToAppear();

        AssertionHelper.assertTrue(
                page.isCheckboxPresent(),
                "Checkbox should be added back"
        );

        AssertionHelper.assertEquals(
                page.getMessageText(),
                "It's back!",
                "Addition message should match expected text"
        );
    }

    // ============================================================
    // Test Case 2: Enable and Disable Input Field
    // ============================================================
    @Story("Input field can be enabled and disabled")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify input field can be enabled and disabled with correct messages.")
    @Test(groups = {"regression"})
    public void testEnableAndDisableInput() {

        DynamicControlsPage page = navigateToDynamicControlsPage();

        // Enable input
        page.clickEnableOrDisable()
                .waitForLoadingToDisappear()
                .waitForInputToBeEnabled();

        AssertionHelper.assertTrue(
                page.isInputEnabled(),
                "Input should be enabled"
        );

        AssertionHelper.assertEquals(
                page.getMessageText(),
                "It's enabled!",
                "Enable message should match expected text"
        );

        // Disable input
        page.clickEnableOrDisable()
                .waitForLoadingToDisappear()
                .waitForInputToBeDisabled();

        AssertionHelper.assertFalse(
                page.isInputEnabled(),
                "Input should be disabled"
        );

        AssertionHelper.assertEquals(
                page.getMessageText(),
                "It's disabled!",
                "Disable message should match expected text"
        );
    }

    // ============================================================
    // Test Case 3: Loading Indicator Behavior
    // ============================================================
    @Story("Loading indicator appears and disappears correctly")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify loading indicator appears and disappears correctly when clicking Remove/Add.")
    @Test(groups = {"regression"})
    public void testLoadingIndicatorBehavior() {

        DynamicControlsPage page = navigateToDynamicControlsPage();

        page.clickRemoveOrAddExpectingLoading();

        AssertionHelper.assertTrue(
                page.wasLoadingIndicatorObserved(),
                "Loading indicator should appear at least once during the operation"
        );
    }
}