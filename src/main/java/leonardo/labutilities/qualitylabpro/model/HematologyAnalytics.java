package leonardo.labutilities.qualitylabpro.model;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.components.HematologyValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfHematologyRecord;
import lombok.Getter;

@Getter
@Entity(name = "hematology")
public class HematologyAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String name;
    double mean;
    double sd;
    double value;
    String date;
    String level;
    String rules;
    String description;

    @Transient
    HematologyValidatorComponent hematologyValidatorComponent;

    public HematologyAnalytics() {

    };

    public HematologyAnalytics(ValuesOfHematologyRecord values, HematologyValidatorComponent hematologyValidatorComponent) {
        this.name = values.name();
        this.mean = values.mean();
        this.sd = values.sd();
        this.value = values.value();
        this.date = values.date();
        this.level = values.level();
        this.hematologyValidatorComponent = hematologyValidatorComponent;
        hematologyValidatorComponent.validator(value, mean, sd);
        this.rules = hematologyValidatorComponent.getRules();
        this.description = hematologyValidatorComponent.getDescription();
    }

    public HematologyAnalytics
            (Long id, String name, double mean, double sd, double value, String date, String level, String rules,
             String description, HematologyValidatorComponent hematologyValidatorComponent) {
        this.id = id;
        this.name = name;
        this.mean = mean;
        this.sd = sd;
        this.value = value;
        this.date = date;
        this.level = level;
        this.rules = rules;
        this.description = description;
        this.hematologyValidatorComponent = hematologyValidatorComponent;
    }
}