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
    private static final Random RAND = new Random();
    private static final String SALT = "EncryptedString";
    private static final List<Integer> generatedIds = new ArrayList<>();

    public User(String email, String password, String role) {

        this.password = encrypt(password.toCharArray());
        this.userId = generateUserId();
        this.email = email;

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

            } else {

                // Create a prepared statement
                PreparedStatement statement = conn.prepareStatement( query );
                statement.setInt( 1, userId );
                statement.setString( 2, email );
                statement.setString( 3, this.password );
                statement.setString( 4, role );
                statement.executeUpdate();

                conn.close();
                statement.close();
                checkResult.close();
            }

        } catch ( SQLException e ) {

            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    public int generateUserId() {
        
        int newId = RAND.nextInt( 900000) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt( 900000) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
    }
    

    public void provideAddress( int houseNumber, String roadName, String cityName, String postCode ) {

        Address address = new Address( houseNumber, roadName, cityName, postCode );
    }
    
   
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

    public void provideBankDetails( String cardName, String cardHolderName, String cardNumber, Date expiryDate, String securityCode ) {

        BankDetails bankDetails = new BankDetails( this.userId, cardName, cardHolderName, cardNumber, expiryDate, securityCode );
        
    }

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

    public static byte[] concatenateBytes( byte[] arr1, byte[] arr2 ) {
        
        byte[] combined = new byte[ arr1.length + arr2.length ];
        System.arraycopy( arr1, 0, combined, 0, arr1.length );
        System.arraycopy( arr2, 0, combined, arr1.length, arr2.length );
        return combined;
    }
    

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

    public int getUserId() {

        return userId;
    }

    public String getEmail() {

        return email;
    }

    
    public int generateRecordId() {

        int newId = RAND.nextInt( 900000 ) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt( 900000 ) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
    }
}
