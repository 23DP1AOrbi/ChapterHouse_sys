package lv.rvt.user;

public class User {
    private String name;
    private String email;
    private static String currentUser;
    private boolean admin;

    public User(String name, String email, boolean admin) {
        this.name = name;
        User.currentUser = name;
        this.email = email;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String toCSV() {
        return this.name + "," + this.email + ",false";
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String username) {
        currentUser = username;
    }

}
