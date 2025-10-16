<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Incidente"%>
<%@page import="Modelo.Usuarios"%>
<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    if (usuarioSesion == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    List<Incidente> lista = (List<Incidente>) request.getAttribute("listaIncidentes");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mis Reportes de Incidentes</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <style>
            body {
                background-color: #f0f2f5;
                font-family: 'Segoe UI', Tahoma, sans-serif;
                margin: 0;
                padding: 0;
            }

            .container-reportes {
                width: 95%;
                max-width: 1300px;
                margin: 60px auto;
                background: #fff;
                border-radius: 20px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.15);
                overflow: hidden;
            }

            .header-reportes {
                background: linear-gradient(45deg, #dc3545, #bb2d3b);
                color: #fff;
                padding: 25px 40px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                flex-wrap: wrap;
            }

            .header-reportes h4 {
                font-weight: 700;
                margin: 0;
                font-size: 1.6rem;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .btn-nuevo {
                background: #fff;
                color: #bb2d3b;
                font-weight: 600;
                border-radius: 8px;
                padding: 10px 18px;
                text-decoration: none;
                transition: all 0.3s ease;
                border: 2px solid transparent;
            }

            .btn-nuevo:hover {
                background: #f8d7da;
                color: #842029;
                border-color: #dc3545;
            }

            .table-container {
                padding: 40px 50px;
            }

            table {
                border-radius: 12px;
                overflow: hidden;
                width: 100%;
            }

            thead {
                background: linear-gradient(45deg, #dc3545, #bb2d3b);
                color: white;
            }

            tbody tr {
                transition: all 0.25s ease;
            }

            tbody tr:hover {
                background-color: #f9ecec;
                transform: scale(1.01);
            }

            th {
                font-weight: 600;
                text-transform: uppercase;
                font-size: 0.95rem;
            }

            td {
                font-size: 0.95rem;
            }

            .alert-info {
                background-color: #f8d7da;
                color: #842029;
                border: none;
                font-weight: 500;
                border-radius: 10px;
                padding: 25px;
            }

            .btn-regresar {
                background: #6c757d;
                color: white;
                font-weight: 600;
                padding: 12px 30px;
                border-radius: 10px;
                text-decoration: none;
                transition: all 0.3s ease;
            }

            .btn-regresar:hover {
                background: #5c636a;
            }

            .footer-actions {
                text-align: center;
                margin-bottom: 40px;
            }

            /* 💻 Responsividad */
            @media (max-width: 992px) {
                .table-container {
                    padding: 25px;
                }
            }

            @media (max-width: 768px) {
                .header-reportes {
                    flex-direction: column;
                    text-align: center;
                    gap: 15px;
                }

                th, td {
                    font-size: 0.85rem;
                }
            }
        </style>
    </head>

    <body>
        <div class="container-reportes">
            <div class="header-reportes">
                <h4><i class="bi bi-exclamation-triangle"></i> Mis Reportes de Incidentes</h4>
                <a href="<%=request.getContextPath()%>/ControladorIncidente?accion=nuevo" class="btn-nuevo">
                    <i class="bi bi-plus-circle"></i> Nuevo Reporte
                </a>
            </div>

            <div class="table-container">
                <% if (lista == null || lista.isEmpty()) { %>
                <div class="alert alert-info text-center">
                    No se han registrado incidentes aún.
                </div>
                <% } else { %>
                <table class="table table-bordered table-hover align-middle text-center">
                    <thead>
                        <tr>
                            <th style="width:5%;">#</th>
                            <th style="width:25%;">Tipo</th>
                            <th style="width:25%;">Fecha y Hora</th>
                            <th style="width:45%;">Descripción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% int i = 1;
                        for (Incidente inc : lista) {%>
                        <tr>
                            <td><%= i++%></td>
                            <td><%= inc.getNombreTipoIncidente()%></td>
                            <td><%= inc.getFechaHoraIncidente()%></td>
                            <td class="text-start"><%= inc.getDescripcion()%></td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <% }%>

                <div class="footer-actions">
                    <a href="<%=request.getContextPath()%>/vistas/Comunicacion/MenuComunicacion.jsp" class="btn-regresar">
                        <i class="bi bi-arrow-left"></i> Regresar al Menú
                    </a>
                </div>
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const urlParams = new URLSearchParams(window.location.search);
                if (urlParams.get("success") === "true") {
                    Swal.fire({
                        icon: 'success',
                        title: '¡Incidente creado!',
                        text: 'Se ha creado el incidente con éxito.',
                        confirmButtonColor: '#dc3545',
                        confirmButtonText: 'Aceptar'
                    });
                }
            });
        </script>

    </body>
</html>
