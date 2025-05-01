package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

public interface ToursRepository {

    void save(Object o) throws ToursException;
    void merge(Object o) throws ToursException;
    void remove(Object o) throws ToursException;


    List<Stop> getStopByNameStart(String name);
    Optional<Route> getRouteById(Long id);
    List<Route> getRoutesBelowPrice(float price);
    List<Route> getRoutesWithStop(Stop stop);
    Long getMaxStopOfRoutes();
    List<Route> getRoutesNotSell();
    List<Route> getTop3RoutesWithMaxRating();    
    Optional<User> getUserById(Long id);
    Optional<Service> getServiceById(Long id);
    Optional<User> getUserByUsername(String username);
    Optional<Supplier> getSupplierById(Long id);
    Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber);
    List<Supplier> getTopNSuppliersInPurchases(int n);
    Optional<Service> getServiceByNameAndSupplierId(String name, Long id);
    Service getMostDemandedService();
    List<Service> getServiceNoAddedToPurchases();
    Optional<Purchase> getPurchaseByCode(String code);
    Long purchasesOnRoute(Route route);
    List<Purchase> getAllPurchasesOfUsername(String username);
    List<User> getUserSpendingMoreThan(float mount);
    List<Purchase> getTop10MoreExpensivePurchasesInServices();
    Long getCountOfPurchasesBetweenDates(Date start, Date end);
    List<User> getTop5UsersMorePurchases();
    List<TourGuideUser> getTourGuidesWithRating1();
    Optional<TourGuideUser> getTourGuideByUsername(String username);
    Optional<DriverUser> getDriverUserByUsername(String username);
}
