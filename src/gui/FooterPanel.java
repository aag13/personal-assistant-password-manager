package gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author aag-pc
 */
public class FooterPanel extends JPanel {

    private JTextArea notificationArea;

    public FooterPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());

        notificationArea = new JTextArea(2, 20);
        add(new JScrollPane(notificationArea));

    }

    public void setNotification(String message) {
        notificationArea.setText(message);

    }
}
