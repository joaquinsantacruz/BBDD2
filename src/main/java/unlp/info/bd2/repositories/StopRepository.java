package unlp.info.bd2.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Stop;

@Repository
public interface StopRepository extends CrudRepository<Stop, Long> {
    
    @Query("")
    List<Stop> getStopByName(String name);
}
