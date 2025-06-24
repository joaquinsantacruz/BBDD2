package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "suppliers")
public class Supplier {

    @Id
    private ObjectId id;

    @Field (name = "business_name")
    private String businessName;

    @Field (name = "authorization_number")
    private String authorizationNumber;

    // @DBRef
    private List<Service> services;

    public Supplier() {}

    public Supplier(String businessName, String authorizationNumber) {
        this.businessName = businessName;
        this.authorizationNumber = authorizationNumber;
        this.services = new ArrayList<Service>();
    }

    public Supplier(String businessName, String authorizationNumber, List<Service> services) {
        this.businessName = businessName;
        this.authorizationNumber = authorizationNumber;
        this.services = services;
    }

    public void addService(Service service){
        this.services.add(service);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}
