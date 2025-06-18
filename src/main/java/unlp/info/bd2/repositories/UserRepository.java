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
        "{ $match: { $expr: { $gte: [ { $size: '$purchaseList' }, ?0 ] } } }"
    })
    List<User> getUsersWithNumberOfPurchases(int number);

    @Aggregation(pipeline = {
        "{ $addFields: { totalPurchases: { $size: '$purchaseList' } } }",
        "{ $sort: { totalPurchases: -1 } }",
    })
    List<User> getTop5UsersMorePurchases(Pageable pageable);
} 
