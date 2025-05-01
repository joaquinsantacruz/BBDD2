package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

public class ToursRepositoryImpl implements ToursRepository{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Object o) throws ToursException{
        try{
            this.getSession().persist(o);
        }
        catch(ConstraintViolationException cve){
            throw new ToursException("Constraint Violation");
        }
        catch(Exception e){
            throw new ToursException("Se produjo otro error");
        }
        
    }

    @Override
    public void merge(Object o) throws ToursException{
        try{
            this.getSession().merge(o);
        }
        catch(ConstraintViolationException cve){
            throw new ToursException("Constraint Violation");
        }
        catch(Exception e){
            throw new ToursException("Se produjo otro error");
        }
    }

    @Override
    public void remove(Object o) throws ToursException{
        try{
            this.getSession().remove(o);
        }
        catch(ConstraintViolationException cve){
            throw new ToursException("Constraint Violation");
        }
        catch(Exception e){
            throw new ToursException("Se produjo otro error");
        }
    }


    public Optional<User> getUserById(Long id){
        return Optional.ofNullable(this.getSession().get(User.class, id));
    }

    public Optional<User> getUserByUsername(String username) {
        return this.getSession().createQuery("FROM User WHERE username = :username", User.class)
                                .setParameter("username", username)
                                .uniqueResultOptional();
    }


    @Override
    public List<Stop> getStopByNameStart(String name) {
        return this.getSession().createQuery("FROM Stop s WHERE s.name LIKE :name", Stop.class)
                                .setParameter("name", name + "%")
                                .getResultList();
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        return Optional.ofNullable(this.getSession().get(Route.class, id));
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        return this.getSession().createQuery("FROM Route r WHERE r.price < :price", Route.class)
                                .setParameter("price", price)
                                .getResultList();
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return this.getSession().createQuery("SELECT DISTINCT r FROM Route r JOIN r.stops s WHERE s = :stop", Route.class)
                                .setParameter("stop", stop)
                                .getResultList();
    }

    @Override
    public Long getMaxStopOfRoutes() {
        Integer amount = this.getSession().createQuery("SELECT max(size(r.stops)) FROM Route r", Integer.class)
                                .getSingleResult();
        return amount.longValue();
    }
    
    @Override
    public Optional<Supplier> getSupplierById(Long id){
        return Optional.ofNullable(this.getSession().get(Supplier.class, id));
    }

    @Override
    public Optional<Service> getServiceById(Long id){
        return Optional.ofNullable(this.getSession().get(Service.class, id));
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber){
        String hql = "FROM Supplier s WHERE s.authorizationNumber = :authorizationNumber";
        return this.getSession()
                    .createQuery(hql, Supplier.class)
                    .setParameter("authorizationNumber", authorizationNumber)
                    .uniqueResultOptional();
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        String hql = """
        FROM Supplier s
        JOIN s.services serv
        JOIN serv.itemServiceList item
        GROUP BY s
        ORDER BY SUM(item.quantity) DESC
        """;
        return this.getSession()
        .createQuery(hql, Supplier.class)
        .setMaxResults(n)
        .getResultList();
    }

    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id){
        String hql = "FROM Service s WHERE s.name = :name and s.supplier.id = :id";
        return this.getSession()
                    .createQuery(hql, Service.class)
                    .setParameter("name", name)
                    .setParameter("id", id)
                    .uniqueResultOptional();
    }
    
    @Override
    public Service getMostDemandedService(){
        String hql = "SELECT is.service FROM ItemService is GROUP BY is.service ORDER BY SUM(is.quantity) DESC ";
        return this.getSession()
                    .createQuery(hql, Service.class)
                    .setMaxResults(1)
                    .uniqueResult();
    }

    @Override
    public List<Service> getServiceNoAddedToPurchases(){
        String hql = "FROM Service s WHERE size(s.itemServiceList) = 0";
        return this.getSession()
                    .createQuery(hql, Service.class)
                    .getResultList();
    }

    @Override
    public List<Route> getRoutesNotSell() {
        String hql = """

                        FROM Route r 
                        LEFT JOIN Purchase p ON p.route = r
                        WHERE p IS NULL
                     """;

        return this.getSession().createQuery(hql, Route.class)
                                .getResultList();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        String hql = """
                SELECT p.route
                FROM Purchase p
                WHERE p.review IS NOT NULL
                GROUP BY p.route
                ORDER BY avg(p.review.rating) DESC
                """;
        return this.getSession().createQuery(hql, Route.class)
                                .setMaxResults(3)
                                .getResultList();
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code){
        return this.getSession().createQuery("FROM Purchase p WHERE p.code = :code", Purchase.class)
                                .setParameter("code", code)
                                .uniqueResultOptional();
    }
    
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return this.getSession().createQuery("FROM Purchase p WHERE p.user.username = :username", Purchase.class)
                                .setParameter("username", username)
                                .getResultList();
    }
    
    @Override
    public List<User> getUserSpendingMoreThan(float amount) {
        String hql = """
                SELECT p.user 
                FROM Purchase p 
                WHERE p.totalPrice >= :amount
                """;
        return this.getSession()
                    .createQuery(hql, User.class)
                    .setParameter("amount", amount)
                    .getResultList();
    }

    @Override
    public List<User> getTop5UsersMorePurchases() {
        String hql = """
                SELECT p.user 
                FROM Purchase p 
                GROUP BY p.user 
                ORDER BY COUNT(p) DESC
                """;
        return this.getSession()
                    .createQuery(hql, User.class)
                    .setMaxResults(5)
                    .getResultList();
    }

    @Override
    public Long purchasesOnRoute(Route route){
        return this.getSession().createQuery("SELECT count(*) FROM Purchase p WHERE p.route = :route", Long.class)
                                .setParameter("route", route)
                                .getSingleResult();
    }


    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        String hql = """
                FROM Purchase p
                WHERE size(p.itemServiceList) > 0
                ORDER BY p.totalPrice DESC
                """;
        return this.getSession().createQuery(hql, Purchase.class)
                        .setMaxResults(10)
                        .getResultList();                        
    }

    @Override
    public Long getCountOfPurchasesBetweenDates(Date start, Date end){
        return this.getSession().createQuery("SELECT count(p) FROM Purchase p WHERE p.date >= :start AND p.date <= :end", Long.class)
                                .setParameter("start", start)
                                .setParameter("end", end)
                                .getSingleResult();
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        String hql = """
                SELECT DISTINCT tg
                FROM Review rev
                JOIN rev.purchase p
                JOIN p.route r              
                JOIN r.tourGuideList tg
                WHERE rev.rating = 1
                """;
        return this.getSession()
                    .createQuery(hql, TourGuideUser.class)
                    .getResultList();
    }

    public Optional<TourGuideUser> getTourGuideByUsername(String username){
        return this.getSession().createQuery("FROM TourGuideUser u WHERE u.username = :username", TourGuideUser.class)
                                .setParameter("username", username)
                                .uniqueResultOptional();
    }

    public Optional<DriverUser> getDriverUserByUsername(String username){
        return this.getSession().createQuery("FROM DriverUser u WHERE u.username = :username", DriverUser.class)
                                .setParameter("username", username)
                                .uniqueResultOptional();
    }
}
