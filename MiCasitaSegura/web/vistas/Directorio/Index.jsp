<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Usuarios"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Directorio Residencial</title>
    <!-- Bootstrap desde tu carpeta local -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css" type="text/css"/>
    <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
</head>

<body>
<div class="container mt-4">
    <h2 class="mb-4 text-center">📖 Directorio Residencial</h2>
    
    <!-- Formulario de búsqueda -->
    <form action="ControladorDirectorio" method="get" class="row g-3 shadow p-3 rounded bg-light">
        <input type="hidden" name="accion" value="buscar"/>
        
        <div class="col-md-3">
            <input type="text" name="nombre" class="form-control" placeholder="Nombre">
        </div>
        <div class="col-md-3">
            <input type="text" name="apellidos" class="form-control" placeholder="Apellidos">
        </div>
        <div class="col-md-2">
            <input type="number" name="lote" class="form-control" placeholder="Lote">
        </div>
        <div class="col-md-2">
            <input type="number" name="numeroCasa" class="form-control" placeholder="Número Casa">
        </div>
        <div class="col-md-2 d-flex gap-2">
            <button type="submit" class="btn btn-primary flex-fill">Buscar</button>
            <a href="ControladorDirectorio?accion=listar" class="btn btn-secondary flex-fill">Limpiar</a>
        </div>
    </form>

    <hr/>

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
            <thead class="table-dark text-center">
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

<script src="../js/bootstrap.bundle.min.js"></script>
</body>
</html>
