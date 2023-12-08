package leonardo.labutilities.qualitylabpro.main;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGeneric;
import leonardo.labutilities.qualitylabpro.services.ValidatorServiceIntegra;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Entity(name = "generic_analytics")
public class GenericAnalytics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String date;
    String level_lot;
    String test_lot;
    String name;
    String level;
    double value;
    double mean;
    double sd;
    String unit_value;
    String rules;
    String description;

    @Transient
    private final ValidatorServiceIntegra validatorServiceIntegra;

    public GenericAnalytics(ValuesOfLevelsGeneric values, ValidatorServiceIntegra validatorServiceIntegra) {
        this.date = values.date();
        this.level_lot = values.level_lot();
        this.test_lot = values.test_lot();
        this.name = values.name();
        this.level = values.level();
        this.value = values.value();
        this.mean = values.mean();
        this.sd = values.sd();
        this.unit_value = values.unit_value();
        this.validatorServiceIntegra = validatorServiceIntegra;
        this.validatorServiceIntegra.validator(value, mean, sd);
        this.rules = validatorServiceIntegra.getRules();
        this.description = validatorServiceIntegra.getDescription();
    }
}
