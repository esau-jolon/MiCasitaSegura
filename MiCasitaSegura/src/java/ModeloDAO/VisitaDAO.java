package ModeloDAO;

import Config.Conexion;
import Controlador.EmailSender;
import Controlador.QRGenerator;
import Modelo.Visitas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitaDAO {

    // 🔹 Listar todas las visitas
    public List<Visitas> listar() {
        List<Visitas> lista = new ArrayList<>();
        String sql = "SELECT * FROM Visitas";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Visitas v = new Visitas();
                v.setIdVisita(rs.getInt("id_visita"));
                v.setNombreVisitante(rs.getString("nombre_visitante"));
                v.setDpiVisitante(rs.getString("dpi_visitante"));
                v.setCorreoVisitante(rs.getString("correo_visitante"));
                v.setIdResidente(rs.getInt("id_residente"));
                v.setIdUsuarioCreador(rs.getInt("id_usuario_creador"));
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

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    v.setIdVisita(rs.getInt("id_visita"));
                    v.setNombreVisitante(rs.getString("nombre_visitante"));
                    v.setDpiVisitante(rs.getString("dpi_visitante"));
                    v.setCorreoVisitante(rs.getString("correo_visitante"));
                    v.setIdResidente(rs.getInt("id_residente"));
                    v.setIdUsuarioCreador(rs.getInt("id_usuario_creador"));
                    v.setTipoVisita(rs.getString("tipo_visita"));
                    v.setFechaVisita(rs.getDate("fecha_visita"));
                    v.setIntentosPermitidos(rs.getInt("intentos_permitidos"));
                    v.setEstado(rs.getBoolean("estado"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    // 🔹 Agregar nueva visita + generar QR
    public boolean add(Visitas v) {
        // Validaciones RN4, RN5
        if ("Por intentos".equalsIgnoreCase(v.getTipoVisita()) && v.getIntentosPermitidos() <= 1) {
            throw new IllegalArgumentException("El número de intentos debe ser mayor a 1 (RN4).");
        }
        if ("Visita".equalsIgnoreCase(v.getTipoVisita())
                && v.getFechaVisita().before(new java.sql.Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("La fecha de visita no puede ser de días pasados (RN5).");
        }

        String sql = "INSERT INTO Visitas(nombre_visitante,dpi_visitante,correo_visitante,id_residente,id_usuario_creador,tipo_visita,fecha_visita,intentos_permitidos,estado) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getNombreVisitante());
            ps.setString(2, v.getDpiVisitante());
            ps.setString(3, v.getCorreoVisitante());
            ps.setInt(4, v.getIdResidente());
            ps.setInt(5, v.getIdUsuarioCreador());
            ps.setString(6, v.getTipoVisita());
            ps.setDate(7, v.getFechaVisita());
            ps.setObject(8, v.getIntentosPermitidos());
            ps.setBoolean(9, v.isEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idVisita = rs.getInt(1);
                    String codigo = "VIS-" + idVisita;

                    byte[] qrBytes = QRGenerator.generarQR(codigo, 250, 250);

                    String sqlQR = "INSERT INTO Codigos_QR(codigo, fecha_inicio, fecha_fin, intentos_disponibles, id_visita, estado) "
                            + "VALUES(?, NOW(), ?, ?, ?, 1)";
                    try (PreparedStatement psQR = con.prepareStatement(sqlQR)) {
                        psQR.setString(1, codigo);
                        if ("Visita".equalsIgnoreCase(v.getTipoVisita())) {
                            psQR.setDate(2, v.getFechaVisita());
                            psQR.setNull(3, Types.INTEGER);
                            psQR.setInt(4, idVisita);
                        } else {
                            psQR.setNull(2, Types.DATE);
                            psQR.setInt(3, v.getIntentosPermitidos());
                            psQR.setInt(4, idVisita);
                        }
                        psQR.executeUpdate();
                    }

                    if (v.getCorreoVisitante() != null && !v.getCorreoVisitante().isEmpty()) {
                        String mensajeVisitante
                                = "¡Hola!\n\nSe ha generado exitosamente tu código QR.\n\n"
                                + "Nombre del visitante: " + v.getNombreVisitante() + "\n"
                                + (v.getTipoVisita().equalsIgnoreCase("Visita")
                                ? "Validez del QR: hasta " + v.getFechaVisita() + "\n"
                                : "Intentos disponibles: " + v.getIntentosPermitidos() + "\n");
                        EmailSender.enviarConAdjunto(
                                v.getCorreoVisitante(),
                                "Notificación de accesos creados",
                                mensajeVisitante,
                                qrBytes
                        );
                    }
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
        String sql = "UPDATE Visitas SET nombre_visitante=?,dpi_visitante=?,correo_visitante=?,id_residente=?,id_usuario_creador=?,tipo_visita=?,fecha_visita=?,intentos_permitidos=?,estado=? "
                + "WHERE id_visita=?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getNombreVisitante());
            ps.setString(2, v.getDpiVisitante());
            ps.setString(3, v.getCorreoVisitante());
            ps.setInt(4, v.getIdResidente());
            ps.setInt(5, v.getIdUsuarioCreador());
            ps.setString(6, v.getTipoVisita());
            ps.setDate(7, v.getFechaVisita());
            ps.setObject(8, v.getIntentosPermitidos());
            ps.setBoolean(9, v.isEstado());
            ps.setInt(10, v.getIdVisita());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Eliminar visita
    public boolean delete(int id) {
        String sql = "DELETE FROM Visitas WHERE id_visita=?";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cancelar visita (FA06)
    public boolean cancelar(int idVisita) {
        String sqlVisita = "UPDATE Visitas SET estado = 0 WHERE id_visita = ?";
        String sqlQR = "UPDATE Codigos_QR SET estado = 0 WHERE id_visita = ?";

        try (Connection con = Conexion.getConnection()) {
            // 🔹 Desactivar la visita
            try (PreparedStatement ps = con.prepareStatement(sqlVisita)) {
                ps.setInt(1, idVisita);
                ps.executeUpdate();
            }

            // 🔹 Desactivar el código QR asociado
            try (PreparedStatement psQR = con.prepareStatement(sqlQR)) {
                psQR.setInt(1, idVisita);
                psQR.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Descargar QR (FA05)
    public void descargarQR(int idVisita, javax.servlet.http.HttpServletResponse response) {
        String sql = "SELECT codigo FROM Codigos_QR WHERE id_visita = ? ORDER BY id_qr DESC LIMIT 1";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVisita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String codigo = rs.getString("codigo");

                    // Regenerar el QR a partir del código (por simplicidad)
                    byte[] qrBytes = QRGenerator.generarQR(codigo, 250, 250);

                    // Configurar la respuesta HTTP como imagen PNG
                    response.setContentType("image/png");
                    response.setHeader("Content-Disposition", "attachment; filename=\"QR-Visita-" + idVisita + ".png\"");
                    response.getOutputStream().write(qrBytes);
                    response.getOutputStream().flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
