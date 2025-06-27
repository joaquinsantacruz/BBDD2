package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
public class User {

    @MongoId
    private ObjectId id;

    private String username;

    private String password;

    private String name;

    private String email;

    private Date birthdate;

    @Field(name = "phone_number")
    private String phoneNumber;

    private boolean active;

    @DBRef(lazy = true)
    @Field(name = "purchase_list")
    private List<Purchase> purchaseList;

    public User(){}

    public User(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = fullName;
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.purchaseList = new ArrayList<Purchase>();
        this.active = true;
    }

    public User(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, boolean active, List<Purchase> purchases){
        this(username, password, fullName, email, birthdate, phoneNumber);
        this.active = active;
        this.purchaseList = purchases;
    }

    public void addPurchase(Purchase purchase){
        this.purchaseList.add(purchase);
    }

    public void removePurchase(Purchase purchase){
        this.purchaseList.remove(purchase);
    }

    public boolean canBeDeactivated(){
        return true;
    }

    public boolean canBeRemoved(){
        return this.purchaseList.isEmpty();
    }
    
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }



}
