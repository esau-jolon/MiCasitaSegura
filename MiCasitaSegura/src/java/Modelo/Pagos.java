package Modelo;

import java.sql.Date;

public class Pagos {

    private int idPago;
    private int idUsuario;
    private int idTipoPago;
    private Date fechaPago;
    private double monto;
    private double mora;
    private double total;
    private String observaciones;
    private String estado; // Realizado o Cancelado

    private Integer mesPagado;   // 🔹 Nuevo campo (puede ser null)
    private Integer anioPagado;  // 🔹 Nuevo campo (puede ser null)

    private String nombreUsuario;   // opcional (para joins en consultas)
    private String nombreTipoPago;  // opcional

    public Pagos() {
    }

    // Getters y Setters
    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getMora() {
        return mora;
    }

    public void setMora(double mora) {
        this.mora = mora;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getMesPagado() {
        return mesPagado;
    }

    public void setMesPagado(Integer mesPagado) {
        this.mesPagado = mesPagado;
    }

    public Integer getAnioPagado() {
        return anioPagado;
    }

    public void setAnioPagado(Integer anioPagado) {
        this.anioPagado = anioPagado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreTipoPago() {
        return nombreTipoPago;
    }

    public void setNombreTipoPago(String nombreTipoPago) {
        this.nombreTipoPago = nombreTipoPago;
    }
}
