package leonardo.labutilities.qualitylabpro.analytics;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevels;
import leonardo.labutilities.qualitylabpro.services.ValidatorService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Table(name = "analytics")
@Entity(name = "analytics")
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
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
    private final ValidatorService validatorService;

    @Transient
    double normalMean;
    @Transient
    double normalDp;
    @Transient
    double highMean;
    @Transient
    double highDp;

    public static Map<String, Analytics> analyticsHashMap = new HashMap<>();

    public Analytics(ValuesOfLevels values, ValidatorService validatorService) {
        this.name = values.name().toUpperCase();
        this.normalValue = values.value1();
        this.highValue = values.value2();
        this.validatorService = validatorService;
        this.data = new SimpleDateFormat("dd/MM/yy hh:mm").format(new Date());
        this.normalDp = DefaultValues.getTestDefaultValuesDp(this.name);
        this.normalMean = DefaultValues.getTestDefaultValuesMeanNormal(this.name);
        this.highDp = DefaultValues.getTestDefaultValuesDpHigh(this.name);
        this.highMean = DefaultValues.getTestDefaultValuesMeanHigh(this.name);
        this.validatorService.validationOfControlsByLevels
                (normalMean, normalDp, highMean, highDp, this.normalValue, this.highValue);
        this.normalValid = this.validatorService.getNormalValid();
        this.highValid = this.validatorService.getHighValid();
        this.normalObs = this.validatorService.getNormalObs();
        this.highObs = this.validatorService.getHighObs();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o)
                .getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Analytics analytics = (Analytics) o;
        return getId() != null && Objects.equals(getId(), analytics.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
