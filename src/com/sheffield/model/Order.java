import java.util.*;
import java.sql.Date;
import java.sql.Connection;
import java.time.LocalDate;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Order {

    private int orderId;
    private int quantity;
    private int orderLineId;
    private Double lineCost;
    private Product product;
    private Double totalCost;
    private Customer customer;
    private int lineNumber = 1;
    private int orderNumber = 1;
    private static final Random RAND = new Random();
    private static final String processOrderChoice = "Y";
    private static final List<Integer> generatedIds = new ArrayList<>();


    public Order( int customerId, Product product, int quantity ) {

        this.orderId = generateOrderId();
        this.orderLineId  = generateOrderId();
        this.lineCost = 0.0; 
        this.quantity = quantity;
        this.product = product;
        this.totalCost = getTotalCost();

        Date orderDate = Date.valueOf( LocalDate.now() );
        String query = "INSERT INTO `Order` VALUES (?, ?, ?, ?, ?, ?)";
        String orderLineQuery = "INSERT INTO OrderLine VALUES (?, ?, ?, ?, ?, ?)";

        try {
            
            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, customerId );
            statement.setInt( 2, orderId );
            statement.setInt( 3, orderNumber );
            statement.setDate( 4, orderDate );
            statement.setDouble( 5, totalCost );
            statement.setString( 6, "Pending" );
            statement.executeUpdate();


            // Create a prepared statement
            PreparedStatement stmt = conn.prepareStatement( orderLineQuery );
            stmt.setInt( 1, product.getProductId() );
            stmt.setInt( 2, orderId );
            stmt.setInt( 3, orderLineId );
            stmt.setInt( 4, lineNumber );
            stmt.setInt( 5, quantity );
            stmt.setDouble( 6, lineCost );

            stmt.executeUpdate();

            lineNumber++;
            orderNumber++;

            // Close the statement and connection
            conn.close();
            stmt.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println("Error creating order: " + e.getMessage());
        }
    }

    /**
     * A function that generates a random 6-digit number to be the order ID number.
     * 
     * @return orderId
     */
    private int generateOrderId() {

        int newId = RAND.nextInt( 900000 ) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt( 900000 ) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
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
