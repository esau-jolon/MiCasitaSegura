package Config;

import java.sql.*;

public class Conexion {
    private Connection con;

    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3308/micasitasegura?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC",
                "root",
                ""
            );
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e);
        }
    }

    public Connection getConnection() {
        return con;
    }
}
