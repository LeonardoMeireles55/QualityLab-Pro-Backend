package leonardo.labutilities.qualitylabpro.dto.analytics;

import leonardo.labutilities.qualitylabpro.infra.config.exception.CustomGlobalErrorHandling;

import java.util.List;

public record GenericValuesGroupByLevel(
    String level,
    List<GenericValuesRecord> values
) {
    public GenericValuesGroupByLevel {
        level = convertLevel(level);
    }
    private String convertLevel(String inputLevel) {
        return switch (inputLevel) {
            case "PCCC1", "Normal C. Assayed", "low", "1" -> "1";
            case "PCCC2", "Low Abn C. Assayed", "normal", "2" -> "2";
            case "high", "3" -> "3";
            default -> throw new CustomGlobalErrorHandling.ResourceNotFoundException(
                    "Level not found."
            );
        };
    }
} 