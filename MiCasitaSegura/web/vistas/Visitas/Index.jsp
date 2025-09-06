<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuarios" %>

<!DOCTYPE html>
%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Visitas" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Lista de Visitas</title>
        <link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <style>
            /* Reutilizamos todo el CSS del Index de usuarios, puedes mantener exactamente igual */
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #0f0f23 0%, #1a1a3e 100%);
                color: #ffffff;
                overflow-x: hidden;
            }
            .container { max-width: 1400px; margin: 0 auto; padding: 2rem 1rem; }
            .page-title { font-size: 3rem; font-weight: 700; background: linear-gradient(135deg, #667eea, #f093fb); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
            .main-card { background: rgba(255,255,255,0.05); border-radius: 24px; padding: 1rem; }
            .btn-add { background: linear-gradient(135deg, #4ade80, #22c55e); color: white; padding: 1rem 2rem; border-radius: 16px; text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem; }
            .modern-table { width: 100%; border-collapse: separate; border-spacing: 0; border-radius: 16px; overflow: hidden; }
            .modern-table th, .modern-table td { padding: 1rem; text-align: center; color: white; }
            .status-active { background: linear-gradient(135deg, #4ade80, #22c55e); color: white; padding: 0.5rem 1rem; border-radius: 50px; }
            .status-inactive { background: linear-gradient(135deg, #6b7280, #4b5563); color: white; padding: 0.5rem 1rem; border-radius: 50px; }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <h1 class="page-title">Gestión de Visitas</h1>
                <p class="page-subtitle">Administra y controla todas las visitas registradas</p>
            </div>

            <div class="main-card">
                <!-- Card Header -->
                <div class="card-header">
                    <div class="header-title">
                        <i class="fas fa-door-open"></i>
                        Lista de Visitas
                    </div>
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
                    <div class="user-stats" style="display:flex; gap:2rem; margin-top:1rem;">
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
                            <span>Inactivas</span>
                        </div>
                    </div>
                </div>

                <!-- Add New Visit Button -->
                <a href="${pageContext.request.contextPath}/ControladorVisitas?accion=add" class="btn-add">
                    <i class="fas fa-plus"></i>
                    Nueva Visita
                </a>

                <!-- Table Container -->
                <div class="table-container">
                    <table class="modern-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Visitante</th>
                                <th>DPI</th>
                                <th>Correo</th>
                                <th>Residente</th>
                                <th>Tipo de Visita</th>
                                <th>Fecha</th>
                                <th>Estado</th>
                                <th>Acciones</th>
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
                                <td><%= v.getFechaVisita()%></td>
                                <td>
                                    <span class="<%= v.isEstado() ? "status-active" : "status-inactive"%>">
                                        <i class="fas fa-<%= v.isEstado() ? "check-circle" : "times-circle"%>"></i>
                                        <%= v.isEstado() ? "Activa" : "Inactiva"%>
                                    </span>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/ControladorVisitas?accion=edit&id=<%= v.getIdVisita()%>" class="btn-action btn-edit">
                                            <i class="fas fa-edit"></i> Editar
                                        </a>
                                        <a href="${pageContext.request.contextPath}/ControladorVisitas?accion=delete&id=<%= v.getIdVisita()%>" 
                                           class="btn-action btn-delete"
                                           onclick="return confirmDeleteVisit('<%= v.getNombreVisitante()%>', '<%= v.getDpiVisitante()%>', '<%= v.getCorreoVisitante()%>');"
                                           title="Eliminar visita">
                                            <i class="fas fa-trash"></i> Eliminar
                                        </a>

                                    </div>
                                </td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="9" class="empty-state">
                                    <div class="empty-icon"><i class="fas fa-door-closed"></i></div>
                                    <div class="empty-title">No hay visitas registradas</div>
                                    <div class="empty-description">Comienza agregando tu primera visita al sistema</div>
                                </td>
                            </tr>
                            <% }%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

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

            // Enhanced confirm dialog for visits
            function confirmDeleteVisit(visitorName, visitorDPI, visitorEmail) {
                return confirm(`¿Estás seguro de que deseas eliminar esta visita?\n\nNombre: ${visitorName}\nDPI: ${visitorDPI}\nCorreo: ${visitorEmail}\n\nEsta acción no se puede deshacer.`);
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