<%@page import="Modelo.Visitas"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuarios" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lista de Usuarios</title>

        <!-- Bootstrap local -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-grid.min.css"/>

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>

        <!-- FontAwesome local -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>

        <!-- SweetAlert2 local -->
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <!-- Bootstrap JS local -->
        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

        <style>
            .root-vars {
                primary-color: #667eea;
                secondary-color: #764ba2;
                accent-color: #f093fb;
                success-color: #4ade80;
                warning-color: #fbbf24;
                danger-color: #f87171;
                background-color: #0f0f23;
                card-background: rgba(255, 255, 255, 0.05);
                text-primary: #ffffff;
                text-secondary: #a0a0a0;
                border-color: rgba(255, 255, 255, 0.1);
                shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.3), 0 10px 10px -5px rgba(0, 0, 0, 0.1);
            }

            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #0f0f23 0%, #1a1a3e 100%);
                min-height: 100vh;
                color: #ffffff;
                overflow-x: hidden;
                primary-color: #667eea;
                secondary-color: #764ba2;
                accent-color: #f093fb;
                success-color: #4ade80;
                warning-color: #fbbf24;
                danger-color: #f87171;
                background-color: #0f0f23;
                card-background: rgba(255, 255, 255, 0.05);
                text-primary: #ffffff;
                text-secondary: #a0a0a0;
                border-color: rgba(255, 255, 255, 0.1);
                shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.3), 0 10px 10px -5px rgba(0, 0, 0, 0.1);
            }

            body::before {
                content: '';
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: 
                    radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%),
                    radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.3) 0%, transparent 50%),
                    radial-gradient(circle at 40% 40%, rgba(120, 219, 226, 0.2) 0%, transparent 50%);
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
                from {
                    opacity: 0;
                    transform: translateY(30px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
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
                background-clip: text;
                margin-bottom: 0.5rem;
                animation: shimmer 2s ease-in-out infinite alternate;
            }

            @keyframes shimmer {
                from {
                    filter: hue-rotate(0deg);
                }
                to {
                    filter: hue-rotate(10deg);
                }
            }

            .page-subtitle {
                color: #a0a0a0;
                font-size: 1.2rem;
                font-weight: 300;
            }

            .main-card {
                background: rgba(255, 255, 255, 0.05);
                backdrop-filter: blur(20px);
                border: 1px solid rgba(255, 255, 255, 0.1);
                border-radius: 24px;
                box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.3), 0 10px 10px -5px rgba(0, 0, 0, 0.1);
                overflow: hidden;
                transition: all 0.3s ease;

                max-width: 95%;   /* antes estaba limitada */
                margin: 0 auto;


            }

            .main-card:hover {
                transform: translateY(-2px);
                box-shadow: 0 25px 35px -5px rgba(0, 0, 0, 0.4), 0 15px 15px -5px rgba(0, 0, 0, 0.15);
            }

            .card-header {
                background: linear-gradient(135deg, #667eea, #764ba2);
                padding: 2rem;
                position: relative;
                overflow: hidden;
            }

            .card-header::before {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
                animation: shine 3s ease-in-out infinite;
            }

            @keyframes shine {
                0% { left: -100%; }
                50% { left: 100%; }
                100% { left: 100%; }
            }

            .header-content {
                display: flex;
                justify-content: space-between;
                align-items: center;
                position: relative;
                z-index: 2;
            }

            .header-title {
                font-size: 1.8rem;
                font-weight: 600;
                color: white;
                display: flex;
                align-items: center;
                gap: 0.75rem;
            }

            .user-stats {
                display: flex;
                gap: 2rem;
                color: rgba(255, 255, 255, 0.9);
                font-size: 0.9rem;
            }

            .stat-item {
                text-align: center;
            }

            .stat-number {
                display: block;
                font-size: 1.5rem;
                font-weight: 700;
            }

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
                gap: 0.75rem;
                transition: all 0.3s ease;
                box-shadow: 0 8px 20px rgba(74, 222, 128, 0.3);
                margin: 2rem;
                font-size: 1rem;
            }

            .btn-add:hover {
                transform: translateY(-2px);
                box-shadow: 0 12px 25px rgba(74, 222, 128, 0.4);
                color: white;
            }

            .btn-add:active {
                transform: translateY(0);
            }

            .table-container {
                padding: 0 2rem 2rem;
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
                background: rgba(255, 255, 255, 0.05);
            }

            .modern-table th {
                padding: 1.5rem 1rem;
                font-weight: 600;
                color: var(text-primary);
                border: none;
                text-align: center;
                font-size: 0.95rem;
                letter-spacing: 0.5px;
                position: relative;
            }

            .modern-table th::after {
                content: '';
                position: absolute;
                bottom: 0;
                left: 0;
                right: 0;
                height: 2px;
                background: linear-gradient(90deg, #667eea, #f093fb);
            }

            .modern-table tbody tr {
                background: rgba(255, 255, 255, 0.02);
                border-bottom: 1px solid rgba(255, 255, 255, 0.1);
                transition: all 0.3s ease;
            }

            .modern-table tbody tr:hover {
                background: rgba(255, 255, 255, 0.08);
                transform: scale(1.01);
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            }

            .modern-table td {
                padding: 1.25rem 1rem;
                border: none;
                color: #ffffff;
                vertical-align: middle;
                text-align: center;
            }

            .user-id {
                font-weight: 700;
                color: #f093fb;
                font-size: 1.1rem;
            }

            .user-info {
                text-align: left !important;
            }

            .user-name {
                font-weight: 600;
                font-size: 1rem;
                margin-bottom: 0.25rem;
            }

            .user-email {
                color: #a0a0a0;
                font-size: 0.9rem;
            }

            .status-badge {
                padding: 0.5rem 1rem;
                border-radius: 50px;
                font-size: 0.85rem;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                border: none;
                position: relative;
                overflow: hidden;
            }

            .status-active {
                background: linear-gradient(135deg, #4ade80, #22c55e);
                color: white;
                box-shadow: 0 4px 15px rgba(74, 222, 128, 0.3);
            }

            .status-inactive {
                background: linear-gradient(135deg, #6b7280, #4b5563);
                color: white;
                box-shadow: 0 4px 15px rgba(107, 114, 128, 0.3);
            }

            .role-badge {
                background: linear-gradient(135deg, #667eea, #764ba2);
                color: white;
                padding: 0.4rem 0.8rem;
                border-radius: 12px;
                font-size: 0.8rem;
                font-weight: 500;
                box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
            }

            .action-buttons {
                display: flex;
                gap: 0.5rem;
                justify-content: center;
            }

            .btn-action {
                padding: 0.6rem 1rem;
                border: none;
                border-radius: 12px;
                font-size: 0.85rem;
                font-weight: 500;
                text-decoration: none;
                transition: all 0.3s ease;
                display: inline-flex;
                align-items: center;
                gap: 0.5rem;
                min-width: 90px;
                justify-content: center;
            }

            .btn-edit {
                background: linear-gradient(135deg, #fbbf24, #f59e0b);
                color: white;
                box-shadow: 0 4px 15px rgba(251, 191, 36, 0.3);
            }

            .btn-edit:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(251, 191, 36, 0.4);
                color: white;
            }

            .btn-delete {
                background: linear-gradient(135deg, #f87171, #ef4444);
                color: white;
                box-shadow: 0 4px 15px rgba(248, 113, 113, 0.3);
            }

            .btn-delete:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(248, 113, 113, 0.4);
                color: white;
            }

            .empty-state {
                text-align: center;
                padding: 4rem 2rem;
                color: var(text-secondary);
            }

            .empty-icon {
                font-size: 4rem;
                margin-bottom: 1rem;
                opacity: 0.5;
            }

            .empty-title {
                font-size: 1.5rem;
                font-weight: 600;
                margin-bottom: 0.5rem;
                color: #ffffff;
            }

            .empty-description {
                font-size: 1rem;
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                .container {
                    padding: 1rem;
                }

                .page-title {
                    font-size: 2rem;
                }

                .header-content {
                    flex-direction: column;
                    gap: 1rem;
                    text-align: center;
                }

                .user-stats {
                    gap: 1rem;
                }

                .btn-add {
                    margin: 1rem;
                    padding: 0.8rem 1.5rem;
                }

                .table-container {
                    padding: 0 1rem 1rem;
                }

                .modern-table {
                    font-size: 0.9rem;
                }

                .modern-table th,
                .modern-table td {
                    padding: 1rem 0.5rem;
                }

                .action-buttons {
                    flex-direction: column;
                }

                .btn-action {
                    min-width: 80px;
                    padding: 0.5rem 0.8rem;
                    font-size: 0.8rem;
                }
            }

            .loading {
                display: inline-block;
                width: 20px;
                height: 20px;
                border: 2px solid rgba(255, 255, 255, 0.3);
                border-radius: 50%;
                border-top-color: #f093fb;
                animation: spin 0.8s ease-in-out infinite;
            }

            @keyframes spin {
                to {
                    transform: rotate(360deg);
                }
            }

            /* Hover effects for icons */
            .fa {
                transition: transform 0.2s ease;
            }

            .btn-action:hover .fa {
                transform: scale(1.1);
            }
        </style>
    </head>
    <body>
        <div class="container">
            <!-- Page Header -->
            <div class="page-header">
                <h1 class="page-title">Gestión de Visitas</h1>
                <p class="page-subtitle">Administra y controla todas las visitas registradas</p>
            </div>

            <!-- Main Card -->
            <div class="main-card">
                <!-- Card Header -->
                <div class="card-header">
                    <div class="header-content">
                        <div class="header-title">
                            <i class="bi bi-door-open"></i>
                            Lista de Visitas
                        </div>
                        <div class="user-stats">
                            <%
                                List<Visitas> lista = (List<Visitas>) request.getAttribute("visitas");
                                int totalVisitas = (lista != null) ? lista.size() : 0;
                                int visitasActivas = 0;
                                if (lista != null) {
                                    for (Visitas v : lista) {
                                        if (v.isEstado()) {
                                            visitasActivas++;
                                        }
                                    }
                                }
                            %>
                            <div class="stat-item">
                                <span class="stat-number"><%= totalVisitas%></span>
                                <span>Total</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-number"><%= visitasActivas%></span>
                                <span>Activas</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-number"><%= totalVisitas - visitasActivas%></span>
                                <span>Canceladas</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Add New Visit Button -->
                <a href="${pageContext.request.contextPath}/ControladorVisita?accion=add" class="btn-add">
                    <i class="bi bi-plus-circle"></i>
                    Registrar Visita
                </a>

                <!-- Table Container -->
                <div class="table-container">
                    <table class="modern-table">
                        <thead>
                            <tr>
                                <th><i class="bi bi-hash"></i> ID</th>
                                <th><i class="bi bi-person"></i> Visitante</th>
                                <th><i class="bi bi-credit-card-2-front"></i> DPI</th>
                                <th><i class="bi bi-envelope"></i> Correo</th>
                                <th><i class="bi bi-house"></i> Residente</th>
                                <th><i class="bi bi-list-ul"></i> Tipo</th>
                                <th><i class="bi bi-calendar-date"></i> Fecha</th>
                                <th><i class="bi bi-toggle-on"></i> Estado</th>
                                <th><i class="bi bi-gear"></i> Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (lista != null && !lista.isEmpty()) {
                                    for (Visitas v : lista) {
                            %>
                            <tr>
                                <td>#<%= v.getIdVisita()%></td>
                                <td><%= v.getNombreVisitante()%></td>
                                <td><%= v.getDpiVisitante()%></td>
                                <td><%= v.getCorreoVisitante()%></td>
                                <td><%= v.getIdResidente()%></td>
                                <td><%= v.getTipoVisita()%></td>
                                <td><%= v.getFechaVisita() != null ? v.getFechaVisita() : "-"%></td>
                                <td>
                                    <span class="status-badge <%= v.isEstado() ? "status-active" : "status-inactive"%>">
                                        <i class="bi <%= v.isEstado() ? "bi-check-circle-fill" : "bi-x-circle-fill"%>"></i>
                                        <%= v.isEstado() ? "Activo" : "Cancelada"%>
                                    </span>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <% if (v.isEstado()) {%>
                                        <!-- ✅ SOLO SI LA VISITA ESTÁ ACTIVA -->
                                        <a href="${pageContext.request.contextPath}/ControladorVisita?accion=edit&id=<%= v.getIdVisita()%>" 
                                           class="btn-action btn-edit" title="Editar visita">
                                            <i class="bi bi-pencil-square"></i> Editar
                                        </a>

                                        <a href="${pageContext.request.contextPath}/ControladorVisita?accion=delete&id=<%= v.getIdVisita()%>" 
                                           class="btn-action btn-delete"
                                           onclick="return confirm('¿Seguro que deseas cancelar esta visita?\n\nVisitante: <%= v.getNombreVisitante()%>\nCorreo: <%= v.getCorreoVisitante()%>');"
                                           title="Cancelar visita">
                                            <i class="bi bi-slash-circle"></i> Cancelar
                                        </a>

                                        <a href="${pageContext.request.contextPath}/ControladorVisita?accion=descargarQR&id=<%= v.getIdVisita()%>"
                                           class="btn-action btn-edit"
                                           style="background: linear-gradient(135deg, #60a5fa, #3b82f6); box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);"
                                           title="Descargar código QR">
                                            <i class="bi bi-qr-code"></i> QR
                                        </a>
                                        <% } else { %>
                                        <!-- ❌ SI LA VISITA ESTÁ CANCELADA -->
                                        <span class="text-muted" style="font-size: 0.9rem;">
                                            <i class="bi bi-info-circle"></i> Sin acciones disponibles
                                        </span>
                                        <% } %>
                                    </div>
                                </td>


                                
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="9" class="empty-state">
                                    <div class="empty-icon">
                                        <i class="bi bi-door-closed"></i>
                                    </div>
                                    <div class="empty-title">No hay visitas registradas</div>
                                    <div class="empty-description">Comienza registrando tu primera visita</div>
                                </td>
                            </tr>
                            <% }%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>

    <!-- Scripts -->
    <script>
        // Smooth scroll and animation effects
        document.addEventListener('DOMContentLoaded', function () {
            // Add loading effect to buttons
            const buttons = document.querySelectorAll('.btn-action, .btn-add');
            buttons.forEach(button => {
                button.addEventListener('click', function (e) {
                    if (!this.classList.contains('btn-delete')) {
                        const icon = this.querySelector('i');
                        const originalClass = icon.className;
                        icon.className = 'fas fa-spinner fa-spin';

                        setTimeout(() => {
                            icon.className = originalClass;
                        }, 1000);
                    }
                });
            });

            // Enhanced table row hover effects
            const tableRows = document.querySelectorAll('.modern-table tbody tr');
            tableRows.forEach(row => {
                row.addEventListener('mouseenter', function () {
                    this.style.transform = 'scale(1.01)';
                });

                row.addEventListener('mouseleave', function () {
                    this.style.transform = 'scale(1)';
                });
            });

            // Add ripple effect to buttons
            function createRipple(event) {
                const button = event.currentTarget;
                const circle = document.createElement('span');
                const diameter = Math.max(button.clientWidth, button.clientHeight);
                const radius = diameter / 2;

                circle.style.width = circle.style.height = `${diameter}px`;
                circle.style.left = `${event.clientX - button.offsetLeft - radius}px`;
                circle.style.top = `${event.clientY - button.offsetTop - radius}px`;
                circle.classList.add('ripple');

                const ripple = button.getElementsByClassName('ripple')[0];
                if (ripple) {
                    ripple.remove();
                }

                button.appendChild(circle);
            }

            const rippleButtons = document.querySelectorAll('.btn-add, .btn-action');
            rippleButtons.forEach(button => {
                button.addEventListener('click', createRipple);
            });
        });

        // Enhanced confirm dialog
        function confirmDelete(userName, userEmail) {
            return confirm(`¿Estás seguro de que deseas eliminar este usuario?\n\nNombre: ${userName}\nCorreo: ${userEmail}\n\nEsta acción no se puede deshacer.`);
        }
    </script>

    <style>
        /* Ripple effect */
        .ripple {
            position: absolute;
            border-radius: 50%;
            transform: scale(0);
            animation: ripple-animation 600ms linear;
            background-color: rgba(255, 255, 255, 0.3);
            pointer-events: none;
        }

        @keyframes ripple-animation {
            to {
                transform: scale(4);
                opacity: 0;
            }
        }
    </style>

</body>
</html>