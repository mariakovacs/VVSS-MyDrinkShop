package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Step 4: integration testing S + V + R real + E real")
class ProductServiceIntegrationStep4Test {

    private static final String TEST_FILE = "test_products.txt";

    private ProductService productService;

    @BeforeEach
    void setUp() {
        new File(TEST_FILE).delete();

        Repository<Integer, Product> productRepo = new FileProductRepository(TEST_FILE);
        ProductValidator productValidator = new ProductValidator();

        productService = new ProductService(productRepo, productValidator);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void addProduct_validProduct_isSavedInFileRepository() {
        Product p = new Product(1, "Limonada", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        assertEquals(1, productService.getAllProducts().size());
        assertEquals("Limonada", productService.getAllProducts().get(0).getNume());
    }

    @Test
    void addProduct_blankName_throwsExceptionAndNotSaved() {
        Product p = new Product(2, "", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        assertEquals(0, productService.getAllProducts().size());
    }

    @Test
    void findById_existingProduct_returnsProductFromFileRepository() {
        Product p = new Product(1, "Limonada", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        Product result = productService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Limonada", result.getNume());
    }
}