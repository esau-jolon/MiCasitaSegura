#include <Servo.h>

// Definir dos servos: entrada y salida
Servo servoEntrada;
Servo servoSalida;

// Definir pines de LEDs
const int ledEntrada = 2;   // LED verde para entrada
const int ledSalida  = 3;   // LED verde para salida

void setup() {
    Serial.begin(9600);          // Comunicación con la computadora
    servoEntrada.attach(9);      // Servo de entrada en pin 9
    servoSalida.attach(10);      // Servo de salida en pin 10
    
    // Configurar pines de LED como salidas
    pinMode(ledEntrada, OUTPUT);
    pinMode(ledSalida, OUTPUT);
    
    // Inicializar servos en posición cerrada
    servoEntrada.write(90);
    servoSalida.write(90);
    
    // Apagar LEDs al inicio
    digitalWrite(ledEntrada, LOW);
    digitalWrite(ledSalida, LOW);
    
    // Mensaje de inicio
    Serial.println("🚀 Arduino listo - Servos y LEDs inicializados");
    Serial.println("📋 Comandos: 'entrada' o 'salida'");
}

void loop() {
    if (Serial.available() > 0) {
        String cmd = Serial.readStringUntil('\n');  // Leer hasta salto de línea
        cmd.trim();                                 // Limpiar espacios
        
        Serial.println("📥 Comando recibido: '" + cmd + "' (longitud: " + cmd.length() + ")");
        
        if (cmd.indexOf("entrada") >= 0 || cmd == "entrada") {
            abrirEntrada();
        } else if (cmd.indexOf("salida") >= 0 || cmd == "salida") {
            abrirSalida();
        } else if (cmd.length() > 0) {
            Serial.println("⚠ Comando inválido: '" + cmd + "'");
            Serial.println("💡 Comandos válidos: 'entrada' o 'salida'");
        }
    }
}

void abrirEntrada() {
    Serial.println("🚪 ➡ ABRIENDO ENTRADA...");
    
    digitalWrite(ledEntrada, HIGH);  // 🔵 Encender LED entrada
    servoEntrada.write(0);           // Abrir la garita
    
    Serial.println("🔄 Servo entrada movido a 0°");
    delay(5000);                     // Mantener abierta 5 segundos
    
    servoEntrada.write(90);          // Cerrar
    digitalWrite(ledEntrada, LOW);   // 🔵 Apagar LED entrada
    
    Serial.println("🔄 Servo entrada movido a 90°");
    Serial.println("✅ ENTRADA COMPLETADA");
}

void abrirSalida() {
    Serial.println("🚪 ⬅ ABRIENDO SALIDA...");
    
    digitalWrite(ledSalida, HIGH);   // 🔵 Encender LED salida
    servoSalida.write(0);            // Abrir la garita
    
    Serial.println("🔄 Servo salida movido a 0°");
    delay(5000);                     // Mantener abierta 5 segundos
    
    servoSalida.write(90);           // Cerrar
    digitalWrite(ledSalida, LOW);    // 🔵 Apagar LED salida
    
    Serial.println("🔄 Servo salida movido a 90°");
    Serial.println("✅ SALIDA COMPLETADA");
}