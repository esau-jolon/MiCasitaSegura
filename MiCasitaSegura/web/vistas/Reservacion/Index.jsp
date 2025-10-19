<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Reservas" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Reservas</title>

        <!-- Bootstrap y estilos -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>
        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

        <style>
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #0f0f23 0%, #1a1a3e 100%);
                color: #fff;
                min-height: 100vh;
            }
            .page-title {
                font-size: 2.5rem;
                text-align: center;
                font-weight: 700;
                background: linear-gradient(135deg, #667eea, #f093fb);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                margin-top: 2rem;
                margin-bottom: 1rem;
            }
            .main-card {
                background: rgba(255, 255, 255, 0.05);
                border-radius: 20px;
                max-width: 95%;
                margin: 0 auto 3rem auto;
                box-shadow: 0 20px 25px -5px rgba(0,0,0,0.3);
                overflow: hidden;
            }
            .card-header {
                background: linear-gradient(135deg, #667eea, #764ba2);
                padding: 1.5rem 2rem;
                font-weight: 600;
                display: flex;
                justify-content: space-between;
                align-items: center;
                color: white;
            }
            .btn-add {
                background: linear-gradient(135deg, #4ade80, #22c55e);
                border: none;
                border-radius: 12px;
                padding: 0.8rem 1.6rem;
                color: white;
                font-weight: 600;
                text-decoration: none;
                box-shadow: 0 8px 20px rgba(74,222,128,0.3);
            }
            .modern-table {
                width: 100%;
                color: #fff;
                border-collapse: separate;
                border-spacing: 0;
            }
            .modern-table thead {
                background: rgba(255,255,255,0.05);
            }
            .modern-table th, .modern-table td {
                text-align: center;
                padding: 1rem;
            }
            .modern-table tbody tr:hover {
                background: rgba(255,255,255,0.08);
            }
            .status-pendiente { background: linear-gradient(135deg, #fbbf24, #f59e0b); color: white; border-radius: 20px; padding: 5px 12px;}
            .status-confirmada { background: linear-gradient(135deg, #4ade80, #22c55e); color: white; border-radius: 20px; padding: 5px 12px;}
            .status-cancelada { background: linear-gradient(135deg, #f87171, #ef4444); color: white; border-radius: 20px; padding: 5px 12px;}
        </style>
    </head>
    <body>

        <h1 class="page-title">Gestión de Reservas</h1>

        <div class="main-card">
            <div class="card-header">
                <span><i class="bi bi-calendar-week"></i> Lista de Reservas</span>
                <a href="${pageContext.request.contextPath}/ControladorReserva?accion=nuevo" class="btn-add">
                    <i class="bi bi-plus-circle"></i> Nueva Reserva
                </a>
            </div>

            <div class="table-responsive">
                <table class="modern-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Área</th>
                            <th>Fecha</th>
                            <th>Hora Inicio</th>
                            <th>Hora Fin</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Reservas> lista = (List<Reservas>) request.getAttribute("listaReservas");
                            if (lista != null && !lista.isEmpty()) {
                                for (Reservas r : lista) {
                        %>
                        <tr>
                            <td>#<%= r.getIdReserva()%></td>
                            <td><%= r.getNombreArea()%></td>
                            <td><%= r.getFechaReserva()%></td>
                            <td><%= r.getHoraInicio()%></td>
                            <td><%= r.getHoraFin()%></td>
                            <td>
                                <%
                                    String claseEstado = "";
                                    switch (r.getNombreEstado().toLowerCase()) {
                                        case "pendiente":
                                            claseEstado = "status-pendiente";
                                            break;
                                        case "confirmada":
                                            claseEstado = "status-confirmada";
                                            break;
                                        case "cancelada":
                                            claseEstado = "status-cancelada";
                                            break;
                                    }
                                %>
                                <span class="<%= claseEstado%>"><%= r.getNombreEstado()%></span>
                            </td>
                            <td>
                                <% if (r.getNombreEstado().equalsIgnoreCase("Pendiente")) {%>
                                <a href="ControladorReserva?accion=cancelar&id=<%= r.getIdReserva()%>"
                                   class="btn btn-danger btn-sm">
                                    <i class="bi bi-x-circle"></i> Cancelar
                                </a>
                                <% } else { %>
                                <span class="text-muted">N/A</span>
                                <% } %>
                            </td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="7" class="text-center text-secondary py-4">
                                <i class="bi bi-calendar-x" style="font-size:2rem;"></i><br>
                                No hay reservas registradas.
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>

    </body>

    <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>
    <script>
        <% if (request.getParameter("success") != null) { %>
    Swal.fire({
        icon: 'success',
        title: 'Reserva creada con éxito',
        text: 'Su solicitud ha sido registrada correctamente.'
    });
        <% } else if (request.getParameter("cancelada") != null) { %>
    Swal.fire({
        icon: 'warning',
        title: 'Reserva cancelada',
        text: 'La reserva ha sido cancelada correctamente.'
    });
        <% } else if (request.getParameter("confirmada") != null) { %>
    Swal.fire({
        icon: 'info',
        title: 'Reserva confirmada',
        text: 'La reserva fue confirmada y se notificó al residente.'
    });
        <% }%>
    </script>

</html>
