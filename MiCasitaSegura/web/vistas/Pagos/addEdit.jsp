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

            // Mes sugerido desde el controlador (RN5)
            String mesSugerido = (String) request.getAttribute("mesSugerido");
        %>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header text-center">
                            <%= (pago == null ? "Nuevo Pago" : "Editar Pago")%>
                        </div>
                        <div class="card-body">
                            <form action="ControladorPago" method="post" id="formPago">
                                <input type="hidden" name="idPago" value="<%= (pago != null ? pago.getIdPago() : "")%>"/>

                                <!-- Nombre del Usuario -->
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

                                <!-- Mes a Pagar (RN5) -->
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
                                    <textarea class="form-control" name="observaciones" id="observaciones" required><%= (pago != null ? pago.getObservaciones() : "")%></textarea>
                                </div>

                                <!-- Datos de Tarjeta -->
                                <div id="datosTarjeta" style="display:none;">
                                    <div class="mb-3">
                                        <label class="form-label">Número de Tarjeta</label>
                                        <input type="text" class="form-control" name="numTarjeta" id="numTarjeta" maxlength="16" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Fecha Vencimiento</label>
                                        <input type="month" class="form-control" name="fechaVenc" id="fechaVenc" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">CVV</label>
                                        <input type="text" class="form-control" name="cvv" id="cvv" maxlength="3" required>
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
                                        <option value="Pendiente" <%= (pago != null && pago.getEstado().equals("Pendiente") ? "selected" : "")%>>Pendiente</option>
                                        <option value="Realizado" <%= (pago != null && pago.getEstado().equals("Realizado") ? "selected" : "")%>>Realizado</option>
                                        <option value="Cancelado" <%= (pago != null && pago.getEstado().equals("Cancelado") ? "selected" : "")%>>Cancelado</option>
                                    </select>
                                </div>

                                <!-- Botones -->
                                <div class="text-center">
                                    <button type="submit" class="btn btn-success" id="btnRegistrar" name="accion" value="<%= (pago == null ? "add" : "edit")%>" disabled>
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
                const datosTarjeta = document.getElementById("datosTarjeta");
                const btnRegistrar = document.getElementById("btnRegistrar");

                btnConsultar.addEventListener("click", function () {
                    const option = tipoPago.options[tipoPago.selectedIndex];
                    if (!option.value) {
                        Swal.fire("Atención", "Debe seleccionar un tipo de pago", "warning");
                        return;
                    }

                    // Asignar monto
                    monto.value = option.getAttribute("data-monto");

                    // Mostrar mes a pagar solo si es mantenimiento
                    if (option.getAttribute("data-nombre").toLowerCase().includes("mantenimiento")) {
                        mesContainer.style.display = "block";
                    } else {
                        mesContainer.style.display = "none";
                    }

                    // Calcular mora
                    let moraCalc = 0;
                    if (option.getAttribute("data-nombre").toLowerCase().includes("mantenimiento")) {
                        const fecha = new Date(fechaPago.value);
                        if (fecha.getDate() > 5) {
                            moraCalc = (fecha.getDate() - 5) * 25;
                        }
                    }
                    mora.value = moraCalc.toFixed(2);
                    total.value = (parseFloat(monto.value) + moraCalc).toFixed(2);

                    // Mostrar tarjeta
                    datosTarjeta.style.display = "block";
                    btnRegistrar.disabled = false;
                });
            });
        </script>

        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>
    </body>
</html>
