package ModeloDAO;

import Config.Conexion;
import Modelo.Pagos;
import java.sql.*;
import java.util.*;

public class PagoDAO {

    // 🔹 Listar solo pagos activos
    public List<Pagos> listar() {
        List<Pagos> lista = new ArrayList<>();
        String sql = "SELECT p.*, "
                + "u.nombre AS nombreUsuario, "
                + "tp.nombre AS nombreTipoPago, "
                + "ep.descripcion AS descripcionEstadoPago "
                + "FROM Pagos p "
                + "INNER JOIN Usuarios u ON p.id_usuario = u.id_usuario "
                + "INNER JOIN TiposPago tp ON p.id_tipo_pago = tp.id_tipo_pago "
                + "INNER JOIN EstadosPago ep ON p.id_estado_pago = ep.IdEstadoPago "
                + "WHERE p.activo = TRUE";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pagos p = new Pagos();
                p.setIdPago(rs.getInt("id_pago"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                p.setIdTipoPago(rs.getInt("id_tipo_pago"));
                p.setIdEstadoPago(rs.getInt("id_estado_pago"));
                p.setFechaPago(rs.getDate("fecha_pago"));
                p.setMonto(rs.getDouble("monto"));
                p.setMora(rs.getDouble("mora"));
                p.setTotal(rs.getDouble("total"));
                p.setObservaciones(rs.getString("observaciones"));
                p.setMesPagado(rs.getObject("mes_pagado", Integer.class));
                p.setAnioPagado(rs.getObject("anio_pagado", Integer.class));
                p.setActivo(rs.getBoolean("activo"));

                // 🧾 Auditoría
                p.setCreadoPor(rs.getInt("creado_por"));
                p.setModificadoPor(rs.getObject("modificado_por", Integer.class));
                p.setFechaCreacion(rs.getDate("fecha_creacion"));
                p.setFechaModificacion(rs.getDate("fecha_modificacion"));

                p.setNombreUsuario(rs.getString("nombreUsuario"));
                p.setNombreTipoPago(rs.getString("nombreTipoPago"));
                p.setNombreEstadoPago(rs.getString("descripcionEstadoPago"));

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Obtener pago por ID
    public Pagos listarId(int id) {
        String sql = "SELECT * FROM Pagos WHERE id_pago=? AND activo=TRUE";
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
                    p.setIdEstadoPago(rs.getInt("id_estado_pago"));
                    p.setFechaPago(rs.getDate("fecha_pago"));
                    p.setMonto(rs.getDouble("monto"));
                    p.setMora(rs.getDouble("mora"));
                    p.setTotal(rs.getDouble("total"));
                    p.setObservaciones(rs.getString("observaciones"));
                    p.setMesPagado(rs.getObject("mes_pagado", Integer.class));
                    p.setAnioPagado(rs.getObject("anio_pagado", Integer.class));
                    p.setActivo(rs.getBoolean("activo"));
                    p.setCreadoPor(rs.getInt("creado_por"));
                    p.setModificadoPor(rs.getObject("modificado_por", Integer.class));
                    p.setFechaCreacion(rs.getDate("fecha_creacion"));
                    p.setFechaModificacion(rs.getDate("fecha_modificacion"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    // 🔹 Agregar pago
    public boolean add(Pagos p) {
        String sql = "INSERT INTO Pagos("
                + "id_usuario, id_tipo_pago, id_estado_pago, fecha_pago, monto, mora, total, observaciones, "
                + "mes_pagado, anio_pagado, activo, creado_por) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getIdUsuario());
            ps.setInt(2, p.getIdTipoPago());
            ps.setInt(3, p.getIdEstadoPago());
            ps.setDate(4, p.getFechaPago());
            ps.setDouble(5, p.getMonto());
            ps.setDouble(6, p.getMora());
            ps.setDouble(7, p.getTotal());
            ps.setString(8, p.getObservaciones());
            ps.setObject(9, p.getMesPagado(), Types.INTEGER);
            ps.setObject(10, p.getAnioPagado(), Types.INTEGER);
            ps.setBoolean(11, p.isActivo());
            ps.setInt(12, p.getCreadoPor());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Editar pago
    public boolean edit(Pagos p) {
        String sql = "UPDATE Pagos SET "
                + "id_usuario=?, id_tipo_pago=?, id_estado_pago=?, fecha_pago=?, monto=?, mora=?, total=?, observaciones=?, "
                + "mes_pagado=?, anio_pagado=?, activo=?, modificado_por=? "
                + "WHERE id_pago=?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getIdUsuario());
            ps.setInt(2, p.getIdTipoPago());
            ps.setInt(3, p.getIdEstadoPago());
            ps.setDate(4, p.getFechaPago());
            ps.setDouble(5, p.getMonto());
            ps.setDouble(6, p.getMora());
            ps.setDouble(7, p.getTotal());
            ps.setString(8, p.getObservaciones());
            ps.setObject(9, p.getMesPagado(), Types.INTEGER);
            ps.setObject(10, p.getAnioPagado(), Types.INTEGER);
            ps.setBoolean(11, p.isActivo());
            ps.setObject(12, p.getModificadoPor(), Types.INTEGER);
            ps.setInt(13, p.getIdPago());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar lógico (borrado suave)
    public boolean eliminarLogico(int idPago) {
        String sql = "UPDATE Pagos SET activo = FALSE WHERE id_pago = ?";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPago);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Restaurar un pago eliminado
    public boolean restaurar(int idPago) {
        String sql = "UPDATE Pagos SET activo = TRUE WHERE id_pago = ?";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPago);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cambiar estado a “Cancelado”
    public boolean cancelar(int id) {
        String sql = "UPDATE Pagos SET id_estado_pago = (SELECT id_estado_pago FROM EstadosPago WHERE descripcion='Cancelado' LIMIT 1) WHERE id_pago=? AND activo=TRUE";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cambiar estado de un pago (por ID de estado y usuario que modifica)
    public boolean cambiarEstado(int idPago, int nuevoEstadoId, int idUsuarioModifica) {
        String sql = "UPDATE Pagos "
                + "SET id_estado_pago = ?, "
                + "modificado_por = ?, "
                + "fecha_modificacion = CURRENT_TIMESTAMP "
                + "WHERE id_pago = ? AND activo = TRUE";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoEstadoId);
            ps.setInt(2, idUsuarioModifica); // 👈 guarda quién hizo el cambio
            ps.setInt(3, idPago);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Obtener el mes siguiente (sin cambios)
    public String obtenerMesSiguiente(int idUsuario) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        String sqlUltimoPago = "SELECT MAX(fecha_pago) AS ultima FROM Pagos WHERE id_usuario=? AND id_tipo_pago=1 AND activo=TRUE";
        String sqlFechaCreacion = "SELECT FechaCreacion FROM Usuarios WHERE id_usuario=?";

        try (Connection con = Conexion.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sqlUltimoPago)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();

                if (rs.next() && rs.getDate("ultima") != null) {
                    java.sql.Date ultima = rs.getDate("ultima");
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(ultima);
                    cal.add(java.util.Calendar.MONTH, 1);
                    return meses[cal.get(java.util.Calendar.MONTH)];
                }
            }

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

        return meses[new java.util.Date().getMonth()];
    }
}
