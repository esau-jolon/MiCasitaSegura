package Controlador;

import Modelo.Paqueteria;
import Modelo.Usuarios;
import ModeloDAO.PaqueteriaDAO;
import ModeloDAO.UsuarioDAO;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

// 📧 Envío de correos
import Controlador.EmailSender;

@WebServlet("/ControladorPaqueteria")
public class ControladorPaqueteria extends HttpServlet {

    // ✅ Vistas
    private static final String INDEX = "vistas/Paqueteria/Index.jsp";
    private static final String ADD_EDIT = "vistas/Paqueteria/addEdit.jsp";

    // ✅ DAOs
    private final PaqueteriaDAO paqueteriaDAO = new PaqueteriaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static final SimpleDateFormat SDF_FECHA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        String acceso;

        if (accion == null) {
            accion = "listar";
        }

        switch (accion.toLowerCase()) {

            // 🔹 LISTAR
            case "listar": {
                List<Paqueteria> listaPaquetes = paqueteriaDAO.listarPaquetes();
                request.setAttribute("paquetes", listaPaquetes);

                if (listaPaquetes.isEmpty()) {
                    request.setAttribute("mensaje", "No hay paquetería pendiente de entregar.");
                }
                acceso = INDEX;
                break;
            }

            // 🔹 BUSCAR (RN03)
            case "buscar": {
                String numeroGuia = request.getParameter("numeroGuia");
                String nombreResidente = request.getParameter("nombreResidente");
                String estado = request.getParameter("estado");

                List<Paqueteria> listaFiltrada = paqueteriaDAO.buscarPaquetes(numeroGuia, nombreResidente, estado);
                request.setAttribute("paquetes", listaFiltrada);
                request.setAttribute("numeroGuia", numeroGuia);
                request.setAttribute("nombreResidente", nombreResidente);
                request.setAttribute("estado", estado);

                if (listaFiltrada == null || listaFiltrada.isEmpty()) {
                    request.setAttribute("mensaje", "No se encontraron resultados con los criterios de búsqueda.");
                }
                acceso = INDEX;
                break;
            }

            // 🔹 NUEVO (form)
            case "add": {
                request.setAttribute("paquete", null);
                request.setAttribute("catalogoResidentes", usuarioDAO.listarResidentesPaqueteria());
                acceso = ADD_EDIT;
                break;
            }

            // 🔹 ENTREGAR
            case "entregar": {
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean exito = paqueteriaDAO.marcarEntregado(id, usuarioSesion.getIdUsuario());

                    if (exito) {
                        Paqueteria p = paqueteriaDAO.obtenerPorId(id);
                        Usuarios residente = usuarioDAO.obtenerPorId(p.getIdResidente());

                        if (residente != null && residente.getCorreo() != null) {
                            final String correoDest = residente.getCorreo();
                            final String nombreResidente = residente.getNombre() + " " + residente.getApellidos();
                            final String numeroGuia = p.getNumeroGuia();
                            final String fechaEntregaStr = (p.getFechaEntrega() != null)
                                    ? SDF_FECHA.format(p.getFechaEntrega())
                                    : SDF_FECHA.format(new Timestamp(System.currentTimeMillis()));
                            final String agenteNombre = usuarioSesion.getNombre() + " " + usuarioSesion.getApellidos();

                            // 📧 Correo enriquecido (HTML)
                            final String asunto = "Entrega de Paquetería";
                            final String cuerpoHtml
                                    = "<div style='font-family:Segoe UI, Arial, sans-serif; font-size:14px; color:#111;'>"
                                    + "<h2 style='color:#16a34a; margin-bottom:8px;'>✅ Paquetería entregada</h2>"
                                    + "<p>Estimado(a) <b>" + nombreResidente + "</b>,</p>"
                                    + "<p>Se confirma la <b>entrega</b> de su paquete.</p>"
                                    + "<table style='border-collapse:collapse; width:100%; margin:12px 0;'>"
                                    + "<tr><td style='background:#f8fafc; border:1px solid #e5e7eb; padding:8px;'>Número de guía</td>"
                                    + "<td style='border:1px solid #e5e7eb; padding:8px;'>" + (numeroGuia != null ? numeroGuia : "N/D") + "</td></tr>"
                                    + "<tr><td style='background:#f8fafc; border:1px solid #e5e7eb; padding:8px;'>Fecha y hora de entrega</td>"
                                    + "<td style='border:1px solid #e5e7eb; padding:8px;'>" + fechaEntregaStr + "</td></tr>"
                                    + "<tr><td style='background:#f8fafc; border:1px solid #e5e7eb; padding:8px;'>Entregado por</td>"
                                    + "<td style='border:1px solid #e5e7eb; padding:8px;'>" + agenteNombre + "</td></tr>"
                                    + "</table>"
                                    + "<p style='margin-top:16px;'>Gracias por utilizar <b>Mi Casita Segura</b>.</p>"
                                    + "<hr style='border:none; border-top:1px solid #e5e7eb; margin:16px 0;'>"
                                    + "<small style='color:#64748b;'>Este es un mensaje automático, por favor no responder.</small>"
                                    + "</div>";

                            new Thread(() -> {
                                try {
                                    EmailSender.enviarConAdjunto(correoDest, asunto, cuerpoHtml, null);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }).start();
                        }

                        response.sendRedirect("ControladorPaqueteria?accion=listar&entregado=true");
                    } else {
                        response.sendRedirect("ControladorPaqueteria?accion=listar&error=entrega_fallida");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("ControladorPaqueteria?accion=listar&error=exception");
                }
                return;
            }

            // 🔹 ELIMINAR (lógico)
            case "delete": {
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    paqueteriaDAO.eliminar(id, usuarioSesion.getIdUsuario());
                    response.sendRedirect("ControladorPaqueteria?accion=listar&deleted=true");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("ControladorPaqueteria?accion=listar&error=delete_failed");
                    return;
                }
            }

            default:
                acceso = INDEX;
        }

        RequestDispatcher vista = request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    // 🔹 POST → Guardar nuevo registro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        try {
            if ("add".equalsIgnoreCase(accion)) {
                String numeroGuia = request.getParameter("numeroGuia");
                int idResidente = Integer.parseInt(request.getParameter("idResidente"));

                if (numeroGuia == null || numeroGuia.trim().isEmpty() || idResidente <= 0) {
                    request.setAttribute("errorMensaje", "Debe completar todos los campos obligatorios.");
                    request.setAttribute("catalogoResidentes", usuarioDAO.listarResidentesPaqueteria());
                    RequestDispatcher vista = request.getRequestDispatcher(ADD_EDIT);
                    vista.forward(request, response);
                    return;
                }

                Paqueteria p = new Paqueteria();
                p.setNumeroGuia(numeroGuia.trim());
                p.setIdResidente(idResidente);
                p.setIdAgenteRegistro(usuarioSesion.getIdUsuario());
                p.setCreadoPor(usuarioSesion.getIdUsuario());

                boolean exito = paqueteriaDAO.registrarPaquete(p, usuarioSesion.getIdUsuario());

                if (exito) {
                    // 📧 Correo al residente (asíncrono)
                    Usuarios residente = usuarioDAO.obtenerPorId(idResidente);
                    if (residente != null && residente.getCorreo() != null) {
                        final String correoDest = residente.getCorreo();
                        final String nombreResidente = residente.getNombre() + " " + residente.getApellidos();
                        final String numeroGuiaFinal = numeroGuia.trim();
                        final String fechaRecepStr = SDF_FECHA.format(new Timestamp(System.currentTimeMillis()));

                        final String asunto = "Paquetería recibida en garita";
                        final String cuerpoHtml
                                = "<div style='font-family:Segoe UI, Arial, sans-serif; font-size:14px; color:#111;'>"
                                + "<h2 style='color:#0284c7; margin-bottom:8px;'>📦 Paquete recibido</h2>"
                                + "<p>Estimado(a) <b>" + nombreResidente + "</b>,</p>"
                                + "<p>Se ha registrado la recepción de un paquete a su nombre.</p>"
                                + "<table style='border-collapse:collapse; width:100%; margin:12px 0;'>"
                                + "<tr><td style='background:#f8fafc; border:1px solid #e5e7eb; padding:8px;'>Número de guía</td>"
                                + "<td style='border:1px solid #e5e7eb; padding:8px;'>" + numeroGuiaFinal + "</td></tr>"
                                + "<tr><td style='background:#f8fafc; border:1px solid #e5e7eb; padding:8px;'>Fecha de recepción</td>"
                                + "<td style='border:1px solid #e5e7eb; padding:8px;'>" + fechaRecepStr + "</td></tr>"
                                + "</table>"
                                + "<p style='margin-top:16px;'>Podrá reclamarlo en la garita de seguridad.<br>"
                                + "Gracias por utilizar <b>Mi Casita Segura</b>.</p>"
                                + "<hr style='border:none; border-top:1px solid #e5e7eb; margin:16px 0;'>"
                                + "<small style='color:#64748b;'>Este es un mensaje automático, por favor no responder.</small>"
                                + "</div>";

                        new Thread(() -> {
                            try {
                                EmailSender.enviarConAdjunto(correoDest, asunto, cuerpoHtml, null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    }

                    response.sendRedirect("ControladorPaqueteria?accion=listar&success=true");
                } else {
                    request.setAttribute("errorMensaje", "No se pudo registrar la paquetería.");
                    request.setAttribute("catalogoResidentes", usuarioDAO.listarResidentesPaqueteria());
                    RequestDispatcher vista = request.getRequestDispatcher(ADD_EDIT);
                    vista.forward(request, response);
                }

            } else {
                response.sendRedirect("ControladorPaqueteria?accion=listar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMensaje", "Ocurrió un error al registrar la paquetería: " + e.getMessage());
            request.setAttribute("catalogoResidentes", usuarioDAO.listarResidentesPaqueteria());
            RequestDispatcher vista = request.getRequestDispatcher(ADD_EDIT);
            vista.forward(request, response);
        }
    }
}
