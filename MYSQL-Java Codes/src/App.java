import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

/**
 * This is the main class of the application.
 * Author: Emmanuel Ita
 */
public class App {

    public static void main(String[] args) {

        DataAccessLayer accessor = new DataAccessLayer();

        try {

            accessor.connect();

            // Create a new user with username "admin" and password "password".
            Customer Emmanuel = new Customer("Emmanuel", "Ita", "eeeiii@gmail.com", "07055522213",
                    Date.valueOf("2003-12-01"));
            // Emmanuel.provideBankDetails("Mastercard", "MR E C. ITA", "12345678912345687",
            // "08/16", "555");

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            accessor.disconnect();
        }

        System.out.println("Hello, World!");
    }
}
