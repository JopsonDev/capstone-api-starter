package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
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

    //public ShoppingCart addProduct(ShoppingCart shoppingCart, int productId, int quantity){
       // ShoppingCartItem shoppingCartItem = new ShoppingCartItem(productService.getById(productId), quantity);


   // }

    // add additional methods here
}
