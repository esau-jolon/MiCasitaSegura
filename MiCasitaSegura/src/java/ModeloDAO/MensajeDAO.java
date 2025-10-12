package ModeloDAO;

import Config.Conexion;
import Modelo.Mensaje;
import java.sql.*;
import java.util.*;

public class MensajeDAO {

    // 🟢 Insertar nuevo mensaje
    public boolean enviarMensaje(Mensaje m) {
        String sql = "INSERT INTO Mensaje (idConversacion, idEmisor, idReceptor, contenido) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, m.getIdConversacion());
            ps.setInt(2, m.getIdEmisor());
            ps.setInt(3, m.getIdReceptor());
            ps.setString(4, m.getContenido());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                registrarAccion(m.getIdEmisor(), "Envió mensaje a usuario ID " + m.getIdReceptor());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al enviar mensaje: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🟡 Listar mensajes por conversación (ordenados por fecha)
    public List<Mensaje> listarMensajesPorConversacion(int idConversacion) {
        List<Mensaje> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mensaje WHERE idConversacion=? ORDER BY fechaEnvio ASC";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idConversacion);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mensaje m = new Mensaje();
                    m.setIdMensaje(rs.getInt("idMensaje"));
                    m.setIdConversacion(rs.getInt("idConversacion"));
                    m.setIdEmisor(rs.getInt("idEmisor"));
                    m.setIdReceptor(rs.getInt("idReceptor"));
                    m.setContenido(rs.getString("contenido"));
                    m.setFechaEnvio(rs.getTimestamp("fechaEnvio"));
                    m.setLeido(rs.getBoolean("leido"));
                    lista.add(m);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar mensajes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // 🟣 Marcar mensajes como leídos
    public void marcarComoLeido(int idMensaje) {
        String sql = "UPDATE Mensaje SET leido = 1 WHERE idMensaje = ?";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMensaje);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al marcar mensaje como leído: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 🔵 Registrar acción en tabla auditoría
    private void registrarAccion(int usuarioId, String accion) {
        String sql = "INSERT INTO auditoria (usuario_id, accion) VALUES (?, ?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setString(2, accion);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar auditoría: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
