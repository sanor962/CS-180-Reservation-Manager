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

    public ReservationManagerGUI() {
        setTitle("Reservation Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        loginPanel = new LoginPanel(this);
        dashboardPanel = new DashboardPanel(this);
        reservationPanel = new ReservationPanel(this);
        paymentPanel = new PaymentPanel(this);

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
    public LoginPanel(ReservationManagerGUI gui) {
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

        // Example action listeners (stub)
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean found = false;
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("accounts.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Account acc = new Account(line);
                        if (acc.getUserName().equals(username) && acc.getPassword().equals(password)) {
                            found = true;
                            break;
                        }
                    } catch (Exception ex) {
                        // skip invalid lines
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading accounts file.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (found) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                gui.showPanel("Dashboard");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
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

                // Basic validation
                if (firstName.isEmpty() || lastName.isEmpty() || ageStr.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int age;
                try {
                    age = Integer.parseInt(ageStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Account newAcc = new Account(firstName, lastName, age, username, password, email, phone);
                    newAcc.setID(newAcc.createID());
                    String accountLine = newAcc.writingInFile();
                    // Check for duplicate username
                    boolean duplicate = false;
                    try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("accounts.txt"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            try {
                                Account acc = new Account(line);
                                if (acc.getUserName().equals(username)) {
                                    duplicate = true;
                                    break;
                                }
                            } catch (Exception ex) {}
                        }
                    }
                    if (duplicate) {
                        JOptionPane.showMessageDialog(this, "Username already exists. Choose a different one.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Append to accounts.txt
                    try (java.io.FileWriter fw = new java.io.FileWriter("accounts.txt", true)) {
                        fw.write(accountLine + System.lineSeparator());
                    }
                    JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Account creation failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    }

// --- Dashboard Panel ---
class DashboardPanel extends JPanel {
    public DashboardPanel(ReservationManagerGUI gui) {
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel welcomeLabel = new JLabel("Welcome! Select a date, time, and concert:");
        topPanel.add(welcomeLabel);

        // Parse concert.txt
        java.util.List<String[]> concerts = new java.util.ArrayList<>();
        java.util.Set<String> dates = new java.util.LinkedHashSet<>();
        java.io.File concertFile = new java.io.File("concert.txt");
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(concertFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    concerts.add(parts);
                    dates.add(parts[1]);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error reading concert.txt", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JComboBox<String> dateBox = new JComboBox<>(dates.toArray(new String[0]));
        topPanel.add(new JLabel("Date:"));
        topPanel.add(dateBox);

        JComboBox<String> timeBox = new JComboBox<>();
        topPanel.add(new JLabel("Time:"));
        topPanel.add(timeBox);

        JComboBox<String> concertBox = new JComboBox<>();
        topPanel.add(new JLabel("Concert:"));
        topPanel.add(concertBox);

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
            // Set timeBox to first available time
            if (timeBox.getItemCount() > 0) {
                timeBox.setSelectedIndex(0);
            }
            // Update concerts for first time
            String selectedTime = (String)timeBox.getSelectedItem();
            java.util.List<String> concertNames = new java.util.ArrayList<>();
            for (String[] c : concerts) {
                if (c[1].equals(selectedDate) && c[2].equals(selectedTime)) {
                    concertNames.add(c[0]);
                }
            }
            concertBox.setModel(new DefaultComboBoxModel<>(concertNames.toArray(new String[0])));
            if (concertBox.getItemCount() > 0) {
                concertBox.setSelectedIndex(0);
            }
        });

        // Update concerts when time changes
        timeBox.addActionListener(e -> {
            String selectedDate = (String)dateBox.getSelectedItem();
            String selectedTime = (String)timeBox.getSelectedItem();
            java.util.List<String> concertNames = new java.util.ArrayList<>();
            for (String[] c : concerts) {
                if (c[1].equals(selectedDate) && c[2].equals(selectedTime)) {
                    concertNames.add(c[0]);
                }
            }
            concertBox.setModel(new DefaultComboBoxModel<>(concertNames.toArray(new String[0])));
            if (concertBox.getItemCount() > 0) {
                concertBox.setSelectedIndex(0);
            }
        });

        JButton nextButton = new JButton("See Available Seats");
        topPanel.add(nextButton);

        add(topPanel, BorderLayout.NORTH);

        nextButton.addActionListener(e -> {
            String selectedDate = (String)dateBox.getSelectedItem();
            String selectedTime = (String)timeBox.getSelectedItem();
            String selectedConcert = (String)concertBox.getSelectedItem();
            gui.reservationPanel.setReservationInfo(selectedDate, selectedTime, selectedConcert);
            gui.showPanel("Reservation");
        });
        // Initialize time and concert boxes
        if (dateBox.getItemCount() > 0) {
            dateBox.setSelectedIndex(0);
            // Trigger action to populate time and concert boxes
            for (ActionListener al : dateBox.getActionListeners()) {
                al.actionPerformed(new java.awt.event.ActionEvent(dateBox, ActionEvent.ACTION_PERFORMED, null));
            }
        }
    }
}

// --- Reservation Panel ---
class ReservationPanel extends JPanel {
    private String selectedDate = null;
    private String selectedTime = null;
    private String selectedConcert = null;

    public ReservationPanel(ReservationManagerGUI gui) {
        setLayout(new BorderLayout());
        JLabel infoLabel = new JLabel("Select seats for your reservation:");
        add(infoLabel, BorderLayout.NORTH);

        // TODO: Use backend SeatingChart for selectedConcert
        JPanel seatGridPanel = new JPanel(new GridLayout(5, 10, 5, 5)); // Example: 5 rows x 10 seats
        java.util.List<JToggleButton> seatButtons = new java.util.ArrayList<>();
        for (int i = 0; i < 50; i++) {
            JToggleButton seatBtn = new JToggleButton("Seat " + (i+1));
            seatGridPanel.add(seatBtn);
            seatButtons.add(seatBtn);
        }
        add(seatGridPanel, BorderLayout.CENTER);

        JButton bookButton = new JButton("Book Selected Seats");
        add(bookButton, BorderLayout.SOUTH);

        bookButton.addActionListener(e -> {
            java.util.List<String> selectedSeats = new java.util.ArrayList<>();
            for (JToggleButton btn : seatButtons) {
                if (btn.isSelected()) {
                    selectedSeats.add(btn.getText());
                }
            }
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // TODO: Use backend methods to create reservation and update seat availability
            JOptionPane.showMessageDialog(this, "Reservation booked for " + selectedDate + " at " + selectedTime + " for concert: " + selectedConcert + " seats: " + selectedSeats);
            gui.showPanel("Dashboard");
        });
    }

    public void setReservationInfo(String date, String time, String concert) {
        this.selectedDate = date;
        this.selectedTime = time;
        this.selectedConcert = concert;
    }
}

// --- Payment Panel ---
class PaymentPanel extends JPanel {
    public PaymentPanel(ReservationManagerGUI gui) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Payment Panel (Stub)");
        add(label, BorderLayout.CENTER);
        // TODO: Add payment summary and processing
    }
}
