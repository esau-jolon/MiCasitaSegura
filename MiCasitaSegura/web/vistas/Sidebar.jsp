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
            }

            .sidebar {
                width: 80px; /* Barra más estrecha inicialmente */
                height: 100vh;
                position: fixed;
                top: 0;
                left: 0;
                background: linear-gradient(180deg, #0d6efd, #0a58ca);
                color: #fff;
                padding-top: 20px;
                transition: width 0.3s ease;
                overflow: hidden;
            }

            .sidebar:hover {
                width: 300px; /* Barra más ancha cuando el mouse pasa por encima */
            }

            /* Logo/Icono arriba */
            .sidebar .logo {
                display: flex;
                align-items: center;
                justify-content: flex-start;
                gap: 8px;
                padding: 0 20px;
                margin-bottom: 40px;
                font-size: 2rem;
                font-weight: bold;
                white-space: nowrap;
            }

            .sidebar .logo i {
                font-size: 3rem; /* Icono más grande */
                margin: 0 auto;
            }

            .sidebar .logo span {
                opacity: 0;
                visibility: hidden;
                transition: opacity 0.3s ease;
                font-size: 1.4rem;
            }

            /* Mostrar texto con hover */
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
                margin: 25px 0;
            }

            .sidebar ul li a {
                color: #fff;
                text-decoration: none;
                padding: 25px 30px;
                display: flex;
                align-items: center;
                gap: 20px;
                border-radius: 15px;
                transition: background 0.3s;
                font-size: 1.3rem;
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

            /* Íconos más grandes en el estado comprimido */
            .sidebar ul li a i {
                font-size: 2.0rem; /* Icono más grande */
                transition: font-size 0.3s ease;
            }

            .sidebar:hover ul li a i {
                font-size: 2.5rem; /* Iconos aún más grandes cuando el mouse pasa por encima */
            }

            .content {
                margin-left: 80px; /* Ajuste el margen para el estado comprimido */
                padding: 30px;
                background: #f8f9fa;
                height: 100vh;
                transition: margin-left 0.3s ease;
            }

            .sidebar:hover ~ .content {
                margin-left: 300px;
            }

            iframe {
                width: 100%;
                height: calc(100vh - 40px);
                border: none;
                border-radius: 8px;
                background: #fff;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
        </style>
    </head>
    <body>
        <!-- Sidebar -->
        <div class="sidebar">
            <!-- Icono de casita segura centrado -->
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
            </ul>
        </div>

        <!-- Contenido -->
        <div class="content">
            <iframe name="contentFrame"></iframe>
        </div>
    </body>
</html>
