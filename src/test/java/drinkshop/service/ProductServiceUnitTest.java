package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Step 1: Unit Testing ProductService cu Mockito")
class ProductServiceUnitTest {

    @Mock
    private Repository<Integer, Product> productRepo;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductService productService;

    @Test
    void addProduct_valid_callsValidatorAndSave() {
        Product p = new Product(1, "Limonada", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        verify(productValidator, times(1)).validate(p);
        verify(productRepo, times(1)).save(p);
    }

    @Test
    void addProduct_invalid_validatorThrows_saveNotCalled() {
        Product p = new Product(2, "", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        doThrow(new ValidationException("Nume invalid"))
                .when(productValidator).validate(p);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        verify(productValidator, times(1)).validate(p);
        verify(productRepo, never()).save(any());
    }

    @Test
    void findById_existingProduct_returnsProduct() {
        Product p = new Product(1, "Limonada", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        when(productRepo.findOne(1)).thenReturn(p);

        Product result = productService.findById(1);

        assertEquals(p, result);
        verify(productRepo, times(1)).findOne(1);
    }
}