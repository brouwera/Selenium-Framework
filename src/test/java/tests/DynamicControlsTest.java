package tests;

import base.BaseTest;
import helpers.AssertionHelper;
import org.testng.annotations.Test;
import pages.DynamicControlsPage;

public class DynamicControlsTest extends BaseTest {

    // ============================================================
    // Test Case 1: Remove and Add Checkbox
    // ============================================================
    @Test(description = "Verify checkbox can be removed and added back with correct messages")
    public void testRemoveAndAddCheckbox() {

        DynamicControlsPage page = new DynamicControlsPage(this).open();

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
                "Message should confirm removal"
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
                "Message should confirm addition"
        );
    }

    // ============================================================
    // Test Case 2: Enable and Disable Input Field
    // ============================================================
    @Test(description = "Verify input field can be enabled and disabled with correct messages")
    public void testEnableAndDisableInput() {

        DynamicControlsPage page = new DynamicControlsPage(this).open();

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
                "Message should confirm enabling"
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
                "Message should confirm disabling"
        );
    }

    // ============================================================
    // Test Case 3: Loading Indicator Behavior
    // ============================================================
    @Test(description = "Verify loading indicator appears and disappears correctly")
    public void testLoadingIndicatorBehavior() {

        DynamicControlsPage page = new DynamicControlsPage(this).open();

        // Trigger loading
        page.clickRemoveOrAdd();

        // Spinner should appear immediately
        AssertionHelper.assertTrue(
                page.isLoadingVisible(),
                "Loading indicator should appear"
        );

        // Then disappear
        page.waitForLoadingToDisappear();

        AssertionHelper.assertFalse(
                page.isLoadingVisible(),
                "Loading indicator should disappear"
        );
    }
}