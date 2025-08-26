package Controlador;

import Modelo.Usuarios;
import ModeloDAO.UsuarioDAO;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControladorUsuario extends HttpServlet {

    String listar = "vistas/listarUsuarios.jsp";
    String add = "vistas/usuarioForm.jsp";  // lo usas para agregar
    String edit = "vistas/usuarioForm.jsp"; // el mismo form sirve para editar
    Usuarios u = new Usuarios();
    UsuarioDAO dao = new UsuarioDAO();
    int id;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acceso = "";
        String action = request.getParameter("accion");

        if (action.equalsIgnoreCase("listar")) {
            acceso = listar;

        } else if (action.equalsIgnoreCase("add")) {
            // Muestra formulario vacío
            acceso = add;

        } else if (action.equalsIgnoreCase("Agregar")) {
            // Captura datos del formulario para crear
            String dpi = request.getParameter("txtDpi");
            String nombre = request.getParameter("txtNombre");
            String apellidos = request.getParameter("txtApellidos");
            String correo = request.getParameter("txtCorreo");
            String contrasena = request.getParameter("txtContrasena");
            int rolId = Integer.parseInt(request.getParameter("txtRolId"));
            Integer numeroCasaId = request.getParameter("txtCasaId").isEmpty() ? null : Integer.parseInt(request.getParameter("txtCasaId"));
            Integer loteId = request.getParameter("txtLoteId").isEmpty() ? null : Integer.parseInt(request.getParameter("txtLoteId"));
            boolean estado = request.getParameter("txtEstado") != null;

            u = new Usuarios(dpi, nombre, apellidos, correo, contrasena, rolId, numeroCasaId, loteId, estado);
            dao.add(u);

            acceso = listar;

        } else if (action.equalsIgnoreCase("editar")) {
            // Pasa el ID al JSP
            request.setAttribute("idUsuario", request.getParameter("id"));
            acceso = edit;

        } else if (action.equalsIgnoreCase("Actualizar")) {
            id = Integer.parseInt(request.getParameter("txtId"));
            String dpi = request.getParameter("txtDpi");
            String nombre = request.getParameter("txtNombre");
            String apellidos = request.getParameter("txtApellidos");
            String correo = request.getParameter("txtCorreo");
            String contrasena = request.getParameter("txtContrasena");
            int rolId = Integer.parseInt(request.getParameter("txtRolId"));
            Integer numeroCasaId = request.getParameter("txtCasaId").isEmpty() ? null : Integer.parseInt(request.getParameter("txtCasaId"));
            Integer loteId = request.getParameter("txtLoteId").isEmpty() ? null : Integer.parseInt(request.getParameter("txtLoteId"));
            boolean estado = request.getParameter("txtEstado") != null;

            u.setIdUsuario(id);
            u.setDpi(dpi);
            u.setNombre(nombre);
            u.setApellidos(apellidos);
            u.setCorreo(correo);
            u.setContrasena(contrasena);
            u.setRolId(rolId);
            u.setNumeroCasaId(numeroCasaId);
            u.setLoteId(loteId);
            u.setEstado(estado);

            dao.edit(u);

            acceso = listar;

        } else if (action.equalsIgnoreCase("eliminar")) {
            id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
            acceso = listar;
        }

        RequestDispatcher vista = request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // reutilizamos la lógica
    }
}
