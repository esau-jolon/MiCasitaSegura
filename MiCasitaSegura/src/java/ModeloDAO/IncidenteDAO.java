package ModeloDAO;

import Config.Conexion;
import Modelo.Incidente;
import java.sql.*;
import java.util.*;

public class IncidenteDAO {

    // 🔹 Crear un nuevo incidente
    public boolean crearIncidente(Incidente i) {
        String sql = "INSERT INTO Incidente (id_residente, id_tipo_incidente, fecha_hora_incidente, descripcion, creado_por, activo) "
                + "VALUES (?, ?, ?, ?, ?, 1)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, i.getIdResidente());
            ps.setInt(2, i.getIdTipoIncidente());
            ps.setTimestamp(3, i.getFechaHoraIncidente());
            ps.setString(4, i.getDescripcion());
            ps.setInt(5, i.getCreadoPor());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Listar incidentes activos
    public List<Incidente> listar() {
        List<Incidente> lista = new ArrayList<>();
        String sql = "SELECT i.*, t.nombre AS tipo, u.nombre AS nombreResidente, u.apellidos AS apellidoResidente "
                + "FROM Incidente i "
                + "LEFT JOIN TipoIncidente t ON i.id_tipo_incidente = t.id_tipo_incidente "
                + "LEFT JOIN Usuarios u ON i.id_residente = u.id_usuario "
                + "WHERE i.activo = 1 "
                + "ORDER BY i.fecha_creacion DESC";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Incidente i = new Incidente();
                i.setIdIncidente(rs.getInt("id_incidente"));
                i.setIdResidente(rs.getInt("id_residente"));
                i.setIdTipoIncidente(rs.getInt("id_tipo_incidente"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setFechaHoraIncidente(rs.getTimestamp("fecha_hora_incidente"));
                i.setNombreTipo(rs.getString("tipo"));
                i.setNombreResidente(rs.getString("nombreResidente"));
                i.setApellidoResidente(rs.getString("apellidoResidente"));
                i.setActivo(rs.getBoolean("activo"));
                lista.add(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Listar incidentes por residente (solo activos)
    public List<Incidente> listarPorResidente(int idResidente) {
        List<Incidente> lista = new ArrayList<>();

        String sql = "SELECT i.*, t.nombre AS nombre_tipo_incidente "
                + "FROM Incidente i "
                + "INNER JOIN TipoIncidente t ON i.id_tipo_incidente = t.id_tipo_incidente "
                + "WHERE i.id_residente = ? AND i.activo = 1 "
                + "ORDER BY i.fecha_hora_incidente DESC";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idResidente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Incidente inc = new Incidente();
                    inc.setIdIncidente(rs.getInt("id_incidente"));
                    inc.setIdResidente(rs.getInt("id_residente"));
                    inc.setIdTipoIncidente(rs.getInt("id_tipo_incidente"));
                    inc.setFechaHoraIncidente(rs.getTimestamp("fecha_hora_incidente"));
                    inc.setDescripcion(rs.getString("descripcion"));
                    inc.setNombreTipoIncidente(rs.getString("nombre_tipo_incidente"));
                    inc.setActivo(rs.getBoolean("activo"));
                    lista.add(inc);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Borrado lógico (marcar inactivo)
    public boolean eliminarLogico(int idIncidente, int idUsuarioModificador) {
        String sql = "UPDATE Incidente "
                + "SET activo = 0, modificado_por = ?, fecha_modificacion = NOW() "
                + "WHERE id_incidente = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioModificador);
            ps.setInt(2, idIncidente);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
