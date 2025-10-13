<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.Usuarios"%>
<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    if (usuarioSesion == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Comunicación Interna - Menú Principal</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <style>
            body {
                background: #f0f2f5;
                font-family: 'Segoe UI', Tahoma, sans-serif;
            }

            .menu-container {
                max-width: 800px;
                margin: 70px auto;
                background: #fff;
                border-radius: 20px;
                box-shadow: 0 8px 20px rgba(0,0,0,0.15);
                padding: 50px 30px;
                text-align: center;
            }

            h2 {
                color: #0d6efd;
                font-weight: 700;
                margin-bottom: 10px;
            }

            p {
                color: #666;
                font-size: 1.1rem;
                margin-bottom: 45px;
            }

            .menu-options {
                display: flex;
                justify-content: center;
                gap: 40px;
                flex-wrap: wrap;
            }

            .option-card {
                background: linear-gradient(135deg, #0d6efd, #0a58ca);
                color: white;
                width: 280px;
                height: 180px;
                border-radius: 18px;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                text-decoration: none;
                font-size: 1.3rem;
                font-weight: 600;
                box-shadow: 0 5px 15px rgba(0,0,0,0.15);
                transition: all 0.3s ease;
            }

            .option-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 20px rgba(0,0,0,0.25);
            }

            .option-card i {
                font-size: 2.5rem;
                margin-bottom: 10px;
            }

            @media (max-width: 768px) {
                .option-card {
                    width: 100%;
                    height: 150px;
                }
            }
        </style>
    </head>
    <body>
        <div class="menu-container">
            <h2><i class="bi bi-chat-dots"></i> Comunicación Interna</h2>
            <p>Seleccione la acción que desea realizar:</p>

            <div class="menu-options">
                <%-- Solo los residentes pueden reportar incidentes --%>
                <% if ("Residente".equalsIgnoreCase(usuarioSesion.getNombreRol())) {%>
                <a href="<%=request.getContextPath()%>/ControladorIncidente?accion=listar"
                   class="option-card"
                   style="background: linear-gradient(135deg, #dc3545, #bb2d3b);">
                    <i class="bi bi-exclamation-triangle"></i>
                    Reportar Incidente
                </a>
                <% }%>

                <a href="<%=request.getContextPath()%>/ControladorConversacion?accion=listar"
                   class="option-card">
                    <i class="bi bi-chat-left-text"></i>
                    Consulta General
                </a>
            </div>
        </div>
    </body>
</html>
