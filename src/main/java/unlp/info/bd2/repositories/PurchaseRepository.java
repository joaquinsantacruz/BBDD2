package unlp.info.bd2.repositories;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    
    Optional<Purchase> findByCode(String code);
    List<Purchase> findByUser_Username(String username);
    
    Long countByDateBetween(Date start, Date end);
    
    List<Purchase> findByItemServiceList_Service(Service service);
    
    Long countByRoute(Route route); //TODO: PREGUNTAR
    
    List<Purchase> findTop10ByOrderByTotalPriceDesc();


}