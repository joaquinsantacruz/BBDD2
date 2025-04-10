package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.*;

public interface ToursRepository {

    void saveRoute(Route route);
    void saveItem(ItemService item);
    void savePurchase(Purchase purchase);
    void saveDriverUser(DriverUser driverUser);
    void saveTourGuideUser(TourGuideUser tourGuideUser);
    void saveSupplier(Supplier supplier);
    void saveStop(Stop stop);
    void saveUser(User user);
    
    Optional<Supplier> getSupplierById(Long id);
    Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber);
    List<Supplier> getTopNSuppliersInPurchases(int n);
}
