import java.sql.*;
import java.sql.Date;
import java.util.*;

import util.HashedPasswordGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    
    private int userId;
    private String email;         
    private String password;   
    private static final Random RAND = new Random();
    private static final List<Integer> generatedIds = new ArrayList<>();

    public User( String email, String password, String role ) {

        this.password = encryptPassword( password );
        this.userId = generateUserId();
        this.email = email;

        String query = "INSERT INTO User VALUES (?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt(1, userId);
            statement.setString(2, email);
            statement.setString(3, this.password);
            statement.setString(4, role);
            statement.executeUpdate();

            conn.close();
            statement.close();

        } catch (SQLException e) {
            
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    public int generateUserId() {
        
        int newId = RAND.nextInt(900000) + 100000;

        if ( generatedIds.contains( newId ) ) {

            newId = RAND.nextInt(900000) + 100000;
        }
        
        generatedIds.add( newId );
        return newId;
    }
    

    public void provideAddress( int houseNumber, String roadName, String cityName, String postCode ) {

        Address address = new Address( houseNumber, roadName, cityName, postCode);
    }

    public void register() {
        
        int recordId = generateRecordId();

        String query = "INSERT INTO PersonalRecord VALUES (?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection( "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020", "asheet1Ie" );

            // Create a prepared statement to add to the PersonalRecord table
            PreparedStatement statement = conn.prepareStatement( query );
            statement.setInt( 1, userId );
            statement.setString( 2, firstName );
            statement.setString( 3, lastName );
            statement.setInt( 4, recordId );

            // Execute the insert statements
            statement.executeUpdate();

            // Close the statement and connection
            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println( "Error creating customer: " + e.getMessage() );
        }      
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

                // Check if the account is locked
                if ( accountLocked ) {

                    return "Account is locked. Please contact support.";

                } else {

                    // Verify the entered password against the stored hash
                    if ( verifyPassword( password, storedPasswordHash ) ) {

                        return "Login successful for user: " + email;
                    }
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

    public String encryptPassword( String password ) {
    
        try {
            
            // Create an instance of the MD5 hashing algorithm
            MessageDigest md = MessageDigest.getInstance( "MD5" );

            // Convert the password string to bytes
            byte[] passwordBytes = password.getBytes();

            // Compute the MD5 hash of the password bytes
            byte[] hashBytes = md.digest( passwordBytes );

            // Convert the hash bytes to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            
            for ( byte b : hashBytes ) {
                
                sb.append( String.format( "%02x", b ) );
            }

            // Return the encrypted password as a string
            return sb.toString();

        } catch ( NoSuchAlgorithmException e ) {

            e.printStackTrace();
        }

        return null;
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

        } catch ( ClassNotFoundException | SQLException e ) {

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
