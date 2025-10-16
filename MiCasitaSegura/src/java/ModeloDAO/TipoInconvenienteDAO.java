package ModeloDAO;

import Config.Conexion;
import Modelo.TipoInconveniente;
import java.sql.*;
import java.util.*;

public class TipoInconvenienteDAO {

    // 🔹 Crear un nuevo tipo de inconveniente
    public boolean crear(TipoInconveniente t) {
        String sql = "INSERT INTO tipoinconveniente (nombre, estado, creadopor) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getNombre());
            ps.setBoolean(2, true); // por defecto activo
            ps.setInt(3, t.getCreadoPor());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Listar todos los tipos de inconveniente
    public List<TipoInconveniente> listar() {
        List<TipoInconveniente> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipoinconveniente ORDER BY idtipoinconveniente DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoInconveniente t = new TipoInconveniente();
                t.setIdTipoInconveniente(rs.getInt("idtipoinconveniente"));
                t.setNombre(rs.getString("nombre"));
                t.setEstado(rs.getBoolean("estado"));

                // Campos opcionales (solo si existen en tu esquema)
                try {
                    t.setFechaCreacion(rs.getTimestamp("fechacreacion"));
                    t.setFechaModificacion(rs.getTimestamp("fechamodificacion"));
                } catch (SQLException ignored) {}

                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Obtener un tipo por ID
    public TipoInconveniente obtenerPorId(int id) {
        TipoInconveniente t = null;
        String sql = "SELECT * FROM tipoinconveniente WHERE idtipoinconveniente = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    t = new TipoInconveniente();
                    t.setIdTipoInconveniente(rs.getInt("idtipoinconveniente"));
                    t.setNombre(rs.getString("nombre"));
                    t.setEstado(rs.getBoolean("estado"));

                    try {
                        t.setFechaCreacion(rs.getTimestamp("fechacreacion"));
                        t.setFechaModificacion(rs.getTimestamp("fechamodificacion"));
                    } catch (SQLException ignored) {}
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    // 🔹 Actualizar un tipo de inconveniente
    public boolean actualizar(TipoInconveniente t) {
        String sql = "UPDATE tipoinconveniente "
                   + "SET nombre = ?, modificadopor = ?, fechamodificacion = CURRENT_TIMESTAMP "
                   + "WHERE idtipoinconveniente = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getNombre());
            ps.setInt(2, t.getModificadoPor());
            ps.setInt(3, t.getIdTipoInconveniente());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cambiar el estado (activar / desactivar)
    public boolean cambiarEstado(int id, boolean estado, int modificadoPor) {
        String sql = "UPDATE tipoinconveniente "
                   + "SET estado = ?, modificadopor = ?, fechamodificacion = CURRENT_TIMESTAMP "
                   + "WHERE idtipoinconveniente = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, estado);
            ps.setInt(2, modificadoPor);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar tipo (opcional)
    public boolean eliminar(int id) {
        String sql = "DELETE FROM tipoinconveniente WHERE idtipoinconveniente = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
