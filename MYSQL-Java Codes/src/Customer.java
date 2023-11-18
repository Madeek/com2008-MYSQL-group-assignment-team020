import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Customer {

    private User user;
    private Staff staff;
    private String email;
    private int customerId;

    public Customer( String firstName, String lastName, String phoneNumber, Date birthDate, User user ) {

        this.user = user;
        this.customerId = user.getUserId();
        this.email = user.getEmail();

        String query = "INSERT INTO Customer VALUES (?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie" );

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, customerId );
            statement.setString( 2, firstName );
            statement.setString( 3, lastName );
            statement.setString( 4, email );
            statement.setString( 5, phoneNumber );
            statement.setDate( 6, birthDate );

            // Execute the insert statement
            statement.executeUpdate();

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
            while (rs.next()) {

                System.out.println(rs.getString("productName") + " - " + rs.getDouble("retailPrice"));
            }

            // Close the connection and statement
            stmt.close();
            conn.close();

        } catch (SQLException e) {

            System.out.println("Error browsing products: " + e.getMessage());
        }
    }

    public boolean placeOrder(List<Product> products, List<Integer> quantities) {

        if (products.size() != quantities.size()) {

            System.out.println("Error: Number of products and quantities do not match.");
            return false;
        }

        for (int i = 0; i < products.size(); i++) {

            Product product = products.get(i);
            int quantity = quantities.get(i);

            if (product.stockQuantity < 1) {

                System.out.println("Sorry, product " + product.getName() + " is out of stock.");

            } else {
                
                Order order = new Order(this.customerId, product, quantity);
                System.out.println("Order placed successfully for product " + product.getName() + " with quantity " + quantity);
            }
        }

        System.out.println("All orders placed successfully.");
        return true;
    }

    public void provideBankDetails(String cardName, String cardHolderName, String cardNumber, Date expiryDate,
            String securityCode) {

        BankDetails bankDetails = new BankDetails(this.customerId, cardName, cardHolderName, cardNumber,
                expiryDate, securityCode);
    }

    public int getCustomerId() {

        return customerId;
    }
}
