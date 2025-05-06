package unlp.info.bd2.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Purchase;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

    Optional<Purchase> getPurchaseByCode(String code);
  
}