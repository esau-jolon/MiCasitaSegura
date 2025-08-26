package Modelo;

public class Vistas {
    private int idVista;
    private String nombreVista;
    private String rutaVista;
    private Integer moduloId; // FK a Modulos

    public Vistas() {
    }

    public Vistas(String nombreVista, String rutaVista, Integer moduloId) {
        this.nombreVista = nombreVista;
        this.rutaVista = rutaVista;
        this.moduloId = moduloId;
    }

    // Getters y Setters ...

    public int getIdVista() {
        return idVista;
    }

    public void setIdVista(int idVista) {
        this.idVista = idVista;
    }

    public String getNombreVista() {
        return nombreVista;
    }

    public void setNombreVista(String nombreVista) {
        this.nombreVista = nombreVista;
    }

    public String getRutaVista() {
        return rutaVista;
    }

    public void setRutaVista(String rutaVista) {
        this.rutaVista = rutaVista;
    }

    public Integer getModuloId() {
        return moduloId;
    }

    public void setModuloId(Integer moduloId) {
        this.moduloId = moduloId;
    }
}

