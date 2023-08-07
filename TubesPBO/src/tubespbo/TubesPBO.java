// TUGAS BESAR PEMROGRAMAN BERBASIS OBJEK
// MANAJEMEN RESTORAN
/** 
 * Rayhan Zanzabila         1301191072
 * Rania Az Zahra           1301194349
 * Laura Imanuela Mustamu   1301194027
 */

package tubespbo;

import Controller.ControllerRestoran;
import View.FrameRestoran;

public class TubesPBO {

    public static void main(String[] args) {
        FrameRestoran frmResto = new FrameRestoran();
        ControllerRestoran ctlResto = new ControllerRestoran(frmResto);
        
        frmResto.setLocationRelativeTo(null);
        frmResto.setVisible(true);
        frmResto.setTitle("Restaurant Management App");
        
    }
    
}
