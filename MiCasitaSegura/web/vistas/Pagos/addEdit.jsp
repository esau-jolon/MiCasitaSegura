<%@ page import="Modelo.Pagos, Modelo.TiposPago, Modelo.Usuarios, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title><%= (request.getAttribute("pago") == null ? "Registrar Pago" : "Editar Pago")%></title>

        <!-- Bootstrap -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-grid.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>

        <!-- SweetAlert2 -->
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <style>
            body { background-color: #e9ecef; }
            .card {
                margin-top: 40px;
                border: none;
                border-radius: 15px;
                box-shadow: 0px 6px 20px rgba(0, 0, 0, 0.15);
                padding: 20px;
            }
            .card-header {
                font-size: 1.6rem;
                font-weight: bold;
                background-color: #0d6efd;
                color: white;
                text-transform: uppercase;
                padding: 1rem;
                border-radius: 10px 10px 0 0;
                text-align: center;
            }
            .form-label { font-weight: 600; font-size: 1.05rem; }
            .form-control, .form-select, textarea {
                font-size: 1rem;
                padding: 0.7rem 1rem;
                border-radius: 8px;
            }
            .btn { font-size: 1.1rem; padding: 0.6rem 1.2rem; border-radius: 8px; }
            .btn-success { background: linear-gradient(135deg, #4ade80, #22c55e); border: none; }
            .btn-secondary { background: linear-gradient(135deg, #6b7280, #4b5563); border: none; }
            .btn i { margin-right: 6px; }
        </style>
    </head>
    <body>

        <%
            Pagos pago = (Pagos) request.getAttribute("pago");
            List<TiposPago> catalogoTiposPago = (List<TiposPago>) request.getAttribute("catalogoTiposPago");
            Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
            String fechaHoy = new java.sql.Date(System.currentTimeMillis()).toString();
            String mesSugerido = (String) request.getAttribute("mesSugerido");
        %>

        <div class="container mt-4">
            <div class="card shadow-lg">
                <div class="card-header text-center bg-primary text-white">
                    <%= (pago == null ? "Nuevo Pago" : "Editar Pago")%>
                </div>
                <div class="card-body">
                    <form action="ControladorPago" method="post" id="formPago">
                        <input type="hidden" name="idPago" value="<%= (pago != null ? pago.getIdPago() : "")%>"/>

                        <!-- Inputs ocultos -->
                        <input type="hidden" name="mesPagado" id="mesPagado"
                               value="<%= (pago != null && pago.getMesPagado() != null ? pago.getMesPagado() : "")%>">
                        <input type="hidden" name="anioPagado" id="anioPagado"
                               value="<%= (pago != null && pago.getAnioPagado() != null ? pago.getAnioPagado() : "")%>">

                        <!-- Input oculto para accion -->
                        <input type="hidden" id="accionForm" name="accion" value="<%= (pago == null ? "add" : "edit")%>">

                        <!-- Nombre Usuario -->
                        <div class="mb-3">
                            <label class="form-label">Nombre del Usuario</label>
                            <input type="text" class="form-control"
                                   value="<%= usuarioSesion != null ? usuarioSesion.getNombre() + " " + usuarioSesion.getApellidos() : ""%>"
                                   readonly>
                        </div>

                        <!-- Tipo de Pago -->
                        <div class="mb-3">
                            <label class="form-label">Tipo de Pago</label>
                            <select class="form-select" name="idTipoPago" id="tipoPago" required>
                                <option value="">Seleccione un tipo</option>
                                <% if (catalogoTiposPago != null) {
                                for (TiposPago t : catalogoTiposPago) {%>
                                <option value="<%= t.getIdTipoPago()%>"
                                        data-monto="<%= t.getMonto()%>"
                                        data-nombre="<%= t.getNombre()%>"
                                        <%= (pago != null && pago.getIdTipoPago() == t.getIdTipoPago() ? "selected" : "")%>>
                                    <%= t.getNombre()%> - Q <%= t.getMonto()%>
                                </option>
                                <% }
                            }%>
                            </select>
                        </div>

                        <!-- Mes a Pagar -->
                        <div class="mb-3" id="mesContainer" style="display:none;">
                            <label class="form-label">Mes a Pagar</label>
                            <input type="text" class="form-control" id="mesPagar"
                                   value="<%= mesSugerido != null ? mesSugerido : ""%>" readonly>
                        </div>

                        <!-- Botón Consultar -->
                        <div class="mb-3 text-center">
                            <button type="button" id="btnConsultar" class="btn btn-primary">
                                <i class="bi bi-search"></i> Consultar
                            </button>
                        </div>

                        <!-- Fecha de Pago -->
                        <div class="mb-3">
                            <label class="form-label">Fecha de Pago</label>
                            <input type="date" class="form-control" name="fechaPago" id="fechaPago"
                                   value="<%= (pago != null ? pago.getFechaPago() : fechaHoy)%>" readonly>
                        </div>

                        <!-- Monto -->
                        <div class="mb-3">
                            <label class="form-label">Monto</label>
                            <input type="number" step="0.01" class="form-control" id="monto" name="monto"
                                   value="<%= (pago != null ? pago.getMonto() : "")%>" readonly>
                        </div>

                        <!-- Mora -->
                        <div class="mb-3">
                            <label class="form-label">Mora</label>
                            <input type="number" step="0.01" class="form-control" id="mora" name="mora"
                                   value="<%= (pago != null ? pago.getMora() : 0)%>" readonly>
                        </div>

                        <!-- Total -->
                        <div class="mb-3">
                            <label class="form-label">Total</label>
                            <input type="number" step="0.01" class="form-control" id="total" name="total"
                                   value="<%= (pago != null ? pago.getTotal() : "")%>" readonly>
                        </div>

                        <!-- Observaciones -->
                        <div class="mb-3">
                            <label class="form-label">Observaciones</label>
                            <textarea class="form-control" name="observaciones" required><%= (pago != null ? pago.getObservaciones() : "")%></textarea>
                        </div>

                        <!-- Datos de Tarjeta -->
                        <div id="datosTarjeta">
                            <div class="mb-3">
                                <label class="form-label">Número de Tarjeta</label>
                                <input type="text" class="form-control" name="numTarjeta" id="numTarjeta" maxlength="19" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Fecha Vencimiento</label>
                                <input type="month" class="form-control" name="fechaVenc" id="fechaVenc" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">CVV</label>
                                <input type="text" class="form-control" name="cvv" id="cvv" maxlength="4" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Nombre Titular</label>
                                <input type="text" class="form-control" name="nombreTitular" id="nombreTitular" required>
                            </div>
                        </div>

                        <!-- Estado -->
                        <div class="mb-3">
                            <label class="form-label">Estado</label>
                            <select class="form-select" name="estado">
                                <option value="Pendiente" <%= (pago != null && "Pendiente".equals(pago.getEstado()) ? "selected" : "")%>>Pendiente</option>
                                <option value="Realizado" <%= (pago != null && "Realizado".equals(pago.getEstado()) ? "selected" : "")%>>Realizado</option>
                                <option value="Cancelado" <%= (pago != null && "Cancelado".equals(pago.getEstado()) ? "selected" : "")%>>Cancelado</option>
                            </select>
                        </div>

                        <!-- Botones -->
                        <div class="text-center">
                            <button type="submit" class="btn btn-success" id="btnRegistrar">
                                <i class="bi bi-save"></i> <%= (pago == null ? "Registrar Pago" : "Actualizar")%>
                            </button>
                            <a href="ControladorPago?accion=listar" class="btn btn-secondary">
                                <i class="bi bi-x-circle"></i> Cancelar
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Script -->
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const tipoPago = document.getElementById("tipoPago");
                const monto = document.getElementById("monto");
                const mora = document.getElementById("mora");
                const total = document.getElementById("total");
                const fechaPago = document.getElementById("fechaPago");
                const mesContainer = document.getElementById("mesContainer");
                const mesPagar = document.getElementById("mesPagar");
                const btnConsultar = document.getElementById("btnConsultar");
                const mesPagado = document.getElementById("mesPagado");
                const anioPagado = document.getElementById("anioPagado");
                const form = document.getElementById("formPago");

                // Acción del botón Consultar
                btnConsultar.addEventListener("click", function () {
                    const option = tipoPago.options[tipoPago.selectedIndex];
                    if (!option || !option.value) {
                        Swal.fire("Atención", "Debe seleccionar un tipo de pago", "warning");
                        return;
                    }

                    monto.value = option.getAttribute("data-monto");

                    if (option.getAttribute("data-nombre").toLowerCase().includes("mantenimiento")) {
                        mesContainer.style.display = "block";
                    } else {
                        mesContainer.style.display = "none";
                        mesPagado.value = "";
                        anioPagado.value = "";
                    }

                    let moraCalc = 0;
                    let anioPago = new Date(fechaPago.value).getFullYear();

                    if (option.getAttribute("data-nombre").toLowerCase().includes("mantenimiento")) {
                        const fechaHoy = new Date(fechaPago.value);
                        const mesSugerido = mesPagar.value;
                        const meses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

                        const idxMesSugerido = meses.indexOf(mesSugerido);
                        if (idxMesSugerido >= 0) {
                            const mesNumero = idxMesSugerido + 1;
                            mesPagado.value = mesNumero;
                            anioPagado.value = anioPago;

                            const fechaLimite = new Date(anioPago, idxMesSugerido, 5);
                            if (fechaHoy > fechaLimite) {
                                const diffMs = fechaHoy - fechaLimite;
                                const diasRetraso = Math.floor(diffMs / (1000 * 60 * 60 * 24));
                                moraCalc = diasRetraso * 25;
                            }
                        }
                    }

                    mora.value = moraCalc.toFixed(2);
                    total.value = (parseFloat(monto.value) + moraCalc).toFixed(2);
                });

                // Interceptar submit para simular cobro (acepta cualquier tarjeta)
                form.addEventListener("submit", function (e) {
                    e.preventDefault();

                    const numTarjeta = document.getElementById("numTarjeta").value.trim();
                    const cvv = document.getElementById("cvv").value.trim();
                    const titular = document.getElementById("nombreTitular").value.trim();

                    // Validaciones rápidas
                    if (numTarjeta.length < 12 || numTarjeta.length > 19 || !/^\d+$/.test(numTarjeta)) {
                        Swal.fire("Error", "Número de tarjeta inválido", "error");
                        return;
                    }
                    if (cvv.length < 3 || cvv.length > 4 || !/^\d+$/.test(cvv)) {
                        Swal.fire("Error", "CVV inválido (3 o 4 dígitos)", "error");
                        return;
                    }
                    if (!titular) {
                        Swal.fire("Error", "Debe ingresar el nombre del titular", "error");
                        return;
                    }

                    Swal.fire({
                        title: 'Confirmar pago',
                        html: `Se intentará cobrar a la tarjeta terminada en <strong>${numTarjeta.slice(-4)}</strong>`,
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonText: 'Sí, continuar',
                        cancelButtonText: 'Cancelar'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            Swal.fire({
                                title: 'Procesando pago...',
                                html: 'Por favor espere un momento',
                                allowOutsideClick: false,
                                didOpen: () => {
                                    Swal.showLoading();
                                }
                            });

                            setTimeout(() => {
                                Swal.close();
                                const authCode = Math.floor(100000 + Math.random() * 900000); // 6 dígitos

                                Swal.fire({
                                    title: "Pago exitoso",
                                    html: `El cobro se realizó correctamente.<br>Autorización: <strong>APROBADO #${authCode}</strong>`,
                                    icon: "success"
                                }).then(() => {
                                    // agregamos código de autorización
                                    let inputAuth = document.getElementById("authCode");
                                    if (!inputAuth) {
                                        inputAuth = document.createElement("input");
                                        inputAuth.type = "hidden";
                                        inputAuth.name = "authCode";
                                        inputAuth.id = "authCode";
                                        form.appendChild(inputAuth);
                                    }
                                    inputAuth.value = authCode;

                                    // forzamos envío sin validaciones HTML5
                                    form.noValidate = true;
                                    form.submit();
                                });

                            }, 2000);
                        }
                    });
                });
            });
        </script>

        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>
    </body>
</html>
