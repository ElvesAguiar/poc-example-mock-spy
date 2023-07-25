package com.devsuperior.examplemockspy.services;

import com.devsuperior.examplemockspy.dto.ProductDTO;
import com.devsuperior.examplemockspy.entities.Product;
import com.devsuperior.examplemockspy.repositories.ProductRepository;
import com.devsuperior.examplemockspy.services.exceptions.InvalidDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private Product entity;

    private ProductDTO dto;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 4L;
        nonExistingId = 1000L;
        countTotalProducts = 3L;
        entity = new Product(existingId, "product", 10.0);
        dto = new ProductDTO(entity);
    }

    @Test
    public void insertShouldCreateProduct() {

        service.insert(dto);

        Assertions.assertTrue(repository.findById(entity.getId()).isPresent());
        Assertions.assertTrue(repository.findById(entity.getId()).get().getName().equals(dto.getName()));
        Assertions.assertTrue(repository.findById(entity.getId()).get().getPrice().equals(dto.getPrice()));

    }

    @Test
    public void insertShouldThrowInvalidDataExceptionWhenInvalidData() {


       Assertions.assertThrows(InvalidDataException.class,()->{
           dto.setName("");
           dto.setPrice(-1.0);
           service.insert(dto);
       });

    }

}
