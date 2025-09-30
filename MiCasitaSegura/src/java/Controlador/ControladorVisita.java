package Controlador;

import Modelo.Visitas;
import Modelo.Usuarios;
import ModeloDAO.VisitaDAO;
import ModeloDAO.UsuarioDAO;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ControladorVisita")
public class ControladorVisita extends HttpServlet {

    String listar = "vistas/Visitas/Index.jsp";
    String addEdit = "vistas/Visitas/addEdit.jsp";

    VisitaDAO dao = new VisitaDAO();
    UsuarioDAO usuarioDao = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String acceso = "";
        String action = request.getParameter("accion");

        if ("listar".equalsIgnoreCase(action)) {
            List<Visitas> listaVisitas = dao.listar();
            request.setAttribute("visitas", listaVisitas);
            acceso = listar;

        } else if ("add".equalsIgnoreCase(action)) {
            request.setAttribute("visita", null);
            acceso = addEdit;

        } else if ("edit".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Visitas visita = dao.listarId(id);
            request.setAttribute("visita", visita);
            acceso = addEdit;

        } else if ("cancelar".equalsIgnoreCase(action)) { // ⚡ FA06
            int id = Integer.parseInt(request.getParameter("id"));
            dao.cancelar(id); // cambia estado y desactiva QR
            request.setAttribute("visitas", dao.listar());
            acceso = listar;

        } else if ("descargarQR".equalsIgnoreCase(action)) { // ⚡ FA05
            int id = Integer.parseInt(request.getParameter("id"));
            dao.descargarQR(id, response); // escribe el PNG al response
            return; // importante: no hacer forward

        } else {
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
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario"); // logueado

        if ("add".equalsIgnoreCase(action)) {
            String nombreVisitante = request.getParameter("nombreVisitante");
            String dpiVisitante = request.getParameter("dpiVisitante");
            String correoVisitante = request.getParameter("correoVisitante");
            int idResidente = Integer.parseInt(request.getParameter("idResidente"));
            String tipoVisita = request.getParameter("tipoVisita");

            Date fechaVisita = null;
            if (request.getParameter("fechaVisita") != null && !request.getParameter("fechaVisita").isEmpty()) {
                fechaVisita = Date.valueOf(request.getParameter("fechaVisita"));
            }

            Integer intentos = null;
            if (request.getParameter("intentosPermitidos") != null && !request.getParameter("intentosPermitidos").isEmpty()) {
                intentos = Integer.parseInt(request.getParameter("intentosPermitidos"));
            }

            boolean estado = Boolean.parseBoolean(request.getParameter("estado"));

            Visitas v = new Visitas();
            v.setNombreVisitante(nombreVisitante);
            v.setDpiVisitante(dpiVisitante);
            v.setCorreoVisitante(correoVisitante);
            v.setIdResidente(idResidente);
            v.setIdUsuarioCreador(usuarioSesion.getIdUsuario()); // ⚡ RN2
            v.setTipoVisita(tipoVisita);
            v.setFechaVisita(fechaVisita);
            v.setIntentosPermitidos(intentos);
            v.setEstado(estado);

            dao.add(v);

        } else if ("edit".equalsIgnoreCase(action)) {
            int idVisita = Integer.parseInt(request.getParameter("idVisita"));
            String nombreVisitante = request.getParameter("nombreVisitante");
            String dpiVisitante = request.getParameter("dpiVisitante");
            String correoVisitante = request.getParameter("correoVisitante");
            int idResidente = Integer.parseInt(request.getParameter("idResidente"));
            String tipoVisita = request.getParameter("tipoVisita");

            Date fechaVisita = null;
            if (request.getParameter("fechaVisita") != null && !request.getParameter("fechaVisita").isEmpty()) {
                fechaVisita = Date.valueOf(request.getParameter("fechaVisita"));
            }

            Integer intentos = null;
            if (request.getParameter("intentosPermitidos") != null && !request.getParameter("intentosPermitidos").isEmpty()) {
                intentos = Integer.parseInt(request.getParameter("intentosPermitidos"));
            }

            boolean estado = Boolean.parseBoolean(request.getParameter("estado"));

            Visitas v = new Visitas();
            v.setIdVisita(idVisita);
            v.setNombreVisitante(nombreVisitante);
            v.setDpiVisitante(dpiVisitante);
            v.setCorreoVisitante(correoVisitante);
            v.setIdResidente(idResidente);
            v.setIdUsuarioCreador(usuarioSesion.getIdUsuario()); // ⚡ RN2
            v.setTipoVisita(tipoVisita);
            v.setFechaVisita(fechaVisita);
            v.setIntentosPermitidos(intentos);
            v.setEstado(estado);

            dao.edit(v);
        }

        response.sendRedirect("ControladorVisita?accion=listar");
    }
    
    
    
}
