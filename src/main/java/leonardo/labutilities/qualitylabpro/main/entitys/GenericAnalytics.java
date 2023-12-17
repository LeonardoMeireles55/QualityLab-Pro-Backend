package leonardo.labutilities.qualitylabpro.main.entitys;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGeneric;
import leonardo.labutilities.qualitylabpro.services.GenericValidatorService;
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
    private final GenericValidatorService genericValidatorService;

    public GenericAnalytics(ValuesOfLevelsGeneric values, GenericValidatorService genericValidatorService) {
        this.date = values.date();
        this.level_lot = values.level_lot();
        this.test_lot = values.test_lot();
        this.name = values.name();
        this.level = values.level();
        this.value = values.value();
        this.mean = values.mean();
        this.sd = values.sd();
        this.unit_value = values.unit_value();
        this.genericValidatorService = genericValidatorService;
        this.genericValidatorService.validator(value, mean, sd);
        this.rules = genericValidatorService.getRules();
        this.description = genericValidatorService.getDescription();
    }
}
