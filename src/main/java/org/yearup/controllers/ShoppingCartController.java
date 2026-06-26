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
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    private final ProductService productService;
    private ShoppingCartService shoppingCartService;
    private UserService userService;


    public ShoppingCartController(ProductService productService, ShoppingCartService shoppingCartService, UserService userService) {
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCart getCart(Principal principal) {
        User user = userService.getByUserName(principal.getName());
        return shoppingCartService.getCart(user.getId());
    }

    @PostMapping("/products/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ShoppingCart> addProductToCart(
            @PathVariable int productId,
            Principal principal) {

        User user = userService.getByUserName(principal.getName());

        shoppingCartService.addProductToCart(user.getId(), productId);

        ShoppingCart cart = shoppingCartService.getCart(user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
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

}
