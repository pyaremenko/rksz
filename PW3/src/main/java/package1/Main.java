package package1;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

import package1.db.DBProcessor;

public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Key SECRET_KEY = getSecureRandomKey( 128);
    public static final Key SECRET_KEY2 = SECRET_KEY;
    public static final String CIPHER = "AES";

    private static Key getSecureRandomKey( int keySize) {
        byte[] secureRandomKeyBytes = new byte[keySize / 8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secureRandomKeyBytes);
        return new SecretKeySpec(secureRandomKeyBytes, CIPHER);
    }

//    public static void main(String[] args) {
//        DBProcessor DBProcessor = new DBProcessor();
//        DBProcessor.initialization();
//        Create
//        Read
//        Update
//        Delete
//        List by criteria
//        DBProcessor.createCategory(1,"vegetables");
//        DBProcessor.createProducts("tomato", 1, 4, 11);
//        DBProcessor.updateProduct(1, "tomato", 1, 12, 10);
//        DBProcessor.updateCategory(1, "vegetables");
//        DBProcessor.showCategoryData();
//        DBProcessor.showProductData();
//        DBProcessor.listProductByCategoryCriteria(2);
//        DBProcessor.deleteCategory(1);
//        DBProcessor.deleteProduct(1);
//    }

}
