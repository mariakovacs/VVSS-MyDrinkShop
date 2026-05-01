package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Step 3: integration testing S + V + R fake")
class ProductServiceIntegrationStep3Test {

    private final Repository<Integer, Product> productRepo = new FakeProductRepository();
    private final ProductValidator productValidator = new ProductValidator();
    private final ProductService productService = new ProductService(productRepo, productValidator);

    @Test
    void addProduct_validProduct_isAddedInRepository() {
        Product p = new Product(1, "Limonada", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        assertEquals(1, productService.getAllProducts().size());
        assertEquals(p, productService.getAllProducts().get(0));
    }

    @Test
    void addProduct_blankName_throwsExceptionAndNotAdded() {
        Product p = new Product(2, "", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        assertEquals(0, productService.getAllProducts().size());
    }

    @Test
    void findById_existingProduct_returnsProduct() {
        Product p = new Product(1, "Limonada", 10.5,
                CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p);

        Product result = productService.findById(1);

        assertEquals(p, result);
    }

    static class FakeProductRepository implements Repository<Integer, Product> {

        private final List<Product> products = new ArrayList<>();

        @Override
        public Product findOne(Integer id) {
            return products.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Product> findAll() {
            return products;
        }

        @Override
        public Product save(Product entity) {
            products.add(entity);
            return entity;
        }

        @Override
        public Product delete(Integer id) {
            Product found = findOne(id);
            if (found != null) {
                products.remove(found);
            }
            return found;
        }

        @Override
        public Product update(Product entity) {
            delete(entity.getId());
            products.add(entity);
            return entity;
        }
    }
}