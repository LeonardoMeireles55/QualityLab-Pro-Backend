package leonardo.labutilities.labcontrol.analitics;
import leonardo.labutilities.labcontrol.analitics.Analitics;

public record AnaliticsList(String name, String date, Double normalValue, Double highNormal, String normalValid, String highValid, String normalObs, String highObs) {
    public AnaliticsList(Analitics analitics) {
        this(analitics.getName(), analitics.getData(), analitics.getNormalValue(), analitics.getHighValue(), analitics.getNormalValid(), analitics.getHighValid(), analitics.getNormalObs(), analitics.getHighObs());
    }
}
