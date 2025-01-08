package leonardo.labutilities.qualitylabpro.dtos.analytics;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;


public record AnalyticsRecord(Long id,
							  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull LocalDateTime date,
							  @NotNull String level_lot, @NotNull String test_lot, @NotNull String name,
							  @NotNull String level, @NotNull Double value, @NotNull Double mean, @NotNull Double sd,
							  @NotNull String unit_value, String rules, String description) {}
