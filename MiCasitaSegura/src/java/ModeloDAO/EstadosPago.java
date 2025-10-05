package Modelo;

public class EstadosPago {

    private int idEstadoPago;
    private String descripcion;

    public EstadosPago() {
    }

    public EstadosPago(int idEstadoPago, String descripcion) {
        this.idEstadoPago = idEstadoPago;
        this.descripcion = descripcion;
    }

    public int getIdEstadoPago() {
        return idEstadoPago;
    }

    public void setIdEstadoPago(int idEstadoPago) {
        this.idEstadoPago = idEstadoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "EstadosPago{" +
                "idEstadoPago=" + idEstadoPago +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
