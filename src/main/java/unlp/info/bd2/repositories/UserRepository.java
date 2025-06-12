package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUsername(String username);

    @Aggregation(pipeline = {
        "{ $lookup: { from: 'purchase', localField: 'purchase_list', foreignField: '_id', as: 'purchases' } }",
        "{ $unwind: '$purchases' }",
        "{ $match: { 'purchases.totalPrice': { $gte: ?0 } } }",
        "{ $group: { _id: '$_id', doc: { $first: '$$ROOT' } } }",
        "{ $replaceRoot: { newRoot: '$doc' } }"
    })
    List<User> getUserSpendingMoreThan(double amount);

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $gte: [ { $size: '$purchase_list' }, ?0 ] } } }"
    })
    List<User> getUsersWithNumberOfPurchases(int number);

    @Aggregation(pipeline = {
        "{ $addFields: { totalPurchases: { $size: '$purchase_list' } } }",
        "{ $sort: { totalPurchases: -1 } }",
        "{ $limit: 5 }"
    })
    List<User> getTop5UsersMorePurchases();
} 
