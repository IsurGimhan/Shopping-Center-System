public abstract class Product {
    private String productId;
    private String productName;
    private int numberOfAvailableItems;
    private double itemPrice;

    public Product(String productId, String productName, int numberOfAvailableItems, double itemPrice) {
        this.productId = productId;
        this.productName = productName;
        this.numberOfAvailableItems = numberOfAvailableItems;
        this.itemPrice = itemPrice;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getNumberOfAvailableItems() {
        return numberOfAvailableItems;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setNumberOfAvailableItems(int numberOfAvailableItems) {
        this.numberOfAvailableItems = numberOfAvailableItems;
    }

    public String getBrand(){
        return getBrand();
    }
    public String getWarrantyPeriod() {
        return getWarrantyPeriod();
    }
    public String getSize() {
        return getSize();
    }

    public String getColour() {
        return getColour();
    }

    @Override
    public String toString() {
        return
                "ProductId:-" + productId + '\n' +
                "Product Name:-" + productName + '\n' +
                "Available items:-" + numberOfAvailableItems +'\n'+
                "Price:-" + itemPrice;
    }


}
