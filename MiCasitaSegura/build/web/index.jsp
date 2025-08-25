<!-- main.jsp -->
<!DOCTYPE html>
<html>
<head>
    <title>Mi Aplicación</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <div class="sidebar">
        <ul>
            <li><a href="home.jsp" target="contentFrame">Home</a></li>
            <li><a href="usuarios.jsp" target="contentFrame">Usuarios</a></li>
            <li><a href="reportes.jsp" target="contentFrame">Reportes</a></li>
        </ul>
    </div>

    <div class="content">
        <iframe name="contentFrame" style="width:100%; height:100vh; border:none;"></iframe>
    </div>
</body>
</html>
