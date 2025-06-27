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

@Document(collection = "purchases")
public class Purchase {

    @MongoId
    ObjectId id;

    private String code;

    @Field(name = "total_price")
    private float totalPrice;

    private Date date;

    private User user;

    private Route route;

    private Review review;

    @DBRef(lazy = true)
    @Field(name = "item_service_list")
    private List<ItemService> itemServiceList = new ArrayList<ItemService>();

    public Purchase(){}

    public Purchase(String code, User user, Route route){
        this.code = code;
        this.user = user;
        this.route = route;
        this.date = new Date();
        this.totalPrice = route.getPrice();
    }

    public Purchase(String code, User user, Route route, Date date){
        this.code = code;
        this.user = user;
        this.route = route;
        this.date = date;
        this.totalPrice = route.getPrice();
    }

    public void addItem(ItemService itemService, float price){
        this.itemServiceList.add(itemService);
        this.totalPrice += price;
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
