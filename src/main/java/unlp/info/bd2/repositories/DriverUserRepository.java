package unlp.info.bd2.repositories;


import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.DriverUser;

@Repository
public interface DriverUserRepository extends CrudRepository<DriverUser, Long> {
 
    Optional<DriverUser> getDriverUserByUsername(String username);

}