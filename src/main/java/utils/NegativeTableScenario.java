package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import pages.TablePage;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an AI‑generated negative scenario for the Course Table page.
 * This model now excludes Min Enrollments logic because the dropdown
 * is not automation-friendly and has been removed from tests.
 */
public class NegativeTableScenario {

    private final String instruction;
    private final Map<String, Object> actions = new HashMap<>();
    private final String expectedOutcome;

    public NegativeTableScenario(String instruction, String expectedOutcome) {
        this.instruction = instruction;
        this.expectedOutcome = expectedOutcome;
    }

    // ============================================================
    // Action Map Handling
    // ============================================================
    public void addAction(String key, Object value) {
        actions.put(key, value);
    }

    public Map<String, Object> getActions() {
        return actions;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getExpectedOutcome() {
        return expectedOutcome;
    }

    // ============================================================
    // Apply Scenario to TablePage (Negative Behavior)
    // ============================================================
    @Step("Apply negative scenario to Table Page")
    public void applyNegativeTo(TablePage tablePage) {

        Allure.addAttachment("Negative Scenario Instructions", instruction);
        Allure.addAttachment("Negative Scenario Actions", actions.toString());
        Allure.addAttachment("Expected Negative Outcome", expectedOutcome);

        // 1. Unsupported or invalid language
        if (actions.containsKey("language")) {
            String lang = (String) actions.get("language");
            tablePage.selectLanguage(lang);
        }

        // 2. Contradictory or invalid level combinations
        if (actions.containsKey("beginner")) {
            tablePage.setBeginner((boolean) actions.get("beginner"));
        }
        if (actions.containsKey("intermediate")) {
            tablePage.setIntermediate((boolean) actions.get("intermediate"));
        }
        if (actions.containsKey("advanced")) {
            tablePage.setAdvanced((boolean) actions.get("advanced"));
        }

        // 3. Unsupported sort column
        if (actions.containsKey("sortBy")) {
            String sort = (String) actions.get("sortBy");
            tablePage.sortBy(sort);
        }
    }

    @Override
    public String toString() {
        return "NegativeTableScenario{" +
                "instruction='" + instruction + '\'' +
                ", actions=" + actions +
                ", expectedOutcome='" + expectedOutcome + '\'' +
                '}';
    }
}