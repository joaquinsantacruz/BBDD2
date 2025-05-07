package unlp.info.bd2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
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
import unlp.info.bd2.utils.ToursException;

import unlp.info.bd2.repositories.DriverUserRepository;
import unlp.info.bd2.repositories.PurchaseRepository;
import unlp.info.bd2.repositories.RouteRepository;
import unlp.info.bd2.repositories.ServiceRepository;
import unlp.info.bd2.repositories.StopRepository;
import unlp.info.bd2.repositories.SupplierRepository;
import unlp.info.bd2.repositories.TourGuideUserRepository;
import unlp.info.bd2.repositories.UserRepository;


public class ToursServiceImpl implements ToursService{

    private DriverUserRepository dur;
    private PurchaseRepository pr;
    private RouteRepository rr;
    private ServiceRepository ser;
    private SupplierRepository sur;
    private TourGuideUserRepository tgr;
    private UserRepository ur;
    private StopRepository str;


    public ToursServiceImpl( DriverUserRepository dur, PurchaseRepository pr, RouteRepository rr, ServiceRepository ser, SupplierRepository sur, TourGuideUserRepository tgr, UserRepository ur ){
        this.dur = dur;
        this.pr = pr;
        this.rr = rr;
        this.ser = ser;
        this.sur = sur;
        this.tgr=tgr;
        this.ur=ur;
    }

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService item = new ItemService(quantity, purchase, service);
        pr.save(purchase);
        return item;
    }

    @Override
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        Review review = new Review(rating, comment, purchase);
        this.pr.save(purchase);
        return review;
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier)throws ToursException {
        Service service = new Service(name, price, description, supplier);
        supplier.addSevice(service);
        sur.save(supplier);
        return service;
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        Optional<DriverUser> opDriverUser = this.dur.getDriverUserByUsername(username);
        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        
        
        if(opDriverUser.isPresent() && optionalRoute.isPresent()){
            Route route = optionalRoute.get();
            DriverUser driver = opDriverUser.get();
            route.addDriver(driver);
            this.rr.save(route);
        }
        else{
            throw new ToursException("No pudo realizarse la asignación");
        }
    }

    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        Optional<TourGuideUser> opTourGuide = this.tgr.getTourGuideByUsername(username);
        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        
        
        if(opTourGuide.isPresent() && optionalRoute.isPresent()){
            Route route = optionalRoute.get();
            TourGuideUser tourGuide = opTourGuide.get();
            route.addTourGuide(tourGuide);
            this.rr.save(route);
        }
        else{
            throw new ToursException("No pudo realizarse la asignación");
        }

    }

    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException {
        DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
        dur.save(driverUser);
        return driverUser;
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        if(this.rr.purchasesOnRoute(route) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }
        
        Purchase purchase = new Purchase(code, user, route);
        user.addPurchase(purchase);
        pr.save(purchase);
        return purchase;
        
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        if(this.rr.purchasesOnRoute(route) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }

        Purchase purchase = new Purchase(code, user, route, date);
        user.addPurchase(purchase);
        pr.save(purchase);
        return purchase;
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException {
        Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
        rr.save(route);
        return route;
    }

    @Override
    @Transactional
    public Stop createStop(String name, String description) throws ToursException {
        Stop stop = new Stop(name, description);
        this.str.save(stop);
        return stop;
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        Supplier supplier = new Supplier(businessName, authorizationNumber);
        sur.save(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber, ArrayList<Service> services) throws ToursException {
        Supplier supplier = new Supplier(businessName, authorizationNumber, services);
        sur.save(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException {
        TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
        tgr.save(tourGuideUser);
        return tourGuideUser;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        User user = new User(username, password, fullName, email, birthdate, phoneNumber);
        ur.save(user);
        return user;
    }

    @Override
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchase.getUser().removePurchase(purchase);
        purchase.getItemServiceList().stream().forEach(item -> item.getService().removeItem(item));
        this.pr.delete(purchase);
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException {
        if (user.isActive()) {
            if (user.canBeDeactivated()) {
                if (!user.canBeRemoved()){
                    user.setActive(false);
                    this.ur.save(user);
                } else {
                    this.ur.delete(user);
                }
            } else {
                throw new ToursException("El usuario no puede ser desactivado");
            }
        } else {
            throw new ToursException("El usuario se encuentra desactivado");            
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) throws ToursException {
        this.ur.save(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return this.ur.getAllPurchasesOfUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return this.pr.getCountOfPurchasesBetweenDates(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes() {
        return this.rr.getMaxStopOfRoutes();
    }

    @Override
    @Transactional(readOnly = true)
    public Service getMostDemandedService() {
        return this.ser.getMostDemandedService(PageRequest.of(0, 1));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return this.pr.getPurchaseByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return this.rr.getRouteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return this.rr.getRoutesBelowPrice(price);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop) {
        return this.rr.getRoutesWithStop(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell() {
        return this.rr.getRoutesNotSell();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return this.ser.findByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> getServiceNoAddedToPurchases() {
        return this.ser.getServiceNoAddedToPurchases();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return this.str.getStopByNameStart(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return this.sur.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return this.sur.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        return this.pr.getTop10MoreExpensivePurchasesInServices();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxRating() {
        return this.rr.getTop3RoutesWithMaxRating();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTop5UsersMorePurchases() {
        return this.ur.getTop5UsersMorePurchases();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return this.sur.getTopNSuppliersInPurchases(PageRequest.of(0, n));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return this.tgr.getTourGuidesWithRating1();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException {
        return this.ur.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return this.ur.getUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount) {
        return this.ur.getUserSpendingMoreThan(mount);
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        
        Optional<Service> opService = this.ser.findById(id);

        if(opService.isPresent()){
            Service service = opService.get();
            service.setPrice(newPrice);
            this.ser.save(service);
            return service;
        }
        else{
            throw new ToursException("El producto no existe");
        }
        
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersItemsSold(int n) {
        return this.sur.getTopNSuppliersItemsSold(PageRequest.of(0, n));
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxServicesOfSupplier() {
        return sur.getMaxServicesOfSupplier();
    }
}
