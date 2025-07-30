package sweii.kochchef.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sweii.kochchef.demo.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findByName(String name);

}
