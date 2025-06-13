package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class DriverUser extends User {

    private String expedient;

    @DBRef
    private List<Route> routes;

    public DriverUser(){}

    public DriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) {
        super(username, password, fullName, email, birthdate, phoneNumber);
        this.expedient = expedient;
        this.routes = new ArrayList<Route>();
    }

    public void addRoute(Route route){
        this.routes.add(route);
    }


    public void removeRoute(Route route){
        this.routes.remove(route);
    }

    public boolean canBeDeactivated(){
        return this.routes.isEmpty();
    }

    public boolean canBeRemoved(){
        return this.routes.isEmpty();
    }
  
    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
