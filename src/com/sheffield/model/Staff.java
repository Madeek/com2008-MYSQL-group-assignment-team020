import java.sql.*;
import java.time.LocalDate;
import java.util.Locale.Category;
import java.util.Scanner;

public class Staff extends User{

    private int staffId;
    private String email;
    private Date hireDate;
    private int orderNumber;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private static final String processOrderChoice = "Y";



    public Staff( String firstName, String lastName, String phoneNumber, String email, String password, int salary, String role ) {

        super( email,  password, role );
        this.staffId = getUserId();
        this.hireDate = Date.valueOf( LocalDate.now() );

        String query = "INSERT INTO Staff VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020","asheet1Ie" );

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, this.staffId );
            statement.setString( 2, firstName );
            statement.setString( 3, lastName );
            statement.setString( 4, email );
            statement.setString( 5, phoneNumber );
            statement.setDate( 6, hireDate );
            statement.setString( 7, role );
            statement.setInt( 8, salary );
            statement.executeUpdate();

            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println( "Error creating staff: " + e.getMessage() );
        }
    }


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


    public void processOrder( Order order ) {

        Date orderDate = Date.valueOf( LocalDate.now() );
        String query = "INSERT INTO Order VALUES ( ?, ?, ?, ?, ?, ? )";

        try {
            
            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, order.getUserId() );
            statement.setInt( 2, order.getOrderId() );
            statement.setInt( 3, orderNumber );
            statement.setDate( 4, orderDate );
            statement.setDouble( 5, order.getTotalCost() );
            statement.setString( 6, "Pending" );
            statement.executeUpdate();

            orderNumber++;

            // Close the statement and connection
            statement.close();
            conn.close();

            // Ask the staff if they want to process the order
            Scanner scanner = new Scanner( System.in );
            System.out.println( "Do you want to process the order? ( Y/N )" );
            String choice = scanner.nextLine();

            if ( choice.equalsIgnoreCase( processOrderChoice ) ) {
                // Connect to the database again
                conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie");

                // Update the status of the order to "Processed"
                String updateSql = "UPDATE Order SET status = 'Processed' WHERE orderID = ?";
                statement = conn.prepareStatement( updateSql );
                statement.setInt(1, order.getOrderId());
                statement.executeUpdate();

                // Update the stock quantity of each product in the order
                updateSql = "UPDATE Product SET stockQuantity = stockQuantity - ? WHERE productID = ?";
                statement = conn.prepareStatement( updateSql );
                statement.setInt(1, order.getQuantity() );
                statement.setInt(2, order.getProduct().getProductId() );
                statement.executeUpdate();

                // Close the statement and connection
                statement.close();
                conn.close();

                System.out.println("Order processed successfully.");

            } else {

                System.out.println("Order not processed.");
            }

        } catch ( SQLException e ) {

            System.out.println( "Error: " + e.getMessage() );
        }
    }

    public void deleteProduct(Product product) {

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

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }


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


    public void addProduct( String name, String brand, double price, int stockQuantity, Product.Category trainsets ) {

        Product product = new Product( name, brand, price, stockQuantity, trainsets );
    }


    public String getFirstName() {

        return firstName;
    }


    public String getLastName() {

        return lastName;
    }


    public String getEmail() {

        return email;
    }


    public String getPhoneNumber() {

        return phoneNumber;
    }
}
