package unlp.info.bd2.repositories;


import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.DriverUser;

@Repository
public interface DriverUserRepository extends CrudRepository<DriverUser, Long> {
 
    Optional<DriverUser> findByUsername(String username);

    @Query("SELECT d FROM DriverUser d WHERE d.routes.size > 0 ORDER BY d.routes.size DESC")
    DriverUser getTopDriverUserWithMoreRoutes(Pageable pageable);
}
