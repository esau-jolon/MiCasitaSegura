package ModeloDAO;

import Config.Conexion;
import Modelo.Conversacion;
import java.sql.*;
import java.util.*;

public class ConversacionDAO {

    // Crear una nueva conversación
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

    // Listar conversaciones por usuario
    public List<Conversacion> listarPorUsuario(int idUsuario) {
        List<Conversacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Conversacion WHERE idResidente=? OR idAgente=? ORDER BY fechaCreacion DESC";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Conversacion c = new Conversacion();
                    c.setIdConversacion(rs.getInt("idConversacion"));
                    c.setIdResidente(rs.getInt("idResidente"));
                    c.setIdAgente(rs.getInt("idAgente"));
                    c.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                    c.setEstado(rs.getString("estado"));
                    lista.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Registrar acción en auditoría
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
