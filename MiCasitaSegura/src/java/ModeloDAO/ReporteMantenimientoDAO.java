package ModeloDAO;

import Config.Conexion;
import Modelo.ReporteMantenimiento;
import java.sql.*;
import java.util.*;

public class ReporteMantenimientoDAO {

    // 🔹 Crear un nuevo reporte de mantenimiento
    public boolean crearReporte(ReporteMantenimiento r) {
        String sql = "INSERT INTO ReporteMantenimiento "
                + "(IdTipoInconveniente, IdResidente, Descripcion, FechaHoraIncidente, CreadoPor) "
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
            System.err.println("Error al crear reporte: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Listar todos los reportes (para administrador o mantenimiento)
    public List<ReporteMantenimiento> listar() {
        List<ReporteMantenimiento> lista = new ArrayList<>();

        String sql = "SELECT r.*, t.Nombre AS NombreTipoInconveniente, "
                + "u.Nombre AS NombreResidente, u.Apellidos AS ApellidoResidente "
                + "FROM ReporteMantenimiento r "
                + "INNER JOIN TipoInconveniente t ON r.IdTipoInconveniente = t.IdTipoInconveniente "
                + "INNER JOIN Usuarios u ON r.IdResidente = u.IdUsuario "
                + "ORDER BY r.FechaCreacion DESC";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReporteMantenimiento r = new ReporteMantenimiento();
                r.setIdReporte(rs.getInt("IdReporte"));
                r.setIdTipoInconveniente(rs.getInt("IdTipoInconveniente"));
                r.setIdResidente(rs.getInt("IdResidente"));
                r.setDescripcion(rs.getString("Descripcion"));
                r.setFechaHoraIncidente(rs.getTimestamp("FechaHoraIncidente"));
                r.setNombreTipoInconveniente(rs.getString("NombreTipoInconveniente"));
                r.setNombreResidente(rs.getString("NombreResidente"));
                r.setApellidoResidente(rs.getString("ApellidoResidente"));
                lista.add(r);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar reportes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Listar reportes por residente (para vista del usuario)
    public List<ReporteMantenimiento> listarPorResidente(int idResidente) {
        List<ReporteMantenimiento> lista = new ArrayList<>();
        String sql = "SELECT r.*, t.Nombre AS NombreTipoInconveniente "
                + "FROM ReporteMantenimiento r "
                + "INNER JOIN TipoInconveniente t ON r.IdTipoInconveniente = t.IdTipoInconveniente "
                + "WHERE r.IdResidente = ? "
                + "ORDER BY r.FechaHoraIncidente DESC";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idResidente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteMantenimiento r = new ReporteMantenimiento();
                    r.setIdReporte(rs.getInt("IdReporte"));
                    r.setIdTipoInconveniente(rs.getInt("IdTipoInconveniente"));
                    r.setIdResidente(rs.getInt("IdResidente"));
                    r.setDescripcion(rs.getString("Descripcion"));
                    r.setFechaHoraIncidente(rs.getTimestamp("FechaHoraIncidente"));
                    r.setNombreTipoInconveniente(rs.getString("NombreTipoInconveniente"));
                    lista.add(r);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar por residente: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Actualizar un reporte existente
    public boolean actualizarReporte(ReporteMantenimiento r) {
        String sql = "UPDATE ReporteMantenimiento SET "
                + "IdTipoInconveniente = ?, Descripcion = ?, "
                + "FechaHoraIncidente = ?, ModificadoPor = ?, "
                + "FechaModificacion = CURRENT_TIMESTAMP "
                + "WHERE IdReporte = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdTipoInconveniente());
            ps.setString(2, r.getDescripcion());
            ps.setTimestamp(3, r.getFechaHoraIncidente());
            ps.setInt(4, r.getModificadoPor());
            ps.setInt(5, r.getIdReporte());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar reporte: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar un reporte (opcional)
    public boolean eliminarReporte(int idReporte) {
        String sql = "DELETE FROM ReporteMantenimiento WHERE IdReporte = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idReporte);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar reporte: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
