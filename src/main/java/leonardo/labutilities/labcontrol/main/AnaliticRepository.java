package leonardo.labutilities.labcontrol.main;

import leonardo.labutilities.labcontrol.analitics.Analitics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnaliticRepository extends JpaRepository<Analitics, Long> {
}
