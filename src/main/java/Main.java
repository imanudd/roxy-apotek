import config.DatabaseConfig;
import ui.Login;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Membuat koneksi database hanya sekali
                Connection conn = DatabaseConfig.connect();

                // Buat JFrame
                JFrame frame = new JFrame("LOGIN - INVENTORY APOTEK ROXY");

                // Pass koneksi ke Login panel
                frame.setContentPane(new Login(conn));

                // Atur JFrame
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal koneksi ke database!");
            }
        });
    }
}
