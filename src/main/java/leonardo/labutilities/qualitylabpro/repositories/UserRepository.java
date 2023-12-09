package leonardo.labutilities.qualitylabpro.repositories;

import jakarta.transaction.Transactional;
import leonardo.labutilities.qualitylabpro.main.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUsername(String userName);
    UserDetails getReferenceByUsernameAndEmail(String userName, String Email);
    @Transactional
    @Modifying
    @Query("UPDATE users u SET u.password = ?2 WHERE u.username = ?1")
    void setPasswordWhereByUsername(String username, String newPassword);
}
