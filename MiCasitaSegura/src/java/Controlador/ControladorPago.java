package Controlador;

import Modelo.Pagos;
import Modelo.TiposPago;
import ModeloDAO.PagoDAO;
import ModeloDAO.TiposPagoDAO;
import Modelo.Usuarios;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/ControladorPago")
public class ControladorPago extends HttpServlet {

    String listar = "vistas/Pagos/Index.jsp";
    String addEdit = "vistas/Pagos/addEdit.jsp";

    PagoDAO dao = new PagoDAO();
    TiposPagoDAO tiposPagoDAO = new TiposPagoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acceso = "";
        String action = request.getParameter("accion");

        if ("listar".equalsIgnoreCase(action)) {
            List<Pagos> lista = dao.listar();
            request.setAttribute("pagos", lista);
            acceso = listar;

        } else if ("add".equalsIgnoreCase(action)) {
            request.setAttribute("pago", null);
            request.setAttribute("catalogoTiposPago", tiposPagoDAO.listar()); // catálogo
            acceso = addEdit;

        } else if ("edit".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Pagos pago = dao.listarId(id);
            request.setAttribute("pago", pago);
            request.setAttribute("catalogoTiposPago", tiposPagoDAO.listar()); // catálogo
            acceso = addEdit;

        } else if ("cancelar".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.cancelar(id);
            request.setAttribute("pagos", dao.listar());
            acceso = listar;

        } else {
            acceso = listar;
        }

        RequestDispatcher vista = request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("accion");
        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if ("add".equalsIgnoreCase(action)) {
            int idUsuario = usuarioSesion.getIdUsuario();
            int idTipoPago = Integer.parseInt(request.getParameter("idTipoPago"));
            double monto = Double.parseDouble(request.getParameter("monto"));
            double mora = Double.parseDouble(request.getParameter("mora"));
            double total = Double.parseDouble(request.getParameter("total"));
            String observaciones = request.getParameter("observaciones");

            Pagos p = new Pagos();
            p.setIdUsuario(idUsuario);
            p.setIdTipoPago(idTipoPago);
            p.setFechaPago(new Date(System.currentTimeMillis())); // fecha automática
            p.setMonto(monto);
            p.setMora(mora);
            p.setTotal(total);
            p.setObservaciones(observaciones);
            p.setEstado("Realizado");

            dao.add(p);

        } else if ("edit".equalsIgnoreCase(action)) {
            int idPago = Integer.parseInt(request.getParameter("idPago"));
            int idTipoPago = Integer.parseInt(request.getParameter("idTipoPago"));
            double monto = Double.parseDouble(request.getParameter("monto"));
            double mora = Double.parseDouble(request.getParameter("mora"));
            double total = Double.parseDouble(request.getParameter("total"));
            String observaciones = request.getParameter("observaciones");
            String estado = request.getParameter("estado");

            Pagos p = new Pagos();
            p.setIdPago(idPago);
            p.setIdUsuario(usuarioSesion.getIdUsuario());
            p.setIdTipoPago(idTipoPago);
            p.setFechaPago(new Date(System.currentTimeMillis())); // mantiene la fecha actualizada
            p.setMonto(monto);
            p.setMora(mora);
            p.setTotal(total);
            p.setObservaciones(observaciones);
            p.setEstado(estado);

            dao.edit(p);
        }

        response.sendRedirect("ControladorPago?accion=listar");
    }
}
