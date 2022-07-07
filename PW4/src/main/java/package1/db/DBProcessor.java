package package1.db;

import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            PreparedStatement st = con.prepareStatement("CREATE TABLE IF NOT EXISTS PRODUCTS (product_id INTEGER PRIMARY KEY AUTOINCREMENT, product_name TEXT, 'type' INTEGER, amount INTEGER, price INTEGER );");
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


    public void createProducts(String pname, int pcat_type, int pamount, int pprice) {
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTS(product_name,type, amount, price) VALUES (?, ?, ?,?)");
            statement.setString(1, pname);
            statement.setInt(2, pcat_type);
            statement.setInt(3, pamount);
            statement.setInt(4, pprice);
            int result = statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    public void updateProduct(int pid, String pname, int ctype, int pamount, int pprice) {
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE PRODUCTS SET product_name = ?, type = ?, amount = ?, price = ? WHERE product_id = ?");
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
    public void deleteProduct(int pid) {
        try {
            PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTS WHERE product_id = ?");
            statement.setInt(1, pid);
            statement.executeUpdate();
            System.out.println("PRODUCT with id=" + pid + " is deleted;");
        } catch (SQLException e) {
            System.out.println("Продукт вже видалено, або його ніколи й не існувало");
            e.printStackTrace();
        }
    }

    public void listProductByCategoryCriteria(int cat_type){
        try {
            PreparedStatement st = con.prepareStatement("SELECT * FROM PRODUCTS WHERE type LIKE ?");
            st.setInt(1, cat_type);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                int id = res.getInt("product_id");
                String name = res.getString("product_name");
                int type = res.getInt("type");
                int amount = res.getInt("product_id");
                int price = res.getInt("price");
                System.out.println("\n---LIST BY CATEGORY TYPE ------\nproduct_id : " + id + "\nproduct_name : " + name + "\ntype : " + type + "\namount : "+ amount + "\nprice : " + price + "\n---LIST BY CATEGORY TYPE ------");
            }
            res.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    public void showProductDataById(int id) {
        try {
            PreparedStatement st = con.prepareStatement("SELECT * FROM PRODUCTS WHERE product_id LIKE ?");
            st.setInt(1, id);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                int idd = res.getInt("product_id");
                String name = res.getString("product_name");
                int type = res.getInt("type");
                int amount = res.getInt("amount");
                int price = res.getInt("price");
                System.out.println("\n-----SHOW BY ID------\nproduct_id : " + idd + "\nproduct_name : " + name + "\ntype : " + type + "\namount : "+ amount + "\nprice : " + price + "\n-----SHOW BY ID------");
            }
            st.close();
            res.close();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    public List<Object> returnProductDataById(int id) {
        try {
            PreparedStatement st = con.prepareStatement("SELECT * FROM PRODUCTS WHERE product_id LIKE ?");
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            List<Object> result = new ArrayList<Object>();
            while (res.next()) {
                result.add(res.getInt("product_id"));
                result.add(res.getString("product_name"));
                result.add(res.getInt("type"));
                result.add(res.getInt("amount"));
                result.add(res.getInt("price"));
            }
            st.close();
            res.close();
            if(result.size() == 0) return null;
            return result;
        } catch (SQLException e) {
            System.out.println("Продукту не існує");
            e.printStackTrace();
        }
        return null;
    }
}

