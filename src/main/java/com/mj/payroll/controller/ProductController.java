package com.mj.payroll.controller;

import com.mj.payroll.exception.ProductNotFoundException;
import com.mj.payroll.model.Product;
import com.mj.payroll.model.assembler.ProductModelAssembler;
import com.mj.payroll.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductModelAssembler assembler;
    private final ProductRepository productRepository;

    @GetMapping("/products")
    public CollectionModel<EntityModel<Product>> getAllProducts() {
        List<EntityModel<Product>> products = productRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(products,
                linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
    }
    @GetMapping("/products/{id}")
    public EntityModel<Product> getProductById(@PathVariable Long id){
         Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
         return assembler.toModel(product);
    }

    @PostMapping("products/")
    public ResponseEntity<?> addProduct(@RequestBody Product product){
        EntityModel<Product> newProduct = assembler.toModel(product);
        return ResponseEntity
                .created(newProduct.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(newProduct);
    }

    @PutMapping("products/{id}")
    public ResponseEntity<?> editProductById(@PathVariable Long id,@RequestBody Product product){
        Product productEntityModel = productRepository.findById(id)
                .map(entity -> {
                    entity.setName(product.getName());
                    entity.setCategory(product.getCategory());
                    entity.setPrice(product.getPrice());
                    return productRepository.save(entity);
                }).orElseGet(() -> {
                    product.setId(id);
                    return productRepository.save(product);
                });

        EntityModel<Product> editedProduct = assembler.toModel(productEntityModel);
        return ResponseEntity
                .created(editedProduct.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(editedProduct);
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id){
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
