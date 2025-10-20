package Modelo;

import java.sql.Timestamp;

public class Paqueteria {

    private int idPaquete;
    private String numeroGuia;
    private int idResidente;
    private int idAgenteRegistro;
    private Timestamp fechaRecepcion;
    private Timestamp fechaEntrega;
    private boolean entregado;
    private int activo; // 1 = activo, 0 = eliminado

    // 🔹 Auditoría
    private Integer creadoPor;
    private Integer modificadoPor;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    // 🔹 Datos adicionales (para mostrar nombres)
    private String nombreResidente;
    private String apellidoResidente;
    private String nombreAgente;
    private String apellidoAgente;

    // 🔹 Constructores
    public Paqueteria() {
    }

    public Paqueteria(String numeroGuia, int idResidente, int idAgenteRegistro, int creadoPor) {
        this.numeroGuia = numeroGuia;
        this.idResidente = idResidente;
        this.idAgenteRegistro = idAgenteRegistro;
        this.creadoPor = creadoPor;
        this.entregado = false;
        this.activo = 1;
    }

    // === Getters & Setters ===
    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(int idResidente) {
        this.idResidente = idResidente;
    }

    public int getIdAgenteRegistro() {
        return idAgenteRegistro;
    }

    public void setIdAgenteRegistro(int idAgenteRegistro) {
        this.idAgenteRegistro = idAgenteRegistro;
    }

    public Timestamp getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Timestamp fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public Timestamp getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Integer getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(Integer modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String nombreResidente) {
        this.nombreResidente = nombreResidente;
    }

    public String getApellidoResidente() {
        return apellidoResidente;
    }

    public void setApellidoResidente(String apellidoResidente) {
        this.apellidoResidente = apellidoResidente;
    }

    public String getNombreAgente() {
        return nombreAgente;
    }

    public void setNombreAgente(String nombreAgente) {
        this.nombreAgente = nombreAgente;
    }

    public String getApellidoAgente() {
        return apellidoAgente;
    }

    public void setApellidoAgente(String apellidoAgente) {
        this.apellidoAgente = apellidoAgente;
    }

    // 🔹 Métodos auxiliares
    public String getNombreCompletoResidente() {
        return (nombreResidente != null ? nombreResidente : "") + " "
                + (apellidoResidente != null ? apellidoResidente : "");
    }

    public String getNombreCompletoAgente() {
        return (nombreAgente != null ? nombreAgente : "") + " "
                + (apellidoAgente != null ? apellidoAgente : "");
    }
}
