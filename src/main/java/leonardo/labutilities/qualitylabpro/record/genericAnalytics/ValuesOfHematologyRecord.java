package leonardo.labutilities.qualitylabpro.record.genericAnalytics;

import leonardo.labutilities.qualitylabpro.domain.entities.HematologyAnalytics;

public record ValuesOfHematologyRecord(
        Long id,
        String name,
        double mean,
        double sd,
        double value,
        String date,
        String level,
        String rules,
        String description

) {
    public ValuesOfHematologyRecord(HematologyAnalytics hematologyAnalytics) {
        this(hematologyAnalytics.getId(), hematologyAnalytics.getName(), hematologyAnalytics.getMean(), hematologyAnalytics.getSd(),
                hematologyAnalytics.getValue(), hematologyAnalytics.getDate(), hematologyAnalytics.getLevel(),
                hematologyAnalytics.getRules(), hematologyAnalytics.getDescription());
    }
}
