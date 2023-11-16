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

    /**
     * A function that returns the current date.
     * 
     * @return currentDate
     */
    public LocalDate getDate() {

        LocalDate currentDate = LocalDate.now();
        return currentDate;
    }

    public Product getItem() {

        return product;
    }

    public double getTotalCost() {

        double totalCost = product.getPrice() * quantity;
        return totalCost;
    }

    public int getQuantity() {

        return quantity;
    }
}
