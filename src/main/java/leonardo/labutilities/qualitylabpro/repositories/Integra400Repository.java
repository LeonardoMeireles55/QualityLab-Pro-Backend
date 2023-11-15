package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.main.Integra400;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Integra400Repository extends JpaRepository<Integra400, Long> {
    boolean existsByName(String name);

    List<Integra400> findAllByName(String name);

    List<Integra400> findAllByLevel(String level);

}
