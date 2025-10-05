<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuarios" %>
<%@ page import="Modelo.Casas" %>
<%@ page import="Modelo.Lotes" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Directorio Residencial</title>

        <!-- Bootstrap y FontAwesome -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-grid.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>

        <!-- SweetAlert2 local -->
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <style>
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #0f0f23 0%, #1a1a3e 100%);
                color: #ffffff;
                min-height: 100vh;
            }

            body::before {
                content: '';
                position: fixed;
                top: 0; left: 0;
                width: 100%; height: 100%;
                background: 
                    radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%),
                    radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.3) 0%, transparent 50%),
                    radial-gradient(circle at 40% 40%, rgba(120, 219, 226, 0.2) 0%, transparent 50%);
                pointer-events: none;
                z-index: -1;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 2rem 1rem;
            }

            .page-header {
                text-align: center;
                margin-bottom: 3rem;
            }

            .page-title {
                font-size: 2.5rem;
                font-weight: 700;
                background: linear-gradient(135deg, #667eea, #f093fb);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }

            .main-card {
                background: rgba(255, 255, 255, 0.05);
                backdrop-filter: blur(20px);
                border: 1px solid rgba(255,255,255,0.1);
                border-radius: 24px;
                box-shadow: 0 20px 25px -5px rgba(0,0,0,0.3),0 10px 10px -5px rgba(0,0,0,0.1);
                padding: 2rem;
                overflow: hidden;
                margin-bottom: 2rem;
            }

            .form-control {
                border-radius: 12px;
                background: rgba(255,255,255,0.05);
                border: 1px solid rgba(255,255,255,0.2);
                color: #ffffff;
            }

            .form-control::placeholder {
                color: rgba(255,255,255,0.6);
            }

            /* ====== SELECTS ====== */
            .form-select {
                appearance: none;
                -webkit-appearance: none;
                -moz-appearance: none;
                background: rgba(255,255,255,0.05) !important;
                color: #fff !important;
                border: 1px solid rgba(255,255,255,0.2);
                border-radius: 12px;
                padding: 0.75rem 1.5rem;  /* Hacerlo más ancho (gordo) */
                font-size: 1.1rem;  /* Un tamaño de fuente más grande */
                font-weight: 500;
                transition: all 0.3s ease;
                background-repeat: no-repeat;
                background-position: right 0.75rem center;
                background-size: 12px 12px;
            }

            .form-select:focus {
                outline: none;
                border-color: #667eea;
                background: rgba(255,255,255,0.1);
            }

            .form-select option {
                background-color: #1a1a3e; /* fondo oscuro para el desplegable */
                color: #ffffff;           /* texto blanco */
            }

            .form-select option:checked {
                background-color: #667eea; /* opción seleccionada */
                color: #fff;
            }

            .btn-primary {
                background: linear-gradient(135deg, #667eea, #764ba2);
                border: none;
                border-radius: 12px;
                transition: all 0.3s ease;
            }

            .btn-primary:hover {
                background: linear-gradient(135deg, #4c6fe0, #5a3cb0);
            }

            .btn-secondary {
                border-radius: 12px;
            }

            .modern-table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                background: transparent;
                border-radius: 16px;
                overflow: hidden;
            }

            .modern-table thead {
                background: rgba(255,255,255,0.05);
            }

            .modern-table th, .modern-table td {
                padding: 1rem;
                text-align: center;
                border: none;
                color: #ffffff;
            }

            .modern-table tbody tr {
                background: rgba(255,255,255,0.02);
                border-bottom: 1px solid rgba(255,255,255,0.1);
                transition: all 0.3s ease;
            }

            .modern-table tbody tr:hover {
                background: rgba(255,255,255,0.08);
                transform: scale(1.01);
            }

            .empty-state {
                text-align: center;
                padding: 2rem;
                color: rgba(255,255,255,0.6);
            }

            .empty-icon {
                font-size: 3rem;
                margin-bottom: 1rem;
                opacity: 0.5;
            }

            .ripple {
                position: absolute;
                border-radius: 50%;
                transform: scale(0);
                animation: ripple-animation 600ms linear;
                background-color: rgba(255,255,255,0.3);
                pointer-events: none;
            }

            @keyframes ripple-animation {
                to { transform: scale(4); opacity: 0; }
            }
        </style>

    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <h1 class="page-title">Directorio Residencial</h1>
            </div>

            <!-- Formulario -->
            <div class="main-card">
                <form action="ControladorDirectorio" method="get" class="row g-3" id="formBusqueda">
                    <input type="hidden" name="accion" value="buscar"/>
                    <div class="col-md-3">
                        <input type="text" name="nombre" class="form-control" placeholder="Nombre"
                               value="<%= request.getParameter("nombre") != null ? request.getParameter("nombre") : ""%>">
                    </div>
                    <div class="col-md-3">
                        <input type="text" name="apellidos" class="form-control" placeholder="Apellidos"
                               value="<%= request.getParameter("apellidos") != null ? request.getParameter("apellidos") : ""%>">
                    </div>
                    <div class="col-md-2">
                        <select name="lote" class="form-select" id="loteSelect">
                            <option value="">-- Seleccione Lote --</option>
                            <%
                                String loteParam = request.getParameter("lote");
                                List<Lotes> lotes = (List<Lotes>) request.getAttribute("lotes");
                                if (lotes != null) {
                                    for (Lotes l : lotes) {
                                        String selected = (loteParam != null && loteParam.equals(String.valueOf(l.getIdLote()))) ? "selected" : "";
                            %>
                            <option value="<%=l.getIdLote()%>" <%=selected%>><%=l.getCodigoLote()%></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select name="numeroCasa" class="form-select" id="casaSelect">
                            <option value="">-- Seleccione Casa --</option>
                            <%
                                String casaParam = request.getParameter("numeroCasa");
                                List<Casas> casas = (List<Casas>) request.getAttribute("casas");
                                if (casas != null) {
                                    for (Casas c : casas) {
                                        String selected = (casaParam != null && casaParam.equals(String.valueOf(c.getIdCasa()))) ? "selected" : "";
                            %>
                            <option value="<%=c.getIdCasa()%>" <%=selected%>><%=c.getNumeroCasa()%></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                    <div class="col-md-2 d-flex gap-2">
                        <button type="submit" class="btn btn-primary flex-fill">Buscar</button>
                        <a href="ControladorDirectorio?accion=listar" class="btn btn-secondary flex-fill">Limpiar</a>
                    </div>
                </form>
            </div>

            <!-- Tabla de resultados -->
            <div class="main-card table-container">
                <table class="modern-table">
                    <thead>
                        <tr>
                            <th>Nombre Completo</th>
                            <th>Lote</th>
                            <th>Número Casa</th>
                            <th>Correo</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Usuarios> usuarios = (List<Usuarios>) request.getAttribute("usuarios");
                            if (usuarios != null && !usuarios.isEmpty()) {
                                for (Usuarios u : usuarios) {
                        %>
                        <tr>
                            <td><%= u.getNombre()%> <%= u.getApellidos()%></td>
                            <td><%= u.getLoteId()%></td>
                            <td><%= u.getNumeroCasaId()%></td>
                            <td><%= u.getCorreo()%></td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="4" class="empty-state">
                                <div class="empty-icon"><i class="fas fa-home"></i></div>
                                <div class="empty-title">No hay registros</div>
                                <div class="empty-description">Realiza una búsqueda o agrega nuevos residentes</div>
                            </td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <script>
            const form = document.getElementById('formBusqueda');
            const loteSelect = document.getElementById('loteSelect');
            const casaSelect = document.getElementById('casaSelect');
            const nombreInput = form.nombre;
            const apellidoInput = form.apellidos;

            // 🔹 Validaciones previas al enviar
            form.addEventListener('submit', function (e) {
                if (!nombreInput.value.trim() && !apellidoInput.value.trim()) {
                    e.preventDefault();
                    Swal.fire({
                        icon: 'warning',
                        title: '¡Ups!',
                        text: 'Debe ingresar al menos Nombre o Apellido.'
                    });
                    return;
                }

                if ((loteSelect.value && !casaSelect.value) || (!loteSelect.value && casaSelect.value)) {
                    e.preventDefault();
                    Swal.fire({
                        icon: 'warning',
                        title: '¡Atención!',
                        text: 'Si selecciona un lote debe seleccionar un número de casa, y viceversa.'
                    });
                    return;
                }
            });

            // 🔹 Mostrar alerta si no se encontró ningún usuario
            <% if (request.getAttribute("noResultados") != null && (boolean) request.getAttribute("noResultados")) { %>
            Swal.fire({
                icon: 'info',
                title: 'Sin resultados',
                text: 'No se encontró ningún usuario con los datos ingresados.',
                confirmButtonColor: '#667eea'
            });
            <% }%>
        </script>
    </body>
</html>
