<%@ page import="Modelo.Usuarios, Modelo.Casas, Modelo.Lotes, Modelo.Roles, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta charset="UTF-8">

        <title><%= (request.getAttribute("usuario") == null ? "Agregar Usuario" : "Editar Usuario")%></title>

        <!-- Bootstrap desde CDN (puedes quitarlo si usas local) -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-grid.min.css"/>

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>

        <!-- FontAwesome local -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>

        <!-- SweetAlert2 local -->
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>


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

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) {%>
        <div class="alert alert-danger text-center">
            <%= error%>
        </div>
        <% }%>


        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header text-center">
                            <%= (u == null ? "Nuevo Usuario" : "Editar Usuario")%>
                        </div>
                        <div class="card-body">
                            <form action="ControladorUsuario" method="post" class="needs-validation" novalidate>
                                <input type="hidden" name="idUsuario" value="<%= (u != null ? u.getIdUsuario() : "")%>"/>

                                <div class="mb-3">
                                    <label class="form-label">DPI Usuario</label>
                                    <input type="text" class="form-control" name="dpi" value="<%= (u != null ? u.getDpi() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Nombre del Usuario</label>
                                    <input type="text" class="form-control" name="nombre" value="<%= (u != null ? u.getNombre() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Apellidos del Usuario</label>
                                    <input type="text" class="form-control" name="apellidos" value="<%= (u != null ? u.getApellidos() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Correo</label>
                                    <input type="email" class="form-control" name="correo" value="<%= (u != null ? u.getCorreo() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Contraseña</label>
                                    <input type="password" class="form-control" name="contrasena"
                                           placeholder="<%= (u == null ? "Ingrese contraseña" : "Dejar en blanco si no desea cambiarla")%>">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Rol</label>
                                    <select class="form-select" name="rolId" required>
                                        <option value="">Seleccione un rol</option>
                                        <%
                                            if (roles != null) {
                                                for (Roles rol : roles) {
                                        %>
                                        <option value="<%= rol.getIdRol()%>"
                                                <%= (u != null && u.getRolId() == rol.getIdRol() ? "selected" : "")%>>
                                            <%= rol.getNombreRol()%>
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
                                        <option value="<%= casa.getIdCasa()%>"
                                                <%= (u != null && u.getNumeroCasaId() != null && u.getNumeroCasaId() == casa.getIdCasa() ? "selected" : "")%>>
                                            Casa <%= casa.getNumeroCasa()%>
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
                                        <option value="<%= lote.getIdLote()%>"
                                                <%= (u != null && u.getLoteId() != null && u.getLoteId() == lote.getIdLote() ? "selected" : "")%>>
                                            <%= lote.getCodigoLote()%>
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
                                    <button type="submit" class="btn btn-success px-4 me-2" name="accion" value="<%= (u == null ? "add" : "edit")%>">
                                        <i class="bi bi-save"></i> <%= (u == null ? "Guardar" : "Actualizar")%>
                                    </button>

                                    <% if (u == null) { %>
                                    <!-- 🔹 Solo mostrar en modo "Nuevo" -->
                                    <button type="button" id="btnLimpiar" class="btn btn-warning px-4 me-2">
                                        <i class="bi bi-brush"></i> Limpiar campos
                                    </button>
                                    <% }%>
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

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const form = document.querySelector("form.needs-validation");
                const dpiInput = form.querySelector("input[name='dpi']");
                const correoInput = form.querySelector("input[name='correo']");
                const contrasenaInput = form.querySelector("input[name='contrasena']");
                const rolSelect = form.querySelector("select[name='rolId']");
                const casaSelect = form.querySelector("select[name='numeroCasaId']");
                const loteSelect = form.querySelector("select[name='loteId']");

                // Regla DPI (exactamente 13 dígitos numéricos)
                function validarDpi() {
                    const regex = /^\d{13}$/;
                    if (!regex.test(dpiInput.value.trim())) {
                        dpiInput.setCustomValidity("El DPI debe tener 13 dígitos.");
                    } else {
                        dpiInput.setCustomValidity("");
                    }
                }

                // Regla correo
                function validarCorreo() {
                    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    correoInput.setCustomValidity(regex.test(correoInput.value.trim()) ? "" : "Formato de correo inválido.");
                }

                // Regla contraseña (mínimo 8 caracteres, al menos 1 mayúscula, 1 número y 1 especial)
                function validarContrasena() {
                    if (contrasenaInput.value.trim().length === 0) {
                        // Si estamos editando y el usuario deja en blanco, no forzamos regla
                        if ("<%= (u == null)%>" === "false") {
                            contrasenaInput.setCustomValidity("");
                            return;
                        }
                    }
                    const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;
                    contrasenaInput.setCustomValidity(regex.test(contrasenaInput.value) ? "" :
                            "Mínimo 8 caracteres, 1 mayúscula, 1 número y 1 caracter especial.");
                }

                // Regla: si Rol = Guardia, desactivar Casa y Lote
                function manejarRol() {
                    const seleccionado = rolSelect.options[rolSelect.selectedIndex].text.toLowerCase();
                    const esGuardia = seleccionado.includes("guardia");

                    casaSelect.disabled = esGuardia;
                    loteSelect.disabled = esGuardia;

                    // Si está deshabilitado, limpiamos validaciones
                    if (esGuardia) {
                        casaSelect.value = "";
                        loteSelect.value = "";
                        casaSelect.removeAttribute("required");
                        loteSelect.removeAttribute("required");
                    } else {
                        casaSelect.setAttribute("required", "required");
                        loteSelect.setAttribute("required", "required");
                    }
                }

                // Validar antes de enviar
                form.addEventListener("submit", function (event) {
                    validarDpi();
                    validarCorreo();
                    validarContrasena();
                    manejarRol();

                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }

                    form.classList.add("was-validated");
                });

                // Listeners en tiempo real
                dpiInput.addEventListener("input", validarDpi);
                correoInput.addEventListener("input", validarCorreo);
                contrasenaInput.addEventListener("input", validarContrasena);
                rolSelect.addEventListener("change", manejarRol);

                // Inicializar estado de casa/lote según rol cargado
                manejarRol();
            });
        </script>


        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const form = document.querySelector("form.needs-validation");

                // 🔹 Limpiar campos (solo si el botón existe)
                const limpiarBtn = document.getElementById("btnLimpiar");
                if (limpiarBtn) {
                    limpiarBtn.addEventListener("click", function () {
                        Swal.fire({
                            title: '¿Deseas limpiar todos los campos?',
                            icon: 'question',
                            showCancelButton: true,
                            confirmButtonText: 'Sí, limpiar',
                            cancelButtonText: 'Cancelar',
                            confirmButtonColor: '#ffc107'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                form.reset();
                            }
                        });
                    });
                }
            });
        </script>

        <!-- Bootstrap JS local -->
        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

    </body>
</html>