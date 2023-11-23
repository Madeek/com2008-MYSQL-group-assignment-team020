import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class Manager extends Staff {

    private User user;
    private Staff staff;
    private String email;
    private int managerId;
    private Date hireDate;
    private static final Random RAND = new Random();


    public Manager( String firstName, String lastName, String phoneNumber, String email, String password, int salary) {

        super(firstName, lastName, phoneNumber, email, password, salary, "Manager");
        this.hireDate = Date.valueOf(LocalDate.now());
        this.managerId = getUserId();
        this.email = getEmail();

        String query = "INSERT INTO Manager VALUES (?, ?, ?, ?, ?, ?)";

        try {

            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020", "team020",
                    "asheet1Ie");

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, managerId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
            statement.setDate(6, hireDate);
            statement.executeUpdate();

            conn.close();
            statement.close();

        } catch (SQLException e) {

            System.out.println("Error creating manager: " + e.getMessage());
        }
    }

    public int generateManagerId() {

        managerId = RAND.nextInt(900000) + 100000;
        return managerId;
    }

    public void appointStaff(String firstName, String lastName, String phoneNumber, String email, int salary) {

       
    }

    public void removeStaff(User user) {

       
    }

    public void promoteStaff(Staff staff) {
        
        Manager manager = new Manager(staff.getFirstName(), staff.getLastName(), staff.getEmail(), staff.getPhoneNumber());
    }
}
