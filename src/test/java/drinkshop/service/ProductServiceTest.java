package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest {

    private ProductService productService;
    private Repository<Integer, Product> productRepo;

    @BeforeEach
    void setUp() {
        productRepo = new Repository<>() {
            private final List<Product> products = new ArrayList<>();

            @Override
            public Product save(Product entity) {
                products.add(entity);
                return entity;
            }

            @Override
            public Product findOne(Integer id) {
                for (Product p : products) {
                    if (p.getId() == id) {
                        return p;
                    }
                }
                return null;
            }

            @Override
            public List<Product> findAll() {
                return products;
            }

            @Override
            public Product delete(Integer id) {
                return null;
            }

            @Override
            public Product update(Product entity) {
                return null;
            }
        };

        productService = new ProductService(productRepo);
    }

    // ==================== ECP ====================

    @Test
    @DisplayName("ECP valid: product with valid name and valid price")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductEcpValidProduct() {
        // Arrange
        Product product = new Product(
                12,
                "Lemonade",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        // Act + Assert
        assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("ECP invalid: empty name")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductEcpEmptyName() {
        // Arrange
        Product product = new Product(
                13,
                "",
                12.0,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("ECP invalid: price equal to zero")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductEcpZeroPrice() {
        // Arrange
        Product product = new Product(
                15,
                "Espresso",
                0.0,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("ECP invalid: negative price")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductEcpNegativePrice() {
        // Arrange
        Product product = new Product(
                16,
                "Cappuccino",
                -3.0,
                CategorieBautura.MILK_COFFEE,
                TipBautura.DAIRY
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("ECP invalid: name is null")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductEcpNullName() {
        // Arrange
        Product product = new Product(
                17,
                null,
                10.0,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    // ==================== BVA ====================

    @Test
    @DisplayName("BVA invalid: name length is 0")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaNameZero() {
        // Arrange
        Product product = new Product(
                18,
                "",
                9.0,
                CategorieBautura.TEA,
                TipBautura.WATER_BASED
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA valid: name length is 1")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaNameOne() {
        // Arrange
        Product product = new Product(
                19,
                "A",
                9.0,
                CategorieBautura.TEA,
                TipBautura.WATER_BASED
        );

        // Act + Assert
        assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA valid: name length is 2")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaNameTwo() {
        // Arrange
        Product product = new Product(
                20,
                "Ab",
                9.0,
                CategorieBautura.TEA,
                TipBautura.WATER_BASED
        );

        // Act + Assert
        assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA invalid: price is -1")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPriceMinusOne() {
        // Arrange
        Product product = new Product(
                21,
                "Still water with lemon",
                -1.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA invalid: price is 0")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPriceZero() {
        // Arrange
        Product product = new Product(
                22,
                "Green Tea",
                0.0,
                CategorieBautura.TEA,
                TipBautura.WATER_BASED
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA valid: price is 0.01")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPriceMinimumValid() {
        // Arrange
        Product product = new Product(
                23,
                "Limonade",
                0.01,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        // Act + Assert
        assertDoesNotThrow(() -> productService.addProduct(product));
    }


    @Test
    @DisplayName("BVA valid: price is 0.02")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPriceJustAboveMinimum() {
         // Arrange
         Product product = new Product(
                 24,
                 "Herbal Tea",
                 0.02,
                 CategorieBautura.TEA,
                 TipBautura.WATER_BASED
    );

    // Act + Assert
    assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA invalid: name is null")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaNameNull() {
        // Arrange
        Product product = new Product(
                25,
                null,
                10.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

}

