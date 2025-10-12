<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Mensaje"%>
<%@page import="Modelo.Usuarios"%>
<%@page import="Modelo.Conversacion"%>

<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    List<Mensaje> mensajes = (List<Mensaje>) request.getAttribute("mensajes");
    int idConversacion = (Integer) request.getAttribute("idConversacion");
    Usuarios contacto = (Usuarios) request.getAttribute("contacto"); // el otro usuario
    int idResidente = (Integer) request.getAttribute("idResidente");
    int idAgente = (Integer) request.getAttribute("idAgente");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chat - Comunicación Interna</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, sans-serif;
            }
            .chat-container {
                width: 85%;
                max-width: 900px;
                margin: 30px auto;
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 5px 20px rgba(0,0,0,0.1);
                display: flex;
                flex-direction: column;
                height: 80vh;
            }
            .chat-header {
                background: linear-gradient(45deg, #0d6efd, #0a58ca);
                color: white;
                padding: 15px;
                border-radius: 12px 12px 0 0;
                display: flex;
                align-items: center;
                justify-content: space-between;
            }
            .chat-header h4 {
                margin: 0;
                font-weight: 600;
            }
            .chat-body {
                flex: 1;
                padding: 15px;
                overflow-y: auto;
                background-color: #f1f5ff;
            }
            .chat-footer {
                padding: 10px 15px;
                border-top: 1px solid #ddd;
                display: flex;
                gap: 10px;
            }
            .chat-footer input {
                flex: 1;
                border-radius: 20px;
                padding: 10px 15px;
                border: 1px solid #ccc;
                outline: none;
                transition: all 0.3s ease;
            }
            .chat-footer input:focus {
                border-color: #0d6efd;
                box-shadow: 0 0 5px rgba(13,110,253,0.5);
            }
            .btn-send {
                background-color: #0d6efd;
                color: white;
                border-radius: 20px;
                padding: 10px 25px;
                border: none;
                font-weight: 600;
                transition: background 0.3s ease;
            }
            .btn-send:hover {
                background-color: #0a58ca;
            }
            .message {
                margin: 8px 0;
                display: flex;
                flex-direction: column;
                max-width: 70%;
                word-wrap: break-word;
                padding: 10px 15px;
                border-radius: 15px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }
            .sent {
                background-color: #0d6efd;
                color: white;
                align-self: flex-end;
                border-bottom-right-radius: 0;
            }
            .received {
                background-color: #e9ecef;
                align-self: flex-start;
                border-bottom-left-radius: 0;
            }
            .timestamp {
                font-size: 0.75rem;
                color: #6c757d;
                text-align: right;
            }
        </style>
    </head>
    <body>
        <div class="chat-container">
            <div class="chat-header">
                <div>
                    <h4><i class="bi bi-shield-lock"></i> Comunicación con:
                        <%= contacto != null
                                ? contacto.getNombre() + " " + contacto.getApellidos() + " (" + contacto.getNombreRol() + ")"
                                : "Usuario desconocido"%>
                    </h4>
                </div>
                <a href="<%=request.getContextPath()%>/ControladorConversacion?accion=listar"
                   class="btn btn-light btn-sm">
                    <i class="bi bi-arrow-left"></i> Volver
                </a>
            </div>

            <div class="chat-body" id="chatBody">
                <% if (mensajes != null && !mensajes.isEmpty()) { %>
                <% for (Mensaje m : mensajes) {%>
                <div class="message <%= (m.getIdEmisor() == usuarioSesion.getIdUsuario()) ? "sent" : "received"%>">
                    <span><%= m.getContenido()%></span>
                    <span class="timestamp"><%= m.getFechaEnvio()%></span>
                </div>
                <% } %>
                <% } else { %>
                <p class="text-center text-muted mt-3">No hay mensajes aún.</p>
                <% }%>
            </div>

            <div class="chat-footer">
                <input type="hidden" id="idConversacion" value="<%= idConversacion%>">
                <input type="hidden" id="idResidente" value="<%= idResidente%>">
                <input type="hidden" id="idAgente" value="<%= idAgente%>">
                <input type="text" id="txtMensaje" placeholder="Escribe un mensaje..." autocomplete="off">
                <button class="btn-send" id="btnEnviar"><i class="bi bi-send"></i></button>
            </div>
        </div>

        <script>
            const userId = '<%= usuarioSesion.getIdUsuario()%>';
            const idConversacion = <%= idConversacion%>;
            const idResidente = <%= idResidente%>;
            const idAgente = <%= idAgente%>;

            // 🔹 Determinar automáticamente quién es el receptor
            const idReceptor = (userId == idResidente) ? idAgente : idResidente;

            // 🔹 WebSocket adaptado dinámicamente al contexto
            const socket = new WebSocket("ws://" + window.location.host + "<%= request.getContextPath()%>/chatSocket?userId=" + userId);
            const chatBody = document.getElementById("chatBody");
            const txtMensaje = document.getElementById("txtMensaje");
            const btnEnviar = document.getElementById("btnEnviar");

            socket.onopen = () => console.log("✅ Conectado al WebSocket como usuario " + userId);

            socket.onmessage = (event) => {
                try {
                    const data = JSON.parse(event.data);
                    mostrarMensaje(data.from, data.text);
                } catch (e) {
                    console.log("Mensaje no JSON:", event.data);
                }
            };

            btnEnviar.addEventListener("click", enviarMensaje);
            txtMensaje.addEventListener("keypress", e => {
                if (e.key === "Enter")
                    enviarMensaje();
            });

            function enviarMensaje() {
                const texto = txtMensaje.value.trim();
                if (texto === "")
                    return;

                fetch("<%= request.getContextPath()%>/ControladorMensaje", {
                    method: "POST",
                    body: new URLSearchParams({
                        accion: "enviar",
                        idConversacion: idConversacion,
                        idReceptor: idReceptor,
                        contenido: texto
                    })
                });

                socket.send(JSON.stringify({from: userId, to: idReceptor, text: texto}));
                mostrarMensaje(userId, texto);
                txtMensaje.value = "";
            }

            function mostrarMensaje(from, text) {
                const div = document.createElement("div");
                div.classList.add("message", from == userId ? "sent" : "received");
                div.innerHTML = `<span>${text}</span><span class="timestamp">\${new Date().toLocaleTimeString()}</span>`;
                chatBody.appendChild(div);
                chatBody.scrollTop = chatBody.scrollHeight;
            }
        </script>
    </body>
</html>


