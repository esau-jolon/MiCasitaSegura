package ModeloDAO;

import Config.Conexion;
import Modelo.ReporteMantenimiento;
import java.sql.*;
import java.util.*;

public class ReporteMantenimientoDAO {

    // 🔹 Crear un nuevo reporte de mantenimiento
    public boolean crearReporte(ReporteMantenimiento r) {
        String sql = "INSERT INTO ReporteMantenimiento "
                + "(id_tipo_inconveniente, id_residente, descripcion, fecha_hora_incidente, creado_por) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdTipoInconveniente());
            ps.setInt(2, r.getIdResidente());
            ps.setString(3, r.getDescripcion());
            ps.setTimestamp(4, r.getFechaHoraIncidente());
            ps.setInt(5, r.getCreadoPor());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Listar todos los reportes (para administrador o mantenimiento)
    public List<ReporteMantenimiento> listar() {
        List<ReporteMantenimiento> lista = new ArrayList<>();
        String sql = "SELECT r.*, t.Nombre AS nombre_tipo_inconveniente, "
                + "u.nombre AS nombreResidente, u.apellidos AS apellidoResidente "
                + "FROM ReporteMantenimiento r "
                + "INNER JOIN TipoInconveniente t ON r.id_tipo_inconveniente = t.id_tipo_inconveniente "
                + "INNER JOIN Usuarios u ON r.id_residente = u.id_usuario "
                + "ORDER BY r.fecha_creacion DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReporteMantenimiento r = new ReporteMantenimiento();
                r.setIdReporte(rs.getInt("id_reporte"));
                r.setIdTipoInconveniente(rs.getInt("id_tipo_inconveniente"));
                r.setIdResidente(rs.getInt("id_residente"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setFechaHoraIncidente(rs.getTimestamp("fecha_hora_incidente"));
                r.setNombreTipoInconveniente(rs.getString("nombre_tipo_inconveniente"));
                r.setNombreResidente(rs.getString("nombreResidente"));
                r.setApellidoResidente(rs.getString("apellidoResidente"));
                lista.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Listar reportes por residente (para vista del usuario)
    public List<ReporteMantenimiento> listarPorResidente(int idResidente) {
        List<ReporteMantenimiento> lista = new ArrayList<>();
        String sql = "SELECT r.*, t.Nombre AS nombre_tipo_inconveniente "
                + "FROM ReporteMantenimiento r "
                + "INNER JOIN TipoInconveniente t ON r.id_tipo_inconveniente = t.id_tipo_inconveniente "
                + "WHERE r.id_residente = ? "
                + "ORDER BY r.fecha_hora_incidente DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idResidente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteMantenimiento r = new ReporteMantenimiento();
                    r.setIdReporte(rs.getInt("id_reporte"));
                    r.setIdTipoInconveniente(rs.getInt("id_tipo_inconveniente"));
                    r.setIdResidente(rs.getInt("id_residente"));
                    r.setDescripcion(rs.getString("descripcion"));
                    r.setFechaHoraIncidente(rs.getTimestamp("fecha_hora_incidente"));
                    r.setNombreTipoInconveniente(rs.getString("nombre_tipo_inconveniente"));
                    lista.add(r);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Actualizar un reporte existente
    public boolean actualizarReporte(ReporteMantenimiento r) {
        String sql = "UPDATE ReporteMantenimiento SET "
                + "id_tipo_inconveniente = ?, descripcion = ?, "
                + "fecha_hora_incidente = ?, modificado_por = ? "
                + "WHERE id_reporte = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdTipoInconveniente());
            ps.setString(2, r.getDescripcion());
            ps.setTimestamp(3, r.getFechaHoraIncidente());
            ps.setInt(4, r.getModificadoPor());
            ps.setInt(5, r.getIdReporte());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar un reporte (opcional)
    public boolean eliminarReporte(int idReporte) {
        String sql = "DELETE FROM ReporteMantenimiento WHERE id_reporte = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idReporte);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
