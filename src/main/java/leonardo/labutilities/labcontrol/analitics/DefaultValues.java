package leonardo.labutilities.labcontrol.analitics;

import jakarta.persistence.*;
import leonardo.labutilities.labcontrol.records.ValuesOfRegister;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public DefaultValues(ValuesOfRegister values) {
        this.name = values.name();
        this.normalDp = values.normaldp();
        this.normalMean = values.normalmean();
    }
}