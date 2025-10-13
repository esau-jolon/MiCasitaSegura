<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.Usuarios"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.TipoIncidente"%>
<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    if (usuarioSesion == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    List<TipoIncidente> tipos = (List<TipoIncidente>) request.getAttribute("tiposIncidente");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Reportar Incidente</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <style>
            body {
                background-color: #f0f2f5;
                font-family: 'Segoe UI', Tahoma, sans-serif;
                margin: 0;
                padding: 0;
                animation: fadeIn 0.7s ease-in;
            }

            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(15px); }
                to { opacity: 1; transform: translateY(0); }
            }

            .container-form {
                width: 95%;
                max-width: 850px;
                margin: 60px auto;
                background: #fff;
                border-radius: 20px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.12);
                overflow: hidden;
            }

            .header-form {
                background: linear-gradient(45deg, #dc3545, #bb2d3b);
                color: #fff;
                padding: 25px 40px;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .header-form h4 {
                font-weight: 700;
                margin: 0;
                font-size: 1.5rem;
            }

            .form-body {
                padding: 40px 50px;
            }

            label {
                font-weight: 600;
                color: #333;
            }

            .form-select, .form-control {
                border-radius: 10px;
                padding: 10px 14px;
                border: 1px solid #ccc;
                transition: all 0.2s ease-in-out;
            }

            .form-select:focus, .form-control:focus {
                border-color: #dc3545;
                box-shadow: 0 0 0 0.2rem rgba(220,53,69,0.25);
            }

            textarea {
                resize: none;
            }

            small {
                color: #6c757d;
            }

            .btn-danger {
                background: linear-gradient(45deg, #dc3545, #bb2d3b);
                border: none;
                border-radius: 8px;
                transition: all 0.3s ease;
                font-weight: 600;
            }

            .btn-danger:hover {
                background: #c82333;
                transform: translateY(-2px);
            }

            .btn-secondary {
                border-radius: 8px;
                font-weight: 600;
            }

            .btn-secondary:hover {
                background: #5c636a;
            }

            .footer-buttons {
                text-align: center;
                margin-top: 40px;
            }

            @media (max-width: 768px) {
                .form-body {
                    padding: 25px;
                }
            }
        </style>
    </head>

    <body>
        <div class="container-form">
            <div class="header-form">
                <i class="bi bi-exclamation-triangle-fill fs-4"></i>
                <h4>Reportar Incidente</h4>
            </div>

            <div class="form-body">
                <form method="POST" action="<%=request.getContextPath()%>/ControladorIncidente">
                    <input type="hidden" name="accion" value="guardar">

                    <div class="mb-4">
                        <label class="form-label">Tipo de incidente</label>
                        <select class="form-select" name="idTipoIncidente" required>
                            <option value="">Seleccione...</option>
                            <% for (TipoIncidente t : tipos) {%>
                            <option value="<%=t.getIdTipoIncidente()%>"><%=t.getNombre()%></option>
                            <% }%>
                        </select>
                    </div>

                    <div class="mb-4">
                        <label class="form-label">Fecha y hora del incidente</label>
                        <input type="datetime-local" class="form-control" name="fechaHoraIncidente" required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label">Descripción</label>
                        <textarea name="descripcion" class="form-control" maxlength="200" rows="4" required></textarea>
                        <small>Máximo 200 caracteres.</small>
                    </div>

                    <div class="footer-buttons">
                        <button type="submit" class="btn btn-danger px-4 me-2">
                            <i class="bi bi-save"></i> Guardar
                        </button>
                        <a href="<%=request.getContextPath()%>/ControladorIncidente?accion=listar"
                           class="btn btn-secondary px-4">
                            <i class="bi bi-arrow-left"></i> Regresar
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
