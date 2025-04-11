package unlp.info.bd2.repositories;

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
    public void saveRoute(Route route) {
        this.getSession().persist(route);
    }

    @Override
    public void saveDriverUser(DriverUser driverUser) {
        this.getSession().persist(driverUser);
    }

    public void saveTourGuideUser(TourGuideUser tourGuideUser){
        this.getSession().persist(tourGuideUser);
    }

    public void saveSupplier(Supplier supplier){
        this.getSession().persist(supplier);
    }
    
    public void saveStop(Stop stop){
        this.getSession().persist(stop);
    }

    public void saveUser(User user){
        this.getSession().persist(user);
    }
    
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(this.getSession().get(User.class, id));
    }

    public Optional<User> getUserByUsername(String username) {
        return this.getSession().createQuery("FROM User WHERE username = :username", User.class)
                                .setParameter("username", username)
                                .uniqueResultOptional();
    }

}
