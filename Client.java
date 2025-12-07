import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles the user interface (Client Class) and communicates with the server
 * Port Number: 6767
 * Host Name: localhost
 *
 * @author Saanvi Verma (verma279), Shalini Murthula (smurthul), Kunj Arora (arora271)
 * @version November 17, 2025
 */

public class Client implements ClientInterface {
    //Variables
    private boolean run;
    private Socket socket;
    protected BufferedReader reader;
    protected PrintWriter writer;
    protected Scanner scanner = new Scanner(System.in);
    protected String account;
    protected String accountID;
    private String host;
    private int port;
    private ArrayList<String> selectedSections = new ArrayList<>();
    private ArrayList<String> availableSections = new ArrayList<>();

    //GUI Variables
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel menuPanel;
    private JPanel reservationListPanel;
    private JPanel makeReservationPanel;
    private JPanel reservationPanel;
    private JPanel addConcertPanel;

    private String selectedDate;
    private String selectedTime;
    private String selectedConcert;
    private String selectedShowID;
    private JPanel seatGridPanel;
    private java.util.ArrayList<JToggleButton> seatButtons;
    private JPanel viewConcertsPanel;


    //Constructor
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.run = false;
        this.account = null;
        this.seatButtons = new ArrayList<>();
        this.accountID = null;
    }

    //Shows certain panel when called
    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    //Creates Login Panel
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Welcome to Concert Reservation System");
        titleLabel.setFont(new Font("Times", Font.BOLD, 22));
        loginPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton, gbc);
        gbc.gridx = 1;
        JButton registerButton = new JButton("Make an Account");
        loginPanel.add(registerButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel hoursLabel = new JLabel("Hours of Operation: Mon–Fri 9AM–11PM, Sat–Sun 10AM–2AM");
        hoursLabel.setFont(new Font("Times", Font.PLAIN, 14));
        loginPanel.add(hoursLabel, gbc);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginPanel, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                writer.write("login\n");
                writer.write(username + "\n");
                writer.write(password + "\n");
                writer.flush();
                String response = reader.readLine();
                if ("success".equals(response)) {
                    String userID = reader.readLine();
                    account = username;
                    accountID = userID;
                    usernameField.setText("");
                    passwordField.setText("");
                    //System.out.println(username + " (ID: " + userID + ")");
                    JOptionPane.showMessageDialog(loginPanel, "Login successful!");
                    //refreshDashboardPanel();
                    //showPanel("Menu");
                    showPanel("Menu");
                } else {
                    JOptionPane.showMessageDialog(loginPanel, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loginPanel, "Error communicating with server.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField ageField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField regularUsernameField = new JTextField();
            JPasswordField regularPasswordField = new JPasswordField();
            JPasswordField confirmPasswordField = new JPasswordField();

            boolean done = false;
            while (!done) {
                JPanel regPanel = new JPanel(new GridLayout(0, 2, 5, 5));
                regPanel.add(new JLabel("First Name:"));
                regPanel.add(firstNameField);
                regPanel.add(new JLabel("Last Name:"));
                regPanel.add(lastNameField);
                regPanel.add(new JLabel("Age:"));
                regPanel.add(ageField);
                regPanel.add(new JLabel("Email:"));
                regPanel.add(emailField);
                regPanel.add(new JLabel("Phone Number:"));
                regPanel.add(phoneField);
                regPanel.add(new JLabel("Username:"));
                regPanel.add(regularUsernameField);
                regPanel.add(new JLabel("Password:"));
                regPanel.add(regularPasswordField);
                regPanel.add(new JLabel("Confirm Password:"));
                regPanel.add(confirmPasswordField);

                int result = JOptionPane.showConfirmDialog(
                        loginPanel,
                        regPanel,
                        "Create Account",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String age = ageField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String username = regularUsernameField.getText().trim();
                String password = new String(regularPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                //Checks all variables
                if (firstName == null || lastName == null || age == null || email == null
                        || phone == null || username == null || password == null || confirmPassword == null) {
                    JOptionPane.showMessageDialog(loginPanel, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (firstName.isEmpty() || lastName.isEmpty() || age.isEmpty() || email.isEmpty()
                        || phone.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(loginPanel, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!email.contains("@") || !email.contains(".com")) {
                    JOptionPane.showMessageDialog(loginPanel, "Please enter a valid email.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (username.length() <= 5) {
                    JOptionPane.showMessageDialog(loginPanel, "Please enter a valid username.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (username.contains(",")) {
                    JOptionPane.showMessageDialog(loginPanel, "Please enter a valid username.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (password.length() <= 8) {
                    JOptionPane.showMessageDialog(loginPanel, "Please enter a valid password.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (password.contains(",")) {
                    JOptionPane.showMessageDialog(loginPanel, "Please enter a valid password.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (phone.length() != 10) {
                    JOptionPane.showMessageDialog(loginPanel, "Please enter a valid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                try {
                    Long p = Long.parseLong(phone);
                } catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(loginPanel, "Please enter a valid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                try {
                    int a = Integer.parseInt(age);
                } catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(loginPanel, "Please enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(loginPanel, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    writer.write("createAccount\n");
                    writer.write(firstName + "\n");
                    writer.write(lastName + "\n");
                    writer.write(age + "\n");
                    writer.write(username + "\n");
                    writer.write(password + "\n");
                    writer.write(email + "\n");
                    writer.write(phone + "\n");
                    writer.flush();

                    String response = reader.readLine();
                    if ("success".equals(response)) {
                        JOptionPane.showMessageDialog(loginPanel, "Account created successfully! You can now log in.");
                        done = true;
                    } else {
                        String error = reader.readLine();
                        JOptionPane.showMessageDialog(loginPanel, error, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(loginPanel, "Error communicating with server.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    //Books the seats that the user picked
    private void bookSeats() {
        ArrayList<String> selectedSeats = new ArrayList<>();
        for (JToggleButton btn : seatButtons) {
            if (btn.isSelected()) {
                String btnText = btn.getText();
                String seatID = btnText.substring(0, btnText.indexOf(" ("));
                selectedSeats.add(seatID);
            }
        }

        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(reservationPanel, "Please select at least one seat.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(reservationPanel, passwordField, "Enter your password to confirm booking:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(reservationPanel, "Password is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            writer.write("makeReservation\n");
            writer.write(account + "\n");
            writer.write(password + "\n");
            writer.flush();
            String passwordCheck = reader.readLine();
            if (passwordCheck.equals("invalid")) {
                JOptionPane.showMessageDialog(reservationPanel, "Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            writer.write(selectedShowID + "\n");
            writer.write(selectedSeats.size() + "\n");
            for (String seatID : selectedSeats) {
                writer.write(seatID + "\n");
            }
            writer.write(selectedDate + "\n");
            writer.flush();

            String response = reader.readLine();
            if (response.startsWith("Seat ")) {
                JOptionPane.showMessageDialog(reservationPanel, response, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double totalPrice = Double.parseDouble(response);
            int confirm = JOptionPane.showConfirmDialog(reservationPanel,
                    "Total Price: $" + totalPrice + "\n\nSeats: " + String.join(", ", selectedSeats) + "\n\nProceed with payment?",
                    "Confirm Booking",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                writer.write("pay\n");
                writer.flush();
                String bookingResult = reader.readLine();
                if ("success".equals(bookingResult)) {
                    String reservationID = reader.readLine();
                    JOptionPane.showMessageDialog(reservationPanel,
                            "Reservation successful!\n\nReservation ID: " + reservationID + "\nTotal: $" + totalPrice + "\nSeats: " + String.join(", ", selectedSeats),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    showPanel("Menu");
                } else {
                    JOptionPane.showMessageDialog(reservationPanel, "Reservation failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                writer.write("cancel\n");
                writer.flush();
                JOptionPane.showMessageDialog(reservationPanel, "Booking cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(reservationPanel, "Error booking reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Loads all seats that are avaliable for the concert
    private void loadSeats() {
        seatGridPanel.removeAll();
        seatButtons.clear();

        JLabel infoLabel = (JLabel)reservationPanel.getClientProperty("infoLabel");
        infoLabel.setText("Select seats for " + selectedConcert + " on " + selectedDate + " at " + selectedTime);
        try {
            writer.write("getAvailableSeats\n");
            writer.write(selectedShowID + "\n");
            writer.write(selectedDate + "\n");
            writer.flush();
            String countStr = reader.readLine();
            int numOfSeats = Integer.parseInt(countStr);
            if (numOfSeats == 0) {
                JLabel noSeatsLabel = new JLabel("No available seats for this concert.");
                noSeatsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                seatGridPanel.setLayout(new BorderLayout());
                seatGridPanel.add(noSeatsLabel, BorderLayout.CENTER);
                seatGridPanel.revalidate();
                seatGridPanel.repaint();
                return;
            }
            int cols = Math.min(10, numOfSeats);
            int rows = (int) Math.ceil((double) numOfSeats / cols);
            seatGridPanel.setLayout(new GridLayout(rows, cols, 5, 5));
            for (int i = 0; i < numOfSeats; i++) {
                String seatLine = reader.readLine();
                String[] seatParts = seatLine.split(",");
                String seatID = seatParts[0];
                double price = Double.parseDouble(seatParts[4]);
                JToggleButton seatBtn = new JToggleButton(seatID + " ($" + price + ")");
                seatGridPanel.add(seatBtn);
                seatButtons.add(seatBtn);
            }
            seatGridPanel.revalidate();
            seatGridPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(reservationPanel, "Error loading seats: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Creates reservation user list panel
    private void createReservationListPanel() {
        reservationListPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Your Reservations:");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        reservationListPanel.add(title, BorderLayout.NORTH);
        JTextArea reservationArea = new JTextArea();
        reservationArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reservationArea);
        reservationListPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton cancelReservationButton = new JButton("Cancel Reservation");
        JButton backButton = new JButton("Back to Main Menu");
        buttonPanel.add(cancelReservationButton);
        buttonPanel.add(backButton);
        reservationListPanel.add(buttonPanel, BorderLayout.SOUTH);
        backButton.addActionListener(e -> showPanel("Menu"));
        cancelReservationButton.addActionListener(e -> {
            String reservationID = JOptionPane.showInputDialog(reservationListPanel, "Enter Reservation ID to cancel:");
            if (reservationID != null && !reservationID.trim().isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(reservationListPanel,
                        "Are you sure you want to cancel reservation " + reservationID + "?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        writer.write("cancelReservation\n");
                        writer.write(reservationID.trim() + "\n");
                        writer.flush();
                        String response = reader.readLine();

                        if ("success".equals(response)) {
                            JOptionPane.showMessageDialog(reservationListPanel, "Your reservation has been cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            refreshReservationListPanel();
                        } else {
                            JOptionPane.showMessageDialog(reservationListPanel, "Cancellation failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(reservationListPanel, "Error cancelling reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        reservationListPanel.putClientProperty("reservationArea", reservationArea);
    }

    //Gets the latest data for reservations
    private void refreshReservationListPanel() {
        JTextArea reservationArea = (JTextArea)reservationListPanel.getClientProperty("reservationArea");

        if (reservationArea == null) {
            //System.out.println("null");
            JOptionPane.showMessageDialog(reservationListPanel, "Error: Reservation area not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        reservationArea.setText("Loading reservations...");
        //System.out.println(account + " (ID: " + accountID + ")");

        try {
            writer.write("getReservations\n");
            writer.write(accountID + "\n");
            writer.flush();

            String count = reader.readLine();
            //System.out.println(count);

            if (count == null || count.trim().isEmpty()) {
                reservationArea.setText("Error: No response from server.");
                return;
            }

            int num = Integer.parseInt(count.trim());
            //System.out.println(num);
            if (num == 0) {
                reservationArea.setText("You have no reservations.");
            } else {
                StringBuilder sb = new StringBuilder();
                if (num > 1) {
                    sb.append("You have ").append(num).append(" reservations:\n\n");
                } else {
                    sb.append("You have ").append(num).append(" reservation:\n\n");
                }
                for (int i = 0; i < num; i++) {
                    String reservationDetails = reader.readLine();
                    //System.out.println((i+1) + ": " + reservationDetails);

                    if (reservationDetails == null) {
                        sb.append("Error reading reservation ").append(i+1).append("\n\n");
                        continue;
                    }
                    String[] parts = reservationDetails.split(",");
                    //System.out.println(parts.length);
                    for (int j = 0; j < parts.length; j++) {
                        //System.out.println("  Part " + j + ": '" + parts[j] + "'");
                    }

                    if (parts.length >= 7) {
                        String reservationID = parts[0];
                        String showID = parts[2];
                        String seatIDs = parts[3].replace("|", ", ");
                        String date = parts[4];
                        String time = parts[5];
                        String totalPrice = parts[6];

                        sb.append("+++++++++++++++++++++++++++++++++++++++++\n");
                        sb.append("Reservation ID: ").append(reservationID).append("\n");
                        sb.append("Show ID: ").append(showID).append("\n");
                        sb.append("Date: ").append(date).append(" at ").append(time).append("\n");
                        sb.append("Seats: ").append(seatIDs).append("\n");
                        sb.append("Total Price: $").append(totalPrice).append("\n");
                        sb.append("+++++++++++++++++++++++++++++++++++++++++\n\n");
                    } else {
                        sb.append("Reservation ").append(i+1).append(":\n");
                        sb.append(reservationDetails).append("\n\n");
                    }
                }
                String finalText = sb.toString();
                //System.out.println(finalText.length());
                reservationArea.setText(finalText);
                reservationArea.revalidate();
                reservationArea.repaint();
            }
        } catch (Exception ex) {
            String error = "Error loading reservations: " + ex.getMessage();
            //System.out.println(error);
            ex.printStackTrace();
            reservationArea.setText(error);
        }
    }

    //Creates main menu panel
    private void createMenuPanel() {
        menuPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Concert Reservation System - Main Menu");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(50, 0, 5, 0));
        menuPanel.add(title, BorderLayout.NORTH);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel hoursLabel = new JLabel("Hours of Operation: Mon–Fri 9AM–11PM, Sat–Sun 10AM–2AM");
        hoursLabel.setFont(new Font("Times", Font.PLAIN, 14));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(hoursLabel);
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton makeReservationBtn = new JButton("1) Make Reservation");
        makeReservationBtn.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(makeReservationBtn, gbc);
        gbc.gridy++;
        JButton cancelReservationBtn = new JButton("2) Cancel Reservation");
        cancelReservationBtn.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(cancelReservationBtn, gbc);
        gbc.gridy++;
        JButton viewReservationsBtn = new JButton("3) View My Reservations");
        viewReservationsBtn.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(viewReservationsBtn, gbc);
        gbc.gridy++;
        JButton viewConcertsBtn = new JButton("4) View All Concerts");
        viewConcertsBtn.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(viewConcertsBtn, gbc);
        gbc.gridy++;
        JButton addConcertBtn = new JButton("5) Add a Concert");
        addConcertBtn.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(addConcertBtn, gbc);
        gbc.gridy++;
        JButton deleteAccountBtn = new JButton("6) Delete Account");
        deleteAccountBtn.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(deleteAccountBtn, gbc);
        gbc.gridy++;
        JButton logoutBtn = new JButton("7) Logout");
        logoutBtn.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(logoutBtn, gbc);
        menuPanel.add(buttonPanel, BorderLayout.CENTER);

        makeReservationBtn.addActionListener(e -> {
            refreshMakeReservationPanel();
            showPanel("MakeReservation");
        });

        cancelReservationBtn.addActionListener(e -> {
            String reservationID = JOptionPane.showInputDialog(menuPanel, "Enter Reservation ID to cancel:");
            if (reservationID != null && !reservationID.trim().isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(menuPanel,
                        "Are you sure you want to cancel reservation " + reservationID + "?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        writer.write("cancelReservation\n");
                        writer.write(reservationID.trim() + "\n");
                        writer.flush();
                        String response = reader.readLine();

                        if ("success".equals(response)) {
                            JOptionPane.showMessageDialog(menuPanel, "Your reservation has been cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(menuPanel, "Cancellation failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(menuPanel, "Error cancelling reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        viewReservationsBtn.addActionListener(e -> {
            refreshReservationListPanel();
            showPanel("ReservationList");
        });

        viewConcertsBtn.addActionListener(e -> {
            refreshViewConcertsPanel();
            showPanel("ViewConcerts");
        });

        addConcertBtn.addActionListener(e -> {
            showPanel("AddConcert");
        });

        deleteAccountBtn.addActionListener(e -> {
            JPanel delPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            delPanel.add(new JLabel("Username:"));
            delPanel.add(usernameField);
            delPanel.add(new JLabel("Password:"));
            delPanel.add(passwordField);
            int result = JOptionPane.showConfirmDialog(menuPanel, delPanel, "Delete Account - WARNING: This cannot be undone!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(menuPanel, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int finalConfirm = JOptionPane.showConfirmDialog(menuPanel,
                        "Are you absolutely sure? This action cannot be undone!",
                        "Final Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (finalConfirm != JOptionPane.YES_OPTION) {
                    return;
                }

                try {
                    writer.write("deleteAccount\n");
                    writer.write(accountID + "\n");
                    writer.write(password + "\n");
                    writer.write(username + "\n");
                    writer.flush();
                    String response = reader.readLine();
                    if (response != null && response.trim().equals("success")) {
                        JOptionPane.showMessageDialog(menuPanel, "Account deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        account = null;
                        accountID = null;
                        showPanel("Login");
                    } else {
                        JOptionPane.showMessageDialog(menuPanel, "Account deletion failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(menuPanel, "Error communicating with server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(menuPanel,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                account = null;
                accountID = null;
                JOptionPane.showMessageDialog(menuPanel, "Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE);
                showPanel("Login");
            }
        });
    }

    //Allows user to view all concerts
    private void createViewConcertsPanel() {
        viewConcertsPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("All Concerts:");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        viewConcertsPanel.add(title, BorderLayout.NORTH);
        JTextArea concertsArea = new JTextArea();
        concertsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(concertsArea);
        viewConcertsPanel.add(scrollPane, BorderLayout.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        viewConcertsPanel.add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(e -> showPanel("Menu"));
        viewConcertsPanel.putClientProperty("concertsArea", concertsArea);
    }

    //Gets latest concert data
    private void refreshViewConcertsPanel() {
        JTextArea concertsArea = (JTextArea)viewConcertsPanel.getClientProperty("concertsArea");

        if (concertsArea == null) {
            //System.out.println("Dnull");
            return;
        }

        concertsArea.setText("Loading concerts...");

        try {
            writer.write("getALlConcerts\n");
            writer.flush();
            int count = Integer.parseInt(reader.readLine());

            if (count == 0) {
                concertsArea.setText("No concerts available.");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Available Concerts:\n\n");

                for (int i = 0; i < count; i++) {
                    String line = reader.readLine();
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String name = parts[0];
                        String date = parts[1];
                        String time = parts[2];
                        String concertID = parts[3];
                        sb.append(concertID).append(". ").append(name)
                                .append(" on ").append(date).append(" at ").append(time).append("\n");
                    }
                }
                concertsArea.setText(sb.toString());
            }
        } catch (Exception ex) {
            concertsArea.setText("Error loading concerts: " + ex.getMessage());
        }
    }

    //Creates add concert panel
    private void createAddConcertPanel() {
        addConcertPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel title = new JLabel("Add New Concert");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addConcertPanel.add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        addConcertPanel.add(new JLabel("Concert Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        nameField.setName("concertNameField");
        addConcertPanel.add(nameField, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        addConcertPanel.add(new JLabel("Date (DD/MM/YYYY):"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(20);
        dateField.setName("concertDateField");
        addConcertPanel.add(dateField, gbc);
        gbc.gridy = 3;
        gbc.gridx = 0;
        addConcertPanel.add(new JLabel("Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        JTextField timeField = new JTextField(20);
        timeField.setName("concertTimeField");
        addConcertPanel.add(timeField, gbc);
        gbc.gridy = 4;
        gbc.gridx = 0;
        JButton createButton = new JButton("Create Concert");
        createButton.setName("addConcertButton");
        addConcertPanel.add(createButton, gbc);
        gbc.gridx = 1;
        JButton backButton = new JButton("Back to Main Menu");
        addConcertPanel.add(backButton, gbc);
        backButton.addActionListener(e -> showPanel("Menu"));

        createButton.addActionListener(e -> {
            JTextField nameFieldLocal = nameField;
            JTextField dateFieldLocal = dateField;
            JTextField timeFieldLocal = timeField;

            boolean done = false;
            while (!done) {
                JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
                form.add(new JLabel("Concert Name:"));
                form.add(nameFieldLocal);
                form.add(new JLabel("Date (DD/MM/YYYY):"));
                form.add(dateFieldLocal);
                form.add(new JLabel("Time (HH:MM):"));
                form.add(timeFieldLocal);

                int result = JOptionPane.showConfirmDialog(
                        addConcertPanel,
                        form,
                        "Create Concert",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }
                String name = nameFieldLocal.getText().trim();
                String date = dateFieldLocal.getText().trim();
                String time = timeFieldLocal.getText().trim();

                if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    JOptionPane.showMessageDialog(addConcertPanel, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (name.contains(" ")) {
                    JOptionPane.showMessageDialog(addConcertPanel, "Invalid name.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!confirmDate(date)) {
                    JOptionPane.showMessageDialog(addConcertPanel, "Invalid date format. Use DD/MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!time.contains(":")) {
                    JOptionPane.showMessageDialog(addConcertPanel, "Invalid time format. Use HH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                String[] parts = time.split(":");
                if (parts.length != 2) {
                    JOptionPane.showMessageDialog(addConcertPanel, "Invalid time format. Use HH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                int h = 0;
                int m = 0;
                try {
                    h = Integer.parseInt(parts[0]);
                    m = Integer.parseInt(parts[1]);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addConcertPanel, "Invalid time format. Use numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (h < 0 || h > 23 || m < 0 || m > 59) {
                    JOptionPane.showMessageDialog(addConcertPanel, "Invalid time. Hours 0-23, minutes 0-59.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                try {
                    writer.write("createConcert\n");
                    writer.write(name + "\n");
                    writer.write(date + "\n");
                    writer.write(time + "\n");
                    writer.flush();

                    String r = reader.readLine();
                    if (r.equals("success")) {
                        JOptionPane.showMessageDialog(addConcertPanel, "Concert Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        nameFieldLocal.setText("");
                        dateFieldLocal.setText("");
                        timeFieldLocal.setText("");
                        showPanel("Menu");
                        done = true;

                    } else {
                        JOptionPane.showMessageDialog(addConcertPanel, "Concert creation failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(addConcertPanel, "Error communicating with server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
            }
        });
    }

    //Allows multiple sections to get booked at once
    private void selectSectionsToBook() {
        availableSections.clear();
        for (JToggleButton btn : seatButtons) {
            if (btn.isEnabled()) {
                String seatID = btn.getText().substring(0, btn.getText().indexOf(" ("));
                String section = "";
                if (seatID != null && seatID.length() > 0) {
                    section = seatID.substring(0, 1);
                } else {
                    section = "";
                }

                boolean found = false;
                for (int i = 0; i < availableSections.size(); i++) {
                    if (availableSections.get(i).equals(section)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    availableSections.add(section);
                }
            }
        }

        JPanel sectionPanel = new JPanel(new GridLayout(0, 1));
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (int i = 0; i < availableSections.size(); i++) {
            JCheckBox cb = new JCheckBox("Section " + availableSections.get(i));
            checkBoxes.add(cb);
            sectionPanel.add(cb);
        }

        int result = JOptionPane.showConfirmDialog(
                reservationPanel,
                sectionPanel,
                "Select Sections to Book All Seats",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            selectedSections.clear();
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    selectedSections.add(availableSections.get(i));
                }
            }
            if (selectedSections.isEmpty()) {
                JOptionPane.showMessageDialog(reservationPanel, "Please select at least one section.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (JToggleButton btn : seatButtons) {
                if (btn.isEnabled()) {
                    String seatID = btn.getText().substring(0, btn.getText().indexOf(" ("));
                    String section = "";
                    if (seatID != null && seatID.length() > 0) {
                        section = seatID.substring(0, 1);
                    } else {
                        section = "";
                    }
                    for (int i = 0; i < selectedSections.size(); i++) {
                        if (selectedSections.get(i).equals(section)) {
                            btn.setSelected(true);
                            break;
                        }
                    }
                }
            }
            bookSeats();
        }
    }

    //Creates creates reservation panel
    private void createReservationPanel() {
        reservationPanel = new JPanel(new BorderLayout());
        JLabel infoLabel = new JLabel("Select seats for your reservation:");
        reservationPanel.add(infoLabel, BorderLayout.NORTH);
        seatGridPanel = new JPanel();
        reservationPanel.add(seatGridPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectSectionsButton = new JButton("Select Sections to Book");
        bottomPanel.add(selectSectionsButton);
        JButton bookButton = new JButton("Book Selected Seats");
        JButton backButton = new JButton("Back to Main Menu");
        bottomPanel.add(bookButton);
        bottomPanel.add(backButton);
        reservationPanel.add(bottomPanel, BorderLayout.SOUTH);
        reservationPanel.putClientProperty("infoLabel", infoLabel);
        bookButton.addActionListener(e -> bookSeats());
        backButton.addActionListener(e -> showPanel("Menu"));
        selectSectionsButton.addActionListener(e -> selectSectionsToBook());
    }

    //Creates the Make reservation panel
    private void createMakeReservationPanel() {
        makeReservationPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Available Concerts - Select One to Book");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        makeReservationPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel concertListPanel = new JPanel();
        concertListPanel.setLayout(new BoxLayout(concertListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(concertListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        makeReservationPanel.add(scrollPane, BorderLayout.CENTER);

        makeReservationPanel.putClientProperty("concertListPanel", concertListPanel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showPanel("Menu"));
        bottomPanel.add(backButton);
        makeReservationPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        /*makeReservationPanel = new JPanel(new BorderLayout());
        JPanel mainVBox = new JPanel();
        mainVBox.setLayout(new BoxLayout(mainVBox, BoxLayout.Y_AXIS));
        mainVBox.add(Box.createVerticalStrut(100));
        JLabel titleLabel = new JLabel("Select a Date, Time, and Concert");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainVBox.add(titleLabel);
        mainVBox.add(Box.createVerticalStrut(20));
        JPanel datePanel = new JPanel();
        datePanel.add(new JLabel("Date:"));
        JComboBox<String> dateBox = new JComboBox<>();
        datePanel.add(dateBox);
        datePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainVBox.add(datePanel);
        mainVBox.add(Box.createVerticalStrut(15));
        JPanel timePanel = new JPanel();
        timePanel.add(new JLabel("Time:"));
        JComboBox<String> timeBox = new JComboBox<>();
        timePanel.add(timeBox);
        timePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainVBox.add(timePanel);
        mainVBox.add(Box.createVerticalStrut(15));
        JPanel concertPanel = new JPanel();
        concertPanel.add(new JLabel("Concert:"));
        JComboBox<String> concertBox = new JComboBox<>();
        concertPanel.add(concertBox);
        concertPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainVBox.add(concertPanel);
        mainVBox.add(Box.createVerticalStrut(15));
        JButton nextButton = new JButton("See Available Seats");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainVBox.add(nextButton);
        mainVBox.add(Box.createVerticalStrut(25));

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(mainVBox);
        makeReservationPanel.add(wrapper, BorderLayout.CENTER);

        makeReservationPanel.putClientProperty("dateBox", dateBox);
        makeReservationPanel.putClientProperty("timeBox", timeBox);
        makeReservationPanel.putClientProperty("concertBox", concertBox);

        /*JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dashboardPanel,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                account = null;
                accountID = null;
                JOptionPane.showMessageDialog(dashboardPanel, "Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE);
                showPanel("Login");
            }
        });

        JButton deleteAccountBtn = new JButton("Delete Account");
        deleteAccountBtn.addActionListener(e -> {
            JPanel delPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            delPanel.add(new JLabel("Username:"));
            delPanel.add(usernameField);
            delPanel.add(new JLabel("Password:"));
            delPanel.add(passwordField);
            int result = JOptionPane.showConfirmDialog(dashboardPanel, delPanel, "Delete Account - WARNING: This cannot be undone!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(dashboardPanel, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int finalConfirm = JOptionPane.showConfirmDialog(dashboardPanel,
                        "Are you absolutely sure? This action cannot be undone!",
                        "Final Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (finalConfirm != JOptionPane.YES_OPTION) {
                    return;
                }

                try {
                    writer.write("deleteAccount\n");
                    writer.write(account + "\n");
                    writer.write(password + "\n");
                    writer.write(username + "\n");
                    writer.flush();
                    String response = reader.readLine();
                    if (response != null && response.trim().equals("success")) {
                        JOptionPane.showMessageDialog(dashboardPanel, "Account deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        account = null;
                        showPanel("Login");
                    } else {
                        JOptionPane.showMessageDialog(dashboardPanel, "Account deletion failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dashboardPanel, "Error communicating with server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });*/

        /*JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        //JButton viewReservationsButton = new JButton("My Reservations");
        JButton viewMenuButton = new JButton("Back to Main Menu");
        //JButton addConcertButton = new JButton("Add Concert");

        //buttonPanel.add(viewReservationsButton);
        buttonPanel.add(viewMenuButton, BorderLayout.CENTER);
        //buttonPanel.add(addConcertButton);

        mainVBox.add(buttonPanel);
        mainVBox.add(Box.createVerticalStrut(15));

        /*viewReservationsButton.addActionListener(e -> {
            refreshReservationListPanel();
            showPanel("ReservationList");
        });*/

        /*viewMenuButton.addActionListener(e -> {
            //refreshMenuPanel();
            showPanel("Menu");
        });

        /*addConcertButton.addActionListener(e -> {
            showPanel("AddConcert");
        });*/

        /*nextButton.addActionListener(e -> {
            String selectedDate = (String)dateBox.getSelectedItem();
            String selectedTime = (String)timeBox.getSelectedItem();
            String selectedConcert = (String)concertBox.getSelectedItem();

            java.util.List<String[]> concerts = (java.util.List<String[]>) makeReservationPanel.getClientProperty("concerts");
            String showID = null;
            for (String[] c : concerts) {
                if (c[0].equals(selectedConcert) && c[1].equals(selectedDate) && c[2].equals(selectedTime)) {
                    showID = c[3];
                    break;
                }
            }
            if (showID == null) {
                JOptionPane.showMessageDialog(makeReservationPanel, "Could not find concert information.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.selectedDate = selectedDate;
            this.selectedTime = selectedTime;
            this.selectedConcert = selectedConcert;
            this.selectedShowID = showID;
            loadSeats();
            showPanel("Reservation");
        });*/
    }

    //Gets latest data for make reservation panel
    private void refreshMakeReservationPanel() {
        JPanel concertListPanel = (JPanel) makeReservationPanel.getClientProperty("concertListPanel");

        if (concertListPanel == null) {
            return;
        }
        concertListPanel.removeAll();
        ArrayList<String[]> concerts = new ArrayList<>();

        try {
            writer.write("getALlConcerts\n");
            writer.flush();
            int count = Integer.parseInt(reader.readLine());

            for (int i = 0; i < count; i++) {
                String line = reader.readLine();
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    concerts.add(parts);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(makeReservationPanel, "Error loading concerts from server.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (concerts.isEmpty()) {
            JLabel noDataLabel = new JLabel("No concerts available at this time.");
            noDataLabel.setFont(noDataLabel.getFont().deriveFont(Font.PLAIN, 16f));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            concertListPanel.add(Box.createVerticalStrut(50));
            concertListPanel.add(noDataLabel);
        } else {
            for (String[] concert : concerts) {
                String name = concert[0];
                String date = concert[1];
                String time = concert[2];
                String showID = concert[3];

                JPanel concertPanel = new JPanel(new BorderLayout());
                concertPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                concertPanel.setMaximumSize(new Dimension(700, 80));

                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                JLabel nameL = new JLabel(name);
                nameL.setFont(nameL.getFont().deriveFont(Font.BOLD, 17f));
                JLabel dateTimeLabel = new JLabel("Date: " + date + "  |  Time: " + time);
                dateTimeLabel.setFont(dateTimeLabel.getFont().deriveFont(Font.PLAIN, 14f));
                JLabel idLabel = new JLabel("Concert ID: " + showID);
                idLabel.setFont(idLabel.getFont().deriveFont(Font.PLAIN, 12f));
                idLabel.setForeground(Color.GRAY);

                info.add(nameL);
                info.add(Box.createVerticalStrut(5));
                info.add(dateTimeLabel);
                info.add(Box.createVerticalStrut(3));
                info.add(idLabel);
                concertPanel.add(info, BorderLayout.CENTER);

                JButton bookButton = new JButton("Book Seats");
                bookButton.setPreferredSize(new Dimension(120, 40));
                bookButton.setFont(bookButton.getFont().deriveFont(Font.BOLD, 14f));
                bookButton.addActionListener(e -> {
                    this.selectedDate = date;
                    this.selectedTime = time;
                    this.selectedConcert = name;
                    this.selectedShowID = showID;
                    loadSeats();
                    showPanel("Reservation");
                });
                concertPanel.add(bookButton, BorderLayout.EAST);
                concertListPanel.add(concertPanel);
                concertListPanel.add(Box.createVerticalStrut(10));
            }
        }

        concertListPanel.revalidate();
        concertListPanel.repaint();
    }

    public static void main(String[] args) {
        //Sets the client to localhost and port 6767
        Client client = new Client("localhost", 6767);
        if (client.connect()) {
            /*Thread thread = new Thread(client);
            thread.start();*/
            client.start();
        } else {
            System.out.println("Could not connect.");
        }
    }

    //Tries to connect with the server
    public boolean connect() {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //Starts the program
    public void start() {
        mainFrame = new JFrame("Concert Reservation System");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
                System.exit(0);
            }
        });

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createMakeReservationPanel();
        createReservationPanel();
        createReservationListPanel();
        createMenuPanel();
        createAddConcertPanel();
        createViewConcertsPanel();

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(makeReservationPanel, "MakeReservation");
        mainPanel.add(reservationPanel, "Reservation");
        mainPanel.add(reservationListPanel, "ReservationList");
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(addConcertPanel, "AddConcert");
        mainPanel.add(viewConcertsPanel, "ViewConcerts");

        mainFrame.add(mainPanel);
        showPanel("Login");
        mainFrame.setVisible(true);
    }

    public void disconnect() {
        //Disconnects from the server
        try {
            if (writer != null) {
                writer.write("disconnect\n");
                writer.flush();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Disconnected.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Helper method to confirm date
    private boolean confirmDate(String date) {
        try {
            String d = "";
            String m = "";
            String y = "";

            d = date.substring(0, date.indexOf("/"));
            m = date.substring(date.indexOf("/") + 1, date.lastIndexOf("/"));
            y = date.substring(date.lastIndexOf("/") + 1);

            int day = Integer.parseInt(d);
            int month = Integer.parseInt(m);

            if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && y.length() == 4) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    //Getter methods
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getAccount() {
        return account;
    }

    public boolean isRunning() {
        return run;
    }

    //Setter methods
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
