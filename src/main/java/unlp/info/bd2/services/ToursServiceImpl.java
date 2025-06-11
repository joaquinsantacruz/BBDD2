package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.ServiceRepository;
import unlp.info.bd2.repositories.SupplierRepository;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService {

    private SupplierRepository supplierRepository;
    private ServiceRepository serviceRepository;

    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createDriverUser'");
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createTourGuideUser'");
    }

    @Override
    public Optional<User> getUserById(ObjectId id) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserByUsername'");
    }

    @Override
    public User updateUser(User user) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createStop'");
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStopByNameStart'");
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createRoute'");
    }

    @Override
    public Optional<Route> getRouteById(ObjectId id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRouteById'");
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoutesBelowPrice'");
    }

    @Override
    public void assignDriverByUsername(String username, ObjectId idRoute) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignDriverByUsername'");
    }

    @Override
    public void assignTourGuideByUsername(String username, ObjectId idRoute) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignTourGuideByUsername'");
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        try {
            Supplier supplier = new Supplier(businessName, authorizationNumber);
            return this.supplierRepository.save(supplier);
        }
        catch (DuplicateKeyException dke) {
            // Mongo usa DuplicateKeyException para violación de índices únicos
            throw new ToursException("Constraint Violation: clave duplicada");
        }
        catch (Exception e) {
            throw new ToursException("Se produjo otro error");
        }
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException {
        try {
            Service service = new Service(name, price, description, supplier);
            return this.serviceRepository.save(service);
        }
        catch (DuplicateKeyException dke) {
            throw new ToursException("Clave duplicada: ya existe un servicio con estos datos");
        }
        catch (Exception e) {
            throw new ToursException("Se produjo un error al guardar el servicio");
        }
    }


    @Override
    public Service updateServicePriceById(ObjectId id, float newPrice) throws ToursException {
        try {
            Service service = this.serviceRepository.findById(id).get(); 
            service.setPrice(newPrice);
            return this.serviceRepository.save(service);
        }
        catch (DuplicateKeyException dke) {
            throw new ToursException("Clave duplicada al actualizar el servicio");
        }
        catch (Exception e) {
            throw new ToursException("Error al actualizar el servicio");
        }
    }


    @Override
    public Optional<Supplier> getSupplierById(ObjectId id) {
        return this.supplierRepository.findById(id);
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return this.supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, ObjectId id) throws ToursException {
        return serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPurchase'");
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPurchase'");
    }

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItemToPurchase'");
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPurchaseByCode'");
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePurchase'");
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addReviewToPurchase'");
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPurchasesOfUsername'");
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserSpendingMoreThan'");
    }

    @Override
    public List<User> getUsersWithNumberOfPurchases(int number) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsersWithNumberOfPurchases'");
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        Pageable pageable = PageRequest.of(0, n);
        return supplierRepository.getTopNSuppliersInPurchases(pageable);
    }

    @Override
    public List<Supplier> getTopNSuppliersItemsSold(int n) {
        Pageable pageable = PageRequest.of(0, n);
        return supplierRepository.getTopNSuppliersItemsSold(pageable);
    }

    @Override
    public List<User> getTop5UsersMorePurchases() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTop5UsersMorePurchases'");
    }

    @Override
    public List<Route> getTop3RoutesWithMoreStops() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTop3RoutesWithMoreStops'");
    }

    @Override
    public Long getCountOfPurchasesBetweenDates(Date start, Date end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCountOfPurchasesBetweenDates'");
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoutesWithStop'");
    }

    @Override
    public Long getMaxStopOfRoutes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxStopOfRoutes'");
    }

    @Override
    public Long getMaxServicesOfSupplier() {
        return supplierRepository.getMaxServicesOfSupplier()
        .map(doc -> doc.getLong("serviceCount"))
        .orElse(0L);
    }

    @Override
    public List<Route> getTop3RoutesWithMaxAverageRating() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTop3RoutesWithMaxAverageRating'");
    }

    @Override
    public Route getMostBestSellingRoute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMostBestSellingRoute'");
    }



    
}
