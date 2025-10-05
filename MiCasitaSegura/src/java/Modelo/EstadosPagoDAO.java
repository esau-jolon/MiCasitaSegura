package ModeloDAO;

import Config.Conexion;
import Modelo.EstadosPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadosPagoDAO {

    // 🔹 Listar todos los estados de pago (para el combo en JSP)
    public List<EstadosPago> listar() {
        List<EstadosPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM EstadosPago ORDER BY IdEstadoPago";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadosPago ep = new EstadosPago();
                ep.setIdEstadoPago(rs.getInt("IdEstadoPago"));
                ep.setDescripcion(rs.getString("Descripcion"));
                lista.add(ep);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Buscar un estado por ID
    public EstadosPago listarId(int id) {
        EstadosPago ep = null;
        String sql = "SELECT * FROM EstadosPago WHERE IdEstadoPago = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ep = new EstadosPago();
                    ep.setIdEstadoPago(rs.getInt("IdEstadoPago"));
                    ep.setDescripcion(rs.getString("Descripcion"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ep;
    }

    // 🔹 Agregar un nuevo estado de pago
    public boolean add(EstadosPago ep) {
        String sql = "INSERT INTO EstadosPago (Descripcion) VALUES (?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ep.getDescripcion());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Editar un estado existente
    public boolean edit(EstadosPago ep) {
        String sql = "UPDATE EstadosPago SET Descripcion=? WHERE IdEstadoPago=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ep.getDescripcion());
            ps.setInt(2, ep.getIdEstadoPago());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar un estado
    public boolean delete(int id) {
        String sql = "DELETE FROM EstadosPago WHERE IdEstadoPago=?";

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
