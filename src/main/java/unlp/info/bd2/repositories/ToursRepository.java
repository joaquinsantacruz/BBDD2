package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

public interface ToursRepository {

    void saveRoute(Route route);
    void saveItem(ItemService item);
    void savePurchase(Purchase purchase);
    void saveDriverUser(DriverUser driverUser);
    void saveTourGuideUser(TourGuideUser tourGuideUser);
    void saveSupplier(Supplier supplier);
    void saveStop(Stop stop);
    void saveUser(User user);
    void updateRoute(Route route);
    List<Stop> getStopByNameStart(String name);
    Optional<Route> getRouteById(Long id);
    List<Route> getRoutesBelowPrice(float price);
    List<Route> getRoutesWithStop(Stop stop);
    Long getMaxStopOfRoutes();
    List<Route> getRoutesNotSell();
    List<Route> getTop3RoutesWithMaxRating();    
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    User updateUser(User user);    
    void saveService(Service service);
    Optional<Supplier> getSupplierById(Long id);
    Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber);
    List<Supplier> getTopNSuppliersInPurchases(int n);
    Optional<Service> getServiceByNameAndSupplierId(String name, Long id);
    Service getMostDemandedService();
    List<Service> getServiceNoAddedToPurchases();
    Service updateServicePriceById(Long id, float newPrice);
    List<Purchase> getAllPurchasesOfUsername(String username);
    List<User> getUserSpendingMoreThan(float amount);
}
