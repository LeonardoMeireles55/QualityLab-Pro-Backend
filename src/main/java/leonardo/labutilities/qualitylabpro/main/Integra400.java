package leonardo.labutilities.qualitylabpro.main;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.records.integra.ValuesOfLevelsIntegra;
import leonardo.labutilities.qualitylabpro.services.ValidatorServiceIntegra;
import lombok.*;


@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Entity(name = "integra_400")
public class Integra400 {
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

    public Integra400(ValuesOfLevelsIntegra values, ValidatorServiceIntegra validatorServiceIntegra) {
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
