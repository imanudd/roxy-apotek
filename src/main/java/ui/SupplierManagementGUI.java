package ui;

import repository.supplierRepo;
import model.suppliers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.time.LocalDateTime;

public class SupplierManagementGUI extends JFrame {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField idField;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton loadButton;
    private supplierRepo repo;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private int userId = 1;

    public SupplierManagementGUI() {
        repo = new supplierRepo();
        setTitle("Supplier Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(100, 149, 180)); // Steel Blue

        nameField = new JTextField();
        addressField = new JTextField();
        phoneField = new JTextField();
        idField = new JTextField();
        idField.setEditable(false); // ID is auto-filled and not editable

        inputPanel.add(new JLabel("Nama Supplier:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Alamat:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("No. Telepon:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("ID Supplier (Auto-filled):"));
        inputPanel.add(idField);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        createButton = new JButton("Create Supplier");
        updateButton = new JButton("Update Supplier");
        deleteButton = new JButton("Delete Supplier");
        loadButton = new JButton("Load Suppliers");
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);

        // Table Model and Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Alamat", "Telepon"}, 0);
        supplierTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(supplierTable);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load suppliers button action
        loadButton.addActionListener(e -> loadSuppliers());

        // Create supplier with validation
        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama supplier tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (SupplierNameExists(name, -1)) {
                JOptionPane.showMessageDialog(this, "Nama supplier sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            suppliers newSupplier = new suppliers(
                    name, address, phone,
                    LocalDateTime.now(), userId,
                    LocalDateTime.now(), userId
            );

            boolean created = repo.createSupplier(newSupplier);
            JOptionPane.showMessageDialog(this, "CREATE: " + (created ? "Berhasil" : "Gagal"));
            if(created) {
                clearInputFields();
                loadSuppliers();
            }
        });

        // Update supplier with validation
        updateButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih supplier terlebih dahulu dari tabel.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = Integer.parseInt(idText);

            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama supplier tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (SupplierNameExists(name, id)) {
                JOptionPane.showMessageDialog(this, "Nama supplier sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            suppliers updateSupplier = new suppliers(
                    name, address, phone,
                    null, 0,
                    LocalDateTime.now(), userId
            );
            updateSupplier.setId(id);

            boolean updated = repo.updateSupplier(updateSupplier);
            JOptionPane.showMessageDialog(this, "UPDATE: " + (updated ? "Berhasil" : "Gagal"));
            if(updated) {
                clearInputFields();
                loadSuppliers();
            }
        });

        // Delete supplier
        deleteButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih supplier terlebih dahulu dari tabel.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = Integer.parseInt(idText);
            suppliers delSupplier = new suppliers(null, null, null, null, 0, null, 0);
            delSupplier.setId(id);

            boolean deleted = repo.deleteSuplier(delSupplier);
            JOptionPane.showMessageDialog(this, "DELETE: " + (deleted ? "Berhasil" : "Gagal"));
            if(deleted) {
                clearInputFields();
                loadSuppliers();
            }
        });

        // Automatically fill text fields when table row selected
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && supplierTable.getSelectedRow() != -1) {
                int selectedRow = supplierTable.getSelectedRow();
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                addressField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        setVisible(true);
        loadSuppliers(); // Load initially
    }

    // Clears text fields except idField
    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
    }

    private void loadSuppliers() {
        tableModel.setRowCount(0);
        supplierRepo repo = new supplierRepo(); // pastikan objek repo tersedia

        List<suppliers> supplierList = repo.getList(); // memanggil method getList()

        for (suppliers s : supplierList) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getSupplierName(),
                s.getAddress(),
                s.getPhone()
            });
        }
    }

    /**
     * Check if supplier name exists in DB for create or update.
     * For update, exclude current record by id.
     *
     * @param name supplier name to check
     * @param excludeId supplier id to exclude from check (-1 for create)
     * @return true if name exists, false otherwise
     */
    private boolean SupplierNameExists(String name, int excludeId) {
        supplierRepo repo = new supplierRepo();
        
        if (repo.isSupplierNameExists(name, excludeId)) {
            JOptionPane.showMessageDialog(this, "Nama supplier sudah ada.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
