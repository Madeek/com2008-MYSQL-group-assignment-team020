import java.sql.*;
import java.util.*;
import org.springframework.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/staff")

public class StaffController {

    private static final String url = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team020";
    private static final String user = "team020";
    private static final String password = "asheet1Ie";

    @GetMapping("staff/products")
    public List<Product> getProducts() {

        List<Product> productList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Product")) {

            while (resultSet.next()) {

                Product product = new Product(
                        resultSet.getString("productName"),
                        resultSet.getString("brand"),
                        resultSet.getDouble("retailPrice"),
                        resultSet.getInt("stockQuantity"),
                        Product.Category.valueOf(resultSet.getString("category"))
                );

                product.setProductId(resultSet.getLong("productID"));
                productList.add(product);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return productList;
    }

    @PutMapping("staff/products/{id}")
    public ResponseEntity<Product> updateProduct( @PathVariable("id") long id, @RequestBody Product product) {

        Optional<Product> productData = productRepository.findById(id);

        if (productData.isPresent()) {

            Product _product = productData.get();
            _product.setName(product.getName());
            _product.setBrand(product.getBrand());
            _product.setPrice(product.getPrice());
            _product.setStockQuantity(product.getStockQuantity());
            _product.setCategory(product.getCategory());
            staff.updateProduct(_product);
            return new ResponseEntity<>(productRepository.save(_product), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("staff/orders")
    public List<Order> getOrders() {

        List<Order> orderList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orders")) {

            while (resultSet.next()) {

                Order order = new Order();
                order.setId(resultSet.getLong("id"));
                order.setCustomerName(resultSet.getString("customer_name"));
                order.setOrderDate(resultSet.getDate("order_date"));
                orderList.add(order);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return orderList;
    }

    @GetMapping("staff/sales")
    public List<Sales> getSales() {
        
        List<Sales> salesList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM sales")) {

            while (resultSet.next()) {
                Sales sales = new Sales();
                sales.setId(resultSet.getLong("id"));
                sales.setProductName(resultSet.getString("product_name"));
                sales.setSaleDate(resultSet.getDate("sale_date"));
                salesList.add(sales);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesList;
    }
}