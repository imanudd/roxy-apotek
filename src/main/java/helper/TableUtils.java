package helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class TableUtils {

    public static void styleTable(JTable table) {
        table.setRowHeight(25);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(51, 153, 255));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(32, 136, 203)); // Match previous dashboard style
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // Note: Foreground color on header might not work with all LookAndFeels (like
        // FlatLaf),
        // but setting it here is safe.
    }
}
