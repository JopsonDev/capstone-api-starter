package org.yearup.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "address")
    private String address = "";

    @Column(name = "city")
    private String city = "";

    @Column(name = "state")
    private String state = "";

    @Column(name = "zip")
    private String zip = "";

    @Column(name = "shipping_amount")
    private BigDecimal shippingAmount = BigDecimal.ZERO;

    public Order(int orderId, int userId, LocalDateTime date, String address, String city, String state, String zip, BigDecimal shippingAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.shippingAmount = shippingAmount;
    }

    public Order() {

    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }
}
