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

        if(this.purchaseRepository.existsByCode(code)){
            throw new ToursException("Constraint Violation");
        }

        if(this.purchaseRepository.countByRouteAndDate(route, new Date()) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }
        
        Purchase purchase = new Purchase(code, user, route);
        return this.purchaseRepository.save(purchase);
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
         if(this.purchaseRepository.existsByCode(code)){
            throw new ToursException("Constraint Violation");
        }

        if(this.purchaseRepository.countByRouteAndDate(route, date) == route.getMaxNumberUsers()){
            throw new ToursException("No puede realizarse la compra");
        }
        
        Purchase purchase = new Purchase(code, user, route);
        return this.purchaseRepository.save(purchase);
    }

    @Override
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
    public void deletePurchase(Purchase purchase) throws ToursException {
        try{
            this.purchaseRepository.delete(purchase);
        }
        catch(Exception e){
            throw new ToursException("Constraint Violation");
        }
    }

    @Override
    public List<Route> getTop3RoutesWithMaxAverageRating() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Route> getTop3RoutesWithMoreStops() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        return this.stopRepository.findByNameStartingWith(name);
    }

    @Override
    public Long getMaxStopOfRoutes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Route getMostBestSellingRoute() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Optional<Route> getRouteById(ObjectId id) {
        return this.routeRepository.findById(id);
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        return this.routeRepository.
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        // TODO Auto-generated method stub
        return null;
    }

     @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getCountOfPurchasesBetweenDates(Date start, Date end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
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
    public Optional<User> getUserById(ObjectId id) throws ToursException {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(User user) throws ToursException {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ToursException("Error actualizando el usuario: " + e.getMessage());
        }
    }

    @Override
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
                catch(Exception e){
                    throw new ToursException("Error eliminando el usuario: " + e.getMessage());
                }
            } else {
                throw new ToursException("El usuario no puede ser desactivado");
            }
        } else {
            throw new ToursException("El usuario se encuentra desactivado");            
        }
    }

    @Override
    public void assignDriverByUsername(String username, ObjectId idRoute) throws ToursException {
        Optional<DriverUser> opDriverUser = this.driverUserRepository.findByUsername(username);
        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        
        try{
            Route route = optionalRoute.get();
            DriverUser driver = opDriverUser.get();
            route.addDriver(driver);
            this.routeRepository.save(route);
        }
        catch(Exception e){
            throw new ToursException("Error asignando el Driver: " + e.getMessage());
        }
    }

    @Override
    public void assignTourGuideByUsername(String username, ObjectId idRoute) throws ToursException {
        Optional<TourGuideUser> opTourGuide = this.tourGuideUserRepository.findByUsername(username);
        Optional<Route> optionalRoute = this.getRouteById(idRoute);
        
        try{
            Route route = optionalRoute.get();
            TourGuideUser tourGuide = opTourGuide.get();
            route.addTourGuide(tourGuide);
            this.routeRepository.save(route);
        }
        catch(Exception e){
            throw new ToursException("Error asignando el TourGuide: " + e.getMessage());
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
        try {
            Service service = new Service(name, price, description, supplier);
            return this.serviceRepository.save(service);
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
    public List<User> getUserSpendingMoreThan(float mount) {
        return this.userRepository.getUserSpendingMoreThan(mount);
    }

    @Override
    public List<User> getUsersWithNumberOfPurchases(int number) {
        return this.userRepository.getUsersWithNumberOfPurchases(number);
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
        return this.userRepository.getTop5UsersMorePurchases();
    }

    @Override
    public Long getMaxServicesOfSupplier() {
        return supplierRepository.getMaxServicesOfSupplier()
        .map(doc -> doc.getLong("serviceCount"))
        .orElse(0L);
    }
}
