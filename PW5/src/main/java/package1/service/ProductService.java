package package1.service;

import org.json.simple.JSONObject;
import package1.db.DBProcessor;
import package1.entity.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
    public DBProcessor db;

    public ProductService(DBProcessor db) {
        this.db = db;
    }


    public JSONObject getProductsData() {
        List<Product> p = db.returnProductData();
        JSONObject products = new JSONObject();
        for (Product product : p) {
            JSONObject j = new JSONObject();
            j.put("category_type", product.getType());
            j.put("product_name", product.getName());
            j.put("amount", product.getAmount());
            j.put("product_id", product.getId());
            j.put("price", product.getPrice());
            products.put(product.getId(), j);
        }
        return products;
    }

    public JSONObject getProductDataByID(int id) {

        Product product = db.returnProductDataByID(id);
        JSONObject j = new JSONObject();
        j.put("category_type", product.getType());
        j.put("product_name", product.getName());
        j.put("amount", product.getAmount());
        j.put("product_id", product.getId());
        j.put("price", product.getPrice());
        return j;
    }

    public void putNewProduct(JSONObject product) {
        long id = (long) product.get("product_id");
        String name = (String) product.get("product_name");
        long type = (long) product.get("type");
        long amount = (long) product.get("amount");
        long price = (long) product.get("price");
        db.createProducts((int) id, name, (int) type, (int) amount, (int) price);
    }

    public void updateProduct(String id, JSONObject product) {
        Product changing = db.returnProductDataByID(Integer.parseInt(id));
        if (product.containsKey("product_name")) changing.setName((String) product.get("product_name"));
        if (product.containsKey("type")) changing.setType((int) (long) product.get("type"));
        if (product.containsKey("amount")) changing.setAmount((int) (long) product.get("amount"));
        if (product.containsKey("price")) changing.setPrice((int) (long) product.get("price"));
        db.updateProduct(changing.getId(), changing.getName(), changing.getType(), changing.getAmount(), changing.getPrice());
    }

    public void deleteProduct(String id) {
        db.deleteProduct(Integer.parseInt(id));
    }

    public List<Integer> returnExistingID() {
        return db.getIds();
    }

}
