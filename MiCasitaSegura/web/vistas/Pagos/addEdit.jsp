<%@ page import="Modelo.Pagos, Modelo.TiposPago, Modelo.Usuarios, java.util.List" %>
<%@ page import="Modelo.EstadosPago" %>
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

            /* 🔹 Ocultar los campos de pago inicialmente */
            #camposPago {
                display: none;
                animation: fadeIn 0.4s ease-in-out;
            }
            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(-10px); }
                to { opacity: 1; transform: translateY(0); }
            }
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
                        <input type="hidden" name="mesPagado" id="mesPagado"
                               value="<%= (pago != null && pago.getMesPagado() != null ? pago.getMesPagado() : "")%>">
                        <input type="hidden" name="anioPagado" id="anioPagado"
                               value="<%= (pago != null && pago.getAnioPagado() != null ? pago.getAnioPagado() : "")%>">
                        <input type="hidden" id="accionForm" name="accion" value="<%= (pago == null ? "add" : "edit")%>">

                        <!-- Usuario -->
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

                        <!-- 🔹 Campos ocultos hasta presionar “Consultar” -->
                        <div id="camposPago">
                            <!-- Fecha -->
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
                                    <input type="text" class="form-control" name="fechaVenc" id="fechaVenc"
                                           maxlength="7" placeholder="MM/YYYY" required>
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
                                <select class="form-select" name="idEstadoPago" id="idEstadoPago" required>
                                    <option value="">Seleccione un estado</option>
                                    <%
                                        List<EstadosPago> catalogoEstadosPago = (List<EstadosPago>) request.getAttribute("catalogoEstadosPago");
                                        if (catalogoEstadosPago != null) {
                                            for (EstadosPago e : catalogoEstadosPago) {
                                    %>
                                    <option value="<%= e.getIdEstadoPago()%>"
                                            <%= (pago != null && pago.getIdEstadoPago() == e.getIdEstadoPago() ? "selected" : "")%>>
                                        <%= e.getDescripcion()%>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>

                            <!-- Botones -->
                            <div class="text-center">
                                <button type="submit" class="btn btn-success" id="btnRegistrar">
                                    <i class="bi bi-save"></i> <%= (pago == null ? "Registrar Pago" : "Actualizar")%>
                                </button>

                                <button type="button" class="btn btn-warning" id="btnLimpiar">
                                    <i class="bi bi-arrow-counterclockwise"></i> Limpiar
                                </button>

                                <a href="ControladorPago?accion=listar" class="btn btn-secondary">
                                    <i class="bi bi-x-circle"></i> Cancelar
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

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
                const camposPago = document.getElementById("camposPago");
                const numTarjeta = document.getElementById("numTarjeta");
                const fechaVenc = document.getElementById("fechaVenc");
                const cvv = document.getElementById("cvv");
                const btnCancelar = document.querySelector(".btn-secondary");
                const btnLimpiar = document.getElementById("btnLimpiar");

                // === Solo números en tarjeta ===
                if (numTarjeta) {
                    numTarjeta.addEventListener("input", function () {
                        this.value = this.value.replace(/\D/g, "");
                    });
                }

                // === Solo números en CVV ===
                if (cvv) {
                    cvv.addEventListener("input", function () {
                        this.value = this.value.replace(/\D/g, "");
                    });
                }

                // === Máscara para fecha de vencimiento (MM/YYYY) ===
                if (fechaVenc) {
                    fechaVenc.addEventListener("input", function () {
                        let valor = this.value.replace(/\D/g, "");
                        if (valor.length > 2)
                            valor = valor.substring(0, 2) + "/" + valor.substring(2, 6);
                        this.value = valor.substring(0, 7);
                    });

                    fechaVenc.addEventListener("blur", function () {
                        const valor = this.value.trim();
                        if (!/^\d{2}\/\d{4}$/.test(valor)) {
                            Swal.fire("Formato inválido", "Ingrese la fecha como MM/YYYY.", "warning");
                            this.value = "";
                            return;
                        }

                        const [mesStr, anioStr] = valor.split("/");
                        const mes = parseInt(mesStr, 10);
                        const anio = parseInt(anioStr, 10);
                        if (mes < 1 || mes > 12) {
                            Swal.fire("Mes inválido", "El mes debe estar entre 01 y 12.", "error");
                            this.value = "";
                            return;
                        }

                        const hoy = new Date();
                        const fechaIngresada = new Date(anio, mes - 1);
                        const fechaActual = new Date(hoy.getFullYear(), hoy.getMonth());
                        if (fechaIngresada < fechaActual) {
                            Swal.fire("Tarjeta vencida", "No puedes ingresar una tarjeta vencida.", "error");
                            this.value = "";
                        }
                    });
                }

                // === CONSULTAR ===
                btnConsultar.addEventListener("click", function () {
                    const option = tipoPago.options[tipoPago.selectedIndex];
                    if (!option || !option.value) {
                        Swal.fire("Atención", "Debe seleccionar un tipo de pago", "warning");
                        return;
                    }

                    monto.value = option.getAttribute("data-monto") || "0.00";
                    const nombre = option.getAttribute("data-nombre")?.toLowerCase() || "";
                    if (nombre.includes("mantenimiento")) {
                        mesContainer.style.display = "block";
                    } else {
                        mesContainer.style.display = "none";
                        mesPagado.value = "";
                        anioPagado.value = "";
                    }

                    let moraCalc = 0;
                    const fechaHoy = new Date(fechaPago.value);
                    const meses = [
                        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
                    ];
                    const idxMes = meses.indexOf(mesPagar.value);
                    if (idxMes >= 0) {
                        const anioPago = fechaHoy.getFullYear();
                        mesPagado.value = idxMes + 1;
                        anioPagado.value = anioPago;
                        const fechaLimite = new Date(anioPago, idxMes, 5);
                        if (fechaHoy > fechaLimite) {
                            const diffMs = fechaHoy - fechaLimite;
                            const diasRetraso = Math.floor(diffMs / (1000 * 60 * 60 * 24));
                            moraCalc = diasRetraso * 25;
                        }
                    }
                    mora.value = moraCalc.toFixed(2);
                    total.value = (parseFloat(monto.value || 0) + moraCalc).toFixed(2);

                    // Mostrar campos después de consultar
                    camposPago.style.display = "block";
                    camposPago.scrollIntoView({behavior: "smooth"});
                });

                // === Confirmación visual de pago ===
                form.addEventListener("submit", function (e) {
                    e.preventDefault();
                    const last4 = numTarjeta.value.slice(-4);
                    Swal.fire({
                        title: "Confirmar pago",
                        html: last4 ? `Se cobrará a la tarjeta terminada en <b>${last4}</b>. ¿Desea continuar?` : "¿Desea continuar?",
                        icon: "question",
                        showCancelButton: true,
                        confirmButtonText: "Sí, continuar",
                        cancelButtonText: "Cancelar"
                    }).then((res) => {
                        if (res.isConfirmed) {
                            Swal.fire({
                                title: "Procesando...",
                                html: "Espere por favor",
                                allowOutsideClick: false,
                                didOpen: () => Swal.showLoading()
                            });
                            setTimeout(() => {
                                Swal.close();
                                form.submit();
                            }, 1200);
                        }
                    });
                });

                // === Confirmación al cancelar ===
                if (btnCancelar) {
                    btnCancelar.addEventListener("click", function (e) {
                        e.preventDefault();
                        Swal.fire({
                            title: "¿Desea cancelar el proceso?",
                            text: "Se perderán los datos ingresados.",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonText: "Sí, cancelar",
                            cancelButtonText: "No, continuar aquí"
                        }).then((res) => {
                            if (res.isConfirmed) {
                                window.location.href = "ControladorPago?accion=listar";
                            }
                        });
                    });
                }

                // === LIMPIAR formulario ===
                if (btnLimpiar) {
                    btnLimpiar.addEventListener("click", function () {
                        Swal.fire({
                            title: "¿Desea limpiar el formulario?",
                            text: "Se borrarán todos los campos ingresados.",
                            icon: "question",
                            showCancelButton: true,
                            confirmButtonText: "Sí, limpiar",
                            cancelButtonText: "No"
                        }).then((res) => {
                            if (res.isConfirmed) {
                                form.reset();
                                monto.value = "";
                                mora.value = "0.00";
                                total.value = "";
                                mesContainer.style.display = "none";
                                camposPago.style.display = "none";
                                Swal.fire("Formulario reiniciado", "Puede comenzar de nuevo.", "success");
                            }
                        });
                    });
                }
            });
        </script>


        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>
    </body>
</html>
