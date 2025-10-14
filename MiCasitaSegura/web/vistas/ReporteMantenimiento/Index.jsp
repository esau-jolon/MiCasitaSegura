<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.ReporteMantenimiento"%>
<%@page import="Modelo.TipoInconveniente"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reportes de Mantenimiento</title>

        <!-- Bootstrap y estilos -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

        <style>
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #1a1a2e, #16213e);
                min-height: 100vh;
                color: #fff;
                overflow-x: hidden;
            }

            .container {
                max-width: 1300px;
                margin: 0 auto;
                padding: 2rem;
            }

            .header {
                text-align: center;
                margin-bottom: 3rem;
            }

            .header h1 {
                font-size: 2.8rem;
                font-weight: 700;
                background: linear-gradient(135deg, #ff416c, #ff4b2b);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }

            .header p {
                color: #aaa;
                font-size: 1.1rem;
            }

            .card {
                background: rgba(255,255,255,0.05);
                border-radius: 20px;
                overflow: hidden;
                box-shadow: 0 10px 25px rgba(0,0,0,0.25);
            }

            .card-header {
                background: linear-gradient(45deg, #dc3545, #bb2d3b);
                padding: 1.5rem;
                text-align: center;
                font-size: 1.4rem;
                font-weight: 600;
                color: white;
            }

            .btn-add {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                gap: .6rem;
                background: linear-gradient(135deg, #22c55e, #16a34a);
                color: white;
                border: none;
                padding: 1rem 1.8rem;
                border-radius: 16px;
                font-weight: 600;
                font-size: 1rem;
                text-decoration: none;
                transition: all 0.3s ease;
                box-shadow: 0 8px 20px rgba(34,197,94,0.3);
                margin: 2rem auto;
            }

            .btn-add:hover {
                transform: translateY(-3px);
                box-shadow: 0 12px 25px rgba(34,197,94,0.4);
                color: white;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 2rem;
            }

            th, td {
                padding: 1rem;
                text-align: center;
            }

            thead {
                background: rgba(255,255,255,0.08);
            }

            th {
                text-transform: uppercase;
                font-weight: 600;
                color: #ffbaba;
            }

            tr {
                background: rgba(255,255,255,0.03);
                transition: all 0.2s ease;
            }

            tr:hover {
                background: rgba(255,255,255,0.1);
            }

            .status-enviado {
                background: linear-gradient(135deg, #22c55e, #16a34a);
                color: white;
                padding: .4rem 1rem;
                border-radius: 25px;
                font-weight: 600;
            }

            .status-pendiente {
                background: linear-gradient(135deg, #fbbf24, #f59e0b);
                color: #1a1a1a;
                padding: .4rem 1rem;
                border-radius: 25px;
                font-weight: 600;
            }

            .empty-state {
                text-align: center;
                padding: 4rem 2rem;
                color: #bbb;
            }

            .empty-icon {
                font-size: 3.5rem;
                margin-bottom: 1rem;
                opacity: 0.6;
            }

            .text-muted { color: #aaa !important; }
        </style>
    </head>
    <body>

        <div class="container">
            <div class="header">
                <h1><i class="bi bi-tools"></i> Reportes de Mantenimiento</h1>
                <p>Visualiza los reportes que has enviado y crea nuevos si detectas problemas en el sistema.</p>
            </div>

            <div class="card">
                <div class="card-header">
                    <i class="bi bi-gear-fill"></i> Mis Reportes Enviados
                </div>

                <div class="text-center">
                    <a href="${pageContext.request.contextPath}/ControladorReporteMantenimiento?accion=nuevo" class="btn-add">
                        <i class="bi bi-plus-circle"></i> Nuevo Reporte
                    </a>
                </div>

                <div class="table-responsive px-4 pb-4">
                    <table class="table table-borderless align-middle text-light">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Tipo de Inconveniente</th>
                                <th>Descripción</th>
                                <th>Fecha y Hora</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<ReporteMantenimiento> lista = (List<ReporteMantenimiento>) request.getAttribute("listaReportes");
                                if (lista != null && !lista.isEmpty()) {
                                    int i = 1;
                                    for (ReporteMantenimiento r : lista) {
                            %>
                            <tr>
                                <td><%= i++%></td>
                                <td><%= r.getNombreTipoInconveniente() != null ? r.getNombreTipoInconveniente() : "—"%></td>
                                <td><%= r.getDescripcion() != null ? r.getDescripcion() : "Sin descripción"%></td>
                                <td><%= r.getFechaHoraIncidente() != null ? r.getFechaHoraIncidente() : "—"%></td>
                                <td>
                                    <span class="status-enviado">
                                        <i class="bi bi-check-circle-fill"></i> Enviado
                                    </span>
                                </td>
                            </tr>
                            <%  }
                    } else { %>
                            <tr>
                                <td colspan="5" class="empty-state">
                                    <div class="empty-icon"><i class="bi bi-clipboard-x"></i></div>
                                    No has enviado ningún reporte todavía.
                                </td>
                            </tr>
                            <% }%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

        <%
            String success = request.getParameter("success");
            String error = (String) request.getAttribute("error");
            if ("true".equals(success)) {
        %>
        <script>
            Swal.fire({
                icon: 'success',
                title: 'Reporte enviado',
                text: 'Tu reporte fue enviado correctamente.',
                confirmButtonColor: '#16a34a'
            });
        </script>
        <% } else if (error != null) {%>
        <script>
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: '<%= error%>',
                confirmButtonColor: '#dc3545'
            });
        </script>
        <% }%>

    </body>
</html>
