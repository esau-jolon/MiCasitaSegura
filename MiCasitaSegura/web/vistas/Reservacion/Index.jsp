<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Reservas" %>
<%@ page import="Modelo.Usuarios" %>

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
                            <th>Persona Que Reserva</th>
                            <th>Fecha Reservada</th>
                            <th>Hora Inicio</th>
                            <th>Hora Fin</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Reservas> lista = (List<Reservas>) request.getAttribute("listaReservas");
                            Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
                            String rol = (usuarioSesion != null) ? usuarioSesion.getNombreRol() : "";
                            if (lista != null && !lista.isEmpty()) {
                                for (Reservas r : lista) {
                        %>
                        <tr>
                            <td>#<%= r.getIdReserva()%></td>
                            <td><%= r.getNombreArea()%></td>
                            <td><%= (r.getNombreResidente() != null ? r.getNombreResidente() + " " + r.getApellidoResidente() : "—")%></td>
                            <%
                                java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");
                                String fechaFormateada = (r.getFechaReserva() != null) ? formatoFecha.format(r.getFechaReserva()) : "—";
                            %>
                            <td><%= fechaFormateada%></td>
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
                                <% if (r.getNombreEstado().equalsIgnoreCase("Pendiente")) {
                            if ("Administrador".equalsIgnoreCase(rol)) {%>
                                <button type="button" class="btn btn-success btn-sm btn-confirmar" data-id="<%= r.getIdReserva()%>">
                                    <i class="bi bi-check-circle"></i> Confirmar
                                </button>
                                <button type="button" class="btn btn-danger btn-sm btn-cancelar" data-id="<%= r.getIdReserva()%>">
                                    <i class="bi bi-x-circle"></i> Cancelar
                                </button>
                                <% } else {%>
                                <button type="button" class="btn btn-danger btn-sm btn-cancelar" data-id="<%= r.getIdReserva()%>">
                                    <i class="bi bi-x-circle"></i> Cancelar
                                </button>
                                <% }
                        } else { %>
                                <span class="text-muted">N/A</span>
                                <% } %>
                            </td>
                        </tr>
                        <% }
            } else { %>
                        <tr>
                            <td colspan="8" class="text-center text-secondary py-4">
                                <i class="bi bi-calendar-x" style="font-size:2rem;"></i><br>
                                No hay reservas registradas.
                            </td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <script>
            document.addEventListener("DOMContentLoaded", function () {

                // 🟥 CANCELAR RESERVA
                const botonesCancelar = document.querySelectorAll(".btn-cancelar");
                botonesCancelar.forEach(boton => {
                    boton.addEventListener("click", function () {
                        const idReserva = this.getAttribute("data-id");
                        Swal.fire({
                            title: "¿Desea cancelar la reserva?",
                            text: "Esta acción no se puede deshacer.",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#d33",
                            cancelButtonColor: "#6c757d",
                            confirmButtonText: "Sí, cancelar",
                            cancelButtonText: "No, mantener"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = "ControladorReserva?accion=cancelar&id=" + idReserva;
                            }
                        });
                    });
                });

                // 🟩 CONFIRMAR RESERVA (solo admin)
                const botonesConfirmar = document.querySelectorAll(".btn-confirmar");
                botonesConfirmar.forEach(boton => {
                    boton.addEventListener("click", function () {
                        const idReserva = this.getAttribute("data-id");
                        Swal.fire({
                            title: "¿Confirmar esta reserva?",
                            text: "El residente será notificado por correo electrónico.",
                            icon: "question",
                            showCancelButton: true,
                            confirmButtonColor: "#198754",
                            cancelButtonColor: "#6c757d",
                            confirmButtonText: "Sí, confirmar",
                            cancelButtonText: "Cancelar"
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = "ControladorReserva?accion=confirmar&id=" + idReserva;
                            }
                        });
                    });
                });
            });
        </script>

    </body>
</html>
