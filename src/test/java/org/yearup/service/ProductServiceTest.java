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

import static org.junit.jupiter.api.Assertions.*;
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
}