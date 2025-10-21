package ModeloDAO;

import Config.Conexion;
import Modelo.Conversacion;
import java.sql.*;
import java.util.*;

public class ConversacionDAO {

    // 🔹 Crear una nueva conversación con auditoría
    public boolean crearConversacion(Conversacion c) {
        String sql = "INSERT INTO Conversacion "
                + "(idResidente, idAgente, fechaCreacion, estado, creadoPor) "
                + "VALUES (?, ?, NOW(), ?, ?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getIdResidente());
            ps.setInt(2, c.getIdAgente());
            ps.setBoolean(3, c.isEstado());
            ps.setObject(4, c.getCreadoPor());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Verificar si ya existe una conversación entre residente y agente
    public boolean existeConversacion(int idResidente, int idAgente) {
        String sql = "SELECT COUNT(*) FROM Conversacion WHERE idResidente = ? AND idAgente = ? AND estado = TRUE";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idResidente);
            ps.setInt(2, idAgente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Obtener conversación por ID
    public Conversacion obtenerPorId(int idConversacion) {
        Conversacion c = null;
        String sql = "SELECT * FROM Conversacion WHERE idConversacion = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idConversacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Conversacion();
                    c.setIdConversacion(rs.getInt("idConversacion"));
                    c.setIdResidente(rs.getInt("idResidente"));
                    c.setIdAgente(rs.getInt("idAgente"));
                    c.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                    c.setEstado(rs.getBoolean("estado"));
                    c.setCreadoPor((Integer) rs.getObject("creadoPor"));
                    c.setModificadoPor((Integer) rs.getObject("modificadoPor"));
                    c.setFechaModificacion(rs.getTimestamp("fechaModificacion"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    // 🔹 Listar conversaciones por usuario (con nombres de residente y agente)
    public List<Conversacion> listarPorUsuario(int idUsuario) {
        List<Conversacion> lista = new ArrayList<>();

        String sql = "SELECT "
                + "c.idConversacion, c.idResidente, "
                + "r.nombre AS nombreResidente, r.apellidos AS apellidoResidente, "
                + "c.idAgente, g.nombre AS nombreAgente, g.apellidos AS apellidoAgente, "
                + "c.fechaCreacion, c.estado, c.creadoPor, c.modificadoPor, c.fechaModificacion "
                + "FROM Conversacion c "
                + "LEFT JOIN Usuarios r ON c.idResidente = r.id_usuario "
                + "LEFT JOIN Usuarios g ON c.idAgente = g.id_usuario "
                + "WHERE c.idResidente = ? OR c.idAgente = ? "
                + "ORDER BY c.fechaCreacion DESC";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Conversacion c = new Conversacion();
                    c.setIdConversacion(rs.getInt("idConversacion"));
                    c.setIdResidente(rs.getInt("idResidente"));
                    c.setNombreResidente(rs.getString("nombreResidente"));
                    c.setApellidoResidente(rs.getString("apellidoResidente"));
                    c.setIdAgente(rs.getInt("idAgente"));
                    c.setNombreAgente(rs.getString("nombreAgente"));
                    c.setApellidoAgente(rs.getString("apellidoAgente"));
                    c.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                    c.setEstado(rs.getBoolean("estado"));
                    c.setCreadoPor((Integer) rs.getObject("creadoPor"));
                    c.setModificadoPor((Integer) rs.getObject("modificadoPor"));
                    c.setFechaModificacion(rs.getTimestamp("fechaModificacion"));
                    lista.add(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("✅ Conversaciones encontradas: " + lista.size());
        return lista;
    }

    // 🔹 Actualizar conversación (por ejemplo, cerrar o modificar agente)
    public boolean actualizarConversacion(Conversacion c) {
        String sql = "UPDATE Conversacion "
                + "SET idAgente = ?, estado = ?, modificadoPor = ?, fechaModificacion = NOW() "
                + "WHERE idConversacion = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getIdAgente());
            ps.setBoolean(2, c.isEstado());
            ps.setObject(3, c.getModificadoPor());
            ps.setInt(4, c.getIdConversacion());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar lógicamente una conversación
    public boolean eliminarConversacion(int idConversacion, int usuarioId) {
        String sql = "UPDATE Conversacion SET estado = FALSE, modificadoPor = ?, fechaModificacion = NOW() WHERE idConversacion = ?";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setInt(2, idConversacion);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
