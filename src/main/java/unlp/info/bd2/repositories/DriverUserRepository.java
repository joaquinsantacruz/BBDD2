package unlp.info.bd2.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.DriverUser;

public interface DriverUserRepository extends MongoRepository<DriverUser, ObjectId> {
} 