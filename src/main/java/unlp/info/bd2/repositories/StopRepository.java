package unlp.info.bd2.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Stop;

@Repository
public interface StopRepository extends CrudRepository<Stop, Long> {
    
}
