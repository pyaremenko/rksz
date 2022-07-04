package package1.db;

import org.sqlite.SQLiteConfig;

import java.sql.*;

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
//            con = DriverManager.getConnection("jdbc:sqlite:" + name);

            PreparedStatement state = con.prepareStatement("create table if not exists 'CATEGORY' ('type' INTEGER, 'category_name' TEXT, PRIMARY KEY (type));");
            PreparedStatement st = con.prepareStatement("CREATE TABLE IF NOT EXISTS PRODUCTS (product_id INTEGER PRIMARY KEY AUTOINCREMENT, product_name TEXT, 'type' INTEGER, amount INTEGER, price INTEGER, FOREIGN KEY (type) REFERENCES CATEGORY(type) ON DELETE CASCADE ON UPDATE CASCADE );");
            int result = state.executeUpdate();
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

    public void createCategory(int type, String cat_name) {
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO  CATEGORY(type, category_name) VALUES ( ?,?)");
            statement.setInt(1, type);
            statement.setString(2, cat_name);
            int i = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    public void createProducts(String pname, int pcat_type, int pamount, int pprice) {
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTS(product_name, type, amount, price) VALUES (?, ?, ?,?)");
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

    public void updateCategory(int ctype, String cat_name) {
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE CATEGORY SET category_name = ? WHERE type = ?");
            statement.setString(1, cat_name);
            statement.setInt(2, ctype);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    public void deleteCategory(int ctype) {
        try {
            PreparedStatement statement = con.prepareStatement("DELETE FROM CATEGORY WHERE type = ?");
            statement.setInt(1, ctype);
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
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вставку");
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
                System.out.println("\n--------------------\nproduct_id : " + id + "\nproduct_name : " + name + "\ntype : " + type + "\namount : "+ amount + "\nprice : " + price + "\n--------------------");
            }
            res.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    public void showCategoryData() {
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM CATEGORY");
            while (res.next()) {
                int type = res.getInt("type");
                String name = res.getString("category_name");
                System.out.println("\n--------------------\ntype: " + type + "\ncategory_name : " + name + "\n--------------------");
            }
            res.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    public void showProductData() {
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM PRODUCTS");
            while (res.next()) {
                int id = res.getInt("product_id");
                String name = res.getString("product_name");
                int type = res.getInt("type");
                int amount = res.getInt("product_id");
                int price = res.getInt("price");
                System.out.println("\n--------------------\nproduct_id : " + id + "\nproduct_name : " + name + "\ntype : " + type + "\namount : "+ amount + "\nprice : " + price + "\n--------------------");
            }
            res.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

}

