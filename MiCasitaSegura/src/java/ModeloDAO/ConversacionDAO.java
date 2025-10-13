package ModeloDAO;

import Config.Conexion;
import Modelo.Conversacion;
import java.sql.*;
import java.util.*;

public class ConversacionDAO {

    // 🔹 Crear una nueva conversación
    public boolean crearConversacion(Conversacion c, int usuarioId) {
        String sql = "INSERT INTO Conversacion (idResidente, idAgente, estado) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getIdResidente());
            ps.setInt(2, c.getIdAgente());
            ps.setString(3, c.getEstado());
            int filas = ps.executeUpdate();

            if (filas > 0) {
                registrarAccion(usuarioId, "Creó conversación con agente ID " + c.getIdAgente());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Verificar si ya existe una conversación entre residente y agente
    public boolean existeConversacion(int idResidente, int idAgente) {
        String sql = "SELECT COUNT(*) FROM Conversacion WHERE idResidente = ? AND idAgente = ?";
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

    // 🔹 Obtener conversación por ID (sin join, solo base)
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
                    c.setEstado(rs.getString("estado"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    // 🔹 Listar conversaciones por usuario (con nombres del residente y agente)
    public List<Conversacion> listarPorUsuario(int idUsuario) {
        List<Conversacion> lista = new ArrayList<>();

        String sql = "SELECT "
                + "c.idConversacion, "
                + "c.idResidente, "
                + "r.nombre AS nombreResidente, "
                + "r.apellidos AS apellidoResidente, "
                + "c.idAgente, "
                + "g.nombre AS nombreAgente, "
                + "g.apellidos AS apellidoAgente, "
                + "c.fechaCreacion, "
                + "c.estado "
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
                    c.setEstado(rs.getString("estado"));
                    lista.add(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("✅ Conversaciones encontradas: " + lista.size());
        return lista;
    }

    // 🔹 Registrar acción en la auditoría
    private void registrarAccion(int usuarioId, String accion) {
        String sql = "INSERT INTO auditoria (usuario_id, accion) VALUES (?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setString(2, accion);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
