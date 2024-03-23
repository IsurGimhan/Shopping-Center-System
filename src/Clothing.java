public class Clothing extends Product {
    private String size;
    private String colour;

    public Clothing(String productId, String productName, int numberOfAvialableItems, double itemPrice, String size, String colour) {
        super(productId, productName, numberOfAvialableItems, itemPrice);
        this.colour = colour;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public String getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return super.toString()+'\n'+
                "Size:-" + size +'\n'+
                "Colour:-" + colour +'\n';
    }
}
