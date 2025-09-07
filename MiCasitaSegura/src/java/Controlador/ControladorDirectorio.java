/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author esauj
 */
@WebServlet(name = "ControladorDirectorio", urlPatterns = {"/ControladorDirectorio"})
public class ControladorDirectorio extends HttpServlet {

    UsuarioDAO dao = new UsuarioDAO();

    CasaDAO casaDAO = new CasaDAO();
    LoteDAO loteDAO = new LoteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("listar".equals(accion)) {
            List<Usuarios> lista = dao.listar();
            request.setAttribute("usuarios", lista);

            // Agregamos los catálogos
            request.setAttribute("casas", casaDAO.listar());
            request.setAttribute("lotes", loteDAO.listar());

            request.getRequestDispatcher("vistas/Directorio/Index.jsp").forward(request, response);

        } else if ("buscar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String lote = request.getParameter("lote");
            String casa = request.getParameter("numeroCasa");

            Integer loteId = (lote != null && !lote.isEmpty()) ? Integer.parseInt(lote) : null;
            Integer casaId = (casa != null && !casa.isEmpty()) ? Integer.parseInt(casa) : null;

            List<Usuarios> lista = dao.buscar(nombre, apellidos, loteId, casaId);

            if (lista.isEmpty()) {
                request.setAttribute("mensaje", "No se encontró ningún usuario con los datos ingresados.");
            }

            request.setAttribute("usuarios", lista);

            // También enviamos los catálogos al JSP
            request.setAttribute("casas", casaDAO.listar());
            request.setAttribute("lotes", loteDAO.listar());

            request.getRequestDispatcher("vistas/Directorio/Index.jsp").forward(request, response);
        }
    }

}
