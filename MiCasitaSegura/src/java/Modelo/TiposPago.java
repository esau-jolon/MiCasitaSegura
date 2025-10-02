package Modelo;

public class TiposPago {
    private int idTipoPago;
    private String nombre;
    private double monto;

    public TiposPago() {
    }

    public TiposPago(int idTipoPago, String nombre, double monto) {
        this.idTipoPago = idTipoPago;
        this.nombre = nombre;
        this.monto = monto;
    }

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "TiposPago{" +
                "idTipoPago=" + idTipoPago +
                ", nombre='" + nombre + '\'' +
                ", monto=" + monto +
                '}';
    }
}
