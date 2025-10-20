package Controlador;

import Modelo.Reservas;
import Modelo.AreasComunes;
import Modelo.Usuarios;
import Modelo.Notificacion;

import ModeloDAO.ReservasDAO;
import ModeloDAO.AreasComunesDAO;
import ModeloDAO.UsuarioDAO;
import ModeloDAO.NotificacionDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

// 📧 Envío de correos
import Controlador.EmailSender;

@WebServlet("/ControladorReserva")
public class ControladorReserva extends HttpServlet {

    private static final String INDEX_VISTA = "vistas/Reservacion/Index.jsp";
    private static final String ADDEDIT_VISTA = "vistas/Reservacion/addEdit.jsp";

    private final ReservasDAO reservasDAO = new ReservasDAO();
    private final AreasComunesDAO areasDAO = new AreasComunesDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String rol = usuarioSesion.getNombreRol() == null ? "" : usuarioSesion.getNombreRol();
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion.toLowerCase()) {

            case "listar": {
                List<Reservas> lista = ("Residente".equalsIgnoreCase(rol))
                        ? reservasDAO.listarPorResidente(usuarioSesion.getIdUsuario())
                        : reservasDAO.listar();
                request.setAttribute("listaReservas", lista);
                request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
                break;
            }

            case "nuevo": {
                request.setAttribute("listaAreas", areasDAO.listarActivas());
                request.setAttribute("nombreUsuario", usuarioSesion.getNombre());
                request.getRequestDispatcher(ADDEDIT_VISTA).forward(request, response);
                break;
            }

            case "cancelar": {
                int idReserva = Integer.parseInt(request.getParameter("id"));
                reservasDAO.cancelarReserva(idReserva, String.valueOf(usuarioSesion.getIdUsuario()));

                Reservas reserva = reservasDAO.obtenerPorId(idReserva);
                Usuarios residente = usuarioDAO.obtenerPorId(reserva.getIdResidente());
                AreasComunes area = areasDAO.buscarPorId(reserva.getIdArea());

                if (residente != null && area != null) {
                    // 🔹 Crear notificación interna
                    String asunto = "Reserva cancelada";
                    String cuerpo = "Estimado residente, su reserva para el área común "
                            + area.getNombre() + " programada para el día "
                            + reserva.getFechaReserva() + " en el horario de "
                            + reserva.getHoraInicio() + " a " + reserva.getHoraFin()
                            + " ha sido cancelada correctamente. Si desea realizar una nueva reserva, "
                            + "puede hacerlo desde el sistema Mi Casita Segura.";

                    try {
                        Notificacion n = new Notificacion();
                        n.setIdGuardia(residente.getIdUsuario()); // destinatario
                        n.setAsunto(asunto);
                        n.setCuerpo(cuerpo);
                        n.setCreadoPor(usuarioSesion.getIdUsuario());
                        notificacionDAO.registrar(n);
                    } catch (Exception ex) {
                        System.err.println("❌ Error al registrar notificación de cancelación: " + ex.getMessage());
                    }

                    // 🔹 Envío de correo
                    new Thread(() -> {
                        try {
                            String mensajeHTML
                                    = "<div style='font-family:Arial,sans-serif;color:#333;line-height:1.6;'>"
                                    + "<h2 style='color:#DC2626;'>Reserva cancelada</h2>"
                                    + "<p>Estimado residente, su reserva para el área común <b>" + area.getNombre() + "</b> "
                                    + "programada para el día <b>" + reserva.getFechaReserva() + "</b> "
                                    + "en el horario de <b>" + reserva.getHoraInicio() + "</b> a <b>" + reserva.getHoraFin() + "</b> "
                                    + "ha sido <b>cancelada correctamente</b>.</p>"
                                    + "<p>Si desea realizar una nueva reserva, puede hacerlo desde el sistema Mi Casita Segura.</p>"
                                    + "<br><p><b>Equipo de Administración - Mi Casita Segura</b></p>"
                                    + "</div>";

                            EmailSender.enviarConAdjunto(residente.getCorreo(), asunto, mensajeHTML, null);
                        } catch (Exception ex) {
                            System.err.println("❌ Error al enviar correo de cancelación: " + ex.getMessage());
                        }
                    }).start();
                }

                response.sendRedirect("ControladorReserva?accion=listar&cancelada=true");
                break;
            }

            case "confirmar": {
                int idReserva = Integer.parseInt(request.getParameter("id"));
                reservasDAO.confirmarReserva(idReserva, String.valueOf(usuarioSesion.getIdUsuario()));

                Reservas reserva = reservasDAO.obtenerPorId(idReserva);
                Usuarios residente = usuarioDAO.obtenerPorId(reserva.getIdResidente());
                AreasComunes area = areasDAO.buscarPorId(reserva.getIdArea());

                if (residente != null && area != null) {
                    String asunto = "Notificación de reserva";
                    String cuerpo = "Estimado residente, su reserva para el área común "
                            + area.getNombre() + " ha sido confirmada exitosamente para el día "
                            + reserva.getFechaReserva() + " en el horario de "
                            + reserva.getHoraInicio() + " a " + reserva.getHoraFin()
                            + ". Le recordamos revisar las políticas de uso del espacio, respetar los tiempos asignados "
                            + "y notificar con al menos 24 horas de anticipación en caso de cancelación o modificación. "
                            + "¡Gracias por contribuir a un uso ordenado de nuestros recursos comunitarios!";

                    try {
                        Notificacion n = new Notificacion();
                        n.setIdGuardia(residente.getIdUsuario());
                        n.setAsunto(asunto);
                        n.setCuerpo(cuerpo);
                        n.setCreadoPor(usuarioSesion.getIdUsuario());
                        notificacionDAO.registrar(n);
                    } catch (Exception ex) {
                        System.err.println("❌ Error al registrar notificación de confirmación: " + ex.getMessage());
                    }

                    new Thread(() -> {
                        try {
                            String mensajeHTML
                                    = "<div style='font-family:Arial,sans-serif;color:#333;line-height:1.6;'>"
                                    + "<h2 style='color:#4F46E5;'>Notificación de reserva</h2>"
                                    + "<p>Estimado residente, su reserva para el área común <b>" + area.getNombre() + "</b> "
                                    + "ha sido confirmada exitosamente para el día <b>" + reserva.getFechaReserva() + "</b> "
                                    + "en el horario de <b>" + reserva.getHoraInicio() + "</b> a <b>" + reserva.getHoraFin() + "</b>.</p>"
                                    + "<p>Le recordamos revisar las políticas de uso del espacio, respetar los tiempos asignados "
                                    + "y notificar con al menos 24 horas de anticipación en caso de cancelación o modificación.</p>"
                                    + "<p>¡Gracias por contribuir a un uso ordenado de nuestros recursos comunitarios!</p>"
                                    + "<br><p><b>Equipo de Administración - Mi Casita Segura</b></p>"
                                    + "</div>";

                            EmailSender.enviarConAdjunto(residente.getCorreo(), asunto, mensajeHTML, null);
                        } catch (Exception ex) {
                            System.err.println("❌ Error al enviar correo de confirmación: " + ex.getMessage());
                        }
                    }).start();
                }

                response.sendRedirect("ControladorReserva?accion=listar&confirmada=true");
                break;
            }

            default:
                response.sendRedirect("ControladorReserva?accion=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        try {
            if ("guardar".equalsIgnoreCase(accion)) {
                int idArea = Integer.parseInt(request.getParameter("idArea"));
                String fechaStr = request.getParameter("fechaReserva");
                String horaInicioStr = request.getParameter("horaInicio");
                String horaFinStr = request.getParameter("horaFin");
                String observaciones = request.getParameter("observaciones");

                if (fechaStr == null || fechaStr.isEmpty()
                        || horaInicioStr == null || horaInicioStr.isEmpty()
                        || horaFinStr == null || horaFinStr.isEmpty()
                        || idArea == 0) {
                    request.setAttribute("error", "Debe completar todos los campos obligatorios.");
                    request.setAttribute("listaAreas", areasDAO.listarActivas());
                    request.setAttribute("nombreUsuario", usuarioSesion.getNombre());
                    request.getRequestDispatcher(ADDEDIT_VISTA).forward(request, response);
                    return;
                }

                Date fecha = Date.valueOf(fechaStr);
                Time horaInicio = Time.valueOf(horaInicioStr + ":00");
                Time horaFin = Time.valueOf(horaFinStr + ":00");

                LocalDate hoy = LocalDate.now();
                if (fecha.toLocalDate().isBefore(hoy)) {
                    request.setAttribute("error", "No se pueden realizar reservas en fechas anteriores a la actual.");
                    request.setAttribute("listaAreas", areasDAO.listarActivas());
                    request.setAttribute("nombreUsuario", usuarioSesion.getNombre());
                    request.getRequestDispatcher(ADDEDIT_VISTA).forward(request, response);
                    return;
                }

                if (!horaInicio.before(horaFin)) {
                    request.setAttribute("error", "La hora de inicio debe ser menor que la hora de finalización.");
                    request.setAttribute("listaAreas", areasDAO.listarActivas());
                    request.setAttribute("nombreUsuario", usuarioSesion.getNombre());
                    request.getRequestDispatcher(ADDEDIT_VISTA).forward(request, response);
                    return;
                }

                boolean existeConflicto = reservasDAO.existeReservaEnHorario(idArea, fecha, horaInicio, horaFin);
                if (existeConflicto) {
                    request.setAttribute("error", "El salón no está disponible en el horario seleccionado, por favor elija otro.");
                    request.setAttribute("listaAreas", areasDAO.listarActivas());
                    request.setAttribute("nombreUsuario", usuarioSesion.getNombre());
                    request.getRequestDispatcher(ADDEDIT_VISTA).forward(request, response);
                    return;
                }

                Reservas r = new Reservas();
                r.setIdArea(idArea);
                r.setIdResidente(usuarioSesion.getIdUsuario());
                r.setFechaReserva(fecha);
                r.setHoraInicio(horaInicio);
                r.setHoraFin(horaFin);
                r.setObservaciones(observaciones);
                r.setCreadoPor(String.valueOf(usuarioSesion.getIdUsuario()));

                boolean ok = reservasDAO.crearReserva(r);

                if (ok) {
                    // Envío de correo asíncrono de registro
                    new Thread(() -> {
                        try {
                            Usuarios residente = usuarioDAO.obtenerPorId(usuarioSesion.getIdUsuario());
                            AreasComunes area = areasDAO.buscarPorId(idArea);
                            String asunto = "Reserva registrada correctamente";
                            String mensajeHTML = "<div style='font-family:Arial,sans-serif;color:#333;line-height:1.6;'>"
                                    + "<h2 style='color:#4F46E5;'>Confirmación de Registro de Reserva</h2>"
                                    + "<p>Estimado <b>" + residente.getNombre() + "</b>,</p>"
                                    + "<p>Su reserva para el área común <b>" + area.getNombre() + "</b> "
                                    + "ha sido registrada exitosamente para el día <b>" + fecha + "</b> "
                                    + "en el horario de <b>" + horaInicio + "</b> a <b>" + horaFin + "</b>.</p>"
                                    + "<p>Pronto recibirá una notificación cuando sea confirmada por administración.</p>"
                                    + "<br><p><b>Equipo de Administración - Mi Casita Segura</b></p>"
                                    + "</div>";
                            EmailSender.enviarConAdjunto(residente.getCorreo(), asunto, mensajeHTML, null);
                        } catch (Exception ex) {
                            System.err.println("❌ Error al enviar correo de creación: " + ex.getMessage());
                        }
                    }).start();

                    response.sendRedirect("ControladorReserva?accion=listar&success=true");
                } else {
                    request.setAttribute("error", "No se pudo registrar la reserva.");
                    request.setAttribute("listaAreas", areasDAO.listarActivas());
                    request.setAttribute("nombreUsuario", usuarioSesion.getNombre());
                    request.getRequestDispatcher(ADDEDIT_VISTA).forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error: " + e.getMessage());
            request.setAttribute("listaAreas", areasDAO.listarActivas());
            request.setAttribute("nombreUsuario", usuarioSesion.getNombre());
            request.getRequestDispatcher(ADDEDIT_VISTA).forward(request, response);
        }
    }
}
