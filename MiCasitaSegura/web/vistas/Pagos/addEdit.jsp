<%@ page import="Modelo.Pagos, Modelo.TiposPago, java.util.List" %>
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
                margin-top: 50px;
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
            .form-label { font-weight: 600; font-size: 1.1rem; }
            .form-control, .form-select, textarea {
                font-size: 1.05rem;
                padding: 0.8rem 1rem;
                border-radius: 8px;
            }
            .btn { font-size: 1.1rem; padding: 0.75rem 1.5rem; border-radius: 8px; }
            .btn-success {
                background: linear-gradient(135deg, #4ade80, #22c55e);
                border: none;
            }
            .btn-secondary {
                background: linear-gradient(135deg, #6b7280, #4b5563);
                border: none;
            }
            .btn i { margin-right: 6px; }
            .mb-3 { margin-bottom: 1.5rem !important; }
        </style>
    </head>
    <body>

        <%
            Pagos pago = (Pagos) request.getAttribute("pago");
            List<TiposPago> catalogoTiposPago = (List<TiposPago>) request.getAttribute("catalogoTiposPago");
            String fechaHoy = new java.sql.Date(System.currentTimeMillis()).toString();
        %>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header text-center">
                            <%= (pago == null ? "Nuevo Pago" : "Editar Pago")%>
                        </div>
                        <div class="card-body">
                            <form action="ControladorPago" method="post">
                                <input type="hidden" name="idPago" value="<%= (pago != null ? pago.getIdPago() : "")%>"/>

                                <!-- Tipo de Pago -->
                                <div class="mb-3">
                                    <label class="form-label">Tipo de Pago</label>
                                    <select class="form-select" name="idTipoPago" id="tipoPago" required>
                                        <option value="">Seleccione un tipo</option>
                                        <%
                                            if (catalogoTiposPago != null) {
                                                for (TiposPago t : catalogoTiposPago) {
                                        %>
                                        <option value="<%= t.getIdTipoPago()%>"
                                                data-monto="<%= t.getMonto()%>"
                                                <%= (pago != null && pago.getIdTipoPago() == t.getIdTipoPago() ? "selected" : "")%>>
                                            <%= t.getNombre()%> - Q <%= t.getMonto()%>
                                        </option>
                                        <% }
                                    }%>
                                    </select>
                                </div>

                                <!-- Fecha de Pago (auto con la fecha actual) -->
                                <div class="mb-3">
                                    <label class="form-label">Fecha de Pago</label>
                                    <input type="date" class="form-control" name="fechaPago" id="fechaPago"
                                           value="<%= (pago != null ? pago.getFechaPago() : fechaHoy)%>" readonly>
                                </div>

                                <!-- Monto -->
                                <div class="mb-3">
                                    <label class="form-label">Monto</label>
                                    <input type="number" step="0.01" class="form-control" id="monto" name="monto"
                                           value="<%= (pago != null ? pago.getMonto() : "")%>" required readonly>
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
                                    <textarea class="form-control" name="observaciones"><%= (pago != null ? pago.getObservaciones() : "")%></textarea>
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
                                    <button type="submit" class="btn btn-success px-4 me-2" name="accion" value="<%= (pago == null ? "add" : "edit")%>">
                                        <i class="bi bi-save"></i> <%= (pago == null ? "Guardar" : "Actualizar")%>
                                    </button>
                                    <a href="ControladorPago?accion=listar" class="btn btn-secondary px-4">
                                        <i class="bi bi-x-circle"></i> Cancelar
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Script: Calcular monto, mora y total -->
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const tipoPagoSelect = document.getElementById("tipoPago");
                const montoInput = document.getElementById("monto");
                const moraInput = document.getElementById("mora");
                const totalInput = document.getElementById("total");
                const fechaPagoInput = document.getElementById("fechaPago");

                function calcularTotal() {
                    const monto = parseFloat(montoInput.value) || 0;
                    const mora = parseFloat(moraInput.value) || 0;
                    totalInput.value = (monto + mora).toFixed(2);
                }

                tipoPagoSelect.addEventListener("change", function () {
                    const option = tipoPagoSelect.options[tipoPagoSelect.selectedIndex];
                    const monto = option.getAttribute("data-monto");
                    montoInput.value = monto || 0;
                    calcularTotal();
                });

                // Calcular mora si la fecha es después del día 5
                function calcularMora() {
                    const option = tipoPagoSelect.options[tipoPagoSelect.selectedIndex];
                    const nombreTipo = option ? option.text.toLowerCase() : "";
                    let mora = 0;

                    // ✅ Solo aplica mora para "Mantenimiento"
                    if (nombreTipo.includes("mantenimiento")) {
                        const fecha = new Date(fechaPagoInput.value);
                        if (fecha.getDate() > 5) {
                            mora = (fecha.getDate() - 5) * 25;
                        }
                    }

                    moraInput.value = mora.toFixed(2);
                    calcularTotal();
                }

                calcularMora();
                calcularTotal();
            });
        </script>

        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>
    </body>
</html>
