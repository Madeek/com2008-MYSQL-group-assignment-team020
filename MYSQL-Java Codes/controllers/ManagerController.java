import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")

public class ManagerController {

    private static final String url = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020";
    private static final String user = "team020";
    private static final String password = "asheet1Ie";

    @GetMapping("/manager/view-users")
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM User")) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("userID"),
                        resultSet.getString("email"),
                        resultSet.getString("encryptedPassword"),
                        resultSet.getString("role")
                );
                user.setId(resultSet.getLong("id"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    @PostMapping("/manager/hire")
    public ResponseEntity<User> hire(@RequestBody User user) {
        // Add user to database
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/manager/fire")
    public ResponseEntity<User> fire(@RequestBody User user) {
        // Remove user from database
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}