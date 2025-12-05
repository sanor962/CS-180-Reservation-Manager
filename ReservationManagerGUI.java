import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReservationManagerGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels
    LoginPanel loginPanel;
    DashboardPanel dashboardPanel;
    public ReservationPanel reservationPanel;
    PaymentPanel paymentPanel;

    // Client connection
    public Client client;

    public ReservationManagerGUI() {
        setTitle("Reservation Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Connect to server
        client = new Client("localhost", 6767);
        if (!client.connect()) {
            JOptionPane.showMessageDialog(this, "Could not connect to server. Please start the server and try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels with client
        loginPanel = new LoginPanel(this, client);
        dashboardPanel = new DashboardPanel(this, client);
        reservationPanel = new ReservationPanel(this, client);
        paymentPanel = new PaymentPanel(this, client);

        // Add panels to mainPanel
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(reservationPanel, "Reservation");
        mainPanel.add(paymentPanel, "Payment");

        add(mainPanel);
        showPanel("Login");
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReservationManagerGUI().setVisible(true);
        });
    }
}

// --- Login Panel ---
class LoginPanel extends JPanel {
    public LoginPanel(ReservationManagerGUI gui, Client client) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton loginButton = new JButton("Login");
        add(loginButton, gbc);
        gbc.gridx = 1;
        JButton registerButton = new JButton("Register");
        add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                client.writer.write("login\n");
                client.writer.write(username + "\n");
                client.writer.write(password + "\n");
                client.writer.flush();
                String response = client.reader.readLine();
                if ("success".equals(response)) {
                    client.account = username; // Store username as account identifier
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    gui.showPanel("Dashboard");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error communicating with server.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            JPanel regPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField ageField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField regUsernameField = new JTextField();
            JPasswordField regPasswordField = new JPasswordField();
            JPasswordField confirmPasswordField = new JPasswordField();

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
            regPanel.add(regUsernameField);
            regPanel.add(new JLabel("Password:"));
            regPanel.add(regPasswordField);
            regPanel.add(new JLabel("Confirm Password:"));
            regPanel.add(confirmPasswordField);

            int result = JOptionPane.showConfirmDialog(this, regPanel, "Create Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String ageStr = ageField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String username = regUsernameField.getText().trim();
                String password = new String(regPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (firstName.isEmpty() || lastName.isEmpty() || ageStr.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    client.writer.write("createAccount\n");
                    client.writer.write(firstName + "\n");
                    client.writer.write(lastName + "\n");
                    client.writer.write(ageStr + "\n");
                    client.writer.write(username + "\n");
                    client.writer.write(password + "\n");
                    client.writer.write(email + "\n");
                    client.writer.write(phone + "\n");
                    client.writer.flush();
                    String response = client.reader.readLine();
                    if ("success".equals(response)) {
                        JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
                    } else {
                        String errorMsg = client.reader.readLine();
                        JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error communicating with server.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

// --- Dashboard Panel ---
class DashboardPanel extends JPanel {
    public DashboardPanel(ReservationManagerGUI gui, Client client) {
        setLayout(new BorderLayout());

        // Top panel for delete button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton deleteAccountButton = new JButton("Delete Account");
        topPanel.add(deleteAccountButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Main VBox for selectors
        JPanel mainVBox = new JPanel();
        mainVBox.setLayout(new BoxLayout(mainVBox, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Select a Date, Time, and Concert");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainVBox.add(titleLabel);
        mainVBox.add(Box.createVerticalStrut(15));

        // Get concerts from server
        java.util.List<String[]> concerts = new java.util.ArrayList<>();
        java.util.Set<String> dates = new java.util.LinkedHashSet<>();
        try {
            client.writer.write("getALlConcerts\n");
            client.writer.flush();
            int count = Integer.parseInt(client.reader.readLine());
            for (int i = 0; i < count; i++) {
                String line = client.reader.readLine();
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    concerts.add(parts);
                    dates.add(parts[1]);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading concerts from server.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Date:"));
        JComboBox<String> dateBox = new JComboBox<>(dates.toArray(new String[0]));
        datePanel.add(dateBox);
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainVBox.add(datePanel);
        mainVBox.add(Box.createVerticalStrut(10));

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Time:"));
        JComboBox<String> timeBox = new JComboBox<>();
        timePanel.add(timeBox);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainVBox.add(timePanel);
        mainVBox.add(Box.createVerticalStrut(10));

        JPanel concertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        concertPanel.add(new JLabel("Concert:"));
        JComboBox<String> concertBox = new JComboBox<>();
        concertPanel.add(concertBox);
        concertPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainVBox.add(concertPanel);
        mainVBox.add(Box.createVerticalStrut(15));

        JButton nextButton = new JButton("See Available Seats");
        nextButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainVBox.add(nextButton);

        add(mainVBox, BorderLayout.CENTER);

        // Update times and concerts when date changes
        dateBox.addActionListener(e -> {
            String selectedDate = (String)dateBox.getSelectedItem();
            java.util.Set<String> times = new java.util.LinkedHashSet<>();
            for (String[] c : concerts) {
                if (c[1].equals(selectedDate)) {
                    times.add(c[2]);
                }
            }
            timeBox.setModel(new DefaultComboBoxModel<>(times.toArray(new String[0])));
            if (timeBox.getItemCount() > 0) {
                timeBox.setSelectedIndex(0);
            }
            String selectedTime = (String)timeBox.getSelectedItem();
            java.util.List<String> concertNames = new java.util.ArrayList<>();
            for (String[] c : concerts) {
                if (c[1].equals(selectedDate) && c[2].equals(selectedTime)) {
                    concertNames.add(c[0]);
                }
            }
            if (concertNames.isEmpty()) {
                for (String[] c : concerts) {
                    if (c[1].equals(selectedDate)) {
                        concertNames.add(c[0]);
                    }
                }
            }
            concertBox.setModel(new DefaultComboBoxModel<>(concertNames.toArray(new String[0])));
            if (concertBox.getItemCount() > 0) {
                concertBox.setSelectedIndex(0);
            }
        });

        timeBox.addActionListener(e -> {
            String selectedDate = (String)dateBox.getSelectedItem();
            String selectedTime = (String)timeBox.getSelectedItem();
            java.util.List<String> concertNames = new java.util.ArrayList<>();
            for (String[] c : concerts) {
                if (c[1].equals(selectedDate) && c[2].equals(selectedTime)) {
                    concertNames.add(c[0]);
                }
            }
            if (concertNames.isEmpty()) {
                for (String[] c : concerts) {
                    if (c[1].equals(selectedDate)) {
                        concertNames.add(c[0]);
                    }
                }
            }
            concertBox.setModel(new DefaultComboBoxModel<>(concertNames.toArray(new String[0])));
            if (concertBox.getItemCount() > 0) {
                concertBox.setSelectedIndex(0);
            }
        });

        nextButton.addActionListener(e -> {
            String selectedDate = (String)dateBox.getSelectedItem();
            String selectedTime = (String)timeBox.getSelectedItem();
            String selectedConcert = (String)concertBox.getSelectedItem();
            
            // Find the showID for the selected concert
            String showID = null;
            for (String[] c : concerts) {
                if (c[0].equals(selectedConcert) && c[1].equals(selectedDate) && c[2].equals(selectedTime)) {
                    showID = c[3]; // ID is the 4th field
                    break;
                }
            }
            
            if (showID == null) {
                JOptionPane.showMessageDialog(this, "Could not find concert information.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            gui.reservationPanel.setReservationInfo(selectedDate, selectedTime, selectedConcert, showID);
            gui.showPanel("Reservation");
        });
        if (dateBox.getItemCount() > 0) {
            dateBox.setSelectedIndex(0);
            for (ActionListener al : dateBox.getActionListeners()) {
                al.actionPerformed(new java.awt.event.ActionEvent(dateBox, ActionEvent.ACTION_PERFORMED, null));
            }
        }

        // Delete Account button logic
        deleteAccountButton.addActionListener(e -> {
            JPanel delPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            delPanel.add(new JLabel("Username:"));
            delPanel.add(usernameField);
            delPanel.add(new JLabel("Password:"));
            delPanel.add(passwordField);
            int result = JOptionPane.showConfirmDialog(this, delPanel, "Delete Account - WARNING: This cannot be undone!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    // Use deleteAccountByCredentials - server will fetch the account ID automatically
                    client.writer.write("deleteAccountByCredentials\n");
                    client.writer.write(username + "\n");
                    client.writer.write(password + "\n");
                    client.writer.flush();
                    String response = client.reader.readLine();
                    if (response != null && response.trim().equals("success")) {
                        JOptionPane.showMessageDialog(this, "Account deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        client.account = null; // Log out
                        gui.showPanel("Login");
                    } else {
                        JOptionPane.showMessageDialog(this, "Account deletion failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error communicating with server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

// --- Reservation Panel ---
class ReservationPanel extends JPanel {
    private String selectedDate = null;
    private String selectedTime = null;
    private String selectedConcert = null;
    private String selectedShowID = null;
    private JPanel seatGridPanel;
    private java.util.List<JToggleButton> seatButtons;
    private JLabel infoLabel;
    private JButton bookButton;
    private ReservationManagerGUI gui;
    private Client client;

    public ReservationPanel(ReservationManagerGUI gui, Client client) {
        this.gui = gui;
        this.client = client;
        setLayout(new BorderLayout());
        
        infoLabel = new JLabel("Select seats for your reservation:");
        add(infoLabel, BorderLayout.NORTH);

        seatGridPanel = new JPanel();
        add(seatGridPanel, BorderLayout.CENTER);

        bookButton = new JButton("Book Selected Seats");
        add(bookButton, BorderLayout.SOUTH);

        seatButtons = new java.util.ArrayList<>();

        bookButton.addActionListener(e -> bookSeats());
    }

    public void setReservationInfo(String date, String time, String concertName, String showID) {
        this.selectedDate = date;
        this.selectedTime = time;
        this.selectedConcert = concertName;
        this.selectedShowID = showID;
        loadSeats();
    }

    private void loadSeats() {
        // Clear previous seats
        seatGridPanel.removeAll();
        seatButtons.clear();

        infoLabel.setText("Select seats for " + selectedConcert + " on " + selectedDate + " at " + selectedTime);

        try {
            // Use getAvailableSeats to get available seats
            client.writer.write("getAvailableSeats\n");
            client.writer.write(selectedShowID + "\n");
            client.writer.write(selectedDate + "\n");
            client.writer.flush();
            
            String countStr = client.reader.readLine();
            int numOfSeats = Integer.parseInt(countStr);

            // Handle case where there are no seats
            if (numOfSeats == 0) {
                JLabel noSeatsLabel = new JLabel("No available seats for this concert.");
                noSeatsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                seatGridPanel.setLayout(new BorderLayout());
                seatGridPanel.add(noSeatsLabel, BorderLayout.CENTER);
                seatGridPanel.revalidate();
                seatGridPanel.repaint();
                return;
            }

            // Dynamically determine grid size based on number of seats
            int cols = Math.min(10, numOfSeats);
            int rows = (int) Math.ceil((double) numOfSeats / cols);
            seatGridPanel.setLayout(new GridLayout(rows, cols, 5, 5));

            for (int i = 0; i < numOfSeats; i++) {
                String seatLine = client.reader.readLine();
                // Parse seat line: seatID,row,isAvailable,number,price
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
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading seats: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bookSeats() {
        java.util.List<String> selectedSeats = new java.util.ArrayList<>();
        for (JToggleButton btn : seatButtons) {
            if (btn.isSelected()) {
                // Extract just the seat ID (before the " ($")
                String btnText = btn.getText();
                String seatID = btnText.substring(0, btnText.indexOf(" ("));
                selectedSeats.add(seatID);
            }
        }
        
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get user's password for reservation
        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, passwordField, "Enter your password to confirm booking:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            client.writer.write("makeReservation\n");
            client.writer.write(client.account + "\n");
            client.writer.write(password + "\n");
            client.writer.write(selectedShowID + "\n");
            client.writer.write(selectedSeats.size() + "\n");
            for (String seatID : selectedSeats) {
                client.writer.write(seatID + "\n");
            }
            client.writer.write(selectedDate + "\n");
            client.writer.flush();

            String response = client.reader.readLine();
            
            // Check if it's a price or error message
            if (response.startsWith("Seat ")) {
                JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totalPrice = Double.parseDouble(response);
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Total Price: $" + totalPrice + "\n\nSeats: " + String.join(", ", selectedSeats) + "\n\nProceed with payment?", 
                "Confirm Booking", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                client.writer.write("pay\n");
                client.writer.flush();
                
                String bookingResult = client.reader.readLine();
                if ("success".equals(bookingResult)) {
                    String reservationID = client.reader.readLine();
                    JOptionPane.showMessageDialog(this, 
                        "Reservation successful!\n\nReservation ID: " + reservationID + "\nTotal: $" + totalPrice + "\nSeats: " + String.join(", ", selectedSeats), 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    gui.showPanel("Dashboard");
                } else {
                    JOptionPane.showMessageDialog(this, "Reservation failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                client.writer.write("cancel\n");
                client.writer.flush();
                JOptionPane.showMessageDialog(this, "Booking cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error booking reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// --- Payment Panel ---
class PaymentPanel extends JPanel {
    public PaymentPanel(ReservationManagerGUI gui, Client client) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Payment Panel (Stub)");
        add(label, BorderLayout.CENTER);
        // TODO: Add payment summary and processing
    }
}
