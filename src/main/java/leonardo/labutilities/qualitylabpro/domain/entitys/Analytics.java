package leonardo.labutilities.qualitylabpro.domain.entitys;

import jakarta.persistence.*;
import leonardo.labutilities.qualitylabpro.record.valuesOfAnalytics.ValuesOfLevelsRecord;
import leonardo.labutilities.qualitylabpro.component.AnalyticsValidatorComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity(name = "analytics")
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Analytics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fk_default_values")
    private Long fkDefaultValues;
    @Column(name = "fk_user")
    private Long fkUser;
    private String name;
    private Date date;
    @Column(name = "normal_value")
    private Double normalValue;
    @Column(name = "high_value")
    private Double highValue;
    @Column(name = "normal_valid")
    private String normalValid;
    @Column(name = "normal_obs")
    private String normalObs;
    @Column(name = "high_valid")
    private String highValid;
    @Column(name = "high_obs")
    private String highObs;

    @Transient
    private final AnalyticsValidatorComponent analyticsValidatorComponent;

    @Transient
    double normalMean;
    @Transient
    double normalDp;
    @Transient
    double highMean;
    @Transient
    double highDp;

    public static Map<String, Analytics> analyticsHashMap = new HashMap<>();

    public Analytics(ValuesOfLevelsRecord values, AnalyticsValidatorComponent analyticsValidatorComponent) {
        this.name = values.name().toUpperCase();
        this.normalValue = values.value1();
        this.highValue = values.value2();
        this.analyticsValidatorComponent = analyticsValidatorComponent;
        this.normalDp = DefaultValues.getTestDefaultValuesNormalSp(this.name);
        this.normalMean = DefaultValues.getTestDefaultValuesMeanNormal(this.name);
        this.highDp = DefaultValues.getTestDefaultValuesHighSd(this.name);
        this.highMean = DefaultValues.getTestDefaultValuesMeanHigh(this.name);
        this.analyticsValidatorComponent.validationOfControlsByLevels
                (normalMean, normalDp, highMean, highDp, this.normalValue, this.highValue);
        this.normalValid = this.analyticsValidatorComponent.getNormalValid();
        this.highValid = this.analyticsValidatorComponent.getHighValid();
        this.normalObs = this.analyticsValidatorComponent.getNormalObs();
        this.highObs = this.analyticsValidatorComponent.getHighObs();
        this.fkDefaultValues = values.defaultId();
        this.fkUser = values.userId();
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
