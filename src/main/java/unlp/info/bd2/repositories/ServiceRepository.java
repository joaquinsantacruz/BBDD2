package unlp.info.bd2.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {
    
    Optional<Service> findByNameAndSupplierId(String name, Long id);

    List<Service> findAllByItemServiceListIsEmpty();

    @Query("SELECT is.service FROM ItemService is GROUP BY is.service ORDER BY SUM(is.quantity) DESC")
    List<Service> getMostDemandedService(Pageable pageable);

}
