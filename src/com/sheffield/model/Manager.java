import java.sql.*;
import java.time.LocalDate;

public class Manager extends Staff {

    private Date hireDate;   



    /**
     * Represents a Manager in the system.
     * Inherits from the User class and adds additional functionality specific to managers.
     *
     * @param firstName The manager's first name.
     * @param lastName The manager's last name.
     * @param phoneNumber The manager's phone number.
     * @param email The manager's email address.
     * @param password The manager's password.
     */
    public Manager( String firstName, String lastName, String phoneNumber, String email, String password ) {

        super( firstName, lastName, phoneNumber, email, password, 50000, "Manager" );
        this.hireDate = Date.valueOf( LocalDate.now() );

        String query = "INSERT INTO Manager VALUES (?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, getUserId());
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
            statement.setDate(6, hireDate);
            statement.executeUpdate();

            conn.close();
            statement.close();

        } catch ( SQLException e ) {

            System.out.println( "Error creating manager: " + e.getMessage() );
        }
    }

    /**
     * Appoints a staff member using the information from a customer object.
     * 
     * @param customer the customer object containing the staff member's information
     */
    public void appointStaff( Customer customer ) {

        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String phoneNumber = customer.getPhoneNumber();
        String email = customer.getEmail();
        String password = customer.getPassword();

        Staff staff = new Staff( firstName, lastName, phoneNumber, email, password, 25000, "Staff" );     
    }

    /**
     * Removes a staff member from the system.
     * This method updates the role of the staff in the User table to "Customer" and deletes the staff from the Staff table.
     *
     * @param staff the staff member to be removed
     */
    public void removeStaff( Staff staff ) {

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020",
                    "team020",
                    "asheet1Ie");

            // Update the role of the staff in the User table to "Customer"
            positionUpdate(staff, "Customer");

            // Delete the staff from the Staff table
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Staff WHERE staffId = ?");
            stmt.setInt(1, staff.getUserId());
            stmt.executeUpdate();

            conn.close();
            stmt.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    /**
     * Promotes a staff member to a manager.
     * 
     * @param staff the staff member to be promoted
     */
    public void promoteStaff( Staff staff ) {

        positionUpdate(staff, "Manager");
        Manager manager = new Manager( staff.getFirstName(), staff.getLastName(), staff.getPhoneNumber(), staff.getEmail(), staff.getPassword() );
    }
}
