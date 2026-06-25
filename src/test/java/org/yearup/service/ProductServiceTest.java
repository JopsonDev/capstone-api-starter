package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void search_shouldReturnAllProducts_withNoFliters() {
        // arrange
        Product featuredProduct = new Product();
        featuredProduct.setPrice(50.0);
        featuredProduct.setSubCategory("Shoes");
        featuredProduct.setFeatured(true);

        Product nonFeaturedProduct = new Product();
        nonFeaturedProduct.setPrice(75.0);
        nonFeaturedProduct.setSubCategory("Hats");
        nonFeaturedProduct.setFeatured(false);

        Mockito.when(productRepository.findAll())
                .thenReturn(List.of(featuredProduct, nonFeaturedProduct));

        // act
        List<Product> results = productService.search(null, null, null, null);

        // assert
        assertEquals(2, results.size());
        assertTrue(results.contains(featuredProduct));
        assertTrue(results.contains(nonFeaturedProduct));
    }

    @Test
    void update_ShouldUpdateAllValues_WithNewValues(){
        //Arrange
        Product oldProduct = new Product();
        oldProduct.setStock(10);
        oldProduct.setName("Wireless Mouse");
        oldProduct.setPrice(24.99);
        oldProduct.setCategoryId(3);
        oldProduct.setDescription("Ergonomic wireless mouse with USB receiver.");
        oldProduct.setSubCategory("Computer Accessories");
        oldProduct.setFeatured(false);
        oldProduct.setImageUrl("images/wireless-mouse.jpg");

        Product updatedProduct = new Product();
        updatedProduct.setStock(35);
        updatedProduct.setName("Wireless Gaming Mouse");
        updatedProduct.setPrice(39.99);
        updatedProduct.setCategoryId(5);
        updatedProduct.setDescription("High-performance gaming mouse with customizable RGB lighting.");
        updatedProduct.setSubCategory("Gaming Peripherals");
        updatedProduct.setFeatured(true);
        updatedProduct.setImageUrl("images/wireless-gaming-mouse.jpg");

        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(oldProduct));

        //Act
        Product result = productService.update(1, updatedProduct);

        //Assert
        assertEquals(updatedProduct.getStock(), result.getStock());
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getCategoryId(), result.getCategoryId());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
        assertEquals(updatedProduct.getSubCategory(), result.getSubCategory());
        assertEquals(updatedProduct.isFeatured(), result.isFeatured());
        assertEquals(updatedProduct.getImageUrl(), result.getImageUrl());

        verify(productRepository).save(oldProduct);
    }
}