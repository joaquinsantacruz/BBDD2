package unlp.info.bd2.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.Service;

public interface ServiceRepository extends MongoRepository<Service, ObjectId> {

    Optional<Service> findByNameAndSupplierId(String name, ObjectId id);

    @Aggregation(pipeline = {
            "{ $group: { _id: '$supplier', totalServices: { $sum: 1 } } }",
            "{ $sort: { totalServices: -1 } }",
            "{ $limit: 1 }",
            "{ $project: { _id: 0, totalServices: 1 } }"
    })
    Long getMaxServicesOfSupplier();
} 
