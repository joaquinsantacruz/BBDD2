package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService{

    private ToursRepository repository;

    public ToursServiceImpl(ToursRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService item = new ItemService(quantity, purchase, service);
        service.addItem(item);
        purchase.addItem(item, service.getPrice()*quantity);
        repository.saveItem(item);
        return item;
    }

    @Override
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        Review review = new Review(rating, comment, purchase);
        purchase.setReview(review);
        this.repository.saveReview(review);
        return review;
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier)throws ToursException {
        Service service = new Service(name, price, description, supplier);
        supplier.addSevice(service);
        repository.saveService(service);
        return service;
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        Optional<User> optionalUser = this.getUserByUsername(username);
        if(!optionalUser.isPresent()){
            throw new ToursException("No pudo realizarse la asignación");
        }

        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        if(!optionalRoute.isPresent()){
            throw new ToursException("No pudo realizarse la asignación");
        }

        DriverUser driverUser = (DriverUser) optionalUser.get();
        Route route = optionalRoute.get();

        route.addDriver(driverUser);
        driverUser.addRoute(route);
        
        this.repository.updateRoute(route);
    }

    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        Optional<User> optionalUser = this.getUserByUsername(username);
        if(!optionalUser.isPresent()){
            throw new ToursException("No pudo realizarse la asignación");
        }

        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        if(!optionalRoute.isPresent()){
            throw new ToursException("No pudo realizarse la asignación");
        }

        TourGuideUser tourGuideUser = (TourGuideUser) optionalUser.get();
        Route route = optionalRoute.get();

        route.addTourGuide(tourGuideUser);
        tourGuideUser.addRoute(route);
        
        this.repository.updateRoute(route);
    }

    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException {
        DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
        repository.saveDriverUser(driverUser);
        return driverUser;
    }

    @Override
    @Transactional
    public User updateUser(User user) throws ToursException {
        return this.repository.updateUser(user);
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        if(this.repository.purchasesOnRoute(route) > route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }

        if(this.repository.getPurchaseByCode(code).isPresent()){
            throw new ToursException("Constraint Violation");
        }

        Purchase purchase = new Purchase(code, user, route);
        user.addPurchase(purchase);
        repository.savePurchase(purchase);
        return purchase;
        
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        if(this.repository.purchasesOnRoute(route) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }

        if(this.repository.getPurchaseByCode(code).isPresent()){
            throw new ToursException("Constraint Violation");
        }

        Purchase purchase = new Purchase(code, user, route, date);
        user.addPurchase(purchase);
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
    @Transactional
    public Stop createStop(String name, String description) throws ToursException {
        Stop stop = new Stop(name, description);
        this.repository.saveStop(stop);
        return stop;
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        if(this.repository.getSupplierByAuthorizationNumber(authorizationNumber).isPresent()){
            throw new ToursException("Constraint Violation");
        }

        Supplier supplier = new Supplier(businessName, authorizationNumber);
        repository.saveSupplier(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException {
        TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
        repository.saveTourGuideUser(tourGuideUser);
        return tourGuideUser;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        if(this.repository.getUserByUsername(username).isPresent()){
            throw new ToursException("Constraint Violation");
        }

        User user = new User(username, password, fullName, email, birthdate, phoneNumber);
        repository.saveUser(user);
        return user;
    }

    @Override
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchase.getUser().removePurchase(purchase);
        purchase.getItemServiceList().stream().forEach(item -> item.getService().removeItem(item));
        this.repository.removePurchase(purchase);
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException {
        
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return this.repository.getAllPurchasesOfUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return this.repository.getCountOfPurchasesBetweenDates(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes() {
        return this.repository.getMaxStopOfRoutes();
    }

    @Override
    @Transactional(readOnly = true)
    public Service getMostDemandedService() {
        return this.repository.getMostDemandedService();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return this.repository.getPurchaseByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return this.repository.getRouteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return this.repository.getRoutesBelowPrice(price);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop) {
        return this.repository.getRoutesWithStop(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell() {
        return this.repository.getRoutesNotSell();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return this.repository.getServiceByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> getServiceNoAddedToPurchases() {
        return this.repository.getServiceNoAddedToPurchases();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return this.repository.getStopByNameStart(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return this.repository.getSupplierByAuthorizationNumber(authorizationNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return this.repository.getSupplierById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        return this.repository.getTop10MoreExpensivePurchasesInServices();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxRating() {
        return this.repository.getTop3RoutesWithMaxRating();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTop5UsersMorePurchases() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return this.repository.getTopNSuppliersInPurchases(n);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException {
        return this.repository.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return this.repository.getUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount) {
        return this.repository.getUserSpendingMoreThan(mount);
    }

    @Override
    @Transactional(readOnly = true)
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        if(!this.repository.getServiceById(id).isPresent()){
            throw new ToursException("Constraint Violation");
        }

        return this.repository.updateServicePriceById(id, newPrice);
        
    }


}
