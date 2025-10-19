package ModeloDAO;

import Config.Conexion;
import Modelo.EstadoReserva;
import java.sql.*;
import java.util.*;

public class EstadoReservaDAO {

    // 🔹 Listar todos los estados activos
    public List<EstadoReserva> listarActivos() {
        List<EstadoReserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM estado_reserva WHERE Activo = TRUE ORDER BY IdEstadoReserva ASC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoReserva e = new EstadoReserva();
                e.setIdEstadoReserva(rs.getInt("IdEstadoReserva"));
                e.setNombreEstado(rs.getString("NombreEstado"));
                e.setDescripcion(rs.getString("Descripcion"));
                e.setCreadoPor(rs.getString("CreadoPor"));
                e.setModificadoPor(rs.getString("ModificadoPor"));
                e.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                e.setFechaModificacion(rs.getTimestamp("FechaModificacion"));
                e.setActivo(rs.getBoolean("Activo"));
                lista.add(e);
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al listar estados de reserva: " + ex.getMessage());
        }
        return lista;
    }

    // 🔹 Buscar estado por ID
    public EstadoReserva buscarPorId(int idEstado) {
        EstadoReserva e = null;
        String sql = "SELECT * FROM estado_reserva WHERE IdEstadoReserva = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEstado);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                e = new EstadoReserva();
                e.setIdEstadoReserva(rs.getInt("IdEstadoReserva"));
                e.setNombreEstado(rs.getString("NombreEstado"));
                e.setDescripcion(rs.getString("Descripcion"));
                e.setCreadoPor(rs.getString("CreadoPor"));
                e.setModificadoPor(rs.getString("ModificadoPor"));
                e.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                e.setFechaModificacion(rs.getTimestamp("FechaModificacion"));
                e.setActivo(rs.getBoolean("Activo"));
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al buscar estado de reserva: " + ex.getMessage());
        }
        return e;
    }

    // 🔹 Crear nuevo estado
    public boolean crearEstado(EstadoReserva e) {
        String sql = "INSERT INTO estado_reserva (NombreEstado, Descripcion, CreadoPor) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombreEstado());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getCreadoPor());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ Error al crear estado de reserva: " + ex.getMessage());
            return false;
        }
    }

    // 🔹 Actualizar estado
    public boolean actualizarEstado(EstadoReserva e) {
        String sql = "UPDATE estado_reserva SET NombreEstado = ?, Descripcion = ?, "
                   + "ModificadoPor = ?, FechaModificacion = CURRENT_TIMESTAMP WHERE IdEstadoReserva = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombreEstado());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getModificadoPor());
            ps.setInt(4, e.getIdEstadoReserva());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ Error al actualizar estado de reserva: " + ex.getMessage());
            return false;
        }
    }

    // 🔹 Desactivar estado (borrado lógico)
    public boolean desactivarEstado(int idEstado, String usuario) {
        String sql = "UPDATE estado_reserva SET Activo = FALSE, ModificadoPor = ?, FechaModificacion = CURRENT_TIMESTAMP WHERE IdEstadoReserva = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setInt(2, idEstado);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("❌ Error al desactivar estado de reserva: " + ex.getMessage());
            return false;
        }
    }
}
