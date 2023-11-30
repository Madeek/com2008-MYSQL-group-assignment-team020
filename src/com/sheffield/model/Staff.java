import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

public class Staff extends User{

    private String email;
    private Date hireDate;
    private String lastName;
    private String firstName;
    private String phoneNumber;

    /**
     * Represents a staff member in the system.
     * Inherits from the User class and stores additional information such as staff ID, hire date, salary, and role.
     */
    public Staff( String firstName, String lastName, String phoneNumber, String email, String password, int salary, String role ) {

        super( email,  password, role );
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.hireDate = Date.valueOf( LocalDate.now() );

        String query = "INSERT INTO Staff VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020","asheet1Ie" );

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, getUserId() );
            statement.setString( 2, firstName );
            statement.setString( 3, lastName );
            statement.setString( 4, email );
            statement.setString( 5, phoneNumber );
            statement.setDate( 6, hireDate );
            statement.setString( 7, role );
            statement.setInt( 8, salary );
    
            // Execute the insert statements
            statement.executeUpdate();
    
            // Close the statement and connection
            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println( "Error creating staff: " + e.getMessage() );
        }
    }


    /**
     * Displays the stock data for a given product.
     * 
     * @param product the product for which to view the stock data
     */
    public void viewStockData( Product product ) {

        // Your code to view stock data goes here
        if ( product.getStockQuantity() == 1 ) {

            System.out.println( "There is " + product.getStockQuantity() + " " + product.getName() + " in stock." );

        } else if ( product.getProductId() == 0 ) {

            System.out.println( "Needs restocking!" );

        } else {

            System.out.println( "There are " + product.getStockQuantity() + " " + product.getName() + "s in stock." );
        }
    }


    public void updateStockData( Product product, int newStock ) {

        product.stockQuantity = newStock;

    }


/**
 * Accepts an order and performs necessary operations such as updating the order status and stock quantity.
 * Additionally, schedules a task to fulfill the order after a specified delay.
 *
 * @param order The order to be accepted.
 */
   public void acceptOrder( Order order ) {
    
        try {

            // Connect to the database again
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie");

            // Update the status of the order to "Confirmed"
            String updateSql = "UPDATE Order SET status = 'Confirmed' WHERE orderID = ?";
            PreparedStatement statement = conn.prepareStatement( updateSql );
            statement.setInt( 1, order.getOrderId() );
            statement.executeUpdate();

            // Update the stock quantity of each product in the order
            updateSql = "UPDATE Product SET stockQuantity = stockQuantity - ? WHERE productID = ?";
            statement = conn.prepareStatement( updateSql );
            statement.setInt( 1, order.getQuantity() );
            statement.setInt( 2, order.getProduct().getProductId() );
            statement.executeUpdate();

            // Close the connection and the statement
            conn.close();
            statement.close();

            System.out.println("Order confirmed successfully.");

            // Create a ThreadPoolExecutor with a single thread
            ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
            );

            ( ( ScheduledExecutorService ) executorService ).schedule( () -> {

                try {

                    Connection conn2 = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie");
                    String updateSql2 = "UPDATE Order SET status = 'Fulfilled' WHERE orderID = ?";
                    PreparedStatement statement2 = conn2.prepareStatement(updateSql2);
                    statement2.setInt(1, order.getOrderId());
                    statement2.executeUpdate();
                    conn2.close();
                    statement2.close();
                    System.out.println("Order fulfilled successfully.");

                } catch ( SQLException e ) {

                    System.out.println( "Error fulfilling order: " + e.getMessage() );
                }                
            }, 10, TimeUnit.SECONDS);
            
        } catch ( SQLException e ) {

            System.out.println( "Error processing order: " + e.getMessage() );
        }
    }


    /**
     * Declines an order by updating its status to "Declined" in the database.
     * 
     * @param order The order to be declined.
     */
    public void declineOrder( Order order ) {
    
        try {

            // Connect to the database again
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie");

            // Update the status of the order to "Processed"
            String updateSql = "UPDATE Order SET status = 'Declined' WHERE orderID = ?";
            PreparedStatement statement = conn.prepareStatement( updateSql );
            statement.setInt( 1, order.getOrderId() );
            statement.executeUpdate();

            // Close the connection and the statement
            conn.close();
            statement.close();

            System.out.println("Order was declined.");
            
        } catch ( SQLException e ) {

            System.out.println( "Error processing order: " + e.getMessage() );
        }
    }

    /**
     * Deletes a product from the database.
     *
     * @param product the product to be deleted
     */
    public void deleteProduct( Product product ) {

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020",
                    "team020",
                    "asheet1Ie");

            PreparedStatement statement = conn.prepareStatement("DELETE FROM Product WHERE productId = ?");
            statement.setInt(1, product.getProductId());
            statement.executeUpdate();

            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            e.printStackTrace();
        }
    }


    /**
     * Retrieves and displays sales information from the database.
     */
    public void viewSales() {

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a statement
            Statement statement = conn.createStatement();

            // Execute the query
            ResultSet rs = statement.executeQuery( "SELECT * FROM Order" );

            // Print the results
            while ( rs.next() ) {

                int orderId = rs.getInt( "orderID" );
                int orderNumber = rs.getInt( "orderNumber" );
                String orderDate = rs.getString( "date" );
                double totalCost = rs.getDouble( "totalCost" );
                String status = rs.getString( "status" );

                System.out.println( "Order ID: " + orderId );
                System.out.println( "Order Number: " + orderNumber );
                System.out.println( "Order Date: " + orderDate );
                System.out.println( "Total Cost: " + totalCost );
                System.out.println( "Status: " + status );
                System.out.println();
            }

            // Close the connection
            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println( "Error: " + e.getMessage() );
        }
    }


    /**
     * Retrieves and prints information about all orders from the database.
     */
    public void browseOrder() {

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a statement
            Statement statement = conn.createStatement();

            // Execute the query
            ResultSet rs = statement.executeQuery( "SELECT * FROM orders" );

            // Print the results
            while ( rs.next() ) {

                int orderId = rs.getInt( "orderID" );
                int orderNumber = rs.getInt( "orderNumber" );
                String orderDate = rs.getString( "date" );
                double totalCost = rs.getDouble( "totalCost" );
                String status = rs.getString( "status" );

                System.out.println( "Order ID: " + orderId );
                System.out.println( "Order Number: " + orderNumber );
                System.out.println( "Order Date: " + orderDate );
                System.out.println( "Total Cost: " + totalCost );
                System.out.println( "Status: " + status );
                System.out.println();
            }

            // Close the connection
            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println( "Error: " + e.getMessage() );
        }
    }


    /**
     * Adds a new product to the inventory.
     *
     * @param name          the name of the product
     * @param brand         the brand of the product
     * @param price         the price of the product
     * @param stockQuantity the stock quantity of the product
     * @param category      the category of the product
     */
    public void addProduct( String name, String brand, double price, int stockQuantity, Product.Category category ) {

        Product product = new Product( name, brand, price, stockQuantity, category );
    }


    /**
     * Returns the first name of the staff member.
     *
     * @return the first name of the staff member
     */
    public String getFirstName() {

        return firstName;
    }


    /**
     * Returns the last name of the staff member.
     *
     * @return the last name of the staff member
     */
    public String getLastName() {

        return lastName;
    }


    /**
     * Returns the phone number of the staff.
     *
     * @return the phone number of the staff
     */
    public String getPhoneNumber() {

        return phoneNumber;
    }
}
