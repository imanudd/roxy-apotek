/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author ADMINPUSING-PC
 */
public class aDashboard extends javax.swing.JPanel {
    //buildBarChart content = new buildBarChart();
    //private DefaultTableModel tableModel;
    private DefaultTableModel tableModel;
    //private javax.swing.JTable jTable1;
    /**
     * Creates new form aDashboard
     */
    public aDashboard() {
        initComponents();
//        
//        jPanel2.add(buildBarChart());
//        jPanel3.add(buildLineChart());
//        jPanel4.add(buildPieChart());
//        
//        tableModel = new DefaultTableModel(new Object[][]{{"1", "Paracetamol", "100", "Strip"},
//            {"2", "Vitamin C", "150", "Box"},
//            {"3", "Obat Batuk", "200", "Botol"}}, new String[]{"ID", "Nama Barang", "Stok", "Satuan"});
//        new JTable(tableModel);
// 
//        jTable1.setModel(tableModel);
//        
//        setVisible(true);   
// Optional global UI tweaks (local to this panel; you can move to main)
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Create main container panel (white card)
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(card, BorderLayout.CENTER);

        // top area: three charts in a row
         JPanel chartsRow = new JPanel(new GridLayout(1, 3, 12, 12));
        chartsRow.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // build charts (each returns ChartPanel)
        ChartPanel bar = buildBarChart();
        ChartPanel line = buildLineChart();
        ChartPanel pie = buildPieChart();

        // use border layout inside chart containers so charts scale nicely
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBackground(Color.WHITE);
        p1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        p1.add(bar, BorderLayout.CENTER);

        jPanel2.setLayout(new BorderLayout());
jPanel2.add(buildBarChart(), BorderLayout.CENTER);

        JPanel p3 = new JPanel(new BorderLayout());
        p3.setBackground(Color.WHITE);
        p3.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        p3.add(pie, BorderLayout.CENTER);

        chartsRow.add(p1);
        chartsRow.add(p2);
        chartsRow.add(p3);

        card.add(chartsRow, BorderLayout.NORTH);

        // bottom area: table inside a light panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 246, 248));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(12, 0, 0, 0),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        // table model: dummy data
        tableModel = new DefaultTableModel(new Object[][]{
            {"1", "Paracetamol", "100", "Strip"},
            {"2", "Vitamin C", "150", "Box"},
            {"3", "Obat Batuk", "200", "Botol"}
        }, new String[]{"ID", "Nama Barang", "Stok", "Satuan"});

        jTable1 = new JTable(tableModel);
        styleTable(jTable1);

        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(jTable1);
        scroll.setPreferredSize(new Dimension(0, 260));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scroll, BorderLayout.CENTER);

        card.add(tablePanel, BorderLayout.CENTER);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jPanel2.setMaximumSize(new java.awt.Dimension(300, 300));
        jPanel2.setLayout(new java.awt.CardLayout());

        jPanel3.setMaximumSize(new java.awt.Dimension(300, 300));
        jPanel3.setLayout(new java.awt.CardLayout());

        jPanel4.setLayout(new java.awt.CardLayout());

        jPanel5.setLayout(new java.awt.CardLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel5.add(jScrollPane1, "card2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    
    private ChartPanel buildBarChart() {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(100, "Obat", "Jan");
//        dataset.addValue(80, "Obat", "Feb");
//        dataset.addValue(60, "Obat", "Mar");
//
//        JFreeChart chart = ChartFactory.createBarChart(
//            "Stok Bulanan", "Bulan", "Jumlah", dataset,
//            PlotOrientation.VERTICAL, true, true, true);
//
//        return new ChartPanel(chart);
 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Obat", "Jan");
        dataset.addValue(80, "Obat", "Feb");
        dataset.addValue(60, "Obat", "Mar");

        JFreeChart chart = ChartFactory.createBarChart(
                "Stok Bulanan", "Bulan", "Jumlah", dataset);
        styleChart(chart);

        // nicer renderer
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.15);

        // title font
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
        chart.getLegend().setVisible(false);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(300, 250));
        panel.setPopupMenu(null);
        panel.setMouseWheelEnabled(false);
        return panel;
    }

    private ChartPanel buildLineChart() {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(50, "Penjualan", "Jan");
//        dataset.addValue(70, "Penjualan", "Feb");
//        dataset.addValue(90, "Penjualan", "Mar");
//
//        JFreeChart chart = ChartFactory.createLineChart(
//            "Grafik Penjualan", "Bulan", "Transaksi", dataset,
//            PlotOrientation.VERTICAL, false, true, false);
//
//        return new ChartPanel(chart);
DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(50, "Penjualan", "Jan");
        dataset.addValue(70, "Penjualan", "Feb");
        dataset.addValue(90, "Penjualan", "Mar");

        JFreeChart chart = ChartFactory.createLineChart(
                "Grafik Penjualan", "Bulan", "Transaksi", dataset);
        styleChart(chart);

        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
        chart.getLegend().setVisible(false);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(300, 250));
        panel.setPopupMenu(null);
        panel.setMouseWheelEnabled(false);
        return panel;
    }

    private ChartPanel buildPieChart() {
//        DefaultPieDataset dataset = new DefaultPieDataset();
//        dataset.setValue("Tablet", 40);
//        dataset.setValue("Syrup", 30);
//        dataset.setValue("Kapsul", 20);
//        dataset.setValue("Salep", 10);
//
//        JFreeChart chart1 = ChartFactory.createPieChart("Jenis Produk", dataset, true, true, false);
//        ChartPanel jPanel2 = new ChartPanel(chart1);
//        
//        return new ChartPanel(chart1);
DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Tablet", 40);
        dataset.setValue("Syrup", 30);
        dataset.setValue("Kapsul", 20);
        dataset.setValue("Salep", 10);

        JFreeChart chart = ChartFactory.createPieChart("Jenis Produk", dataset, true, false, false);
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
        styleChart(chart);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setSectionOutlinesVisible(false);
        plot.setSimpleLabels(true);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(300, 250));
        panel.setPopupMenu(null);
        panel.setMouseWheelEnabled(false);
        return panel;
    }

    private void initChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //ChartPanel jPanel2 = new ChartPanel(chart1);
    }



    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
    chart.getTitle().setPaint(Color.DARK_GRAY);

    // Mengatur plot
    Plot plot = chart.getPlot();
    plot.setBackgroundPaint(new Color(230, 230, 250));
    plot.setOutlinePaint(Color.BLACK);

    // Jika plot adalah CategoryPlot (contoh BarChart)
    if (plot instanceof CategoryPlot) {
        CategoryPlot cplot = (CategoryPlot) plot;
        cplot.setRangeGridlinePaint(Color.GRAY);
        cplot.setRangeGridlinesVisible(true);
        cplot.setDomainGridlinesVisible(false);
    }
    }

    private void styleTable(JTable jTable1) {
    jTable1.setRowHeight(25);
    jTable1.setShowVerticalLines(false);
    jTable1.setIntercellSpacing(new Dimension(0, 0));
    jTable1.setSelectionBackground(new Color(51, 153, 255));
    jTable1.setSelectionForeground(Color.WHITE);
    jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    JTableHeader header = jTable1.getTableHeader();
    header.setReorderingAllowed(false);
    header.setResizingAllowed(false);
    header.setBackground(new Color(32, 136, 203));
    header.setForeground(Color.BLACK);
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    
}

