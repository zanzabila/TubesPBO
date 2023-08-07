package Model;

public class Meja {
    private int nomor;
    private boolean isi;

    public Meja(){};
    
    public Meja(int nomor, boolean isi) {
        this.nomor = nomor;
        this.isi = isi;
    }

    public int getNomor() {
        return nomor;
    }

    public void setNomor(int nomor) {
        this.nomor = nomor;
    }

    public boolean isIsi() {
        return isi;
    }

    public void setIsi(boolean isi) {
        this.isi = isi;
    }
    
}
