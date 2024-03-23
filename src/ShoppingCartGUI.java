import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShoppingCartGUI extends JFrame {
    // Instance variables
    private ShoppingCart shoppingCart;
    private UserHistory userHistory;
    private UserHistoryHandle userHistoryHandle;
    private DefaultTableModel shoppingCartTableModel;
    private JTable shoppingCartTable;
    private JButton buyButton, removeButton;
    private JPanel panel1, panel2, panel3, panel4, panel5;
    private String userName;
    private String selectedProductDetailsFromTheTable;
    private JLabel total, finalPrice, firstUserDiscount, threeItemsDiscount;

    // Constructor
    ShoppingCartGUI(ShoppingCart shoppingCart, String userName) {
        this.shoppingCart = shoppingCart;
        this.userName = userName;
        userHistoryHandle = new UserHistoryHandle();
        userHistoryHandle.LoadUserHistoryFromFile();

        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        panel5 = new JPanel(new GridLayout(4,1));
        panel5.setPreferredSize(new Dimension(400,100));

        ShoppingCartFrame();
        ShoppingCartTable();
        ShoppingCartRemoveButton();
        pricePanel();
        ShoppingCartBuyButton();
        panel2.add(panel5);

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);
        add(panel4, BorderLayout.EAST);
        setSize(650, 500);
        setVisible(true);
    }

    // Method to initialize the shopping cart frame
    public void ShoppingCartFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Shopping Cart");
        setLayout(new BorderLayout());
    }

    // Method to initialize the shopping cart table
    public void ShoppingCartTable() {
        // Column names for the shopping cart table
        String[] columnName = {"Product", "Quantity", "Price"};

        // Customized DefaultTableModel to make cells non-editable
        shoppingCartTableModel = new DefaultTableModel(new Object[][]{}, columnName) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Initialize the shopping cart table with the custom model
        shoppingCartTable = new JTable(shoppingCartTableModel);
        JTableHeader TableHeader = shoppingCartTable.getTableHeader();
        TableHeader.setReorderingAllowed(false);
        shoppingCartTable.setRowHeight(60);

        // Add a list selection listener to the table
        ListSelectionModel selectionModel = shoppingCartTable.getSelectionModel();
        shoppingCartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    shoppingCartTableSelect();
                }
            }
        });

        // Create a scroll pane for the table and set preferred size
        JScrollPane scrollPane = new JScrollPane(shoppingCartTable);
        Dimension preferredSize = new Dimension(580, 250);
        shoppingCartTable.setPreferredScrollableViewportSize(preferredSize);
        panel1.add(scrollPane);
    }

    // Method to initialize the "Confirm Order" button
    public void ShoppingCartBuyButton() {
        buyButton = new JButton("Confirm Order");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShoppingCartBuyButtonActionPerformed();
            }
        });
        panel3.add(buyButton);
    }

    // Method to initialize the "Remove" button
    public void ShoppingCartRemoveButton() {
        removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shoppingCartRemoveButtonAction();
            }
        });
        panel4.add(removeButton);
    }

    // Method to populate the shopping cart table
    public void shoppingCartTable() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (Product product : shoppingCart.getUserSelectedProducts()) {
            double price = shoppingCart.getProductIdAndQuantity().get(product.getProductId()) * product.getItemPrice();
            String formattedPrice = decimalFormat.format(price);

            if (product instanceof Electronic) {
                String productInfo = "<html>" + product.getProductId() + "<br>" + product.getProductName() + "<br>" + product.getBrand() + "," + product.getWarrantyPeriod() + "</html>";
                shoppingCartTableModel.addRow(new Object[]{productInfo, shoppingCart.getProductIdAndQuantity().get(product.getProductId()), formattedPrice});
            } else if (product instanceof Clothing) {
                String productInfo = "<html>" + product.getProductId() + "<br>" + product.getProductName() + "<br>" + product.getSize() + "," + product.getColour() + "</html>";
                shoppingCartTableModel.addRow(new Object[]{productInfo, shoppingCart.getProductIdAndQuantity().get(product.getProductId()), formattedPrice});
            }
        }
    }

    // Method to display total, discounts, and final price
    public void pricePanel() {
        total = new JLabel();
        firstUserDiscount = new JLabel();
        threeItemsDiscount = new JLabel();
        finalPrice = new JLabel();
        pricePanelAction();
        total.setText("Total " + shoppingCart.getTotal()+"£");
        firstUserDiscount.setText("First purchase discount (10%) "+shoppingCart.getFirstUserDiscount()+"£");
        threeItemsDiscount.setText("Three items in the same category discount (20%) "+shoppingCart.getThreeItemDiscount()+"£");
        finalPrice.setText("Final price " + shoppingCart.getFinalPrice()+"£");
    }

    // Method to calculate and display total, discounts, and final price
    public void pricePanelAction() {
        if (userHistoryHandle.getUserHistoryArrayList().isEmpty()) {
            boolean founded = false;
            for (HashMap.Entry<String, Integer> entry : shoppingCart.getProductIdAndQuantity().entrySet()) {
                if (entry.getValue() >= 3) {
                    panel5.add(total);
                    panel5.add(firstUserDiscount);
                    panel5.add(threeItemsDiscount);
                    panel5.add(finalPrice);
                    shoppingCart.totalCost(true, true);
                    founded = true;
                }
            }
            if (!founded) {
                panel5.add(total);
                panel5.add(firstUserDiscount);
                panel5.add(finalPrice);
                shoppingCart.totalCost(true, false);
            }
        } else {
            boolean cancelLoop = false;
            for (UserHistory userHistory1 : userHistoryHandle.getUserHistoryArrayList()) {
                if (userHistory1.getUserName().equals(userName)) {
                    cancelLoop = true;
                    boolean found = false;
                    for (HashMap.Entry<String, Integer> entry : shoppingCart.getProductIdAndQuantity().entrySet()) {
                        System.out.println(entry.getValue());
                        if (entry.getValue() >= 3) {
                            panel5.add(total);
                            panel5.add(threeItemsDiscount);
                            panel5.add(finalPrice);
                            shoppingCart.totalCost(false, true);
                            found = true;
                        }
                    }
                    if (!found) {
                        panel5.add(total);
                        panel5.add(finalPrice);
                        shoppingCart.totalCost(false, false);
                    }
                }
            }
            if (!cancelLoop) {
                boolean notFound = true;
                for (HashMap.Entry<String, Integer> entry : shoppingCart.getProductIdAndQuantity().entrySet()) {
                    if (entry.getValue() >= 3) {
                        panel5.add(total);
                        panel5.add(firstUserDiscount);
                        panel5.add(threeItemsDiscount);
                        panel5.add(finalPrice);
                        shoppingCart.totalCost(true, true);
                        notFound = false;
                    }
                }
                if (notFound) {
                    panel5.add(total);
                    panel5.add(firstUserDiscount);
                    panel5.add(finalPrice);
                    shoppingCart.totalCost(true, false);
                }
            }
        }
    }

    // Method to handle the "Confirm Order" button click event
    public void ShoppingCartBuyButtonActionPerformed() {
        userHistory = new UserHistory(userName, shoppingCart.getProductIdAndQuantity());
        boolean userFound = false;
        for (UserHistory userDetails : userHistoryHandle.getUserHistoryArrayList()) {
            if (userDetails.getUserName().equals(userName)) {
                for (HashMap.Entry<String, Integer> entry : shoppingCart.getProductIdAndQuantity().entrySet()) {
                    String keys = entry.getKey();
                    int newValue = entry.getValue();

                    if (userDetails.getProductMap().containsKey(keys)) {
                        int value = userHistory.getProductMap().get(keys) + newValue;

                        // Add the existing value to the new value and update in productMap
                        userDetails.getProductMap().put(keys, value);

                    } else {
                        // If the key is not present, simply put the new value in productMap
                        userDetails.getProductMap().put(keys, newValue);
                    }
                }
                userFound = true;
            }
        }
        if (!userFound) {
            userHistoryHandle.getUserHistoryArrayList().add(userHistory);
        }
        userHistoryHandle.saveUserHistoryToFile();
        shoppingCart.getUserSelectedProducts().clear();
        shoppingCart.getProductIdAndQuantity().clear();
    }

    // Method to handle the selection of a row in the shopping cart table
    public void shoppingCartTableSelect() {
        int selectedRow = shoppingCartTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedProductDetailsFromTheTable = shoppingCartTableModel.getValueAt(selectedRow, 0).toString();
        }
    }

    // Method to handle the "Remove" button click event
    public void shoppingCartRemoveButtonAction() {
        if (selectedProductDetailsFromTheTable != null) {
            Pattern pattern = Pattern.compile("<html>(.*?)<br>");
            Matcher matcher = pattern.matcher(selectedProductDetailsFromTheTable);

            // Check if the pattern is found
            if (matcher.find()) {
                // Extract and print the matched part
                String productId = matcher.group(1);
                shoppingCart.removeProduct(productId);
            }
        }
    }
}
