package sweii.kochchef.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sweii.kochchef.demo.models.User;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findUserByUsernameOrEmail(String username, String email);
}
