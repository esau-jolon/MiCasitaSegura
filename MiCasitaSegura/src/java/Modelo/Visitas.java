package Modelo;

import java.sql.Date;

public class Visitas {
    private int idVisita;
    private String nombreVisitante;
    private String dpiVisitante;
    private String correoVisitante;
    private int idResidente; // FK a Usuarios
    private String tipoVisita; // 'Visita' o 'Por intentos'
    private Date fechaVisita;  // solo aplica a tipo 'Visita'
    private Integer intentosPermitidos; // solo aplica a 'Por intentos'
    private boolean estado;

    public Visitas() {
    }

    public Visitas(String nombreVisitante, String dpiVisitante, String correoVisitante,
                   int idResidente, String tipoVisita, Date fechaVisita, Integer intentosPermitidos, boolean estado) {
        this.nombreVisitante = nombreVisitante;
        this.dpiVisitante = dpiVisitante;
        this.correoVisitante = correoVisitante;
        this.idResidente = idResidente;
        this.tipoVisita = tipoVisita;
        this.fechaVisita = fechaVisita;
        this.intentosPermitidos = intentosPermitidos;
        this.estado = estado;
    }

    // Getters y Setters ...
}
