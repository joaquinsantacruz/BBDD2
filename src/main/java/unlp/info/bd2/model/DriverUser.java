package unlp.info.bd2.model;


import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("DRIVER")
public class DriverUser extends User {

    @Column    
    private String expedient;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {})
    @JoinTable(
        name = "driver_route", 
        joinColumns = @JoinColumn(name = "driver_id"),
        inverseJoinColumns = @JoinColumn(name = "route_id")
    )
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

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRouts(List<Route> routs) {
        this.routes = routs;
    }

    public boolean canBeDeactivated(){
        return this.routes.isEmpty();
    }

    public boolean canBeRemoved(){
        return this.routes.isEmpty();
    }

}
