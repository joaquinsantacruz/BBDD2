package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(name = "total_price", nullable = false)
    private float totalPrice;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "review_id")
    private Review review;

    @OneToMany(mappedBy = "purchase", cascade = {CascadeType.REMOVE, CascadeType.PERSIST},  fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ItemService> itemServiceList = new ArrayList<ItemService>();

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

    public void addItem(ItemService item, float price){
        this.itemServiceList.add(item);
        this.totalPrice += price;
    }

    public void removeFromUser(){
        this.user.removePurchase(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
