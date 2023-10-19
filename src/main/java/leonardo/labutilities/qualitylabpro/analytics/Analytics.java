package leonardo.labutilities.qualitylabpro.analytics;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevels;
import leonardo.labutilities.qualitylabpro.services.ValidatorService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Table(name = "analytics")
@Entity(name = "analytics")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Analytics {
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

    public static Map<String, Analytics> analyticsHashMap = new HashMap<>();

    public Analytics(ValuesOfLevels values) {
        this.name = values.name().toUpperCase();
        this.normalValue = values.value1();
        this.highValue = values.value2();
        this.normalDp = DefaultValues.getTestDefaultValuesDp(this.name);
        this.normalMean = DefaultValues.getTestDefaultValuesMeanNormal(this.name);
        this.highDp = DefaultValues.getTestDefaultValuesDpHigh(this.name);
        this.highMean = DefaultValues.getTestDefaultValuesMeanHigh(this.name);
        BuildAnalytics();
    }

    public void BuildAnalytics() {
        this.data = new SimpleDateFormat("dd/MM/yy hh:mm").format(new Date());
        ValidatorService validatorService = new ValidatorService();
        validatorService.validationOfControlsByLevels(normalMean, normalDp, highMean, highDp, this.normalValue, this.highValue);
        this.normalValid = validatorService.getNormalValid();
        this.highValid = validatorService.getHighValid();
        this.normalObs = validatorService.getNormalObs();
        this.highObs = validatorService.getHighObs();
        }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Analytics analytics = (Analytics) o;
        return getId() != null && Objects.equals(getId(), analytics.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
