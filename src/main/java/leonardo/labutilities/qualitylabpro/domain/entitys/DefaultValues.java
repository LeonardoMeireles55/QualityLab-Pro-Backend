package leonardo.labutilities.qualitylabpro.domain.entitys;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.record.defaultValues.DefaultRegisterRecord;
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
    @Column(name = "fk_lot")
    private Long fkLot;
    @Column(name = "fk_user")
    private Long fkUser;
    private String name;
    private double normalSd;
    private double normalMean;
    private double highSd;
    private double highMean;

    private double normalMaxValue;
    private double highMaxValue;

    public DefaultValues(DefaultRegisterRecord values) {
        this.name = values.name().toUpperCase();
        this.normalSd = values.normalSd();
        this.normalMean = values.normalMean();
        this.highSd = values.highSd();
        this.highMean = values.highMean();
        this.normalMaxValue = Math.round(values.normalMean() + (3 * values.normalSd()) * 100 / 100.00);
        this.highMaxValue = Math.round(values.highMean() + (3 * values.highSd()) * 100 / 100.00);
        this.fkLot = values.lotId();
        this.fkUser = values.user_id();
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
