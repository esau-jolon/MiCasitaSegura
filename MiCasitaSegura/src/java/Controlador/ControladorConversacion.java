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

    // 🔹 Rutas de vistas JSP
    private final String listar = "vistas/Comunicacion/Conversaciones.jsp";
    private final String crear = "vistas/Comunicacion/NuevaConversacion.jsp";

    // 🔹 DAOs
    private final ConversacionDAO conversacionDAO = new ConversacionDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // ==============================================================
    // MÉTODO GET: mostrar vistas o listar datos
    // ==============================================================
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
                    List<Usuarios> agentes = usuarioDAO.listarGuardias();
                    request.setAttribute("agentes", agentes);
                    acceso = crear;
                } else {
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

    // ==============================================================
    // MÉTODO POST: guardar o procesar formularios
    // ==============================================================
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
                case "guardar": {
                    int idAgente = Integer.parseInt(request.getParameter("idAgente"));
                    int idResidente = usuarioSesion.getIdUsuario();

                    // ✅ Validar si ya existe una conversación entre ambos
                    boolean existe = conversacionDAO.existeConversacion(idResidente, idAgente);

                    if (existe) {
                        request.setAttribute("mensajeError", "Ya existe una conversación con el usuario seleccionado.");

                        // Volver a cargar lista de agentes
                        List<Usuarios> agentes = usuarioDAO.listarGuardias();
                        request.setAttribute("agentes", agentes);

                        RequestDispatcher rd = request.getRequestDispatcher(crear);
                        rd.forward(request, response);
                        return;
                    }

                    // 🔹 Crear nueva conversación
                    Conversacion nueva = new Conversacion();
                    nueva.setIdResidente(idResidente);
                    nueva.setIdAgente(idAgente);
                    nueva.setEstado(true); // ahora boolean
                    nueva.setCreadoPor(idResidente); // auditoría

                    boolean creada = conversacionDAO.crearConversacion(nueva);

                    if (creada) {
                        // Registrar acción opcional si lo manejas en tabla auditoría
                        usuarioDAO.registrarAccion(idResidente,
                                "Creó una conversación con el agente ID " + idAgente);
                        response.sendRedirect("ControladorConversacion?accion=listar");
                    } else {
                        request.setAttribute("mensajeError", "No se pudo crear la conversación.");
                        List<Usuarios> agentes = usuarioDAO.listarGuardias();
                        request.setAttribute("agentes", agentes);
                        RequestDispatcher rd = request.getRequestDispatcher(crear);
                        rd.forward(request, response);
                    }
                    break;
                }

                default:
                    response.sendRedirect("ControladorConversacion?accion=listar");
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("mensajeError", "Error al procesar la solicitud: " + ex.getMessage());
            List<Usuarios> agentes = usuarioDAO.listarGuardias();
            request.setAttribute("agentes", agentes);
            RequestDispatcher rd = request.getRequestDispatcher(crear);
            rd.forward(request, response);
        }
    }
}
