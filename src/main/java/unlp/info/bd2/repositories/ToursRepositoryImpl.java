package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import unlp.info.bd2.model.*;

public class ToursRepositoryImpl implements ToursRepository{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public void saveItem(ItemService item) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void savePurchase(Purchase purchase) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveRoute(Route route) {
        this.getSession().persist(route);
    }

    @Override
    public void updateRoute(Route route) {
        this.getSession().merge(route);
    }
    

    @Override
    public void saveStop(Stop stop) {
        this.getSession().persist(stop);
    }
    
    @Override
    public List<Stop> getStopByNameStart(String name) {
        return this.getSession().createQuery("FROM Stop s WHERE s.name LIKE :name", Stop.class)
                                .setParameter("name", name + "%")
                                .getResultList();
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        return this.getSession().createQuery("FROM Route r WHERE r.id = :id", Route.class)
                                .setParameter("id", id)
                                .getResultStream()
                                .findFirst();
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        return this.getSession().createQuery("FROM Route r WHERE r.price < :price", Route.class)
                                .setParameter("price", price)
                                .getResultList();
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return this.getSession().createQuery("FROM Route r JOIN r.stops s WHERE s = :stop", Route.class)
                                .setParameter("stop", stop)
                                .getResultList();
    }

    @Override
    public Long getMaxStopOfRoutes() {
        return this.getSession().createQuery("SELECT max(size(r.stops)) FROM Route r JOIN r.stops", Long.class)
                                .getSingleResult();
    }

    @Override
    public List<Route> getRoutesNotSell() {
        String hql = """
                        FROM Route r 
                        WHERE NOT EXISTS (
                            FROM Purchase p
                            WHERE p.route = r)
                     """;

        return this.getSession().createQuery(hql, Route.class)
                                .getResultList();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        String hql = """
                FROM Purchase p
                WHERE p.review.rating IS NOT NULL
                GROUP BY p.route
                ORDER BY avg(p.review.rating) DESC
                """;
        return this.getSession().createQuery(hql, Route.class)
                                .setMaxResults(3)
                                .getResultList();
    }

    
    


    
    
    
    

}
