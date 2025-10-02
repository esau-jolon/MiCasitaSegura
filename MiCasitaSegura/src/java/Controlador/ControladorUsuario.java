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
import javax.servlet.http.HttpSession;

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
        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario"); // 👈 usuario logueado

        try {
            if ("add".equalsIgnoreCase(action)) {
                // === Datos comunes ===
                String dpi = request.getParameter("dpi");
                String nombre = request.getParameter("nombre");
                String apellidos = request.getParameter("apellidos");
                String correo = request.getParameter("correo");
                String contrasena = request.getParameter("contrasena");
                int rolId = Integer.parseInt(request.getParameter("rolId"));

                // --- Lee casa/lote protegiendo null ---
                String casaParam = request.getParameter("numeroCasaId");
                Integer numeroCasaId = (casaParam == null || casaParam.isEmpty())
                        ? null : Integer.parseInt(casaParam);

                String loteParam = request.getParameter("loteId");
                Integer loteId = (loteParam == null || loteParam.isEmpty())
                        ? null : Integer.parseInt(loteParam);

                boolean estado = Boolean.parseBoolean(request.getParameter("estado"));

                // 🚨 Validar duplicados
                if (dao.existeDpiOCorreo(dpi, correo, null)) {
                    request.setAttribute("error", "El DPI o el correo ya están registrados.");
                    request.setAttribute("usuario", null);
                    request.setAttribute("catalogoCasas", new CasaDAO().listar());
                    request.setAttribute("catalogoLotes", new LoteDAO().listar());
                    request.setAttribute("catalogoRoles", new RoleDAO().listar());
                    request.getRequestDispatcher(addEdit).forward(request, response);
                    return;
                }

                // --- Si rol es guardia, forzar null ---
                final int ID_ROL_GUARDIA = 3;
                if (rolId == ID_ROL_GUARDIA) {
                    numeroCasaId = null;
                    loteId = null;
                }
                Usuarios u = new Usuarios();
                u.setDpi(dpi);
                u.setNombre(nombre);
                u.setApellidos(apellidos);
                u.setCorreo(correo);
                u.setContrasena(contrasena);
                u.setRolId(rolId);
                u.setNumeroCasaId(numeroCasaId);
                u.setLoteId(loteId);
                u.setEstado(estado);
                u.setCreadoPor(usuarioSesion.getIdUsuario()); // 👈 auditoría

                // 👇 Auditoría: quién creó el usuario
                if (usuarioSesion != null) {
                    u.setCreadoPor(usuarioSesion.getIdUsuario());
                }

                dao.add(u);

            } else if ("edit".equalsIgnoreCase(action)) {
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                String dpi = request.getParameter("dpi");
                String nombre = request.getParameter("nombre");
                String apellidos = request.getParameter("apellidos");
                String correo = request.getParameter("correo");
                String contrasena = request.getParameter("contrasena");

                int rolId = Integer.parseInt(request.getParameter("rolId"));

                String casaParam = request.getParameter("numeroCasaId");
                Integer numeroCasaId = (casaParam == null || casaParam.isEmpty())
                        ? null : Integer.parseInt(casaParam);

                String loteParam = request.getParameter("loteId");
                Integer loteId = (loteParam == null || loteParam.isEmpty())
                        ? null : Integer.parseInt(loteParam);

                boolean estado = Boolean.parseBoolean(request.getParameter("estado"));

                // 🚨 Validar duplicados excluyendo al propio usuario
                if (dao.existeDpiOCorreo(dpi, correo, idUsuario)) {
                    request.setAttribute("error", "El DPI o el correo ya están registrados.");
                    request.setAttribute("usuario", dao.listarId(idUsuario));
                    request.setAttribute("catalogoCasas", new CasaDAO().listar());
                    request.setAttribute("catalogoLotes", new LoteDAO().listar());
                    request.setAttribute("catalogoRoles", new RoleDAO().listar());
                    request.getRequestDispatcher(addEdit).forward(request, response);
                    return;
                }

                final int ID_ROL_GUARDIA = 3;
                if (rolId == ID_ROL_GUARDIA) {
                    numeroCasaId = null;
                    loteId = null;
                }

                Usuarios u = dao.listarId(idUsuario); // usuario existente
                u.setDpi(dpi);
                u.setNombre(nombre);
                u.setApellidos(apellidos);
                u.setCorreo(correo);

                if (contrasena != null && !contrasena.trim().isEmpty()) {
                    u.setContrasena(contrasena);
                }

                u.setRolId(rolId);
                u.setNumeroCasaId(numeroCasaId);
                u.setLoteId(loteId);
                u.setEstado(estado);

                // 👇 Auditoría: quién modificó
                if (usuarioSesion != null) {
                    u.setModificadoPor(usuarioSesion.getIdUsuario());
                }

                dao.edit(u);
            }

            response.sendRedirect("ControladorUsuario?accion=listar");

        } catch (NumberFormatException ex) {
            throw new ServletException("Formato numérico inválido en los parámetros.", ex);
        }
    }

}
