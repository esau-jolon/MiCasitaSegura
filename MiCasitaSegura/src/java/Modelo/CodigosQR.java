package Modelo;

import java.sql.Timestamp;

public class CodigosQR {
    private int idQr;
    private String codigo;
    private String tipo; // 'permanente'
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private Integer intentosDisponibles;
    private Integer idUsuario; // FK a Usuarios
    private Integer idVisita;  // FK a Visitas
    private boolean estado;

    public CodigosQR() {
    }

    public CodigosQR(String codigo, String tipo, Timestamp fechaInicio, Timestamp fechaFin,
                     Integer intentosDisponibles, Integer idUsuario, Integer idVisita, boolean estado) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.intentosDisponibles = intentosDisponibles;
        this.idUsuario = idUsuario;
        this.idVisita = idVisita;
        this.estado = estado;
    }

    // Getters y Setters ...
}

