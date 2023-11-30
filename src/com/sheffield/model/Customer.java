import java.sql.*;
import java.util.*;
import java.sql.Date;

public class Customer extends User {

    private int customerId;
    private String lastName;
    private String firstName;
    private String phoneNumber;

    /**
     * Represents a customer in the system.
     * Inherits from the User class.
     *
     * @param firstName The customer's first name.
     * @param lastName The customer's last name.
     * @param phoneNumber The customer's phone number.
     * @param email The customer's email address.
     * @param password The customer's password.
     * @param birthDate The customer's date of birth.
     */
    public Customer( String firstName, String lastName, String email, String password, String phoneNumber,
            Date birthDate ) {

        super( email, password, "Customer" );
        this.lastName = lastName;
        this.firstName = firstName;
        this.customerId = getUserId();
        this.phoneNumber = phoneNumber;


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

            statement.executeUpdate();
    
            // Close the statement and connection
            conn.close(); 
            statement.close();

        } catch ( SQLException e ) {

            System.out.println("Error creating customer: " + e.getMessage());
        }
    }


    /**
     * This method allows the customer to browse the products in the database.
     * It connects to the database, executes a query to retrieve all products,
     * and prints the product name and retail price for each product.
     * If an error occurs during the process, it prints an error message.
     */
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

    /**
     * Creates an order for the customer with the given list of products and quantities.
     * 
     * @param products   the list of products to be ordered
     * @param quantities the list of quantities corresponding to each product
     */
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
            }
        }
    }

    /**
     * Edits the quantities of the given orders.
     * 
     * @param orders         the list of orders to be edited
     * @param newQuantities  the list of new quantities corresponding to each order
     * @return               true if all orders were edited successfully, false otherwise
     */
    public boolean editOrder( List<Order> orders, List<Integer> newQuantities ) {

        if ( orders.size() != newQuantities.size() ) {

            System.out.println("Error: Number of orders and quantities do not match.");
            return false;
        }

        for ( int i = 0; i < orders.size(); i++ ) {
            Order order = orders.get( i );
            int newQuantity = newQuantities.get( i );

            if ( newQuantity <= 0 ) {

                System.out.println( "Error: Invalid quantity for product " + order.getProduct().getName() );
                continue;
            }

            if ( newQuantity <= 0 ) {

                System.out.println("Order canceled for product " + order.getProduct().getName());
                orders.remove(order);

            } else if ( order.getProduct().getStockQuantity() < newQuantity ) {

                System.out.println("Sorry, product " + order.getProduct().getName() + " does not have sufficient stock.");

            } else {

                order.setQuantity( newQuantity );
                System.out.println( "Order edited successfully. New quantity for product " + order.getProduct().getName()
                        + " is " + newQuantity );
            }
        }

        System.out.println("All orders edited successfully.");
        return true;
    }

    /**
     * Returns the customer ID.
     *
     * @return the customer ID
     */
    public int getCustomerId() {

        return customerId;
    }


    /**
     * Returns the first name of the customer.
     *
     * @return the first name of the customer
     */
    public String getFirstName() {

        return firstName;
    }


    /**
     * Returns the last name of the customer.
     *
     * @return the last name of the customer
     */
    public String getLastName() {

        return lastName;
    }


    /**
     * Returns the phone number of the customer.
     *
     * @return the phone number of the customer
     */
    public String getPhoneNumber() {

        return phoneNumber;
    }
}
