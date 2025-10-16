<%@ page import="Modelo.TipoInconveniente, Modelo.Usuarios, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reporte de Mantenimiento</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
    <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

    <style>
        body {
            background-color: #eef1f6;
            font-family: 'Segoe UI', Tahoma, sans-serif;
        }

        .card {
            margin: 60px auto;
            max-width: 750px;
            border: none;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.15);
            overflow: hidden;
        }

        .card-header {
            background: linear-gradient(45deg, #0d6efd, #0a58ca);
            color: white;
            text-align: center;
            font-size: 1.4rem;
            font-weight: bold;
            padding: 1rem;
        }

        .form-label {
            font-weight: 600;
            color: #333;
        }

        .form-control, .form-select {
            border-radius: 8px;
            padding: 0.6rem 1rem;
            font-size: 1rem;
        }

        .btn {
            border-radius: 8px;
            font-weight: 600;
        }

        .btn-primary {
            background: linear-gradient(135deg, #2563eb, #1e40af);
            border: none;
        }

        .btn-warning {
            background: linear-gradient(135deg, #f59e0b, #b45309);
            border: none;
            color: white;
        }

        .btn-secondary {
            background: linear-gradient(135deg, #6b7280, #4b5563);
            border: none;
        }

        @keyframes fadeIn {
            from {opacity: 0; transform: translateY(15px);}
            to {opacity: 1; transform: translateY(0);}
        }

        form {
            animation: fadeIn 0.6s ease-in-out;
        }
    </style>
</head>
<body>

<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    List<TipoInconveniente> tipos = (List<TipoInconveniente>) request.getAttribute("tiposInconveniente");
    java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
    String fechaActual = ahora.toString().substring(0,16); // formato yyyy-MM-ddTHH:mm
%>

<div class="container">
    <div class="card">
        <div class="card-header">
            Reporte de Mantenimiento
        </div>
        <div class="card-body">
            <form action="ControladorReporteMantenimiento" method="post" id="formReporte">
                <input type="hidden" name="accion" value="guardar"/>

                <!-- 🔹 Nombre del Residente -->
                <div class="mb-3">
                    <label class="form-label">Residente</label>
                    <input type="text" class="form-control" readonly
                           value="<%= usuarioSesion != null ? usuarioSesion.getNombre() + " " + usuarioSesion.getApellidos() : "" %>">
                </div>

                <!-- 🔹 Tipo de Inconveniente (RN1) -->
                <div class="mb-3">
                    <label class="form-label">Tipo de Inconveniente</label>
                    <select class="form-select" name="idTipoInconveniente" id="idTipoInconveniente" required>
                        <option value="">Seleccione una opción</option>
                        <% if (tipos != null) {
                               for (TipoInconveniente t : tipos) { %>
                                   <option value="<%= t.getIdTipoInconveniente() %>"><%= t.getNombre() %></option>
                        <%     }
                           } %>
                    </select>
                </div>

                <!-- 🔹 Descripción -->
                <div class="mb-3">
                    <label class="form-label">Descripción del problema</label>
                    <textarea name="descripcion" id="descripcion" class="form-control" rows="4" maxlength="500"
                              placeholder="Describa el inconveniente ocurrido..." required></textarea>
                </div>

                <!-- 🔹 Fecha y hora del incidente -->
                <div class="mb-3">
                    <label class="form-label">Fecha y hora del incidente</label>
                    <input type="datetime-local" class="form-control" id="fechaHoraIncidente" name="fechaHoraIncidente"
                           value="<%= fechaActual %>" required>
                </div>

                <!-- 🔹 Botones -->
                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-primary me-2" id="btnEnviar" disabled>
                        <i class="bi bi-send"></i> Enviar Reporte
                    </button>
                    <button type="button" class="btn btn-warning me-2" id="btnLimpiar">
                        <i class="bi bi-arrow-clockwise"></i> Limpiar
                    </button>
                    <a href="ControladorReporteMantenimiento?accion=listar" class="btn btn-secondary">
                        <i class="bi bi-x-circle"></i> Cancelar
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formReporte");
    const btnEnviar = document.getElementById("btnEnviar");
    const btnLimpiar = document.getElementById("btnLimpiar");

    const tipo = document.getElementById("idTipoInconveniente");
    const descripcion = document.getElementById("descripcion");
    const fecha = document.getElementById("fechaHoraIncidente");

    // 🧩 RN2: Solo habilitar "Enviar" si todos los campos están llenos
    function validarCampos() {
        const completo = tipo.value && descripcion.value.trim() && fecha.value;
        btnEnviar.disabled = !completo;
    }

    [tipo, descripcion, fecha].forEach(campo => {
        campo.addEventListener("input", validarCampos);
    });

    // 🔸 FA1: Limpiar formulario
    btnLimpiar.addEventListener("click", () => {
        Swal.fire({
            title: "¿Desea limpiar el formulario?",
            text: "Se borrarán todos los datos ingresados.",
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "Sí, limpiar",
            cancelButtonText: "No"
        }).then((res) => {
            if (res.isConfirmed) {
                form.reset();
                btnEnviar.disabled = true;
                Swal.fire("Formulario limpio", "Puede volver a ingresar los datos.", "success");
            }
        });
    });

    // ✅ Confirmación visual al enviar
    form.addEventListener("submit", (e) => {
        e.preventDefault();
        Swal.fire({
            title: "¿Enviar reporte?",
            text: "¿Está seguro de que desea enviar este reporte de mantenimiento?",
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "Sí, enviar",
            cancelButtonText: "Cancelar"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: "Enviando...",
                    text: "Su reporte está siendo procesado.",
                    allowOutsideClick: false,
                    didOpen: () => Swal.showLoading()
                });
                setTimeout(() => form.submit(), 800);
            }
        });
    });
});
</script>

<script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>
</body>
</html>
