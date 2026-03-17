package mydrinkshop;

import drinkshop.domain.*;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.OrderService;
import drinkshop.service.StocService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    public void computeTotalUsesRepositoryPriceWhenProductExists() {
        Repository<Integer, Order> orderRepo = new OrderRepo();
        Repository<Integer, Product> productRepo = new ProductRepo();
        productRepo.save(new Product(1, "Espresso", 8.5, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC));

        OrderService orderService = new OrderService(orderRepo, productRepo);
        Order order = new Order(1, List.of(new OrderItem(new Product(1, "Old", 1.0,
                CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC), 2)), 0.0);

        assertEquals(17.0, orderService.computeTotal(order), 0.0001);
    }

    @Test
    public void stockConsumptionKeepsDecimalPrecision() {
        Repository<Integer, Stoc> stocRepo = new StocRepo();
        stocRepo.save(new Stoc(1, "lapte", 10.0, 1.0));

        StocService stocService = new StocService(stocRepo);
        Reteta reteta = new Reteta(1, List.of(new IngredientReteta("lapte", 2.5)));

        stocService.consuma(reteta);

        assertEquals(7.5, stocRepo.findOne(1).getCantitate(), 0.0001);
    }

    private static class ProductRepo extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }

    private static class OrderRepo extends AbstractRepository<Integer, Order> {
        @Override
        protected Integer getId(Order entity) {
            return entity.getId();
        }
    }

    private static class StocRepo extends AbstractRepository<Integer, Stoc> {
        @Override
        protected Integer getId(Stoc entity) {
            return entity.getId();
        }
    }
}
