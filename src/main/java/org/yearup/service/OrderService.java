package org.yearup.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private ProfileService profileService;
    private ShoppingCartService shoppingCartService;
    private OrderRepository orderRepository;
    private OrderLineItemRepository orderLineItemRepository;

    public OrderService(ProfileService profileService, ShoppingCartService shoppingCartService, OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository) {
        this.profileService = profileService;
        this.shoppingCartService = shoppingCartService;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public boolean checkCartForEmpty(ShoppingCart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot checkout with an empty cart");
        }
        return true;
    }

    public Order createOrder(int userId) {
        ShoppingCart cart = shoppingCartService.getByUserId(userId);

        if (!checkCartForEmpty(cart)) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDateTime.now());

        Profile profile = profileService.getById(userId);
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());

        order.setShippingAmount(BigDecimal.ZERO);

        Order savedOrder = orderRepository.save(order);

        for (ShoppingCartItem item : cart.getItems().values()) {
            OrderLineItem lineItem = new OrderLineItem();

            lineItem.setOrderId(savedOrder.getOrderId());
            lineItem.setProductId(item.getProductId());
            lineItem.setSalesPrice(item.getProduct().getPrice());
            lineItem.setQuantity(item.getQuantity());
            lineItem.setDiscount(0.00);

            orderLineItemRepository.save(lineItem);
        }

        return savedOrder;
    }
}
