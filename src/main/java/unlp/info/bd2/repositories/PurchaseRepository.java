package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.Purchase;

public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {

    List<Purchase> findByUserUsername(String username);

    Long countByDateBetween(Date start, Date end);

} 
