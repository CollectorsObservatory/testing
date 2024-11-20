package equipe06;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); 
        } catch (Exception ex) {
            System.err.println("Erreur d'initialisation de FlatLaf!");
        }

        // Lancer l'application avec la fenêtre principale
        java.awt.EventQueue.invokeLater(() -> {
            equipe06.gui.MainWindow mainWindow = new equipe06.gui.MainWindow();
            mainWindow.setVisible(true);
        });
    }
}
