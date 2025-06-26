package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import unlp.info.bd2.model.Supplier;

public interface SupplierRepository extends MongoRepository<Supplier, ObjectId> {

    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);

    @Query("SELECT s FROM Supplier s JOIN s.services serv JOIN serv.itemServiceList item GROUP BY s ORDER BY SUM(item.quantity) DESC")
    List<Supplier> getTopNSuppliersInPurchases(Pageable pageable);

    @Query("SELECT s FROM Supplier s JOIN s.services serv JOIN serv.itemServiceList item GROUP BY s ORDER BY SUM(item.quantity) DESC")
    List<Supplier> getTopNSuppliersItemsSold(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $project: {serviceCount: { $size: $services}}}",
            "{ $sort: {serviceCount: -1}}",
            "{ $limit: 1}"
    })
    Long getMaxServicesOfSupplier();
} 
