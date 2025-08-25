<!-- main.jsp -->
<!DOCTYPE html>
<html>
<head>
    <title>Mi Casita Segura</title>
    <!-- Bootstrap local -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="../Scripts/bootstrap-icons.min.css">

    <!-- Estilos personalizados -->
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .sidebar {
            width: 60px;
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
            width: 250px;
        }

        /* Logo/Icono arriba */
        .sidebar .logo {
            display: flex;
            align-items: center;
            justify-content: flex-start; /* que inicie desde la izquierda */
            gap: 8px;
            padding: 0 15px; /* espacio interno */
            margin-bottom: 30px;
            font-size: 1.5rem;
            font-weight: bold;
            white-space: nowrap;
        }

        .sidebar .logo i {
            font-size: 2rem; /* icono más grande */
            margin: 0 auto; /* centra el icono cuando está colapsado */
        }

        .sidebar .logo span {
            opacity: 0;
            visibility: hidden;
            transition: opacity 0.3s ease;
            font-size: 1rem;
        }

        /* Mostrar texto con hover */
        .sidebar:hover .logo i {
            margin: 0; /* quita centrado */
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
            margin: 15px 0;
        }

        .sidebar ul li a {
            color: #fff;
            text-decoration: none;
            padding: 12px 20px;
            display: flex;
            align-items: center;
            gap: 10px;
            border-radius: 8px;
            transition: background 0.3s;
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
            margin-left: 60px;
            padding: 20px;
            background: #f8f9fa;
            height: 100vh;
            transition: margin-left 0.3s ease;
        }

        .sidebar:hover ~ .content {
            margin-left: 250px;
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
            <li><a href="home.jsp" target="contentFrame"><i class="bi bi-house"></i><span> Home</span></a></li>
            <li><a href="usuarios.jsp" target="contentFrame"><i class="bi bi-people"></i><span> Usuarios</span></a></li>
            <li><a href="reportes.jsp" target="contentFrame"><i class="bi bi-bar-chart"></i><span> Reportes</span></a></li>
        </ul>
    </div>

    <!-- Contenido -->
    <div class="content">
        <iframe name="contentFrame"></iframe>
    </div>
</body>
</html>
