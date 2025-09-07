package Controlador;

import Modelo.Usuarios;
import ModeloDAO.CasaDAO;
import ModeloDAO.LoteDAO;
import ModeloDAO.UsuarioDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControladorDirectorio", urlPatterns = {"/ControladorDirectorio"})
public class ControladorDirectorio extends HttpServlet {

    UsuarioDAO usuarioDAO = new UsuarioDAO();
    CasaDAO casaDAO = new CasaDAO();
    LoteDAO loteDAO = new LoteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        // Cargamos siempre los catálogos
        request.setAttribute("casas", casaDAO.listar());
        request.setAttribute("lotes", loteDAO.listar());

        if ("listar".equals(accion)) {
            request.setAttribute("usuarios", usuarioDAO.listar());
            request.getRequestDispatcher("vistas/Directorio/Index.jsp").forward(request, response);

        } else if ("buscar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String loteStr = request.getParameter("lote");
            String casaStr = request.getParameter("numeroCasa");

            // Validación: al menos nombre o apellido
            if ((nombre == null || nombre.isEmpty()) && (apellidos == null || apellidos.isEmpty())) {
                request.setAttribute("mensaje", "Debe ingresar al menos Nombre o Apellido para buscar.");
                request.getRequestDispatcher("vistas/Directorio/Index.jsp").forward(request, response);
                return;
            }

            // Validación FA3 combinada: lote y casa
            boolean loteSeleccionado = loteStr != null && !loteStr.isEmpty();
            boolean casaSeleccionada = casaStr != null && !casaStr.isEmpty();

            if ((loteSeleccionado && !casaSeleccionada) || (casaSeleccionada && !loteSeleccionado)) {
                request.setAttribute("mensaje", "Si selecciona un lote debe seleccionar un número de casa, y viceversa.");
                request.getRequestDispatcher("vistas/Directorio/Index.jsp").forward(request, response);
                return;
            }

            Integer loteId = loteSeleccionado ? Integer.parseInt(loteStr) : null;
            Integer casaId = casaSeleccionada ? Integer.parseInt(casaStr) : null;

            List<Usuarios> lista = usuarioDAO.buscar(nombre, apellidos, loteId, casaId);

            if (lista == null || lista.isEmpty()) {
                request.setAttribute("mensaje", "No se encontró ningún usuario con los datos ingresados.");
            }

            request.setAttribute("usuarios", lista);
            request.getRequestDispatcher("vistas/Directorio/Index.jsp").forward(request, response);

        } else {
            response.sendRedirect("ControladorDirectorio?accion=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
