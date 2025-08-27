package ModeloDAO;

import Config.Conexion;
import Modelo.Casas;
import java.sql.*;
import java.util.*;

public class CasaDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<Casas> listar() {
        List<Casas> lista = new ArrayList<>();
        String sql = "SELECT * FROM Casas";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Casas c = new Casas();
                c.setIdCasa(rs.getInt("id_casa"));
                c.setNumeroCasa(rs.getInt("numero_casa"));
                lista.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Casas listarId(int id) {
        String sql = "SELECT * FROM Casas WHERE id_casa=?";
        Casas c = new Casas();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                c.setIdCasa(rs.getInt("id_casa"));
                c.setNumeroCasa(rs.getInt("numero_casa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
}
