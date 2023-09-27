package leonardo.labutilities.labcontrol.main;

import lombok.Getter;

@Getter
public class Validar {
    private String validNormal;
    private String obsNormal;
    private String validHigh;
    private String obsHigh;

    public void validacao(Double meanNormal, Double dpNormal, Double valueNormal, Double valueHigh) {
        var regra1sPositiva = meanNormal + (dpNormal * 1);
        var regra2sPositiva = meanNormal + (dpNormal * 2);
        var regra3sPositiva = meanNormal + (dpNormal * 3);

        var regra1sNegativa = meanNormal - (dpNormal * 1);
        var regra2sNegativa = meanNormal - (dpNormal * 2);
        var regra3sNegativa = meanNormal - (dpNormal * 3);


        if(valueNormal >= regra1sPositiva || valueNormal <= regra1sNegativa) {
            if(valueNormal >= regra3sPositiva || valueNormal <= regra3sNegativa) {
                this.validNormal = "Reprovado";
                this.obsNormal = "3s";
            } else if (valueNormal >= regra2sPositiva || valueNormal <= regra2sNegativa) {
                this.validNormal = "Aprovado";
                this.obsNormal = "2s";
            } else {
                this.validNormal = "Aprovado";
                this.obsNormal = "1s";
            }
        }
        if(valueHigh >= regra1sPositiva || valueHigh <= regra1sNegativa) {
            if(valueHigh >= regra3sPositiva || valueHigh <= regra3sNegativa) {
                this.validHigh = "Reprovado";
                this.obsHigh = "3s";
            } else if (valueHigh >= regra2sPositiva || valueHigh <= regra2sNegativa) {
                this.validHigh = "Aprovado";
                this.obsHigh = "2s";
            } else {
                this.validHigh = "Aprovado";
                this.obsHigh = "2s";
            }
        }
    }
}
