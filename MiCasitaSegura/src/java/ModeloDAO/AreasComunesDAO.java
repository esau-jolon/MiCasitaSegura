package ModeloDAO;

import Config.Conexion;
import Modelo.AreasComunes;
import java.sql.*;
import java.util.*;

public class AreasComunesDAO {

    // 🔹 Listar todas las áreas activas
    public List<AreasComunes> listarActivas() {
        List<AreasComunes> lista = new ArrayList<>();
        String sql = "SELECT * FROM areas_comunes WHERE Activo = TRUE ORDER BY Nombre ASC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AreasComunes a = new AreasComunes();
                a.setIdArea(rs.getInt("IdArea"));
                a.setNombre(rs.getString("Nombre"));
                a.setDescripcion(rs.getString("Descripcion"));
                a.setCapacidad(rs.getInt("Capacidad"));
                a.setCreadoPor(rs.getString("CreadoPor"));
                a.setModificadoPor(rs.getString("ModificadoPor"));
                a.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                a.setFechaModificacion(rs.getTimestamp("FechaModificacion"));
                a.setActivo(rs.getBoolean("Activo"));
                lista.add(a);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar áreas comunes: " + e.getMessage());
        }
        return lista;
    }

    // 🔹 Buscar área por ID
    public AreasComunes buscarPorId(int idArea) {
        AreasComunes a = null;
        String sql = "SELECT * FROM areas_comunes WHERE IdArea = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idArea);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                a = new AreasComunes();
                a.setIdArea(rs.getInt("IdArea"));
                a.setNombre(rs.getString("Nombre"));
                a.setDescripcion(rs.getString("Descripcion"));
                a.setCapacidad(rs.getInt("Capacidad"));
                a.setCreadoPor(rs.getString("CreadoPor"));
                a.setModificadoPor(rs.getString("ModificadoPor"));
                a.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                a.setFechaModificacion(rs.getTimestamp("FechaModificacion"));
                a.setActivo(rs.getBoolean("Activo"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar área común: " + e.getMessage());
        }
        return a;
    }

    // 🔹 Crear nueva área común
    public boolean crearArea(AreasComunes a) {
        String sql = "INSERT INTO areas_comunes (Nombre, Descripcion, Capacidad, CreadoPor) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getNombre());
            ps.setString(2, a.getDescripcion());
            ps.setInt(3, a.getCapacidad());
            ps.setString(4, a.getCreadoPor());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al crear área común: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Actualizar área común
    public boolean actualizarArea(AreasComunes a) {
        String sql = "UPDATE areas_comunes SET Nombre = ?, Descripcion = ?, Capacidad = ?, "
                   + "ModificadoPor = ?, FechaModificacion = CURRENT_TIMESTAMP WHERE IdArea = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getNombre());
            ps.setString(2, a.getDescripcion());
            ps.setInt(3, a.getCapacidad());
            ps.setString(4, a.getModificadoPor());
            ps.setInt(5, a.getIdArea());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar área común: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Desactivar (borrado lógico)
    public boolean desactivarArea(int idArea, String usuario) {
        String sql = "UPDATE areas_comunes SET Activo = FALSE, ModificadoPor = ?, FechaModificacion = CURRENT_TIMESTAMP WHERE IdArea = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setInt(2, idArea);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al desactivar área común: " + e.getMessage());
            return false;
        }
    }
}
