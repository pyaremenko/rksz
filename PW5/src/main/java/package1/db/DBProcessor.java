package package1.db;

import org.sqlite.SQLiteConfig;
import package1.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBProcessor {

    private Connection con;
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String DRIVER = "org.sqlite.JDBC";


    private static Connection getConnection() throws ClassNotFoundException {
        Class.forName(DRIVER);
        Connection connection = null;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection(DB_URL,config.toProperties());
        } catch (SQLException ex) {}
        return connection;
    }

    public void initialization() {
        try {
            con = getConnection();
            PreparedStatement st = con.prepareStatement("CREATE TABLE IF NOT EXISTS PRODUCTS (product_id INTEGER PRIMARY KEY, product_name TEXT, 'type' INTEGER, amount INTEGER, price INTEGER );");
            int res = st.executeUpdate();

        } catch (ClassNotFoundException e) {
            System.out.println("Не знайшли драйвер JDBC");
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }
    }

    public final static String CREATE = "INSERT INTO PRODUCTS(product_id, product_name,type, amount, price) VALUES (?, ?, ?, ?,?)";
    public void createProducts(int id, String pname, int pcat_type, int pamount, int pprice) {
        try {
            PreparedStatement statement = con.prepareStatement(CREATE);
            statement.setInt(1, id);
            statement.setString(2, pname);
            statement.setInt(3, pcat_type);
            statement.setInt(4, pamount);
            statement.setInt(5, pprice);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    public final static String UPDATE = "UPDATE PRODUCTS SET product_name = ?, type = ?, amount = ?, price = ? WHERE product_id = ?";
    public void updateProduct(int pid, String pname, int ctype, int pamount, int pprice) {
        try {
            PreparedStatement statement = con.prepareStatement(UPDATE);
            statement.setString(1, pname);
            statement.setInt(2, ctype);
            statement.setInt(3, pamount);
            statement.setInt(4, pprice);
            statement.setInt(5, pid);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    public final static String DELETE_BY_ID = "DELETE FROM PRODUCTS WHERE product_id = ?";
    public void deleteProduct(int pid) {
        try {
            PreparedStatement statement = con.prepareStatement(DELETE_BY_ID);
            statement.setInt(1, pid);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Продукт вже видалено, або його ніколи й не існувало");
            e.printStackTrace();
        }
    }

    public static final String GET_PRODUCTS_BY_ID = "SELECT * FROM PRODUCTS WHERE product_id LIKE ?";
    public Product returnProductDataByID(int id) {
        try {
            PreparedStatement st = con.prepareStatement(GET_PRODUCTS_BY_ID);
            st.setInt(1,id);
            ResultSet res = st.executeQuery();
            Product product = new Product();
            while (res.next()) {

                product.setId(res.getInt("product_id"));
                product.setName(res.getString("product_name"));
                product.setType(res.getInt("type"));
                product.setAmount(res.getInt("amount"));
                product.setPrice(res.getInt("price"));
            }
            st.close();
            res.close();
            return product;
        } catch (SQLException e) {
            System.out.println("Продукту не існує");
            e.printStackTrace();
        }
        return null;
    }

    public static final String GET_PRODUCTS = "SELECT * FROM PRODUCTS";
    public List<Product> returnProductData() {
        try {
            PreparedStatement st = con.prepareStatement(GET_PRODUCTS);
            ResultSet res = st.executeQuery();
            List<Product> set = new ArrayList<>();
            while (res.next()) {
                Product product = new Product();
                product.setId(res.getInt("product_id"));
                product.setName(res.getString("product_name"));
                product.setType(res.getInt("type"));
                product.setAmount(res.getInt("amount"));
                product.setPrice(res.getInt("price"));
                set.add(product);
            }
            st.close();
            res.close();
            return set;
        } catch (SQLException e) {
            System.out.println("Продукту не існує");
            e.printStackTrace();
        }
        return null;
    }
    public static final String GET_IDS = "SELECT product_id FROM PRODUCTS";
    public List<Integer> getIds() {
        try {
            PreparedStatement st = con.prepareStatement(GET_IDS);
            ResultSet res = st.executeQuery();
            List<Integer> set = new ArrayList<>();
            while (res.next()) {
                set.add(res.getInt("product_id"));
            }
            st.close();
            res.close();
            return set;
        } catch (SQLException e) {
            System.out.println("Продукту не існує");
            e.printStackTrace();
        }
        return null;
    }
}

