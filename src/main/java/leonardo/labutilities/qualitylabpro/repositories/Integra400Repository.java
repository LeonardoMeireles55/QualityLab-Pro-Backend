package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.main.Integra400;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface Integra400Repository extends JpaRepository<Integra400, Long> {
    boolean existsByName(String name);

    List<Integra400> findAllByName(String name);
    boolean existsByDateAndLevelAndName(String date, String level, String value);

    List<Integra400> findAllByNameOrderByDateAsc(String name);
    List<Integra400> findAllByNameOrderByDateDesc(String name);

    List<Integra400> findAllByLevel(String level);
}
