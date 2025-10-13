<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Conversacion"%>
<%@page import="Modelo.Usuarios"%>
<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    List<Conversacion> conversaciones = (List<Conversacion>) request.getAttribute("conversaciones");

    if (usuarioSesion == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Comunicación Interna - Mi Casita Segura</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <style>
            body {
                background-color: #f0f2f5;
                font-family: 'Segoe UI', Tahoma, sans-serif;
            }

            .chat-list-container {
                width: 90%;
                max-width: 900px;
                margin: 40px auto;
                background: #fff;
                border-radius: 15px;
                box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
                overflow: hidden;
            }

            .chat-header {
                background: linear-gradient(45deg, #0d6efd, #0a58ca);
                color: white;
                padding: 18px 25px;
                text-align: center;
                font-size: 1.4rem;
                font-weight: 600;
            }

            .chat-header i {
                margin-right: 8px;
            }

            .chat-list {
                padding: 0;
                margin: 0;
                list-style: none;
            }

            .chat-item {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 15px 20px;
                border-bottom: 1px solid #eee;
                cursor: pointer;
                transition: background 0.25s ease, transform 0.15s ease;
            }

            .chat-item:hover {
                background-color: #e9f1ff;
                transform: scale(1.01);
            }

            .chat-info {
                display: flex;
                align-items: center;
                gap: 15px;
            }

            .chat-avatar {
                width: 48px;
                height: 48px;
                border-radius: 50%;
                background: #0d6efd;
                color: white;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.3rem;
                font-weight: bold;
            }

            .chat-name {
                font-weight: 600;
                color: #333;
                font-size: 1.1rem;
            }

            .chat-meta {
                text-align: right;
            }

            .chat-date {
                font-size: 0.8rem;
                color: #6c757d;
            }

            .no-conv {
                text-align: center;
                font-style: italic;
                color: gray;
                padding: 25px;
            }

            .btn-nueva {
                display: block;
                text-align: center;
                background: linear-gradient(45deg, #0d6efd, #0a58ca);
                color: #fff;
                font-weight: 600;
                padding: 12px;
                border-radius: 0 0 15px 15px;
                text-decoration: none;
                transition: background 0.3s ease;
            }

            .btn-nueva:hover {
                background: #0a58ca;
                color: #fff;
            }
        </style>
    </head>

    <body>

        <div class="chat-list-container">

            <div class="chat-header">
                <i class="bi bi-chat-dots"></i> Comunicación Interna
            </div>

            <% if (conversaciones != null && !conversaciones.isEmpty()) { %>
            <ul class="chat-list">
                <% for (Conversacion c : conversaciones) {
                       String nombreContacto;
                       if ("Residente".equalsIgnoreCase(usuarioSesion.getNombreRol())) {
                           nombreContacto = c.getNombreCompletoAgente(); // residente ve al guardia
                       } else {
                           nombreContacto = c.getNombreCompletoResidente(); // guardia ve al residente
                       }
                %>
                <li class="chat-item" onclick="abrirChat(<%=c.getIdConversacion()%>)">
                    <div class="chat-info">
                        <div class="chat-avatar">
                            <i class="bi bi-person-fill"></i>
                        </div>
                        <div>
                            <div class="chat-name"><%= nombreContacto %></div>
                            <div class="chat-date">
                                <i class="bi bi-clock"></i>
                                <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(c.getFechaCreacion()) %>
                            </div>
                        </div>
                    </div>
                    <div class="chat-meta">
                        <span class="badge bg-primary"><%= c.getEstado() %></span>
                    </div>
                </li>
                <% } %>
            </ul>
            <% } else { %>
            <p class="no-conv">No tienes conversaciones registradas aún.</p>
            <% } %>

            <% if ("Residente".equalsIgnoreCase(usuarioSesion.getNombreRol())) { %>
            <a href="<%=request.getContextPath()%>/ControladorConversacion?accion=crear" class="btn-nueva">
                <i class="bi bi-plus-circle"></i> Crear Nueva Conversación
            </a>
            <% } %>

        </div>

        <script>
            function abrirChat(idConversacion) {
                window.location.href = "<%=request.getContextPath()%>/ControladorMensaje?accion=listar&idConversacion=" + idConversacion;
            }
        </script>

    </body>
</html>
