package unlp.info.bd2.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
   
    List<Route> findByPriceLessThan(float price);

    List<Route> findByStopsContains(Stop stop);

    @Query("FROM Route r ORDER BY size(r.stops) DESC")
    List<Route> getTop3RoutesWithMoreStops(Pageable pageable);


    @Query("FROM Route r LEFT JOIN Purchase p ON p.route = r WHERE p IS NULL")
    List<Route> getRoutesNotSell();

    @Query("""
            SELECT p.route 
            FROM Purchase p 
            WHERE p.review IS NOT NULL 
            GROUP BY p.route 
            ORDER BY p.review.rating DESC
            """)
    List<Route> getTop3RoutesWithMaxAverageRating(Pageable pageable);

    @Query("""
            SELECT DISTINCT r
            FROM Route r
            JOIN Purchase p ON p.route = r
            WHERE p.review.rating = 1
            """)
    List<Route> getRoutesWithMinRating();

    @Query("""
            SELECT p.route
            FROM Purchase p
            GROUP BY p.route
            ORDER BY COUNT(p) DESC
            """)
    List<Route> getMostBestSellingRoute(Pageable pageable);

    @Query("SELECT max(size(r.stops)) FROM Route r ")
    Long getMaxStopOfRoutes();
}
