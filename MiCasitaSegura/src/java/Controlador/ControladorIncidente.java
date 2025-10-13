package Controlador;

import Modelo.Incidente;
import Modelo.TipoIncidente;
import Modelo.Usuarios;
import Modelo.Notificacion;
import ModeloDAO.IncidenteDAO;
import ModeloDAO.TipoIncidenteDAO;
import ModeloDAO.UsuarioDAO;
import ModeloDAO.NotificacionDAO;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ControladorIncidente")
public class ControladorIncidente extends HttpServlet {

    private final String listarVista = "vistas/Comunicacion/Incidentes.jsp";
    private final String nuevoVista = "vistas/Comunicacion/NuevoIncidente.jsp";

    private final IncidenteDAO incidenteDAO = new IncidenteDAO();
    private final TipoIncidenteDAO tipoDAO = new TipoIncidenteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        String accion = request.getParameter("accion");
        String acceso = "";

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 🔹 Mostrar lista de incidentes del residente
        if ("listar".equalsIgnoreCase(accion)) {
            List<Incidente> incidentes = incidenteDAO.listarPorResidente(usuarioSesion.getIdUsuario());
            request.setAttribute("listaIncidentes", incidentes);
            acceso = listarVista;

            // 🔹 Mostrar formulario para nuevo incidente
        } else if ("nuevo".equalsIgnoreCase(accion)) {
            List<TipoIncidente> tipos = tipoDAO.listar();
            request.setAttribute("tiposIncidente", tipos);
            acceso = nuevoVista;
        }

        RequestDispatcher vista = request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
        String accion = request.getParameter("accion");

        if ("guardar".equalsIgnoreCase(accion)) {
            try {
                int idTipo = Integer.parseInt(request.getParameter("idTipoIncidente"));
                String fechaHoraStr = request.getParameter("fechaHoraIncidente");
                String descripcion = request.getParameter("descripcion");

                // 🔸 Validación de campos
                if (idTipo <= 0 || fechaHoraStr == null || fechaHoraStr.isEmpty() || descripcion == null || descripcion.isEmpty()) {
                    request.setAttribute("error", "Debe completar todos los campos del formulario.");
                    request.setAttribute("tiposIncidente", tipoDAO.listar());
                    request.getRequestDispatcher(nuevoVista).forward(request, response);
                    return;
                }

                Timestamp fechaHora = Timestamp.valueOf(fechaHoraStr.replace("T", " ") + ":00");

                // 🧱 Crear el incidente
                Incidente inc = new Incidente();
                inc.setIdResidente(usuarioSesion.getIdUsuario());
                inc.setIdTipoIncidente(idTipo);
                inc.setFechaHoraIncidente(fechaHora);
                inc.setDescripcion(descripcion);
                inc.setCreadoPor(usuarioSesion.getIdUsuario());

                boolean exito = incidenteDAO.crearIncidente(inc);

                if (exito) {
                    // 🔹 RN4: Enviar correo y notificación a guardias
                    List<Usuarios> guardias = usuarioDAO.listarPorRolActivo("Guardia");

                    TipoIncidente tipo = tipoDAO.listar()
                            .stream()
                            .filter(t -> t.getIdTipoIncidente() == idTipo)
                            .findFirst()
                            .orElse(null);

                    String asunto = "Reporte de incidente";
                    String cuerpo = "Se le informa que el residente " + usuarioSesion.getNombre() + " " + usuarioSesion.getApellidos()
                            + ", que vive en casa #" + (usuarioSesion.getNumeroCasa() != null ? usuarioSesion.getNumeroCasa() : "N/A")
                            + ", ha reportado un incidente.\n\n"
                            + "📌 Tipo: " + (tipo != null ? tipo.getNombre() : "Desconocido") + "\n"
                            + "🕒 Fecha y hora: " + fechaHoraStr.replace("T", " ") + "\n"
                            + "📝 Descripción: " + descripcion + "\n\n"
                            + "Por favor, tomar las acciones correspondientes.";

                    // 📧 Enviar correo y registrar notificación para cada guardia activo
                    for (Usuarios g : guardias) {
                        EmailSender.enviarConAdjunto(g.getCorreo(), asunto, cuerpo, null);

                        Notificacion n = new Notificacion();
                        n.setIdGuardia(g.getIdUsuario());
                        n.setIdIncidente(inc.getIdIncidente());
                        n.setAsunto(asunto);
                        n.setCuerpo(cuerpo);
                        n.setCreadoPor(usuarioSesion.getIdUsuario());
                        notificacionDAO.registrar(n);
                    }

                    response.sendRedirect("ControladorIncidente?accion=listar&success=true");
                    return;
                } else {
                    request.setAttribute("error", "No se pudo crear el incidente. Intente nuevamente.");
                    request.setAttribute("tiposIncidente", tipoDAO.listar());
                    request.getRequestDispatcher(nuevoVista).forward(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error al guardar el incidente: " + e.getMessage());
                request.setAttribute("tiposIncidente", tipoDAO.listar());
                request.getRequestDispatcher(nuevoVista).forward(request, response);
            }
        }
    }
}
