<%@ page import="Modelo.Usuarios" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Mi Casita Segura</title>

        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>

        <style>
            body {
                margin: 0;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f0f2f5;
            }

            .sidebar {
                width: 80px;
                height: 100vh;
                position: fixed;
                top: 0;
                left: 0;
                background: linear-gradient(180deg, #0d6efd, #0a58ca);
                color: #fff;
                padding-top: 20px;
                transition: width 0.3s ease;
                overflow: hidden;
                z-index: 1000;
            }

            .sidebar:hover { width: 280px; }

            .sidebar .logo {
                display: flex;
                align-items: center;
                justify-content: flex-start;
                gap: 8px;
                padding: 0 20px;
                margin-bottom: 30px;
                font-size: 1.8rem;
                font-weight: bold;
                white-space: nowrap;
            }

            .sidebar .logo i {
                font-size: 2.5rem;
                margin: 0 auto;
            }

            .sidebar .logo span {
                opacity: 0;
                visibility: hidden;
                transition: opacity 0.3s ease;
                font-size: 1.2rem;
            }

            .sidebar:hover .logo span {
                opacity: 1;
                visibility: visible;
            }

            .sidebar ul {
                list-style: none;
                padding-left: 0;
            }

            .sidebar ul li {
                margin: 20px 0;
            }

            .sidebar ul li a {
                color: #fff;
                text-decoration: none;
                padding: 15px 20px;
                display: flex;
                align-items: center;
                gap: 15px;
                border-radius: 12px;
                transition: background 0.3s;
                font-size: 1.1rem;
                font-weight: 600;
            }

            .sidebar ul li a:hover {
                background: rgba(255, 255, 255, 0.2);
            }

            .sidebar ul li a span {
                opacity: 0;
                visibility: hidden;
                transition: opacity 0.3s ease;
            }

            .sidebar:hover ul li a span {
                opacity: 1;
                visibility: visible;
            }

            .content {
                margin-left: 80px;
                padding: 30px;
                transition: margin-left 0.3s ease;
            }

            .sidebar:hover ~ .content {
                margin-left: 280px;
            }

            .user-header {
                width: 100%;
                background: linear-gradient(180deg, #0d6efd, #0a58ca);
                padding: 15px 25px;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                margin-bottom: 20px;
                font-size: 1.3rem;
                font-weight: 600;
                color: #ffffff;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .user-header i {
                font-size: 1.8rem;
                color: #ffd700;
            }

            iframe {
                width: 95%;
                height: 80vh;
                border: none;
                border-radius: 15px;
                background: #ffffff;
                box-shadow: 0 10px 30px rgba(0,0,0,0.15);
                transition: all 0.3s ease;
                display: block;
                margin: 0 auto;
            }

            iframe:hover {
                box-shadow: 0 15px 40px rgba(0,0,0,0.2);
            }
        </style>
    </head>
    <body>

        <%
            Usuarios usuario = (Usuarios) session.getAttribute("usuario");
            String rol = (usuario != null && usuario.getNombreRol() != null) ? usuario.getNombreRol() : "";
        %>

        <!-- Sidebar -->
        <div class="sidebar">
            <div class="logo">
                <i class="bi bi-house-fill"></i>
                <span>MI CASITA SEGURA</span>
            </div>

            <ul>
                <!--  Directorio (todos los roles) -->
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorDirectorio?accion=listar" target="contentFrame">
                        <i class="bi bi-journal-bookmark"></i><span> DIRECTORIO</span>
                    </a>
                </li>

                <!--  Mantenimiento de usuarios (solo Administrador) -->
                <% if ("Administrador".equalsIgnoreCase(rol)) { %>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorUsuario?accion=listar" target="contentFrame">
                        <i class="bi bi-people"></i><span> MANTENIMIENTO DE USUARIOS</span>
                    </a>
                </li>
                <% } %>

                <!--  Registrar visitante (Administrador, Residente, Guardia) -->
                <% if ("Administrador".equalsIgnoreCase(rol)
                        || "Residente".equalsIgnoreCase(rol)
                        || "Guardia".equalsIgnoreCase(rol)) { %>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorVisita?accion=listar" target="contentFrame">
                        <i class="bi bi-door-open"></i><span> REGISTRAR VISITANTE</span>
                    </a>
                </li>
                <% } %>

                <!--  Comunicación interna (todos los roles menos null) -->
                <% if (!rol.isEmpty()) { %>
                <li>
                    <a href="<%=request.getContextPath()%>/vistas/Comunicacion/MenuComunicacion.jsp" target="contentFrame">
                        <i class="bi bi-chat-dots"></i><span> COMUNICACIÓN INTERNA</span>
                    </a>
                </li>
                <% } %>

                <!-- Reportes de mantenimiento (Administrador y Residente) -->
                <% if ("Administrador".equalsIgnoreCase(rol)
                        || "Residente".equalsIgnoreCase(rol)) { %>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorReporteMantenimiento?accion=listar" target="contentFrame">
                        <i class="bi bi-tools"></i><span> REPORTES DE MANTENIMIENTO</span>
                    </a>
                </li>
                <% } %>

                <!--  Reservas de áreas comunes (Administrador y Residente) -->
                <% if ("Administrador".equalsIgnoreCase(rol)
                        || "Residente".equalsIgnoreCase(rol)) { %>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorReserva?accion=listar" target="contentFrame">
                        <i class="bi bi-calendar-week"></i><span> RESERVAS DE ÁREAS COMUNES</span>
                    </a>
                </li>
                <% } %>

                <!--  Paquetería (Administrador y Residente) -->
                <% if ("Administrador".equalsIgnoreCase(rol)
                        ) { %>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorPaqueteria?accion=listar" target="contentFrame">
                        <i class="bi bi-box-seam"></i><span> REGISTRAR PAQUETERÍA</span>
                    </a>
                </li>
                <% } %>

                <!--  Pagos (Administrador y Residente) -->
                <% if ("Administrador".equalsIgnoreCase(rol)
                        || "Residente".equalsIgnoreCase(rol)) { %>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorPago?accion=listar" target="contentFrame">
                        <i class="bi bi-cash-coin"></i><span> GESTIONAR PAGOS</span>
                    </a>
                </li>
                <% } %>

                <!--  Cerrar sesión (todos los roles) -->
                <li>
                    <a href="<%=request.getContextPath()%>/LogoutServlet">
                        <i class="bi bi-box-arrow-right"></i><span> CERRAR SESIÓN</span>
                    </a>
                </li>
            </ul>
        </div>

        <!-- Contenido -->
        <div class="content">
            <div class="user-header">
                <% if (usuario != null) { %>
                    <i class="bi bi-person-circle"></i>
                    <span>
                        Bienvenido, <%= usuario.getNombre()%> <%= usuario.getApellidos()%>
                        (<%= usuario.getNombreRol() != null ? usuario.getNombreRol() : "Sin rol"%>)
                    </span>
                <% } %>
            </div>

            <iframe name="contentFrame"></iframe>
        </div>
    </body>
</html>
