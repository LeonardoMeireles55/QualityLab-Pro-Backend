package leonardo.labutilities.labcontrol.analitics;
import jakarta.persistence.*;
import leonardo.labutilities.labcontrol.main.Validar;
import leonardo.labutilities.labcontrol.records.ValuesOfPotassio;
import leonardo.labutilities.labcontrol.records.ValuesOfSodio;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.text.SimpleDateFormat;
import java.util.Date;

@Table(name = "Analitos")
@Entity(name = "Analitos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Analitics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String data;
    private Double valueNormal;
    private  Double valueHigh;
    private String validNormal;
    private String obsNormal;
    private String validHigh;
    private String obsHigh;
    @Transient
    double meanNormal;
    @Transient
    double dpNormal;

    public Analitics(ValuesOfSodio values) {
        this.name = "Sódio";
        this.valueNormal = values.value1();
        this.valueHigh = values.value2();
        this.dpNormal = 2.5;
        this.meanNormal = 30.5;
        BuildAnalytics();
    }

    public Analitics(ValuesOfPotassio values) {
        this.name = "Potássio";
        this.valueNormal = values.value1();
        this.valueHigh = values.value2();
        this.dpNormal = 3.5;
        this.meanNormal = 20.5;
        BuildAnalytics();
    }

    public void BuildAnalytics() {
        this.data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Validar validar = new Validar();
        validar.validacao(meanNormal, dpNormal, this.valueNormal, this.valueHigh);
        this.validNormal = validar.getValidNormal();
        this.validHigh = validar.getValidHigh();
        this.obsNormal = validar.getObsNormal();
        this.obsHigh = validar.getObsHigh();
        }
    }
