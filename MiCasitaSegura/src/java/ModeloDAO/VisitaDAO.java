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

// 🔹 Agregar nueva visita + generar QR y enviar notificaciones (RN4–RN7)
    public boolean add(Visitas v) {
        // 🔸 RN4: Validar intentos permitidos
        if ("Por intentos".equalsIgnoreCase(v.getTipoVisita()) && v.getIntentosPermitidos() <= 1) {
            throw new IllegalArgumentException("El número de intentos debe ser mayor a 1 (RN4).");
        }

        // 🔸 RN5: Validar fecha de visita (no puede ser pasada)
        if ("Visita".equalsIgnoreCase(v.getTipoVisita())) {
            java.sql.Date hoy = new java.sql.Date(System.currentTimeMillis());

            java.util.Calendar calVisita = java.util.Calendar.getInstance();
            calVisita.setTime(v.getFechaVisita());
            java.util.Calendar calHoy = java.util.Calendar.getInstance();
            calHoy.setTime(hoy);

            calVisita.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calVisita.set(java.util.Calendar.MINUTE, 0);
            calVisita.set(java.util.Calendar.SECOND, 0);
            calVisita.set(java.util.Calendar.MILLISECOND, 0);

            calHoy.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calHoy.set(java.util.Calendar.MINUTE, 0);
            calHoy.set(java.util.Calendar.SECOND, 0);
            calHoy.set(java.util.Calendar.MILLISECOND, 0);

            if (calVisita.before(calHoy)) {
                throw new IllegalArgumentException("La fecha de visita no puede ser de días pasados (RN5).");
            }
        }

        // 🔸 Inserción principal
        String sql = "INSERT INTO Visitas(nombre_visitante, dpi_visitante, correo_visitante, id_residente, id_usuario_creador, tipo_visita, fecha_visita, intentos_permitidos, estado) "
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

            // 🔹 Obtener ID generado
            int idVisita = 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idVisita = rs.getInt(1);
                }
            }

            // 🔹 Generar QR y correos
            if (idVisita > 0) {
                final int idVisitaFinal = idVisita;

                new Thread(() -> {
                    try (Connection con2 = Conexion.getConnection()) {
                        String codigo = "VIS-" + idVisitaFinal;
                        byte[] qrBytes = QRGenerator.generarQR(codigo, 250, 250);

                        // 🔸 Insertar registro QR
                        String sqlQR = "INSERT INTO Codigos_QR(codigo, fecha_inicio, fecha_fin, intentos_disponibles, id_visita, estado) "
                                + "VALUES(?, NOW(), ?, ?, ?, 1)";
                        try (PreparedStatement psQR = con2.prepareStatement(sqlQR)) {
                            psQR.setString(1, codigo);
                            if ("Visita".equalsIgnoreCase(v.getTipoVisita())) {
                                psQR.setDate(2, v.getFechaVisita());
                                psQR.setNull(3, java.sql.Types.INTEGER);
                                psQR.setInt(4, idVisitaFinal);
                            } else {
                                psQR.setNull(2, java.sql.Types.DATE);
                                psQR.setInt(3, v.getIntentosPermitidos());
                                psQR.setInt(4, idVisitaFinal);
                            }
                            psQR.executeUpdate();
                        }

                        // 🔹 Obtener correo del residente
                        String correoResidente = null;
                        try (PreparedStatement psRes = con2.prepareStatement(
                                "SELECT correo FROM Usuarios WHERE id_usuario = ?")) {
                            psRes.setInt(1, v.getIdResidente());
                            try (ResultSet rsRes = psRes.executeQuery()) {
                                if (rsRes.next()) {
                                    correoResidente = rsRes.getString("correo");
                                }
                            }
                        }

                        // 🔹 Fecha y hora actual
                        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
                        String hora = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());
                        String validez = v.getTipoVisita().equalsIgnoreCase("Visita")
                                ? ("hasta " + v.getFechaVisita())
                                : (v.getIntentosPermitidos() + " intentos");

                        // 🔸 RN7: Notificación al visitante
                        if (v.getCorreoVisitante() != null && !v.getCorreoVisitante().isEmpty()) {
                            String mensajeVisitante
                                    = "¡Hola!\n\n"
                                    + "Se ha generado exitosamente tu código QR de acceso al residencial.\n\n"
                                    + "A continuación, encontrarás los detalles de tu registro:\n\n"
                                    + "Nombre del visitante: " + v.getNombreVisitante() + "\n"
                                    + "Validez del código QR: " + validez + "\n\n"
                                    + "Instrucciones importantes:\n"
                                    + "• Guarda este correo o el código QR adjunto.\n"
                                    + "• Preséntalo al llegar al residencial para que el personal de seguridad lo escanee.\n\n"
                                    + "¡Gracias por coordinar tu visita con anticipación!";

                            EmailSender.enviarConAdjunto(
                                    v.getCorreoVisitante(),
                                    "Notificación de accesos creados",
                                    mensajeVisitante,
                                    qrBytes
                            );
                            System.out.println("[INFO] Correo enviado al visitante: " + v.getCorreoVisitante());
                        }

                        // 🔸 RN6: Notificación al residente
                        if (correoResidente != null && !correoResidente.isEmpty()) {
                            String mensajeResidente
                                    = "El código QR fue generado exitosamente para la persona " + v.getNombreVisitante()
                                    + " el día " + fecha + " a las " + hora + " para acceder al condominio.\n"
                                    + "Este código tiene una validez de " + validez + ".\n\n"
                                    + "En caso de cualquier irregularidad, por favor contacte al administrador del sistema.";

                            EmailSender.enviarConAdjunto(
                                    correoResidente,
                                    "Notificación de accesos creados",
                                    mensajeResidente,
                                    qrBytes
                            );
                            System.out.println("[INFO] Correo enviado al residente: " + correoResidente);
                        } else {
                            System.out.println("[WARN] No se encontró correo del residente para id_residente=" + v.getIdResidente());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("[ERROR] Error en hilo de envío de correo: " + e.getMessage());
                    }
                }).start();
            }

            return true;

        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar la visita.");
        }
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

    // 🔹 "Eliminar" = pasar a estado inactivo tanto la visita como su QR
    public boolean delete(int idVisita) {
        String sqlVisita = "UPDATE Visitas SET estado = 0 WHERE id_visita = ?";
        String sqlQR = "UPDATE Codigos_QR SET estado = 0 WHERE id_visita = ?";

        try (Connection con = Conexion.getConnection()) {
            // Desactivar la visita
            try (PreparedStatement ps = con.prepareStatement(sqlVisita)) {
                ps.setInt(1, idVisita);
                ps.executeUpdate();
            }

            // Desactivar el código QR
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
