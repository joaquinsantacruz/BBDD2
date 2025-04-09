package unlp.info.bd2.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import unlp.info.bd2.model.*;

public class ToursRepositoryImpl implements ToursRepository{

    @Autowired
    private SessionFactory sessionFactory;

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

    public void saveTourGuideUser(TourGuideUser tourGuideUser){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(tourGuideUser);
    }

    public void saveSupplier(Supplier supplier){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(supplier);
    }
    
    public void saveStop(Stop stop){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(stop);
    }

    public void saveUser(User user){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
    }
    
    

}
