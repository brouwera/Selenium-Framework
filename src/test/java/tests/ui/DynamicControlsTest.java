package tests.ui;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.DynamicControlsPage;
import pages.HomePage;
import utils.AiScenarioGenerator;

@Epic("Dynamic Controls")
@Feature("Dynamic UI Behavior")
@Owner("Adam Brouwer")
public class DynamicControlsTest extends BaseTest {

    // ============================================================
    // Navigation Helper
    // ============================================================
    @Step("Navigate to Dynamic Controls Page")
    private DynamicControlsPage navigateToDynamicControlsPage() {
        return new HomePage(getDriver(), getWait())
                .open()
                .goToDynamicControlsPage()
                .waitForPageReady();
    }

    // ============================================================
    // Test Case 1: Remove and Add Checkbox
    // ============================================================
    @Story("Checkbox can be removed and added back")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that the checkbox can be removed and added back, and that the correct success messages are displayed.")
    @Test
    public void testRemoveAndAddCheckbox() {

        AiScenarioGenerator.attachSuggestedScenarios("Dynamic Controls Page");

        DynamicControlsPage page = navigateToDynamicControlsPage();

        // Act — Remove checkbox
        page.clickRemoveOrAdd()
                .waitForCheckboxToDisappear();

        // Assert — Checkbox removed
        AssertionHelper.assertFalse(
                page.isCheckboxPresent(),
                "Checkbox should be removed from the DOM"
        );

        AssertionHelper.assertEquals(
                page.getMessageText(),
                "It's gone!",
                "Removal message should match expected text"
        );

        // Act — Add checkbox back
        page.clickRemoveOrAdd()
                .waitForCheckboxToAppear();

        // Assert — Checkbox added
        AssertionHelper.assertTrue(
                page.isCheckboxPresent(),
                "Checkbox should be added back to the DOM"
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
    @Description("Validates that the input field can be enabled and disabled, and that the correct success messages are displayed.")
    @Test
    public void testEnableAndDisableInput() {

        AiScenarioGenerator.attachSuggestedScenarios("Dynamic Controls Page");

        DynamicControlsPage page = navigateToDynamicControlsPage();

        // Act — Enable input
        page.clickEnableOrDisable()
                .waitForInputToBeEnabled();

        // Assert — Input enabled
        AssertionHelper.assertTrue(
                page.isInputEnabled(),
                "Input field should be enabled"
        );

        AssertionHelper.assertEquals(
                page.getMessageText(),
                "It's enabled!",
                "Enable message should match expected text"
        );

        // Act — Disable input
        page.clickEnableOrDisable()
                .waitForInputToBeDisabled();

        // Assert — Input disabled
        AssertionHelper.assertFalse(
                page.isInputEnabled(),
                "Input field should be disabled"
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
    @Description("Validates that the loading indicator appears at least once during a Remove/Add operation.")
    @Test
    public void testLoadingIndicatorBehavior() {

        AiScenarioGenerator.attachSuggestedScenarios("Dynamic Controls Page");

        DynamicControlsPage page = navigateToDynamicControlsPage();

        // Act
        page.clickRemoveOrAddExpectingLoading();

        // Assert
        AssertionHelper.assertTrue(
                page.wasLoadingIndicatorObserved(),
                "Loading indicator should appear at least once during the operation"
        );
    }
}