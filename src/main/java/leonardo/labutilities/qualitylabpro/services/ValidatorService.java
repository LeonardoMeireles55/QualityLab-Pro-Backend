package leonardo.labutilities.qualitylabpro.services;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ValidatorService {
    private String normalValid;
    private String normalObs;
    private String highValid;
    private String highObs;

    public void validationOfControlsByLevels(Double normalMean, Double normalSd,
                                             Double highMean, Double highSd,
                                             Double normalValue, Double highValue) {

        double positive1sRuleNormal = normalMean + (normalSd * 1);
        double positive2sRuleNormal = normalMean + (normalSd * 2);
        double positive3sRuleNormal = normalMean + (normalSd * 3);

        double negative1sRuleNormal = normalMean - (normalSd * 1);
        double negative2sRuleNormal = normalMean - (normalSd * 2);
        double negative3sRuleNormal = normalMean - (normalSd * 3);

        double positive1sRuleHigh = highMean + (highSd * 1);
        double positive2sRuleHigh = highMean + (highSd * 2);
        double positive3sRuleHigh = highMean + (highSd * 3);

        double negative1sRuleHigh = highMean - (highSd * 1);
        double negative2sRuleHigh = highMean - (highSd * 2);
        double negative3sRuleHigh = highMean - (highSd * 3);

        if (normalValue >= positive1sRuleNormal || normalValue <= negative1sRuleNormal) {
            if (normalValue >= positive3sRuleNormal || normalValue <= negative3sRuleNormal) {
                if (normalValue <= negative3sRuleNormal) {
                    this.normalValid = "Failed";
                    this.normalObs = "-3s";
                } else {
                    this.normalValid = "Failed";
                    this.normalObs = "+3s";
                }

            } else if (normalValue >= positive2sRuleNormal || normalValue <= negative2sRuleNormal) {
                if (normalValue <= negative2sRuleNormal) {
                    this.normalValid = "Approved";
                    this.normalObs = "-2s";
                } else {
                    this.normalValid = "Approved";
                    this.normalObs = "+2s";
                }
            } else {
                if (normalValue >= positive1sRuleNormal) {
                    this.normalValid = "Approved";
                    this.normalObs = "+1s";
                } else {
                    this.normalValid = "Approved";
                    this.normalObs = "-1s";
                }
            }
        } else {
            this.normalValid = "Approved";
            this.normalObs = "Average";
        }

        if (highValue >= positive1sRuleHigh || highValue <= negative1sRuleHigh) {
            if (highValue >= positive3sRuleHigh || highValue <= negative3sRuleHigh) {
                if (highValue <= negative3sRuleHigh) {
                    this.highValid = "Failed";
                    this.highObs = "-3s";
                } else {
                    this.highValid = "Failed";
                    this.highObs = "+3s";
                }

            } else if (highValue >= positive2sRuleHigh || highValue <= negative2sRuleHigh) {
                if (highValue <= negative2sRuleHigh) {
                    this.highValid = "Approved";
                    this.highObs = "-2s";
                } else {
                    this.highValid = "Approved";
                    this.highObs = "+2s";
                }
            } else {
                if (highValue >= positive1sRuleHigh) {
                    this.highValid = "Approved";
                    this.highObs = "+1s";
                } else {
                    this.highValid = "Approved";
                    this.highObs = "-1s";
                }
            }
        } else {
            this.highValid = "Approved";
            this.highObs = "Average";
        }
    }
}
