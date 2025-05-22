package config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    public static Connection connect() {
        try {
            Dotenv dotenv = Dotenv.configure()
            .directory(".env")
            .load();

            String dbHost = dotenv.get("DB_HOST");
            String dbPort = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            String dbUser = dotenv.get("DB_USER");
            String dbPassword = dotenv.get("DB_PASSWORD");

            if (dbHost == null || dbPort == null || dbName == null || dbUser == null || dbPassword == null) {
                System.err.println("Variabel .env tidak lengkap atau tidak terbaca!");
                return null;
            }

            String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName+"?prepareThreshold=0";
            
            System.out.println("url : "+ url);

            return DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (SQLException e) {
            System.err.println("Koneksi ke database gagal: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }

        return null;
    }
}
