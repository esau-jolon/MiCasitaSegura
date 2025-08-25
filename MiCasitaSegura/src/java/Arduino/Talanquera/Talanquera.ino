#include <Servo.h>

Servo myservo;

void setup() {
  Serial.begin(9600);  // Comunicación con la computadora
  myservo.attach(9);   // Servo en el pin 9
  myservo.write(0);    // Posición inicial
}

void loop() {
  if (Serial.available() > 0) {
    String cmd = Serial.readStringUntil('\n');  // Leer hasta salto de línea
    cmd.trim(); // Eliminar espacios extra

    if (cmd == "true") {
      // Abrir la talanquera por 5 segundos
      myservo.write(90);  // Mover el servo a 90 grados
      delay(5000);        // Esperar 5 segundos
      myservo.write(0);   // Volver a la posición inicial
    } else if (cmd == "false") {
      // No hacer nada (o añadir más lógica aquí si es necesario)
      myservo.write(0);  // Asegurarse de que el servo esté cerrado
    }
  }
}
