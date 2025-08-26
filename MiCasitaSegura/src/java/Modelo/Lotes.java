package Modelo;

public class Lotes {
    private int idLote;
    private String codigoLote; // único

    public Lotes() {
    }

    public Lotes(String codigoLote) {
        this.codigoLote = codigoLote;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public String getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }
}
