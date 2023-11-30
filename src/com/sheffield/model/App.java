import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class of the application.
 * Author: Emmanuel Ita
 */
public class App {

    public static void main(String[] args) {

        Manager manager = new Manager("Nick", "Tyson", "02323230300", "mmcdgmail.com", "nt@gmail.com");
        manager.provideAddress(5, "Balaclava Road", "Sheffield", "S3 2HD");
        manager.addProduct("Scenery", "Suburu", 2345.00, 10, Product.Category.LOCOMOTIVES );
        manager.addProduct("Product 4", "Brand 4", 40.0, 100, Product.Category.CONTROLLERS);

        Staff staff = new Staff("John", "Jones", "0215411564", "jj@gmail.com", "wertyui", 25000, "Staff");

        Customer customer = new Customer( "Lil", "T", "eee@gmail.com","fuvgjfbb",
        "075465464", Date.valueOf("2020-11-20") );
        customer.browseProducts();

        List<Product> products = new ArrayList<>();
        products.add(new Product("Product 3", "Brand 3", 30.0, 100,
        Product.Category.TRACK));
        products.add( new Product("Product 4", "Brand 4", 40.0, 100,
        Product.Category.CONTROLLERS));

        List<Integer> quantities = new ArrayList<>();
        quantities.add(15);
        quantities.add(20);

        customer.createOrder( products, quantities );

        System.out.println("Hello, World!");
    }
}
