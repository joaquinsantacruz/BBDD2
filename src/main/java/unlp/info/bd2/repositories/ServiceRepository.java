package unlp.info.bd2.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.Service;

public interface ServiceRepository extends MongoRepository<Service, ObjectId> {
} 