import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.security.MessageDigest;
import util.HashedPasswordGenerator;
import java.security.NoSuchAlgorithmException;

public class User {
    
    private int userId;
    private String email;         
    private String password; 
    private String watchword;   
    private static final Random RAND = new Random();
    private static final String SALT = "EncryptedString";
    private static final List<Integer> generatedIds = new ArrayList<>();

    /**
     * Represents a user in the system.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param role The user's role.
     */
    public User( String email, String password, String role ) {

        this.email = email;
        this.watchword = password;
        this.userId = generateUserId();
        this.password = encrypt( password.toCharArray() );

        String query = "INSERT INTO User VALUES ( ?, ?, ?, ? )";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie");

            // Check if email already exists in the database
            String checkQuery = "SELECT email FROM User WHERE email = ?";
            PreparedStatement checkStatement = conn.prepareStatement( checkQuery );
            checkStatement.setString( 1, email );
            ResultSet checkResult = checkStatement.executeQuery();

            if ( checkResult.next() ) {

                System.out.println( "Error creating user: Email is already in use" );
                checkResult.close();

            } else {

                // Create a prepared statement
                PreparedStatement stmt = conn.prepareStatement( query );
                stmt.setInt( 1, userId );
                stmt.setString( 2, email );
                stmt.setString( 3, this.password );
                stmt.setString( 4, role );
                stmt.executeUpdate();

                conn.close();
                stmt.close();
            }

        } catch ( SQLException e ) {

            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    /**
     * Generates a unique user ID.
     * 
     * @return The generated user ID.
     */
    public int generateUserId() {
        
        int newId = RAND.nextInt( 900000) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt( 900000) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
    }
    

    /**
     * Provides the address for the user.
     * 
     * @param houseNumber the house number of the address
     * @param roadName the name of the road
     * @param cityName the name of the city
     * @param postCode the postal code of the address
     */
    public void provideAddress( int houseNumber, String roadName, String cityName, String postCode ) {

        Address address = new Address( getUserId(), houseNumber, roadName, cityName, postCode );
    }
    
   
    /**
     * Retrieves and displays the personal record of the user from the database.
     * The personal record includes the user ID, first name, last name, and record ID.
     */
    public void viewPersonalRecord() {

        String query = "SELECT * FROM PersonalRecord WHERE userId = ?";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, userId );

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and print the personal record
            while ( resultSet.next() ) {

                System.out.println( "User ID: " + resultSet.getInt( "userId" ) );
                System.out.println( "First name: " + resultSet.getString( "forename" ) );
                System.out.println( "Last name: " + resultSet.getString( "surname" ) );
                System.out.println( "Record ID: " + resultSet.getInt( "recordID" ) );
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            conn.close();

        } catch ( SQLException e ) {

            System.out.println( "Error viewing personal record: " + e.getMessage() );
        }
    }


    public String verifyLogin( Connection connection, String email, char[] password ) {

        try {

            // Query the database to fetch user information
            String query = "SELECT userID, encryptedPassword FROM User WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement( query );
            statement.setString( 1, email );
            ResultSet resultSet = statement.executeQuery();

            if ( resultSet.next() ) {

                String userId = resultSet.getString( "userId" );
                String storedPasswordHash = resultSet.getString( "encryptedPassword" );

                // Verify the entered password against the stored hash
                if ( verifyPassword( password, storedPasswordHash ) ) {

                    return "Login successful for user: " + email;
                }
            }

        } catch ( SQLException e ) {

            e.printStackTrace();
        }
        return "User not found.";
    }

    private static boolean verifyPassword( char[] password, String storedPasswordHash ) {

        try {

            String hashedEnteredPassword = HashedPasswordGenerator.hashPassword( password );
            return hashedEnteredPassword.equals( storedPasswordHash );

        } catch ( Exception e ) {

            e.printStackTrace();
            return false;
        }
    }

    /**
     * Provides the bank details for the user.
     * 
     * @param cardName       the name of the card
     * @param cardHolderName the name of the card holder
     * @param cardNumber     the card number
     * @param expiryDate     the expiry date of the card
     * @param securityCode   the security code of the card
     */
    public void provideBankDetails( String cardName, String cardHolderName, String cardNumber, Date expiryDate, String securityCode ) {

        BankDetails bankDetails = new BankDetails( this.userId, cardName, cardHolderName, cardNumber, expiryDate, securityCode );
        
    }

    /**
     * Encrypts a given string using SHA-256 hashing algorithm.
     *
     * @param strToEncrypt the string to be encrypted
     * @return the encrypted string in hexadecimal format
     */
    public static String encrypt( char[] strToEncrypt ) {

        try {

            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );

            // Concatenate the salt and password bytes
            byte[] saltedPasswordBytes = concatenateBytes( SALT.getBytes(), new String( strToEncrypt ).getBytes() );

            // Update the digest with the salted password bytes
            md.update( saltedPasswordBytes );

            // Get the hashed password bytes
            byte[] hashedPasswordBytes = md.digest();

            // Convert the hashed password bytes to a hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();

            for ( byte b : hashedPasswordBytes ) {

                hexStringBuilder.append( String.format( "%02x", b ) );
            }

            return hexStringBuilder.toString();

        } catch ( NoSuchAlgorithmException e ) {

            // Handle the exception, e.g., log it or throw a custom exception
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Concatenates two byte arrays into a single byte array.
     * 
     * @param arr1 the first byte array
     * @param arr2 the second byte array
     * @return the combined byte array
     */
    public static byte[] concatenateBytes( byte[] arr1, byte[] arr2 ) {
        
        byte[] combined = new byte[ arr1.length + arr2.length ];
        System.arraycopy( arr1, 0, combined, 0, arr1.length );
        System.arraycopy( arr2, 0, combined, arr1.length, arr2.length );
        return combined;
    }
    

    /**
     * Checks the user type based on the provided first name and last name.
     * 
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @return a string indicating the user type ("This person is a staff member.", "This person is a customer.", "This person is a manager.", or "Unknown user type.")
     * @throws SQLException if an error occurs while accessing the database
     */
    public String checkUserType( String firstName, String lastName ) {

        try {

            // Create a connection to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020","asheet1Ie");

            // Create a statement object to execute SQL queries
            Statement stmt = conn.createStatement();

            // Execute a SELECT query on the Staff table, Customer table, and Manager table
            ResultSet staffResult = stmt.executeQuery("SELECT * FROM Staff");
            ResultSet customerResult = stmt.executeQuery("SELECT * FROM Customer");
            ResultSet managerResult = stmt.executeQuery("SELECT * FROM Manager");

            // Iterate through the result set of each query and check if the name matches
            // the input name
            while ( staffResult.next() ) {

                if ( ( staffResult.getString( "first_name" ).equalsIgnoreCase( firstName ) ) && ( staffResult.getString( "last_name" ).equalsIgnoreCase( lastName ) ) ) {

                    return "This person is a staff member.";
                }
            }

            while ( customerResult.next() ) {

                if ( ( customerResult.getString( "first_name" ).equalsIgnoreCase( firstName ) ) && ( customerResult.getString( "last_name" ).equalsIgnoreCase( lastName ) ) ) {

                    return "This person is a customer.";
                }
            }

            while ( managerResult.next() ) {

                if ( ( managerResult.getString( "first_name" ).equalsIgnoreCase( firstName ) ) && ( managerResult.getString( "last_name" ).equalsIgnoreCase( lastName ) ) ) {

                    return "This person is a manager.";
                }
            }

            // If no match is found, return "Unknown user type."
            return "Unknown user type.";

        } catch ( SQLException e ) {

            e.printStackTrace();
        }

        return "Error occurred while checking user type.";
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {

        return userId;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address as a String
     */
    public String getEmail() {

        return email;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password as a String
     */
    public String getPassword() {

        return watchword;
    }

    public void positionUpdate(User user, String newRole) {
        
        try {
            
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie");

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement("UPDATE User SET role = ? WHERE userId = ?");
            statement.setString(1, newRole);
            statement.setInt(2, user.getUserId());
            statement.executeUpdate();

            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println("Error updating user position: " + e.getMessage());
        }
    }

    
    /**
     * Generates a unique record ID for the user.
     * 
     * @return The generated record ID.
     */
    public int generateRecordId() {

        int newId = RAND.nextInt( 900000 ) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt( 900000 ) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
    }
}
