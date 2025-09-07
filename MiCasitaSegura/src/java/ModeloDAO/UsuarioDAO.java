package ModeloDAO;

import Config.Conexion;
import Controlador.EmailSender;
import Controlador.QRGenerator;
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
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idUsuario = rs.getInt(1);

                // Código único con prefijo
                String codigo = "USR-" + idUsuario;

                // Guardar en Codigos_QR (solo datos básicos por ahora)
                String sqlQR = "INSERT INTO Codigos_QR(codigo, tipo, fecha_inicio, id_usuario, estado) "
                        + "VALUES(?, 'permanente', NOW(), ?, 0)";
                PreparedStatement psQR = con.prepareStatement(sqlQR);
                psQR.setString(1, codigo);
                psQR.setInt(2, idUsuario);
                psQR.executeUpdate();

                // --- Lanzar en segundo plano la generación de QR y envío del correo ---
                new Thread(() -> {
                    try {
                        // Generar QR
                        byte[] qrBytes = QRGenerator.generarQR(codigo, 250, 250);

                        // Construir mensaje
                        String mensaje = "Estimado(a) " + u.getNombre() + " " + u.getApellidos() + ",\n\n"
                                + "Le damos la bienvenida al sistema *Mi Casita Segura* como nuevo residente.\n\n"
                                + "Adjunto encontrará su código QR personal, el cual le permitirá acceder a las instalaciones.\n\n"
                                + "⚠️ Importante:\n"
                                + "- Uso personal e intransferible.\n"
                                + "- No lo comparta.\n"
                                + "- Guárdelo en un lugar seguro.\n\n"
                                + "Gracias por confiar en Mi Casita Segura.\n\n"
                                + "Atentamente,\n"
                                + "Administración - Mi Casita Segura";

                        // Enviar correo
                        EmailSender.enviarConAdjunto(
                                u.getCorreo(),
                                "Bienvenido a Mi Casita Segura",
                                mensaje,
                                qrBytes
                        );
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start(); // se ejecuta en paralelo

            }
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

    public boolean puedeAbrirVisita(int idVisita) {
        String selectSql = "SELECT id_qr, estado, fecha_fin, intentos_disponibles "
                + "FROM Codigos_QR WHERE id_visita = ? ORDER BY id_qr DESC LIMIT 1";
        String updateSql = "UPDATE Codigos_QR SET estado = ?, intentos_disponibles = ? WHERE id_qr = ?";

        try (Connection con = cn.getConnection();
                PreparedStatement psSelect = con.prepareStatement(selectSql)) {

            psSelect.setInt(1, idVisita);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                int idQr = rs.getInt("id_qr");
                boolean estadoActual = rs.getBoolean("estado");
                java.sql.Timestamp fechaFin = rs.getTimestamp("fecha_fin");
                int intentos = rs.getInt("intentos_disponibles");

                // 🔹 Validar fecha
                if (fechaFin != null && fechaFin.before(new java.util.Date())) {
                    return false; // QR expirado
                }

                // 🔹 Validar intentos
                if (intentos <= 0) {
                    return false; // Sin intentos disponibles
                }

                // Alternar estado
                boolean nuevoEstado = !estadoActual;

                // Reducir intentos si entra/sale
                int nuevosIntentos = intentos - 1;

                try (PreparedStatement psUpdate = con.prepareStatement(updateSql)) {
                    psUpdate.setBoolean(1, nuevoEstado);
                    psUpdate.setInt(2, nuevosIntentos);
                    psUpdate.setInt(3, idQr);
                    psUpdate.executeUpdate();
                }

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean puedeAbrirUsuario(int idUsuario) {
        String selectSql = "SELECT id_qr, estado FROM Codigos_QR WHERE id_usuario = ? ORDER BY id_qr DESC LIMIT 1";
        String updateSql = "UPDATE Codigos_QR SET estado = ? WHERE id_qr = ?";
        String selectUserSql = "SELECT nombre, apellidos, correo FROM Usuarios WHERE id_usuario = ?";
        String insertBitacoraSql = "INSERT INTO Bitacora (id_usuario, accion, descripcion) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement psSelect = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psUser = null;
        PreparedStatement psBitacora = null;
        ResultSet rs = null;
        ResultSet rsUser = null;

        try {
            con = cn.getConnection();

            // 📌 Consultar el último QR del usuario
            psSelect = con.prepareStatement(selectSql);
            psSelect.setInt(1, idUsuario);
            rs = psSelect.executeQuery();

            if (rs.next()) {
                int idQr = rs.getInt("id_qr");
                boolean estadoActual = rs.getBoolean("estado");

                // Alternar estado: si estaba fuera → entra, si estaba dentro → sale
                boolean nuevoEstado = !estadoActual;

                // 📌 Actualizar el estado en Codigos_QR
                psUpdate = con.prepareStatement(updateSql);
                psUpdate.setBoolean(1, nuevoEstado);
                psUpdate.setInt(2, idQr);
                psUpdate.executeUpdate();

                // 📌 Obtener datos del usuario
                psUser = con.prepareStatement(selectUserSql);
                psUser.setInt(1, idUsuario);
                rsUser = psUser.executeQuery();

                if (rsUser.next()) {
                    String nombre = rsUser.getString("nombre");
                    String apellidos = rsUser.getString("apellidos");
                    String correo = rsUser.getString("correo");

                    String tipoAcceso = nuevoEstado ? "Entrada" : "Salida";
                    String fechaHora = new java.util.Date().toString();

                    // 📌 Construir mensaje de notificación
                    String mensaje = "Estimado(a) " + nombre + " " + apellidos + ",\n\n"
                            + "Se ha registrado el uso de su código QR en el sistema Mi Casita Segura.\n\n"
                            + "Detalles del acceso:\n"
                            + "- Tipo: " + tipoAcceso + "\n"
                            + "- Fecha y hora: " + fechaHora + "\n\n"
                            + "⚠️ Recuerde que este QR es personal e intransferible.\n\n"
                            + "Atentamente,\n"
                            + "Administración - Mi Casita Segura";

                    // 📧 Enviar correo sin adjunto (solo aviso)
                    EmailSender.enviarConAdjunto(
                            correo,
                            "Notificación de acceso - Mi Casita Segura",
                            mensaje,
                            null
                    );

                    // 📝 Insertar en Bitácora
                    psBitacora = con.prepareStatement(insertBitacoraSql);
                    psBitacora.setInt(1, idUsuario);
                    psBitacora.setString(2, tipoAcceso);
                    psBitacora.setString(3, "El usuario " + nombre + " " + apellidos + " realizó una " + tipoAcceso);
                    psBitacora.executeUpdate();
                }

                // 🔹 Si estaba afuera (estado = false), ahora entra → puede pasar
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
                if (rsUser != null) {
                    rsUser.close();
                }
                if (psUser != null) {
                    psUser.close();
                }
                if (psBitacora != null) {
                    psBitacora.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return false; // ❌ Por defecto, no puede abrir
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

    public List<Usuarios> buscar(String nombre, String apellidos, Integer loteId, Integer numeroCasaId) {
        List<Usuarios> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Usuarios WHERE 1=1 ");

        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND nombre LIKE ? ");
        }
        if (apellidos != null && !apellidos.trim().isEmpty()) {
            sql.append(" AND apellidos LIKE ? ");
        }
        if (loteId != null && numeroCasaId != null) {
            sql.append(" AND lote_id = ? AND numero_casa_id = ? ");
        } else if ((loteId != null && numeroCasaId == null) || (loteId == null && numeroCasaId != null)) {
            // FA3 → número de casa incompleto
            return Collections.emptyList();
        }

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql.toString());

            int index = 1;
            if (nombre != null && !nombre.trim().isEmpty()) {
                ps.setString(index++, "%" + nombre + "%");
            }
            if (apellidos != null && !apellidos.trim().isEmpty()) {
                ps.setString(index++, "%" + apellidos + "%");
            }
            if (loteId != null && numeroCasaId != null) {
                ps.setInt(index++, loteId);
                ps.setInt(index++, numeroCasaId);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCorreo(rs.getString("correo"));
                u.setNumeroCasaId(rs.getInt("numero_casa_id"));
                u.setLoteId(rs.getInt("lote_id"));
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

}
