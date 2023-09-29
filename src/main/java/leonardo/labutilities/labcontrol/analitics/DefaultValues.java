package leonardo.labutilities.labcontrol.analitics;

import jakarta.persistence.*;
import leonardo.labutilities.labcontrol.records.ValuesOfRegister;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "default_values")
@Table(name = "default_values")
@Getter
@Setter
@NoArgsConstructor
public class DefaultValues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private double normalDp;
    private double normalMean;
    private double highDp;
    private double highMean;

    public DefaultValues(ValuesOfRegister values) {
        this.name = values.name();
        this.normalDp = values.normaldp();
        this.normalMean = values.normalmean();
        this.highDp = values.highdp();
        this.highMean = values.highmean();
    }
    public DefaultValues(List<ValuesOfRegister> valuesList) {
        for(int i = 0; i <= 4; i++) {
            this.name = valuesList.get(i).name();
            this.normalDp = valuesList.get(i).normaldp();
            this.normalMean = valuesList.get(i).normalmean();
            this.highDp = valuesList.get(i).highdp();
            this.highMean = valuesList.get(i).highmean();
        }
    }
}