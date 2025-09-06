/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDAO;

/**
 *
 * @author esauj
 */

import Config.Conexion;
import Controlador.EmailSender;
import Controlador.QRGenerator;
import Modelo.Visitas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitaDAO {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // 🔹 Listar todas las visitas
    public List<Visitas> listar() {
        List<Visitas> lista = new ArrayList<>();
        String sql = "SELECT * FROM Visitas";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Visitas v = new Visitas();
                v.setIdVisita(rs.getInt("id_visita"));
                v.setNombreVisitante(rs.getString("nombre_visitante"));
                v.setDpiVisitante(rs.getString("dpi_visitante"));
                v.setCorreoVisitante(rs.getString("correo_visitante"));
                v.setIdResidente(rs.getInt("id_residente"));
                v.setTipoVisita(rs.getString("tipo_visita"));
                v.setFechaVisita(rs.getDate("fecha_visita"));
                v.setIntentosPermitidos(rs.getInt("intentos_permitidos"));
                v.setEstado(rs.getBoolean("estado"));
                lista.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Obtener una visita por ID
    public Visitas listarId(int id) {
        String sql = "SELECT * FROM Visitas WHERE id_visita=?";
        Visitas v = new Visitas();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                v.setIdVisita(rs.getInt("id_visita"));
                v.setNombreVisitante(rs.getString("nombre_visitante"));
                v.setDpiVisitante(rs.getString("dpi_visitante"));
                v.setCorreoVisitante(rs.getString("correo_visitante"));
                v.setIdResidente(rs.getInt("id_residente"));
                v.setTipoVisita(rs.getString("tipo_visita"));
                v.setFechaVisita(rs.getDate("fecha_visita"));
                v.setIntentosPermitidos(rs.getInt("intentos_permitidos"));
                v.setEstado(rs.getBoolean("estado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    // 🔹 Agregar nueva visita + generar QR
    public boolean add(Visitas v) {
        String sql = "INSERT INTO Visitas(nombre_visitante,dpi_visitante,correo_visitante,id_residente,tipo_visita,fecha_visita,intentos_permitidos,estado) VALUES(?,?,?,?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, v.getNombreVisitante());
            ps.setString(2, v.getDpiVisitante());
            ps.setString(3, v.getCorreoVisitante());
            ps.setInt(4, v.getIdResidente());
            ps.setString(5, v.getTipoVisita());
            ps.setDate(6, v.getFechaVisita());
            ps.setObject(7, v.getIntentosPermitidos());
            ps.setBoolean(8, v.isEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idVisita = rs.getInt(1);

                // Prefijo para visitantes
                String codigo = "VIS-" + idVisita;

                // Generar QR
                byte[] qrBytes = QRGenerator.generarQR(codigo, 250, 250);

                // Guardar QR en Codigos_QR
                String sqlQR = "INSERT INTO Codigos_QR(codigo, fecha_inicio, fecha_fin, intentos_disponibles, id_visita, estado) "
                        + "VALUES(?, NOW(), ?, ?, ?, 1)";
                PreparedStatement psQR = con.prepareStatement(sqlQR);

                // Si es visita por fecha
                if ("Visita".equalsIgnoreCase(v.getTipoVisita())) {
                    psQR.setString(1, codigo);
                    psQR.setDate(2, v.getFechaVisita()); // fecha límite
                    psQR.setNull(3, Types.INTEGER);
                    psQR.setInt(4, idVisita);
                }
                // Si es visita por intentos
                else if ("Por intentos".equalsIgnoreCase(v.getTipoVisita())) {
                    psQR.setString(1, codigo);
                    psQR.setNull(2, Types.DATE);
                    psQR.setInt(3, v.getIntentosPermitidos());
                    psQR.setInt(4, idVisita);
                }

                psQR.executeUpdate();

                // 📧 Enviar correo al visitante con su QR
                if (v.getCorreoVisitante() != null && !v.getCorreoVisitante().isEmpty()) {
                    String mensaje = "Estimado(a) " + v.getNombreVisitante() + ",\n\n"
                            + "Se le ha generado un código QR para ingresar a la residencial Mi Casita Segura.\n\n"
                            + "Detalles de su acceso:\n"
                            + "- Tipo: " + v.getTipoVisita() + "\n"
                            + (v.getTipoVisita().equals("Visita") ? "- Fecha límite: " + v.getFechaVisita() + "\n" : "")
                            + (v.getTipoVisita().equals("Por intentos") ? "- Intentos disponibles: " + v.getIntentosPermitidos() + "\n" : "")
                            + "\n⚠️ Recuerde que este QR es personal y temporal.\n\n"
                            + "Atentamente,\n"
                            + "Administración - Mi Casita Segura";

                    EmailSender.enviarConAdjunto(
                            v.getCorreoVisitante(),
                            "Código QR de acceso - Mi Casita Segura",
                            mensaje,
                            qrBytes
                    );
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Editar visita
    public boolean edit(Visitas v) {
        String sql = "UPDATE Visitas SET nombre_visitante=?,dpi_visitante=?,correo_visitante=?,id_residente=?,tipo_visita=?,fecha_visita=?,intentos_permitidos=?,estado=? WHERE id_visita=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, v.getNombreVisitante());
            ps.setString(2, v.getDpiVisitante());
            ps.setString(3, v.getCorreoVisitante());
            ps.setInt(4, v.getIdResidente());
            ps.setString(5, v.getTipoVisita());
            ps.setDate(6, v.getFechaVisita());
            ps.setObject(7, v.getIntentosPermitidos());
            ps.setBoolean(8, v.isEstado());
            ps.setInt(9, v.getIdVisita());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar visita
    public boolean delete(int id) {
        String sql = "DELETE FROM Visitas WHERE id_visita=?";
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
