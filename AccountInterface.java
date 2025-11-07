/**
 * AccountInterface - Interface for Account class
 *
 * @author Saanvi Verma
 * @version November 5, 2025
 */

public interface AccountInterface {
    String getUserName();
    String getPassword();
    String getFirstName();
    String getLastName();
    int getAge();
    void setUserName(String userName);
    String getPhoneNumber();
    void setPhoneNumber(String phoneNumber);
    String getEmail();
    String getID();
    void setPassword(String password);
    void setEmail(String email);
    String createID();
    String toString();
    boolean equals(Object obj);
}
