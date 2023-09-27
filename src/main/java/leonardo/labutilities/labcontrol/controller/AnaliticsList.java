package leonardo.labutilities.labcontrol.controller;
import leonardo.labutilities.labcontrol.analitics.Analitics;

public record AnaliticsList(String name, String date, Double valueNormal, Double valueHigh, String validNormal, String validHigh) {
    public AnaliticsList(Analitics analitics) {
        this(analitics.getName(), analitics.getData(), analitics.getValueNormal(), analitics.getValueHigh(), analitics.getValidNormal(), analitics.getValidHigh());
    }
}
