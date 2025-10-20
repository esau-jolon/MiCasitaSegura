package ModeloDAO;

import Config.Conexion;
import Modelo.Paqueteria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaqueteriaDAO {

    // 🔹 Registrar nuevo paquete
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

    // 🔹 Marcar paquete como entregado
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

    // 🔹 Listar paquetes activos con información adicional
    public List<Paqueteria> listarPaquetes() {
        List<Paqueteria> lista = new ArrayList<>();
        String sql = "SELECT p.*, "
                + "r.nombre AS nombreResidente, r.apellidos AS apellidoResidente, "
                + "a.nombre AS nombreAgente, a.apellidos AS apellidoAgente "
                + "FROM paqueteria p "
                + "LEFT JOIN usuarios r ON p.IdResidente = r.id_usuario "
                + "LEFT JOIN usuarios a ON p.IdAgenteRegistro = a.id_usuario "
                + "WHERE p.Activo = 1 ORDER BY p.FechaRecepcion DESC";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paqueteria p = new Paqueteria();
                p.setIdPaquete(rs.getInt("IdPaquete"));
                p.setNumeroGuia(rs.getString("NumeroGuia"));
                p.setIdResidente(rs.getInt("IdResidente"));
                p.setIdAgenteRegistro(rs.getInt("IdAgenteRegistro"));
                p.setFechaRecepcion(rs.getTimestamp("FechaRecepcion"));
                p.setFechaEntrega(rs.getTimestamp("FechaEntrega"));
                p.setEntregado(rs.getBoolean("Entregado"));
                p.setActivo(rs.getInt("Activo"));
                p.setCreadoPor(rs.getInt("CreadoPor"));
                p.setModificadoPor(rs.getInt("ModificadoPor"));
                p.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                p.setFechaModificacion(rs.getTimestamp("FechaModificacion"));
                p.setNombreResidente(rs.getString("nombreResidente"));
                p.setApellidoResidente(rs.getString("apellidoResidente"));
                p.setNombreAgente(rs.getString("nombreAgente"));
                p.setApellidoAgente(rs.getString("apellidoAgente"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Obtener un paquete por ID
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
                p.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                p.setFechaModificacion(rs.getTimestamp("FechaModificacion"));
                p.setActivo(rs.getInt("Activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    // 🔹 Eliminación lógica (borrado suave)
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

    // 🔹 Registrar acción en auditoría
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

    public List<Paqueteria> buscarPaquetes(String numeroGuia, String nombreResidente, String estado) {
        List<Paqueteria> lista = new ArrayList<>();

        // ✅ Consulta SQL clara y compatible con Java 8
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, u.nombre AS nombreResidente, u.apellidos AS apellidoResidente "
                + "FROM paqueteria p "
                + "INNER JOIN usuarios u ON p.IdResidente = u.id_usuario "
                + "WHERE 1=1 "
        );

        // 🔍 Filtros dinámicos
        if (numeroGuia != null && !numeroGuia.trim().isEmpty()) {
            sql.append(" AND p.NumeroGuia LIKE ? ");
        }
        if (nombreResidente != null && !nombreResidente.trim().isEmpty()) {
            sql.append(" AND (u.nombre LIKE ? OR u.apellidos LIKE ?) ");
        }
        if (estado != null && !estado.trim().isEmpty()) {
            if (estado.equalsIgnoreCase("pendiente")) {
                sql.append(" AND p.Entregado = 0 ");
            } else if (estado.equalsIgnoreCase("entregado")) {
                sql.append(" AND p.Entregado = 1 ");
            }
        }

        sql.append(" ORDER BY p.FechaRecepcion DESC ");

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;

            if (numeroGuia != null && !numeroGuia.trim().isEmpty()) {
                ps.setString(index++, "%" + numeroGuia + "%");
            }
            if (nombreResidente != null && !nombreResidente.trim().isEmpty()) {
                ps.setString(index++, "%" + nombreResidente + "%");
                ps.setString(index++, "%" + nombreResidente + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paqueteria p = new Paqueteria();
                    p.setIdPaquete(rs.getInt("IdPaquete"));
                    p.setNumeroGuia(rs.getString("NumeroGuia"));
                    p.setIdResidente(rs.getInt("IdResidente"));
                    p.setFechaRecepcion(rs.getTimestamp("FechaRecepcion"));
                    p.setFechaEntrega(rs.getTimestamp("FechaEntrega"));
                    p.setEntregado(rs.getBoolean("Entregado"));

                    // 🏠 Nombres del residente
                    p.setNombreResidente(rs.getString("nombreResidente"));
                    p.setApellidoResidente(rs.getString("apellidoResidente"));

                    lista.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] buscarPaquetes(): " + e.getMessage());
        }

        return lista;
    }

}
