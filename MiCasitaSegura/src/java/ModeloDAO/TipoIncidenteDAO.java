package ModeloDAO;

import Config.Conexion;
import Modelo.TipoIncidente;
import java.sql.*;
import java.util.*;

public class TipoIncidenteDAO {

    // 🔹 Listar todos los tipos de incidentes (para ComboBox)
    public List<TipoIncidente> listar() {
        List<TipoIncidente> lista = new ArrayList<>();
        String sql = "SELECT * FROM TipoIncidente ORDER BY nombre ASC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoIncidente t = new TipoIncidente();
                t.setIdTipoIncidente(rs.getInt("id_tipo_incidente"));
                t.setNombre(rs.getString("nombre"));
                t.setDescripcion(rs.getString("descripcion"));
                lista.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
