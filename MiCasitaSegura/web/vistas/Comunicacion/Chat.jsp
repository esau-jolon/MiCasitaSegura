<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Mensaje"%>
<%@page import="Modelo.Usuarios"%>
<%@page import="Modelo.Conversacion"%>

<%
    Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuario");
    List<Mensaje> mensajes = (List<Mensaje>) request.getAttribute("mensajes");
    int idConversacion = (Integer) request.getAttribute("idConversacion");
    Usuarios contacto = (Usuarios) request.getAttribute("contacto");
    int idResidente = (Integer) request.getAttribute("idResidente");
    int idAgente = (Integer) request.getAttribute("idAgente");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Chat - Comunicación Interna</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/Scripts/bootstrap-icons.min.css"/>
        <script src="<%=request.getContextPath()%>/Scripts/sweetalert2.all.min.js"></script>

        <style>
            body {
                background-color: #f0f2f5;
                font-family: 'Segoe UI', Tahoma, sans-serif;
                margin: 0;
                padding: 0;
            }

            /* 📱 Contenedor general del chat */
            .chat-container {
                width: 96%;
                max-width: 1400px;
                margin: 40px auto;
                background: #fff;
                border-radius: 25px;
                box-shadow: 0 10px 25px rgba(0,0,0,0.15);
                display: flex;
                flex-direction: column;
                height: 88vh;
                overflow: hidden;
            }

            /* 🔵 Encabezado */
            /* 🔵 Encabezado */
            .chat-header {
                background: linear-gradient(45deg, #0d6efd, #0a58ca);
                color: white;
                padding: 24px 35px;
                border-radius: 25px 25px 0 0;
                display: flex;
                align-items: center;
                justify-content: space-between;
                box-shadow: 0 3px 10px rgba(0,0,0,0.2);
            }

            .chat-header h4 {
                margin: 0;
                font-weight: 600;
                font-size: 1.7rem;
                display: flex;
                align-items: center;
                gap: 8px;
            }

            /* 🟣 Botón Volver estilizado */
            .btn-volver {
                background-color: rgba(255, 255, 255, 0.2);
                color: #fff;
                border: 2px solid rgba(255, 255, 255, 0.4);
                border-radius: 50px;
                padding: 10px 18px;
                font-size: 1rem;
                font-weight: 500;
                text-decoration: none;
                display: flex;
                align-items: center;
                gap: 8px;
                transition: all 0.3s ease;
            }

            .btn-volver i {
                font-size: 1.2rem;
            }

            .btn-volver:hover {
                background-color: #fff;
                color: #0d6efd;
                border-color: #fff;
                transform: translateY(-2px);
                box-shadow: 0 3px 8px rgba(255,255,255,0.3);
            }


            /* 🗨️ Cuerpo del chat */
            .chat-body {
                flex: 1;
                padding: 50px 70px;
                overflow-y: auto;
                background-color: #e9efff;
                display: flex;
                flex-direction: column;
            }

            /* 💬 Mensajes estilo WhatsApp / Telegram */
            .message {
                display: inline-block;
                margin: 20px 0;
                padding: 25px 32px;
                border-radius: 35px;
                max-width: 80%;
                word-wrap: break-word;
                font-size: 1.6rem; /* 🟩 Texto más grande */
                line-height: 1.7;
                position: relative;
                box-shadow: 0 4px 12px rgba(0,0,0,0.12);
                animation: fadeIn 0.3s ease;
            }

            @keyframes fadeIn {
                from {opacity: 0; transform: translateY(10px);}
                to {opacity: 1; transform: translateY(0);}
            }

            /* 🔹 Mensajes enviados (tú) */
            .sent {
                background-color: #0d6efd;
                color: white;
                align-self: flex-end;
                text-align: right;
                border-bottom-right-radius: 0;
                margin-left: auto;
            }

            /* 🔸 Mensajes recibidos (otro usuario) */
            .received {
                background-color: #f1f0f0;
                color: #212529;
                align-self: flex-start;
                text-align: left;
                border-bottom-left-radius: 0;
                margin-right: auto;
            }

            /* 💬 Cola estilo WhatsApp */
            .sent::after {
                content: "";
                position: absolute;
                right: -16px;
                bottom: 0;
                border-left: 16px solid #0d6efd;
                border-top: 16px solid transparent;
                border-bottom: 16px solid transparent;
            }

            .received::after {
                content: "";
                position: absolute;
                left: -16px;
                bottom: 0;
                border-right: 16px solid #f1f0f0;
                border-top: 16px solid transparent;
                border-bottom: 16px solid transparent;
            }

            .timestamp {
                font-size: 1.05rem;
                color: #c8c8c8;
                display: block;
                margin-top: 10px;
            }

            /* ✍️ Footer */
            .chat-footer {
                padding: 25px 40px;
                border-top: 1px solid #ddd;
                display: flex;
                gap: 25px;
                background-color: #fff;
            }

            .chat-footer input {
                flex: 1;
                border-radius: 45px;
                padding: 20px 35px;
                border: 3px solid #0d6efd;
                outline: none;
                transition: all 0.3s ease;
                font-size: 1.4rem;
            }

            .chat-footer input:focus {
                box-shadow: 0 0 12px rgba(13,110,253,0.4);
            }

            .btn-send {
                background-color: #0d6efd;
                color: white;
                border-radius: 50%;
                width: 70px;
                height: 70px;
                border: none;
                font-size: 2rem;
                display: flex;
                align-items: center;
                justify-content: center;
                transition: background 0.3s ease, transform 0.1s ease;
            }

            .btn-send:hover {
                background-color: #0a58ca;
                transform: scale(1.05);
            }

            /* Scrollbar */
            .chat-body::-webkit-scrollbar {
                width: 14px;
            }
            .chat-body::-webkit-scrollbar-thumb {
                background-color: rgba(0,0,0,0.3);
                border-radius: 12px;
            }

            /* 📱 Versión responsive (móviles) */
            @media (max-width: 768px) {
                .chat-container {
                    height: 92vh;
                    width: 98%;
                    margin: 10px auto;
                }

                .chat-header h4 {
                    font-size: 1.3rem;
                }

                .chat-body {
                    padding: 20px 15px;
                }

                .message {
                    font-size: 1.2rem;
                    padding: 18px 24px;
                }

                .chat-footer input {
                    font-size: 1.1rem;
                    padding: 14px 22px;
                }

                .btn-send {
                    width: 55px;
                    height: 55px;
                    font-size: 1.5rem;
                }
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
                   class="btn-volver">
                    <i class="bi bi-arrow-left-circle"></i> Volver
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
            const idReceptor = (userId == idResidente) ? idAgente : idResidente;

            const socket = new WebSocket("ws://" + window.location.host + "<%= request.getContextPath()%>/chatSocket?userId=" + userId);
            const chatBody = document.getElementById("chatBody");
            const txtMensaje = document.getElementById("txtMensaje");
            const btnEnviar = document.getElementById("btnEnviar");

            socket.onmessage = (event) => {
                try {
                    const data = JSON.parse(event.data);
                    if (data.text)
                        mostrarMensaje(data.from, data.text);
                } catch (e) {
                    console.warn("Mensaje no JSON:", event.data);
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

                const spanTexto = document.createElement("span");
                spanTexto.textContent = text;

                const spanHora = document.createElement("span");
                spanHora.classList.add("timestamp");
                spanHora.textContent = new Date().toLocaleTimeString();

                div.appendChild(spanTexto);
                div.appendChild(spanHora);

                chatBody.appendChild(div);
                chatBody.scrollTop = chatBody.scrollHeight;
            }

            // Auto-scroll al abrir
            window.onload = () => {
                chatBody.scrollTop = chatBody.scrollHeight;
            };
        </script>
    </body>
</html>
