package com.devsuperior.examplemockspy.services;

import com.devsuperior.examplemockspy.dto.ProductDTO;
import com.devsuperior.examplemockspy.entities.Product;
import com.devsuperior.examplemockspy.repositories.ProductRepository;
import com.devsuperior.examplemockspy.services.exceptions.InvalidDataException;
import com.devsuperior.examplemockspy.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;

    private Product product;
    private ProductDTO dto;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        product = new Product(1L, "ps5", 2000.0);
        dto = new ProductDTO(product);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

    }

    @Test
    public void insertShouldReturnProductDTOWhenValidData() {
        ProductService serviceSpy = Mockito.spy(service);
        Mockito.doNothing().when(serviceSpy).validateData(dto);

        ProductDTO result = serviceSpy.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), "ps5");
    }

    @Test
    public void insertShouldReturnInvalidDataExceptionWhenProductNameIsBlank() {
        dto.setName("");
        ProductService serviceSpy = Mockito.spy(service);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(dto);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.insert(dto);
        });
    }

    @Test
    public void insertShouldReturnInvalidDataExceptionWhenProductPriceIsNotPositive() {
        dto.setPrice(-5.0);
        ProductService serviceSpy = Mockito.spy(service);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(dto);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.insert(dto);
        });
    }

    @Test
    public void updateShouldReturnProductWhenIdExistsAndValidData() {

        ProductService serviceSpy = Mockito.spy(service);
        Mockito.doNothing().when(serviceSpy).validateData(dto);

        ProductDTO result = serviceSpy.update(existingId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);

    }

    @Test
    public void updateShouldReturnInvalidDataExceptionWhenIdExistsAndProductNameIsBlank() {
        dto.setName("");

        ProductService serviceSpy = Mockito.spy(service);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(dto);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.update(existingId, dto);
        });
    }

    @Test
    public void updateShouldReturnInvalidDataExceptionWhenIdExistsAndProductPriceIsNotPositive() {
        dto.setPrice(-5.0);

        ProductService serviceSpy = Mockito.spy(service);
        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(dto);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.update(existingId, dto);
        });
    }

    @Test
    public void updateShouldReturnResourceNotFindExceptionWhenIdDoesNotExistsAndValidData() {


        ProductService serviceSpy = Mockito.spy(service);

        Mockito.doNothing().when(serviceSpy).validateData(dto);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = serviceSpy.update(nonExistingId, dto);
        });
    }

    @Test
    public void updateShouldReturnInvalidDataExceptionWhenIdDoesNotExistsAndProductNameIsBlack (){
        dto.setName("");

        ProductService serviceSpy = Mockito.spy(service);

        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(dto);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.update(nonExistingId, dto);
        });
    }

    @Test
    public void updateShouldReturnInvalidDataExceptionWhenIdDoesNotExistsAndProductPriceIsNotPositive (){
        dto.setPrice(-5.0);

        ProductService serviceSpy = Mockito.spy(service);

        Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(dto);

        Assertions.assertThrows(InvalidDataException.class, () -> {
            ProductDTO result = serviceSpy.update(nonExistingId, dto);
        });
    }

}
