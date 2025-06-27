package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
import unlp.info.bd2.repositories.DriverUserRepository;
import unlp.info.bd2.repositories.ItemServiceRepository;
import unlp.info.bd2.repositories.PurchaseRepository;
import unlp.info.bd2.repositories.ReviewRepository;
import unlp.info.bd2.repositories.RouteRepository;
import unlp.info.bd2.repositories.ServiceRepository;
import unlp.info.bd2.repositories.StopRepository;
import unlp.info.bd2.repositories.SupplierRepository;
import unlp.info.bd2.repositories.TourGuideUserRepository;
import unlp.info.bd2.repositories.UserRepository;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService {

    @Autowired
    private ItemServiceRepository itemServiceRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

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

    @Autowired
    private DriverUserRepository driverUserRepository;


    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        try{
            ItemService item = new ItemService(quantity, purchase, service);
            purchase.addItem(item, quantity * service.getPrice());
            service.addItem(item);
            this.itemServiceRepository.save(item);
            this.purchaseRepository.save(purchase);
            this.serviceRepository.save(service);
            return item;
        }
        catch(Exception e){
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        try{
            Review review = new Review(rating, comment, purchase);
            this.purchaseRepository.save(purchase);
            return review;
        }
        catch(Exception e){
            throw new ToursException("Constraint Violation");
        }

    }


    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {

        if(this.purchaseRepository.findByCode(code).isPresent()){
            throw new ToursException("Constraint Violation");
        }

        return this.createPurchase(code, new Date(), route, user);
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {

        if(this.purchaseRepository.findByCode(code).isPresent()){
            throw new ToursException("Constraint Violation");
        }

        if(this.purchaseRepository.countByRouteIdAndDate(route.getId(), date) >= route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }

        Purchase purchase = new Purchase(code, user, route, date);
        this.purchaseRepository.save(purchase);
        user.addPurchase(purchase);
        this.userRepository.save(user);
        return purchase;
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        try{
            Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
            return this.routeRepository.save(route);
        }
        catch(Exception e){
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    @Transactional
    public Stop createStop(String name, String description) throws ToursException {
        try{
            Stop stop = new Stop(name, description);
            return this.stopRepository.save(stop);
        }
        catch(Exception e){
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException {
        Optional<Purchase> optionalPurchase = this.purchaseRepository.findByCode(purchase.getCode());
        if (optionalPurchase.isEmpty()) {
            throw new ToursException("La compra con codigo " + purchase.getCode() + " no existe.");
        }

        Purchase realPurchase = optionalPurchase.get();
        try{
            this.itemServiceRepository.deleteAll(realPurchase.getItemServiceList());
            this.purchaseRepository.delete(realPurchase);
        }
        catch(Exception e){
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxAverageRating() {
        return this.routeRepository.getTop3RoutesWithMaxAverageRating(PageRequest.ofSize(3));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMoreStops() {
        return this.routeRepository.getTop3RoutesWithMoreStops(PageRequest.ofSize(3));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return this.stopRepository.findByNameStartingWith(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes() {
        return this.routeRepository.getMaxStopOfRoutes();
    }

    @Override
    @Transactional(readOnly = true)
    public Route getMostBestSellingRoute() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Route> routes = this.routeRepository.getMostBestSellingRoute(pageable);
        return routes.isEmpty() ? null : routes.get(0); 
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return this.purchaseRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(ObjectId id) {
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
        return this.routeRepository.findByStopsContaining(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow();
        return this.purchaseRepository.findByUser_Id(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return this.purchaseRepository.countByDateBetween(start, end);
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber) throws ToursException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ToursException("Constraint Violation");
        }
        try {
            User user = new User(username, password, fullName, email, birthdate, phoneNumber);
            return userRepository.save(user);   
        } catch (Exception e) {
            throw new ToursException("Error creando el usuario: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ToursException("Constraint Violation");
        }

        try {
            DriverUser user = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
            return driverUserRepository.save(user);
        } catch (Exception e) {
            throw new ToursException("Error creando el usuario: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ToursException("Constraint Violation");
        }

        try {
            TourGuideUser user = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
            return tourGuideUserRepository.save(user);
        } catch (Exception e) {
            throw new ToursException("Error creando el usuario: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(ObjectId id) throws ToursException {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User updateUser(User user) throws ToursException {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ToursException("Error actualizando el usuario: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException {
        Optional<User> optionalUser = this.userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()) {
            throw new ToursException("El usuario con nombre: " + user.getUsername() + " no existe.");
        }

        User realUser = optionalUser.get();

        if (!realUser.isActive()) {
            throw new ToursException("El usuario ya se encuentra desactivado");
        }

        if (!realUser.canBeDeactivated()) {
            throw new ToursException("El usuario no puede ser desactivado");
        }

        try {
            if (realUser.canBeRemoved()) {
                this.userRepository.delete(realUser);
            } else {
                realUser.setActive(false);
                this.userRepository.save(realUser);
            }
        } catch (Exception e) {
            throw new ToursException("Error eliminando el usuario: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, ObjectId idRoute) throws ToursException {
        Optional<DriverUser> optionalDriverUser = this.driverUserRepository.findByUsername(username);
        if (optionalDriverUser.isEmpty()) {
            throw new ToursException("El Driver user con nombre: " + username + " no existe.");
        }

        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        if (optionalRoute.isEmpty()) {
            throw new ToursException("La ruta con id: " + idRoute + " no existe.");
        }


        Route route = optionalRoute.get();
        DriverUser driver = optionalDriverUser.get();
        try{
            route.addDriver(driver);
            this.routeRepository.save(route);
            this.driverUserRepository.save(driver);
        }
        catch(Exception e){
            throw new ToursException("Error asignando el Driver: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, ObjectId idRoute) throws ToursException {
        Optional<TourGuideUser> optionalTourGuideUser = this.tourGuideUserRepository.findByUsername(username);
        if (optionalTourGuideUser.isEmpty()) {
            throw new ToursException("El Tour Guide user con nombre: " + username + " no existe.");
        }

        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        if (optionalRoute.isEmpty()) {
            throw new ToursException("La ruta con id: " + idRoute + " no existe.");
        }

        Route route = optionalRoute.get();
        TourGuideUser tourGuide = optionalTourGuideUser.get();
        try{
            route.addTourGuide(tourGuide);
            this.routeRepository.save(route);
            this.tourGuideUserRepository.save(tourGuide);
        }
        catch(Exception e){
            throw new ToursException("Error asignando el Driver: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        if (this.supplierRepository.findByAuthorizationNumber(authorizationNumber).isPresent()) {
            throw new ToursException("Constraint Violation: clave duplicada");
        }
        try {
            Supplier supplier = new Supplier(businessName, authorizationNumber);
            return this.supplierRepository.save(supplier);
        } catch (Exception e) {
            throw new ToursException("Se produjo otro error");
        }
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException {
        Service service = new Service(name, price, description, supplier);
        this.serviceRepository.save(service);
        supplier.addService(service);
        this.supplierRepository.save(supplier);
        return service;
    }


    @Override
    @Transactional
    public Service updateServicePriceById(ObjectId id, float newPrice) throws ToursException {
        Optional<Service> optionalService = this.serviceRepository.findById(id);
        if (optionalService.isEmpty()) {
            throw new ToursException("El servicio con ID " + id + " no existe.");
        }

        Service service = optionalService.get();
        service.setPrice(newPrice);
        return this.serviceRepository.save(service);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(ObjectId id) {
        return this.supplierRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return this.supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, ObjectId id) throws ToursException {
        return serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float amount) {
        return this.userRepository.getUserSpendingMoreThan(amount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithNumberOfPurchases(int number) {
        return this.userRepository.getUsersWithNumberOfPurchases(number);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        Pageable pageable = PageRequest.of(0, n);
        return supplierRepository.getTopNSuppliersInPurchases(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersItemsSold(int n) {
        Pageable pageable = PageRequest.of(0, n);
        return supplierRepository.getTopNSuppliersItemsSold(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTop5UsersMorePurchases() {
        return this.userRepository.getTop5UsersMorePurchases(PageRequest.of(0, 5));
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxServicesOfSupplier() {
        return supplierRepository.getMaxServicesOfSupplier();
    }
}
