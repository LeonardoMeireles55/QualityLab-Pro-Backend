package leonardo.labutilities.qualitylabpro.domain.entities;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.record.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.component.GenericValidatorComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Entity(name = "generic_analytics")
public class GenericAnalytics extends
        RepresentationModel<GenericAnalytics> {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String date;
    @Column(name = "level_lot")
    String levelLot;
    @Column(name = "test_lot")
    String testLot;
    String name;
    String level;
    double value;
    double mean;
    double sd;
    @Column(name = "unit_value")
    String unitValue;
    String rules;
    String description;

    @Transient
    private final GenericValidatorComponent genericValidatorComponent;

    public GenericAnalytics(ValuesOfLevelsGenericRecord values, GenericValidatorComponent genericValidatorComponent) {
        this.date = values.date();
        this.levelLot = values.level_lot();
        this.testLot = values.test_lot();
        this.name = values.name();
        this.level = values.level();
        this.value = values.value();
        this.mean = values.mean();
        this.sd = values.sd();
        this.unitValue = values.unit_value();
        this.genericValidatorComponent = genericValidatorComponent;
        genericValidatorComponent.validator(value, mean, sd);
        this.rules = genericValidatorComponent.getRules();
        this.description = genericValidatorComponent.getDescription();
    }
}
