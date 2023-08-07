package Model;

public class Manajer extends Staf {
    private String password;

    public Manajer(String password, int id_staf, String nama) {
        super(id_staf, nama);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean cekPassword(String pass) {
        if(pass.equals(password)) {
            return true;
        } else {
            return false;
        }
    }
}
