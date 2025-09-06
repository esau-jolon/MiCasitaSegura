/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

/**
 *
 * @author esauj
 */


import Modelo.Visitas;
import ModeloDAO.VisitaDAO;
import ModeloDAO.UsuarioDAO; // Para obtener nombres de residentes si quieres mostrarlos en combo

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.annotation.WebServlet;

@WebServlet("/ControladorVisita")
public class ControladorVisita extends HttpServlet {

    String listar = "vistas/Visitas/Index.jsp";
    String addEdit = "vistas/Visitas/addEdit.jsp";

    VisitaDAO dao = new VisitaDAO();
    int id;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String acceso = "";
        String action = request.getParameter("accion");

        UsuarioDAO residenteDao = new UsuarioDAO();

        if ("listar".equalsIgnoreCase(action)) {
            List<Visitas> listaVisitas = dao.listar();
            request.setAttribute("visitas", listaVisitas);
            acceso = listar;

        } else if ("add".equalsIgnoreCase(action)) {
            request.setAttribute("visita", null);
            request.setAttribute("catalogoResidentes", residenteDao.listar());
            acceso = addEdit;

        } else if ("edit".equalsIgnoreCase(action)) {
            id = Integer.parseInt(request.getParameter("id"));
            Visitas visita = dao.listarId(id);
            request.setAttribute("visita", visita);
            request.setAttribute("catalogoResidentes", residenteDao.listar());
            acceso = addEdit;

        } else if ("delete".equalsIgnoreCase(action)) {
            id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
            // Recargar lista después de borrar
            List<Visitas> listaVisitas = dao.listar();
            request.setAttribute("visitas", listaVisitas);
            acceso = listar;
        }

        RequestDispatcher vista = request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }
    /*
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("accion");

        if ("add".equalsIgnoreCase(action)) {
            String nombreVisitante = request.getParameter("nombreVisitante");
            String dpiVisitante = request.getParameter("dpiVisitante");
            String correoVisitante = request.getParameter("correoVisitante");
            int idResidente = Integer.parseInt(request.getParameter("idResidente"));
            String tipoVisita = request.getParameter("tipoVisita");
            String fechaVisita = request.getParameter("fechaVisita"); // Formato "yyyy-MM-dd"
            boolean estado = Boolean.parseBoolean(request.getParameter("estado"));

            Visitas = new Visitas(nombreVisitante, dpiVisitante, correoVisitante, idResidente, tipoVisita, fechaVisita, estado);
            dao.add(v);

        } else if ("edit".equalsIgnoreCase(action)) {
            int idVisita = Integer.parseInt(request.getParameter("idVisita"));
            String nombreVisitante = request.getParameter("nombreVisitante");
            String dpiVisitante = request.getParameter("dpiVisitante");
            String correoVisitante = request.getParameter("correoVisitante");
            int idResidente = Integer.parseInt(request.getParameter("idResidente"));
            String tipoVisita = request.getParameter("tipoVisita");
            String fechaVisita = request.getParameter("fechaVisita"); // Formato "yyyy-MM-dd"
            boolean estado = Boolean.parseBoolean(request.getParameter("estado"));

            Visitas v = new Visitas();
            v.setIdVisita(idVisita);
            v.setNombreVisitante(nombreVisitante);
            v.setDpiVisitante(dpiVisitante);
            v.setCorreoVisitante(correoVisitante);
            v.setIdResidente(idResidente);
            v.setTipoVisita(tipoVisita);
            v.setFechaVisita(fechaVisita);
            v.setEstado(estado);

            dao.edit(v);
        }

        // Redirige siempre al listado después del POST
        response.sendRedirect("ControladorVisitas?accion=listar");
    }
*/
}
