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

    
    public Product( String name, String brand, double price, int stockQuantity, Category category ) {

        this.productId = generateProductId();
        this.productCode = generateProductCode();
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stockQuantity = stockQuantity;
        
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

    private int generateProductId() {

        int newId = RAND.nextInt(900000) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt(900000) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
    }

    private String generateProductCode() {
        
        return "P" + productId;
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

    public void setProductId( long long1 ) {

        productId = ( int ) long1;
    }
}
