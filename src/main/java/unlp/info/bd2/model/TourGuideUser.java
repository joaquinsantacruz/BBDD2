package unlp.info.bd2.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("TOUR_GUIDE")
public class TourGuideUser extends User {

    @Column
    private String education;

    @ManyToMany
    @JoinTable(
        name = "tourguide_routes",
        joinColumns = @JoinColumn(name = "tour_guide_id"),
        inverseJoinColumns = @JoinColumn(name = "route_id")
    )
    private List<Route> routes;


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

}
