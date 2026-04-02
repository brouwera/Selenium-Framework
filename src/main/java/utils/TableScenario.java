package utils;

import io.qameta.allure.Step;
import pages.TablePage;

import java.util.List;
import java.util.Map;

/**
 * Represents an AI-generated table scenario including:
 * - A natural language instruction (sorting/filtering)
 * - The expected resulting dataset after applying the instruction
 * - A helper method to apply the instruction to the TablePage
 *
 * This class is intentionally lightweight and framework-aligned.
 */
public class TableScenario {

    // ============================================================
    // Fields
    // ============================================================
    private final String instruction;
    private final List<Map<String, String>> expectedRows;
    private final Map<String, Object> actions;

    // ============================================================
    // Constructor
    // ============================================================
    public TableScenario(String instruction,
                         List<Map<String, String>> expectedRows,
                         Map<String, Object> actions) {
        this.instruction = instruction;
        this.expectedRows = expectedRows;
        this.actions = actions;
    }

    // ============================================================
    // Getters
    // ============================================================
    public String getInstruction() {
        return instruction;
    }

    public List<Map<String, String>> getExpectedRows() {
        return expectedRows;
    }

    public Map<String, Object> getActions() {
        return actions;
    }

    // ============================================================
    // Apply Scenario to TablePage
    // ============================================================
    @Step("Apply AI-generated table scenario to UI")
    public void applyTo(TablePage page) {

        // Language filter
        if (actions.containsKey("language")) {
            String lang = (String) actions.get("language");
            page.selectLanguage(lang);
        }

        // Level filters
        if (actions.containsKey("beginner")) {
            page.setBeginner((Boolean) actions.get("beginner"));
        }
        if (actions.containsKey("intermediate")) {
            page.setIntermediate((Boolean) actions.get("intermediate"));
        }
        if (actions.containsKey("advanced")) {
            page.setAdvanced((Boolean) actions.get("advanced"));
        }

        // Minimum enrollments
        if (actions.containsKey("minEnrollments")) {
            String min = String.valueOf(actions.get("minEnrollments"));
            page.setMinEnrollments(min);
        }

        // Sorting
        if (actions.containsKey("sortBy")) {
            String sortColumn = (String) actions.get("sortBy");
            page.sortBy(sortColumn);
        }
    }
}