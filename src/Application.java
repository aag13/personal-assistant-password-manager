
import gui.MainFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author aag-pc
 */
public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame();
            }
        });
    }
}
