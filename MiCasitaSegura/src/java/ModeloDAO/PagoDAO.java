package ModeloDAO;

import Config.Conexion;
import Modelo.Pagos;

import java.sql.*;
import java.util.*;

public class PagoDAO {

    // Listar todos los pagos
    public List<Pagos> listar() {
        List<Pagos> lista = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre AS nombreUsuario, tp.nombre AS nombreTipoPago "
                + "FROM Pagos p "
                + "INNER JOIN Usuarios u ON p.id_usuario = u.id_usuario "
                + "INNER JOIN TiposPago tp ON p.id_tipo_pago = tp.id_tipo_pago";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pagos p = new Pagos();
                p.setIdPago(rs.getInt("id_pago"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                p.setIdTipoPago(rs.getInt("id_tipo_pago"));
                p.setFechaPago(rs.getDate("fecha_pago"));
                p.setMonto(rs.getDouble("monto"));
                p.setMora(rs.getDouble("mora"));
                p.setTotal(rs.getDouble("total"));
                p.setObservaciones(rs.getString("observaciones"));
                p.setEstado(rs.getString("estado"));
                p.setNombreUsuario(rs.getString("nombreUsuario"));
                p.setNombreTipoPago(rs.getString("nombreTipoPago"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Obtener pago por ID
    public Pagos listarId(int id) {
        String sql = "SELECT * FROM Pagos WHERE id_pago=?";
        Pagos p = null;

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Pagos();
                    p.setIdPago(rs.getInt("id_pago"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                    p.setIdTipoPago(rs.getInt("id_tipo_pago"));
                    p.setFechaPago(rs.getDate("fecha_pago"));
                    p.setMonto(rs.getDouble("monto"));
                    p.setMora(rs.getDouble("mora"));
                    p.setTotal(rs.getDouble("total"));
                    p.setObservaciones(rs.getString("observaciones"));
                    p.setEstado(rs.getString("estado"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    // Agregar pago
    public boolean add(Pagos p) {
        String sql = "INSERT INTO Pagos(id_usuario, id_tipo_pago, fecha_pago, monto, mora, total, observaciones, estado) "
                + "VALUES(?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getIdUsuario());
            ps.setInt(2, p.getIdTipoPago());
            ps.setDate(3, p.getFechaPago());
            ps.setDouble(4, p.getMonto());
            ps.setDouble(5, p.getMora());
            ps.setDouble(6, p.getTotal());
            ps.setString(7, p.getObservaciones());
            ps.setString(8, p.getEstado());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Editar pago
    public boolean edit(Pagos p) {
        String sql = "UPDATE Pagos SET id_usuario=?, id_tipo_pago=?, fecha_pago=?, monto=?, mora=?, total=?, observaciones=?, estado=? WHERE id_pago=?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getIdUsuario());
            ps.setInt(2, p.getIdTipoPago());
            ps.setDate(3, p.getFechaPago());
            ps.setDouble(4, p.getMonto());
            ps.setDouble(5, p.getMora());
            ps.setDouble(6, p.getTotal());
            ps.setString(7, p.getObservaciones());
            ps.setString(8, p.getEstado());
            ps.setInt(9, p.getIdPago());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cancelar pago (estado = Cancelado)
    public boolean cancelar(int id) {
        String sql = "UPDATE Pagos SET estado='Cancelado' WHERE id_pago=?";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String obtenerMesSiguiente(int idUsuario) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String sqlUltimoPago = "SELECT MAX(fecha_pago) AS ultima FROM Pagos WHERE id_usuario=? AND id_tipo_pago=1"; // 1 = Mantenimiento
        String sqlFechaCreacion = "SELECT FechaCreacion FROM Usuarios WHERE id_usuario=?";

        try (Connection con = Conexion.getConnection()) {
            // 🔹 Primero revisamos el último pago
            try (PreparedStatement ps = con.prepareStatement(sqlUltimoPago)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getDate("ultima") != null) {
                    java.sql.Date ultima = rs.getDate("ultima");
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(ultima);
                    cal.add(java.util.Calendar.MONTH, 1); // siguiente mes
                    return meses[cal.get(java.util.Calendar.MONTH)];
                }
            }

            // 🔹 Si no hay pagos, usamos la fecha de creación del usuario
            try (PreparedStatement ps = con.prepareStatement(sqlFechaCreacion)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getDate("FechaCreacion") != null) {
                    java.sql.Date fechaCreacion = rs.getDate("FechaCreacion");
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(fechaCreacion);
                    return meses[cal.get(java.util.Calendar.MONTH)];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔹 Si todo falla, devolvemos el mes actual
        return meses[new java.util.Date().getMonth()];
    }

}
