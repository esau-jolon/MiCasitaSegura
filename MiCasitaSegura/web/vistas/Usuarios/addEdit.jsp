<%@page import="Modelo.Usuarios"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title><%= (request.getAttribute("usuario") == null ? "Agregar Usuario" : "Editar Usuario")%></title>
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <style>
            body {
                background-color: #f8f9fa;
            }
            .card {
                margin-top: 40px;
            }
            .card-header {
                font-size: 1.3rem;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card shadow">
                        <div class="card-header text-center bg-primary text-white">
                            <%= (request.getAttribute("usuario") == null ? "Nuevo Usuario" : "Editar Usuario")%>
                        </div>
                        <div class="card-body">
                            <form action="ControladorUsuario" method="post" class="needs-validation" novalidate>
                                <%
                                    Usuarios u = (Usuarios) request.getAttribute("usuario");
                                %>

                                <!-- ID oculto -->
                                <input type="hidden" name="idUsuario"
                                       value="<%= (u != null ? u.getIdUsuario() : "")%>" />

                                <div class="mb-3">
                                    <label class="form-label">DPI</label>
                                    <input type="text" class="form-control" name="dpi"
                                           value="<%= (u != null ? u.getDpi() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Nombre</label>
                                    <input type="text" class="form-control" name="nombre"
                                           value="<%= (u != null ? u.getNombre() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Apellidos</label>
                                    <input type="text" class="form-control" name="apellidos"
                                           value="<%= (u != null ? u.getApellidos() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Correo</label>
                                    <input type="email" class="form-control" name="correo"
                                           value="<%= (u != null ? u.getCorreo() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Contraseña</label>
                                    <input type="password" class="form-control" name="contrasena"
                                           placeholder="<%= (u == null ? "Ingrese contraseña" : "Dejar en blanco si no desea cambiarla")%>">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Rol</label>
                                    <input type="number" class="form-control" name="rolId"
                                           value="<%= (u != null ? u.getRolId() : "")%>">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Número de Casa</label>
                                    <select class="form-select" name="numeroCasaId" required>
                                        <option value="">Seleccione una casa</option>
                                        <%
                                            List<Integer> casas = (List<Integer>) request.getAttribute("catalogoCasas");
                                            if (casas != null) {
                                                for (Integer casa : casas) {
                                        %>
                                        <option value="<%= casa%>" <%= (u != null && u.getNumeroCasaId() != null && u.getNumeroCasaId().equals(casa) ? "selected" : "")%>>
                                            <%= casa%>
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
                                            List<Integer> lotes = (List<Integer>) request.getAttribute("catalogoLotes");
                                            if (lotes != null) {
                                                for (Integer lote : lotes) {
                                        %>
                                        <option value="<%= lote%>" <%= (u != null && u.getLoteId() != null && u.getLoteId().equals(lote) ? "selected" : "")%>>
                                            <%= lote%>
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
                                        <option value="true" <%= (u != null && u.isEstado() ? "selected" : "")%>>Activo</option>
                                        <option value="false" <%= (u != null && !u.isEstado() ? "selected" : "")%>>Inactivo</option>
                                    </select>
                                </div>

                                <div class="text-center">
                                    <button type="submit" class="btn btn-success px-4"
                                            name="accion"
                                            value="<%= (u == null ? "add" : "edit")%>">
                                        <i class="bi bi-save"></i>
                                        <%= (u == null ? "Guardar" : "Actualizar")%>
                                    </button>
                                    <a href="ControladorUsuario?accion=listar" class="btn btn-secondary px-4">
                                        <i class="bi bi-arrow-left-circle"></i> Cancelar
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
