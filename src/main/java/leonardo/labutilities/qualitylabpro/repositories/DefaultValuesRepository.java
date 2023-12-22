package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.domain.entitys.DefaultValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultValuesRepository extends JpaRepository<DefaultValues, Long> {
    void deleteByName(String name);
    boolean existsByName(String name);
}
