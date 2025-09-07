<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuarios" %>
<%@ page import="Modelo.Casas" %>
<%@ page import="Modelo.Lotes" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Directorio Residencial</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css" type="text/css"/>
    <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
    <style>
        body {
            background: #f5f6fa;
            font-family: Arial, sans-serif;
        }
        .page-title {
            font-weight: 700;
            margin-bottom: 1.5rem;
            text-align: center;
            color: #2f3640;
        }
        .search-card {
            background: #ffffff;
            padding: 1.5rem;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
            margin-bottom: 2rem;
        }
        .table thead th {
            background-color: #2f3640;
            color: white;
        }
        .table tbody tr:hover {
            background-color: #dcdde1;
        }
    </style>
</head>
<body>
<div class="container mt-4">

    <h2 class="page-title">📖 Directorio Residencial</h2>

    <!-- Formulario de búsqueda -->
    <div class="search-card">
        <form action="ControladorDirectorio" method="get" class="row g-3">
            <input type="hidden" name="accion" value="buscar"/>

            <div class="col-md-3">
                <input type="text" name="nombre" class="form-control" placeholder="Nombre">
            </div>
            <div class="col-md-3">
                <input type="text" name="apellidos" class="form-control" placeholder="Apellidos">
            </div>
            <div class="col-md-2">
                <select name="lote" class="form-select">
                    <option value="">-- Seleccione Lote --</option>
                    <%
                        List<Lotes> lotes = (List<Lotes>) request.getAttribute("lotes");
                        if (lotes != null) {
                            for (Lotes l : lotes) {
                    %>
                        <option value="<%= l.getIdLote() %>"><%= l.getCodigoLote() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="col-md-2">
                <select name="numeroCasa" class="form-select">
                    <option value="">-- Seleccione Casa --</option>
                    <%
                        List<Casas> casas = (List<Casas>) request.getAttribute("casas");
                        if (casas != null) {
                            for (Casas c : casas) {
                    %>
                        <option value="<%= c.getIdCasa() %>"><%= c.getNumeroCasa() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="col-md-2 d-flex gap-2">
                <button type="submit" class="btn btn-primary flex-fill">Buscar</button>
                <a href="ControladorDirectorio?accion=listar" class="btn btn-secondary flex-fill">Limpiar</a>
            </div>
        </form>
    </div>

    <!-- Mensaje si no hay resultados -->
    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
    %>
        <div class="alert alert-warning text-center"><%= mensaje %></div>
    <%
        }
    %>

    <!-- Tabla de resultados -->
    <div class="table-responsive">
        <table class="table table-bordered table-hover mt-3">
            <thead class="text-center">
                <tr>
                    <th>Nombre Completo</th>
                    <th>Lote</th>
                    <th>Número Casa</th>
                    <th>Correo</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Usuarios> usuarios = (List<Usuarios>) request.getAttribute("usuarios");
                if (usuarios != null && !usuarios.isEmpty()) {
                    for (Usuarios u : usuarios) {
            %>
                <tr>
                    <td><%= u.getNombre() %> <%= u.getApellidos() %></td>
                    <td class="text-center"><%= u.getLoteId() %></td>
                    <td class="text-center"><%= u.getNumeroCasaId() %></td>
                    <td><%= u.getCorreo() %></td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="4" class="text-center text-muted">No hay registros para mostrar</td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
