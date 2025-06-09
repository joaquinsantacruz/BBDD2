package unlp.info.bd2.persistence;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
} 