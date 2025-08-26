package Modelo;

public class Modulos {
    private int idModulo;
    private String nombreModulo;

    public Modulos() {
    }

    public Modulos(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }
}
