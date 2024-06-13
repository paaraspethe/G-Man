import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GymMembershipSystem extends JFrame {
    private Connection connection;
    private JTextField nameField, addressField, phoneField, monthsField;
    private JButton addButton, deleteButton, displayButton, updateButton;
    private JTextArea displayText;

    public GymMembershipSystem() {
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        nameField = new JTextField(20);
        addressField = new JTextField(20);
        phoneField = new JTextField(20);
        monthsField = new JTextField(20);

        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMember();
            }
        });

        displayButton = new JButton("Display");
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayMembers();
            }
        });

        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMember();
            }
        });

        displayText = new JTextArea(10, 30);
        displayText.setEditable(false);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Number of Months:"), gbc);

        gbc.gridx = 1;
        inputPanel.add(monthsField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(updateButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JScrollPane(displayText), BorderLayout.NORTH);
    }

    public void addMember() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        int months = Integer.parseInt(monthsField.getText());

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        String query = "INSERT INTO members (name, address, phone, months) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, phone);
            statement.setInt(4, months);
            statement.executeUpdate();

            displayText.setText("Member added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add member: " + e.getMessage());
        }
    }

    public void deleteMember() {
        String name = nameField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to delete.");
            return;
        }

        String query = "DELETE FROM members WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            int deletedCount = statement.executeUpdate();

            if (deletedCount > 0) {
                displayText.setText("Member deleted successfully.");
            } else {
                displayText.setText("No matching member found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete member: " + e.getMessage());
        }
    }

    public void displayMembers() {
        String query = "SELECT * FROM members";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                int months = resultSet.getInt("months");

                sb.append("Name: ").append(name).append("\n");
                sb.append("Address: ").append(address).append("\n");
                sb.append("Phone: ").append(phone).append("\n");
                sb.append("Months: ").append(months).append("\n");
                sb.append("---------------------------\n");
            }

            if (sb.length() > 0) {
                displayText.setText(sb.toString());
            } else {
                displayText.setText("No members found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to display members: " + e.getMessage());
        }
    }

    public void updateMember() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        int months = Integer.parseInt(monthsField.getText());

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to update.");
            return;
        }

        String query = "UPDATE members SET address = ?, phone = ?, months = ? WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, address);
            statement.setString(2, phone);
            statement.setInt(3, months);
            statement.setString(4, name);
            int updatedCount = statement.executeUpdate();

            if (updatedCount > 0) {
                displayText.setText("Member updated successfully.");
            } else {
                displayText.setText("No matching member found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update member: " + e.getMessage());
        }
    }

    public void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/gym_database";
        String username = "root";
        String password = "paras06";

        try {
            connection = DriverManager.getConnection(url, username, password);
            displayText.setText("Connected to database.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GymMembershipSystem system = new GymMembershipSystem();
                system.connectToDatabase();
                system.setVisible(true);
            }
        });
    }
}
