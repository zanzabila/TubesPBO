package Model;

public class DetailPesanan {
    private int id_detail_pesanan, id_menu, id_pesanan;
    private String status;

    public DetailPesanan() {}

    public DetailPesanan(int id_detail_pesanan, int id_menu, int id_pesanan, String status) {
        this.id_detail_pesanan = id_detail_pesanan;
        this.id_menu = id_menu;
        this.id_pesanan = id_pesanan;
        this.status = status;
    }

    public int getId_detail_pesanan() {
        return id_detail_pesanan;
    }

    public void setId_detail_pesanan(int id_detail_pesanan) {
        this.id_detail_pesanan = id_detail_pesanan;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(int id_pesanan) {
        this.id_pesanan = id_pesanan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
