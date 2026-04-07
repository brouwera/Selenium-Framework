package utils;

import io.qameta.allure.Allure;

import java.util.List;
import java.util.Random;

/**
 * Generates curated negative scenarios for the Course Table page.
 * This generator now excludes Min Enrollments logic because the
 * dropdown is not automation-friendly and has been removed from tests.
 */
public class NegativeScenarioGenerator {

    private static final Random RANDOM = new Random();

    // ============================================================
    // Negative Input Pools
    // ============================================================
    private static final List<String> UNSUPPORTED_LANGUAGES = List.of(
            "JavaScript", "C#", "Ruby", "Go", "TypeScript", "Swift", "Kotlin"
    );

    private static final List<String> UNSUPPORTED_SORT_COLUMNS = List.of(
            "Difficulty", "Rating", "Duration", "Instructor", "Category"
    );

    // ============================================================
    // Scenario Generation Entry Point
    // ============================================================
    public static NegativeTableScenario generateNegativeScenario() {

        String instruction = generateInstruction();
        NegativeTableScenario scenario = new NegativeTableScenario(
                instruction,
                "UI should remain stable and show either fallback behavior or no results"
        );

        // Randomly choose which negative category to include
        int mode = RANDOM.nextInt(3);

        switch (mode) {
            case 0 -> addUnsupportedLanguage(scenario);
            case 1 -> addContradictoryLevels(scenario);
            case 2 -> addUnsupportedSortColumn(scenario);
        }

        Allure.addAttachment("Generated Negative Scenario", scenario.toString());
        return scenario;
    }

    // ============================================================
    // Negative Scenario Builders
    // ============================================================

    private static void addUnsupportedLanguage(NegativeTableScenario scenario) {
        String lang = pickRandom(UNSUPPORTED_LANGUAGES);
        scenario.addAction("language", lang);
    }

    private static void addContradictoryLevels(NegativeTableScenario scenario) {
        scenario.addAction("beginner", false);
        scenario.addAction("intermediate", false);
        scenario.addAction("advanced", false);
    }

    private static void addUnsupportedSortColumn(NegativeTableScenario scenario) {
        String sort = pickRandom(UNSUPPORTED_SORT_COLUMNS);
        scenario.addAction("sortBy", sort);
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    private static String generateInstruction() {
        return "AI-generated negative scenario to validate UI stability under invalid or unsupported inputs.";
    }

    private static <T> T pickRandom(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}