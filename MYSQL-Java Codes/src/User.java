import java.sql.*;

public class User {

    private String name;

    public User(String name) {

        this.name = name;
    }


    public void register() {

        Customer customer = new Customer(name);
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

}
