package lv.rvt.roles;

public class User {
    private String name;
    private String email;
    private boolean admin;

    public User(String name, String email, boolean admin) {
        this.name = name;
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


    public void buyBook(){}

    


}
