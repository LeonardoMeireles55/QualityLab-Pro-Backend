package leonardo.labutilities.labcontrol.main;

import leonardo.labutilities.labcontrol.analitics.DefaultValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultRepositoyStatic extends JpaRepository<DefaultValues, String> {
}
