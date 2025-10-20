<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Paqueteria" %>
<%@ page import="Modelo.Usuarios" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Paquetería</title>

        <!-- Bootstrap y estilos -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>
        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

        <style>
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #0f172a, #1e293b);
                color: #fff;
                min-height: 100vh;
            }
            .page-title {
                font-size: 2.5rem;
                text-align: center;
                font-weight: 700;
                background: linear-gradient(135deg, #38bdf8, #6366f1);
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
                background: linear-gradient(135deg, #38bdf8, #6366f1);
                padding: 1.5rem 2rem;
                font-weight: 600;
                display: flex;
                justify-content: space-between;
                align-items: center;
                color: white;
            }
            .btn-add {
                background: linear-gradient(135deg, #22c55e, #16a34a);
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
            .modern-table th, .modern-table td {
                text-align: center;
                padding: 1rem;
            }
            .modern-table tbody tr:hover {
                background: rgba(255,255,255,0.08);
            }
            .status-pendiente {
                background: linear-gradient(135deg, #fbbf24, #f59e0b);
                color: white;
                border-radius: 20px;
                padding: 5px 12px;
            }
            .status-entregado {
                background: linear-gradient(135deg, #22c55e, #15803d);
                color: white;
                border-radius: 20px;
                padding: 5px 12px;
            }
        </style>
    </head>
    <body>

        <h1 class="page-title">Gestión de Paquetería</h1>

        <div class="main-card">
            <div class="card-header">
                <span><i class="bi bi-box-seam"></i> Lista de Paquetes</span>
                <a href="${pageContext.request.contextPath}/ControladorPaqueteria?accion=add" class="btn-add">
                    <i class="bi bi-plus-circle"></i> Registrar Paquete
                </a>
            </div>

            <div class="table-responsive">
                <table class="modern-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Número Guía</th>
                            <th>Residente</th>
                            <th>Fecha Recepción</th>
                            <th>Fecha Entrega</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Paqueteria> lista = (List<Paqueteria>) request.getAttribute("paquetes");
                            if (lista != null && !lista.isEmpty()) {
                                for (Paqueteria p : lista) {
                        %>
                        <tr>
                            <td>#<%= p.getIdPaquete()%></td>
                            <td><%= p.getNumeroGuia()%></td>
                            <td><%= p.getNombreCompletoResidente()%></td>
                            <td><%= p.getFechaRecepcion() != null ? p.getFechaRecepcion().toString() : "—"%></td>
                            <td><%= p.getFechaEntrega() != null ? p.getFechaEntrega().toString() : "—"%></td>
                            <td>
                                <% if (p.isEntregado()) { %>
                                <span class="status-entregado">Entregado</span>
                                <% } else { %>
                                <span class="status-pendiente">Pendiente</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (!p.isEntregado()) {%>
                                <button type="button" class="btn btn-success btn-sm btn-entregar" data-id="<%= p.getIdPaquete()%>">
                                    <i class="bi bi-check-circle"></i> Entregar
                                </button>
                                <button type="button" class="btn btn-danger btn-sm btn-eliminar" data-id="<%= p.getIdPaquete()%>">
                                    <i class="bi bi-trash"></i> Eliminar
                                </button>
                                <% } else { %>
                                <span class="text-muted">N/A</span>
                                <% } %>
                            </td>
                        </tr>
                        <% }
                    } else { %>
                        <tr>
                            <td colspan="7" class="text-center text-secondary py-4">
                                <i class="bi bi-inbox" style="font-size:2rem;"></i><br>
                                No hay paquetería pendiente de entregar.
                            </td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function () {

                // ✅ Entregar paquete
                const entregarBtns = document.querySelectorAll(".btn-entregar");
                entregarBtns.forEach(btn => {
                    btn.addEventListener("click", function () {
                        const id = this.getAttribute("data-id");
                        Swal.fire({
                            title: "¿Entregar este paquete?",
                            text: "El residente será notificado por correo electrónico.",
                            icon: "question",
                            showCancelButton: true,
                            confirmButtonColor: "#22c55e",
                            cancelButtonColor: "#6b7280",
                            confirmButtonText: "Sí, entregar",
                            cancelButtonText: "Cancelar"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = "ControladorPaqueteria?accion=entregar&id=" + id;
                            }
                        });
                    });
                });

                // ❌ Eliminar paquete
                const eliminarBtns = document.querySelectorAll(".btn-eliminar");
                eliminarBtns.forEach(btn => {
                    btn.addEventListener("click", function () {
                        const id = this.getAttribute("data-id");
                        Swal.fire({
                            title: "¿Eliminar registro?",
                            text: "Este paquete se marcará como inactivo.",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#ef4444",
                            cancelButtonColor: "#6b7280",
                            confirmButtonText: "Sí, eliminar",
                            cancelButtonText: "Cancelar"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = "ControladorPaqueteria?accion=delete&id=" + id;
                            }
                        });
                    });
                });
            });
        </script>

    </body>
</html>
