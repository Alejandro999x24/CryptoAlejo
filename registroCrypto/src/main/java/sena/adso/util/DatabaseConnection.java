package sena.adso.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Conexiones para probar dos puertos posibles
    private static final String URL_PRIMARY = "jdbc:mysql://localhost:3308/alejocrypto?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String URL_FALLBACK = "jdbc:mysql://localhost:3306/alejocrypto?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";

    // Usuario y contraseña REALES
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 1. Intentar puerto 3308
            try {
                return DriverManager.getConnection(URL_PRIMARY, USER, PASSWORD);
            } catch (SQLException ex1) {
                System.err.println("❗ No se pudo conectar al puerto 3308: " + ex1.getMessage());
            }

            // 2. Intentar puerto 3306
            try {
                return DriverManager.getConnection(URL_FALLBACK, USER, PASSWORD);
            } catch (SQLException ex2) {
                System.err.println("❗ No se pudo conectar al puerto 3306: " + ex2.getMessage());
                throw ex2; // lanzar error final
            }

        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ Driver MySQL no encontrado", e);
        }
    }
}
