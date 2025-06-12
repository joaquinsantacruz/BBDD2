package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class TourGuideUser extends User {

    private String education;

    @DBRef
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

    public void removeRoute(Route route){
        this.routes.remove(route);
    }

    public boolean canBeDeactivated(){
        return this.routes.isEmpty();
    }

    public boolean canBeRemoved(){
        return this.routes.isEmpty();
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

}
