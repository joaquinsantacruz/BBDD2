package unlp.info.bd2.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

public interface RouteRepository extends MongoRepository<Route, ObjectId> {
    
    List<Route> findByPriceLessThan(float price);

    List<Route> findByStopsContaining(Stop stop);
    
    @Aggregation(pipeline = {
        "{ $addFields: { stopsCount: { $size: '$stops' } } }",
        "{ $sort: { stopsCount: -1 } }"
    })
    List<Route> getTop3RoutesWithMoreStops(Pageable pageable);

    @Aggregation(pipeline = {
        "{ $project: { _id: 0, stopsCount: { $size: '$stops' } } }",
        "{ $group: { _id: null, maxStops: { $max: '$stopsCount' } } }"
    })
    Long getMaxStopOfRoutes();

    @Query("FROM Route r ORDER BY size(r.stops) DESC")
    List<Route> getTop3RoutesWithMaxAverageRating(Pageable pageable);

    @Query("""
            SELECT p.route
            FROM Purchase p
            GROUP BY p.route
            ORDER BY COUNT(p) DESC
            """)
    List<Route> getMostBestSellingRoute(Pageable pageable);
    
}
