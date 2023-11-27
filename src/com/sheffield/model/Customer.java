import java.sql.*;
import java.sql.Date;
import java.util.*;

// TODO: Fix createOrder() function

public class Customer extends User {

    private Staff staff;    
    private String email;
    private int customerId;

    public Customer( String firstName, String lastName, String email, String password, String phoneNumber,
            Date birthDate ) {

        super( email, password, "Customer" );
        this.customerId = getUserId();

        String query = "INSERT INTO Customer VALUES (?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement to add to the Customer table
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt( 1, customerId );
            statement.setString( 2, firstName );
            statement.setString( 3, lastName );
            statement.setString( 4, email );
            statement.setString( 5, phoneNumber );
            statement.setDate( 6, birthDate );


            // Register their personal details
            int recordId = generateRecordId();

            String query2 = "INSERT INTO PersonalRecord VALUES ( ?, ?, ?, ? )";
    
            // Create a prepared statement to add to the PersonalRecord table
            PreparedStatement stmt = conn.prepareStatement( query2 );
            stmt.setInt( 1, customerId );
            stmt.setString( 2, firstName );
            stmt.setString( 3, lastName );
            stmt.setInt( 4, recordId );
    
            // Execute the insert statements
            stmt.executeUpdate();
            statement.executeUpdate();
    
            // Close the statement and connection
            conn.close();
            stmt.close();  
            statement.close();

        } catch ( SQLException e ) {

            System.out.println("Error creating customer: " + e.getMessage());
        }
    }


    public void browseProducts() {

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a statement
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");

            // Print the results
            while ( rs.next() ) {

                System.out.println( rs.getString("productName") + " - " + rs.getDouble( "retailPrice" ) );
            }

            // Close the connection and statement
            stmt.close();
            conn.close();

        } catch (SQLException e) {

            System.out.println("Error browsing products: " + e.getMessage());
        }
    }

    public void createOrder( List<Product> products, List<Integer> quantities ) {

        if ( products.size() != quantities.size() ) {

            System.out.println( "Error: Number of products and quantities do not match." );
        }

        for ( int i = 0; i < products.size(); i++ ) {

            Product product = products.get( i );
            int quantity = quantities.get( i );

            if ( product.getStockQuantity() < quantities.get( i ) ) {

                System.out.println( "Sorry, product " + product.getName() + " is out of stock." );

            } else {

                Order order = new Order( this.customerId, product, quantity );
                System.out.println( "Order placed successfully for product " + product.getName() + " with quantity " + quantity );
                // staff.processOrder( order );
            }
        }
    }

    public boolean editOrder(List<Order> orders, List<Integer> newQuantities) {

        if ( orders.size() != newQuantities.size() ) {

            System.out.println("Error: Number of orders and quantities do not match.");
            return false;
        }

        for (int i = 0; i < orders.size(); i++) {

            Order order = orders.get(i);
            int newQuantity = newQuantities.get(i);

            if (newQuantity <= 0) {
                System.out.println("Error: Invalid quantity for product " + order.getProduct().getName());
                continue;
            }

            if (newQuantity == 0) {
                System.out.println("Order canceled for product " + order.getProduct().getName());
                orders.remove(order);
                continue;
            }

            if (order.getProduct().getStockQuantity() < newQuantity) {
                System.out
                        .println("Sorry, product " + order.getProduct().getName() + " does not have sufficient stock.");
                continue;
            }

            order.setQuantity(newQuantity);
            System.out.println("Order edited successfully. New quantity for product " + order.getProduct().getName()
                    + " is " + newQuantity);
        }

        System.out.println("All orders edited successfully.");
        return true;
    }

    public int getCustomerId() {

        return customerId;
    }
}
