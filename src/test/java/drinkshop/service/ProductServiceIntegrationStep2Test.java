package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Step 2: integration testing S + V real, R mock")
@Tag("IntegrationTesting")
class ProductServiceIntegrationStep2Test {

    @Mock
    private Repository<Integer, Product> productRepo;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepo, new ProductValidator());
    }

    @Test
    @DisplayName("addProduct - produs valid: validator real, save() apelat")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_validProduct_passesRealValidator() {
        Product p = new Product(1, "Limonada", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        verify(productRepo, times(1)).save(p);
    }

    @Test
    @DisplayName("addProduct - nume gol: validator real arunca exceptie, save() nu e apelat")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_blankName_realValidatorThrows() {
        Product p = new Product(1, "", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        verify(productRepo, never()).save(any());
    }

    @Test
    @DisplayName("addProduct - pret negativ: validator real arunca exceptie, save() nu e apelat")
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    void addProduct_negativePrice_realValidatorThrows() {
        Product p = new Product(1, "Limonada", -5.0,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        verify(productRepo, never()).save(any());
    }
}