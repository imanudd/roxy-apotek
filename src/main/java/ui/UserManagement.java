package ui;

import java.sql.Connection;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import usecase.userUc;
import model.user;
import repository.usersRepo;
import javax.swing.JPasswordField;

public class UserManagement extends javax.swing.JPanel {

        private final userUc uc;
        private DefaultTableModel tableModel;
        private int currentPage = 1;
        private int limit = 15;
        private javax.swing.JButton btnPrev;
        private javax.swing.JButton btnNext;
        private javax.swing.JLabel labelPage;

        public UserManagement(Connection conn) {
                initPaginationComponents();
                initComponents();

                // Inisialisasi usecase
                uc = new userUc(new usersRepo(conn));

                // Buat model tabel dengan kolom sesuai data user
                tableModel = new DefaultTableModel(new Object[] { "ID", "Nama", "Email", "Phone", "Status" }, 0);
                jTable1.setModel(tableModel);

                // Load data user ke tabel saat panel di-load
                loadUsers();

                // Listener saat pilih baris tabel, untuk menampilkan data ke textfield
                jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                                if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                                        int selectedRow = jTable1.getSelectedRow();
                                        textFieldId.setText(tableModel.getValueAt(selectedRow, 0).toString()); // id
                                        textFieldName.setText(tableModel.getValueAt(selectedRow, 1).toString()); // nama
                                        textFieldEmail.setText(tableModel.getValueAt(selectedRow, 2).toString()); // email
                                        textFieldPhone.setText(tableModel.getValueAt(selectedRow, 3).toString()); // phone
                                        // Password tidak ditampilkan dari tabel karena biasanya tidak disimpan di tabel
                                        textFieldPassword.setText(""); // kosongkan password saat pilih user
                                }
                        }
                });

                textFieldId.setEditable(false);
                textFieldId.setVisible(false);

                helper.TableUtils.styleTable(jTable1);
        }

        private void initPaginationComponents() {
                btnPrev = new javax.swing.JButton("< Previous");
                btnNext = new javax.swing.JButton("Next >");
                labelPage = new javax.swing.JLabel("Page: 1");

                btnPrev.addActionListener(e -> {
                        if (currentPage > 1) {
                                currentPage--;
                                loadUsers();
                        }
                });

                btnNext.addActionListener(e -> {
                        currentPage++;
                        loadUsers();
                });
        }

        private void loadUsers() {
                tableModel.setRowCount(0); // bersihkan dulu tabel

                String search = textFieldId.getText().trim(); // search bisa disesuaikan
                int offset = (currentPage - 1) * limit;
                List<user> userList = uc.listUser(search, 0, limit, offset); // ambil list user dari usecase

                if (labelPage != null) {
                        labelPage.setText("Page: " + currentPage);
                }

                for (user s : userList) {
                        tableModel.addRow(new Object[] {
                                        s.getId(),
                                        s.getUserName(),
                                        s.getEmail(),
                                        s.getPhoneNumber(),
                                        s.isStatus() ? "Aktif" : "Nonaktif"
                        });
                }
        }

        private void clearInputFields() {
                textFieldId.setText("");
                textFieldName.setText("");
                textFieldEmail.setText("");
                textFieldPhone.setText("");
                textFieldPassword.setText("");
        }

        private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {
                String name = textFieldName.getText().trim();
                String email = textFieldEmail.getText().trim();
                String phone = textFieldPhone.getText().trim();
                String password = new String(textFieldPassword.getPassword()).trim();

                if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nama user tidak boleh kosong.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                user newUser = new user();
                newUser.setUserName(name);
                newUser.setEmail(email);
                newUser.setPhoneNumber(phone);
                // newUser.setStatus(true); // default aktif
                newUser.setPassword(password);

                boolean success = uc.register(newUser);
                JOptionPane.showMessageDialog(this, "CREATE: " + (success ? "Berhasil" : "Gagal"));
                if (success) {
                        clearInputFields();
                        loadUsers();
                }
        }

        private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
                String idStr = textFieldId.getText().trim();
                String name = textFieldName.getText().trim();
                String email = textFieldEmail.getText().trim();
                String phone = textFieldPhone.getText().trim();
                String password = new String(textFieldPassword.getPassword()).trim();

                if (idStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "ID user tidak boleh kosong.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nama user tidak boleh kosong.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                user updateUser = new user();
                updateUser.setId(Integer.parseInt(idStr));
                updateUser.setUserName(name);
                updateUser.setEmail(email);
                updateUser.setPhoneNumber(phone);
                updateUser.setPassword(password);

                boolean success = uc.updateUser(Integer.parseInt(idStr), updateUser);
                JOptionPane.showMessageDialog(this, "UPDATE: " + (success ? "Berhasil" : "Gagal"));
                if (success) {
                        clearInputFields();
                        loadUsers();
                }
        }

        private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
                String idStr = textFieldId.getText().trim();

                if (idStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "ID user tidak boleh kosong.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                boolean success = uc.softDeleteUser(Integer.parseInt(idStr));
                JOptionPane.showMessageDialog(this, "DELETE: " + (success ? "Berhasil" : "Gagal"));
                if (success) {
                        clearInputFields();
                        loadUsers();
                }
        }

        private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {
                loadUsers();
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * Generated by GUI editor, jangan dihapus!
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                jLabelName = new javax.swing.JLabel();
                jLabelEmail = new javax.swing.JLabel();
                textFieldName = new javax.swing.JTextField();
                textFieldId = new javax.swing.JTextField();
                btnCreate = new javax.swing.JButton();
                btnUpdate = new javax.swing.JButton();
                btnDelete = new javax.swing.JButton();
                jLabelHeader = new javax.swing.JLabel();
                jLabelPhone = new javax.swing.JLabel();
                textFieldEmail = new javax.swing.JTextField();
                jLabelPassword = new javax.swing.JLabel();
                textFieldPhone = new javax.swing.JTextField();
                textFieldPassword = new JPasswordField();
                btnRefresh = new javax.swing.JButton();

                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null }
                                },
                                new String[] {
                                                "Title 1", "Title 2", "Title 3", "Title 4"
                                }));
                jScrollPane1.setViewportView(jTable1);

                setBackground(new java.awt.Color(255, 255, 204));
                setToolTipText("");
                setPreferredSize(new java.awt.Dimension(900, 750));

                jLabelName.setFont(new java.awt.Font("Gill Sans", 0, 14)); // NOI18N
                jLabelName.setForeground(new java.awt.Color(102, 102, 102));
                jLabelName.setText("Nama User");

                jLabelEmail.setFont(new java.awt.Font("Gill Sans", 0, 14)); // NOI18N
                jLabelEmail.setForeground(new java.awt.Color(102, 102, 102));
                jLabelEmail.setText("Email");

                btnCreate.setBackground(new java.awt.Color(255, 153, 153));
                btnCreate.setForeground(new java.awt.Color(255, 255, 255));
                btnCreate.setText("Create");
                btnCreate.setBorder(null);
                btnCreate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnCreateActionPerformed(evt);
                        }
                });

                btnUpdate.setBackground(new java.awt.Color(255, 153, 153));
                btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
                btnUpdate.setText("Update");
                btnUpdate.setBorder(null);
                btnUpdate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnUpdateActionPerformed(evt);
                        }
                });

                btnDelete.setBackground(new java.awt.Color(255, 153, 153));
                btnDelete.setForeground(new java.awt.Color(255, 255, 255));
                btnDelete.setText("Delete");
                btnDelete.setBorder(null);
                btnDelete.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnDeleteActionPerformed(evt);
                        }
                });

                jLabelHeader.setFont(new java.awt.Font("Gill Sans", 0, 36)); // NOI18N
                jLabelHeader.setForeground(new java.awt.Color(102, 102, 102));
                jLabelHeader.setText("USER MANAGEMENT");

                jLabelPhone.setFont(new java.awt.Font("Gill Sans", 0, 14)); // NOI18N
                jLabelPhone.setForeground(new java.awt.Color(102, 102, 102));
                jLabelPhone.setText("Phone");

                jLabelPassword.setFont(new java.awt.Font("Gill Sans", 0, 14)); // NOI18N
                jLabelPassword.setForeground(new java.awt.Color(102, 102, 102));
                jLabelPassword.setText("Password");

                btnRefresh.setBackground(new java.awt.Color(255, 153, 153));
                btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
                btnRefresh.setText("Load Data");
                btnRefresh.setBorder(null);
                btnRefresh.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnRefreshActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(40, 40, 40)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jLabelHeader,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                451,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addContainerGap(
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE))
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                layout.createSequentialGroup()
                                                                                                                .addGroup(layout.createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                .addComponent(jScrollPane1,
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                820,
                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                                .addComponent(btnPrev)
                                                                                                                                                .addPreferredGap(
                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                .addComponent(labelPage)
                                                                                                                                                .addPreferredGap(
                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                .addComponent(btnNext)
                                                                                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                layout.createSequentialGroup()
                                                                                                                                                                .addGroup(layout.createParallelGroup(
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                .addComponent(jLabelName)
                                                                                                                                                                                .addComponent(jLabelEmail)
                                                                                                                                                                                .addComponent(jLabelPhone)
                                                                                                                                                                                .addComponent(jLabelPassword))
                                                                                                                                                                .addGap(40, 40, 40)
                                                                                                                                                                .addGroup(layout.createParallelGroup(
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                                                                                .addComponent(btnCreate,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                135,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                .addGap(18, 18, 18)
                                                                                                                                                                                                .addComponent(btnUpdate,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                135,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                .addGap(18, 18, 18)
                                                                                                                                                                                                .addComponent(btnDelete,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                135,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                .addGap(18, 18, 18)
                                                                                                                                                                                                .addComponent(btnRefresh,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                135,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                                                                .addGroup(layout.createParallelGroup(
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                                                                false)
                                                                                                                                                                                                .addComponent(textFieldName,
                                                                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                                                400,
                                                                                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                                                                                .addComponent(textFieldEmail)
                                                                                                                                                                                                .addComponent(textFieldPhone)
                                                                                                                                                                                                .addComponent(textFieldPassword)
                                                                                                                                                                                                .addComponent(textFieldId,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                46,
                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                                                                                .addGap(40, 40, 40)))));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(40, 40, 40)
                                                                .addComponent(jLabelHeader)
                                                                .addGap(30, 30, 30)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabelName)
                                                                                .addComponent(textFieldName,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabelEmail)
                                                                                .addComponent(textFieldEmail,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabelPhone)
                                                                                .addComponent(textFieldPhone,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabelPassword)
                                                                                .addComponent(textFieldPassword,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(textFieldId,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btnCreate,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                23,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnUpdate,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                23,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnDelete,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                23,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnRefresh,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                23,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(30, 30, 30)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                400,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btnPrev)
                                                                                .addComponent(labelPage)
                                                                                .addComponent(btnNext))
                                                                .addContainerGap(40, Short.MAX_VALUE)));
        }// </editor-fold>

        // Variables declaration
        private javax.swing.JButton btnCreate;
        private javax.swing.JButton btnUpdate;
        private javax.swing.JButton btnDelete;
        private javax.swing.JButton btnRefresh;
        private javax.swing.JLabel jLabelName;
        private javax.swing.JLabel jLabelEmail;
        private javax.swing.JLabel jLabelPhone;
        private javax.swing.JLabel jLabelPassword;
        private javax.swing.JLabel jLabelHeader;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JTextField textFieldName;
        private javax.swing.JTextField textFieldEmail;
        private javax.swing.JTextField textFieldPhone;
        private javax.swing.JPasswordField textFieldPassword;
        private javax.swing.JTextField textFieldId;
}
