import java.sql.*;
import java.util.*;

public class Product {

    public enum Category {

        TRAINSETS, TRAINPACKS, TRACKPACKS, LOCOMOTIVES, CARRIAGES, WAGONS, TRACK, CONTROLLERS 
    }

    int stockQuantity;
    private String name;
    private String brand;
    private double price;
    private int productId;
    private String productCode;
    private static final Random RAND = new Random();
    private static final List<Integer> generatedIds = new ArrayList<>();

    
    /**
     * Represents a product in the inventory.
     * 
     * @param name The name of the product.
     * @param brand The brand of the product.
     * @param price The price of the product. 
     * @param stockQuantity The quantity of the product.
     * @param category The category of the product.
     */
    public Product( String name, String brand, double price, int stockQuantity, Category category ) {

        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.productId = generateProductId();
        this.productCode = generateProductCode();
        
        String query = "INSERT INTO Product VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, productId );
            statement.setString( 2, productCode );
            statement.setString( 3, brand );
            statement.setString( 4, name );
            statement.setDouble( 5, price );
            statement.setDouble( 6, stockQuantity );
            statement.setString( 7, String.valueOf( category ) );

            // Execute the insert statement
            statement.executeUpdate();

            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println( "Error creating customer: " + e.getMessage() );
        } 
    }

    /**
     * Generates a unique product ID.
     *
     * @return The generated product ID.
     */
    private int generateProductId() {

        int newId = RAND.nextInt(900000) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt(900000) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
    }

    /**
     * Generates a product code by appending the product ID to the letter "P".
     *
     * @return the generated product code
     */
    private String generateProductCode() {
        
        return "P" + productId;
    }
    

    /**
     * Returns the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {

        return name;
    }

    
    /**
     * Returns the product ID.
     *
     * @return the product ID
     */
    public int getProductId() {

        return productId;
    }

    /**
     * Returns the brand of the product.
     *
     * @return the brand of the product
     */
    public String getBrand() {

        return brand;
    }

    /**
     * Returns the price of the product.
     *
     * @return the price of the product
     */
    public double getPrice() {

        return price;
    }

    /**
     * Returns the stock quantity of the product.
     *
     * @return the stock quantity of the product
     */
    public int getStockQuantity() {

        return stockQuantity;
    }

    /**
     * Sets the product ID.
     * 
     * @param long1 the product ID as a long value
     */
    public void setProductId( long long1 ) {

        productId = ( int ) long1;
    }
}
