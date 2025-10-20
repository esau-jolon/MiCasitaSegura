package Controlador;

import Modelo.Pagos;
import Modelo.TiposPago;
import Modelo.EstadosPago;
import ModeloDAO.PagoDAO;
import ModeloDAO.TiposPagoDAO;
import ModeloDAO.EstadosPagoDAO;
import Modelo.Usuarios;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.YearMonth;
import java.util.List;

@WebServlet("/ControladorPago")
public class ControladorPago extends HttpServlet {

    String listar = "vistas/Pagos/Index.jsp";
    String addEdit = "vistas/Pagos/addEdit.jsp";

    PagoDAO dao = new PagoDAO();
    TiposPagoDAO tiposPagoDAO = new TiposPagoDAO();
    EstadosPagoDAO estadosPagoDAO = new EstadosPagoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acceso = "";
        String action = request.getParameter("accion");

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if ("listar".equalsIgnoreCase(action)) {
            List<Pagos> lista = dao.listar();
            request.setAttribute("pagos", lista);
            acceso = listar;

        } else if ("add".equalsIgnoreCase(action)) {
            request.setAttribute("pago", null);
            request.setAttribute("catalogoTiposPago", tiposPagoDAO.listar());
            request.setAttribute("catalogoEstadosPago", estadosPagoDAO.listar());

            if (usuarioSesion != null) {
                String mesSugerido = dao.obtenerMesSiguiente(usuarioSesion.getIdUsuario());
                request.setAttribute("mesSugerido", mesSugerido);
            }

            acceso = addEdit;

        } else if ("edit".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Pagos pago = dao.listarId(id);
            request.setAttribute("pago", pago);
            request.setAttribute("catalogoTiposPago", tiposPagoDAO.listar());
            request.setAttribute("catalogoEstadosPago", estadosPagoDAO.listar());

            if (usuarioSesion != null) {
                String mesSugerido = dao.obtenerMesSiguiente(usuarioSesion.getIdUsuario());
                request.setAttribute("mesSugerido", mesSugerido);
            }

            acceso = addEdit;

        } else if ("cancelar".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.cancelar(id);
            request.setAttribute("pagos", dao.listar());
            acceso = listar;

        } else if ("eliminar".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean eliminado = dao.eliminarLogico(id);

            if (eliminado) {
                request.setAttribute("mensaje", "El pago fue eliminado correctamente.");
            } else {
                request.setAttribute("mensajeError", "No se pudo eliminar el pago.");
            }

            request.setAttribute("pagos", dao.listar());
            acceso = listar;

            // ✅ Nuevo: Confirmar pago (estado = 2)
        } else if ("confirmar".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean actualizado = dao.cambiarEstado(id, 2); // Estado Realizado
            if (actualizado) {
                request.setAttribute("mensaje", "Pago confirmado exitosamente.");
            } else {
                request.setAttribute("mensajeError", "No se pudo confirmar el pago.");
            }
            request.setAttribute("pagos", dao.listar());
            acceso = listar;

            // ❌ Nuevo: Cancelar pago (estado = 3)
        } else if ("cancelarPago".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean actualizado = dao.cambiarEstado(id, 3); // Estado Cancelado
            if (actualizado) {
                request.setAttribute("mensaje", "Pago cancelado correctamente.");
            } else {
                request.setAttribute("mensajeError", "No se pudo cancelar el pago.");
            }
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

        try {
            if ("add".equalsIgnoreCase(action)) {

                int idUsuario = usuarioSesion.getIdUsuario();
                int idTipoPago = Integer.parseInt(request.getParameter("idTipoPago"));
                int idEstadoPago = Integer.parseInt(request.getParameter("idEstadoPago"));
                double monto = Double.parseDouble(request.getParameter("monto"));
                double mora = Double.parseDouble(request.getParameter("mora"));
                double total = Double.parseDouble(request.getParameter("total"));
                String observaciones = request.getParameter("observaciones");

                // === Datos de tarjeta ===
                String numTarjeta = request.getParameter("numTarjeta");
                String fechaVenc = request.getParameter("fechaVenc");
                String cvv = request.getParameter("cvv");
                String nombreTitular = request.getParameter("nombreTitular");

                // ======= VALIDACIONES =======
                if (idTipoPago <= 0) {
                    throw new IllegalArgumentException("Debe seleccionar un tipo de pago válido.");
                }

                if (idEstadoPago <= 0) {
                    throw new IllegalArgumentException("Debe seleccionar un estado de pago válido.");
                }

                if (monto <= 0 || total <= 0) {
                    throw new IllegalArgumentException("El monto y el total deben ser mayores que cero.");
                }

                if (observaciones == null || observaciones.trim().isEmpty()) {
                    throw new IllegalArgumentException("Debe ingresar observaciones del pago.");
                }

                if (numTarjeta == null || !numTarjeta.matches("\\d{12,19}")) {
                    throw new IllegalArgumentException("Número de tarjeta inválido. Solo se permiten entre 12 y 19 dígitos.");
                }

                if (cvv == null || !cvv.matches("\\d{3,4}")) {
                    throw new IllegalArgumentException("CVV inválido. Debe tener 3 o 4 dígitos numéricos.");
                }

                if (nombreTitular == null || nombreTitular.trim().isEmpty()) {
                    throw new IllegalArgumentException("Debe ingresar el nombre del titular de la tarjeta.");
                }

                if (fechaVenc == null || fechaVenc.trim().isEmpty()) {
                    throw new IllegalArgumentException("Debe indicar la fecha de vencimiento de la tarjeta.");
                }

                try {
                    String[] partes = fechaVenc.split("/");
                    if (partes.length != 2) {
                        throw new IllegalArgumentException("Formato inválido. Use MM/YYYY.");
                    }

                    int mes = Integer.parseInt(partes[0]);
                    int anio = Integer.parseInt(partes[1]);

                    if (mes < 1 || mes > 12) {
                        throw new IllegalArgumentException("El mes de vencimiento debe estar entre 01 y 12.");
                    }

                    YearMonth fechaSeleccionada = YearMonth.of(anio, mes);
                    YearMonth fechaActual = YearMonth.now();

                    if (fechaSeleccionada.isBefore(fechaActual)) {
                        throw new IllegalArgumentException("La tarjeta está vencida. Use una con fecha válida.");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Formato de fecha de vencimiento inválido. Use MM/YYYY.");
                }

                // ======= CREAR PAGO =======
                Pagos p = new Pagos();
                p.setIdUsuario(idUsuario);
                p.setIdTipoPago(idTipoPago);
                p.setIdEstadoPago(idEstadoPago);
                p.setFechaPago(new Date(System.currentTimeMillis()));
                p.setMonto(monto);
                p.setMora(mora);
                p.setTotal(monto + mora);
                p.setObservaciones(observaciones);
                p.setActivo(true);

                // Guardar mes/año si aplica
                if (idTipoPago == 1) {
                    String mesPagadoStr = request.getParameter("mesPagado");
                    String anioPagadoStr = request.getParameter("anioPagado");
                    if (mesPagadoStr != null && anioPagadoStr != null) {
                        p.setMesPagado(Integer.parseInt(mesPagadoStr));
                        p.setAnioPagado(Integer.parseInt(anioPagadoStr));
                    }
                }

                dao.add(p);

            } else if ("edit".equalsIgnoreCase(action)) {
                int idPago = Integer.parseInt(request.getParameter("idPago"));
                int idTipoPago = Integer.parseInt(request.getParameter("idTipoPago"));
                int idEstadoPago = Integer.parseInt(request.getParameter("idEstadoPago"));
                double monto = Double.parseDouble(request.getParameter("monto"));
                double mora = Double.parseDouble(request.getParameter("mora"));
                String observaciones = request.getParameter("observaciones");

                if (idTipoPago <= 0) {
                    throw new IllegalArgumentException("Debe seleccionar un tipo de pago válido.");
                }

                if (idEstadoPago <= 0) {
                    throw new IllegalArgumentException("Debe seleccionar un estado de pago válido.");
                }

                if (monto <= 0) {
                    throw new IllegalArgumentException("El monto debe ser mayor que cero.");
                }

                Pagos p = new Pagos();
                p.setIdPago(idPago);
                p.setIdUsuario(usuarioSesion.getIdUsuario());
                p.setIdTipoPago(idTipoPago);
                p.setIdEstadoPago(idEstadoPago);
                p.setFechaPago(new Date(System.currentTimeMillis()));
                p.setMonto(monto);
                p.setMora(mora);
                p.setTotal(monto + mora);
                p.setObservaciones(observaciones);
                p.setActivo(true);

                if (idTipoPago == 1) {
                    String mesPagadoStr = request.getParameter("mesPagado");
                    String anioPagadoStr = request.getParameter("anioPagado");
                    if (mesPagadoStr != null && anioPagadoStr != null) {
                        p.setMesPagado(Integer.parseInt(mesPagadoStr));
                        p.setAnioPagado(Integer.parseInt(anioPagadoStr));
                    }
                } else {
                    p.setMesPagado(null);
                    p.setAnioPagado(null);
                }

                dao.edit(p);
            }

            response.sendRedirect("ControladorPago?accion=listar");

        } catch (IllegalArgumentException ex) {
            request.setAttribute("mensajeError", ex.getMessage());
            request.setAttribute("catalogoTiposPago", tiposPagoDAO.listar());
            request.setAttribute("catalogoEstadosPago", estadosPagoDAO.listar());
            RequestDispatcher vista = request.getRequestDispatcher(addEdit);
            vista.forward(request, response);

        } catch (Exception ex) {
            request.setAttribute("mensajeError", "Ocurrió un error inesperado: " + ex.getMessage());
            request.setAttribute("catalogoTiposPago", tiposPagoDAO.listar());
            request.setAttribute("catalogoEstadosPago", estadosPagoDAO.listar());
            RequestDispatcher vista = request.getRequestDispatcher(addEdit);
            vista.forward(request, response);
        }
    }
}
