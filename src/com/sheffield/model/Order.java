import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Order {

    private Staff staff;
    private int orderId;
    private int quantity;
    private int customerId;
    private int lineNumber;
    private int orderLineId;
    private Double lineCost;
    private Product product;
    private Customer customer;
    private static final Random RAND = new Random();

    public Order(int customerId, Product product, int quantity) {

        this.orderId = generateOrderId();
        this.orderLineId;
        this.lineNumber;
        this.lineCost;
        this.quantity = quantity;
        this.product = product;
        this.customerId = customerId;

        String query = "INSERT INTO OrderLine VALUES (?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, product.getProductId());
            statement.setInt(2, this.orderId);
            statement.setInt(3, orderLineId);
            statement.setInt(4, lineNumber);
            statement.setInt(5, quantity);
            statement.setDouble(6, lineCost);

            statement.executeUpdate();

            process();

        } catch (SQLException e) {

            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private void process() {

        Order order = new Order(this.customerId, this.product, this.quantity);
        staff.processOrder(order);
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

    public Product getProduct() {

        return product;
    }

    public double getTotalCost() {

        return product.getPrice() * quantity;
    }

    public int getQuantity() {
        
        return quantity;
    }
}
