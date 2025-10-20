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
                background: linear-gradient(90deg, #38bdf8, #4f46e5);
                padding: 1.2rem 2rem;
                font-weight: 600;
                display: flex;
                justify-content: space-between;
                align-items: center;
                color: white;
                border-bottom: 1px solid rgba(255,255,255,0.15);
            }

            .btn-add {
                background: linear-gradient(135deg, #22c55e, #16a34a);
                border: none;
                border-radius: 10px;
                padding: 0.7rem 1.6rem;
                color: white;
                font-weight: 600;
                text-decoration: none;
                box-shadow: 0 4px 14px rgba(74,222,128,0.35);
                transition: transform 0.2s ease;
            }

            .btn-add:hover {
                transform: translateY(-2px);
            }

            .search-form {
                background: rgba(255, 255, 255, 0.08);
                padding: 1rem 1.5rem;
                border-bottom: 1px solid rgba(255, 255, 255, 0.12);
            }

            .search-form .form-control,
            .search-form .form-select {
                border-radius: 12px;
                background: rgba(255,255,255,0.15);
                border: 1px solid rgba(255,255,255,0.25);
                color: #fff;
                transition: all 0.25s ease-in-out;
            }

            .search-form .form-control::placeholder {
                color: rgba(255,255,255,0.7);
            }

            .search-form .form-control:focus,
            .search-form .form-select:focus {
                background: rgba(255,255,255,0.25);
                border-color: #38bdf8;
                box-shadow: 0 0 0 0.2rem rgba(56,189,248,0.25);
                color: #fff;
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

            .btn-primary {
                background: linear-gradient(135deg, #38bdf8, #4f46e5);
                border: none;
                border-radius: 10px;
                color: #fff;
                font-weight: 600;
                box-shadow: 0 4px 10px rgba(59,130,246,0.3);
                transition: transform 0.2s ease;
            }

            .btn-primary:hover {
                transform: translateY(-2px);
                background: linear-gradient(135deg, #60a5fa, #6366f1);
            }

            .btn-clear {
                background: linear-gradient(135deg, #9ca3af, #6b7280);
                border: none;
                border-radius: 10px;
                color: #fff;
                font-weight: 600;
                box-shadow: 0 4px 10px rgba(156,163,175,0.3);
                transition: transform 0.2s ease;
            }

            .btn-clear:hover {
                transform: translateY(-2px);
                background: linear-gradient(135deg, #d1d5db, #9ca3af);
            }
        </style>
    </head>
    <body>

        <%
            List<Paqueteria> lista = (List<Paqueteria>) request.getAttribute("paquetes");
            String mensaje = (String) request.getAttribute("mensaje");

            // ✅ Determina si hay resultados en general
            boolean hayResultados = (lista != null && !lista.isEmpty());

            // ✅ Determina si hay paquetes pendientes
            boolean hayPendientes = false;
            if (lista != null) {
                for (Paqueteria p : lista) {
                    if (!p.isEntregado()) {
                        hayPendientes = true;
                        break;
                    }
                }
            }

            // ✅ Saber si es búsqueda o vista general
            String accion = request.getParameter("accion");
            boolean esBusqueda = (accion != null && accion.equals("buscar"));
        %>

        <h1 class="page-title">Gestión de Paquetería</h1>

        <div class="main-card">
            <div class="card-header">
                <span><i class="bi bi-box-seam"></i> Lista de Paquetes</span>
                <a href="${pageContext.request.contextPath}/ControladorPaqueteria?accion=add" class="btn-add">
                    <i class="bi bi-plus-circle"></i> Registrar Paquete
                </a>
            </div>

            <% if (esBusqueda ? hayResultados : hayPendientes) { %>
            <!-- 🔍 Formulario de búsqueda -->
            <form action="ControladorPaqueteria" method="get" class="search-form">
                <input type="hidden" name="accion" value="buscar">
                <div class="row g-2 align-items-center">
                    <div class="col-md-4">
                        <input type="text" name="numeroGuia" value="${param.numeroGuia != null ? param.numeroGuia : ''}"
                               class="form-control" placeholder="Número de guía...">
                    </div>
                    <div class="col-md-4">
                        <input type="text" name="nombreResidente" value="${param.nombreResidente != null ? param.nombreResidente : ''}"
                               class="form-control" placeholder="Nombre del residente...">
                    </div>
                    <div class="col-md-2">
                        <select name="estado" class="form-select">
                            <option value="">-- Todos los estados --</option>
                            <option value="pendiente" ${param.estado == 'pendiente' ? 'selected' : ''}>Pendiente</option>
                            <option value="entregado" ${param.estado == 'entregado' ? 'selected' : ''}>Entregado</option>
                        </select>
                    </div>
                    <div class="col-md-2 d-flex gap-2">
                        <button class="btn btn-primary w-50" type="submit"><i class="bi bi-search"></i></button>
                        <button type="button" class="btn btn-clear w-50" onclick="window.location.href = 'ControladorPaqueteria?accion=listar'">
                            <i class="bi bi-x-circle"></i>
                        </button>
                    </div>
                </div>
            </form>

            <!-- 🧾 Tabla de resultados -->
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
                        <% for (Paqueteria p : lista) {%>
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
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } else {%>
            <!-- 📦 Mensaje cuando no hay paquetes -->
            <div class="text-center py-5">
                <i class="bi bi-inbox" style="font-size:3rem; color:#94a3b8;"></i>
                <h4 class="mt-3 text-secondary">
                    <%= (mensaje != null ? mensaje : (esBusqueda ? "No se encontraron resultados para la búsqueda" : "No hay paquetería pendiente de entregar"))%>
                </h4>
            </div>
            <% }%>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function () {

                // ✅ Entregar paquete
                document.querySelectorAll(".btn-entregar").forEach(btn => {
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
                document.querySelectorAll(".btn-eliminar").forEach(btn => {
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
