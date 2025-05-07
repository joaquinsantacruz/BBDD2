package unlp.info.bd2.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE u.purchases.size > 0 ORDER BY u.purchases.size DESC")
    List<User> findTop5UsersMorePurchases(Pageable pageable);

    List<User> findDistinctByPurchase_TotalPriceGreaterThanEqual(float mount);

    @Query("SELECT u FROM User u WHERE u.purchases.size = ?1")
    List<User> getUsersWithNumberOfPurchases(int number);
}
