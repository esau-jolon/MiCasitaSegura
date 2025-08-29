package Controlador;

import Modelo.Usuarios;
import ModeloDAO.CasaDAO;
import ModeloDAO.LoteDAO;
import ModeloDAO.UsuarioDAO;
import ModeloDAO.RoleDAO;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.annotation.WebServlet;

@WebServlet("/ControladorUsuario")
public class ControladorUsuario extends HttpServlet {

    String listar = "vistas/Usuarios/Index.jsp";
    String addEdit = "vistas/Usuarios/addEdit.jsp";

    UsuarioDAO dao = new UsuarioDAO();
    int id;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String acceso = "";
        String action = request.getParameter("accion");

        CasaDAO casasDao = new CasaDAO();
        LoteDAO lotesDao = new LoteDAO();
        RoleDAO roleDao = new RoleDAO();

        if ("listar".equalsIgnoreCase(action)) {
            List<Usuarios> listaUsuarios = dao.listar();
            request.setAttribute("usuarios", listaUsuarios);
            acceso = listar;

        } else if ("add".equalsIgnoreCase(action)) {
            request.setAttribute("usuario", null);
            request.setAttribute("catalogoCasas", casasDao.listar());
            request.setAttribute("catalogoLotes", lotesDao.listar());
            request.setAttribute("catalogoRoles", roleDao.listar());
            acceso = addEdit;

        } else if ("edit".equalsIgnoreCase(action)) {
            id = Integer.parseInt(request.getParameter("id"));
            Usuarios usuario = dao.listarId(id);
            request.setAttribute("usuario", usuario);
            request.setAttribute("catalogoCasas", casasDao.listar());
            request.setAttribute("catalogoLotes", lotesDao.listar());
            request.setAttribute("catalogoRoles", roleDao.listar());
            acceso = addEdit;

        } else if ("delete".equalsIgnoreCase(action)) {
            id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
            // 🔹 Recargar lista después de borrar
            List<Usuarios> listaUsuarios = dao.listar();
            request.setAttribute("usuarios", listaUsuarios);
            acceso = listar;
        }

        RequestDispatcher vista = request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("accion");

        if ("add".equalsIgnoreCase(action)) {
            // Crear usuario
            String dpi = request.getParameter("dpi");
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            int rolId = Integer.parseInt(request.getParameter("rolId"));
            Integer numeroCasaId = request.getParameter("numeroCasaId").isEmpty() ? null : Integer.parseInt(request.getParameter("numeroCasaId"));
            Integer loteId = request.getParameter("loteId").isEmpty() ? null : Integer.parseInt(request.getParameter("loteId"));
            boolean estado = Boolean.parseBoolean(request.getParameter("estado"));

            Usuarios u = new Usuarios(dpi, nombre, apellidos, correo, contrasena, rolId, numeroCasaId, loteId, estado);
            dao.add(u);

        } else if ("edit".equalsIgnoreCase(action)) {
            // Actualizar usuario
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String dpi = request.getParameter("dpi");
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            int rolId = Integer.parseInt(request.getParameter("rolId"));
            Integer numeroCasaId = request.getParameter("numeroCasaId").isEmpty() ? null : Integer.parseInt(request.getParameter("numeroCasaId"));
            Integer loteId = request.getParameter("loteId").isEmpty() ? null : Integer.parseInt(request.getParameter("loteId"));
            boolean estado = Boolean.parseBoolean(request.getParameter("estado"));

            Usuarios u = new Usuarios();
            u.setIdUsuario(idUsuario);
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
        }

        // 🔹 Redirige siempre al listado después del POST
        response.sendRedirect("ControladorUsuario?accion=listar");
    }
}
