package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    public void saveItem(ItemService item) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(item);
    }

    @Override
    public void savePurchase(Purchase purchase) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(purchase);
    }

    @Override
    public void saveRoute(Route route) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(route);
    }

    @Override
    public void saveDriverUser(DriverUser driverUser) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(driverUser);

    }

    @Override
    public void saveTourGuideUser(TourGuideUser tourGuideUser){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(tourGuideUser);
    }

    @Override
    public void saveSupplier(Supplier supplier){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(supplier);
    }

    @Override
    public void saveStop(Stop stop){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(stop);
    }

    @Override
    public void saveUser(User user){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
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

}
