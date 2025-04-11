package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("DRIVER")
public class DriverUser extends User {

    @Column(nullable = true)
    private String expedient;

    @ManyToMany
    @JoinTable(
        name = "drivers_routes", 
        joinColumns = @JoinColumn(name = "driver_user_id"),
        inverseJoinColumns = @JoinColumn(name = "route_id")
    )
    private List<Route> routes; 

    public DriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) {
        super(username, password, fullName, email, birthdate, phoneNumber);
        this.expedient = expedient;
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

    public void setRouts(List<Route> routs) { //TODO: ESTA MAL ESCRITO EL NOMBRE??
        this.routes = routs;
    }
}
