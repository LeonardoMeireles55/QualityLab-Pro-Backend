package leonardo.labutilities.qualitylabpro.components;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Getter
@Component
public class GenericValidatorComponent {
    String description;
    String rules;
    public void validator(Double value, Double mean, Double sd) {
        double positive1sRuleNormal = mean + (sd * 1);
        double positive2sRuleNormal = mean + (sd * 2);
        double positive3sRuleNormal = mean + (sd * 3);

        double negative1sRuleNormal = mean - (sd * 1);
        double negative2sRuleNormal = mean - (sd * 2);
        double negative3sRuleNormal = mean - (sd * 3);

        if (value >= positive1sRuleNormal || value <= negative1sRuleNormal) {
            if (value >= positive3sRuleNormal || value <= negative3sRuleNormal) {
                if (value <= negative3sRuleNormal) {
                    this.description = "Failed";
                    this.rules = "-3s";
                } else {
                    this.description = "Failed";
                    this.rules = "+3s";
                }

            } else if (value >= positive2sRuleNormal || value <= negative2sRuleNormal) {
                if (value <= negative2sRuleNormal) {
                    this.description = "Approved";
                    this.rules = "-2s";
                } else {
                    this.description = "Approved";
                    this.rules = "+2s";
                }
            } else {
                if (value >= positive1sRuleNormal) {
                    this.description = "Approved";
                    this.rules = "+1s";
                } else {
                    this.description = "Approved";
                    this.rules = "-1s";
                }
            }
        } else {
            this.description = "Approved";
            this.rules = "Average";
        }
    }
}
