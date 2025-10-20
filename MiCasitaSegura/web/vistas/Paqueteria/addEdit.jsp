<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuarios" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Paquetería</title>

        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>
        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

        <style>
            body {
                background: linear-gradient(135deg, #eef2f3, #cfd9df);
                font-family: 'Inter', sans-serif;
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 60px;
            }
            .card {
                width: 100%;
                max-width: 850px;
                border: none;
                border-radius: 32px;
                overflow: hidden;
                box-shadow: 0 25px 55px rgba(0, 0, 0, 0.25);
                animation: fadeIn 0.6s ease-in-out;
            }
            .card-header {
                background: linear-gradient(135deg, #38bdf8, #6366f1);
                color: #fff;
                text-align: center;
                font-weight: 700;
                font-size: 1.8rem;
                padding: 1.8rem;
                border-bottom: 3px solid rgba(255, 255, 255, 0.2);
            }
            .card-body {
                background: #fff;
                padding: 3rem;
            }
            label {
                font-weight: 600;
                color: #222;
                margin-bottom: 0.7rem;
            }
            .form-control, .form-select {
                border-radius: 14px;
                border: 1px solid #bbb;
                transition: all 0.3s ease;
                font-size: 1.1rem;
                padding: 0.8rem 1.2rem;
                width: 100%;
            }
            .form-control:focus, .form-select:focus {
                border-color: #38bdf8;
                box-shadow: 0 0 0 0.25rem rgba(56, 189, 248, 0.25);
            }
            .btn-success {
                background: linear-gradient(135deg, #22c55e, #15803d);
                border: none;
                border-radius: 14px;
                font-weight: 700;
                padding: 0.8rem 2.5rem;
            }
            .btn-secondary {
                background: linear-gradient(135deg, #6b7280, #4b5563);
                border: none;
                border-radius: 14px;
                font-weight: 700;
                padding: 0.8rem 2.5rem;
            }
            .text-center {
                margin-top: 2rem;
            }
            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(20px); }
                to { opacity: 1; transform: translateY(0); }
            }
        </style>
    </head>
    <body>

        <%
            List<Usuarios> residentes = (List<Usuarios>) request.getAttribute("catalogoResidentes");
            String errorMensaje = (String) request.getAttribute("errorMensaje");
        %>

        <div class="card">
            <div class="card-header">
                <i class="bi bi-box-seam"></i> Registrar Paquete
            </div>

            <div class="card-body">
                <% if (errorMensaje != null) {%>
                <div class="alert alert-danger text-center"><%= errorMensaje%></div>
                <% } %>

                <form action="ControladorPaqueteria" method="post" class="needs-validation" novalidate>

                    <!-- Número de guía -->
                    <div class="mb-3">
                        <label class="form-label">Número de Guía</label>
                        <input type="text" name="numeroGuia" class="form-control" required placeholder="Ejemplo: GT12345">
                    </div>

                    <!-- Residente destinatario -->
                    <div class="mb-3">
                        <label class="form-label">Residente Destinatario</label>
                        <select name="idResidente" id="idResidente" class="form-select" required>
                            <option value="">Seleccione un residente...</option>
                            <% if (residentes != null) {
                            for (Usuarios r : residentes) {%>
                            <option value="<%= r.getIdUsuario()%>" 
                                    data-casa="<%= (r.getNumeroCasa() != null ? r.getNumeroCasa() : "")%>"
                                    data-lote="<%= (r.getCodigoLote() != null ? r.getCodigoLote() : "")%>"
                                    data-correo="<%= (r.getCorreo() != null ? r.getCorreo() : "")%>">
                                <%= r.getNombre()%> <%= r.getApellidos()%>
                                <% if (r.getNumeroCasa() != null) {%>
                                - Casa <%= r.getNumeroCasa()%>
                                <% } %>
                            </option>
                            <%  }
                        }%>
                        </select>
                    </div>

                    <!-- Campos autocompletados solo visuales -->
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Número de Casa</label>
                            <input type="text" id="numeroCasa" class="form-control" readonly>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Código de Lote</label>
                            <input type="text" id="codigoLote" class="form-control" readonly>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Correo del Residente</label>
                            <input type="text" id="correoResidente" class="form-control" readonly>
                        </div>
                    </div>

                    <div class="text-center">
                        <button type="submit" name="accion" value="add" class="btn btn-success me-3">
                            <i class="bi bi-check-circle"></i> Guardar
                        </button>
                        <a href="ControladorPaqueteria?accion=listar" class="btn btn-secondary">
                            <i class="bi bi-x-circle"></i> Cancelar
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <script>
            // ✅ Autocompletar campos visuales al seleccionar residente
            const selectResidente = document.getElementById('idResidente');
            const campoCasa = document.getElementById('numeroCasa');
            const campoLote = document.getElementById('codigoLote');
            const campoCorreo = document.getElementById('correoResidente');

            selectResidente.addEventListener('change', function () {
                const opcion = this.options[this.selectedIndex];
                campoCasa.value = opcion.getAttribute('data-casa') || '';
                campoLote.value = opcion.getAttribute('data-lote') || '';
                campoCorreo.value = opcion.getAttribute('data-correo') || '';
            });

            // Validación de formulario con alerta
            document.querySelector('form').addEventListener('submit', function (e) {
                if (!this.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                    Swal.fire({
                        icon: 'error',
                        title: 'Campos incompletos',
                        text: 'Por favor, complete todos los campos requeridos.'
                    });
                }
                this.classList.add('was-validated');
            });
        </script>

    </body>
</html>
