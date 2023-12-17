package leonardo.labutilities.qualitylabpro.main.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import leonardo.labutilities.qualitylabpro.records.defaultValues.DefaultRegisterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(name = "default_values")
@Getter
@Setter
@NoArgsConstructor(force = true)
public class DefaultValues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fk_lot;
    private Long fk_user;
    private String name;
    private double normalSd;
    private double normalMean;
    private double highSd;
    private double highMean;

    private double normalMaxValue;
    private double highMaxValue;

    public DefaultValues(DefaultRegisterDTO values) {
        this.name = values.name().toUpperCase();
        this.normalSd = values.normalsd();
        this.normalMean = values.normalmean();
        this.highSd = values.highsd();
        this.highMean = values.highmean();
        this.normalMaxValue = Math.round(values.normalmean() + (3 * values.normalsd()) * 100 / 100.00);
        this.highMaxValue = Math.round(values.highmean() + (3 * values.highsd()) * 100 / 100.00);
        this.fk_lot = values.lotId();
        this.fk_user = values.user_id();
        }

    public DefaultValues(String name, double normalSd, double normalMean, double highSd, double highMean) {
        this.name = name;
        this.normalSd = normalSd;
        this.normalMean = normalMean;
        this.highSd = highSd;
        this.highMean = highMean;
        this.normalMaxValue = Math.round(this.normalMean + (3 * this.normalSd) * 100 / 100.00);
        this.highMaxValue = Math.round(this.highMean + (3 * this.highSd) * 100 / 100.00);
    }

    public static Map<String, DefaultValues> defaultValuesMap = new HashMap<String, DefaultValues>();

    public static double getTestDefaultValuesNormalSp(String testName) {
        return defaultValuesMap.get(testName).getNormalSd();
    }

    public static double getTestDefaultValuesMeanNormal(String testName) {
        return defaultValuesMap.get(testName).getNormalMean();
    }

    public static double getTestDefaultValuesHighSd(String testName) {
        return defaultValuesMap.get(testName).getHighSd();
    }

    public static double getTestDefaultValuesMeanHigh(String name) {
        return defaultValuesMap.get(name).getHighMean();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer()
                .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DefaultValues that = (DefaultValues) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass().hashCode() : getClass().hashCode();
    }
}
