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
    private int lineNumber;
    private int orderNumber;
    private int orderLineId;
    private Double lineCost;
    private Product product;
    private Double totalCost;
    private Customer customer;
    private static final Random RAND = new Random();
    private static final List<Integer> generatedIds = new ArrayList<>();


    /**
     * Represents an order made by a customer.
     * Each order contains information such as the customer ID, product, quantity, order line ID, line cost, total cost, and order ID.
     * The order is stored in the database and can be retrieved or modified as needed.
     * 
     * @param userId The ID of the user.
     * @param product The product ordered.
     * @param quantity The quantity of the order.
     */
    public Order( int userId, Product product, int quantity ) {
        
        this.product = product;
        this.quantity = quantity;
        this.orderLineId  = orderId;
        this.lineCost = getTotalCost(); 
        this.totalCost = getTotalCost();
        this.orderId = generateOrderId();

        Date orderDate = Date.valueOf( LocalDate.now() );
        String query = "INSERT INTO `Order` VALUES (?, ?, ?, ?, ?, ?)";

        try {
            
            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a prepared statement
            PreparedStatement stmt = conn.prepareStatement( query );
            stmt.setInt( 1, userId );
            stmt.setInt( 2, orderId );
            stmt.setInt( 3, orderNumber );
            stmt.setDate( 4, orderDate );
            stmt.setDouble( 5, totalCost );
            stmt.setString( 6, "Pending" );
            stmt.executeUpdate();

            orderNumber++;

            // Close the statement and connection
            conn.close();
            stmt.close();

            // Add the order to the order line
            addToOrderLine();

        } catch ( SQLException e ) {

            System.out.println("Error creating order: " + e.getMessage());
        }
    }

    /**
     * Adds a new order line to the database.
     */
    public void addToOrderLine() {
            
            String query = "INSERT INTO OrderLine VALUES (?, ?, ?, ?, ?, ?)";
    
            try {
    
                // Connect to the database
                Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );
    
                // Create a prepared statement
                PreparedStatement stmt = conn.prepareStatement( query );
                stmt.setInt( 1, product.getProductId() );
                stmt.setInt( 2, orderId );
                stmt.setInt( 3, orderLineId );
                stmt.setInt( 4, lineNumber );
                stmt.setInt( 5, quantity );
                stmt.setDouble( 6, lineCost );
                stmt.executeUpdate();
    
                lineNumber++;
    
                // Close the statement and connection
                conn.close();
                stmt.close();
    
            } catch ( SQLException e ) {
    
                System.out.println("Error creating orderline: " + e.getMessage());
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

    /**
     * Returns the order ID.
     *
     * @return the order ID
     */
    public int getOrderId() {

        return orderId;
    }

    /**
     * Returns the user ID associated with this order.
     *
     * @return the user ID
     */
    public int getUserId() {
            
        return customer.getCustomerId();
    }

    /**
     * Returns the product associated with this order.
     *
     * @return the product associated with this order
     */
    public Product getProduct() {

        return product;
    }

    /**
     * Calculates and returns the total cost of the order.
     * The total cost is calculated by multiplying the price of the product by the quantity.
     *
     * @return the total cost of the order
     */
    public double getTotalCost() {

        return product.getPrice() * quantity;
    }

    /**
     * Returns the quantity of the order.
     *
     * @return the quantity of the order
     */
    public int getQuantity() {
        
        return quantity;
    }

    /**
     * Sets the quantity of the order and updates the corresponding records in the database.
     * 
     * @param newQuantity the new quantity of the order
     */
    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
        
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database_name", "username", "password");
            
            // Update quantity in Order table
            String updateOrderQuery = "UPDATE Order SET quantity = ? WHERE orderId = ?";
            PreparedStatement updateOrderStmt = conn.prepareStatement(updateOrderQuery);
            updateOrderStmt.setInt(1, newQuantity);
            updateOrderStmt.setInt(2, this.orderId);
            updateOrderStmt.executeUpdate();
            
            // Update quantity in OrderLine table
            String updateOrderLineQuery = "UPDATE OrderLine SET quantity = ? WHERE orderId = ?";
            PreparedStatement updateOrderLineStmt = conn.prepareStatement(updateOrderLineQuery);
            updateOrderLineStmt.setInt(1, newQuantity);
            updateOrderLineStmt.setInt(2, this.orderId);
            updateOrderLineStmt.executeUpdate();
            
            // Close database connection and statements
            conn.close();
            updateOrderStmt.close();
            updateOrderLineStmt.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
