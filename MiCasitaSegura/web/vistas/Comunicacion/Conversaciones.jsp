<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Conversacion"%>
<%@page import="Modelo.Usuarios"%>
<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    List<Conversacion> conversaciones = (List<Conversacion>) request.getAttribute("conversaciones");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Comunicación Interna - Mi Casita Segura</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, sans-serif;
            }

            .container {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 5px 20px rgba(0,0,0,0.1);
                padding: 25px;
                margin-top: 25px;
            }

            h2 {
                color: #0d6efd;
                font-weight: 700;
                text-align: center;
            }

            .btn-nueva {
                background: linear-gradient(45deg, #0d6efd, #0a58ca);
                color: #fff;
                font-weight: 600;
                border-radius: 8px;
                transition: background 0.3s ease;
            }

            .btn-nueva:hover {
                background: #0a58ca;
            }

            table {
                width: 100%;
            }

            th {
                background-color: #0d6efd;
                color: white;
                text-align: center;
            }

            td {
                text-align: center;
                vertical-align: middle;
            }

            .acciones i {
                font-size: 1.4rem;
                cursor: pointer;
                transition: transform 0.2s ease;
            }

            .acciones i:hover {
                transform: scale(1.2);
            }

            .no-conv {
                text-align: center;
                font-style: italic;
                color: gray;
                margin-top: 15px;
            }
        </style>
    </head>
    
    
    <body>
        
        
        <div class="container">
            <h2><i class="bi bi-chat-dots"></i> Comunicación Interna</h2>
            <p class="text-muted text-center">
                Canal directo entre <strong>residentes</strong> y <strong>guardias de seguridad</strong>.
            </p>

            <div class="d-flex justify-content-between mb-3">
                <div></div>
                <% if (usuarioSesion != null && "Residente".equalsIgnoreCase(usuarioSesion.getNombreRol())) {%>
                <a href="<%=request.getContextPath()%>/ControladorConversacion?accion=crear" 
                   class="btn btn-nueva">
                    <i class="bi bi-plus-circle"></i> Nueva conversación
                </a>
                <% } %>
            </div>

            <% if (conversaciones != null && !conversaciones.isEmpty()) { %>
            <table class="table table-bordered table-hover align-middle">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Residente</th>
                        <th>Agente</th>
                        <th>Fecha</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Conversacion c : conversaciones) {%>
                    <tr>
                        <td><%= c.getIdConversacion()%></td>
                        <td><%= c.getIdResidente()%></td>
                        <td><%= c.getIdAgente()%></td>
                        <td><%= c.getFechaCreacion()%></td>
                        <td><span class="badge bg-primary"><%= c.getEstado()%></span></td>
                        <td class="acciones">
                            <a href="<%=request.getContextPath()%>/ControladorMensaje?accion=listar&idConversacion=<%=c.getIdConversacion()%>" 
                               class="btn btn-outline-primary btn-sm">
                                <i class="bi bi-chat-left-text"></i> Abrir Chat
                            </a>

                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <% } else { %>
            <p class="no-conv">No tienes conversaciones registradas aún.</p>
            <% }%>
        </div>
    </body>
</html>
