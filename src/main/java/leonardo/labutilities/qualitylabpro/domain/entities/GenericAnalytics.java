package leonardo.labutilities.qualitylabpro.domain.entities;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.components.GenericValidatorComponent;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Entity(name = "generic_analytics")
@Getter
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
    GenericValidatorComponent genericValidatorComponent;

    public GenericAnalytics() {

    };

    public GenericAnalytics
            (Long id, String date, String levelLot, String testLot, String name, String level, double value,
             double mean, double sd, String unitValue, String rules, String description,
             GenericValidatorComponent genericValidatorComponent) {
        this.id = id;
        this.date = date;
        this.levelLot = levelLot;
        this.testLot = testLot;
        this.name = name;
        this.level = level;
        this.value = value;
        this.mean = mean;
        this.sd = sd;
        this.unitValue = unitValue;
        this.rules = rules;
        this.description = description;
        this.genericValidatorComponent = genericValidatorComponent;
    }

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
