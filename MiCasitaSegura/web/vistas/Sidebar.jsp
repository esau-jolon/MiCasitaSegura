<!-- main.jsp -->
<!DOCTYPE html>
<html>
    <head>
        <title>Mi Casita Segura</title>
        <!-- Bootstrap local -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>

        <!-- Estilos personalizados -->
        <style>
            body {
                margin: 0;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f0f2f5;
            }

            /* Sidebar */
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

            .sidebar:hover {
                width: 280px; 
            }

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

            .sidebar:hover .logo i {
                margin: 0;
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
                padding: 15px 20px; /* menos ancho */
                display: flex;
                align-items: center;
                gap: 15px; /* menos espacio entre icono y texto */
                border-radius: 12px;
                transition: background 0.3s;
                font-size: 1.1rem; /* más pequeño */
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

            .sidebar ul li a i {
                font-size: 1.7rem; /* iconos un poco más pequeños */
                transition: font-size 0.3s ease;
            }

            .sidebar:hover ul li a i {
                font-size: 2rem; 
            }

            /* Contenido */
            .content {
                margin-left: 80px; 
                padding: 30px;
                transition: margin-left 0.3s ease;
            }

            .sidebar:hover ~ .content {
                margin-left: 280px;
            }

            /* Header usuario */
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

            /* iframe */
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
        <!-- Sidebar -->
        <div class="sidebar">
            <div class="logo">
                <i class="bi bi-house-fill"></i>
                <span>MI CASITA SEGURA</span>
            </div>

            <ul>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorDirectorio?accion=listar" target="contentFrame">
                        <i class="bi bi-journal-bookmark"></i><span> DIRECTORIO</span>
                    </a>
                </li>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorUsuario?accion=listar" target="contentFrame">
                        <i class="bi bi-people"></i><span> MANTENIMIENTO DE USUARIOS</span>
                    </a>
                </li>
                <li>
                    <a href="<%=request.getContextPath()%>/ControladorVisita?accion=listar" target="contentFrame">
                        <i class="bi bi-door-open"></i><span> VISITAS</span>
                    </a>
                </li>

                <li>
                    <a href="<%=request.getContextPath()%>/ControladorPago?accion=listar" target="contentFrame">
                        <i class="bi bi-cash-coin"></i><span> PAGOS</span>
                    </a>
                </li>
                <li>
                    <a href="<%=request.getContextPath()%>/LogoutServlet">
                        <i class="bi bi-box-arrow-right"></i><span> CERRAR SESIÓN</span>
                    </a>
                </li>
            </ul>
        </div>

        <!-- Contenido -->
        <div class="content">
            <!-- Header usuario fuera de la sidebar -->
            <div class="user-header">
                <%
                    Modelo.Usuarios usuario = (Modelo.Usuarios) session.getAttribute("usuario");
                    if (usuario != null) {
                %>
                <i class="bi bi-person-circle"></i>
                <span>
                    Bienvenido, <%= usuario.getNombre()%> <%= usuario.getApellidos()%> 
                    (<%= usuario.getNombreRol() != null ? usuario.getNombreRol() : "Sin rol"%>)
                </span>
                <% }%>
            </div>

            <iframe name="contentFrame"></iframe>
        </div>
    </body>
</html>
