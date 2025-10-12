package Controlador;

import Modelo.Conversacion;
import Modelo.Usuarios;
import ModeloDAO.ConversacionDAO;
import ModeloDAO.UsuarioDAO;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/ControladorConversacion")
public class ControladorConversacion extends HttpServlet {

    // Rutas a las vistas JSP
    private final String listar = "vistas/Comunicacion/Conversaciones.jsp";
    private final String crear = "vistas/Comunicacion/NuevaConversacion.jsp";

    // DAOs
    private final ConversacionDAO conversacionDAO = new ConversacionDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acceso = "";
        String action = request.getParameter("accion");

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        // Si no hay sesión, redirigir al login
        if (usuarioSesion == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        switch (action == null ? "" : action) {

            // 🔹 LISTAR CONVERSACIONES
            case "listar":
                List<Conversacion> conversaciones = conversacionDAO.listarPorUsuario(usuarioSesion.getIdUsuario());
                request.setAttribute("conversaciones", conversaciones);
                acceso = listar;
                break;

            // 🔹 CREAR NUEVA CONVERSACIÓN (solo residentes)
            case "crear":
                if ("Residente".equalsIgnoreCase(usuarioSesion.getNombreRol())) {
                    // ✅ Ahora sí: lista solo guardias (rol_id = 2)
                    List<Usuarios> agentes = usuarioDAO.listarGuardias();
                    request.setAttribute("agentes", agentes);
                    acceso = crear;
                } else {
                    // Si no es residente, no puede crear
                    request.setAttribute("mensajeError", "Solo los residentes pueden crear conversaciones.");
                    acceso = listar;
                }
                break;

            // 🔹 CANCELAR CREACIÓN
            case "cancelar":
                acceso = listar;
                break;

            default:
                acceso = listar;
                break;
        }

        RequestDispatcher vista = request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("accion");
        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            switch (action) {
                // 🔹 GUARDAR NUEVA CONVERSACIÓN
                case "guardar":
                    int idAgente = Integer.parseInt(request.getParameter("idAgente"));
                    int idResidente = usuarioSesion.getIdUsuario();

                    // Validar si ya existe conversación con este agente
                    List<Conversacion> existentes = conversacionDAO.listarPorUsuario(idResidente);
                    boolean existe = existentes.stream().anyMatch(c -> c.getIdAgente() == idAgente);

                    if (existe) {
                        // FA4 - Conversación ya existente
                        request.setAttribute("mensajeError", "Ya existe una conversación con este agente.");
                        request.setAttribute("conversaciones", conversacionDAO.listarPorUsuario(idResidente));
                        RequestDispatcher rd = request.getRequestDispatcher(listar);
                        rd.forward(request, response);
                        return;
                    }

                    // Crear nueva conversación
                    Conversacion c = new Conversacion(idResidente, idAgente, "Activa");
                    boolean creada = conversacionDAO.crearConversacion(c, idResidente);

                    if (creada) {
                        usuarioDAO.registrarAccion(idResidente, "Creó conversación con agente ID " + idAgente);
                        response.sendRedirect("ControladorConversacion?accion=listar");
                    } else {
                        request.setAttribute("mensajeError", "No se pudo crear la conversación.");
                        RequestDispatcher rd = request.getRequestDispatcher(crear);
                        rd.forward(request, response);
                    }
                    break;

                default:
                    response.sendRedirect("ControladorConversacion?accion=listar");
                    break;
            }

        } catch (Exception ex) {
            request.setAttribute("mensajeError", "Error al procesar la solicitud: " + ex.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher(crear);
            rd.forward(request, response);
        }
    }
}
