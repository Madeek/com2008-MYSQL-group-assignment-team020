import java.sql.*;

public class Staff {

    private int staffId;
    private int orderNumber;
    private String name;
    private String email;
    private String phoneNumber;

    public Staff(int staffId, String name, String email, String phoneNumber) {

        this.staffId = staffId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
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

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a statement
            Statement stmt = conn.createStatement();

            orderNumber++;

            // Insert the order into the database
            String sql = "INSERT INTO orders (orderID, orderNumber, date, totalCost, status) VALUES (" +
                    order.getOrderId() + ", " +
                    orderNumber + ", " +
                    "'" + order.getDate() + "', " +
                    order.getTotalCost() + ", " +
                    "'" + "Processed" + "')";

            stmt.executeUpdate(sql);

            // Update the stock quantity of each product in the order
            String updateSql = "UPDATE products SET stockQuantity = stockQuantity - " + order.getQuantity()
                    + " WHERE productID = " + order.getItem().getProductId();
            stmt.executeUpdate(updateSql);

            // Close the statement and connection
            stmt.close();
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
}
