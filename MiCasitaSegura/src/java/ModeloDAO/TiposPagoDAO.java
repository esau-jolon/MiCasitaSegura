package ModeloDAO;

import Config.Conexion;
import Modelo.TiposPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TiposPagoDAO {

    // 🔹 Listar todos los tipos de pago (para el catálogo)
    public List<TiposPago> listar() {
        List<TiposPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM TiposPago";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TiposPago tp = new TiposPago();
                tp.setIdTipoPago(rs.getInt("id_tipo_pago"));
                tp.setNombre(rs.getString("nombre"));
                tp.setMonto(rs.getDouble("monto"));
                lista.add(tp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Buscar tipo de pago por ID
    public TiposPago listarId(int id) {
        TiposPago tp = null;
        String sql = "SELECT * FROM TiposPago WHERE id_tipo_pago = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tp = new TiposPago();
                    tp.setIdTipoPago(rs.getInt("id_tipo_pago"));
                    tp.setNombre(rs.getString("nombre"));
                    tp.setMonto(rs.getDouble("monto"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tp;
    }

    // 🔹 Agregar un tipo de pago nuevo (opcional)
    public boolean add(TiposPago tp) {
        String sql = "INSERT INTO TiposPago(nombre, monto) VALUES(?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tp.getNombre());
            ps.setDouble(2, tp.getMonto());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Editar un tipo de pago existente
    public boolean edit(TiposPago tp) {
        String sql = "UPDATE TiposPago SET nombre=?, monto=? WHERE id_tipo_pago=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tp.getNombre());
            ps.setDouble(2, tp.getMonto());
            ps.setInt(3, tp.getIdTipoPago());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar un tipo de pago
    public boolean delete(int id) {
        String sql = "DELETE FROM TiposPago WHERE id_tipo_pago=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
