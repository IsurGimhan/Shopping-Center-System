public class Electronic extends Product {
    private String brand;
    private String warrantyPeriod;

    public Electronic(String productId, String productName, int numberOfAvailableItems, double itemPrice, String brand, String warrantyPeriod) {
        super(productId, productName, numberOfAvailableItems, itemPrice);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getBrand() {
        return brand;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    @Override
    public String toString() {

        return super.toString()+'\n'+
               "Brand:-" + brand + '\n' +
                "Warranty period:-" + warrantyPeriod +'\n';
    }
}
