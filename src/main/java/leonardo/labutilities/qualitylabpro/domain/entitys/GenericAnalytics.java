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
    @Column(name = "levelLot")
    String levelLot;
    @Column(name = "testLot")
    String testLot;
    String name;
    String level;
    double value;
    double mean;
    double sd;
    @Column(name = "unitValue")
    String unitValue;
    String rules;
    String description;

    @Transient
    private final GenericValidatorComponent genericValidatorComponent;

    public GenericAnalytics(ValuesOfLevelsGenericRecord values, GenericValidatorComponent genericValidatorComponent) {
        this.date = values.date();
        this.levelLot = values.levelLot();
        this.testLot = values.testLot();
        this.name = values.name();
        this.level = values.level();
        this.value = values.value();
        this.mean = values.mean();
        this.sd = values.sd();
        this.unitValue = values.unitValue();
        this.genericValidatorComponent = genericValidatorComponent;
        genericValidatorComponent.validator(value, mean, sd);
        this.rules = genericValidatorComponent.getRules();
        this.description = genericValidatorComponent.getDescription();
    }
}
