package Modelo;

import java.sql.Date;

public class Pagos {

    private int idPago;
    private int idUsuario;
    private int idTipoPago;
    private int idEstadoPago;
    private Date fechaPago;
    private double monto;
    private double mora;
    private double total;
    private String observaciones;

    private Integer mesPagado;
    private Integer anioPagado;
    private boolean activo;

    // 🧾 Campos de auditoría
    private int creadoPor;
    private Integer modificadoPor;
    private Date fechaCreacion;
    private Date fechaModificacion;

    // 🔹 Campos no persistentes (para mostrar en la vista)
    private String nombreUsuario;
    private String nombreTipoPago;
    private String nombreEstadoPago;

    public Pagos() {
        this.activo = true;
    }

    // ======= Getters y Setters =======
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

    public int getIdEstadoPago() {
        return idEstadoPago;
    }

    public void setIdEstadoPago(int idEstadoPago) {
        this.idEstadoPago = idEstadoPago;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(int creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Integer getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(Integer modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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

    public String getNombreEstadoPago() {
        return nombreEstadoPago;
    }

    public void setNombreEstadoPago(String nombreEstadoPago) {
        this.nombreEstadoPago = nombreEstadoPago;
    }

    @Override
    public String toString() {
        return "Pagos{"
                + "idPago=" + idPago
                + ", idUsuario=" + idUsuario
                + ", idTipoPago=" + idTipoPago
                + ", idEstadoPago=" + idEstadoPago
                + ", fechaPago=" + fechaPago
                + ", monto=" + monto
                + ", mora=" + mora
                + ", total=" + total
                + ", observaciones='" + observaciones + '\''
                + ", mesPagado=" + mesPagado
                + ", anioPagado=" + anioPagado
                + ", activo=" + activo
                + ", creadoPor=" + creadoPor
                + ", modificadoPor=" + modificadoPor
                + ", fechaCreacion=" + fechaCreacion
                + ", fechaModificacion=" + fechaModificacion
                + '}';
    }
}
