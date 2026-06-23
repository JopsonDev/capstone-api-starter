package org.yearup.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_item_id")
    private int orderLineItemId;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "sales_price")
    private BigDecimal salesPrice = BigDecimal.ZERO;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "discount")
    private BigDecimal discount = BigDecimal.ZERO;

    public OrderLineItem(int orderLineItemId, int orderId, int productId, BigDecimal salesPrice, int quantity, BigDecimal discount) {
        this.orderLineItemId = orderLineItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public OrderLineItem() {

    }

    public int getOrderLineItemId() {
        return orderLineItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }
}
