<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login - Mi Casita Segura</title>

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
            body {
                background: linear-gradient(135deg, #0d6efd, #0a58ca);
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .login-card {
                background: #fff;
                border-radius: 15px;
                box-shadow: 0px 4px 10px rgba(0,0,0,0.2);
                padding: 40px;
                width: 400px;
                text-align: center;
            }
            .login-card h2 {
                margin-bottom: 20px;
                font-weight: bold;
                color: #0d6efd;
            }
            .form-control {
                border-radius: 10px;
                padding-left: 40px;
            }
            .input-icon {
                position: relative;
            }
            .input-icon i {
                position: absolute;
                top: 50%;
                left: 10px;
                transform: translateY(-50%);
                color: #0d6efd;
            }
            .btn-login {
                border-radius: 10px;
                width: 100%;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="login-card">
            <h2><i class="bi bi-house-lock"></i> Mi Casita Segura</h2>
            <form action="LoginServlet" method="post">
                <div class="mb-3 input-icon">
                    <i class="fa-solid fa-envelope"></i>
                    <input type="email" name="correo" class="form-control" placeholder="Correo electrónico" required>
                </div>
                <div class="mb-3 input-icon">
                    <i class="fa-solid fa-lock"></i>
                    <input type="password" name="contrasena" class="form-control" placeholder="Contraseña" required>
                </div>
                <button type="submit" class="btn btn-primary btn-login">Iniciar Sesión</button>
            </form>
        </div>

        <%-- Mostrar error con SweetAlert si existe --%>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <script>
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: '<%= error %>'
            });
        </script>
        <% } %>
    </body>
</html>
