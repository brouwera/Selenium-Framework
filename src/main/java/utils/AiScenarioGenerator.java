package utils;

import config.ConfigManager;
import io.qameta.allure.Allure;

import java.util.List;

/**
 * Generates AI-driven scenario suggestions, edge cases,
 * and exploratory test ideas. These are attached to Allure
 * for visibility but do not affect test execution.
 */
public final class AiScenarioGenerator {

    private AiScenarioGenerator() {}

    /**
     * Generate scenario suggestions for a given feature or test area.
     */
    public static List<String> generateScenarios(String featureName) {

        // If AI is disabled, return nothing
        if (!ConfigManager.isAiScenarioEnabled()) {
            return List.of();
        }

        return AiDataGenerator.generateScenarioSuggestions(featureName);
    }

    /**
     * Attach scenario suggestions to Allure.
     */
    public static void attachSuggestedScenarios(String featureName) {

        if (!ConfigManager.isAiScenarioEnabled()) {
            return;
        }

        List<String> scenarios = generateScenarios(featureName);

        if (scenarios.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("AI‑Suggested Scenarios for ").append(featureName).append(":\n\n");

        for (int i = 0; i < scenarios.size(); i++) {
            sb.append("• ").append(scenarios.get(i)).append("\n");
        }

        Allure.addAttachment(
                "AI Scenario Suggestions — " + featureName,
                sb.toString()
        );
    }
}