/**
 * Account - class that manages the information in the account
 *
 * @author Saanvi Verma (verma279) and Shalini Murthula (smurthul)
 * @version November 5, 2025
 */
public class Account implements AccountInterface {
    private String firstName;
    private String lastName;
    private int age;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private String id;

    //Constructor
    public Account(String firstName, String lastName, int age, String userName,
                   String password, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        //createID();
    }

    //Constructor that takes in from the line from the file
    public Account(String line) {
        String[] partOfAccount = line.split(",");
        this.firstName = partOfAccount[0];
        this.lastName = partOfAccount[1];
        this.age = Integer.parseInt(partOfAccount[2]);
        this.userName = partOfAccount[3];
        this.password = partOfAccount[4];
        this.email = partOfAccount[5];
        this.phoneNumber = partOfAccount[6];
        this.setID(partOfAccount[7]);
        //createID();
    }

    //Getter Methods
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    //Setter Methods
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setID(String id1) {
        this.id = id1;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Creating a specialized ID per account
    public String createID() {
        String firstName1 = firstName.toUpperCase();
        String result;
        if (firstName1.length() == 0) {
            result = "A";
        } else if (firstName1.length() >= 2) {
            result = firstName1.charAt(1) + "" + firstName1.charAt((firstName1.length() - 2));
        } else {
            result = firstName1.charAt(0) + "";
        }
        String lastName1 = lastName.toUpperCase();
        if (lastName1.length() == 0) {
            result = "B" + result;
        } else if (lastName1.length() >= 2) {
            result = (lastName1.charAt(0) + "" + lastName1.charAt(2)).concat(result);
        } else {
            result = lastName1.charAt(0) + "";
        }
        result = result.concat("-" + phoneNumber.substring(6, 10));
        result = result.replaceAll("6", "");
        result = result.replaceAll("7", "");
        setID(result);
        return result;
    }

    //To write the account in the file
    public String writingInFile() {
        return getFirstName() + "," + getLastName() + "," + getAge() + "," +
                getUserName() + "," + getPassword() + "," + getEmail() + "," +
                getPhoneNumber() + "," + getID();
    }

    //To String method
    public String toString() {
        return "First Name: " + getFirstName() + "\nLast Name: " + getLastName() +
                "\nAge: " + getAge() + "\nUsername: " + getUserName() + "\nPassword: " +
                getPassword() + "\nID: " + getID() + "\nEmail: " + getEmail() +
                "\nPhone Number: " + getPhoneNumber();
    }
}
