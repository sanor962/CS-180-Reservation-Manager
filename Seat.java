import java.io.Serializable;

/**
 * Represents a single seat with different variables
 *
 * @author Kunj Arora (arora271) and Saanvi Verma (verma279)
 * @version November 7, 2025
 */

public class Seat implements SeatInterface, Serializable {
    private String seatID;
    private String row;
    private boolean isAvailable;
    private int number;
    private double price;

    //Constructor
    public Seat(String seatID, String row, int number, double price) {
        this.seatID = seatID;
        this.row = row;
        this.number = number;
        this.price = price;
        this.isAvailable = true;
    }

    //Constructor with text file line
    public Seat(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException();
        }
        this.seatID = parts[0];
        this.row = parts[1];
        this.isAvailable = Boolean.parseBoolean(parts[2]);
        this.number = Integer.parseInt(parts[3]);
        this.price = Double.parseDouble(parts[4]);
    }

    //Setter methods
    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(boolean avaliable) {
        this.isAvailable = avaliable;
    }

    //Getter methods
    public String getSeatID() {
        return seatID;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double getPrice() {
        return price;
    }

    public String getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    //To String
    public String toString() {
        return "Seat ID: " + seatID + "\n Available: " + isAvailable + "\n Price: $" + price;
    }

    //Writing in the file
    public String writingInFile() {
        return getSeatID() + "," + getRow() + "," + isAvailable() + "," + getNumber() + "," + getPrice();
    }
}

