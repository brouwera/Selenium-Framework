package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.InputsPage;

import static org.testng.Assert.assertEquals;

public class InputsTest extends BaseTest {

    private InputsPage goToInputsPage() {
        HomePage home = new HomePage(BaseTest.getDriver(), wait);
        home.clickPracticeButton();
        return home.clickTestInputsLink();
    }

    @Test
    public void enterValidNumberTest() {
        InputsPage inputs = goToInputsPage();
        inputs.clearInput();
        inputs.enterNumber("12345");

        assertEquals(inputs.getInputValue(), "12345");
    }

    @Test
    public void enterNegativeNumberTest() {
        InputsPage inputs = goToInputsPage();
        inputs.clearInput();
        inputs.enterNumber("-42");

        assertEquals(inputs.getInputValue(), "-42");
    }

    @Test
    public void enterDecimalNumberTest() {
        InputsPage inputs = goToInputsPage();
        inputs.clearInput();
        inputs.enterNumber("3.14");

        assertEquals(inputs.getInputValue(), "3.14");
    }
}