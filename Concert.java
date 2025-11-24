/**
 * Concert class - basic information for each concert
 *
 * @author Saanvi Verma (verma279) and Shalini Murthula (smurthul)
 * @version November 21, 2025
 */
public class Concert implements ConcertInterface {
    private String name;
    private String date;
    private String time;
    private int ID;

    public Concert(String name, String date, String time, int num) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.ID = num;
    }

    public Concert(String line) {
        String[] partOfConcert = line.split(",");
        if (partOfConcert.length < 5) {
            throw new IllegalArgumentException();
        }
        this.name = partOfConcert[1];
        this.date = partOfConcert[2];
        this.time = partOfConcert[3];
        this.ID = Integer.parseInt(partOfConcert[4]);
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String toString() {
        return "Name: " + getName() + "\nDate: " + getDate() + "\nTime: " + getTime() + "\nID: " + getID();
    }

    public String writingInFile() {
        return getName() + "," + getDate() + "," + getTime() + "," + getID();
    }
}
