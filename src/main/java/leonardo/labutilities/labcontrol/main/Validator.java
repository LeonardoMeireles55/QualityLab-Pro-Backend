package leonardo.labutilities.labcontrol.main;

import lombok.Getter;

@Getter
public class Validator {
    private String normalValid;
    private String normalObs;
    private String highValid;
    private String highObs;

    public void validacao(Double meanNormal, Double dpNormal, Double valueNormal, Double valueHigh) {
        var regra1sPositiva = meanNormal + (dpNormal * 1);
        var regra2sPositiva = meanNormal + (dpNormal * 2);
        var regra3sPositiva = meanNormal + (dpNormal * 3);

        var regra1sNegativa = meanNormal - (dpNormal * 1);
        var regra2sNegativa = meanNormal - (dpNormal * 2);
        var regra3sNegativa = meanNormal - (dpNormal * 3);


        if(valueNormal >= regra1sPositiva || valueNormal <= regra1sNegativa) {
            if(valueNormal >= regra3sPositiva || valueNormal <= regra3sNegativa) {
                this.normalValid = "Reprovado";
                this.normalObs = "3s";
            } else if (valueNormal >= regra2sPositiva || valueNormal <= regra2sNegativa) {
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
        if(valueHigh >= regra1sPositiva || valueHigh <= regra1sNegativa) {
            if(valueHigh >= regra3sPositiva || valueHigh <= regra3sNegativa) {
                this.highValid = "Reprovado";
                this.highObs = "3s";
            } else if (valueHigh >= regra2sPositiva || valueHigh <= regra2sNegativa) {
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
