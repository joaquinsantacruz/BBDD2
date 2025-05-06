package unlp.info.bd2.repositories;


import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {
    
    Optional<Service> getServiceById(Long id);

    @Query("")
    Service getMostDemandedService();

    @Query("")
    Optional<Service> getServiceByNameAndSupplierId(String name, Long id);

    @Query("")
    List<Service> getServiceNoAddedToPurchases();
}