public class Main {
    private ShoppingManager shoppingManager;

    public static void main(String[] args) {
        Main main = new Main();

        main.shoppingManager = new WestminsterShoppingManager();
        main.shoppingManager.consoleMenu();

    }
}