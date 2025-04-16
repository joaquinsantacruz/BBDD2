package unlp.info.bd2.repositories;

import java.util.Date;
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
        this.getSession().persist(item);
    }

    @Override
    public void savePurchase(Purchase purchase) {
        this.getSession().persist(purchase);
    }

    @Override
    public void saveReview(Review review){
        this.getSession().persist(review);
    }

    @Override
    public void saveRoute(Route route) {
        this.getSession().persist(route);
    }

    @Override
    public void saveDriverUser(DriverUser driverUser) {
        this.getSession().persist(driverUser);
    }

    @Override
    public void saveTourGuideUser(TourGuideUser tourGuideUser){
        this.getSession().persist(tourGuideUser);
    }

    @Override
    public void saveSupplier(Supplier supplier){
        this.getSession().persist(supplier);
    }

    @Override
    public void saveUser(User user){
        this.getSession().persist(user);
    }

    public Optional<User> getUserById(Long id){
        return Optional.ofNullable(this.getSession().get(User.class, id));
    }

    public Optional<User> getUserByUsername(String username) {
        return this.getSession().createQuery("FROM User WHERE username = :username", User.class)
                                .setParameter("username", username)
                                .uniqueResultOptional();
    }

    public User updateUser(User user) {
        return this.getSession().merge(user);
    }

    @Override
    public void updateRoute(Route route) {
        this.getSession().merge(route);
    }

    @Override
    public void removePurchase(Purchase purchase){
        this.getSession().remove(purchase);
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
        return this.getSession().createQuery("FROM Route r JOIN r.stops s WHERE s = :stop", Route.class)
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
    public void saveService(Service service){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(service);
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id){
        Supplier supplier = this.getSession().get(Supplier.class, id);
        return Optional.ofNullable(supplier);
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
        return this.getSession()
        .createQuery("""
            SELECT is.service.supplier 
            FROM ItemService is 
            GROUP BY is.service.supplier 
            ORDER BY SUM(is.quantity) DESC
            """, Supplier.class)
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
        String hql = "FROM Service s WHERE s NOT IN (SELECT is.service FROM ItemService is)";
        return this.getSession()
                    .createQuery(hql, Service.class)
                    .getResultList();
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice){
        Service service = this.getSession().get(Service.class, id);
        service.setPrice(newPrice);
        return service;
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
        return this.getUserByUsername(username).get().getPurchaseList();
    }
    
    // Gastado en todas las compras de un usuario = 7 users
    // @Override
    // public List<User> getUserSpendingMoreThan(float amount) {
    //     String hql = """
    //             SELECT p.user 
    //             FROM Purchase p 
    //             GROUP BY p.user 
    //             HAVING SUM(p.totalPrice) > :amount
    //             """;
    //     return this.getSession()
    //                 .createQuery(hql, User.class)
    //                 .setParameter("amount", amount)
    //                 .getResultList();
    // }

    // Gastado en una sola compra con mayor estricto = 5 users
    // @Override
    // public List<User> getUserSpendingMoreThan(float amount) {
    //     String hql = """
    //             SELECT p.user 
    //             FROM Purchase p 
    //             WHERE p.totalPrice > :amount
    //             """;
    //     return this.getSession()
    //                 .createQuery(hql, User.class)
    //                 .setParameter("amount", amount)
    //                 .getResultList();
    // }

    // Gastado en una sola compra con mayor o igual = 6 users
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

    // @Override
    // public void deleteUser(User user) {
    //     // Si el usuario tiene compras, no se toca
    //     // Si el usuario no tiene compras y active = true, se pone active = false
    //     // Si el usuario no tiene compras y active = false, se elimina fisicamente y en cascada se eliminan sus compras e items
    //     if (user.getPurchaseList().isEmpty()) {
    //         if (user.isActive()) {
    //             user.setActive(false);
    //             this.updateUser(user);
    //         } else {
    //             this.getSession().remove(user);
    //         }
    //     } else {
    //         System.out.println("El usuario tiene compras, no se puede eliminar.");
    //     }
    // }

    @Override
    public void deleteUser(User user) {
        // Si el usuario tiene compras, active = false
        // Si el usuario no tiene compras, se elimina fisicamente y en cascada se eliminan sus compras e items
        if (user.getPurchaseList().isEmpty()) {
            this.getSession().remove(user);
        } else {
            user.setActive(false);
            this.updateUser(user);
        }
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
                JOIN p.itemServiceList is
                JOIN is.service s
                GROUP BY p
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

    public boolean isTourGuideOnARoute(User user) {
        String hql = """
                SELECT COUNT(r) > 0 
                FROM Route r 
                WHERE :user MEMBER OF r.tourGuideList
                """;
        return this.getSession()
                    .createQuery(hql, Boolean.class)
                    .setParameter("user", user)
                    .getSingleResult();
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        String hql = """
                SELECT DISTINCT p.tourGuide 
                FROM Purchase p 
                WHERE p.review.rating = 1
                """;
        return this.getSession()
                    .createQuery(hql, TourGuideUser.class)
                    .getResultList();
    }
}
