package leonardo.labutilities.qualitylabpro.analytics;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.records.defaultvalues.DefaultRegister;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private double normalMaxValue;

    private double highMaxValue;

    public DefaultValues(DefaultRegister values) {
        this.name = values.name().toUpperCase();
        this.normalDp = values.normaldp();
        this.normalMean = values.normalmean();
        this.highDp = values.highdp();
        this.highMean = values.highmean();
        this.normalMaxValue = Math.round(values.normalmean() + (3 * values.normaldp()) * 100 / 100.00);
        this.highMaxValue = Math.round(values.highmean() + (3 * values.highdp()) * 100 / 100.00);
    }


    public static Map<String, DefaultValues> defaultValuesMap = new HashMap<String, DefaultValues>();

    public static double getTestDefaultValuesDp(String testName) {
        return defaultValuesMap.get(testName).getNormalDp();
    }

    public static double getTestDefaultValuesMeanNormal(String testName) {
        return defaultValuesMap.get(testName).getNormalMean();
    }

    public static double getTestDefaultValuesDpHigh(String testName) {
        return defaultValuesMap.get(testName).getHighDp();
    }

    public static double getTestDefaultValuesMeanHigh(String name) {
        return defaultValuesMap.get(name).getHighMean();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DefaultValues that = (DefaultValues) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
