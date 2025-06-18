package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.User;

public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {

    List<Purchase> findByUser_Id(ObjectId Id);

    Optional<Purchase> findByCode(String code);

    Long countByRoute(Route route);

    Long countByDateBetween(Date start, Date end);

    @Aggregation(pipeline = {
        "{ '$match': { 'totalPrice': { '$gte': ?0 } } }",
        "{ '$group': { '_id': '$user' } }",
        "{ '$lookup': { 'from': 'users', 'localField': '_id.$id', 'foreignField': '_id', 'as': 'user' } }",
        "{ '$unwind': '$user' }",
        "{ '$replaceRoot': { 'newRoot': '$user' } }"
    })
    List<User> getUserSpendingMoreThan(double amount);

    @Aggregation(pipeline = {
        "{ $match: { review: { $ne: null } } }",
        "{ $group: { _id: '$route.$id', averageRating: { $avg: '$review.rating' } } }",
        "{ $sort: { averageRating: -1 } }",
        "{ $lookup: { from: 'routes', localField: '_id', foreignField: '_id', as: 'route' } }",
        "{ $unwind: '$route' }",
        "{ $replaceRoot: { newRoot: '$route' } }"
    })
    List<Route> getTop3RoutesWithMaxAverageRating(Pageable pageable);

    @Aggregation(pipeline = {
        "{ $group: { _id: '$route.$id', totalSales: { $sum: 1 } } }",
        "{ $sort: { totalSales: -1 } }",
        "{ $lookup: { from: 'routes', localField: '_id', foreignField: '_id', as: 'route' } }",
        "{ $unwind: '$route' }",
        "{ $replaceRoot: { newRoot: '$route' } }"
    })
    List<Route> getMostBestSellingRoute(Pageable pageable);
}
