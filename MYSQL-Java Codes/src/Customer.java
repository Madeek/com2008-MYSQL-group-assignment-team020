import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Customer {

    private int customerId;
    private Staff staff;
    private Customer customer;
    private static final Random RAND = new Random();

    public Customer(String firstName, String lastName, String email, String phoneNumber, Date birthDate) {

        this.customerId = generateCustomerId();

        String query = "INSERT INTO Customer VALUES (?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, customerId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
            statement.setDate(6, birthDate);
            statement.executeUpdate();

        } catch (SQLException e) {

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

    public boolean placeOrder(Customer customer, Product product, int quantity) {

        Order order = new Order(customer, product, quantity);

        // Your code to create an order goes here
        if (product.stockQuantity < 1) {

            System.out.println("Sorry, this product is out of stock.");
            return false;

        } else {

            // Add the item to the cart-
            staff.processOrder(order);
            System.out.println("Order placed successfully, and is now being processed");
            return true;
        }
    }

    public void provideBankDetails(String cardName, String cardHolderName, String cardNumber, String expiryDate,
            String securityCode) {

        BankDetails bankDetails = new BankDetails(this.customerId, cardName, cardHolderName, cardNumber,
                expiryDate, securityCode);
    }

    private int generateCustomerId() {

        customerId = RAND.nextInt(900000) + 100000;
        return customerId;
    }

    public int getCustomerId() {

        return customerId;
    }
}
