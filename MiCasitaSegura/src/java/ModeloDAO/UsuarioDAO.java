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

    public Usuarios login(String correo, String contrasena) {
        Usuarios u = null;

        String sql = "SELECT u.*, r.nombre_rol, c.numero_casa AS numeroCasa, l.codigo_lote AS codigoLote "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.rol_id = r.id_rol "
                + "LEFT JOIN casas c ON u.numero_casa_id = c.id_casa "
                + "LEFT JOIN lotes l ON u.lote_id = l.id_lote "
                + "WHERE u.correo=? AND u.contrasena=? AND u.estado=1";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuarios();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setCorreo(rs.getString("correo"));
                    u.setContrasena(rs.getString("contrasena"));
                    u.setRolId(rs.getInt("rol_id"));
                    u.setNombreRol(rs.getString("nombre_rol"));
                    u.setNumeroCasaId(rs.getObject("numero_casa_id") != null ? rs.getInt("numero_casa_id") : null);
                    u.setLoteId(rs.getObject("lote_id") != null ? rs.getInt("lote_id") : null);
                    u.setEstado(rs.getBoolean("estado"));

                    // 🏠 Campos descriptivos
                    u.setNumeroCasa(rs.getObject("numeroCasa") != null ? rs.getInt("numeroCasa") : null);
                    u.setCodigoLote(rs.getString("codigoLote"));

                    // 🕒 Auditoría
                    u.setCreadoPor(rs.getObject("CreadoPor") != null ? rs.getInt("CreadoPor") : null);
                    u.setModificadoPor(rs.getObject("ModificadoPor") != null ? rs.getInt("ModificadoPor") : null);
                    u.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                    u.setFechaModificacion(rs.getTimestamp("FechaModificacion"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] Falló login(): " + e.getMessage());
        }

        return u;
    }

    public Usuarios obtenerPorId(int idUsuario) {
        Usuarios u = null;
        String sql = "SELECT u.*, r.nombre_rol FROM usuarios u "
                + "INNER JOIN roles r ON u.rol_id = r.id_rol WHERE u.id_usuario = ?";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuarios();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setCorreo(rs.getString("correo"));
                    u.setRolId(rs.getInt("rol_id"));
                    u.setNombreRol(rs.getString("nombre_rol"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    public void registrarAccion(int usuarioId, String accion) {
        String sql = "INSERT INTO auditoria(usuario_id, accion) VALUES (?, ?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setString(2, accion);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Auditoría registrada: " + accion + " para usuario " + usuarioId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Usuarios> listar() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setDpi(rs.getString("dpi"));
                u.setNombre(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRolId(rs.getInt("rol_id"));
                u.setNumeroCasaId(rs.getObject("numero_casa_id") != null ? rs.getInt("numero_casa_id") : null);
                u.setLoteId(rs.getObject("lote_id") != null ? rs.getInt("lote_id") : null);
                u.setEstado(rs.getBoolean("estado"));

                // Auditoría
                u.setCreadoPor(rs.getObject("CreadoPor") != null ? rs.getInt("CreadoPor") : null);
                u.setModificadoPor(rs.getObject("ModificadoPor") != null ? rs.getInt("ModificadoPor") : null);
                u.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                u.setFechaModificacion(rs.getTimestamp("FechaModificacion"));

                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Usuarios> listarGuardias() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT u.*, r.nombre_rol "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.rol_id = r.id_rol "
                + "WHERE u.rol_id = 2 AND u.estado = 1";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setDpi(rs.getString("dpi"));
                u.setNombre(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRolId(rs.getInt("rol_id"));
                u.setNumeroCasaId(rs.getObject("numero_casa_id") != null ? rs.getInt("numero_casa_id") : null);
                u.setLoteId(rs.getObject("lote_id") != null ? rs.getInt("lote_id") : null);
                u.setEstado(rs.getBoolean("estado"));

                // Nombre del rol
                u.setNombreRol(rs.getString("nombre_rol"));

                // Auditoría
                u.setCreadoPor(rs.getObject("CreadoPor") != null ? rs.getInt("CreadoPor") : null);
                u.setModificadoPor(rs.getObject("ModificadoPor") != null ? rs.getInt("ModificadoPor") : null);
                u.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                u.setFechaModificacion(rs.getTimestamp("FechaModificacion"));

                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Usuarios> listarResidentes() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE rol_id = 1";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

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
        Usuarios u = null;

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuarios();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setDpi(rs.getString("dpi"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setCorreo(rs.getString("correo"));
                    u.setContrasena(rs.getString("contrasena"));
                    u.setRolId(rs.getInt("rol_id"));
                    u.setNumeroCasaId(rs.getObject("numero_casa_id") != null ? rs.getInt("numero_casa_id") : null);
                    u.setLoteId(rs.getObject("lote_id") != null ? rs.getInt("lote_id") : null);
                    u.setEstado(rs.getBoolean("estado"));

                    // Auditoría
                    u.setCreadoPor(rs.getObject("CreadoPor") != null ? rs.getInt("CreadoPor") : null);
                    u.setModificadoPor(rs.getObject("ModificadoPor") != null ? rs.getInt("ModificadoPor") : null);
                    u.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                    u.setFechaModificacion(rs.getTimestamp("FechaModificacion"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public boolean add(Usuarios u) {
        String sql = "INSERT INTO Usuarios(dpi,nombre,apellidos,correo,contrasena,rol_id,numero_casa_id,lote_id,estado,CreadoPor) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getDpi());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellidos());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getContrasena());
            ps.setInt(6, u.getRolId());
            ps.setObject(7, u.getNumeroCasaId());
            ps.setObject(8, u.getLoteId());
            ps.setBoolean(9, u.isEstado());
            ps.setObject(10, u.getCreadoPor());
            ps.executeUpdate();

            int idUsuario = 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idUsuario = rs.getInt(1);
                }
            }

            if (idUsuario > 0) {
                final int idUsuarioFinal = idUsuario;

                new Thread(() -> {
                    try (Connection con2 = Conexion.getConnection()) {
                        String codigo = "USR-" + idUsuarioFinal;
                        byte[] qrBytes = QRGenerator.generarQR(codigo, 250, 250);

                        // Insertar el registro QR
                        String sqlQR = "INSERT INTO Codigos_QR(codigo, tipo, fecha_inicio, id_usuario, estado) VALUES(?, 'permanente', NOW(), ?, 0)";
                        try (PreparedStatement psQR = con2.prepareStatement(sqlQR)) {
                            psQR.setString(1, codigo);
                            psQR.setInt(2, idUsuarioFinal);
                            psQR.executeUpdate();
                        }

                        // Recuperar datos actualizados del usuario
                        String nombre = "", apellidos = "", correo = "";
                        try (PreparedStatement psDatos = con2.prepareStatement(
                                "SELECT nombre, apellidos, correo FROM Usuarios WHERE id_usuario = ?")) {
                            psDatos.setInt(1, idUsuarioFinal);
                            try (ResultSet rsDatos = psDatos.executeQuery()) {
                                if (rsDatos.next()) {
                                    nombre = rsDatos.getString("nombre");
                                    apellidos = rsDatos.getString("apellidos");
                                    correo = rsDatos.getString("correo");
                                }
                            }
                        }

                        // 📩 Mensaje EXACTO solicitado
                        String mensaje = "<p>¡Hola!</p>"
                                + "<p>Se ha generado exitosamente tu <b>código QR</b> de acceso al residencial.</p>"
                                + "<p><b>Detalles de tu registro:</b><br>"
                                + "Nombre del residente: <b>" + nombre + " " + apellidos + "</b><br>"
                                + "Validez del código QR: Permanente</p>"
                                + "<p><b>Instrucciones importantes:</b><br>"
                                + "Guarda este correo o el código QR adjunto.<br>"
                                + "Preséntalo al llegar al residencial para que el personal de seguridad lo escanee y valide tu acceso.</p>"
                                + "<hr><small>Mensaje automático del sistema <b>Mi Casita Segura</b></small>";

                        EmailSender.enviarConAdjunto(
                                correo,
                                "Bienvenido a Mi Casita Segura",
                                mensaje,
                                qrBytes
                        );

                        System.out.println("[INFO] Correo de bienvenida enviado a: " + correo);

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("[ERROR] Falló el hilo de correo en add(Usuarios): " + e.getMessage());
                    }
                }).start();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] No se pudo registrar el usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean edit(Usuarios u) {
        String sql = "UPDATE Usuarios SET dpi=?,nombre=?,apellidos=?,correo=?,contrasena=?,rol_id=?,numero_casa_id=?,lote_id=?,estado=?,ModificadoPor=? "
                + "WHERE id_usuario=?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getDpi());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellidos());
            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getContrasena());
            ps.setInt(6, u.getRolId());
            ps.setObject(7, u.getNumeroCasaId());
            ps.setObject(8, u.getLoteId());
            ps.setBoolean(9, u.isEstado());
            ps.setObject(10, u.getModificadoPor()); // 👈 Auditoría
            ps.setInt(11, u.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE Usuarios SET estado = 0 WHERE id_usuario=?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean activar(int id) {
        String sql = "UPDATE Usuarios SET estado = 1 WHERE id_usuario=?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existeUsuario(String dpi, String correo) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE dpi=? OR correo=?";

        try (Connection con = Conexion.getConnection();
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
            return Collections.emptyList(); // número de casa incompleto
        }

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {

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

            try (ResultSet rs = ps.executeQuery()) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // TALANQUERA
    public boolean puedeAbrirVisita(int idVisita) {
        String selectSql = "SELECT id_qr, estado, fecha_fin, intentos_disponibles "
                + "FROM Codigos_QR WHERE id_visita = ? ORDER BY id_qr DESC LIMIT 1";
        String updateSql = "UPDATE Codigos_QR SET estado = ?, intentos_disponibles = ? WHERE id_qr = ?";

        try (Connection con = Conexion.getConnection();
                PreparedStatement psSelect = con.prepareStatement(selectSql)) {

            psSelect.setInt(1, idVisita);

            try (ResultSet rs = psSelect.executeQuery()) {
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

                    // Alternar estado y reducir intentos
                    boolean nuevoEstado = !estadoActual;
                    int nuevosIntentos = intentos - 1;

                    try (PreparedStatement psUpdate = con.prepareStatement(updateSql)) {
                        psUpdate.setBoolean(1, nuevoEstado);
                        psUpdate.setInt(2, nuevosIntentos);
                        psUpdate.setInt(3, idQr);
                        psUpdate.executeUpdate();
                    }

                    return true;
                }
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

        try (Connection con = Conexion.getConnection();
                PreparedStatement psSelect = con.prepareStatement(selectSql)) {

            // 📌 Consultar el último QR del usuario
            psSelect.setInt(1, idUsuario);

            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    int idQr = rs.getInt("id_qr");
                    boolean estadoActual = rs.getBoolean("estado");

                    // Alternar estado: si estaba fuera → entra, si estaba dentro → sale
                    boolean nuevoEstado = !estadoActual;

                    // 📌 Actualizar estado QR
                    try (PreparedStatement psUpdate = con.prepareStatement(updateSql)) {
                        psUpdate.setBoolean(1, nuevoEstado);
                        psUpdate.setInt(2, idQr);
                        psUpdate.executeUpdate();
                    }

                    // 📌 Obtener datos del usuario
                    try (PreparedStatement psUser = con.prepareStatement(selectUserSql)) {
                        psUser.setInt(1, idUsuario);

                        try (ResultSet rsUser = psUser.executeQuery()) {
                            if (rsUser.next()) {
                                String nombre = rsUser.getString("nombre");
                                String apellidos = rsUser.getString("apellidos");
                                String correo = rsUser.getString("correo");

                                String tipoAcceso = nuevoEstado ? "Entrada" : "Salida";
                                String fechaHora = new java.util.Date().toString();

                                // 📧 Enviar correo sin adjunto (solo aviso)
                                String mensaje = "Estimado(a) " + nombre + " " + apellidos + ",\n\n"
                                        + "Se ha registrado el uso de su código QR en el sistema Mi Casita Segura.\n\n"
                                        + "Detalles del acceso:\n"
                                        + "- Tipo: " + tipoAcceso + "\n"
                                        + "- Fecha y hora: " + fechaHora + "\n\n"
                                        + "⚠️ Recuerde que este QR es personal e intransferible.\n\n"
                                        + "Atentamente,\n"
                                        + "Administración - Mi Casita Segura";

                                EmailSender.enviarConAdjunto(
                                        correo,
                                        "Notificación de acceso - Mi Casita Segura",
                                        mensaje,
                                        null
                                );

                                // 📝 Insertar en Bitácora
                                try (PreparedStatement psBitacora = con.prepareStatement(insertBitacoraSql)) {
                                    psBitacora.setInt(1, idUsuario);
                                    psBitacora.setString(2, tipoAcceso);
                                    psBitacora.setString(3, "El usuario " + nombre + " " + apellidos + " realizó una " + tipoAcceso);
                                    psBitacora.executeUpdate();
                                }
                            }
                        }
                    }

                    // 🔹 Si estaba afuera (estado = false), ahora entra → puede pasar
                    return !estadoActual;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // ❌ Por defecto, no puede abrir
    }

    public boolean existeDpiOCorreo(String dpi, String correo, Integer excluirId) {
        boolean existe = false;
        String sql = "SELECT COUNT(*) FROM usuarios WHERE (dpi = ? OR correo = ?)";
        if (excluirId != null) {
            sql += " AND id_usuario <> ?";
        }

        try (Connection con = cn.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dpi);
            ps.setString(2, correo);
            if (excluirId != null) {
                ps.setInt(3, excluirId);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                existe = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existe;
    }

    public List<Usuarios> listarPorRolActivo(String nombreRol) {
        List<Usuarios> lista = new ArrayList<>();

        String sql = "SELECT u.*, r.nombre_rol, c.numero_casa AS numeroCasa, l.codigo_lote AS codigoLote "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.rol_id = r.id_rol "
                + "LEFT JOIN casas c ON u.numero_casa_id = c.id_casa "
                + "LEFT JOIN lotes l ON u.lote_id = l.id_lote "
                + "WHERE r.nombre_rol = ? AND u.estado = 1";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreRol);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuarios u = new Usuarios();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setDpi(rs.getString("dpi"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellidos(rs.getString("apellidos"));
                    u.setCorreo(rs.getString("correo"));
                    u.setContrasena(rs.getString("contrasena"));
                    u.setRolId(rs.getInt("rol_id"));
                    u.setNumeroCasaId(rs.getObject("numero_casa_id") != null ? rs.getInt("numero_casa_id") : null);
                    u.setLoteId(rs.getObject("lote_id") != null ? rs.getInt("lote_id") : null);
                    u.setEstado(rs.getBoolean("estado"));

                    // 🔹 Rol (desde tabla Roles)
                    u.setNombreRol(rs.getString("nombre_rol"));

                    // 🔹 Nuevos campos descriptivos (JOIN con Casas y Lotes)
                    u.setNumeroCasa(rs.getObject("numeroCasa") != null ? rs.getInt("numeroCasa") : null);
                    u.setCodigoLote(rs.getString("codigoLote"));

                    // 🔹 Auditoría
                    u.setCreadoPor(rs.getObject("CreadoPor") != null ? rs.getInt("CreadoPor") : null);
                    u.setModificadoPor(rs.getObject("ModificadoPor") != null ? rs.getInt("ModificadoPor") : null);
                    u.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
                    u.setFechaModificacion(rs.getTimestamp("FechaModificacion"));

                    lista.add(u);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] listarPorRolActivo(" + nombreRol + "): " + e.getMessage());
        }

        return lista;
    }

    public List<Usuarios> listarResidentesPaqueteria() {
        List<Usuarios> lista = new ArrayList<>();

        String sql = "SELECT "
                + "u.id_usuario, "
                + "u.nombre, "
                + "u.apellidos, "
                + "u.correo, "
                + "c.numero_casa AS numeroCasa, "
                + "l.codigo_lote AS codigoLote "
                + "FROM usuarios u "
                + "LEFT JOIN casas c ON u.numero_casa_id = c.id_casa "
                + "LEFT JOIN lotes l ON u.lote_id = l.id_lote "
                + "WHERE u.rol_id = 1 AND u.estado = 1 "
                + "ORDER BY u.apellidos, u.nombre";

        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCorreo(rs.getString("correo"));

                // 🏠 Campos descriptivos del JOIN
                u.setNumeroCasa(rs.getObject("numeroCasa") != null ? rs.getInt("numeroCasa") : null);
                u.setCodigoLote(rs.getString("codigoLote"));

                lista.add(u);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] listarResidentesPaqueteria(): " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

}
