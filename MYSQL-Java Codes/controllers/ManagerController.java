import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")

public class ManagerController {

    @GetMapping("/manager/view-users")
    public String viewUsers() {

        // Code to view users
        return "Viewing users";
    }

    @PostMapping("/manager/hire-staff")
    public String hireStaff() {

        // Code to hire staff
        return "Staff hired";
    }

    @DeleteMapping("/manager/fire-staff")
    public String fireStaff() {

        // Code to fire staff
        return "Staff fired";
    }
}
