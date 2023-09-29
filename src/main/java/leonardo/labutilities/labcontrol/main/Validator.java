package leonardo.labutilities.labcontrol.main;

import lombok.Getter;

@Getter
public class Validator {
    private String normalValid;
    private String normalObs;
    private String highValid;
    private String highObs;

    public void validacao(Double normalMean, Double normalDp, Double highMean, Double highDp, Double normalValue, Double highValue) {
        var regra1sPositivaNormal = normalMean + (normalDp * 1);
        var regra2sPositivaNormal = normalMean + (normalDp * 2);
        var regra3sPositivaNormal = normalMean + (normalDp * 3);

        var regra1sNegativaNormal = normalMean - (normalDp * 1);
        var regra2sNegativaNormal = normalMean - (normalDp * 2);
        var regra3sNegativaNormal = normalMean - (normalDp * 3);

        var regra1sPositivaHigh = highMean + (highDp * 1);
        var regra2sPositivaHigh = highMean + (highDp * 2);
        var regra3sPositivaHigh = highMean + (highDp * 3);

        var regra1sNegativaHigh = highMean - (highDp * 1);
        var regra2sNegativaHigh = highMean - (highDp * 2);
        var regra3sNegativaHigh = highMean - (highDp * 3);


        if(normalValue >= regra1sPositivaNormal || normalValue <= regra1sNegativaNormal) {
            if(normalValue >= regra3sPositivaNormal || normalValue <= regra3sNegativaNormal) {
                this.normalValid = "Reprovado";
                this.normalObs = "3s";
            } else if (normalValue >= regra2sPositivaNormal || normalValue <= regra2sNegativaNormal) {
                this.normalValid = "Aprovado";
                this.normalObs = "2s";
            } else {
                this.normalValid = "Aprovado";
                this.normalObs = "1s";
            }
        } else {
            this.normalValid = "Aprovado";
            this.normalObs = "media";
        }
        if(highValue >= regra1sPositivaHigh || highValue <= regra1sNegativaHigh) {
            if(highValue >= regra3sPositivaHigh || highValue <= regra3sNegativaHigh) {
                this.highValid = "Reprovado";
                this.highObs = "3s";
            } else if (highValue >= regra2sPositivaHigh || highValue <= regra2sNegativaHigh) {
                this.highValid = "Aprovado";
                this.highObs = "2s";
            } else {
                this.highValid = "Aprovado";
                this.highObs = "1s";
            }
        } else {
            this.highValid = "Aprovado";
            this.highObs = "media";
        }
    }
}
