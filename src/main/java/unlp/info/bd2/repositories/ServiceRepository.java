package unlp.info.bd2.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.Service;

public interface ServiceRepository extends MongoRepository<Service, ObjectId> {

    Optional<Service> findByNameAndSupplierId(String name, ObjectId id);

    
} 
