package Modelo;

import java.sql.Date;

public class Visitas {

    private int idVisita;
    private String nombreVisitante;
    private String dpiVisitante;
    private String correoVisitante;
    private int idResidente;        // FK a Usuarios (residente anfitrión)
    private int idUsuarioCreador;   // FK a Usuarios (quien registró la visita)
    private String tipoVisita;      // 'Visita' o 'Por intentos'
    private Date fechaVisita;       // aplica a tipo 'Visita'
    private Integer intentosPermitidos; // aplica a 'Por intentos'
    private boolean estado;

    public Visitas() {
    }

    public Visitas(String nombreVisitante, String dpiVisitante, String correoVisitante,
            int idResidente, int idUsuarioCreador, String tipoVisita,
            Date fechaVisita, Integer intentosPermitidos, boolean estado) {
        this.nombreVisitante = nombreVisitante;
        this.dpiVisitante = dpiVisitante;
        this.correoVisitante = correoVisitante;
        this.idResidente = idResidente;
        this.idUsuarioCreador = idUsuarioCreador;
        this.tipoVisita = tipoVisita;
        this.fechaVisita = fechaVisita;
        this.intentosPermitidos = intentosPermitidos;
        this.estado = estado;
    }

    // Getters & Setters
    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String nombreVisitante) {
        this.nombreVisitante = nombreVisitante;
    }

    public String getDpiVisitante() {
        return dpiVisitante;
    }

    public void setDpiVisitante(String dpiVisitante) {
        this.dpiVisitante = dpiVisitante;
    }

    public String getCorreoVisitante() {
        return correoVisitante;
    }

    public void setCorreoVisitante(String correoVisitante) {
        this.correoVisitante = correoVisitante;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente;
    }

    public int getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(int idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }

    public String getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(String tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public Date getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(Date fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public Integer getIntentosPermitidos() {
        return intentosPermitidos;
    }

    public void setIntentosPermitidos(Integer intentosPermitidos) {
        this.intentosPermitidos = intentosPermitidos;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
