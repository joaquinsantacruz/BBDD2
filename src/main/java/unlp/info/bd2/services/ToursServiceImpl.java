package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PersistenceException;
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
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService{

    private ToursRepository repository;

    public ToursServiceImpl(ToursRepository repository){
        this.repository = repository;
    }

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService item = new ItemService(quantity, purchase, service);
        purchase.addItem(item, 0.5f);
        repository.saveItem(item);
        return item;
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier)throws ToursException {
        Service service = new Service(name, price, description, supplier);
        supplier.addSevice(service);
        repository.saveService(service);
        return service;
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException {
        DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
        repository.saveDriverUser(driverUser);
        return driverUser;
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase(code, user, route);
        repository.savePurchase(purchase);
        return purchase;
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase(code, user, route, date);
        repository.savePurchase(purchase);
        return purchase;
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException {
        Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
        repository.saveRoute(route);
        return route;
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        Stop stop = new Stop(name, description);
        repository.saveStop(stop);
        return stop;
    }

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        try {
        Supplier supplier = new Supplier(businessName, authorizationNumber);
        repository.saveSupplier(supplier);
        return supplier;
        } catch (PersistenceException e) {
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException {
        TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
        repository.saveTourGuideUser(tourGuideUser);
        return tourGuideUser;
    }

    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        User user = new User(username, password, fullName, email, birthdate, phoneNumber);
        repository.saveUser(user);
        return user;
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Long getMaxStopOfRoutes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Service getMostDemandedService() {
        return this.repository.getMostDemandedService();
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Route> getRoutsNotSell() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return this.repository.getServiceByNameAndSupplierId(name, id);
    }

    @Override
    public List<Service> getServiceNoAddedToPurchases() {
        return this.repository.getServiceNoAddedToPurchases();
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return this.repository.getSupplierByAuthorizationNumber(authorizationNumber);
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return this.repository.getSupplierById(id);
    }

    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<User> getTop5UsersMorePurchases() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return this.repository.getTopNSuppliersInPurchases(n);
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        try {
            return this.repository.updateServicePriceById(id, newPrice);
        } catch (Exception e) {
            throw new ToursException("No existe el producto");
        }
    }

    @Override
    public User updateUser(User user) throws ToursException {
        // TODO Auto-generated method stub
        return null;
    }

}
