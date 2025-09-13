package Controlador;

import Modelo.Usuarios;
import ModeloDAO.UsuarioDAO;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        
        UsuarioDAO dao = new UsuarioDAO();
        Usuarios user = dao.login(correo, contrasena);
        
        if (user != null && user.isEstado()) {
            // Usuario válido y activo
            HttpSession session = request.getSession();
            session.setAttribute("usuario", user);
            
            // AQUÍ registrar inicio de sesión exitoso en auditoría
            try {
                dao.registrarAccion(user.getIdUsuario(), "Inicio de sesión");
            } catch (Exception e) {
                // Log del error pero no interrumpir el flujo del login
                System.err.println("Error al registrar acción de login: " + e.getMessage());
            }
            
            // Redirigir al main.jsp (sidebar + contenido)
            response.sendRedirect(request.getContextPath() + "/vistas/Sidebar.jsp");
            
        } else {
            // Login fallido - registrar intento fallido si quieres
            if (user != null) {
                // Usuario existe pero está inactivo
                try {
                    dao.registrarAccion(user.getIdUsuario(), "Intento de login - usuario inactivo");
                } catch (Exception e) {
                    System.err.println("Error al registrar intento fallido: " + e.getMessage());
                }
            }
            // Si user es null, significa credenciales incorrectas
            
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}