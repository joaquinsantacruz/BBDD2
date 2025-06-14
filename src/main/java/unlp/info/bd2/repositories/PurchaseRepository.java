package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;

public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {

    List<Purchase> findByUser_Username(String username);

    Optional<Purchase> findByCode(String code);

    Long countByRoute(Route route);

    Long countByDateBetween(Date start, Date end);

} 
