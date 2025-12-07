/**
 * ConcertInterface - Interface for Concert class
 *
 * @author Saanvi Verma (verma279)
 * @version November 22, 2025
 */

public interface ConcertInterface {
    String getName();
    String getDate();
    String getTime();
    int getID();
    void setName(String name);
    void setDate(String date);
    void setTime(String time);
    void setID(int iD);
    String toString();
    String writingInFile();
}
