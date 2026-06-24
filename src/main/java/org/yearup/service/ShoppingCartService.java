package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.*;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService {
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId) {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> items = shoppingCartRepository.findByUserId(userId);
        ShoppingCart cart = new ShoppingCart();
        for (CartItem i : items) {
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem(productService.getById(i.getProductId()), i.getQuantity());
            cart.add(shoppingCartItem);
        }
        return cart;
    }

    public void addProductToCart(int userId, int productId) {
        Product product = productService.getById(productId);

        if (product == null) {
            System.out.println("No product with that id available");
            return;
        }

        List<CartItem> items = shoppingCartRepository.findByUserId(userId);

        for (CartItem i : items) {
            if (i.getProductId() == productId) {
                i.setQuantity(i.getQuantity() + 1);
                shoppingCartRepository.save(i);
                return;
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(1);

        shoppingCartRepository.save(cartItem);
    }

    public ShoppingCart getCart(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();

        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);

        for (CartItem cartItem : cartItems) {
            Product product = productService.getById(cartItem.getProductId());

            if (product != null) {
                ShoppingCartItem item =
                        new ShoppingCartItem(product, cartItem.getQuantity());

                shoppingCart.add(item);
            }
        }
        return shoppingCart;
    }

    public void deleteProductFromCart(int userId, int productId) {
        List<CartItem> items = shoppingCartRepository.findByUserId(userId);

        for (CartItem i : items) {
            if (i.getProductId() == productId) {
                shoppingCartRepository.delete(i);
                return;
            }
        }
    }

    public void updateCart(int userId, int productId, int quantity) {
        List<CartItem> items = shoppingCartRepository.findByUserId(userId);

        for (CartItem i : items) {
            if (i.getProductId() == productId) {
                if (quantity <= 0) {
                    shoppingCartRepository.delete(i);
                } else {
                    i.setQuantity(quantity);
                    shoppingCartRepository.save(i);
                }
                return;
            }
        }
    }

    public void clearCart(int userId) {
        List<CartItem> items = shoppingCartRepository.findByUserId(userId);

        shoppingCartRepository.deleteAll(items);
    }


}



   // }

    // add additional methods here
