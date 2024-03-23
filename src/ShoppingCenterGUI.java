import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class ShoppingCenterGUI extends JFrame {
    // Instance variables
    private ShoppingManager shoppingManager;
    private ShoppingCart shoppingCart;
    private ShoppingCartGUI shoppingCartGUI;
    private JLabel text;
    private JComboBox Category;
    private JButton sort;
    private JButton ShoppingCart;
    private DefaultTableModel TableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel textTitle;
    private JLabel infoData;
    private JButton addToCart;
    private JPanel addToCartPanel;
    private String userName;

    // Constructor
    ShoppingCenterGUI(ShoppingManager shoppingManager, ShoppingCart shoppingCart, String userName) {
        this.shoppingManager = shoppingManager;
        this.shoppingCart = shoppingCart;
        this.userName = userName;

        // Initialization methods
        ShoppingCenterFrame();
        ShoppingCenterCategoryComboBox();
        shoppingTableSortButton();
        ShoppingCenterShoppingCartButton();
        ShoppingCenterItemTable();
        ShoppingCenterSelectedProductInfo();
        ShoppingCenterAddToCartButton();

        // Create and organize panels
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(1000, 100));
        add(panel1, BorderLayout.NORTH);
        panel1.add(text);
        panel1.add(Category);
        panel1.add(sort);
        panel1.add(ShoppingCart);

        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(1000, 300));
        add(panel2, BorderLayout.CENTER);
        panel2.add(scrollPane);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setPreferredSize(new Dimension(540, 230));
        add(panel3, BorderLayout.SOUTH);
        panel3.add(textTitle, BorderLayout.NORTH);
        panel3.add(infoData, BorderLayout.WEST);
        panel3.add(addToCartPanel, BorderLayout.SOUTH);

        setSize(650, 700);
        setVisible(true);
    }

    // GUI initialization methods

    public void ShoppingCenterFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Westminster Shopping Center");
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    }

    public void ShoppingCenterCategoryComboBox() {
        text = new JLabel("Select Product Category");

        Category = new JComboBox(new String[]{"All", "Electronic", "Clothes"});
        Category.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e != null) {
                    categoryComboBoxAction();
                }
            }
        });
        Category.setLocation(620, 630);
    }

    public void shoppingTableSortButton() {
        sort = new JButton("Sort");
        sort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e != null) {
                    shoppingTableSortActionMethod();
                }
            }
        });
    }

    public void ShoppingCenterShoppingCartButton() {
        ShoppingCart = new JButton("Shopping cart");
        ShoppingCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shoppingCartGUI = new ShoppingCartGUI(shoppingCart, userName);
                shoppingCartGUI.shoppingCartTable();
            }
        });
    }

    // Product item list
    public void ShoppingCenterItemTable() {
        // Table model and initialization
        String[] ColumnName = {"Product ID", "Name", "Category", "Price", "Info"};
        TableModel = new DefaultTableModel(new Object[][]{}, ColumnName) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add products to the table model
        for (Product product : shoppingManager.getProductList()) {
            if (product instanceof Electronic) {
                TableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), "Electronics", product.getItemPrice(), (product.getBrand() + ',' + product.getWarrantyPeriod())});
            } else if (product instanceof Clothing) {
                TableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), "Clothing", product.getItemPrice(), (product.getSize() + ',' + product.getColour())});
            }
        }

        // Create a JTable with custom rendering for highlighting rows
        table = new JTable(TableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                // Highlight rows based on conditions
                ArrayList<Integer> rowsToHighlight = new ArrayList<>();
                for (Product product : shoppingManager.getProductList()) {
                    if (product.getNumberOfAvailableItems() < 3) {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            Object rowNum = table.getValueAt(i, 0);
                            if (product.getProductId().equals(rowNum.toString())) {
                                rowsToHighlight.add(i);
                            }
                        }
                    }
                }
                // Set background color based on identified rows
                if (rowsToHighlight.contains(row)) {
                    component.setBackground(Color.RED);
                } else {
                    component.setBackground(getBackground());
                }
                return component;
            }
        };

        // Configure table header and selection model
        JTableHeader TableHeader = table.getTableHeader();
        TableHeader.setReorderingAllowed(false);
        ListSelectionModel selectionModel = table.getSelectionModel();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    TableRowSelectAction();
                }
            }
        });

        // Set preferred column width and create scroll pane
        TableColumn column = table.getColumnModel().getColumn(4);
        column.setPreferredWidth(170);
        scrollPane = new JScrollPane(table);
        Dimension preferredSize = new Dimension(580, 250);
        table.setPreferredScrollableViewportSize(preferredSize);
    }

    public void ShoppingCenterSelectedProductInfo() {
        textTitle = new JLabel();
        textTitle.setHorizontalAlignment(JLabel.LEFT);
        textTitle.setFont(new Font("TimesRoman", Font.BOLD, 17));

        infoData = new JLabel();
        infoData.setBounds(0, 250, 100, 50);
        infoData.setFont(new Font("TimesRoman", Font.PLAIN, 15));
    }

    public void ShoppingCenterAddToCartButton() {
        addToCart = new JButton("Add to cart");
        addToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCartButtonAction();
            }
        });
        addToCartPanel = new JPanel(new FlowLayout());
        addToCartPanel.add(addToCart);
    }

    // Action methods

    public void categoryComboBoxAction() {
        int rowCount = TableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            TableModel.removeRow(i);
        }

        if (Category.getSelectedIndex() == 0) {
            for (Product product : shoppingManager.getProductList()) {
                if (product instanceof Electronic) {
                    TableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), "Electronics", product.getItemPrice(), (product.getBrand() + ',' + product.getWarrantyPeriod())});
                } else if (product instanceof Clothing) {
                    TableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), "Clothing", product.getItemPrice(), (product.getSize() + ',' + product.getColour())});
                }
            }
        } else if (Category.getSelectedIndex() == 1) {
            for (Product product : shoppingManager.getProductList()) {
                if (product instanceof Electronic) {
                    TableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), "Electronics", product.getItemPrice(), (product.getBrand() + ',' + product.getWarrantyPeriod())});
                }
            }
        } else if (Category.getSelectedIndex() == 2) {
            for (Product product : shoppingManager.getProductList()) {
                if (product instanceof Clothing) {
                    TableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), "Clothing", product.getItemPrice(), (product.getSize() + ',' + product.getColour())});
                }
            }
        }
    }

    public void TableRowSelectAction() {
        int SelectedRow = table.getSelectedRow();
        if (SelectedRow != -1) {
            for (Product product : shoppingManager.getProductList()) {
                if (product.getProductId() == TableModel.getValueAt(SelectedRow, 0)) {
                    if (product instanceof Electronic) {
                        textTitle.setText("Selected product details");
                        infoData.setText("<html>Product Id:- " + product.getProductId() +
                                "<br>Category:- " + "Electronic" +
                                "<br>Name:- " + product.getProductName() +
                                "<br>Brand:- " + product.getBrand() +
                                "<br>Warranty:- " + product.getWarrantyPeriod() +
                                "<br>Items available:- " + product.getNumberOfAvailableItems() + "</html>");

                    } else if (product instanceof Clothing) {
                        textTitle.setText("Selected product details");
                        infoData.setText("<html>Product Id:- " + product.getProductId() +
                                "<br>Category:- " + "Clothing" +
                                "<br>Name:- " + product.getProductName() +
                                "<br>Size:- " + product.getSize() +
                                "<br>Colour:- " + product.getColour() +
                                "<br>Items available:- " + product.getNumberOfAvailableItems() + "</html>");
                    }
                }
            }
        }
    }

    public void addCartButtonAction() {
        Object selectedRow = TableModel.getValueAt(table.getSelectedRow(), 0);
        for (Product product : shoppingManager.getProductList()) {
            if (product.getNumberOfAvailableItems() != 0) {
                if (product.getProductId().equals(selectedRow)) {
                    if (shoppingCart.getUserSelectedProducts().isEmpty()) {
                        shoppingCart.addProduct(product);
                        int availableItem = product.getNumberOfAvailableItems();
                        availableItem -= 1;
                        product.setNumberOfAvailableItems(availableItem);
                    } else {
                        boolean found = false;
                        for (Product product1 : shoppingCart.getUserSelectedProducts()) {
                            if (Objects.equals(product.getProductId(), product1.getProductId())) {
                                int quantity = shoppingCart.getProductIdAndQuantity().get(product1.getProductId()) + 1;
                                shoppingCart.addItemsToPIQhashmap(product1.getProductId(), quantity);
                                found = true;
                                int availableItem = product.getNumberOfAvailableItems();
                                availableItem -= 1;
                                product.setNumberOfAvailableItems(availableItem);
                                break;
                            }
                        }
                        if (!found) {
                            shoppingCart.addProduct(product);
                            int availableItem = product.getNumberOfAvailableItems();
                            availableItem -= 1;
                            product.setNumberOfAvailableItems(availableItem);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void shoppingTableSortActionMethod() {
        Collections.sort(shoppingManager.getProductList(), Comparator.comparing(Product::getProductName));
        int rowCount = TableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            TableModel.removeRow(i);
        }
        for (Product product : shoppingManager.getProductList()) {
            if (product instanceof Electronic) {
                TableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), "Electronics", product.getItemPrice(), (product.getBrand() + ',' + product.getWarrantyPeriod())});
            } else if (product instanceof Clothing) {
                TableModel.addRow(new Object[]{product.getProductId(), product.getProductName(), "Clothing", product.getItemPrice(), (product.getSize() + ',' + product.getColour())});
            }
        }
    }
}

