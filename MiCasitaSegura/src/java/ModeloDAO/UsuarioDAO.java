package ModeloDAO;

import Config.Conexion;
import Intefaces.UsuarioCrud;
import Modelo.Usuarios;
import java.sql.*;
import java.util.*;

public class UsuarioDAO implements UsuarioCrud {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Override
    public List<Usuarios> listar() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setDpi(rs.getString("dpi"));
                u.setNombre(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRolId(rs.getInt("rol_id"));
                u.setNumeroCasaId(rs.getInt("numero_casa_id"));
                u.setLoteId(rs.getInt("lote_id"));
                u.setEstado(rs.getBoolean("estado"));
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Usuarios listarId(int id) {
        String sql = "SELECT * FROM Usuarios WHERE id_usuario=?";
        Usuarios u = new Usuarios();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setDpi(rs.getString("dpi"));
                u.setNombre(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRolId(rs.getInt("rol_id"));
                u.setNumeroCasaId(rs.getInt("numero_casa_id"));
                u.setLoteId(rs.getInt("lote_id"));
                u.setEstado(rs.getBoolean("estado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public boolean add(Usuarios u) {
        String sql = "INSERT INTO Usuarios(dpi,nombre,apellidos,correo,contrasena,rol_id,numero_casa_id,lote_id,estado) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, u.getDpi());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellidos());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getContrasena());
            ps.setInt(6, u.getRolId());
            ps.setObject(7, u.getNumeroCasaId());
            ps.setObject(8, u.getLoteId());
            ps.setBoolean(9, u.isEstado());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean edit(Usuarios u) {
        String sql = "UPDATE Usuarios SET dpi=?,nombre=?,apellidos=?,correo=?,contrasena=?,rol_id=?,numero_casa_id=?,lote_id=?,estado=? WHERE id_usuario=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, u.getDpi());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellidos());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getContrasena());
            ps.setInt(6, u.getRolId());
            ps.setObject(7, u.getNumeroCasaId());
            ps.setObject(8, u.getLoteId());
            ps.setBoolean(9, u.isEstado());
            ps.setInt(10, u.getIdUsuario());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Usuarios WHERE id_usuario=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    public boolean puedeAbrirTalanquera(int idUsuario) {
        String sql = "SELECT estado FROM Codigos_QR WHERE id_usuario = ? ORDER BY id_qr DESC LIMIT 1";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = cn.getConnection(); // 👈 Igual que en add()
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                boolean estado = rs.getBoolean("estado");
                // Si estado = false (0) -> puede abrir
                return !estado;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false; // por defecto no abre
    }
     */
    public boolean puedeAbrirTalanquera(int idUsuario) {
        String selectSql = "SELECT id_qr, estado FROM Codigos_QR WHERE id_usuario = ? ORDER BY id_qr DESC LIMIT 1";
        String updateSql = "UPDATE Codigos_QR SET estado = ? WHERE id_qr = ?";

        Connection con = null;
        PreparedStatement psSelect = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;

        try {
            con = cn.getConnection();

            // Consulta el último estado
            psSelect = con.prepareStatement(selectSql);
            psSelect.setInt(1, idUsuario);
            rs = psSelect.executeQuery();

            if (rs.next()) {
                int idQr = rs.getInt("id_qr");
                boolean estadoActual = rs.getBoolean("estado");

                boolean nuevoEstado = !estadoActual; // Alternar: si estaba 0 → 1, si estaba 1 → 0

                // Actualizar el estado
                psUpdate = con.prepareStatement(updateSql);
                psUpdate.setBoolean(1, nuevoEstado);
                psUpdate.setInt(2, idQr);
                psUpdate.executeUpdate();

                // Si el estado actual era 0, el usuario estaba "afuera", entonces ahora entra (estado 1)
                // Devuelve true si el usuario puede pasar (cuando estaba fuera, o sea estado 0)
                return !estadoActual;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (psSelect != null) {
                    psSelect.close();
                }
                if (psUpdate != null) {
                    psUpdate.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return false; // por defecto, no puede abrir
    }

    public boolean existeUsuario(String dpi, String correo) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE dpi=? OR correo=?";
        try (Connection con = cn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dpi);
            ps.setString(2, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
