<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Usuarios"%>
<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    List<Usuarios> agentes = (List<Usuarios>) request.getAttribute("agentes");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nueva Conversación - Mi Casita Segura</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
    <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, sans-serif;
        }

        .container {
            max-width: 600px;
            margin-top: 50px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            padding: 25px 30px;
        }

        h2 {
            color: #0d6efd;
            font-weight: 700;
            text-align: center;
            margin-bottom: 25px;
        }

        .form-label {
            font-weight: 600;
            color: #0a58ca;
        }

        .btn-guardar {
            background: linear-gradient(45deg, #0d6efd, #0a58ca);
            color: #fff;
            font-weight: 600;
            border-radius: 8px;
            padding: 10px 20px;
        }

        .btn-guardar:hover {
            background: #0a58ca;
        }

        .btn-cancelar {
            border: 1px solid #6c757d;
            color: #6c757d;
            font-weight: 600;
            border-radius: 8px;
            padding: 10px 20px;
        }

        .btn-cancelar:hover {
            background-color: #6c757d;
            color: white;
        }

        .select {
            border-radius: 8px;
            padding: 10px;
        }

        .alert {
            border-radius: 10px;
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2><i class="bi bi-plus-circle"></i> Nueva Conversación</h2>
        <form action="<%=request.getContextPath()%>/ControladorConversacion" method="POST">
            <input type="hidden" name="accion" value="guardar">

            <div class="mb-3">
                <label class="form-label">Selecciona al agente de seguridad:</label>
                <select name="idAgente" class="form-select select" required>
                    <option value="">-- Selecciona un agente --</option>
                    <% 
                        if (agentes != null && !agentes.isEmpty()) {
                            for (Usuarios a : agentes) {
                                if ("Guardia".equalsIgnoreCase(a.getNombreRol())) {
                    %>
                        <option value="<%=a.getIdUsuario()%>">
                            <%=a.getNombre()%> <%=a.getApellidos()%> 
                            <% if (a.getCorreo() != null) { %>(<%=a.getCorreo()%>)<% } %>
                        </option>
                    <% 
                                } 
                            }
                        } else { 
                    %>
                        <option disabled>No hay agentes registrados.</option>
                    <% } %>
                </select>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <a href="<%=request.getContextPath()%>/ControladorConversacion?accion=listar" class="btn btn-cancelar">
                    <i class="bi bi-arrow-left"></i> Cancelar
                </a>
                <button type="submit" class="btn btn-guardar">
                    <i class="bi bi-save"></i> Guardar
                </button>
            </div>

            <% if (request.getAttribute("mensajeError") != null) { %>
                <div class="alert alert-danger mt-3 text-center">
                    <i class="bi bi-exclamation-triangle"></i> 
                    <%= request.getAttribute("mensajeError") %>
                </div>
            <% } %>

        </form>
    </div>
</body>
</html>
