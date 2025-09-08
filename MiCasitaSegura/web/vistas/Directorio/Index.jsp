<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.Usuarios" %>
<%@ page import="Modelo.Casas" %>
<%@ page import="Modelo.Lotes" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Directorio Residencial</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
</head>
<body>
<div class="container mt-4">

    <h2 class="text-center mb-4">📖 Directorio Residencial</h2>

    <!-- Formulario de búsqueda -->
    <div class="card p-4 mb-4 shadow-sm">
        <form action="ControladorDirectorio" method="get" class="row g-3" id="formBusqueda">
            <input type="hidden" name="accion" value="buscar"/>

            <div class="col-md-3">
                <input type="text" name="nombre" class="form-control" placeholder="Nombre">
            </div>
            <div class="col-md-3">
                <input type="text" name="apellidos" class="form-control" placeholder="Apellidos">
            </div>
            <div class="col-md-2">
                <select name="lote" class="form-select" id="loteSelect">
                    <option value="">-- Seleccione Lote --</option>
                    <%
                        List<Lotes> lotes = (List<Lotes>) request.getAttribute("lotes");
                        if (lotes != null) {
                            for (Lotes l : lotes) {
                    %>
                        <option value="<%= l.getIdLote() %>"><%= l.getCodigoLote() %></option>
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
                        List<Casas> casas = (List<Casas>) request.getAttribute("casas");
                        if (casas != null) {
                            for (Casas c : casas) {
                    %>
                        <option value="<%= c.getIdCasa() %>"><%= c.getNumeroCasa() %></option>
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

    <!-- Mensaje -->
    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
    %>
        <div class="alert alert-warning text-center"><%= mensaje %></div>
    <%
        }
    %>

    <!-- Tabla de resultados -->
    <div class="table-responsive">
        <table class="table table-bordered table-hover mt-3 text-center">
            <thead class="table-dark">
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
                    <td><%= u.getNombre() %> <%= u.getApellidos() %></td>
                    <td><%= u.getLoteId() %></td>
                    <td><%= u.getNumeroCasaId() %></td>
                    <td><%= u.getCorreo() %></td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="4" class="text-muted">No hay registros para mostrar</td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>

<!-- Scripts al final -->

<!-- Bootstrap JS -->
<script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>

<!-- SweetAlert2 -->
<script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

<script>
const form = document.getElementById('formBusqueda');
const loteSelect = document.getElementById('loteSelect');
const casaSelect = document.getElementById('casaSelect');
const nombreInput = form.nombre;
const apellidoInput = form.apellidos;

form.addEventListener('submit', function(e) {
    // Validar al menos nombre o apellido
    if (!nombreInput.value.trim() && !apellidoInput.value.trim()) {
        e.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: '¡Ups!',
            text: 'Debe ingresar al menos Nombre o Apellido.'
        });
        return;
    }

    // Validar lote/casa combinados
    if ((loteSelect.value && !casaSelect.value) || (casaSelect.value && !loteSelect.value)) {
        e.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: '¡Atención!',
            text: 'Si selecciona un lote debe seleccionar un número de casa, y viceversa.'
        });
        return;
    }
});
</script>

</body>
</html>
