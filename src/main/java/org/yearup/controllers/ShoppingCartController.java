package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.service.ProductService;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;
import java.security.Principal;
import java.util.List;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    private final ProductService productService;
    // a shopping cart controller depends on the service layer
    private ShoppingCartService shoppingCartService;
    private UserService userService;


    public ShoppingCartController(ProductService productService, ShoppingCartService shoppingCartService, UserService userService) {
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }


    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCart getCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        return shoppingCartService.getByUserId(user.getId());

    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be added)
    // return the updated cart with status 201 Created
    @PostMapping("/products/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addProduct(@PathVariable int productId, Principal principal) {
        User user = userService.getByUserName(principal.getName());

        if(productService.getById(productId) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No product found with id: " + productId);
        }

        shoppingCartService.addProductToCart(user.getId(), productId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ShoppingCart> clearCart(Principal principal) {
        User user = userService.getByUserName(principal.getName());

        shoppingCartService.clearCart(user.getId());

        ShoppingCart cart = shoppingCartService.getByUserId(user.getId());

        return ResponseEntity.ok(cart);
    }


    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId, Principal principal) {
        User user = userService.getByUserName(principal.getName());

        if(productService.getById(productId) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No product found with id: " + productId);
        }
        shoppingCartService.deleteProductFromCart(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ShoppingCart> updateProductQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        User user = userService.getByUserName(principal.getName());

        if(productService.getById(productId) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No product found with id: " + productId);
        }

        shoppingCartService.updateCart(user.getId(), productId, item.getQuantity());

        ShoppingCart cart = shoppingCartService.getByUserId(user.getId());

        return ResponseEntity.ok(cart);
    }



    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)



    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)

}
