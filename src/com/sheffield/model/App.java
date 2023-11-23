import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import Product.Category;

/**
 * This is the main class of the application.
 * Author: Emmanuel Ita
 */
public class App {

    public static void main( String[] args ) {

        DataAccessLayer accessor = new DataAccessLayer();

        try {

            accessor.connect();

            Staff staff = new Staff( "Has", "RRER", "07085787213", "ttrte@gmail.com", "ssrwre", 30000, "Staff" );
            staff.provideAddress( 5, "Balaclava Road", "Sheffield", "S3 2HD" );
            Product product = new Product( "Scenery", "Suburu", 2345.00, 10, Product.Category.LOCOMOTIVES );

            staff.viewStockData(product);
            
            // List<Product> products = new ArrayList<Product>();
            // products.add(product);+

            // List<Integer> quantities = new ArrayList<Integer>();
            // quantities.add(15);

            // Customer customer = new Customer( "John", "Jones", "jjjj@gmail.com","bbdedrr", "07500465464", Date.valueOf("2010-11-20") );
            // customer.browseProducts();
            // customer.createOrder(products, quantities);


        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            accessor.disconnect();
        }

        System.out.println("Hello, World!");
    }
}
