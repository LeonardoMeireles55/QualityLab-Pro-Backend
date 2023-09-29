package leonardo.labutilities.labcontrol.analitics;
import jakarta.persistence.*;
import leonardo.labutilities.labcontrol.controller.DefaultValuesManager;
import leonardo.labutilities.labcontrol.main.Validator;
import leonardo.labutilities.labcontrol.records.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.text.SimpleDateFormat;
import java.util.Date;

@Table(name = "analytics")
@Entity(name = "analytics")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Analitics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String data;
    private Double normalValue;
    private Double highValue;
    private String normalValid;
    private String normalObs;
    private String highValid;
    private String highObs;
    @Transient
    double normalMean;
    @Transient
    double normalDp;
    @Transient
    double highMean;
    @Transient
    double highDp;


    public Analitics(ValuesOfLevels values) {
        this.name = values.name();
        this.normalValue = values.value1();
        this.highValue = values.value2();
        this.normalDp = DefaultValuesManager.getTestDefaultValuesDp(name);
        this.normalMean = DefaultValuesManager.getTestDefaultValuesMeanNormal(name);
        this.highDp = DefaultValuesManager.getTestDefaultValuesDpHigh(name);
        this.highMean = DefaultValuesManager.getTestDefaultValuesMeanHigh(name);
        BuildAnalytics();
    }

    public void BuildAnalytics() {
        this.data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Validator validator = new Validator();
        validator.validacao(normalMean, normalDp, highMean, highDp, this.normalValue, this.highValue);
        this.normalValid = validator.getNormalValid();
        this.highValid = validator.getHighValid();
        this.normalObs = validator.getNormalObs();
        this.highObs = validator.getHighObs();
        }
    }
