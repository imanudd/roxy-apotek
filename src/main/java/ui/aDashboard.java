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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import java.text.DecimalFormat;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import repository.LogStockRepo;
import repository.itemsRepo;
import repository.transactionsRepo;
import model.items;
import model.transaction;
import model.LogStock;

/**
 *
 * @author ADMINPUSING-PC
 */
public class aDashboard extends javax.swing.JPanel {

        private LogStockRepo logStockRepo;
        private itemsRepo itemsRepo;
        private transactionsRepo transactionsRepo;

        /**
         * Creates new form aDashboard
         */
        public aDashboard(Connection conn) {
                this.logStockRepo = new LogStockRepo(conn);
                this.itemsRepo = new itemsRepo(conn);
                this.transactionsRepo = new transactionsRepo(conn);

                initComponents();

                UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
                UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
                UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
                UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));

                setLayout(new BorderLayout());
                setBackground(new Color(250, 250, 250));
                setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

                // Create main container panel (white card)
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(new Color(250, 250, 250));
                card.setBorder(BorderFactory.createEmptyBorder(12, 50, 12, 50));
                add(card, BorderLayout.CENTER);

                // Create charts
                ChartPanel bar = buildBarChart();
                ChartPanel line = buildLineChart();
                ChartPanel pie = buildPieChart();

                // Style chart containers
                JPanel p1 = createChartContainer(bar);
                JPanel p2 = createChartContainer(line);
                JPanel p3 = createChartContainer(pie);

                // Top Area: Stock (Bar) and Sales (Line)
                JPanel topRow = new JPanel(new GridLayout(1, 2, 12, 12));
                topRow.setBackground(new Color(250, 250, 250));
                topRow.add(p1);
                topRow.add(p2);

                // Bottom Area: Pie Chart
                // We wrap p3 in another panel so it doesn't stretch weirdly if we don't want it
                // to
                JPanel bottomRow = new JPanel(new BorderLayout());
                bottomRow.setBackground(new Color(250, 250, 250));
                bottomRow.add(p3, BorderLayout.CENTER);

                // Main Layout
                JPanel mainContent = new JPanel(new BorderLayout(12, 12));
                mainContent.setBackground(new Color(250, 250, 250));
                mainContent.add(topRow, BorderLayout.CENTER);
                mainContent.add(bottomRow, BorderLayout.SOUTH);

                card.add(mainContent, BorderLayout.CENTER);

                // Wrap the card in a ScrollPane to prevent cutoff
                JScrollPane scrollPane = new JScrollPane(card);
                scrollPane.setBorder(null);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
                scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

                add(scrollPane, BorderLayout.CENTER);
        }

        private JPanel createChartContainer(ChartPanel chartPanel) {
                JPanel p = new JPanel(new BorderLayout());
                p.setBackground(Color.WHITE);
                p.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
                p.add(chartPanel, BorderLayout.CENTER);
                return p;
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
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

                jPanel5.add(jScrollPane1, "card2");

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(jPanel5,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jPanel2,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                300,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jPanel3,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                300,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addGap(12, 12, 12)
                                                                                                .addComponent(jPanel4,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                300,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(16, 16, 16)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel1Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(jPanel2,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                300,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel3,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                300,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel4,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                300,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jPanel5,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                323,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(14, Short.MAX_VALUE)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
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
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                try {
                        // Get data for last 365 days (1 year)
                        System.out.println("Fetching LogStock data for last 365 days...");
                        List<LogStock> logs = logStockRepo.getList(null, 365, 0, 0);
                        System.out.println("Fetched " + logs.size() + " logs.");

                        // Format for grouping
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", java.util.Locale.ENGLISH);

                        // Group by Month and Activity
                        Map<String, Map<String, Integer>> grouped = logs.stream()
                                        .filter(l -> l.getCreatedAt() != null)
                                        .collect(Collectors.groupingBy(
                                                        l -> l.getCreatedAt().format(formatter),
                                                        Collectors.groupingBy(LogStock::getActivityName,
                                                                        Collectors.summingInt(LogStock::getQty))));

                        // Add to dataset (sorted manually)
                        java.time.LocalDate now = java.time.LocalDate.now();
                        for (int i = 5; i >= 0; i--) { // Show last 6 months
                                java.time.LocalDate d = now.minusMonths(i);
                                String monthLabel = d.format(formatter);

                                Map<String, Integer> activities = grouped.getOrDefault(monthLabel, Map.of());

                                int in = activities.getOrDefault("stock_in", 0);
                                int out = activities.getOrDefault("stock_out", 0);
                                dataset.addValue(in, "Stock In", monthLabel);
                                dataset.addValue(out, "Stock Out", monthLabel);

                                System.out.println("Month: " + monthLabel + " -> In: " + in + ", Out: " + out);
                        }

                } catch (SQLException e) {
                        e.printStackTrace();
                }

                JFreeChart chart = ChartFactory.createBarChart(
                                "Aktivitas Stok (6 Bulan Terakhir)", "Bulan", "Qty", dataset);
                styleChart(chart);

                CategoryPlot plot = (CategoryPlot) chart.getPlot();
                BarRenderer renderer = (BarRenderer) plot.getRenderer();
                renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
                renderer.setShadowVisible(false);
                renderer.setMaximumBarWidth(0.15);

                // Format Axis to Integer
                NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
                rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                rangeAxis.setNumberFormatOverride(new DecimalFormat("0"));

                renderer.setSeriesPaint(0, new Color(46, 204, 113)); // Green for In
                renderer.setSeriesPaint(1, new Color(231, 76, 60)); // Red for Out

                chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
                chart.getLegend().setVisible(true);

                ChartPanel panel = new ChartPanel(chart);
                panel.setPreferredSize(new Dimension(300, 250));
                panel.setPopupMenu(null);
                panel.setMouseWheelEnabled(false);
                return panel;
        }

        private ChartPanel buildLineChart() {
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                try {
                        // Get sales for last 365 days
                        System.out.println("Fetching Transactions for last 365 days...");
                        List<transaction> trans = transactionsRepo.getAllTransaction(365, 0, 0);
                        System.out.println("Fetched " + trans.size() + " transactions.");

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", java.util.Locale.ENGLISH);

                        Map<String, Double> salesPerMonth = trans.stream()
                                        .filter(t -> t.getCreatedAt() != null)
                                        .collect(Collectors.groupingBy(
                                                        t -> t.getCreatedAt().format(formatter),
                                                        Collectors.summingDouble(transaction::getGrandTotal)));

                        java.time.LocalDate now = java.time.LocalDate.now();
                        for (int i = 5; i >= 0; i--) { // Show last 6 months
                                java.time.LocalDate d = now.minusMonths(i);
                                String monthLabel = d.format(formatter);
                                Double total = salesPerMonth.getOrDefault(monthLabel, 0.0);
                                dataset.addValue(total, "Penjualan", monthLabel);

                                System.out.println("Month: " + monthLabel + " -> Sales: " + total);
                        }

                } catch (SQLException e) {
                        e.printStackTrace();
                }

                JFreeChart chart = ChartFactory.createLineChart(
                                "Penjualan (IDR)", "Bulan", "Rupiah", dataset);
                styleChart(chart);

                chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
                chart.getLegend().setVisible(false);

                // Format Axis to Currency/Number
                CategoryPlot plot = (CategoryPlot) chart.getPlot();
                NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
                rangeAxis.setNumberFormatOverride(new DecimalFormat("#,##0"));

                ChartPanel panel = new ChartPanel(chart);
                panel.setPreferredSize(new Dimension(300, 250));
                panel.setPopupMenu(null);
                panel.setMouseWheelEnabled(false);
                return panel;
        }

        private ChartPanel buildPieChart() {
                DefaultPieDataset dataset = new DefaultPieDataset();

                try {
                        List<items> allArgs = itemsRepo.getAllItems("", 0, 0, 0);
                        Map<String, Long> brandCounts = allArgs.stream()
                                        .collect(Collectors.groupingBy(items::getBrandName, Collectors.counting()));

                        // Top 5 brands
                        brandCounts.entrySet().stream()
                                        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                                        .limit(5)
                                        .forEach(e -> dataset.setValue(e.getKey(), e.getValue()));

                } catch (Exception e) {
                        e.printStackTrace();
                }

                JFreeChart chart = ChartFactory.createPieChart("Top 5 Brands", dataset, true, false, false);
                chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
                styleChart(chart);

                // Position legend at bottom to give more room for pie chart
                chart.getLegend().setPosition(org.jfree.chart.ui.RectangleEdge.BOTTOM);

                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
                plot.setBackgroundPaint(new Color(250, 250, 250));
                plot.setSectionOutlinesVisible(false);
                plot.setSimpleLabels(true);
                plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));
                plot.setInteriorGap(0.02); // Reduce interior gap to make pie larger
                plot.setLabelGap(0.02); // Reduce label gap

                ChartPanel panel = new ChartPanel(chart);
                panel.setPreferredSize(new Dimension(350, 300));
                panel.setMinimumDrawWidth(300);
                panel.setMinimumDrawHeight(250);
                panel.setPopupMenu(null);
                panel.setMouseWheelEnabled(false);
                return panel;
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

}
