package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;

import java.util.List;

public class OrderService {

    private final Repository<Integer, Order> orderRepo;
    private final Repository<Integer, Product> productRepo;

    public OrderService(Repository<Integer, Order> orderRepo, Repository<Integer, Product> productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;

    }

    public void addOrder(Order o) {
        orderRepo.save(o);
    }

    public void updateOrder(Order o) {
        orderRepo.update(o);
    }

    public void deleteOrder(int id) {
        orderRepo.delete(id);
    }

    public List<Order> getAllOrders() {
//        return StreamSupport.stream(orderRepo.findAll().spliterator(), false)
//                .collect(Collectors.toList());
        return orderRepo.findAll();
    }

    public Order findById(int id) {
        return orderRepo.findOne(id);
    }

//    public double computeTotal(Order o) {
//        return o.getItems().stream()
//                .mapToDouble(i -> {
//                    Product product = productRepo.findOne(i.getProduct().getId());
//                    if (product == null) {
//                        product = i.getProduct();
//                    }
//                    return product.getPret() * i.getQuantity();
//                })
//                .sum();
//    }

    public double computeTotal(Order o) {
        // D1 - comanda null
        if (o == null) {
            throw new IllegalArgumentException("Comanda nu poate fi null.");
        }
        List<OrderItem> items = o.getItems();
        // D2 - lista null sau goala
        if (items == null || items.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        // D3 - parcurgere itemi
        for (OrderItem item : items) {
            // D4 - validare item
            if (item == null ||
                    item.getProduct() == null ||
                    item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item invalid.");
            }
            Product product = item.getProduct();
            total += product.getPret() * item.getQuantity();
        }
        // D5 - discount
        if (total > 200.0) {
            total *= 0.95;
        }
        return total;
    }

    public void addItem(Order o, OrderItem item) {
        o.getItems().add(item);
        orderRepo.update(o);
    }

    public void removeItem(Order o, OrderItem item) {
        o.getItems().remove(item);
        orderRepo.update(o);
    }
}