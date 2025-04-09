package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.*;

public interface ToursRepository {

    void saveRoute(Route route);
    void saveItem(ItemService item);
    void savePurchase(Purchase purchase);
    void saveStop(Stop stop);
    void updateRoute(Route route);
    List<Stop> getStopByNameStart(String name);
    Optional<Route> getRouteById(Long id);
    List<Route> getRoutesBelowPrice(float price);
    List<Route> getRoutesWithStop(Stop stop);
    Long getMaxStopOfRoutes();
    List<Route> getRoutesNotSell();
    List<Route> getTop3RoutesWithMaxRating();
}
