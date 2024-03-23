

public class User {
    private String userName;
    private char[] password;




    public User(String userName, char[] password) {
        this.userName = userName;
        this.password = password;

    }

    public String getUserName() {
        return userName;
    }

    public char[] getPassword() {
        return password;
    }


}
