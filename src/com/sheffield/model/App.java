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

            // Staff staff = new Staff( "Has", "RRER", "07085787213", "ttrte@gmail.com", "ssrwre", 30000, "Staff" );
            // staff.provideAddress( 5, "Balaclava Road", "Sheffield", "S3 2HD" );
            // staff.viewStockData(product);
            

            Customer customer = new Customer( "Lil", "T", "jd@gmail.com","fuvgjfbb", "075465464", Date.valueOf("2020-11-20") );
            // customer.browseProducts();
            
            // Product product = new Product( "Scenery", "Suburu", 2345.00, 10, Product.Category.LOCOMOTIVES );
            // System.out.println( product.getName() );

            
            List<Product> products = new ArrayList<>();
            products.add(new Product("Product 3", "Brand 3", 30.0, 100, Product.Category.TRACK));
            products.add( new Product("Product 4", "Brand 4", 40.0, 100, Product.Category.CONTROLLERS));

            List<Integer> quantities = new ArrayList<>();
            quantities.add(15);
            quantities.add(20);

            customer.createOrder( products, quantities );


        } catch ( Exception e ) {

            e.printStackTrace();

        } finally {

            accessor.disconnect();
        }

        System.out.println("Hello, World!");
    }
}
