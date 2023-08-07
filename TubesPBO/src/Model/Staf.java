package Model;

public class Staf {
    private int id_staf;
    private String nama;

    public Staf() {}

    public Staf(int id_staf, String nama) {
        this.id_staf = id_staf;
        this.nama = nama;
    }

    public int getId_staf() {
        return id_staf;
    }

    public void setId_staf(int id_staf) {
        this.id_staf = id_staf;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
        
}
