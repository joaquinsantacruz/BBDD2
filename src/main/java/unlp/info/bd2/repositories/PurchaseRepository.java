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

    Long countByRouteIdAndDate(ObjectId id, Date today);

    Long countByDateBetween(Date start, Date end);

}
