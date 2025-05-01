package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("TOUR_GUIDE")
public class TourGuideUser extends User {

    @Column
    private String education;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {})
    @JoinTable(
        name = "tourguide_route",
        joinColumns = @JoinColumn(name = "tour_guide_id"),
        inverseJoinColumns = @JoinColumn(name = "route_id")
    )
    private List<Route> routes;

    public TourGuideUser(){}

    public TourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) {
        super(username, password, fullName, email, birthdate, phoneNumber);
        this.education = education;
        this.routes = new ArrayList<Route>();
    }

    public void addRoute(Route route){
        this.routes.add(route);
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public boolean canBeDeactivated(){
        return this.routes.isEmpty();
    }

    public boolean canBeRemoved(){
        return this.routes.isEmpty();
    }
}
