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
                background-color: #eef1f6;
                font-family: 'Segoe UI', Tahoma, sans-serif;
                margin: 0;
                padding: 0;
                animation: fadeIn 0.6s ease-in;
                font-size: 18px; /* ✅ Texto base grande */
            }

            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(10px); }
                to { opacity: 1; transform: translateY(0); }
            }

            /* 📦 Contenedor principal */
            .container-form {
                width: 90%;
                max-width: 950px; /* 🔽 Reducido de 1200px */
                margin: 70px auto;
                background: #fff;
                border-radius: 25px;
                box-shadow: 0 15px 35px rgba(0, 0, 0, 0.12);
                overflow: hidden;
                transition: all 0.3s ease;
            }

            .container-form:hover {
                box-shadow: 0 18px 45px rgba(0, 0, 0, 0.16);
            }

            /* 🔴 Encabezado */
            .header-form {
                background: linear-gradient(90deg, #b71c1c, #e53935);
                color: #fff;
                padding: 30px 55px; /* 🔽 menos alto */
                display: flex;
                align-items: center;
                justify-content: flex-start;
                gap: 15px;
                border-radius: 25px 25px 0 0;
            }

            .header-form i {
                font-size: 2.3rem;
            }

            .header-form h4 {
                font-weight: 700;
                margin: 0;
                font-size: 2rem;
                letter-spacing: 0.5px;
            }

            /* 🧾 Cuerpo del formulario */
            .form-body {
                padding: 55px 70px; /* 🔽 menos padding */
            }

            label {
                font-weight: 700;
                color: #1e1e1e;
                margin-bottom: 10px;
                display: block;
                font-size: 1.3rem; /* ✅ Letras grandes */
            }

            .form-select, .form-control {
                border-radius: 12px;
                padding: 14px 18px;
                border: 1px solid #bfbfbf;
                font-size: 1.2rem; /* ✅ Texto de input grande */
                transition: all 0.2s ease-in-out;
                height: auto;
            }

            .form-select:focus, .form-control:focus {
                border-color: #b71c1c;
                box-shadow: 0 0 0 0.25rem rgba(183, 28, 28, 0.25);
            }

            textarea {
                resize: none;
                min-height: 150px;
            }

            small {
                color: #6c757d;
                font-size: 1rem;
            }

            /* 🎨 Botones */
            .btn-danger {
                background: linear-gradient(45deg, #dc3545, #b71c1c);
                border: none;
                border-radius: 10px;
                font-weight: 700;
                font-size: 1.2rem;
                padding: 14px 40px;
                transition: all 0.3s ease;
            }

            .btn-danger:hover {
                background: #a51818;
                transform: translateY(-2px);
            }

            .btn-secondary {
                border-radius: 10px;
                font-weight: 700;
                font-size: 1.2rem;
                padding: 14px 40px;
            }

            .btn-secondary:hover {
                background: #5c636a;
            }

            .footer-buttons {
                text-align: center;
                margin-top: 55px;
            }

            /* 📱 Responsive */
            @media (max-width: 992px) {
                .container-form {
                    width: 95%;
                }

                .form-body {
                    padding: 35px;
                }

                .header-form {
                    flex-direction: column;
                    text-align: center;
                    padding: 25px;
                }

                .header-form h4 {
                    font-size: 1.8rem;
                }

                label {
                    font-size: 1.1rem;
                }

                .form-select, .form-control {
                    font-size: 1rem;
                    padding: 12px;
                }

                .btn-danger, .btn-secondary {
                    font-size: 1rem;
                    padding: 12px 30px;
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
