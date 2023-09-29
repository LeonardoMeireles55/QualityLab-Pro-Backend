package leonardo.labutilities.labcontrol.controller;

import jakarta.persistence.*;
import leonardo.labutilities.labcontrol.analitics.DefaultValues;

import java.util.HashMap;
import java.util.Map;
@Table(name = "DefaultValues")
@Entity(name = "DefaultValues")
public class DefaultValuesManager extends DefaultValuesController {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Transient
    private static DefaultValuesController dvc;

    public static Map<String, DefaultValues> defaultValuesMap = new HashMap<String, DefaultValues>();

//    public DefaultValuesManager() {
//        defaultValuesMap.put("sodio", new DefaultValues(2.5, 30.5));
//        defaultValuesMap.put("potassio", new DefaultValues(3.5, 20.5));
//    }

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
}
