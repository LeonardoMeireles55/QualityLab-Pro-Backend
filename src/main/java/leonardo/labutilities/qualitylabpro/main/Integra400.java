package leonardo.labutilities.qualitylabpro.main;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.records.integra.ValuesOfLevelsIntegra;
import leonardo.labutilities.qualitylabpro.services.ValidatorServiceIntegra;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Entity(name = "integra_400")
public class Integra400 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    Date date;
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
//    String format = "dd-MM-yyyy HH:mm:ss";
//    SimpleDateFormat sdf = new SimpleDateFormat(format);

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
