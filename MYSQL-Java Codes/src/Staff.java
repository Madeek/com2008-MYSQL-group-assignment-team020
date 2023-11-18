import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class Staff {

    private int staffId;
    private String email;
    private Date hireDate;
    private int orderNumber;
    private String position;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private static final Random RAND = new Random();


    public Staff(String firstName, String lastName, String email, String phoneNumber, int salary) {

        this.position = "Staff";
        this.staffId = generateStaffId();
        this.hireDate = Date.valueOf(LocalDate.now());

        String query = "INSERT INTO Manager VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, staffId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
            statement.setDate(6, hireDate);
            statement.setString(7, position);
            statement.setInt(8, salary);
            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println("Error creating staff: " + e.getMessage());
        }
    }

    private int generateStaffId() {
        
        staffId = RAND.nextInt(900000) + 100000;
        return staffId;
    }

    public void viewStockData(Product product) {

        // Your code to view stock data goes here
        if (product.getStockQuantity() == 1) {

            System.out.println("There is " + product.getStockQuantity() + " " + product.getName() + " in stock.");

        } else if (product.getProductId() == 0) {

            System.out.println("Needs restocking!");

        } else {

            System.out.println("There are " + product.getStockQuantity() + " " + product.getName() + "s in stock.");
        }
    }

    public void updateStockData(Product product, int newStock) {

        product.stockQuantity = newStock;

    }

    public void processOrder(Order order) {

        Date orderDate = Date.valueOf(LocalDate.now());

        String query = "INSERT INTO Order VALUES (?, ?, ?, ?, ?, ?)"; 

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement
            
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, order.getUserId());
            statement.setInt(2, order.getOrderId());
            statement.setInt(3, orderNumber);
            statement.setDate(4, orderDate);
            statement.setDouble(5, order.getTotalCost());
            statement.setString(6, "Processed");
            statement.executeUpdate();
            
            orderNumber++;

            // Update the stock quantity of each product in the order
            String updateSql = "UPDATE products SET stockQuantity = stockQuantity - " + order.getQuantity()
                    + " WHERE productID = " + order.getItem().getProductId();
            statement.executeUpdate(updateSql);

            // Close the statement and connection
            statement.close();
            conn.close();

        } catch (SQLException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewSales() {

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a statement
            Statement stmt = conn.createStatement();

            // Execute the query
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders");

            // Print the results
            while (rs.next()) {

                int orderId = rs.getInt("orderID");
                int orderNumber = rs.getInt("orderNumber");
                String orderDate = rs.getString("date");
                double totalCost = rs.getDouble("totalCost");
                String status = rs.getString("status");

                System.out.println("Order ID: " + orderId);
                System.out.println("Order Number: " + orderNumber);
                System.out.println("Order Date: " + orderDate);
                System.out.println("Total Cost: " + totalCost);
                System.out.println("Status: " + status);
                System.out.println();
            }

            // Close the connection
            conn.close();

        } catch (SQLException e) {

            System.out.println("Error: " + e.getMessage());
        }

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
