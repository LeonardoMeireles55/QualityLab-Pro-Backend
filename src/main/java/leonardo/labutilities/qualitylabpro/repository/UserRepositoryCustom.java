package leonardo.labutilities.qualitylabpro.repository;

import jakarta.transaction.Transactional;
import leonardo.labutilities.qualitylabpro.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryCustom extends JpaRepository<User, Long> {
    UserDetails findByUsername(String userName);
    UserDetails getReferenceByUsernameAndEmail(String userName, String Email);
    @Transactional
    @Modifying
    @Query("UPDATE users u SET u.password = ?2 WHERE u.username = ?1")
    void setPasswordWhereByUsername(String username, String newPassword);

    boolean existsByUsername(String name);

    boolean existsByEmail(String email);
}
