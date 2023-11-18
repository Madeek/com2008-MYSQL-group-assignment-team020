import java.sql.Date;

/**
 * This is the main class of the application.
 * Author: Emmanuel Ita
 */
public class App {

    public static void main(String[] args) {

        DataAccessLayer accessor = new DataAccessLayer();

        try {

            accessor.connect();
            

            User user = new User("aaavvf@gmail.com", "tterssdff", "Customer");

            // Create a new user with username "admin" and password "password".
            Customer customer = new Customer("Aybike", "Koyunlu", "07055522213",
                    Date.valueOf("2003-12-01"), user);
            customer.provideBankDetails("Visa", "MR A B. C", "12345678912345687",
            Date.valueOf("2016-12-01"), "555");

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            accessor.disconnect();
        }

        System.out.println("Hello, World!");
    }
}
