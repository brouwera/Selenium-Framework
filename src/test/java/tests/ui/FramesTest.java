package tests.ui;

import base.BaseTest;
import helpers.AssertionHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.FramesPage;
import pages.NestedFramesPage;
import pages.IFramePage;
import utils.AiScenarioGenerator;

@Epic("Frames")
@Feature("Nested Frames and iFrame Editor")
@Owner("Adam Brouwer")
public class FramesTest extends BaseTest {

    // ============================================================
    // Navigation Helper
    // ============================================================
    @Step("Navigate to Frames Page")
    private FramesPage navigateToFramesPage() {
        return new FramesPage(getDriver(), getWait()).open();
    }

    // ============================================================
    // Test Case 1: Nested Frames
    // ============================================================
    @Story("Verify text inside nested frames")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate that LEFT, MIDDLE, RIGHT, and BOTTOM frame texts are correct.")
    @Test
    public void testNestedFrames() {

        AiScenarioGenerator.attachSuggestedScenarios("Frames Page");

        FramesPage framesPage = navigateToFramesPage();
        NestedFramesPage nested = framesPage.clickNestedFrames();

        AssertionHelper.assertEquals(
                nested.getLeftFrameText(),
                "LEFT",
                "Left frame text should match expected value"
        );

        AssertionHelper.assertEquals(
                nested.getMiddleFrameText(),
                "MIDDLE",
                "Middle frame text should match expected value"
        );

        AssertionHelper.assertEquals(
                nested.getRightFrameText(),
                "RIGHT",
                "Right frame text should match expected value"
        );

        AssertionHelper.assertEquals(
                nested.getBottomFrameText(),
                "BOTTOM",
                "Bottom frame text should match expected value"
        );
    }

    // ============================================================
    // Test Case 2: iFrame Default Text
    // ============================================================
    @Story("Verify default TinyMCE editor text")
    @Severity(SeverityLevel.MINOR)
    @Description("Validate that the TinyMCE editor loads with the expected default text.")
    @Test
    public void testIFrameDefaultText() {

        AiScenarioGenerator.attachSuggestedScenarios("Frames Page");

        FramesPage framesPage = navigateToFramesPage();
        IFramePage iframe = framesPage.clickIFrame();

        String actual = iframe.getDefaultEditorText().trim();

        AssertionHelper.assertEquals(
                actual,
                "Your content goes here.",
                "Default TinyMCE editor text should match expected value"
        );
    }
}