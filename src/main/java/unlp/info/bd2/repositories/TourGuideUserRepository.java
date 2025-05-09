package unlp.info.bd2.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.TourGuideUser;

@Repository
public interface TourGuideUserRepository extends CrudRepository<TourGuideUser, Long> {
    
    Optional<TourGuideUser> findByUsername(String username);

    @Query("")
    List<TourGuideUser> getTourGuidesWithRating1();

}