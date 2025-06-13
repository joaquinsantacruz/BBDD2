package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;

public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {

    List<Purchase> findByUserUsername(String username);

    boolean existsByCode(String code);

    Long countByRouteAndDate(Route route, Date date);

    Long countByDateBetween(Date start, Date end);

} 
