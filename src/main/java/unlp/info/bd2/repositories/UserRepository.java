package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUsername(String username);

    @Aggregation(pipeline = {
        "{ $match: { $expr: { $gte: [ { $size: '$purchase_list' }, ?0 ] } } }"
    })
    List<User> getUsersWithNumberOfPurchases(int number);

    @Aggregation(pipeline = {
        "{ $addFields: { totalPurchases: { $size: '$purchase_list' } } }",
        "{ $sort: { totalPurchases: -1 } }",
    })
    List<User> getTop5UsersMorePurchases(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $lookup: { from: 'purchases', localField: '_id', foreignField: 'user._id', as: 'purchase_list' } }",
            "{ $match: { 'purchase_list.total_price': { $gte: ?0 } } }"
    })
    List<User> getUserSpendingMoreThan(float amount);

} 
