package unlp.info.bd2.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE SIZE(u.purchaseList) > 0 ORDER BY SIZE(u.purchaseList) DESC")
    List<User> findTopUsersMorePurchases(Pageable pageable);

    List<User> findDistinctByPurchaseList_TotalPriceGreaterThanEqual(float mount);

    @Query("SELECT u FROM User u WHERE SIZE(u.purchaseList) = ?1")
    List<User> getUsersWithNumberOfPurchases(int number);
}
