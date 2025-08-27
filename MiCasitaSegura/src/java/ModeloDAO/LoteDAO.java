package ModeloDAO;

import Config.Conexion;
import Modelo.Lotes;
import java.sql.*;
import java.util.*;

public class LoteDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<Lotes> listar() {
        List<Lotes> lista = new ArrayList<>();
        String sql = "SELECT * FROM Lotes";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Lotes l = new Lotes();
                l.setIdLote(rs.getInt("id_lote"));
                l.setCodigoLote(rs.getString("codigo_lote"));
                lista.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Lotes listarId(int id) {
        String sql = "SELECT * FROM Lotes WHERE id_lote=?";
        Lotes l = new Lotes();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                l.setIdLote(rs.getInt("id_lote"));
                l.setCodigoLote(rs.getString("codigo_lote"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }
}
