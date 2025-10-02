<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Pagos" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Pagos</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-grid.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>

    <!-- FontAwesome (opcional) -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>

    <!-- SweetAlert2 -->
    <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>
    <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #0f0f23 0%, #1a1a3e 100%);
            min-height: 100vh;
            color: #ffffff;
            overflow-x: hidden;
        }
        .container { max-width: 1400px; margin: 0 auto; padding: 2rem 1rem; }
        .page-header { text-align: center; margin-bottom: 3rem; }
        .page-title {
            font-size: 3rem; font-weight: 700;
            background: linear-gradient(135deg, #667eea, #f093fb);
            -webkit-background-clip: text; -webkit-text-fill-color: transparent;
        }
        .main-card {
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 24px;
            overflow: hidden;
            margin: 0 auto;
        }
        .card-header { background: linear-gradient(135deg, #667eea, #764ba2); padding: 2rem; }
        .header-title { font-size: 1.8rem; font-weight: 600; color: white; display: flex; align-items: center; gap: 0.75rem; }
        .btn-add {
            background: linear-gradient(135deg, #4ade80, #22c55e);
            border: none; border-radius: 16px; padding: 1rem 2rem;
            color: white; font-weight: 600; text-decoration: none;
            display: inline-flex; align-items: center; gap: 0.75rem;
            margin: 2rem; font-size: 1rem;
        }
        .modern-table { width: 100%; border-collapse: separate; border-spacing: 0; }
        .modern-table th, .modern-table td {
            padding: 1rem; text-align: center; color: #fff;
        }
        .status-realizado {
            background: linear-gradient(135deg, #4ade80, #22c55e);
            color: white; padding: 0.5rem 1rem; border-radius: 50px;
        }
        .status-cancelado {
            background: linear-gradient(135deg, #f87171, #ef4444);
            color: white; padding: 0.5rem 1rem; border-radius: 50px;
        }
        .action-buttons { display: flex; gap: 0.5rem; justify-content: center; }
        .btn-action { padding: 0.6rem 1rem; border-radius: 12px; font-size: 0.85rem; font-weight: 500; }
        .btn-edit { background: linear-gradient(135deg, #fbbf24, #f59e0b); color: white; }
        .btn-cancel { background: linear-gradient(135deg, #f87171, #ef4444); color: white; }
    </style>
</head>
<body>

<div class="container">
    <!-- Page Header -->
    <div class="page-header">
        <h1 class="page-title">Gestión de Pagos</h1>
        <p class="page-subtitle">Consulta, registra y administra los pagos realizados</p>
    </div>

    <!-- Main Card -->
    <div class="main-card">
        <!-- Card Header -->
        <div class="card-header">
            <div class="header-content">
                <div class="header-title">
                    <i class="bi bi-cash-coin"></i> Lista de Pagos
                </div>
            </div>
        </div>

        <!-- Add New Pago Button -->
        <a href="${pageContext.request.contextPath}/ControladorPago?accion=add" class="btn-add">
            <i class="bi bi-credit-card"></i> Registrar Pago
        </a>

        <!-- Table -->
        <div class="table-container">
            <table class="modern-table">
                <thead>
                    <tr>
                        <th><i class="bi bi-hash"></i> ID</th>
                        <th><i class="bi bi-person"></i> Usuario</th>
                        <th><i class="bi bi-wallet2"></i> Tipo de Pago</th>
                        <th><i class="bi bi-calendar-date"></i> Fecha</th>
                        <th><i class="bi bi-currency-dollar"></i> Monto</th>
                        <th><i class="bi bi-exclamation-triangle"></i> Mora</th>
                        <th><i class="bi bi-cash-stack"></i> Total</th>
                        <th><i class="bi bi-chat-text"></i> Observaciones</th>
                        <th><i class="bi bi-toggle-on"></i> Estado</th>
                        <th><i class="bi bi-gear"></i> Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Pagos> lista = (List<Pagos>) request.getAttribute("pagos");
                        if (lista != null && !lista.isEmpty()) {
                            for (Pagos p : lista) {
                    %>
                    <tr>
                        <td>#<%= p.getIdPago() %></td>
                        <td><%= p.getNombreUsuario() %></td>
                        <td><%= p.getNombreTipoPago() %></td>
                        <td><%= p.getFechaPago() %></td>
                        <td>Q <%= p.getMonto() %></td>
                        <td>Q <%= p.getMora() %></td>
                        <td><strong>Q <%= p.getTotal() %></strong></td>
                        <td><%= p.getObservaciones() != null ? p.getObservaciones() : "-" %></td>
                        <td>
                            <span class="<%= p.getEstado().equals("Realizado") ? "status-realizado" : "status-cancelado" %>">
                                <i class="bi <%= p.getEstado().equals("Realizado") ? "bi-check-circle-fill" : "bi-x-circle-fill" %>"></i>
                                <%= p.getEstado() %>
                            </span>
                        </td>
                        <td>
                            <div class="action-buttons">
                                <a href="${pageContext.request.contextPath}/ControladorPago?accion=edit&id=<%= p.getIdPago()%>" 
                                   class="btn-action btn-edit">
                                    <i class="bi bi-pencil-square"></i> Editar
                                </a>
                                <a href="${pageContext.request.contextPath}/ControladorPago?accion=cancelar&id=<%= p.getIdPago()%>" 
                                   class="btn-action btn-cancel"
                                   onclick="return confirm('¿Estás seguro de que deseas cancelar este pago?\n\nTotal: Q<%= p.getTotal() %>');">
                                    <i class="bi bi-x-circle"></i> Cancelar
                                </a>
                            </div>
                        </td>
                    </tr>
                    <%  }
                    } else { %>
                    <tr>
                        <td colspan="10" class="empty-state">
                            <div class="empty-icon"><i class="bi bi-wallet-x"></i></div>
                            <div class="empty-title">No hay pagos registrados</div>
                            <div class="empty-description">Comienza registrando tu primer pago</div>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
