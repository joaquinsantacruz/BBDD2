package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "purchases")
public class Purchase {

    @Id
    ObjectId id;

    @Field
    private String code;

    @Field(name = "total_price")
    private float totalPrice;

    @Field
    private Date date;

    //TODO: put annotation
    private User user;

    //TODO: put annotation
    private Route route;

    @Field
    private Review review;

    @Field
    private List<ItemService> itemServiceList;

    public Purchase(){}

    public Purchase(String code, User user, Route route){
        this.code = code;
        this.user = user;
        this.route = route;
        this.totalPrice = route.getPrice();
        this.date = new Date();
        user.addPurchase(this);
    }

    public Purchase(String code, User user, Route route, Date date){
        this.code = code;
        this.user = user;
        this.route = route;
        this.date = date;
        this.user.addPurchase(this);
        this.totalPrice = route.getPrice();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setItemServiceList(List<ItemService> itemServiceList) {
        this.itemServiceList = itemServiceList;
    }
}
