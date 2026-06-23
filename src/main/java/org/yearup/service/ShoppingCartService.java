package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
       List<CartItem> items = shoppingCartRepository.findByUserId(userId);
       ShoppingCart cart = new ShoppingCart();
       for(CartItem i: items){
           ShoppingCartItem shoppingCartItem = new ShoppingCartItem(productService.getById(i.getProductId()), i.getQuantity());
           cart.add(shoppingCartItem);
       }
       return cart;
    }

    public void addProductToCart(int userId, int productId) {
        if (productService.getById(productId) == null) {
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

    public void deleteProductFromCart(int userId, int productId){
        if (productService.getById(productId) == null) {
            System.out.println("No product");
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
    }



   // }

    // add additional methods here
}
