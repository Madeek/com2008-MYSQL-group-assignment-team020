import java.time.LocalDate;
import java.util.*;

public class Order {

    private int orderId;
    private int quantity;
    private Product product;
    private Customer customer;
    private static final Random RAND = new Random();

    public Order(Customer customer, Product product, int quantity) {

        this.orderId = generateOrderId();
    }

    /**
     * A function that generates a random 6-digit number to be the order ID number.
     * 
     * @return orderId
     */
    private int generateOrderId() {
        orderId = RAND.nextInt(900000) + 100000;
        return orderId;
    }

    public int getOrderId() {

        return orderId;
    }

    public int getUserId() {
            
        return customer.getCustomerId();
    }

    public Product getItem() {

        return product;
    }

    public double getTotalCost() {

        return product.getPrice() * quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
