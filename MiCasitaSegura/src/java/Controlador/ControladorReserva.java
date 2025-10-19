package Controlador;

import Modelo.Reservas;
import Modelo.EstadoReserva;
import Modelo.AreasComunes;
import Modelo.Usuarios;
import Modelo.Notificacion;

import ModeloDAO.ReservasDAO;
import ModeloDAO.AreasComunesDAO;
import ModeloDAO.EstadoReservaDAO;
import ModeloDAO.NotificacionDAO;
import ModeloDAO.UsuarioDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ControladorReserva")
public class ControladorReserva extends HttpServlet {

    // ✅ Vistas
    private static final String INDEX_VISTA = "vistas/Reservacion/Index.jsp";
    private static final String ADDEDIT_VISTA = "vistas/Reservacion/addEdit.jsp";

    // ✅ DAOs
    private final ReservasDAO reservasDAO = new ReservasDAO();
    private final AreasComunesDAO areasDAO = new AreasComunesDAO();
    private final EstadoReservaDAO estadoDAO = new EstadoReservaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion.toLowerCase()) {

            // 🔹 LISTAR RESERVAS
            case "listar": {
                List<Reservas> lista;

                if ("Residente".equalsIgnoreCase(usuarioSesion.getNombreRol())) {
                    lista = reservasDAO.listarPorResidente(usuarioSesion.getIdUsuario());
                } else {
                    lista = reservasDAO.listar();
                }

                request.setAttribute("listaReservas", lista);
                request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
                break;
            }

            // 🔹 NUEVA RESERVA
            case "nuevo": {
                List<AreasComunes> areas = areasDAO.listarActivas();
                request.setAttribute("listaAreas", areas); // ✅ Atributo corregido (coincide con JSP)
                RequestDispatcher rd = request.getRequestDispatcher(ADDEDIT_VISTA);
                rd.forward(request, response);
                break;
            }

            // 🔹 CANCELAR RESERVA
            case "cancelar": {
                int idReserva = Integer.parseInt(request.getParameter("id"));
                reservasDAO.cancelarReserva(idReserva, String.valueOf(usuarioSesion.getIdUsuario()));
                response.sendRedirect("ControladorReserva?accion=listar&cancelada=true");
                break;
            }

            // 🔹 CONFIRMAR RESERVA (solo admin)
            case "confirmar": {
                int idReserva = Integer.parseInt(request.getParameter("id"));
                reservasDAO.confirmarReserva(idReserva, String.valueOf(usuarioSesion.getIdUsuario()));

                // 🔔 Notificar al residente
                Reservas reserva = reservasDAO.obtenerPorId(idReserva);
                Usuarios residente = usuarioDAO.obtenerPorId(reserva.getIdResidente());
                AreasComunes area = areasDAO.buscarPorId(reserva.getIdArea());

                if (residente != null && area != null) {
                    String asunto = "Notificación de reserva";
                    String cuerpo = "Estimado " + residente.getNombre() + ", su reserva para el área común "
                            + area.getNombre() + " ha sido confirmada exitosamente para el día "
                            + reserva.getFechaReserva() + " en el horario de "
                            + reserva.getHoraInicio() + " a " + reserva.getHoraFin()
                            + ".\n\nLe recordamos respetar los tiempos asignados y notificar con 24h de anticipación "
                            + "si desea cancelarla. ¡Gracias por contribuir a un uso ordenado de nuestros recursos!";

                    // Guardar notificación
                    Notificacion n = new Notificacion();
                    n.setIdGuardia(residente.getIdUsuario());
                    n.setAsunto(asunto);
                    n.setCuerpo(cuerpo);
                    n.setCreadoPor(usuarioSesion.getIdUsuario());
                    notificacionDAO.registrar(n);

                    // (Opcional) Enviar correo si usas EmailSender
                    // EmailSender.enviarConAdjunto(residente.getCorreo(), asunto, cuerpo, null);
                }

                response.sendRedirect("ControladorReserva?accion=listar&confirmada=true");
                break;
            }

            default:
                response.sendRedirect("ControladorReserva?accion=listar");
        }
    }

    // 🔹 POST → crear o actualizar reserva
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

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
            switch (accion.toLowerCase()) {

                // 🔹 GUARDAR NUEVA RESERVA
                case "guardar": {
                    int idArea = Integer.parseInt(request.getParameter("idArea"));
                    String fechaStr = request.getParameter("fechaReserva");
                    String horaInicioStr = request.getParameter("horaInicio");
                    String horaFinStr = request.getParameter("horaFin");
                    String observaciones = request.getParameter("observaciones");

                    if (fechaStr == null || fechaStr.isEmpty()
                            || horaInicioStr == null || horaInicioStr.isEmpty()
                            || horaFinStr == null || horaFinStr.isEmpty()) {

                        request.setAttribute("error", "Debe completar todos los campos obligatorios.");
                        request.setAttribute("listaAreas", areasDAO.listarActivas()); // ✅ Consistente
                        RequestDispatcher rd = request.getRequestDispatcher(ADDEDIT_VISTA);
                        rd.forward(request, response);
                        return;
                    }

                    Date fecha = Date.valueOf(fechaStr);
                    Time horaInicio = Time.valueOf(horaInicioStr + ":00");
                    Time horaFin = Time.valueOf(horaFinStr + ":00");

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
                        response.sendRedirect("ControladorReserva?accion=listar&success=true");
                    } else {
                        request.setAttribute("error", "No se pudo registrar la reserva.");
                        request.setAttribute("listaAreas", areasDAO.listarActivas()); // ✅ Consistente
                        RequestDispatcher rd = request.getRequestDispatcher(ADDEDIT_VISTA);
                        rd.forward(request, response);
                    }
                    break;
                }

                default:
                    response.sendRedirect("ControladorReserva?accion=listar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error: " + e.getMessage());
            request.setAttribute("listaAreas", areasDAO.listarActivas()); // ✅ Consistente
            RequestDispatcher rd = request.getRequestDispatcher(ADDEDIT_VISTA);
            rd.forward(request, response);
        }
    }
}
