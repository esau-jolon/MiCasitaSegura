<%@ page import="Modelo.Usuarios, Modelo.Casas, Modelo.Lotes, Modelo.Roles, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta charset="UTF-8">

        <title><%= (request.getAttribute("usuario") == null ? "Agregar Usuario" : "Editar Usuario")%></title>

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
                            <%= (u == null ? "Nuevo Usuario" : "Editar Usuario")%>
                        </div>
                        <div class="card-body">
                            <form action="ControladorUsuario" method="post" class="needs-validation" novalidate>
                                <input type="hidden" name="idUsuario" value="<%= (u != null ? u.getIdUsuario() : "")%>"/>

                                <div class="mb-3">
                                    <label class="form-label">DPI</label>
                                    <input type="text" class="form-control" name="dpi" value="<%= (u != null ? u.getDpi() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Nombre</label>
                                    <input type="text" class="form-control" name="nombre" value="<%= (u != null ? u.getNombre() : "")%>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Apellidos</label>
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
            (function () {
                try {
                    console.log("Iniciando validaciones JavaScript...");

                    const form = document.querySelector("form[action='ControladorUsuario']");
                    if (!form) {
                        console.error("Formulario no encontrado: asegúrate que 'action' es ControladorUsuario");
                        return;
                    }
                    console.log("Formulario encontrado");

                    const rolSelect = form.querySelector("select[name='rolId']");
                    const casaSelect = form.querySelector("select[name='numeroCasaId']");
                    const loteSelect = form.querySelector("select[name='loteId']");
                    const dpiInput = form.querySelector("input[name='dpi']");
                    const correoInput = form.querySelector("input[name='correo']");
                    const passInput = form.querySelector("input[name='contrasena']");
                    const idUsuarioInput = form.querySelector("input[name='idUsuario']");

                    // Contenedor para errores (lo creamos si no existe)
                    let erroresBox = document.getElementById("errores-validacion");
                    if (!erroresBox) {
                        erroresBox = document.createElement("div");
                        erroresBox.id = "errores-validacion";
                        erroresBox.style.marginBottom = "1rem";
                        form.prepend(erroresBox);
                    }

                    function showErrors(list) {
                        erroresBox.innerHTML = "";
                        if (list.length === 0) {
                            return;
                        }
                        const alertDiv = document.createElement("div");
                        alertDiv.className = "alert alert-danger";

                        const ul = document.createElement("ul");
                        ul.style.margin = "0";
                        ul.style.paddingLeft = "20px";

                        list.forEach(msg => {
                            const li = document.createElement("li");
                            li.textContent = msg;
                            ul.appendChild(li);
                        });

                        alertDiv.appendChild(ul);
                        erroresBox.appendChild(alertDiv);

                        // Scroll suave al área de errores
                        erroresBox.scrollIntoView({behavior: "smooth", block: "center"});
                    }

                    function validarRol() {
                        if (!rolSelect || !casaSelect || !loteSelect)
                            return;

                        // Detecta por texto del rol. Ajusta si tus textos son diferentes.
                        const rolTexto = (rolSelect.options[rolSelect.selectedIndex] || {}).text || "";
                        const esGuardia = rolTexto.toLowerCase().includes("guardia");

                        console.log("Rol seleccionado:", rolTexto, "Es guardia:", esGuardia);

                        if (esGuardia) {
                            casaSelect.value = "";
                            loteSelect.value = "";
                            casaSelect.disabled = true;
                            loteSelect.disabled = true;
                            casaSelect.removeAttribute("required");
                            loteSelect.removeAttribute("required");
                        } else {
                            casaSelect.disabled = false;
                            loteSelect.disabled = false;
                            casaSelect.setAttribute("required", "true");
                            loteSelect.setAttribute("required", "true");
                        }
                    }

                    // Aplicar validación de rol al cambiar
                    if (rolSelect) {
                        rolSelect.addEventListener("change", validarRol);
                        validarRol(); // aplicar al cargar
                    }

                    form.addEventListener("submit", function (e) {
                        console.log("Formulario enviado, iniciando validaciones...");
                        erroresBox.innerHTML = "";
                        const errores = [];

                        // Determinar si es add o edit: si idUsuario vacío => add
                        const isAdd = !idUsuarioInput || idUsuarioInput.value.trim() === "";
                        console.log("Modo:", isAdd ? "Agregar" : "Editar");

                        const dpi = dpiInput ? dpiInput.value.trim() : "";
                        const correo = correoInput ? correoInput.value.trim() : "";
                        const pass = passInput ? passInput.value.trim() : "";

                        // Validación DPI - solo verificar que no esté vacío y que sean números
                        if (!dpi) {
                            errores.push("El DPI es obligatorio.");
                        } else if (!/^\d+$/.test(dpi)) {
                            errores.push("El DPI debe contener solo números.");
                        } else if (dpi.length < 8 || dpi.length > 15) {
                            errores.push("El DPI debe tener entre 8 y 15 dígitos.");
                        }

                        // Validación correo - básica
                        if (!correo) {
                            errores.push("El correo es obligatorio.");
                        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(correo)) {
                            errores.push("El correo no tiene un formato válido.");
                        }

                        // Validación contraseña - más flexible
                        if (isAdd) {
                            // En modo agregar, la contraseña es obligatoria
                            if (!pass) {
                                errores.push("La contraseña es obligatoria.");
                            } else if (pass.length < 6) {
                                errores.push("La contraseña debe tener al menos 6 caracteres.");
                            }
                        } else {
                            // En modo editar, si escriben contraseña debe ser válida
                            if (pass && pass.length < 6) {
                                errores.push("Si cambia la contraseña, debe tener al menos 6 caracteres.");
                            }
                        }

                        // Validación rol
                        if (!rolSelect || !rolSelect.value) {
                            errores.push("Seleccione un rol válido.");
                        }

                        // Si rol no es guardia, verificar casa y lote
                        if (rolSelect && rolSelect.value) {
                            const rolTexto = (rolSelect.options[rolSelect.selectedIndex] || {}).text || "";
                            if (!rolTexto.toLowerCase().includes("guardia")) {
                                if (!casaSelect || !casaSelect.value) {
                                    errores.push("Seleccione un número de casa válido.");
                                }
                                if (!loteSelect || !loteSelect.value) {
                                    errores.push("Seleccione un lote válido.");
                                }
                            }
                        }

                        console.log("Errores encontrados:", errores.length);

                        if (errores.length > 0) {
                            e.preventDefault();
                            showErrors(errores);
                            console.log("Formulario bloqueado por errores de validación");
                            return false;
                        }

                        console.log("Validaciones pasadas, enviando formulario...");
                        return true;
                    });

                    console.log("Validaciones JavaScript configuradas correctamente");

                } catch (err) {
                    console.error("Error en validaciones JavaScript:", err);
                    // En caso de error en las validaciones, permitir el envío del formulario
                    // para que las validaciones del servidor tomen el control
                }
            })();
        </script>



        <!-- Bootstrap JS (opcional si usas validaciones de formulario o componentes interactivos) -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Bootstrap Icons (para los íconos si los usas) -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

    </body>
</html>