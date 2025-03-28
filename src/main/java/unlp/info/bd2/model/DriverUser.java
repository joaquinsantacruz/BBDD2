package unlp.info.bd2.model;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class DriverUser extends User {

    @Column(nullable = false)
    private String expedient;

    @ManyToMany
    @JoinTable(
        name = "drivers_routes", 
        joinColumns = @JoinColumn(name = "driver_user_id"),
        inverseJoinColumns = @JoinColumn(name = "route_id")
    )
    private List<Route> routes; 

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRouts(List<Route> routs) { //TODO: ESTA MAL ESCRITO EL NOMBRE??
        this.routes = routs;
    }
}
