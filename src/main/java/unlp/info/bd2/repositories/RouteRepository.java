package unlp.info.bd2.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

public interface RouteRepository extends MongoRepository<Route, ObjectId> {

    @Query("FROM Route r ORDER BY size(r.stops) DESC")
    List<Route> getTop3RoutesWithMoreStops(Pageable pageable);

    @Query("SELECT max(size(r.stops)) FROM Route r ")
    Long getMaxStopOfRoutes();

    List<Route> findByStopsContaining(Stop stop);

    List<Route> findByPriceLessThan(float price);

    
}
