package com.petcare.ui;

import com.petcare.dao.CustomerDAO;
import com.petcare.dao.DAOFactory;
import com.petcare.model.Customer;
import com.petcare.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerForm extends JPanel implements Refreshable {

    private final CustomerDAO customerDAO = DAOFactory.getCustomerDAO();
    private final NonEditableTableModel tableModel = new NonEditableTableModel(
            new Object[] { "ID", "First Name", "Last Name", "Phone", "Email", "Address" }, 0);

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField searchField;
    private JTable table;

    private Integer selectedId = null;

    public CustomerForm() {
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        add(root, BorderLayout.CENTER);

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        root.add(form, BorderLayout.NORTH);

        JLabel firstNameLabel = new JLabel("First Name:");
        GridBagConstraints gbc_firstNameLabel = new GridBagConstraints();
        gbc_firstNameLabel.anchor = GridBagConstraints.WEST;
        gbc_firstNameLabel.insets = new Insets(5, 5, 5, 5);
        gbc_firstNameLabel.gridx = 0;
        gbc_firstNameLabel.gridy = 0;
        form.add(firstNameLabel, gbc_firstNameLabel);

        firstNameField = new JTextField(15);
        GridBagConstraints gbc_firstNameField = new GridBagConstraints();
        gbc_firstNameField.fill = GridBagConstraints.HORIZONTAL;
        gbc_firstNameField.insets = new Insets(5, 5, 5, 0);
        gbc_firstNameField.gridx = 1;
        gbc_firstNameField.gridy = 0;
        form.add(firstNameField, gbc_firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        GridBagConstraints gbc_lastNameLabel = new GridBagConstraints();
        gbc_lastNameLabel.anchor = GridBagConstraints.WEST;
        gbc_lastNameLabel.insets = new Insets(5, 5, 5, 5);
        gbc_lastNameLabel.gridx = 0;
        gbc_lastNameLabel.gridy = 1;
        form.add(lastNameLabel, gbc_lastNameLabel);

        lastNameField = new JTextField(15);
        GridBagConstraints gbc_lastNameField = new GridBagConstraints();
        gbc_lastNameField.fill = GridBagConstraints.HORIZONTAL;
        gbc_lastNameField.insets = new Insets(5, 5, 5, 0);
        gbc_lastNameField.gridx = 1;
        gbc_lastNameField.gridy = 1;
        form.add(lastNameField, gbc_lastNameField);

        JLabel phoneLabel = new JLabel("Phone (07XXXXXXXX):");
        GridBagConstraints gbc_phoneLabel = new GridBagConstraints();
        gbc_phoneLabel.anchor = GridBagConstraints.WEST;
        gbc_phoneLabel.insets = new Insets(5, 5, 5, 5);
        gbc_phoneLabel.gridx = 0;
        gbc_phoneLabel.gridy = 2;
        form.add(phoneLabel, gbc_phoneLabel);

        phoneField = new JTextField(15);
        GridBagConstraints gbc_phoneField = new GridBagConstraints();
        gbc_phoneField.fill = GridBagConstraints.HORIZONTAL;
        gbc_phoneField.insets = new Insets(5, 5, 5, 0);
        gbc_phoneField.gridx = 1;
        gbc_phoneField.gridy = 2;
        form.add(phoneField, gbc_phoneField);

        JLabel emailLabel = new JLabel("Email:");
        GridBagConstraints gbc_emailLabel = new GridBagConstraints();
        gbc_emailLabel.anchor = GridBagConstraints.WEST;
        gbc_emailLabel.insets = new Insets(5, 5, 5, 5);
        gbc_emailLabel.gridx = 0;
        gbc_emailLabel.gridy = 3;
        form.add(emailLabel, gbc_emailLabel);

        emailField = new JTextField(15);
        GridBagConstraints gbc_emailField = new GridBagConstraints();
        gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
        gbc_emailField.insets = new Insets(5, 5, 5, 0);
        gbc_emailField.gridx = 1;
        gbc_emailField.gridy = 3;
        form.add(emailField, gbc_emailField);

        JLabel addressLabel = new JLabel("Address:");
        GridBagConstraints gbc_addressLabel = new GridBagConstraints();
        gbc_addressLabel.anchor = GridBagConstraints.WEST;
        gbc_addressLabel.insets = new Insets(5, 5, 5, 5);
        gbc_addressLabel.gridx = 0;
        gbc_addressLabel.gridy = 4;
        form.add(addressLabel, gbc_addressLabel);

        addressField = new JTextField(15);
        GridBagConstraints gbc_addressField = new GridBagConstraints();
        gbc_addressField.fill = GridBagConstraints.HORIZONTAL;
        gbc_addressField.insets = new Insets(5, 5, 5, 0);
        gbc_addressField.gridx = 1;
        gbc_addressField.gridy = 4;
        form.add(addressField, gbc_addressField);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
        GridBagConstraints gbc_buttons = new GridBagConstraints();
        gbc_buttons.gridwidth = 2;
        gbc_buttons.insets = new Insets(5, 5, 0, 5);
        gbc_buttons.gridx = 0;
        gbc_buttons.gridy = 5;
        form.add(buttons, gbc_buttons);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> addCustomer());
        buttons.add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> updateCustomer());
        buttons.add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> deleteCustomer());
        buttons.add(deleteBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearForm());
        buttons.add(clearBtn);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(5, 5));
        root.add(tablePanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tablePanel.add(searchPanel, BorderLayout.NORTH);

        JLabel searchLabel = new JLabel("Search:");
        searchPanel.add(searchLabel);

        searchField = new JTextField(15);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadTable(searchField.getText().trim()));
        searchPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Show All");
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            loadTable(null);
        });
        searchPanel.add(refreshBtn);

        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                fillFormFromRow(table.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        loadTable(null);
    }

    private void loadTable(String keyword) {
        try {
            tableModel.setRowCount(0);
            List<Customer> customers = ValidationUtil.isEmpty(keyword)
                    ? customerDAO.getAll() : customerDAO.search(keyword);
            for (Customer c : customers) {
                tableModel.addRow(new Object[] { c.getCustomerId(), c.getFirstName(), c.getLastName(),
                        c.getPhone(), c.getEmail(), c.getAddress() });
            }
        } catch (SQLException e) {
            showDbError(e);
        }
    }

    private void fillFormFromRow(int row) {
        selectedId = (Integer) tableModel.getValueAt(row, 0);
        firstNameField.setText((String) tableModel.getValueAt(row, 1));
        lastNameField.setText((String) tableModel.getValueAt(row, 2));
        phoneField.setText((String) tableModel.getValueAt(row, 3));
        emailField.setText((String) tableModel.getValueAt(row, 4));
        addressField.setText((String) tableModel.getValueAt(row, 5));
    }

    private boolean validateForm() {
        if (ValidationUtil.isEmpty(firstNameField.getText()) || ValidationUtil.isEmpty(lastNameField.getText())) {
            JOptionPane.showMessageDialog(this, "First name and last name are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!ValidationUtil.isValidPhone(phoneField.getText())) {
            JOptionPane.showMessageDialog(this, "Enter a valid 10-digit phone number starting with 0.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Enter a valid email address, or leave it blank.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void addCustomer() {
        if (!validateForm()) return;
        try {
            Customer c = new Customer();
            c.setFirstName(firstNameField.getText().trim());
            c.setLastName(lastNameField.getText().trim());
            c.setPhone(phoneField.getText().trim());
            c.setEmail(emailField.getText().trim());
            c.setAddress(addressField.getText().trim());
            customerDAO.add(c);
            JOptionPane.showMessageDialog(this, "Customer added successfully.");
            clearForm();
            loadTable(null);
        } catch (SQLException e) {
            showDbError(e);
        }
    }

    private void updateCustomer() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Select a customer from the table first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) return;
        try {
            Customer c = new Customer();
            c.setCustomerId(selectedId);
            c.setFirstName(firstNameField.getText().trim());
            c.setLastName(lastNameField.getText().trim());
            c.setPhone(phoneField.getText().trim());
            c.setEmail(emailField.getText().trim());
            c.setAddress(addressField.getText().trim());
            customerDAO.update(c);
            JOptionPane.showMessageDialog(this, "Customer updated successfully.");
            clearForm();
            loadTable(null);
        } catch (SQLException e) {
            showDbError(e);
        }
    }

    private void deleteCustomer() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Select a customer from the table first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this customer and all their pets/appointments?", "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            customerDAO.delete(selectedId);
            clearForm();
            loadTable(null);
        } catch (SQLException e) {
            showDbError(e);
        }
    }

    private void clearForm() {
        selectedId = null;
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        table.clearSelection();
    }

    private void showDbError(SQLException e) {
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onShow() {
        if (searchField != null) {
            searchField.setText("");
        }
        loadTable(null);
    }
}
