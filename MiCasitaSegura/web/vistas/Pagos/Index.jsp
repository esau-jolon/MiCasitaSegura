<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Pagos" %>
<%@ page import="Modelo.Usuarios" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Pagos</title>

        <!-- Bootstrap -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-grid.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>

        <!-- SweetAlert2 -->
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>
        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

        <style>
            /* --- Fondo y estructura general --- */
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #0f0f23 0%, #1a1a3e 100%);
                min-height: 100vh;
                color: #ffffff;
                overflow-x: hidden;
            }

            body::before {
                content: '';
                position: fixed;
                top: 0; left: 0;
                width: 100%; height: 100%;
                background:
                    radial-gradient(circle at 20% 80%, rgba(120,119,198,0.3) 0%, transparent 50%),
                    radial-gradient(circle at 80% 20%, rgba(255,119,198,0.3) 0%, transparent 50%),
                    radial-gradient(circle at 40% 40%, rgba(120,219,226,0.2) 0%, transparent 50%);
                pointer-events: none;
                z-index: -1;
            }

            .container {
                max-width: 1400px;
                margin: 0 auto;
                padding: 2rem 1rem;
                animation: fadeInUp 0.8s ease-out;
            }

            @keyframes fadeInUp {
                from { opacity: 0; transform: translateY(30px); }
                to { opacity: 1; transform: translateY(0); }
            }

            .page-header {
                text-align: center;
                margin-bottom: 3rem;
            }

            .page-title {
                font-size: 3rem;
                font-weight: 700;
                background: linear-gradient(135deg, #667eea, #f093fb);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                margin-bottom: .5rem;
                animation: shimmer 2s ease-in-out infinite alternate;
            }

            @keyframes shimmer { from { filter: hue-rotate(0deg); } to { filter: hue-rotate(10deg); } }

            .page-subtitle {
                color: #a0a0a0;
                font-size: 1.1rem;
            }

            .main-card {
                background: rgba(255,255,255,0.05);
                backdrop-filter: blur(20px);
                border: 1px solid rgba(255,255,255,0.1);
                border-radius: 24px;
                overflow: hidden;
                transition: all .3s ease;
                max-width: 95%;
                margin: 0 auto;
            }

            .main-card:hover {
                transform: translateY(-2px);
                box-shadow: 0 25px 35px -5px rgba(0,0,0,0.4), 0 15px 15px -5px rgba(0,0,0,0.15);
            }

            .card-header {
                background: linear-gradient(135deg, #667eea, #764ba2);
                padding: 2rem;
                position: relative;
                overflow: hidden;
            }

            .header-title {
                font-size: 1.8rem;
                font-weight: 600;
                color: white;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: .75rem;
            }

            /* --- Botón principal --- */
            .btn-add {
                background: linear-gradient(135deg, #4ade80, #22c55e);
                border: none;
                border-radius: 16px;
                padding: 1rem 2rem;
                color: white;
                font-weight: 600;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: .75rem;
                transition: all .3s ease;
                box-shadow: 0 8px 20px rgba(74,222,128,.3);
                margin: 2rem auto;
                font-size: 1rem;
                justify-content: center;
            }

            .btn-add:hover {
                transform: translateY(-2px);
                box-shadow: 0 12px 25px rgba(74,222,128,.4);
                color: white;
            }

            /* --- Tabla --- */
            .table-container {
                padding: 0 2rem 2rem;
                overflow-x: auto;
            }

            .modern-table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                background: transparent;
                border-radius: 16px;
                overflow: hidden;
            }

            .modern-table thead {
                background: rgba(255,255,255,0.05);
            }

            .modern-table th, .modern-table td {
                padding: 1rem;
                text-align: center;
                vertical-align: middle;
                border: none;
            }

            .modern-table th {
                font-weight: 600;
                color: #a5b4fc;
                text-transform: uppercase;
            }

            .modern-table tbody tr {
                background: rgba(255,255,255,0.02);
                transition: all .3s ease;
            }

            .modern-table tbody tr:hover {
                background: rgba(255,255,255,0.08);
                transform: scale(1.01);
            }

            /* --- Estados --- */
            .status-realizado { background: linear-gradient(135deg, #4ade80, #22c55e); color: white; padding: .5rem 1rem; border-radius: 50px; font-weight: 600; }
            .status-cancelado { background: linear-gradient(135deg, #f87171, #ef4444); color: white; padding: .5rem 1rem; border-radius: 50px; font-weight: 600; }
            .status-pendiente { background: linear-gradient(135deg, #facc15, #fbbf24); color: #1a1a1a; padding: .5rem 1rem; border-radius: 50px; font-weight: 600; }

            /* --- Botones de acción --- */
            .action-buttons { display: flex; flex-wrap: wrap; gap: .5rem; justify-content: center; }

            .btn-action { padding: .6rem 1rem; border-radius: 12px; font-size: .85rem; font-weight: 500; border: none; transition: transform .2s ease-in-out; text-decoration: none; display: inline-flex; align-items: center; gap: .5rem; min-width: 90px; justify-content: center; }
            .btn-edit { background: linear-gradient(135deg, #fbbf24, #f59e0b); color: white; }
            .btn-cancel { background: linear-gradient(135deg, #f87171, #ef4444); color: white; }
            .btn-delete { background: linear-gradient(135deg, #6b7280, #374151); color: white; }
            .btn-confirm { background: linear-gradient(135deg, #4ade80, #22c55e); color: white; }
            .btn-cancel-pay { background: linear-gradient(135deg, #ef4444, #dc2626); color: white; }

            .btn-action:hover { transform: scale(1.05); }

            .disabled-action { opacity: 0.5; pointer-events: none; cursor: not-allowed; }

            .empty-state { text-align: center; padding: 4rem 2rem; color: #a0a0a0; }
            .empty-icon { font-size: 4rem; margin-bottom: 1rem; opacity: 0.5; }
        </style>
    </head>
    <body>

        <div class="container">
            <div class="main-card">
                <div class="page-header">
                    <h1 class="page-title">Gestión de Pagos</h1>
                    <p class="page-subtitle">Consulta, registra y administra los pagos realizados</p>
                </div>

                <div class="main-card">
                    <div class="card-header">
                        <div class="header-title"><i class="bi bi-cash-coin"></i> Lista de Pagos</div>
                    </div>

                    <a href="${pageContext.request.contextPath}/ControladorPago?accion=add" class="btn-add">
                        <i class="bi bi-credit-card"></i> Pagar Servicio
                    </a>

                    <div class="table-container">
                        <table class="modern-table">
                            <thead>
                                <tr>
                                    <th>ID</th><th>Usuario</th><th>Tipo</th><th>Fecha</th><th>Mes/Año</th>
                                    <th>Monto</th><th>Mora</th><th>Total</th><th>Observaciones</th><th>Estado</th><th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    List<Pagos> lista = (List<Pagos>) request.getAttribute("pagos");
                                    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
                                    boolean esAdmin = (usuarioSesion != null && "Administrador".equalsIgnoreCase(usuarioSesion.getNombreRol()));

                                    if (lista != null && !lista.isEmpty()) {
                                        for (Pagos p : lista) {
                                            String estado = p.getNombreEstadoPago();
                                            String claseEstado = "";
                                            String icono = "";
                                            boolean deshabilitar = false;

                                            if ("Realizado".equalsIgnoreCase(estado)) {
                                                claseEstado = "status-realizado";
                                                icono = "bi-check-circle-fill";
                                                deshabilitar = true;
                                            } else if ("Pendiente".equalsIgnoreCase(estado)) {
                                                claseEstado = "status-pendiente";
                                                icono = "bi-hourglass-split";
                                            } else if ("Cancelado".equalsIgnoreCase(estado)) {
                                                claseEstado = "status-cancelado";
                                                icono = "bi-x-circle-fill";
                                            }
                                %>
                                <tr>
                                    <td>#<%= p.getIdPago()%></td>
                                    <td><%= p.getNombreUsuario()%></td>
                                    <td><%= p.getNombreTipoPago()%></td>
                                    <td><%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getFechaPago())%></td>
                                    <td><%= (p.getMesPagado() != null && p.getAnioPagado() != null) ? p.getMesPagado() + "/" + p.getAnioPagado() : "-"%></td>
                                    <td>Q <%= p.getMonto()%></td>
                                    <td>Q <%= p.getMora()%></td>
                                    <td><strong>Q <%= p.getTotal()%></strong></td>
                                    <td><%= p.getObservaciones() != null ? p.getObservaciones() : "-"%></td>
                                    <td>
                                        <span class="<%= claseEstado%>">
                                            <i class="bi <%= icono%>"></i> <%= estado%>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/ControladorPago?accion=edit&id=<%= p.getIdPago()%>"
                                               class="btn-action btn-edit <%= deshabilitar ? "disabled-action" : ""%>"
                                               title="<%= deshabilitar ? "No se puede editar un pago realizado" : ""%>">
                                                <i class="bi bi-pencil-square"></i> Editar
                                            </a>

                                            <% if (esAdmin) {%>
                                            <!-- 🟢 Confirmar pago -->
                                            <button type="button" class="btn-action btn-confirm"
                                                    onclick="confirmarPago(<%= p.getIdPago()%>, <%= p.getTotal()%>)">
                                                <i class="bi bi-check2-circle"></i> Confirmar
                                            </button>

                                            <!-- 🔴 Cancelar pago -->
                                            <button type="button" class="btn-action btn-cancel-pay"
                                                    onclick="cancelarPago(<%= p.getIdPago()%>, <%= p.getTotal()%>)">
                                                <i class="bi bi-x-circle"></i> Cancelar
                                            </button>
                                            <% }%>

                                            <!-- 🗑️ Eliminar -->
                                            <button type="button"
                                                    class="btn-action btn-delete"
                                                    onclick="confirmarEliminacion(<%= p.getIdPago()%>, <%= p.getTotal()%>)">
                                                <i class="bi bi-trash"></i> Eliminar
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                                <% }
                                } else { %>
                                <tr>
                                    <td colspan="11" class="text-center text-muted py-5">
                                        <i class="bi bi-wallet-x display-6 d-block mb-3"></i>
                                        No hay pagos registrados aún
                                    </td>
                                </tr>
                                <% }%>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // 🟢 Confirmar pago (estado 2)
            function confirmarPago(idPago, total) {
                Swal.fire({
                    title: '¿Procesar este pago?',
                    text: "Total: Q" + total.toFixed(2),
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#22c55e',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Sí, confirmar',
                    cancelButtonText: 'Cancelar'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = '<%=request.getContextPath()%>/ControladorPago?accion=confirmar&id=' + idPago;
                    }
                });
            }

            // 🔴 Cancelar pago (estado 3)
            function cancelarPago(idPago, total) {
                Swal.fire({
                    title: '¿Deseas cancelar este pago?',
                    text: "Total: Q" + total.toFixed(2),
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#ef4444',
                    cancelButtonColor: '#3085d6',
                    confirmButtonText: 'Sí, cancelar',
                    cancelButtonText: 'Volver'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = '<%=request.getContextPath()%>/ControladorPago?accion=cancelarPago&id=' + idPago;
                    }
                });
            }

            // 🗑️ Eliminar lógico
            function confirmarEliminacion(idPago, total) {
                Swal.fire({
                    title: '¿Deseas eliminar este pago?',
                    text: "Total: Q" + total.toFixed(2),
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#6b7280',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Sí, eliminar',
                    cancelButtonText: 'Cancelar'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = '<%=request.getContextPath()%>/ControladorPago?accion=eliminar&id=' + idPago;
                    }
                });
            }
        </script>
    </body>
</html>
