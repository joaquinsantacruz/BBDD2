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

    // Posible arreglo
    @Aggregation(pipeline = {
        "{ $match: { review: { $ne: null } } }",
        "{ $group: { _id: '$route.$id', averageRating: { $avg: '$review.rating' } } }",
        "{ $sort: { averageRating: -1 } }",
        "{ $lookup: { from: 'routes', localField: '_id', foreignField: '_id', as: 'route' } }",
        "{ $unwind: '$route' }",
        "{ $replaceRoot: { newRoot: '$route' } }"
    })
    List<Route> getTop3RoutesWithMaxAverageRating(Pageable pageable);

    // Posible arreglo
    @Aggregation(pipeline = {
        "{ $group: { _id: '$route.$id', totalSales: { $sum: 1 } } }",
        "{ $sort: { totalSales: -1 } }",
        "{ $lookup: { from: 'routes', localField: '_id', foreignField: '_id', as: 'route' } }",
        "{ $unwind: '$route' }",
        "{ $replaceRoot: { newRoot: '$route' } }"
    })
    List<Route> getMostBestSellingRoute(Pageable pageable);
    
}
