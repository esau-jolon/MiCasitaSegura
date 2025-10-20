package ModeloDAO;

import Config.Conexion;
import Modelo.Reservas;
import java.sql.*;
import java.util.*;

public class ReservasDAO {

    // 🔹 Crear nueva reserva
    public boolean crearReserva(Reservas r) {
        String sql = "INSERT INTO reservas (IdArea, IdResidente, FechaReserva, HoraInicio, HoraFin, Observaciones, CreadoPor) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdArea());
            ps.setInt(2, r.getIdResidente());
            ps.setDate(3, r.getFechaReserva());
            ps.setTime(4, r.getHoraInicio());
            ps.setTime(5, r.getHoraFin());
            ps.setString(6, r.getObservaciones());
            ps.setString(7, r.getCreadoPor());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al crear reserva: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Obtener una reserva por su ID
    public Reservas obtenerPorId(int idReserva) {
        Reservas r = null;
        String sql = "SELECT r.*, a.Nombre AS NombreArea, e.NombreEstado, "
                + "u.nombre AS NombreResidente, u.apellidos AS ApellidoResidente "
                + "FROM reservas r "
                + "INNER JOIN areas_comunes a ON r.IdArea = a.IdArea "
                + "INNER JOIN estado_reserva e ON r.IdEstadoReserva = e.IdEstadoReserva "
                + "INNER JOIN usuarios u ON r.IdResidente = u.id_usuario "
                + "WHERE r.IdReserva = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                r = new Reservas();
                r.setIdReserva(rs.getInt("IdReserva"));
                r.setIdArea(rs.getInt("IdArea"));
                r.setIdResidente(rs.getInt("IdResidente"));
                r.setFechaReserva(rs.getDate("FechaReserva"));
                r.setHoraInicio(rs.getTime("HoraInicio"));
                r.setHoraFin(rs.getTime("HoraFin"));
                r.setIdEstadoReserva(rs.getInt("IdEstadoReserva"));
                r.setObservaciones(rs.getString("Observaciones"));
                r.setNombreArea(rs.getString("NombreArea"));
                r.setNombreEstado(rs.getString("NombreEstado"));
                r.setNombreResidente(rs.getString("NombreResidente"));
                r.setApellidoResidente(rs.getString("ApellidoResidente"));
                r.setActivo(rs.getBoolean("Activo"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener reserva por ID: " + e.getMessage());
        }

        return r;
    }

    // 🔹 Listar todas las reservas (vista admin)
    public List<Reservas> listar() {
        List<Reservas> lista = new ArrayList<>();
        String sql = "SELECT r.*, a.Nombre AS NombreArea, e.NombreEstado, "
                + "u.nombre AS NombreResidente, u.apellidos AS ApellidoResidente "
                + "FROM reservas r "
                + "INNER JOIN areas_comunes a ON r.IdArea = a.IdArea "
                + "INNER JOIN estado_reserva e ON r.IdEstadoReserva = e.IdEstadoReserva "
                + "INNER JOIN usuarios u ON r.IdResidente = u.id_usuario "
                + "WHERE r.Activo = TRUE "
                + "ORDER BY r.IdReserva ASC"; // ✅ De menor a mayor ID

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reservas r = new Reservas();
                r.setIdReserva(rs.getInt("IdReserva"));
                r.setIdArea(rs.getInt("IdArea"));
                r.setIdResidente(rs.getInt("IdResidente"));
                r.setFechaReserva(rs.getDate("FechaReserva"));
                r.setHoraInicio(rs.getTime("HoraInicio"));
                r.setHoraFin(rs.getTime("HoraFin"));
                r.setIdEstadoReserva(rs.getInt("IdEstadoReserva"));
                r.setObservaciones(rs.getString("Observaciones"));
                r.setNombreArea(rs.getString("NombreArea"));
                r.setNombreEstado(rs.getString("NombreEstado"));
                r.setNombreResidente(rs.getString("NombreResidente"));
                r.setApellidoResidente(rs.getString("ApellidoResidente"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar reservas: " + e.getMessage());
        }
        return lista;
    }

    // 🔹 Listar reservas por residente (corrigido con JOIN y nombres)
    public List<Reservas> listarPorResidente(int idResidente) {
        List<Reservas> lista = new ArrayList<>();
        String sql = "SELECT r.*, a.Nombre AS NombreArea, e.NombreEstado, "
                + "u.nombre AS NombreResidente, u.apellidos AS ApellidoResidente "
                + "FROM reservas r "
                + "INNER JOIN areas_comunes a ON r.IdArea = a.IdArea "
                + "INNER JOIN estado_reserva e ON r.IdEstadoReserva = e.IdEstadoReserva "
                + "INNER JOIN usuarios u ON r.IdResidente = u.id_usuario "
                + "WHERE r.IdResidente = ? AND r.Activo = TRUE "
                + "ORDER BY r.IdReserva ASC"; // ✅ De menor a mayor ID

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idResidente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reservas r = new Reservas();
                r.setIdReserva(rs.getInt("IdReserva"));
                r.setIdArea(rs.getInt("IdArea"));
                r.setFechaReserva(rs.getDate("FechaReserva"));
                r.setHoraInicio(rs.getTime("HoraInicio"));
                r.setHoraFin(rs.getTime("HoraFin"));
                r.setNombreArea(rs.getString("NombreArea"));
                r.setNombreEstado(rs.getString("NombreEstado"));
                r.setNombreResidente(rs.getString("NombreResidente"));
                r.setApellidoResidente(rs.getString("ApellidoResidente"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar reservas por residente: " + e.getMessage());
        }
        return lista;
    }

    // 🔹 Confirmar reserva (cambia estado a 2)
    public boolean confirmarReserva(int idReserva, String usuario) {
        String sql = "UPDATE reservas SET IdEstadoReserva = 2, ModificadoPor = ?, FechaModificacion = CURRENT_TIMESTAMP WHERE IdReserva = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setInt(2, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al confirmar reserva: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Cancelar reserva (cambia estado a 3)
    public boolean cancelarReserva(int idReserva, String usuario) {
        String sql = "UPDATE reservas SET IdEstadoReserva = 3, ModificadoPor = ?, FechaModificacion = CURRENT_TIMESTAMP WHERE IdReserva = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setInt(2, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al cancelar reserva: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Verificar conflictos de horario
    public boolean existeReservaEnHorario(int idArea, java.sql.Date fecha, java.sql.Time horaInicio, java.sql.Time horaFin) {
        String sql = "SELECT COUNT(*) FROM reservas "
                + "WHERE IdArea = ? AND FechaReserva = ? "
                + "AND ((HoraInicio < ? AND HoraFin > ?) OR (HoraInicio < ? AND HoraFin > ?)) "
                + "AND IdEstadoReserva != 3"; // 3 = Cancelada

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idArea);
            ps.setDate(2, fecha);
            ps.setTime(3, horaFin);
            ps.setTime(4, horaInicio);
            ps.setTime(5, horaFin);
            ps.setTime(6, horaInicio);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al verificar horario de reserva: " + e.getMessage());
        }
        return false;
    }
}
