

import java.util.ArrayList;

public interface ShoppingManager {



    void consoleMenu();

    void addProduct(String productId);

    void removeProduct(String productId);

    void printProductList();

    void saveFiles();
    void loadFiles();

    ArrayList<Product> getProductList();

}
