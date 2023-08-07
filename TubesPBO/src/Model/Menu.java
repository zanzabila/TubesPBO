package Model;

public class Menu {
    private String nama, jenis;
    private int idMenu, harga;
    
    public Menu() {}
    
    public Menu(String nama, String jenis, int idMenu, int harga) {
        this.nama = nama;
        this.jenis = jenis;
        this.idMenu = idMenu;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
    
}
