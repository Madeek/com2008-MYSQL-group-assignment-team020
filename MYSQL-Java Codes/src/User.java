import java.sql.*;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    
    private int userId;
    private String email;
    private String password;   
    private static final Random RAND = new Random();


    public User(String email, String password, String role) {

        this.password = encryptPassword(password);
        this.userId = generateUserId();
        this.email = email;

        String query = "INSERT INTO User VALUES (?, ?, ?, ?)";

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setString(2, email);
            statement.setString(3, this.password);
            statement.setString(4, role);
            statement.executeUpdate();

        } catch (SQLException e) {
            
            System.out.println("Error creating user: " + e.getMessage());
        }
    }
    public int generateUserId() {

        userId = RAND.nextInt(900000) + 100000;
        return userId;
    }

    public String encryptPassword(String password) {
    
        try {
            
            // Create an instance of the MD5 hashing algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convert the password string to bytes
            byte[] passwordBytes = password.getBytes();

            // Compute the MD5 hash of the password bytes
            byte[] hashBytes = md.digest(passwordBytes);

            // Convert the hash bytes to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            
            for (byte b : hashBytes) {
                
                sb.append(String.format("%02x", b));
            }

            // Return the encrypted password as a string
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        return null;
    }
    

    public String checkUserType( String firstName, String lastName ) {

        try {

            // Load the JDBC driver for your database
            Class.forName("com.mysql.jdbc.Driver");

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

}
