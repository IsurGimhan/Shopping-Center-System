import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class LoginPageGUI extends JFrame{
    private User userClass;
    private ShoppingCart shoppingCart;
    private UserInfoHandle userInfoHandle;
    private ShoppingCenterGUI shoppingCenterGUI;
    private ShoppingManager shoppingManager;
    private JTextArea userName;
    private JPasswordField password;
    private JLabel userNameLabel, passwordLabel;
    private JPanel panel1,panel2,panel3,panel4,panel5;
    private JButton signIn,signUp;
    private ArrayList<User> userInfoArrayCopy;


    LoginPageGUI(ArrayList<User> userInfoArrayCopy){
        this.userInfoArrayCopy = userInfoArrayCopy;
        userInfoHandle = new UserInfoHandle();
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        panel5 = new JPanel();
        panel5.setPreferredSize(new Dimension(400,60));

        LoginPageFrame();
        userNameTextArea();
        panel3.add(userNameLabel);
        panel3.add(userName);
        passwordTextArea();
        panel4.add(passwordLabel);
        panel4.add(password);
        panel1.add(panel3);
        panel1.add(panel4);
        signInButton();
        panel2.add(signIn);
        signUpButton();
        panel2.add(signUp);

        add(panel5,BorderLayout.NORTH);
        add(panel1,BorderLayout.CENTER);
        add(panel2,BorderLayout.SOUTH);

        setSize(400,300);
        setVisible(true);
    }


    public void LoginPageFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login page");
        setLayout(new BorderLayout(0,30));
    }

    public void userNameTextArea(){
        userNameLabel = new JLabel("User Name");
        userName = new JTextArea();
        userName.setPreferredSize(new Dimension(200,userName.getPreferredSize().height));
    }

    public void passwordTextArea(){
        passwordLabel = new JLabel("Password");
        password = new JPasswordField();
        password.setPreferredSize(new Dimension(200,password.getPreferredSize().height));
    }

    public void signInButton(){
        signIn = new JButton("Sign In");
        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signInButtonActionPerformed();
            }
        });
    }

    public void signUpButton(){
        signUp = new JButton("Sign Up");
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpButtonActionPerformed();
            }
        });
    }

    public void signUpButtonActionPerformed(){
        boolean userFound = false;
        for(User user :userInfoArrayCopy){
            if(user.getUserName().equals(userName.getText())){
                JOptionPane.showMessageDialog(null,"User already exist","Alert",JOptionPane.WARNING_MESSAGE);
                userFound = true;
            }
        }
        if(!userFound){
            userClass = new User(userName.getText(),password.getPassword());
            userInfoHandle.getUserInfo().add(userClass);
            userInfoHandle.saveUserInfoTOFile();
            shoppingCart = new ShoppingCart();
            shoppingManager = new WestminsterShoppingManager();
            shoppingManager.loadFiles();
            shoppingCenterGUI = new ShoppingCenterGUI(shoppingManager,shoppingCart,userName.getText());
        }
    }

    public void signInButtonActionPerformed(){
        if(userInfoArrayCopy.isEmpty()){
            JOptionPane.showMessageDialog(null,"Invalid User name or Password","Alert",JOptionPane.WARNING_MESSAGE);
        }else{
            boolean found = false;
           for(User user : userInfoArrayCopy){
               if(Objects.equals(user.getUserName(), userName.getText()) && Arrays.equals(user.getPassword(), password.getPassword())){
                   shoppingCart = new ShoppingCart();
                   shoppingManager = new WestminsterShoppingManager();
                   shoppingManager.loadFiles();
                   shoppingCenterGUI = new ShoppingCenterGUI(shoppingManager,shoppingCart,userName.getText());
                   found = true;
               }
           }
           if(!found){
               JOptionPane.showMessageDialog(null,"Invalid User name or Password","Alert",JOptionPane.WARNING_MESSAGE);
           }
        }
    }
}
