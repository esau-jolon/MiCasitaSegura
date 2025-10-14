package Controlador;

import Modelo.ReporteMantenimiento;
import Modelo.TipoInconveniente;
import Modelo.Usuarios;
import Modelo.Notificacion;

import ModeloDAO.ReporteMantenimientoDAO;
import ModeloDAO.TipoInconvenienteDAO;
import ModeloDAO.UsuarioDAO;
import ModeloDAO.NotificacionDAO;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ControladorReporteMantenimiento")
public class ControladorReporteMantenimiento extends HttpServlet {

    // ✅ Ruta correcta según tu estructura de carpetas
    private static final String INDEX_VISTA = "vistas/ReporteMantenimiento/Index.jsp";

    private final ReporteMantenimientoDAO reporteDAO = new ReporteMantenimientoDAO();
    private final TipoInconvenienteDAO tipoDAO = new TipoInconvenienteDAO();
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
            case "listar": {
                List<ReporteMantenimiento> lista = reporteDAO.listar();
                List<TipoInconveniente> tipos = tipoDAO.listar();

                request.setAttribute("listaReportes", lista);
                request.setAttribute("tiposInconveniente", tipos);
                request.setAttribute("mostrarForm", false);

                request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
                break;
            }

            case "nuevo": {
                List<ReporteMantenimiento> lista = reporteDAO.listar();
                List<TipoInconveniente> tipos = tipoDAO.listar();

                request.setAttribute("listaReportes", lista);
                request.setAttribute("tiposInconveniente", tipos);
                request.setAttribute("mostrarForm", true);

                request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
                break;
            }

            case "eliminar": {
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    reporteDAO.eliminarReporte(id);
                    response.sendRedirect("ControladorReporteMantenimiento?accion=listar&success=deleted");
                } else {
                    response.sendRedirect("ControladorReporteMantenimiento?accion=listar&error=invalid_id");
                }
                break;
            }

            default:
                response.sendRedirect("ControladorReporteMantenimiento?accion=listar");
        }
    }

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
                case "guardar": {
                    int idTipo = Integer.parseInt(request.getParameter("idTipoInconveniente"));
                    String fechaHoraStr = request.getParameter("fechaHoraIncidente");
                    String descripcion = request.getParameter("descripcion");

                    // 🔸 RN2: Validación de campos obligatorios
                    if (idTipo <= 0 || fechaHoraStr == null || fechaHoraStr.isEmpty()
                            || descripcion == null || descripcion.trim().isEmpty()) {

                        request.setAttribute("error", "Debe completar todos los campos del formulario.");
                        request.setAttribute("tiposInconveniente", tipoDAO.listar());
                        request.setAttribute("listaReportes", reporteDAO.listar());
                        request.setAttribute("mostrarForm", true);
                        request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
                        return;
                    }

                    Timestamp fechaHora = Timestamp.valueOf(fechaHoraStr.replace("T", " ") + ":00");

                    // 🧱 Crear el reporte de mantenimiento
                    ReporteMantenimiento r = new ReporteMantenimiento();
                    r.setIdTipoInconveniente(idTipo);
                    r.setIdResidente(usuarioSesion.getIdUsuario());
                    r.setDescripcion(descripcion.trim());
                    r.setFechaHoraIncidente(fechaHora);
                    r.setCreadoPor(usuarioSesion.getIdUsuario());

                    boolean exito = reporteDAO.crearReporte(r);

                    if (exito) {
                        // 🔹 RN3: Notificar a administradores
                        List<Usuarios> admins = usuarioDAO.listarPorRolActivo("Administrador");

                        TipoInconveniente tipo = tipoDAO.listar()
                                .stream()
                                .filter(t -> t.getIdTipoInconveniente() == idTipo)
                                .findFirst()
                                .orElse(null);

                        String asunto = "Reporte de mantenimiento";
                        String cuerpo = "El residente " + usuarioSesion.getNombre() + " " + usuarioSesion.getApellidos()
                                + " ha ingresado un reporte de error del sistema.\n\n"
                                + "Detalle del reporte:\n"
                                + "• Tipo inconveniente: " + (tipo != null ? tipo.getNombre() : "Desconocido") + "\n"
                                + "• Descripción: " + descripcion + "\n"
                                + "• Fecha y hora de incidente: " + fechaHoraStr.replace('T', ' ') + "\n\n"
                                + "Por favor, tomar las acciones correspondientes.";

                        for (Usuarios a : admins) {
                            EmailSender.enviarConAdjunto(a.getCorreo(), asunto, cuerpo, null);

                            Notificacion n = new Notificacion();
                            n.setIdGuardia(a.getIdUsuario()); // o IdAdministrador según tu modelo
                            n.setAsunto(asunto);
                            n.setCuerpo(cuerpo);
                            n.setCreadoPor(usuarioSesion.getIdUsuario());
                            notificacionDAO.registrar(n);
                        }

                        response.sendRedirect("ControladorReporteMantenimiento?accion=listar&success=true");
                        return;

                    } else {
                        request.setAttribute("error", "No se pudo guardar el reporte. Intente nuevamente.");
                        request.setAttribute("tiposInconveniente", tipoDAO.listar());
                        request.setAttribute("listaReportes", reporteDAO.listar());
                        request.setAttribute("mostrarForm", true);
                        request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
                    }
                    break;
                }

                case "actualizar": { // opcional (para futuras ediciones)
                    int idReporte = Integer.parseInt(request.getParameter("idReporte"));
                    int idTipo = Integer.parseInt(request.getParameter("idTipoInconveniente"));
                    String fechaHoraStr = request.getParameter("fechaHoraIncidente");
                    String descripcion = request.getParameter("descripcion");

                    if (idReporte <= 0 || idTipo <= 0 || fechaHoraStr == null || fechaHoraStr.isEmpty()
                            || descripcion == null || descripcion.trim().isEmpty()) {

                        request.setAttribute("error", "Debe completar todos los campos para actualizar.");
                        request.setAttribute("tiposInconveniente", tipoDAO.listar());
                        request.setAttribute("listaReportes", reporteDAO.listar());
                        request.setAttribute("mostrarForm", true);
                        request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
                        return;
                    }

                    Timestamp fechaHora = Timestamp.valueOf(fechaHoraStr.replace("T", " ") + ":00");

                    ReporteMantenimiento r = new ReporteMantenimiento();
                    r.setIdReporte(idReporte);
                    r.setIdTipoInconveniente(idTipo);
                    r.setDescripcion(descripcion.trim());
                    r.setFechaHoraIncidente(fechaHora);
                    r.setModificadoPor(usuarioSesion.getIdUsuario());

                    boolean ok = reporteDAO.actualizarReporte(r);

                    if (ok) {
                        response.sendRedirect("ControladorReporteMantenimiento?accion=listar&updated=true");
                    } else {
                        request.setAttribute("error", "No se pudo actualizar el reporte.");
                        request.setAttribute("tiposInconveniente", tipoDAO.listar());
                        request.setAttribute("listaReportes", reporteDAO.listar());
                        request.setAttribute("mostrarForm", true);
                        request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
                    }
                    break;
                }

                default:
                    response.sendRedirect("ControladorReporteMantenimiento?accion=listar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error: " + e.getMessage());
            request.setAttribute("tiposInconveniente", tipoDAO.listar());
            request.setAttribute("listaReportes", reporteDAO.listar());
            request.setAttribute("mostrarForm", true);
            request.getRequestDispatcher(INDEX_VISTA).forward(request, response);
        }
    }
}
