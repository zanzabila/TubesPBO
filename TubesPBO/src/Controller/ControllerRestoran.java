package Controller;

import DBConnector.DBConnection;
import Model.DetailPesanan;
import Model.Manajer;
import Model.Meja;
import Model.Menu;
import Model.Pesanan;
import Model.Staf;
import View.FrameRestoran;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControllerRestoran {
    private FrameRestoran frmResto;
    private List<Meja> tables;
    private List<Menu> menus;
    private List<String> orders;
    private List<Integer> ready;
    private List<Integer> tablePay;
    private List<Integer> listIdMenu;
    private List<String> isiPesanan;
    private List<String> mejaKoki;
    private List<String> isiPesananKoki;
    private List<Integer> listIdPesanan;
    private ArrayList<Pesanan> pesanan;
    private ArrayList<Pesanan> pesananSdgMasak;
    private ArrayList<Pesanan> pesananSiapAntar;
    private ArrayList<Pesanan> pesananSdhAntar;
    private ArrayList<Pesanan> pesananSdhBayar;
    private ArrayList<Staf> staf;
    private ArrayList<Manajer> manajer;
    private ArrayList<DetailPesanan> details;
    private DBConnection conn;
    private String q;
    private int noMeja, noMejaKoki;

    public ControllerRestoran(FrameRestoran frmResto) {
        try {
            this.frmResto = frmResto;
            this.conn = new DBConnection();
            tables = new ArrayList<>();
            menus = new ArrayList<>();
            orders = new ArrayList<>();
            ready = new ArrayList<>();
            tablePay = new ArrayList<>();
            listIdMenu = new ArrayList<>();
            isiPesanan = new ArrayList<>();
            mejaKoki = new ArrayList<>();
            isiPesananKoki = new ArrayList<>();
            listIdPesanan = new ArrayList<>();
            pesanan = new ArrayList<>();
            pesananSdgMasak = new ArrayList<>();
            pesananSiapAntar = new ArrayList<>();
            pesananSdhAntar = new ArrayList<>();
            pesananSdhBayar = new ArrayList<>();
            staf = new ArrayList<>();
            manajer = new ArrayList<>();
            details = new ArrayList<>();
            ambilSemuaPesanan();
            getDetailPesanan();
            getStafManajer();
            getAllMenus();
            getAllMeja();
            getAllReady();
            getMejaBayar();
            getAllMejaKoki();
            getAllPesananKoki();
            frmResto.loadMenu(menus); //load menu di tab pemesanan
            frmResto.loadMejaPemesanan(tables); // load no meja di tab pemesanan
            frmResto.loadSiapAntar(ready); //load no meja yang siap antar di tab antar
            frmResto.loadMejaSiapBayar(tablePay); //load no meja yang siap antar di tab antar
            frmResto.displayListMejaKoki((ArrayList<String>) mejaKoki); //display no meja di tab koki
            frmResto.displayListPesananKoki((ArrayList<String>) isiPesananKoki); //display nama makanan di tab koki
            frmResto.addButtonListener(new ButtonListener());
            frmResto.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    noMejaKoki = frmResto.getMejaKoki();
                    getAllPesananKoki();
                    frmResto.displayListPesananKoki((ArrayList<String>) isiPesananKoki);
                }
            });
            frmResto.loadTotalHarga(0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public class ButtonListener  implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Tambah":
                    String so = frmResto.getOrder();
                    int jo = frmResto.getQty();
                    for(int i=0; i<jo; i++) {
                        orders.add(so);
                    }
                    frmResto.displayListPesanan((ArrayList<String>) orders);
                    break;
                case "Hapus":
                    int idx_ord = frmResto.getIndexListPemesanan();
                    orders.remove(idx_ord);
                    frmResto.displayListPesanan((ArrayList<String>) orders);
                    frmResto.enableBtnHapus(false);
                    break;
                case "Pesan":
                    try {
                        noMeja = frmResto.getNoMejaPemesanan();
                        int id_order = 0;
                        int id_makanan = 0;
                        q = "INSERT INTO pesanan(no_meja, status_pesanan) VALUES (" + 
                                    noMeja + ", 'Sedang Dimasak')";
                        conn.query(q);
                        q = "SELECT * FROM pesanan WHERE no_meja = " + Integer.toString(noMeja);
                        ResultSet rs = conn.getData(q);
                        while (rs.next()) {
                            id_order = rs.getInt("id_pesanan");
                        }
                        for (String s: orders) {
                            q = "SELECT * FROM menu WHERE nama = '" + s + "'";
                            rs = conn.getData(q);
                            while (rs.next()) {
                                id_makanan = rs.getInt("id_menu");
                            }
                            q = "INSERT INTO detail_pesanan(id_menu, status, id_pesanan) VALUES (" + id_makanan
                                 + ", 'Belum Dimasak', " + id_order + ")";
                            conn.query(q);
                        }
                        
                        q = "UPDATE meja SET isi=1 WHERE no_meja = " + noMeja;
                        conn.query(q);
                        orders.clear();
                        getAllMeja();
                        frmResto.loadMejaPemesanan(tables);
                        frmResto.displayListPesanan((ArrayList<String>) orders);
                        getAllMejaKoki();
                        frmResto.displayListMejaKoki((ArrayList<String>) mejaKoki);
                        ambilSemuaPesanan();
                        
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    frmResto.enableBtnHapus(false);
                                              
                    break;
                case "Konfirmasi":
                    try {
                        noMeja = frmResto.getMejaKoki();
                        q = "SELECT * FROM pesanan WHERE status_pesanan='Sedang Dimasak' AND no_meja=" + noMeja;
                        ResultSet rs = conn.getData(q);
                        rs.next();
                        int idPesanan = rs.getInt("id_pesanan");
                                                                                                                                      
                        String namaMenu = frmResto.getPesananKoki();
                        q = "SELECT * FROM menu WHERE nama= '" + namaMenu + "'";
                        rs = conn.getData(q);
                        rs.next();
                        int idMenu = rs.getInt("id_menu");
                        
                        q = "SELECT * FROM detail_pesanan WHERE status = 'Belum Dimasak' AND id_pesanan = " + idPesanan 
                                + " AND id_menu = " + idMenu ;
                        rs = conn.getData(q);
                        rs.next();
                        int idDetail = rs.getInt("id_detail_pesanan");
                                                                                                                            
                        q = "UPDATE detail_pesanan SET status='Sudah Dimasak' WHERE id_detail_pesanan = " + idDetail ;
                        conn.query(q);
                        
                        cekSiapAntar();
                        getAllReady();
                        frmResto.loadSiapAntar(ready);
                        getAllPesananKoki();
                        frmResto.displayListPesananKoki((ArrayList<String>) isiPesananKoki);
                        getAllMejaKoki();
                        frmResto.displayListMejaKoki((ArrayList<String>) mejaKoki);
                        ambilSemuaPesanan();
                    } catch(SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "Antar":
                    try {
                        noMeja = ready.get(frmResto.getMejaReady());
                        q = "UPDATE pesanan SET status_pesanan='Sudah Diantar' WHERE status_pesanan='Siap Diantar' AND no_meja=" + noMeja;
                        conn.query(q);
                        getAllReady();
                        frmResto.loadSiapAntar(ready);
                        getMejaBayar();
                        frmResto.loadMejaSiapBayar(tablePay);
                        ambilSemuaPesanan();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "Hitung":
                    try {
                        listIdMenu.clear();
                        int total_harga = 0;
                        int id_order = 0;
                        int noMeja = frmResto.getMejaReadyPay();
                        q = "SELECT * FROM pesanan WHERE status_pesanan = 'Sudah Diantar' AND no_meja = " + noMeja;
                        ResultSet rs = conn.getData(q);
                        while (rs.next()) {
                            id_order = rs.getInt("id_pesanan");
                        }
                        q = "SELECT * FROM detail_pesanan WHERE id_pesanan = " + id_order;
                        rs = conn.getData(q);
                        while (rs.next()) {
                            listIdMenu.add(rs.getInt("id_menu"));
                        }
                        
                        isiPesanan.clear();
                        for (int i: listIdMenu) {
                            q = "SELECT * FROM menu WHERE id_menu = " + i;
                            rs = conn.getData(q);
                            while (rs.next()) {
                                isiPesanan.add(rs.getString("nama"));
                                total_harga += rs.getInt("harga");
                            }
                        }
                        frmResto.loadTotalHarga(total_harga);
                        frmResto.displayListPembayaran((ArrayList<String>)isiPesanan);
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    
                    break;
                case "Bayar":
                    noMeja = frmResto.getMejaReadyPay();
                    q = "UPDATE pesanan SET status_pesanan='Sudah Dibayar' WHERE no_meja=" + noMeja;
                    conn.query(q);
                    isiPesanan.clear();
                    frmResto.displayListPembayaran((ArrayList<String>) isiPesanan);
                    q = "UPDATE meja SET isi=0 WHERE no_meja = " + noMeja;
                    conn.query(q);
                    getMejaBayar();
                    frmResto.loadMejaSiapBayar(tablePay);
                    frmResto.loadTotalHarga(0);
                    getAllMeja();
                    frmResto.loadMejaPemesanan(tables);
                    ambilSemuaPesanan();
                    break;
                case "Log in":
                    frmResto.setMenuPopuler(menuPopuler());
                    frmResto.setOmzet(Omzet());
                    String frmNama = frmResto.getLoginNama();
                    String frmPass = frmResto.getLoginPass();
                    boolean cek = false;
                    int i = 0;
                    for(; i < manajer.size(); i++) {
                        if(manajer.get(i).getNama().equals(frmNama)) {
                            cek = true;
                            break;
                        }
                    }
                    if (cek) {
                        if (manajer.get(i).cekPassword(frmPass)) {
                            frmResto.displayLaporan();
                        } else {
                            frmResto.displayGagalLogin();
                        }
                    } else {
                        frmResto.displayGagalLogin();
                    }
                    
                    break;
                default:
                    break;
            }
        }
    }
    
    public void ambilSemuaPesanan() {
        try {
            pesanan.clear();
            ResultSet entries = conn.getData("SELECT * FROM pesanan");
            while (entries.next()){
                Pesanan p = new Pesanan();
                p.setId_pesanan(entries.getInt("id_pesanan"));
                p.setNo_meja(entries.getInt("no_meja"));
                p.setStatus_pesanan(entries.getString("status_pesanan"));
                pesanan.add(p);
            }
            ambilPerStatus("Sedang Dimasak", pesananSdgMasak);
            ambilPerStatus("Siap Diantar", pesananSiapAntar);
            ambilPerStatus("Sudah Diantar", pesananSdhAntar);
            ambilPerStatus("Sudah Dibayar", pesananSdhBayar);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void ambilPerStatus(String stts, ArrayList<Pesanan> psnn) {
        psnn.clear();
        for(Pesanan p: pesanan) {
            if(p.getStatus_pesanan().equals(stts)) {
                psnn.add(p);
            }
        }
    }
    
    public void getDetailPesanan() {
        try {
            details.clear();
            ResultSet entries = conn.getData("SELECT * FROM detail_pesanan");
            while (entries.next()){
                DetailPesanan dp = new DetailPesanan();
                dp.setId_detail_pesanan(entries.getInt("id_detail_pesanan"));
                dp.setId_menu(entries.getInt("id_menu"));
                dp.setId_pesanan(entries.getInt("id_pesanan"));
                dp.setStatus(entries.getString("status"));
                details.add(dp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getStafManajer() {
        try {
            ResultSet entries = conn.getData("SELECT * FROM staf");
            while (entries.next()){
                Staf s = new Staf();
                s.setId_staf(entries.getInt("id_staf"));
                s.setNama(entries.getString("nama"));
                staf.add(s);
            }
            entries = conn.getData("SELECT * FROM manajer");
            while (entries.next()) {
                int id_manajer = entries.getInt("id_staf");
                String password = entries.getString("password");
                String nama = "";
                for(Staf s: staf) {
                    if (s.getId_staf() == id_manajer) {
                        nama = s.getNama();
                        break;
                    }
                }
                manajer.add(new Manajer(password, id_manajer, nama));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getAllMenus() {
        try {
            menus.clear();
            ResultSet entries = conn.getData("SELECT * FROM menu");
            while (entries.next()){
                Menu m = new Menu();
                m.setIdMenu(entries.getInt("id_menu"));
                m.setNama(entries.getString("nama"));
                m.setJenis(entries.getString("jenis"));
                m.setHarga(entries.getInt("harga"));
                menus.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getAllMeja() {
        try {
            tables.clear();
            ResultSet entries = conn.getData("SELECT * FROM meja");
            while (entries.next()){
                Meja mj = new Meja();
                mj.setNomor(entries.getInt("no_meja"));
                mj.setIsi(entries.getBoolean("isi"));
                tables.add(mj);
            }
        } catch (SQLException ex){
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getAllReady() {
        try {
            ready.clear();
            ResultSet entries = conn.getData("SELECT * FROM pesanan WHERE status_pesanan = 'Siap Diantar'");
            while (entries.next()){
                ready.add(entries.getInt("no_meja"));
            }
        } catch (SQLException ex){
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getMejaBayar() {
        try {
            tablePay.clear();
            ResultSet entries = conn.getData("SELECT * FROM pesanan WHERE status_pesanan='Sudah Diantar'");
            while (entries.next()){
                tablePay.add(entries.getInt("no_meja"));
            }
        } catch (SQLException ex){
            Logger.getLogger(ControllerRestoran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getAllMejaKoki() {
        try {
            mejaKoki.clear();
            q = "SELECT * FROM pesanan WHERE status_pesanan='Sedang Dimasak'";
            ResultSet rs = conn.getData(q);
            while (rs.next()){
                mejaKoki.add(Integer.toString(rs.getInt("no_meja")));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
    
    
    public void getAllPesananKoki() {
        try {
            int idPesanan = 0;
            isiPesananKoki.clear();
            listIdMenu.clear();
            q = "SELECT * FROM pesanan WHERE status_pesanan='Sedang Dimasak' AND no_meja=" + noMejaKoki;
            ResultSet rs = conn.getData(q);
            while (rs.next()){
                idPesanan = rs.getInt("id_pesanan");
            }
            
            q = "SELECT * FROM detail_pesanan WHERE status='Belum Dimasak' AND id_pesanan=" + idPesanan;
            rs = conn.getData(q);
            while(rs.next()) {
                listIdMenu.add(rs.getInt("id_menu"));
            }
            for(int id: listIdMenu) {
                q = "SELECT * FROM menu WHERE id_menu=" + id;
                rs = conn.getData(q);
                while(rs.next()) {
                    isiPesananKoki.add(rs.getString("nama"));
                }
            }
            
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
    
    public void cekSiapAntar() {
        try {
            listIdPesanan.clear();
            q = "SELECT * FROM pesanan, detail_pesanan WHERE pesanan.id_pesanan=detail_pesanan.id_pesanan AND pesanan.status_pesanan='Sedang Dimasak'";
            ResultSet rs = conn.getData(q);
            while(rs.next()) {
                if (listIdPesanan.isEmpty()) {
                    listIdPesanan.add(rs.getInt("id_pesanan"));
                    continue;
                }
                if (listIdPesanan.get(listIdPesanan.size()-1) != rs.getInt("id_pesanan")) {
                    listIdPesanan.add(rs.getInt("id_pesanan"));
                }
            }
            
            for(int id: listIdPesanan) {
                q = "SELECT * FROM detail_pesanan WHERE id_pesanan=" + id;
                boolean siap = true;
                rs = conn.getData(q);
                while(rs.next()) {
                    if ((rs.getString("status")).equals("Belum Dimasak")) {
                        siap = false;
                    }
                }
                if (siap) {
                    q = "UPDATE pesanan SET status_pesanan='Siap Diantar' WHERE id_pesanan=" + id;
                    conn.query(q);
                }
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public String menuPopuler() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for(Pesanan p: pesananSdhBayar) {
            for(DetailPesanan dp: details) {
                if(dp.getId_pesanan() == p.getId_pesanan()) {
                    int idMenu = dp.getId_menu();
                    if(map.containsKey(idMenu)) {
                        map.put(idMenu, map.get(idMenu) + 1);
                    } else {
                        map.put(idMenu, 1);
                    }
                }
            }
        }
        int max = 0;
        int idMax = 0;
        for (Map.Entry m: map.entrySet()) {
            if (((int)m.getValue()) > max) {
                max = (int) m.getValue();
                idMax = (int) m.getKey();
            } 
        }
        for(Menu m: menus) {
            if(m.getIdMenu() == idMax) {
                return m.getNama();
            }
        }
        return "";
    }
    
    public int Omzet() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for(Pesanan p: pesananSdhBayar) {
            for(DetailPesanan dp: details) {
                if(dp.getId_pesanan() == p.getId_pesanan()) {
                    int idMenu = dp.getId_menu();
                    if(map.containsKey(idMenu)) {
                        map.put(idMenu, map.get(idMenu) + 1);
                    } else {
                        map.put(idMenu, 1);
                    }
                }
            }
        }
        int total=0;
        int harga=0;
        for (Map.Entry m: map.entrySet()) {
            for (Menu mn: menus) {
                if ((int)m.getKey() == mn.getIdMenu()) {
                    harga = mn.getHarga();
                }
            }
            total += harga * (int)m.getValue();
        }
        return total;
    }
    
}
