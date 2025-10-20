package ModeloDAO;

import Config.Conexion;
import Modelo.Paqueteria;
import java.sql.*;
import java.util.*;

public class PaqueteriaDAO {

    // 🔹 Registrar un nuevo paquete
    public boolean registrarPaquete(Paqueteria p, int usuarioId) {
        String sql = "INSERT INTO paqueteria (NumeroGuia, IdResidente, IdAgenteRegistro, CreadoPor) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNumeroGuia());
            ps.setInt(2, p.getIdResidente());
            ps.setInt(3, p.getIdAgenteRegistro());
            ps.setInt(4, usuarioId);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                registrarAccion(usuarioId, "Registró paquete con guía " + p.getNumeroGuia());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Marcar un paquete como entregado
    public boolean marcarEntregado(int idPaquete, int usuarioId) {
        String sql = "UPDATE paqueteria SET Entregado = 1, FechaEntrega = NOW(), ModificadoPor = ? WHERE IdPaquete = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setInt(2, idPaquete);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                registrarAccion(usuarioId, "Marcó como entregado el paquete ID " + idPaquete);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Listar todos los paquetes (con nombres del residente y agente)
    public List<Paqueteria> listarPaquetes() {
        List<Paqueteria> lista = new ArrayList<>();
        String sql =
            "SELECT " +
            "p.IdPaquete, p.NumeroGuia, p.FechaRecepcion, p.FechaEntrega, p.Entregado, " +
            "r.id_usuario AS idResidente, r.nombre AS nombreResidente, r.apellidos AS apellidoResidente, " +
            "a.id_usuario AS idAgente, a.nombre AS nombreAgente, a.apellidos AS apellidoAgente " +
            "FROM paqueteria p " +
            "LEFT JOIN usuarios r ON p.IdResidente = r.id_usuario " +
            "LEFT JOIN usuarios a ON p.IdAgenteRegistro = a.id_usuario " +
            "WHERE p.Activo = 1 " +
            "ORDER BY p.FechaRecepcion DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paqueteria p = new Paqueteria();
                p.setIdPaquete(rs.getInt("IdPaquete"));
                p.setNumeroGuia(rs.getString("NumeroGuia"));
                p.setFechaRecepcion(rs.getTimestamp("FechaRecepcion"));
                p.setFechaEntrega(rs.getTimestamp("FechaEntrega"));
                p.setEntregado(rs.getBoolean("Entregado"));
                p.setIdResidente(rs.getInt("idResidente"));
                p.setNombreResidente(rs.getString("nombreResidente"));
                p.setApellidoResidente(rs.getString("apellidoResidente"));
                p.setIdAgenteRegistro(rs.getInt("idAgente"));
                p.setNombreAgente(rs.getString("nombreAgente"));
                p.setApellidoAgente(rs.getString("apellidoAgente"));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Obtener paquete por ID
    public Paqueteria obtenerPorId(int idPaquete) {
        Paqueteria p = null;
        String sql = "SELECT * FROM paqueteria WHERE IdPaquete = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaquete);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new Paqueteria();
                p.setIdPaquete(rs.getInt("IdPaquete"));
                p.setNumeroGuia(rs.getString("NumeroGuia"));
                p.setIdResidente(rs.getInt("IdResidente"));
                p.setIdAgenteRegistro(rs.getInt("IdAgenteRegistro"));
                p.setFechaRecepcion(rs.getTimestamp("FechaRecepcion"));
                p.setFechaEntrega(rs.getTimestamp("FechaEntrega"));
                p.setEntregado(rs.getBoolean("Entregado"));
                p.setCreadoPor(rs.getInt("CreadoPor"));
                p.setModificadoPor(rs.getInt("ModificadoPor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    // 🔹 Eliminar (borrado lógico)
    public boolean eliminar(int idPaquete, int usuarioId) {
        String sql = "UPDATE paqueteria SET Activo = 0, ModificadoPor = ? WHERE IdPaquete = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setInt(2, idPaquete);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                registrarAccion(usuarioId, "Eliminó paquete ID " + idPaquete);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Auditoría (bitácora simple)
    private void registrarAccion(int usuarioId, String accion) {
        String sql = "INSERT INTO auditoria (usuario_id, accion) VALUES (?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setString(2, accion);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
