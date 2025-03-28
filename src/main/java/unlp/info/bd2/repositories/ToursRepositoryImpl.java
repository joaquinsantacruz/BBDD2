package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Session;

import unlp.info.bd2.model.*;

public class ToursRepositoryImpl implements ToursRepository{

    
    
        SessionFactory factory;
    
        public void saveItem(ItemService item){
            Transaction tx = null;
            try(Session session = factory.openSession()){
                tx = session.beginTransaction();
                session.persist(item);
                tx.commit();
            }
            catch (Exception e){
                if(tx != null){
                    tx.rollback();
                }
            }
        }
    
        public void savePurchase(Purchase purchase){
            Transaction tx = null;
            try(Session session = factory.openSession()){
                tx = session.beginTransaction();
                session.persist(purchase);
                tx.commit();
            }
            catch (Exception e){
                if(tx != null){
                    tx.rollback();
                }
            }
        }
    
        public void saveRoute(Route route){
            Transaction tx = null;
            try(Session session = factory.openSession()){
                tx = session.beginTransaction();
                session.persist(route);
                tx.commit();
            }
            catch (Exception e){
                if(tx != null){
                    tx.rollback();
                }
            }
        }
    
}
