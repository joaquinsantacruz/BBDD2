package unlp.info.bd2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
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
import unlp.info.bd2.repositories.*;
import unlp.info.bd2.utils.ToursException;


public class ToursServiceImpl implements ToursService{

    @Autowired
    private DriverUserRepository driverUserRepository;
    
    @Autowired
    private ItemServiceRepository itemServiceRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private TourGuideUserRepository tourGuideUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        try{
            ItemService item = new ItemService(quantity, purchase, service);
            purchase.addItem(item, quantity * service.getPrice());
            service.addItem(item);
            this.itemServiceRepository.save(item);
            return item;
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
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        try{
            Review review = new Review(rating, comment, purchase);
            this.reviewRepository.save(review);
            return review;
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
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier)throws ToursException {
        try{
            Service service = new Service(name, price, description, supplier);
            this.serviceRepository.save(service);
            return service;
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
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        Optional<DriverUser> opDriverUser = this.driverUserRepository.findByUsername(username);
        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        
        try{
            Route route = optionalRoute.get();
            DriverUser driver = opDriverUser.get();
            route.addDriver(driver);
            this.routeRepository.save(route);
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
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        Optional<TourGuideUser> opTourGuide = this.tourGuideUserRepository.findByUsername(username);
        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        
        try{
            Route route = optionalRoute.get();
            TourGuideUser tourGuide = opTourGuide.get();
            route.addTourGuide(tourGuide);
            this.routeRepository.save(route);
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
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException {
        try{
            DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
            return this.driverUserRepository.save(driverUser);
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
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        if(this.purchaseRepository.countByRouteAndDate(route, new Date()) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }
        
        try{
            Purchase purchase = new Purchase(code, user, route);
            return this.purchaseRepository.save(purchase);
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
        if(this.purchaseRepository.countByRouteAndDate(route, date) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }

        try{
            Purchase purchase = new Purchase(code, user, route, date);
            return this.purchaseRepository.save(purchase);
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
        try{
            Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
            return this.routeRepository.save(route);
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
    public Stop createStop(String name, String description) throws ToursException {
        try{
            Stop stop = new Stop(name, description);
            return this.stopRepository.save(stop);
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
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        try{
            Supplier supplier = new Supplier(businessName, authorizationNumber);
            return this.supplierRepository.save(supplier);
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
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException {
        try{
            TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
            return this.tourGuideUserRepository.save(tourGuideUser);
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
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        try{
            User user = new User(username, password, fullName, email, birthdate, phoneNumber);
            return this.userRepository.save(user);
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
    public void deletePurchase(Purchase purchase) throws ToursException {
        try{
            purchase.removeFromUser();
            this.purchaseRepository.delete(purchase);
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
    public void deleteUser(User user) throws ToursException {
        if (user.isActive()) {
            if (user.canBeDeactivated()) {
                try{
                    if (!user.canBeRemoved()){
                        user.setActive(false);
                        this.userRepository.save(user);
                    } else {
                        this.userRepository.delete(user);
                    }
                }
                catch(ConstraintViolationException cve){
                    throw new ToursException("Constraint Violation");
                }
                catch(Exception e){
                    throw new ToursException("Se produjo otro error");
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
        try{
            return this.userRepository.save(user);
        }
        catch(ConstraintViolationException cve){
            throw new ToursException("Constraint Violation");
        }
        catch(Exception e){
            throw new ToursException("Se produjo otro error");
        }
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        
        Optional<Service> opService = this.serviceRepository.findById(id);

        try{
            Service service = opService.get();
            service.setPrice(newPrice);
            this.serviceRepository.save(service);
            return service;
        }
        catch(ConstraintViolationException cve){
            throw new ToursException("Constraint Violation");
        }
        catch(Exception e){
            throw new ToursException("Se produjo otro error");
        }
        
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
        return this.serviceRepository.getMostDemandedService(PageRequest.of(0, 1)).get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return this.purchaseRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return this.routeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return this.routeRepository.findByPriceLessThan(price);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop) {
        return this.routeRepository.findByStopsContains(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell() {
        return this.routeRepository.getRoutesNotSell();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return this.serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> getServiceNoAddedToPurchases() {
        return this.serviceRepository.findAllByItemServiceListIsEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return this.stopRepository.getStopByNameStartingWith(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return this.supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return this.supplierRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getTop10MoreExpensivePurchasesWithServices() {
        return this.purchaseRepository.findTop10ByItemServiceListIsNotEmptyOrderByTotalPriceDesc(PageRequest.ofSize(10));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxAverageRating() {
        return this.routeRepository.getTop3RoutesWithMaxAverageRating(PageRequest.ofSize(3));
    }

    @Override
    public List<Route> getTop3RoutesWithMoreStops() {
        return this.routeRepository.getTop3RoutesWithMoreStops(PageRequest.ofSize(3));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTop5UsersMorePurchases() {
        return this.userRepository.findTopUsersMorePurchases(PageRequest.ofSize(5));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return this.supplierRepository.getTopNSuppliersInPurchases(PageRequest.of(0, n));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return this.tourGuideUserRepository.getTourGuidesWithRating1();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return this.userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount) {
        return this.userRepository.findDistinctByPurchaseList_TotalPriceGreaterThanEqual(mount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersItemsSold(int n) {
        return this.supplierRepository.getTopNSuppliersItemsSold(PageRequest.of(0, n));
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxServicesOfSupplier() {
        return this.supplierRepository.getMaxServicesOfSupplier();
    }
	@Override
	public List<User> getUsersWithNumberOfPurchases(int number) {
		return this.userRepository.getUsersWithNumberOfPurchases(number);
	}

	@Override
	public DriverUser getDriverUserWithMoreRoutes() {
        List<DriverUser> drivers = this.driverUserRepository.getTopDriverUserWithMoreRoutes(PageRequest.of(0, 1));
        return drivers.isEmpty() ? null : drivers.get(0);
	}

    @Override
    public Route getMostBestSellingRoute() {
        return this.routeRepository.getMostBestSellingRoute(PageRequest.ofSize(1)).get(0);
    }

    @Override
    public List<Route> getRoutesWithMinRating() {
        return this.routeRepository.getRoutesWithMinRating();
    }

   

   
}
