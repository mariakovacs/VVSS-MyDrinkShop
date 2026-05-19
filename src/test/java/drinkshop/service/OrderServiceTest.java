package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.AbstractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("WB – OrderService.computeTotal() – Coverage complet")
class OrderServiceTest {

    private static class InMemoryProductRepository extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }

    private static class InMemoryOrderRepository extends AbstractRepository<Integer, Order> {
        @Override
        protected Integer getId(Order entity) {
            return entity.getId();
        }
    }

    private InMemoryProductRepository productRepo;
    private InMemoryOrderRepository orderRepo;
    private OrderService service;

    @BeforeEach
    void setUp() {
        productRepo = new InMemoryProductRepository();
        orderRepo = new InMemoryOrderRepository();
        service = new OrderService(orderRepo, productRepo);
    }

    @Test
    @Tag("WhiteBox")
    @DisplayName("o = null -> exception")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void o_null_throwsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.computeTotal(null)
        );
        assertEquals("Comanda nu poate fi null.", ex.getMessage());
    }

    @Test
    @Tag("WhiteBox")
    @DisplayName("items = null -> 0.0")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void itemsNull_returnsZero() {
        Order order = new Order(1, null, 0.0);
        assertEquals(0.0, service.computeTotal(order), 0.0001);
    }

    @Test
    @Tag("WhiteBox")
    @DisplayName("items = [] -> 0.0")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void itemsEmpty_returnsZero() {
        Order order = new Order(1, new ArrayList<>(), 0.0);
        assertEquals(0.0, service.computeTotal(order), 0.0001);
    }

    @Test
    @Tag("WhiteBox")
    @DisplayName("items = [null] -> exception")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void itemsContainsNull_throwsException() {
        List<OrderItem> items = new ArrayList<>();
        items.add(null);
        Order order = new Order(1, items, 0.0);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.computeTotal(order)
        );
        assertEquals("Item invalid.", ex.getMessage());
    }


    @Test
    @Tag("WhiteBox")
    @DisplayName("TC05 - product = null -> exception")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void tc05_nullProduct_throwsException() {
        List<OrderItem> items = List.of(new OrderItem(null, 2));
        Order order = new Order(1, items, 0.0);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.computeTotal(order)
        );
        assertEquals("Item invalid.", ex.getMessage());
    }

    @Test
    @Tag("WhiteBox")
    @DisplayName("TC06 - quantity <= 0 -> exception")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void tc06_invalidQuantity_throwsException() {
        Product p = new Product(1, "Cafea", 20.0, null, null);
        List<OrderItem> items = List.of(new OrderItem(p, 0));
        Order order = new Order(1, items, 0.0);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.computeTotal(order)
        );
        assertEquals("Item invalid.", ex.getMessage());
    }

    @Test
    @Tag("WhiteBox")
    @DisplayName("TC07 - valid order, 1 item, no discount")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void tc07_validOrder_oneItem_noDiscount() {
        Product p = new Product(1, "Cafea", 20.0, null, null);
        List<OrderItem> items = List.of(new OrderItem(p, 3));
        Order order = new Order(1, items, 0.0);

        assertEquals(60.0, service.computeTotal(order), 0.0001);
    }

    @Test
    @Tag("WhiteBox")
    @DisplayName("TC08 - valid order, multiple items, no discount")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void tc08_validOrder_multipleItems_noDiscount() {
        Product p1 = new Product(1, "Cafea", 20.0, null, null);
        Product p2 = new Product(2, "Ceai", 30.0, null, null);

        List<OrderItem> items = List.of(
                new OrderItem(p1, 2),
                new OrderItem(p2, 3)
        );

        Order order = new Order(1, items, 0.0);

        assertEquals(130.0, service.computeTotal(order), 0.0001);
    }

    @Test
    @Tag("WhiteBox")
    @DisplayName("TC09 - valid order, discount applied")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void tc09_discountApplied() {
        Product p = new Product(1, "Cafea", 50.0, null, null);
        List<OrderItem> items = List.of(new OrderItem(p, 5));
        Order order = new Order(1, items, 0.0);

        assertEquals(237.5, service.computeTotal(order), 0.0001);
    }
}