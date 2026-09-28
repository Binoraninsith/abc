package com.petcare.ui;

import com.petcare.dao.DAOFactory;
import com.petcare.model.User;
import com.petcare.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton loginBtn;

    public LoginForm() {
        setTitle("Pet Care Centre - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(637, 754);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(panel);
                                        
        JLabel titleLabel = new JLabel("Pet Care Centre");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        GridBagConstraints gbc_titleLabel = new GridBagConstraints();
        gbc_titleLabel.insets = new Insets(5, 5, 15, 5);
        gbc_titleLabel.gridwidth = 3;
        gbc_titleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_titleLabel.gridx = 3;
        gbc_titleLabel.gridy = 0;
        panel.add(titleLabel, gbc_titleLabel);
        titleLabel.setForeground(Theme.ACCENT);
        titleLabel.setFont(Theme.font(Font.BOLD, 24f));
                                        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\binor\\OneDrive\\Pictures\\PetCareCentre\\images\\Pet-Care-Center_c.png"));
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 3;
        gbc_lblNewLabel.gridy = 2;
        panel.add(lblNewLabel, gbc_lblNewLabel);
                                                                
      JLabel usernameLabel = new JLabel("Username:");
      usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
      GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
      gbc_usernameLabel.insets = new Insets(5, 5, 5, 5);
      gbc_usernameLabel.gridx = 0;
      gbc_usernameLabel.gridy = 5;
      panel.add(usernameLabel, gbc_usernameLabel);
                                                        
      usernameField = new JTextField(16);
      GridBagConstraints gbc_usernameField = new GridBagConstraints();
      gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
      gbc_usernameField.insets = new Insets(5, 5, 5, 5);
      gbc_usernameField.gridx = 3;
      gbc_usernameField.gridy = 5;
      panel.add(usernameField, gbc_usernameField);
                        
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
    GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
    gbc_passwordLabel.insets = new Insets(5, 5, 5, 5);
    gbc_passwordLabel.gridx = 0;
    gbc_passwordLabel.gridy = 7;
    panel.add(passwordLabel, gbc_passwordLabel);
                        
    passwordField = new JPasswordField(16);
    GridBagConstraints gbc_passwordField = new GridBagConstraints();
     gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
     gbc_passwordField.insets = new Insets(5, 5, 5, 5);
     gbc_passwordField.gridx = 3;
     gbc_passwordField.gridy = 7;
     panel.add(passwordField, gbc_passwordField);
                
      JLabel roleLabel = new JLabel("Login as:");
      roleLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
     GridBagConstraints gbc_roleLabel = new GridBagConstraints();
      gbc_roleLabel.anchor = GridBagConstraints.WEST;
      gbc_roleLabel.insets = new Insets(5, 5, 5, 5);
      gbc_roleLabel.gridx = 0;
      gbc_roleLabel.gridy = 9;
      panel.add(roleLabel, gbc_roleLabel);
        
                loginBtn = new JButton("Login");
                loginBtn.setFont(new Font("Tahoma", Font.BOLD, 15));
                loginBtn.addActionListener(e -> attemptLogin());
                
                        roleCombo = new JComboBox<>(new String[] { "ADMIN", "STAFF" });
                        GridBagConstraints gbc_roleCombo = new GridBagConstraints();
                        gbc_roleCombo.fill = GridBagConstraints.HORIZONTAL;
                        gbc_roleCombo.insets = new Insets(5, 5, 5, 5);
                        gbc_roleCombo.gridx = 3;
                        gbc_roleCombo.gridy = 9;
                        panel.add(roleCombo, gbc_roleCombo);
       GridBagConstraints gbc_loginBtn = new GridBagConstraints();
       gbc_loginBtn.gridwidth = 2;
       gbc_loginBtn.insets = new Insets(15, 5, 5, 5);
       gbc_loginBtn.fill = GridBagConstraints.HORIZONTAL;
       gbc_loginBtn.gridx = 2;
       gbc_loginBtn.gridy = 13;
       panel.add(loginBtn, gbc_loginBtn);
                
       getRootPane().setDefaultButton(loginBtn);

        JLabel hintLabel = new JLabel("Default: admin / admin123 (ADMIN), reception / staff123 (STAFF)");
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        GridBagConstraints gbc_hintLabel = new GridBagConstraints();
        gbc_hintLabel.gridwidth = 4;
        gbc_hintLabel.insets = new Insets(10, 5, 0, 0);
        gbc_hintLabel.gridx = 3;
        gbc_hintLabel.gridy = 17;
        panel.add(hintLabel, gbc_hintLabel);

        Theme.style(panel);
        hintLabel.setForeground(Theme.MUTED);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = (String) roleCombo.getSelectedItem();

        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = DAOFactory.getUserDAO().authenticate(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.",
                        "Login failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (user.getRole() == null || !user.getRole().equalsIgnoreCase(selectedRole)) {
                JOptionPane.showMessageDialog(this,
                        "This account is registered as " + user.getRole()
                                + ", not " + selectedRole + ".\nPlease select the correct role.",
                        "Role mismatch", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new MainFrame(user).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not connect to the database.\nCheck MySQL is running and DBConnection settings are correct.\n\n"
                            + ex.getMessage(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
