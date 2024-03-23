import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCart {
    private ArrayList<Product> userSelectedProducts;
    private HashMap<String,Integer> productIdAndQuantity;
    private String total;
    private String firstUserDiscount;
    private String threeItemDiscount;
    private String finalPrice;

    public ShoppingCart() {
        userSelectedProducts = new ArrayList<>();
        productIdAndQuantity = new HashMap<>();
    }

    public void addProduct(Product product){
        userSelectedProducts.add(product);
        productIdAndQuantity.put(product.getProductId(),1);


    }

    public void removeProduct(String productId){
        Object removingProduct = null;
        for(Product product : userSelectedProducts){
            if(productId.equals(product.getProductId())){
                removingProduct = product;
            }
        }
        userSelectedProducts.remove(removingProduct);
        String removingKey = null;
        for(HashMap.Entry<String, Integer> entry : productIdAndQuantity.entrySet()){
            if(productId.equals(entry.getKey())){
                removingKey = entry.getKey();
            }
        }
        productIdAndQuantity.remove(removingKey);
    }

    public void totalCost(boolean firstPurchase, boolean threeItems){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double price = 0;
        double discountedPrice = 0;
        for(Product product : userSelectedProducts){
            price = price + (product.getItemPrice()*productIdAndQuantity.get(product.getProductId()));
        }
        total = decimalFormat.format(price);
        double firstUserDis = (price*10)/100;
        firstUserDiscount = decimalFormat.format(firstUserDis);
        double threeItemDis = (price*20)/100;
        threeItemDiscount = decimalFormat.format(threeItemDis);

        if(firstPurchase && threeItems){
            discountedPrice = price - ((price*30)/100);
        }else if(firstPurchase){
            discountedPrice = price -((price*10)/100);
        }else if(threeItems){
            discountedPrice = price -((price*20)/100);
        }else{
            discountedPrice = price;
        }
        finalPrice = decimalFormat.format(discountedPrice);
    }

    public ArrayList<Product> getUserSelectedProducts() {
        return userSelectedProducts;
    }

    public HashMap<String, Integer> getProductIdAndQuantity() {
        return productIdAndQuantity;
    }

    public void addItemsToPIQhashmap(String key,int value){
        productIdAndQuantity.put(key,value);
    }

    public String getTotal() {
        return total;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public String getFirstUserDiscount() {
        return firstUserDiscount;
    }

    public String getThreeItemDiscount() {
        return threeItemDiscount;
    }
}
