package ModeloDAO;

import Config.Conexion;
import Modelo.Notificacion;
import java.sql.*;
import java.util.*;

public class NotificacionDAO {

    
    public boolean registrar(Notificacion n) {
        String sql = "INSERT INTO Notificacion (id_incidente, id_guardia, asunto, cuerpo, creado_por) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n.getIdIncidente());
            ps.setInt(2, n.getIdGuardia());
            ps.setString(3, n.getAsunto());
            ps.setString(4, n.getCuerpo());
            ps.setInt(5, n.getCreadoPor());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Listar notificaciones enviadas
    public List<Notificacion> listarPorIncidente(int idIncidente) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT n.*, u.nombre AS nombreGuardia "
                   + "FROM Notificacion n "
                   + "LEFT JOIN Usuarios u ON n.id_guardia = u.id_usuario "
                   + "WHERE n.id_incidente = ? ORDER BY n.fecha_envio DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idIncidente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notificacion n = new Notificacion();
                    n.setIdNotificacion(rs.getInt("id_notificacion"));
                    n.setIdGuardia(rs.getInt("id_guardia"));
                    n.setAsunto(rs.getString("asunto"));
                    n.setCuerpo(rs.getString("cuerpo"));
                    n.setFechaEnvio(rs.getTimestamp("fecha_envio"));
                    n.setNombreGuardia(rs.getString("nombreGuardia"));
                    lista.add(n);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
