import java.util.List;
import java.util.Random;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Address {

    private int addressId;
    private static final Random RAND = new Random();    
    private static final List<Integer> generatedIds = new ArrayList<>();

    
    /**
        * Constructs an Address object with the specified details and inserts it into the database.
        *
        * @param houseNumber The house number of the address.
        * @param roadName The name of the road.
        * @param cityName The name of the city.
        * @param postCode The postal code of the address.
    */
    public Address ( int houseNumber, String roadName, String cityName, String postCode ) {

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

    /**
     * Generates a unique address ID.
     *
     * @return The generated address ID.
     */
    public int generateAddressId() {

        int newId = RAND.nextInt( 900000) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt( 900000) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
       
    }
}
