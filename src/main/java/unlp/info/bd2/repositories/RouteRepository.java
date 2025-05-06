package unlp.info.bd2.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {

    Optional<Route> getRouteById(Long id);
   
    @Query("")
    Long getMaxStopOfRoutes();
    
    @Query("")
    List<Route> getRoutesBelowPrice(float price);

    @Query("")
    List<Route> getRoutesWithStop(Stop stop);

    @Query("")
    List<Route> getRoutsNotSell();

    @Query("")
    List<Route> getTop3RoutesWithMaxRating();
}