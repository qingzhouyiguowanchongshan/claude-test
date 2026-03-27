package com.shop.product;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Product> list(@RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return repo.findByCategory(category);
        }
        return repo.findAll();
    }

    @GetMapping("/categories")
    public List<String> categories() {
        return repo.findAllCategories();
    }
}
