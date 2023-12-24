package leonardo.labutilities.qualitylabpro.domain.entitys;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.record.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.component.GenericValidatorComponent;
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
    private final GenericValidatorComponent genericValidatorComponent;

    public GenericAnalytics(ValuesOfLevelsGenericRecord values, GenericValidatorComponent genericValidatorComponent) {
        this.date = values.date();
        this.level_lot = values.level_lot();
        this.test_lot = values.test_lot();
        this.name = values.name();
        this.level = values.level();
        this.value = values.value();
        this.mean = values.mean();
        this.sd = values.sd();
        this.unit_value = values.unit_value();
        this.genericValidatorComponent = genericValidatorComponent;
        genericValidatorComponent.validator(value, mean, sd);
        this.rules = genericValidatorComponent.getRules();
        this.description = genericValidatorComponent.getDescription();
    }
}
