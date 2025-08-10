
<%-- 
    Document   : add
    Created on : 30-ago-2018, 19:58:16
    Author     : Joel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <title>Login</title>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center mt-5">
                <div class="col-lg-4">
                    <div class="card shadow">
                        <div class="card-header text-center bg-primary text-white">
                            <h3>Iniciar Sesión</h3>
                        </div>
                        <div class="card-body">
                            <form action="Controlador" method="post">
                                <div class="mb-3">
                                    <label for="txtUser" class="form-label">Usuario:</label>
                                    <input type="text" class="form-control" id="txtUser" name="txtUser" placeholder="Ingrese su usuario" required>
                                </div>
                                <div class="mb-3">
                                    <label for="txtPass" class="form-label">Contraseña:</label>
                                    <input type="password" class="form-control" id="txtPass" name="txtPass" placeholder="Ingrese su contraseña" required>
                                </div>
                                <div class="d-grid">
                                    <input type="submit" class="btn btn-primary" name="accion" value="Login">
                                </div>
                            </form>
                        </div>
                        <div class="card-footer text-center">
                            <small>Por favor, ingrese sus credenciales</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

