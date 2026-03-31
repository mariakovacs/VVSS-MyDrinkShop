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
                15,
                "Lemonade",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        // Act + Assert
        assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("ECP invalid: Name cannot be empty!")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductEcpEmptyName() {
        // Arrange
        Product product = new Product(
                16,
                "",
                15.0,
                CategorieBautura.MILK_COFFEE,
                TipBautura.PLANT_BASED
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("ECP invalid: Name cannot be empty!")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductEcpNullName() {
        // Arrange
        Product product = new Product(
                17,
                null,
                18.5,
                CategorieBautura.BUBBLE_TEA,
                TipBautura.POWDER
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }


    @Test
    @DisplayName("ECP invalid: Invalid price!")
    @Tag("ECP")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductEcpNegativePrice() {
        // Arrange
        Product product = new Product(
                18,
                "Matcha Latte",
                -5.0,
                CategorieBautura.TEA,
                TipBautura.POWDER
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    // ==================== BVA ====================

    @Test
    @DisplayName("BVA invalid: Invalid price!")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPriceMinusOne() {
        // Arrange
        Product product = new Product(
                19,
                "Expresso",
                -1.0,
                CategorieBautura.CLASSIC_COFFEE,
                TipBautura.BASIC
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA invalid: Invalid price!")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPriceMinusZeroPointZeroOne() {
        // Arrange
        Product product = new Product(
                20,
                "Latte",
                -0.01,
                CategorieBautura.MILK_COFFEE,
                TipBautura.DAIRY
        );

        // Act + Assert
        assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA invalid: Invalid price!")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPriceZero() {
        // Arrange
        Product product = new Product(
                21,
                "Matcha",
                0.0,
                CategorieBautura.TEA,
                TipBautura.POWDER
        );

        // Act + Assert
        assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA valid: price = 0.01")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPricePointZeroOne() {
        // Arrange
        Product product = new Product(
                22,
                "Limonada",
                0.1,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA valid: price = 0.02")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPricePointZeroTwo() {
        // Arrange
        Product product = new Product(
                23,
                "Ceai Verde",
                0.02,
                CategorieBautura.TEA,
                TipBautura.WATER_BASED
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA valid: price = 9999.99")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaPriceLarge() {
        // Arrange
        Product product = new Product(
                24,
                "Latte Grande",
                9999.99,
                CategorieBautura.MILK_COFFEE,
                TipBautura.DAIRY
        );

        // Act + Assert
        assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA invalid: Name cannot be null!")
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
        assertDoesNotThrow(() -> productService.addProduct(product));
    }


    @Test
    @DisplayName("BVA invalid: Name cannot be empty!")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaNameEmpty() {
         // Arrange
         Product product = new Product(
                 26,
                 "",
                 12,
                 CategorieBautura.TEA,
                 TipBautura.BASIC
    );

    // Act + Assert
    assertDoesNotThrow(() -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA valid: name = 'A'")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaNameOneChar() {
        // Arrange
        Product product = new Product(
                27,
                "A",
                15.0,
                CategorieBautura.JUICE,
                TipBautura.BASIC
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

    @Test
    @DisplayName("BVA valid: name = 'AB'")
    @Tag("BVA")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void addProductBvaNameTwoChar() {
        // Arrange
        Product product = new Product(
                28,
                "A",
                20.0,
                CategorieBautura.MILK_COFFEE,
                TipBautura.PLANT_BASED
        );

        // Act + Assert
        assertThrows(ValidationException.class, () -> productService.addProduct(product));
    }

}

