package Controlador;

import Modelo.Conversacion;
import Modelo.Mensaje;
import Modelo.Usuarios;
import ModeloDAO.ConversacionDAO;
import ModeloDAO.MensajeDAO;
import ModeloDAO.UsuarioDAO;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/ControladorMensaje")
public class ControladorMensaje extends HttpServlet {

    private final MensajeDAO mensajeDAO = new MensajeDAO();
    private final ConversacionDAO conversacionDAO = new ConversacionDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        switch (accion == null ? "" : accion) {
            case "listar":
                try {
                    int idConversacion = Integer.parseInt(request.getParameter("idConversacion"));

                    // Obtener mensajes de la conversación
                    List<Mensaje> mensajes = mensajeDAO.listarMensajesPorConversacion(idConversacion);

                    // Obtener detalles de la conversación
                    Conversacion c = conversacionDAO.obtenerPorId(idConversacion);
                    if (c == null) {
                        response.sendRedirect("ControladorConversacion?accion=listar");
                        return;
                    }

                    // Enviar datos a la vista
                    request.setAttribute("mensajes", mensajes);
                    request.setAttribute("idConversacion", idConversacion);
                    request.setAttribute("idResidente", c.getIdResidente());
                    request.setAttribute("idAgente", c.getIdAgente());

                    // Determinar quién es el contacto (el otro usuario)
                    int idContacto = (usuarioSesion.getIdUsuario() == c.getIdResidente())
                            ? c.getIdAgente()
                            : c.getIdResidente();
                    Usuarios contacto = usuarioDAO.obtenerPorId(idContacto);
                    request.setAttribute("contacto", contacto);

                    // Ir al chat
                    RequestDispatcher vista = request.getRequestDispatcher("vistas/Comunicacion/Chat.jsp");
                    vista.forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("ControladorConversacion?accion=listar");
                }
                break;

            default:
                response.sendRedirect("ControladorConversacion?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if ("enviar".equalsIgnoreCase(accion)) {
                int idConversacion = Integer.parseInt(request.getParameter("idConversacion"));
                int idReceptor = Integer.parseInt(request.getParameter("idReceptor"));
                String contenido = request.getParameter("contenido");

                if (contenido == null || contenido.trim().isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("El mensaje no puede estar vacío.");
                    return;
                }

                // Crear y guardar el mensaje
                Mensaje mensaje = new Mensaje();
                mensaje.setIdConversacion(idConversacion);
                mensaje.setIdEmisor(usuarioSesion.getIdUsuario());
                mensaje.setIdReceptor(idReceptor);
                mensaje.setContenido(contenido.trim());

                boolean enviado = mensajeDAO.enviarMensaje(mensaje);

                if (enviado) {
                    // 🔹 Enviar notificación en tiempo real (WebSocket)
                    ChatWebSocket.enviarMensajeEnTiempoReal(
                            String.valueOf(idReceptor),
                            "{\"from\":\"" + usuarioSesion.getIdUsuario() +
                            "\", \"to\":\"" + idReceptor +
                            "\", \"text\":\"" + contenido.replace("\"", "\\\"") + "\"}"
                    );

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Mensaje enviado con éxito");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Error al enviar el mensaje.");
                }

            } else {
                response.sendRedirect("ControladorConversacion?accion=listar");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error al procesar mensaje: " + ex.getMessage());
        }
    }
}
