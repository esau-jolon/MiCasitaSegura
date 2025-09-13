package Controlador;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Obtener la sesión actual, si existe
        if (session != null) {
            // Registrar cierre de sesión en auditoría si quieres
            Object usuarioObj = session.getAttribute("usuario");
            if (usuarioObj != null) {
                Modelo.Usuarios usuario = (Modelo.Usuarios) usuarioObj;
                ModeloDAO.UsuarioDAO dao = new ModeloDAO.UsuarioDAO();
                dao.registrarAccion(usuario.getIdUsuario(), "Cierre de sesión");
            }

            session.invalidate(); // Cerrar sesión
        }

        // Redirigir al login
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
