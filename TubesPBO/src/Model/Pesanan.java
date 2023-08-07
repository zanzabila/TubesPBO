package Model;

public class Pesanan {
    private int id_pesanan, no_meja;
    private String status_pesanan;

    public Pesanan() {}
    
    public Pesanan(int id_pesanan, int no_meja, String status_pesanan) {
        this.id_pesanan = id_pesanan;
        this.no_meja = no_meja;
        this.status_pesanan = status_pesanan;
    }

    public int getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(int id_pesanan) {
        this.id_pesanan = id_pesanan;
    }

    public int getNo_meja() {
        return no_meja;
    }

    public void setNo_meja(int no_meja) {
        this.no_meja = no_meja;
    }

    public String getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(String status_pesanan) {
        this.status_pesanan = status_pesanan;
    }
    
}
