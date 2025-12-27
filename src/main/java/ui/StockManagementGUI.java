package ui;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.LogStock;
import model.brands;
import model.stock;
import model.items;
import usecase.supplierUc;
import model.suppliers;
import repository.LogStockRepo;
import repository.stocksRepo;
import repository.supplierRepo;
import repository.brandsRepo;
import repository.itemsRepo;
import usecase.BrandUC;
import usecase.itemsUc;
import usecase.logStockUc;
import usecase.stockUc;

public class StockManagementGUI extends javax.swing.JPanel {

    private final supplierUc supplierUc;
    private final BrandUC brandUc;
    private final itemsUc itemsUc;
    private final stockUc stockUc;
    private final logStockUc logStockUc;
    private DefaultTableModel tableModel;

    HashMap<String, Integer> supplierMap = new HashMap<>();
    HashMap<String, Integer> brandMap = new HashMap<>();
    HashMap<String, Integer> itemMap = new HashMap<>();

    private int currentPage = 1;
    private int limit = 10;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnNext;
    private javax.swing.JLabel labelPage;

    public StockManagementGUI(Connection conn) {
        initPaginationComponents();
        initComponents();

        this.supplierUc = new supplierUc(new supplierRepo(conn), new brandsRepo(conn));
        this.itemsUc = new itemsUc(new itemsRepo(conn), new stocksRepo(conn));
        this.stockUc = new stockUc(new stocksRepo(conn), new itemsRepo(conn));
        this.logStockUc = new logStockUc(new LogStockRepo(conn));
        this.brandUc = new BrandUC(new brandsRepo(conn), new itemsRepo(conn));

        this.tableModel = (DefaultTableModel) tLogStock.getModel();

        // Ensure table model is set if not already by initComponents (redundant but
        // safe)
        if (tLogStock.getModel().getColumnCount() == 0) {
            tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Activity Name", "Barang",
                    "References(Supplier/TrxId)", "Qty", "Tanggal Dibuat" });
            tLogStock.setModel(tableModel);
        }

        loadCbSuppliers();
        loadLogStock();

        textFieldId.setEditable(false);
        textFieldId.setVisible(false);

        helper.TableUtils.styleTable(tLogStock);
    }

    private void initPaginationComponents() {
        btnPrev = new javax.swing.JButton("< Previous");
        btnNext = new javax.swing.JButton("Next >");
        labelPage = new javax.swing.JLabel("Page: 1");

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadLogStock();
            }
        });

        btnNext.addActionListener(e -> {
            currentPage++;
            loadLogStock();
        });
    }

    private void loadLogStock() {
        tableModel.setRowCount(0);
        int offset = (currentPage - 1) * limit;
        List<LogStock> listLogStock = logStockUc.getList("", 0, limit, offset);

        if (labelPage != null) {
            labelPage.setText("Page: " + currentPage);
        }

        for (LogStock s : listLogStock) {
            String formattedDate = "";
            if (s.getCreatedAt() != null) {
                formattedDate = s.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            }
            tableModel.addRow(new Object[] {
                    s.getId(),
                    s.getActivityName(),
                    s.getItemName(),
                    s.getRefId(),
                    s.getQty(),
                    formattedDate,
            });
        }
    }

    private void loadCbSuppliers() {
        List<suppliers> supplierList = supplierUc.getSuppliersList("", 0, 0, 0);

        for (suppliers s : supplierList) {
            supplierMap.put(s.getSupplierName(), s.getId());
            cbSupplier.addItem(s.getSupplierName());
        }
    }

    private void loadCbBrands() {
        if (cbSupplier.getSelectedItem() == null)
            return;
        String supplierName = cbSupplier.getSelectedItem().toString();
        List<brands> brandList = brandUc.listBrands("", supplierMap.get(supplierName), 0, 0, 0);

        for (brands b : brandList) {
            brandMap.put(b.getBrandName(), b.getId());
            cbBrand.addItem(b.getBrandName());
        }
    }

    private void loadCbItems() {
        if (cbBrand.getSelectedItem() == null)
            return;
        String brandName = cbBrand.getSelectedItem().toString();
        List<items> itemList = itemsUc.getAllItems("", brandMap.get(brandName), 0, 0);

        for (items i : itemList) {
            itemMap.put(i.getItemName(), i.getId());
            cbItem.addItem(i.getItemName());
        }
    }

    private void clearInputFields() {
        textFieldId.setText("");
        textFieldQty.setText("");
        // Optional: Reset selections if needed, but usually keeping them is better for
        // rapid entry
    }

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {
        if (cbItem.getSelectedItem() == null || cbSupplier.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data barang dan supplier.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qtyStr = textFieldQty.getText().trim();
        if (qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kuantitas tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format kuantitas salah.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (qty <= 0) {
            JOptionPane.showMessageDialog(this, "Kuantitas harus lebih dari 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        stock s = new stock();
        s.setItemId(itemMap.get(cbItem.getSelectedItem().toString()));
        s.setStockIn(qty);
        s.setUpdatedAt(LocalDateTime.now());

        LogStock lg = new LogStock();
        lg.setActivityName("stock_in");
        lg.setItemId(itemMap.get(cbItem.getSelectedItem().toString()));
        lg.setRefId(supplierMap.get(cbSupplier.getSelectedItem().toString()));
        lg.setQty(qty);
        lg.setCreatedAt(LocalDateTime.now());

        boolean successLg = logStockUc.createLogStock(lg);
        if (!successLg) {
            System.out.println("error insert log");
            JOptionPane.showMessageDialog(this, "Gagal menyimpan log stock.");
            return;
        }

        boolean success = stockUc.updateStockIn(s, 1);
        JOptionPane.showMessageDialog(this, "CREATE: " + (success ? "Berhasil" : "Gagal"));
        if (success) {
            clearInputFields();
            loadLogStock();
        }
    }

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        loadLogStock();
    }

    // Cascading combo box events
    private void cbSupplierActionPerformed(java.awt.event.ActionEvent evt) {
        cbBrand.removeAllItems();
        loadCbBrands();
    }

    private void cbBrandActionPerformed(java.awt.event.ActionEvent evt) {
        cbItem.removeAllItems();
        loadCbItems();
    }

    private void textFieldQtyKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }

    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tLogStock = new javax.swing.JTable();
        jLabelHeader = new javax.swing.JLabel();
        jLabelSupplier = new javax.swing.JLabel();
        cbSupplier = new javax.swing.JComboBox<>();
        jLabelBrand = new javax.swing.JLabel();
        cbBrand = new javax.swing.JComboBox<>();
        jLabelItem = new javax.swing.JLabel();
        cbItem = new javax.swing.JComboBox<>();
        jLabelQty = new javax.swing.JLabel();
        textFieldQty = new javax.swing.JTextField();
        btnCreate = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        textFieldId = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 204));
        setPreferredSize(new java.awt.Dimension(900, 750));

        tLogStock.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "ID", "Activity Name", "Barang", "References(Supplier/TrxId)", "Qty", "Tanggal Dibuat"
                }));
        jScrollPane2.setViewportView(tLogStock);

        jLabelHeader.setFont(new java.awt.Font("Gill Sans", 0, 36)); // NOI18N
        jLabelHeader.setForeground(new java.awt.Color(102, 102, 102));
        jLabelHeader.setText("STOCK BARANG");

        jLabelSupplier.setFont(new java.awt.Font("Gill Sans", 0, 14)); // NOI18N
        jLabelSupplier.setForeground(new java.awt.Color(102, 102, 102));
        jLabelSupplier.setText("Supplier");

        cbSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSupplierActionPerformed(evt);
            }
        });

        jLabelBrand.setFont(new java.awt.Font("Gill Sans", 0, 14)); // NOI18N
        jLabelBrand.setForeground(new java.awt.Color(102, 102, 102));
        jLabelBrand.setText("Brand");

        cbBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBrandActionPerformed(evt);
            }
        });

        jLabelItem.setFont(new java.awt.Font("Gill Sans", 0, 14)); // NOI18N
        jLabelItem.setForeground(new java.awt.Color(102, 102, 102));
        jLabelItem.setText("Barang");

        jLabelQty.setFont(new java.awt.Font("Gill Sans", 0, 14)); // NOI18N
        jLabelQty.setForeground(new java.awt.Color(102, 102, 102));
        jLabelQty.setText("Kuantitas");

        textFieldQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldQtyKeyTyped(evt);
            }
        });

        btnCreate.setBackground(new java.awt.Color(255, 153, 153));
        btnCreate.setForeground(new java.awt.Color(255, 255, 255));
        btnCreate.setText("Restock");
        btnCreate.setBorder(null);
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

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
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 451,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                .createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jScrollPane2,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 820,
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
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout
                                                                .createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabelSupplier)
                                                                        .addComponent(jLabelBrand)
                                                                        .addComponent(jLabelItem)
                                                                        .addComponent(jLabelQty))
                                                                .addGap(40, 40, 40)
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(btnCreate,
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
                                                                                .addComponent(cbSupplier, 0,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)
                                                                                .addComponent(cbBrand, 0,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)
                                                                                .addComponent(cbItem, 0,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)
                                                                                .addComponent(textFieldQty,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        287,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelSupplier)
                                        .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelBrand)
                                        .addComponent(cbBrand, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelItem)
                                        .addComponent(cbItem, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelQty)
                                        .addComponent(textFieldQty, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textFieldId, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 23,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 23,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnPrev)
                                        .addComponent(labelPage)
                                        .addComponent(btnNext))
                                .addContainerGap(40, Short.MAX_VALUE)));
    }

    // Variables declaration
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cbBrand;
    private javax.swing.JComboBox<String> cbItem;
    private javax.swing.JComboBox<String> cbSupplier;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelSupplier;
    private javax.swing.JLabel jLabelBrand;
    private javax.swing.JLabel jLabelItem;
    private javax.swing.JLabel jLabelQty;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tLogStock;
    private javax.swing.JTextField textFieldId;
    private javax.swing.JTextField textFieldQty;
}
