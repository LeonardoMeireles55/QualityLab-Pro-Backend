package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.main.Integra400;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Integra400Repository extends JpaRepository<Integra400, Long> {
    boolean existsByName(String name);

    List<Integra400> findAllByName(Pageable pageable, String name);
    boolean existsByDateAndLevelAndName(String date, String level, String value);

    List<Integra400> findAllByNameOrderByDateAsc(String name);
    List<Integra400> findAllByNameOrderByDateDesc(String name);

    List<Integra400> findAllByLevel(String level);

    List<Integra400> findAllByNameAndLevel(Pageable pageable, String name, String Level);
}
