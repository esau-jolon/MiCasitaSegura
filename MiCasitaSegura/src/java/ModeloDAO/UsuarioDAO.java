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
                u.setContrasena(rs.getString("contraseña"));
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
                u.setContrasena(rs.getString("contraseña"));
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
        String sql = "INSERT INTO Usuarios(dpi,nombre,apellidos,correo,contraseña,rol_id,numero_casa_id,lote_id,estado) VALUES(?,?,?,?,?,?,?,?,?)";
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
        String sql = "UPDATE Usuarios SET dpi=?,nombre=?,apellidos=?,correo=?,contraseña=?,rol_id=?,numero_casa_id=?,lote_id=?,estado=? WHERE id_usuario=?";
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
}
