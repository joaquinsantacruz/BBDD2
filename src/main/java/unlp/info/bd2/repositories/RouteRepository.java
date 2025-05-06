package unlp.info.bd2.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {

    Optional<Route> getRouteById(Long id);
   
}