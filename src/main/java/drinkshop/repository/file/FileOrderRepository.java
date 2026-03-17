package drinkshop.repository.file;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class FileOrderRepository
        extends FileAbstractRepository<Integer, Order> {

    private Repository<Integer, Product> productRepository;

    public FileOrderRepository(String fileName, Repository<Integer, Product> productRepository) {
        super(fileName);
        this.productRepository = productRepository;
        loadFromFile();
    }

    @Override
    protected Integer getId(Order entity) {
        return entity.getId();
    }

    @Override
    protected Order extractEntity(String line) {

        // Format: id,productId:qty|productId:qty,total
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid order row format: " + line);
        }

        int id = Integer.parseInt(parts[0]);

        List<OrderItem> items = new ArrayList<>();
        String[] products = parts[1].split("\\|");

        for (String product : products) {
            String[] prodParts = product.split(":");
            if (prodParts.length != 2) {
                throw new IllegalArgumentException("Invalid order item format: " + product);
            }

            int productId = Integer.parseInt(prodParts[0]);
            int quantity = Integer.parseInt(prodParts[1]);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Order item quantity must be > 0 for product id=" + productId);
            }
            Product productEntity = productRepository.findOne(productId);
            if (productEntity == null) {
                throw new IllegalArgumentException("Order references missing product id=" + productId);
            }

            items.add(new OrderItem(productEntity, quantity));
        }

        double totalPrice = Double.parseDouble(parts[2]);

        Order order = new Order(id, items, totalPrice);
        double computedTotal = order.getItems().stream().mapToDouble(OrderItem::getTotal).sum();
        if (Math.abs(computedTotal - totalPrice) > 0.0001d) {
            order.setTotalPrice(computedTotal);
        }

        return order;
    }

    @Override
    protected String createEntityAsString(Order entity) {

        StringBuilder sb = new StringBuilder();
        double total = 0.0;

        for (OrderItem item : entity.getItems()) {

            if (sb.length() > 0) {
                sb.append("|");
            }

            sb.append(item.getProduct().getId())
                    .append(":")
                    .append(item.getQuantity());
            total += item.getTotal();
        }

        return entity.getId() + "," +
                sb + "," +
                total;
    }
}
