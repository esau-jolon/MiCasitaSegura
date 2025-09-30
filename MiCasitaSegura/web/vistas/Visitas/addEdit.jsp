<%@ page import="Modelo.Visitas, Modelo.Usuarios, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    Visitas v = (Visitas) request.getAttribute("visita");
    List<Usuarios> residentes = (List<Usuarios>) request.getAttribute("catalogoResidentes");
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario"); // ⚡ usuario logueado
%>
<html>
    <head>
        <meta charset="UTF-8">
        <title><%= (v == null ? "Registrar Visita" : "Editar Visita")%></title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
    </head>
    <body>
        <div class="container mt-4">
            <div class="card">
                <div class="card-header text-center">
                    <%= (v == null ? "Registrar Nueva Visita" : "Editar Visita")%>
                </div>
                <div class="card-body">
                    <form action="ControladorVisita" method="post">
                        <input type="hidden" name="idVisita" value="<%= (v != null ? v.getIdVisita() : "")%>"/>

                        <!-- ⚡ Usuario que registra -->
                        <div class="mb-3">
                            <label class="form-label">Registrado por</label>
                            <input type="hidden" name="idUsuarioCreador" value="<%= usuarioSesion.getIdUsuario()%>"/>
                            <input type="text" class="form-control" 
                                   value="<%= usuarioSesion.getNombre() + " " + usuarioSesion.getApellidos()%>" readonly/>
                        </div>

                        <!-- Nombre visitante -->
                        <div class="mb-3">
                            <label class="form-label">Nombre del Visitante *</label>
                            <input type="text" class="form-control" name="nombreVisitante"
                                   value="<%= (v != null ? v.getNombreVisitante() : "")%>" required>
                        </div>

                        <!-- DPI -->
                        <div class="mb-3">
                            <label class="form-label">DPI</label>
                            <input type="text" class="form-control" name="dpiVisitante"
                                   value="<%= (v != null ? v.getDpiVisitante() : "")%>">
                        </div>

                        <!-- Correo -->
                        <div class="mb-3">
                            <label class="form-label">Correo del Visitante</label>
                            <input type="email" class="form-control" name="correoVisitante"
                                   value="<%= (v != null ? v.getCorreoVisitante() : "")%>">
                        </div>

                        <!-- Residente anfitrión -->
                        <div class="mb-3">
                            <label class="form-label">Residente que recibe *</label>
                            <select class="form-select" name="idResidente" required>
                                <option value="">Seleccione un residente</option>
                                <%
                                    if (residentes != null) {
                                        for (Usuarios u : residentes) {
                                %>
                                <option value="<%= u.getIdUsuario()%>"
                                        <%= (v != null && v.getIdResidente() == u.getIdUsuario() ? "selected" : "")%>>
                                    <%= u.getNombre()%> <%= u.getApellidos()%> (Casa: <%= u.getNumeroCasaId()%>)
                                </option>
                                <% }
                            }%>
                            </select>
                        </div>

                        <!-- Tipo de visita -->
                        <div class="mb-3">
                            <label class="form-label">Tipo de Visita *</label>
                            <select class="form-select" id="tipoVisita" name="tipoVisita" required>
                                <option value="">Seleccione un tipo</option>
                                <option value="Visita" <%= (v != null && "Visita".equals(v.getTipoVisita()) ? "selected" : "")%>>Visita</option>
                                <option value="Por intentos" <%= (v != null && "Por intentos".equals(v.getTipoVisita()) ? "selected" : "")%>>Por intentos</option>
                            </select>
                        </div>

                        <!-- Fecha -->
                        <div class="mb-3" id="campoFecha" style="display:none;">
                            <label class="form-label">Fecha de Visita *</label>
                            <input type="date" class="form-control" name="fechaVisita"
                                   value="<%= (v != null && v.getFechaVisita() != null ? v.getFechaVisita().toString() : "")%>">
                        </div>

                        <!-- Intentos -->
                        <div class="mb-3" id="campoIntentos" style="display:none;">
                            <label class="form-label">Intentos Permitidos *</label>
                            <input type="number" class="form-control" name="intentosPermitidos" min="2"
                                   value="<%= (v != null && v.getIntentosPermitidos() != null ? v.getIntentosPermitidos() : "")%>">
                        </div>

                        <!-- Estado -->
                        <div class="mb-3">
                            <label class="form-label">Estado</label>
                            <select class="form-select" name="estado">
                                <option value="true" <%= (v != null && v.isEstado() ? "selected" : "")%>>Activa</option>
                                <option value="false" <%= (v != null && !v.isEstado() ? "selected" : "")%>>Inactiva</option>
                            </select>
                        </div>

                        <!-- Botones -->
                        <div class="text-center">
                            <button type="submit" class="btn btn-success px-4 me-2" name="accion" value="<%= (v == null ? "add" : "edit")%>">
                                <i class="bi bi-save"></i> <%= (v == null ? "Guardar" : "Actualizar")%>
                            </button>
                            <% if (v != null) {%>
                            <a href="ControladorVisita?accion=cancelar&id=<%= v.getIdVisita()%>" class="btn btn-danger px-4 me-2">
                                <i class="bi bi-x-circle"></i> Cancelar Visita
                            </a>
                            <a href="ControladorVisita?accion=descargarQR&id=<%= v.getIdVisita()%>" class="btn btn-info px-4">
                                <i class="bi bi-qr-code"></i> Descargar QR
                            </a>
                            <% }%>
                            <a href="ControladorVisita?accion=listar" class="btn btn-secondary px-4">
                                <i class="bi bi-arrow-left"></i> Volver
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", () => {
                const tipoVisita = document.getElementById("tipoVisita");
                const campoFecha = document.getElementById("campoFecha");
                const campoIntentos = document.getElementById("campoIntentos");

                function toggleCampos() {
                    if (tipoVisita.value === "Visita") {
                        campoFecha.style.display = "block";
                        campoIntentos.style.display = "none";
                    } else if (tipoVisita.value === "Por intentos") {
                        campoFecha.style.display = "none";
                        campoIntentos.style.display = "block";
                    } else {
                        campoFecha.style.display = "none";
                        campoIntentos.style.display = "none";
                    }
                }

                tipoVisita.addEventListener("change", toggleCampos);
                toggleCampos();
            });
        </script>
    </body>
</html>
