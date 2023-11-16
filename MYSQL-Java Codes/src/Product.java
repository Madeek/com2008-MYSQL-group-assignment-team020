import java.sql.*;
import java.util.*;

public class Product {

    public enum Category {

        LOCOMOTIVE, PIECE, CARRIAGE, WAGGON, CONTROLLER, SET
    }

    private int productId;
    private String name;
    private String brand;
    private double price;
    int stockQuantity;
    private Category category;
    private static final Random RAND = new Random();

    public Product(String name, String brand, double price, int stockQuantity, Category category) {

        this.productId = generateProductId();
    }

    private int generateProductId() {

        productId = RAND.nextInt(900000) + 100000;
        return productId;
    }

    public void deleteProduct(Product product) {

        try {

            // Connect to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020",
                    "team020",
                    "asheet1Ie");

            PreparedStatement statement = connection.prepareStatement("DELETE FROM products WHERE productId = ?");
            statement.setInt(1, product.getProductId());
            statement.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public String getName() {

        return name;
    }

    public int getProductId() {

        return productId;
    }

    public String getBrand() {

        return brand;
    }

    public double getPrice() {

        return price;
    }

    public int getStockQuantity() {

        return stockQuantity;
    }
}
