package unlp.info.bd2.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.TourGuideUser;

@Repository
public interface TourGuideUserRepository extends CrudRepository<TourGuideUser, Long> {
    
    Optional<TourGuideUser> getTourGuideByUsername(String username);

}