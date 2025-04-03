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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void savePurchase(Purchase purchase) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveRoute(Route route) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(route);

    }

}
