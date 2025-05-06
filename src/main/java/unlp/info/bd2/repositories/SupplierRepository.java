package unlp.info.bd2.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Supplier;

@Repository
public interface SupplierRepository extends CrudRepository<Supplier, Long> {
    
    Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber);

    Optional<Supplier> getSupplierById(Long id);

    @Query("")
    List<Supplier> getTopNSuppliersInPurchases(int n);

    
}
