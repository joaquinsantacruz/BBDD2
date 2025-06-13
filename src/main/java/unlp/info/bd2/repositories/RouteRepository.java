package unlp.info.bd2.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

public interface RouteRepository extends MongoRepository<Route, ObjectId> {

    @Aggregation(pipeline = {
        "{ $addFields: { stopsCount: { $size: '$stops' } } }",
        "{ $sort: {'$stopsCount: -1' } }",
        "{ $limit: 3 }"
    })
    List<Route> getTop3RoutesWithMoreStops();

    List<Route> findByStopsContaining(Stop stop);

    
}
