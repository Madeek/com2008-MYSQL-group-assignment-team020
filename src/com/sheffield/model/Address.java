import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Address {

    private int addressId;
    private static final Random RAND = new Random();
    
    public Address (int houseNumber, String roadName, String cityName, String postCode) {

        this.addressId = generateAddressId();
        String query = "INSERT INTO Address VALUES (?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a prepared statement to add to the Customer table
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, addressId );
            statement.setInt( 2, houseNumber );
            statement.setString( 3, roadName );
            statement.setString( 4, cityName );
            statement.setString( 5, postCode );

            // Execute the insert statements
            statement.executeUpdate();

            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println("Error creating address: " + e.getMessage());
        }      
    }

    public int generateAddressId() {

        return RAND.nextInt(900000) + 100000;
       
    }
}
