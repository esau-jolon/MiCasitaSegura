package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Datos de conexión (puedes moverlos a un archivo de configuración si quieres)
    private static final String URL = "jdbc:mysql://localhost:3308/micasitasegura?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Bloque estático: se ejecuta una sola vez cuando se carga la clase
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error cargando el Driver de MySQL: " + e.getMessage());
        }
    }

    /**
     * Devuelve una nueva conexión a la base de datos.
     * El que la use es responsable de cerrarla cuando ya no la necesite.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
