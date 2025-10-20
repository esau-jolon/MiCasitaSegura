<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.AreasComunes" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Nueva Reserva</title>

        <!-- Bootstrap y librerías -->
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/all.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>
        <script src="<%=request.getContextPath()%>/Scripts/bootstrap.bundle.min.js"></script>

        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

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
                max-width: 1100px; /* 🔹 Aumentado: formulario más ancho */
                border: none;
                border-radius: 32px;
                overflow: hidden;
                box-shadow: 0 25px 55px rgba(0, 0, 0, 0.25);
                animation: fadeIn 0.6s ease-in-out;
            }

            .card-header {
                background: linear-gradient(135deg, #667eea, #764ba2);
                color: #fff;
                text-align: center;
                font-weight: 700;
                font-size: 1.9rem; /* 🔹 Letras del título más grandes */
                text-transform: uppercase;
                letter-spacing: 1px;
                padding: 2rem 1.5rem;
                border-bottom: 3px solid rgba(255, 255, 255, 0.2);
            }

            .card-body {
                background: #fff;
                padding: 3.5rem 4.5rem; /* 🔹 Más aire interno */
            }

            label {
                font-weight: 600;
                color: #222;
                margin-bottom: 0.7rem;
                font-size: 1.2rem; /* 🔹 Texto más grande */
            }

            .form-control,
            .form-select,
            textarea {
                border-radius: 16px;
                border: 1px solid #bbb;
                transition: all 0.3s ease;
                padding: 1.2rem 1.5rem; /* 🔹 Campos más altos */
                font-size: 1.15rem; /* 🔹 Letra más grande */
            }

            .form-control:focus,
            .form-select:focus,
            textarea:focus {
                border-color: #667eea;
                box-shadow: 0 0 0 0.25rem rgba(102, 126, 234, 0.25);
            }

            textarea {
                resize: none;
                min-height: 140px; /* 🔹 Más espacio para observaciones */
            }

            .btn-success {
                background: linear-gradient(135deg, #22c55e, #16a34a);
                border: none;
                border-radius: 14px;
                font-weight: 700;
                font-size: 1.15rem;
                padding: 1rem 2.8rem;
            }

            .btn-success:hover {
                background: linear-gradient(135deg, #16a34a, #15803d);
                transform: translateY(-1px);
            }

            .btn-secondary {
                background: linear-gradient(135deg, #6b7280, #4b5563);
                border: none;
                border-radius: 14px;
                font-weight: 700;
                font-size: 1.15rem;
                padding: 1rem 2.8rem;
            }

            .btn-secondary:hover {
                background: linear-gradient(135deg, #4b5563, #374151);
                transform: translateY(-1px);
            }

            .text-center {
                margin-top: 3rem; /* 🔹 Más separación entre formulario y botones */
            }

            .text-center button,
            .text-center a {
                min-width: 200px; /* 🔹 Botones grandes */
                margin: 0 15px; /* 🔹 Separación entre botones */
            }

            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(20px); }
                to { opacity: 1; transform: translateY(0); }
            }

            /* 🔹 Responsive */
            @media (max-width: 768px) {
                .card {
                    max-width: 95%;
                }

                .card-body {
                    padding: 2rem;
                }

                label {
                    font-size: 1rem;
                }

                .form-control,
                .form-select {
                    font-size: 1rem;
                }

                .text-center button,
                .text-center a {
                    min-width: 150px;
                    margin: 0 8px;
                }
            }
        </style>


    </head>
    <body>

        <%
            List<AreasComunes> areas = (List<AreasComunes>) request.getAttribute("listaAreas");
            String error = (String) request.getAttribute("error");
        %>

        <div class="card">
            <div class="card-header">
                <i class="bi bi-calendar-week"></i> Registrar Nueva Reserva
            </div>

            <div class="card-body">
                <% if (error != null) {%>
                <div class="alert alert-danger text-center"><%= error%></div>
                <% } %>

                <form action="ControladorReserva" method="post" class="needs-validation" novalidate>

                    <%
                        String nombreUsuario = (String) request.getAttribute("nombreUsuario");
                    %>

                    <div class="mb-3">
                        <label class="form-label">Residente</label>
                        <input type="text" class="form-control" value="<%= nombreUsuario != null ? nombreUsuario : ""%>" readonly>
                    </div>


                    <div class="mb-3">
                        <label class="form-label">Área Común</label>
                        <select name="idArea" class="form-select" required>
                            <option value="">Seleccione un área</option>
                            <% if (areas != null) {
                                    for (AreasComunes a : areas) {%>
                            <option value="<%= a.getIdArea()%>"><%= a.getNombre()%></option>
                            <% }
                                }%>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Fecha de Reserva</label>
                        <input type="date" name="fechaReserva" class="form-control" required>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Hora Inicio</label>
                            <input type="time" name="horaInicio" class="form-control" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Hora Fin</label>
                            <input type="time" name="horaFin" class="form-control" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Observaciones</label>
                        <textarea name="observaciones" class="form-control" rows="3" placeholder="Opcional..."></textarea>
                    </div>

                    <div class="text-center mt-4">
                        <button type="submit" name="accion" value="guardar" class="btn btn-success me-2 px-4">
                            <i class="bi bi-check-circle"></i> Guardar
                        </button>
                        <a href="ControladorReserva?accion=listar" class="btn btn-secondary px-4">
                            <i class="bi bi-x-circle"></i> Cancelar
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <script>
            document.querySelector('form').addEventListener('submit', function (e) {
                if (!this.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                    Swal.fire({
                        icon: 'error',
                        title: 'Campos incompletos',
                        text: 'Por favor, complete todos los campos requeridos antes de continuar.'
                    });
                }
                this.classList.add('was-validated');
            });
        </script>

    </body>
</html>
