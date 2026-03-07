package tests;

import base.BaseTest;
import helpers.AssertionHelper;
import org.testng.annotations.Test;
import pages.FramesPage;
import pages.NestedFramesPage;
import pages.IFramePage;

public class FramesTest extends BaseTest {

    // ============================================================
    // Test Case 1: Nested Frames
    // ============================================================
    @Test(description = "Verify text inside all nested frames (left, middle, right, bottom)")
    public void testNestedFrames() {

        FramesPage framesPage = new FramesPage(getDriver(), getWait())
                .open();

        NestedFramesPage nested = framesPage.clickNestedFrames();

        AssertionHelper.assertEquals(
                nested.getLeftFrameText(),
                "LEFT",
                "Left frame text should match"
        );

        AssertionHelper.assertEquals(
                nested.getMiddleFrameText(),
                "MIDDLE",
                "Middle frame text should match"
        );

        AssertionHelper.assertEquals(
                nested.getRightFrameText(),
                "RIGHT",
                "Right frame text should match"
        );

        AssertionHelper.assertEquals(
                nested.getBottomFrameText(),
                "BOTTOM",
                "Bottom frame text should match"
        );
    }

    // ============================================================
    // Test Case 2: iFrame Default Text
    // ============================================================
    @Test(description = "Verify default TinyMCE text is visible inside the iFrame")
    public void testIFrameDefaultText() {

        FramesPage framesPage = new FramesPage(getDriver(), getWait())
                .open();

        IFramePage iframe = framesPage.clickIFrame();

        String actual = iframe.getDefaultEditorText();

        AssertionHelper.assertEquals(
                actual.trim(),
                "Your content goes here.",
                "Default editor text should match"
        );
    }
}