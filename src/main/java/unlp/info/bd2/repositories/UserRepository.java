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
    
    Optional<User> getUserById(Long id);
    
    Optional<User> getUserByUsername(String username);
    
    @Query("")
    List<Purchase> getAllPurchasesOfUsername(String username);

    @Query("")
    List<User> getTop5UsersMorePurchases();

    @Query("")
    List<User> getUserSpendingMoreThan(float mount);
}