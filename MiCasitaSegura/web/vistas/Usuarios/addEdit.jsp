<%@ page import="Modelo.Usuarios, Modelo.Casas, Modelo.Lotes, Modelo.Roles, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= (request.getAttribute("usuario") == null ? "Agregar Usuario" : "Editar Usuario") %></title>

    <!-- Bootstrap desde CDN (puedes quitarlo si usas local) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #e9ecef;
        }

        .card {
            margin-top: 50px;
            border: none;
            border-radius: 10px;
            box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1);
        }

        .card-header {
            font-size: 1.4rem;
            font-weight: bold;
            background-color: #0d6efd;
            color: white;
            text-transform: uppercase;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
        }

        .form-label {
            font-weight: 500;
        }

        .btn i {
            margin-right: 5px;
        }
    </style>
</head>
<body>

<%
    Usuarios u = (Usuarios) request.getAttribute("usuario");
    List<Casas> casas = (List<Casas>) request.getAttribute("catalogoCasas");
    List<Lotes> lotes = (List<Lotes>) request.getAttribute("catalogoLotes");
    List<Roles> roles = (List<Roles>) request.getAttribute("catalogoRoles");
%>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header text-center">
                    <%= (u == null ? "Nuevo Usuario" : "Editar Usuario") %>
                </div>
                <div class="card-body">
                    <form action="ControladorUsuario" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="idUsuario" value="<%= (u != null ? u.getIdUsuario() : "") %>"/>

                        <div class="mb-3">
                            <label class="form-label">DPI</label>
                            <input type="text" class="form-control" name="dpi" value="<%= (u != null ? u.getDpi() : "") %>" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Nombre</label>
                            <input type="text" class="form-control" name="nombre" value="<%= (u != null ? u.getNombre() : "") %>" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Apellidos</label>
                            <input type="text" class="form-control" name="apellidos" value="<%= (u != null ? u.getApellidos() : "") %>" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Correo</label>
                            <input type="email" class="form-control" name="correo" value="<%= (u != null ? u.getCorreo() : "") %>" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Contraseña</label>
                            <input type="password" class="form-control" name="contrasena"
                                   placeholder="<%= (u == null ? "Ingrese contraseña" : "Dejar en blanco si no desea cambiarla") %>">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Rol</label>
                            <select class="form-select" name="rolId" required>
                                <option value="">Seleccione un rol</option>
                                <%
                                    if (roles != null) {
                                        for (Roles rol : roles) {
                                %>
                                <option value="<%= rol.getIdRol() %>"
                                    <%= (u != null && u.getRolId() == rol.getIdRol() ? "selected" : "") %>>
                                    <%= rol.getNombreRol() %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Número de Casa</label>
                            <select class="form-select" name="numeroCasaId" required>
                                <option value="">Seleccione una casa</option>
                                <%
                                    if (casas != null) {
                                        for (Casas casa : casas) {
                                %>
                                <option value="<%= casa.getIdCasa() %>"
                                    <%= (u != null && u.getNumeroCasaId() != null && u.getNumeroCasaId() == casa.getIdCasa() ? "selected" : "") %>>
                                    Casa <%= casa.getNumeroCasa() %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Lote</label>
                            <select class="form-select" name="loteId" required>
                                <option value="">Seleccione un lote</option>
                                <%
                                    if (lotes != null) {
                                        for (Lotes lote : lotes) {
                                %>
                                <option value="<%= lote.getIdLote() %>"
                                    <%= (u != null && u.getLoteId() != null && u.getLoteId() == lote.getIdLote() ? "selected" : "") %>>
                                    <%= lote.getCodigoLote() %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Estado</label>
                            <select class="form-select" name="estado">
                                <option value="true" <%= (u != null && u.isEstado() ? "selected" : "") %>>Activo</option>
                                <option value="false" <%= (u != null && !u.isEstado() ? "selected" : "") %>>Inactivo</option>
                            </select>
                        </div>

                        <div class="text-center">
                            <button type="submit" class="btn btn-success px-4 me-2" name="accion" value="<%= (u == null ? "add" : "edit") %>">
                                <i class="bi bi-save"></i> <%= (u == null ? "Guardar" : "Actualizar") %>
                            </button>
                            <a href="ControladorUsuario?accion=listar" class="btn btn-secondary px-4">
                                <i class="bi bi-x-circle"></i> Cancelar
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS (opcional si usas validaciones de formulario o componentes interactivos) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Bootstrap Icons (para los íconos si los usas) -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

</body>
</html>
