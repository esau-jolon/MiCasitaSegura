package ModeloDAO;

import Config.Conexion;
import Modelo.TipoInconveniente;
import java.sql.*;
import java.util.*;

public class TipoInconvenienteDAO {

    // 🔹 Crear un nuevo tipo de inconveniente
    public boolean crear(TipoInconveniente t) {
        String sql = "INSERT INTO TipoInconveniente (nombre, estado, creado_por) VALUES (?, ?, ?)";

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
        String sql = "SELECT * FROM TipoInconveniente ORDER BY id_tipo_inconveniente DESC";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoInconveniente t = new TipoInconveniente();
                t.setIdTipoInconveniente(rs.getInt("id_tipo_inconveniente"));
                t.setNombre(rs.getString("nombre"));
                t.setEstado(rs.getBoolean("estado"));
                t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                t.setFechaModificacion(rs.getTimestamp("fecha_modificacion"));
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
        String sql = "SELECT * FROM TipoInconveniente WHERE id_tipo_inconveniente = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    t = new TipoInconveniente();
                    t.setIdTipoInconveniente(rs.getInt("id_tipo_inconveniente"));
                    t.setNombre(rs.getString("nombre"));
                    t.setEstado(rs.getBoolean("estado"));
                    t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    t.setFechaModificacion(rs.getTimestamp("fecha_modificacion"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    // 🔹 Actualizar un tipo de inconveniente
    public boolean actualizar(TipoInconveniente t) {
        String sql = "UPDATE TipoInconveniente "
                + "SET nombre = ?, modificado_por = ?, fecha_modificacion = CURRENT_TIMESTAMP "
                + "WHERE id_tipo_inconveniente = ?";

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
        String sql = "UPDATE TipoInconveniente "
                + "SET estado = ?, modificado_por = ?, fecha_modificacion = CURRENT_TIMESTAMP "
                + "WHERE id_tipo_inconveniente = ?";

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
        String sql = "DELETE FROM TipoInconveniente WHERE id_tipo_inconveniente = ?";

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
