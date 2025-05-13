package ui;


import model.user;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    JPanel content = buildMainContent();
    
    public Dashboard(user currentUser) {
        setTitle("Dashboard - Apotek Roxy");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidebar = buildSidebar();
               

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
               

        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 45));
        panel.setLayout(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(200, 0));

        String[] menus = {
            "Dashboard", "Manajemen Barang", "Manajemen Supplier", "Manajemen Brand",
            "Stok Barang", "Log Stok", "Transaksi Penjualan", "Laporan Transaksi", "User Management"
        };

        for (String menu : menus) {
            JButton btn = new JButton(menu);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(60, 63, 65));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            // Menambahkan ActionListener untuk tombol
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (menu.equals("Dashboard")) {
                        // Aksi yang ingin dilakukan saat tombol "Manajemen Supplier" ditekan
                        content.setVisible(true);
                    } if (menu.equals("Manajemen Supplier")) {
                        // Aksi yang ingin dilakukan saat tombol "Manajemen Supplier" ditekan               
                        SupplierManagementGUI supplierManagementGUI = new SupplierManagementGUI();
                        add(supplierManagementGUI);;
                        content.setVisible(false);
                    } else {
                        // Aksi untuk tombol lainnya jika diperlukan
                        System.out.println("Tombol " + menu + " ditekan");
                    }
                }
            });
            panel.add(btn);
        }

        return panel;
    }

    private JPanel buildMainContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel summaryLabel = new JLabel("Ringkasan Stok & Transaksi", SwingConstants.CENTER);
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(summaryLabel, BorderLayout.NORTH);

        JPanel chartsPanel = new JPanel(new GridLayout(1, 3));
        chartsPanel.add(buildBarChart());
        chartsPanel.add(buildLineChart());
        chartsPanel.add(buildPieChart());

        JTable table = new JTable(new Object[][] {
            {"1", "Paracetamol", "100", "Strip"},
            {"2", "Vitamin C", "150", "Box"},
            {"3", "Obat Batuk", "200", "Botol"}
        }, new Object[] {"ID", "Nama Barang", "Stok", "Satuan"});

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("Data Stok", SwingConstants.CENTER), BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 1));
        bottomPanel.add(tablePanel);

        panel.add(chartsPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private ChartPanel buildBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Obat", "Jan");
        dataset.addValue(80, "Obat", "Feb");
        dataset.addValue(60, "Obat", "Mar");

        JFreeChart chart = ChartFactory.createBarChart(
            "Stok Bulanan", "Bulan", "Jumlah", dataset,
            PlotOrientation.VERTICAL, false, true, false);

        return new ChartPanel(chart);
    }

    private ChartPanel buildLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(50, "Penjualan", "Jan");
        dataset.addValue(70, "Penjualan", "Feb");
        dataset.addValue(90, "Penjualan", "Mar");

        JFreeChart chart = ChartFactory.createLineChart(
            "Grafik Penjualan", "Bulan", "Transaksi", dataset,
            PlotOrientation.VERTICAL, false, true, false);

        return new ChartPanel(chart);
    }

    private ChartPanel buildPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Tablet", 40);
        dataset.setValue("Syrup", 30);
        dataset.setValue("Kapsul", 20);
        dataset.setValue("Salep", 10);

        JFreeChart chart = ChartFactory.createPieChart("Jenis Produk", dataset, true, true, false);
        return new ChartPanel(chart);
    }
    
    
}
