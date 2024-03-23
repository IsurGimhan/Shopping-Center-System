import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WestminsterShoppingManager implements ShoppingManager {

    private ArrayList<Product> productList = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private String productId;
    private int option;
    private int productType;

    @Override
    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void consoleMenu() {
        boolean exit = true;
        while (exit) {
            System.out.println("1. Add a new product");
            System.out.println("2. Delete a product");
            System.out.println("3. Print the list of products");
            System.out.println("4. Save in a file");
            System.out.println("5. GUI");
            System.out.println("6. Exit");

            boolean correctInput = false;
            while (!correctInput) {
                try {
                    System.out.println("Select an option:-");
                    option = scanner.nextInt();
                    if (option < 1 || option > 6) {
                        System.out.println("Invalid input");
                    } else {
                        correctInput = true;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input" + e);
                    scanner.nextLine();
                }
            }

            switch (option) {
                case 1:
                    productIdGetter();
                    addProduct(productId);
                    break;

                case 2:
                    removeProduct(productId);
                    break;

                case 3:
                    printProductList();
                    break;

                case 4:
                    saveFiles();
                    break;

                case 5:
                    UserInfoHandle uih = new UserInfoHandle();
                    uih.loadDataFromUserDetails();
                    LoginPageGUI gui = new LoginPageGUI(uih.getUserInfo());
                    exit = false;
                    break;

                case 6:
                    exit = false;
                    break;
            }
        }
    }

    public String productIdGetter() {
        boolean loop = true;
        while (loop) {
            try {
                System.out.println("Enter the product ID:-");
                productId = scanner.next();
                if (productId.matches(".*[0-9].*") && productId.matches(".*[a-zA-Z].*")) {
                    loop = false;
                }
            } catch (NullPointerException e) {
                System.out.println("Invalid ProductId" + e);
                scanner.nextLine();
            }
        }
        return productId;
    }

    public void addProduct(String productId) {
        System.out.println("Select the product type \n 1.Electronic \n 2.Clothing");
        boolean correctInput = false;
        while (!correctInput) {
            try {
                System.out.println("Enter the Product type 1 or 2 :-");
                productType = scanner.nextInt();
                if (productType < 1 || productType > 2) {
                    System.out.println("Invalid Input");
                } else {
                    correctInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
                scanner.nextLine();
            }
        }


        String productName = null;
        while (productName == null || productName.contains(" ")) {
            System.out.println("Enter the Product Name:-");
            productName = scanner.next();

        }


        int numberOfProducts = 0;
        boolean correctInput2 = false;
        scanner.nextLine();
        while (!correctInput2) {
            try {
                System.out.println("Enter the products amount:-");
                numberOfProducts = scanner.nextInt();
                if (numberOfProducts < 1) {
                    System.out.println("Invalid input");
                } else {
                    correctInput2 = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }

        Double productPrice = 0.0;
        boolean done = false;
        while (!done) {
            try {
                System.out.println("Enter the product price:-");
                productPrice = scanner.nextDouble();
                if (productPrice < 1) {
                    System.out.println("Invalid input");
                } else {
                    done = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }

        switch (productType) {
            case 1:
                String productBrand = null;
                boolean looping = false;
                while (!looping) {
                    try {
                        System.out.println("Enter the electronic item brand:-");
                        productBrand = scanner.next();
                        looping = true;
                    } catch (NoSuchElementException | IllegalStateException e) {
                        System.out.println("Invalid input");
                    }
                }

                String productWarranty = null;
                boolean bool = false;
                while (!bool) {
                    try {
                        System.out.println("Enter the item warranty period in weeks:-");
                        int productWarrantyWeeks = scanner.nextInt();
                        productWarranty = productWarrantyWeeks + " " + "weeks warranty";
                        if (numberOfProducts < 1) {
                            System.out.println("Invalid input");
                        } else {
                            bool = true;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input");
                        scanner.nextLine();
                    }
                }

                Electronic electronic = new Electronic(productId, productName, numberOfProducts, productPrice, productBrand, productWarranty);
                productList.add(electronic);
                System.out.println("Product has been added to the system");
                break;

            case 2:
                String clotheSize = null;
                boolean input2 = false;
                while (!input2) {
                    try {
                        System.out.println("Enter the size of the clothe:-");
                        clotheSize = scanner.next();
                        input2 = true;
                    } catch (NoSuchElementException | IllegalStateException e) {
                        System.out.println("Invalid input");
                    }
                }

                String clotheColour = null;
                boolean loop = false;
                while (!loop) {
                    try {
                        System.out.println("Enter the colour of the clothe:-");
                        clotheColour = scanner.next();
                        loop = true;
                    } catch (NoSuchElementException | IllegalStateException e) {
                        System.out.println("Invalid input");
                    }
                }
                Clothing clothes = new Clothing(this.productId, productName, numberOfProducts, productPrice, clotheSize, clotheColour);
                productList.add(clothes);
                System.out.println("Product has been added to the system");
                break;
        }
    }

    public void removeProduct(String removingItem) {
        boolean idFound = false;
        while (!idFound) {
            productIdGetter();
            for (Product product : productList) {
                if (product.getProductId().equals(productId)) {
                    System.out.println(product);
                    productList.remove(product);
                    System.out.println("Product has been removed successfully");
                    System.out.println("Available items in the list :- " + productList.size());
                    try {
                        FileWriter fileWriter = new FileWriter("shoppingManager.txt");
                    } catch (IOException e) {
                        System.out.println(e);
                        break;
                    }
                    saveFiles();

                    idFound = true;
                    break;
                }
            }
            if (!idFound) {
                System.out.println("Product ID does not match with the database");
            }
        }
    }

    public void printProductList() {
        Collections.sort(productList, Comparator.comparing(Product::getProductName));
        for (Product product : productList) {
            System.out.println(product);
        }
    }

    public void saveFiles() {
        String filePath = "shoppingManager.txt";
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            for (Product product : productList) {
                if (product instanceof Electronic) {
                    fileWriter.write("Electronic" + ",");
                    fileWriter.write(product.getProductId() + ",");
                    fileWriter.write(product.getProductName() + ",");
                    fileWriter.write(product.getNumberOfAvailableItems() + ",");
                    fileWriter.write(product.getItemPrice() + ",");
                    fileWriter.write(product.getBrand() + ",");
                    fileWriter.write(String.valueOf(product.getWarrantyPeriod()) + '\n');
                } else if (product instanceof Clothing) {
                    fileWriter.write("Clothing" + ",");
                    fileWriter.write(product.getProductId() + ",");
                    fileWriter.write(product.getProductName() + ",");
                    fileWriter.write(product.getNumberOfAvailableItems() + ",");
                    fileWriter.write(product.getItemPrice() + ",");
                    fileWriter.write(product.getSize() + ",");
                    fileWriter.write(product.getColour() + '\n');
                }
            }
            fileWriter.close();
            System.out.println("product information has been saved to the file");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void loadFiles() {
        try {
            String filePath = "shoppingManager.txt";
            File shoppingManager = new File(filePath);
            Scanner fileReader = new Scanner(shoppingManager);
            while (fileReader.hasNextLine()) {
                String[] temp = fileReader.nextLine().split(",");
                if (temp[0].equals("Electronic")) {
                    Electronic electronic = new Electronic(temp[1], temp[2], Integer.parseInt(temp[3]), Double.parseDouble(temp[4]), temp[5], temp[6]);
                    productList.add(electronic);
                } else if (temp[0].equals("Clothing")) {
                    Clothing clothing = new Clothing(temp[1], temp[2], Integer.parseInt(temp[3]), Double.parseDouble(temp[4]), temp[5], temp[6]);
                    productList.add(clothing);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
}
