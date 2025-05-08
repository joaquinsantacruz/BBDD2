package unlp.info.bd2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Pageable;
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
import unlp.info.bd2.repositories.*;
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

    private DriverUserRepository driverUserRepository;
    private ItemServiceRepository itemServiceRepository;
    private PurchaseRepository purchaseRepository;
    private ReviewRepository reviewRepository;
    private RouteRepository routeRepository;
    private ServiceRepository serviceRepository;
    private StopRepository stopRepository;
    private SupplierRepository supplierRepository;
    private TourGuideUserRepository tourGuideUserRepository;
    private UserRepository userRepository;

    

    public ToursServiceImpl(DriverUserRepository driverUserRepository, ItemServiceRepository itemServiceRepository,
            PurchaseRepository purchaseRepository, ReviewRepository reviewRepository, RouteRepository routeRepository,
            ServiceRepository serviceRepository, StopRepository stopRepository, SupplierRepository supplierRepository,
            TourGuideUserRepository tourGuideUserRepository, UserRepository userRepository) {

        this.driverUserRepository = driverUserRepository;
        this.itemServiceRepository = itemServiceRepository;
        this.purchaseRepository = purchaseRepository;
        this.reviewRepository = reviewRepository;
        this.routeRepository = routeRepository;
        this.serviceRepository = serviceRepository;
        this.stopRepository = stopRepository;
        this.supplierRepository = supplierRepository;
        this.tourGuideUserRepository = tourGuideUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService item = new ItemService(quantity, purchase, service);
        this.purchaseRepository.save(purchase);
        return item;
    }

    @Override
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        Review review = new Review(rating, comment, purchase);
        this.purchaseRepository.save(purchase);
        return review;
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier)throws ToursException {
        Service service = new Service(name, price, description, supplier);
        supplier.addSevice(service);
        this.supplierRepository.save(supplier);
        return service;
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        Optional<DriverUser> opDriverUser = this.dur.findByUsername(username);
        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        
        
        if(opDriverUser.isPresent() && optionalRoute.isPresent()){
            Route route = optionalRoute.get();
            DriverUser driver = opDriverUser.get();
            route.addDriver(driver);
            this.routeRepository.save(route);
        }
        else{
            throw new ToursException("No pudo realizarse la asignación");
        }
    }

    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        Optional<TourGuideUser> opTourGuide = this.tgr.findByUsername(username);
        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        
        
        if(opTourGuide.isPresent() && optionalRoute.isPresent()){
            Route route = optionalRoute.get();
            TourGuideUser tourGuide = opTourGuide.get();
            route.addTourGuide(tourGuide);
            this.routeRepository.save(route);
        }
        else{
            throw new ToursException("No pudo realizarse la asignación");
        }

    }

    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException {
        DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
        this.driverUserRepository.save(driverUser);
        return driverUser;
    }

    @Override
    @Transactional //TODO: PREGUNTAR
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        if(this.purchaseRepository.countByRoute(route) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }
        
        try{
            Purchase purchase = new Purchase(code, user, route);
            user.addPurchase(purchase);
            this.purchaseRepository.save(purchase);
            return purchase;
        }
        catch(ConstraintViolationException cve){
            throw new ToursException("Constraint Violation");
        }
        catch(Exception e){
            throw new ToursException("Se produjo otro error");
        }
        
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        if(this.purchaseRepository.countByRoute(route) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }

        try{
            Purchase purchase = new Purchase(code, user, route);
            user.addPurchase(purchase);
            this.purchaseRepository.save(purchase);
            return purchase;
        }
        catch(ConstraintViolationException cve){
            throw new ToursException("Constraint Violation");
        }
        catch(Exception e){
            throw new ToursException("Se produjo otro error");
        }
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException {
        Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
        this.routeRepository.save(route);
        return route;
    }

    @Override
    @Transactional
    public Stop createStop(String name, String description) throws ToursException {
        Stop stop = new Stop(name, description);
        this.stopRepository.save(stop);
        return stop;
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        Supplier supplier = new Supplier(businessName, authorizationNumber);
        this.supplierRepository.save(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber, ArrayList<Service> services) throws ToursException {
        Supplier supplier = new Supplier(businessName, authorizationNumber, services);
        this.supplierRepository.save(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException {
        TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
        this.tourGuideUserRepository.save(tourGuideUser);
        return tourGuideUser;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        User user = new User(username, password, fullName, email, birthdate, phoneNumber);
        this.userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchase.getUser().removePurchase(purchase);
        purchase.getItemServiceList().stream().forEach(item -> item.getService().removeItem(item));
        this.purchaseRepository.delete(purchase);
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException {
        if (user.isActive()) {
            if (user.canBeDeactivated()) {
                if (!user.canBeRemoved()){
                    user.setActive(false);
                    this.userRepository.save(user);
                } else {
                    this.userRepository.delete(user);
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
        this.userRepository.save(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return this.purchaseRepository.findByUser_Username(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return this.purchaseRepository.countByDateBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseWithService(Service service){
        return this.purchaseRepository.findByItemServiceList_Service(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes() {
        return this.routeRepository.getMaxStopOfRoutes();
    }

    @Override
    @Transactional(readOnly = true)
    public Service getMostDemandedService() {
        return this.serviceRepository.getMostDemandedService();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return this.purchaseRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return this.routeRepository.getRouteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return this.routeRepository.getRoutesBelowPrice(price);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop) {
        return this.routeRepository.getRoutesWithStop(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell() {
        return this.routeRepository.getRoutesNotSell();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return this.serviceRepository.getServiceByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> getServiceNoAddedToPurchases() {
        return this.serviceRepository.getServiceNoAddedToPurchases();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return this.stopRepository.getStopByNameStart(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return this.supplierRepository.getSupplierByAuthorizationNumber(authorizationNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return this.supplierRepository.getSupplierById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getTop10MoreExpensivePurchasesWithServices() {
        return this.purchaseRepository.findTop10ByOrderByTotalPriceDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxAverageRating() {
        return this.routeRepository.getTop3RoutesWithMaxRating();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTop5UsersMorePurchases() {
        return this.ur.findTopUsersMorePurchases(Pageable.ofSize(5));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return this.supplierRepository.getTopNSuppliersInPurchases(n);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return this.tourGuideUserRepository.getTourGuidesWithRating1();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException {
        return this.ur.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return this.ur.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount) {
        return this.ur.findDistinctByPurchase_TotalPriceGreaterThanEqual(mount);
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        
        Optional<Service> opService = this.serviceRepository.getServiceById(id);

        if(opService.isPresent()){
            Service service = opService.get();
            service.setPrice(newPrice);
            this.serviceRepository.save(service);
            return service;
        }
        else{
            throw new ToursException("El producto no existe");
        }
        
    }

	@Override
	public List<User> getUsersWithNumberOfPurchases(int number) {
		return this.ur.getUsersWithNumberOfPurchases(number);
	}

	@Override
	public DriverUser getDriverUserWithMoreRoutes() {
        return this.dur.getTopDriverUserWithMoreRoutes(Pageable.ofSize(1));
	}
}
