package leonardo.labutilities.qualitylabpro.domain.entities;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.component.HematologyValidatorComponent;
import leonardo.labutilities.qualitylabpro.record.genericAnalytics.ValuesOfHematologyRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
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
    private final HematologyValidatorComponent hematologyValidatorComponent;

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


}