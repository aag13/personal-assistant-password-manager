package gui;

import java.awt.CardLayout;
import java.util.LinkedList;
import javax.swing.JPanel;

public class BodyPanel extends JPanel {

    private JPanel emptyPanel;
    private PasswordPanel passwordPanel;
    private DownloaderPanel downloaderPanel;
    private TaskTimerPanel taskTimerPanel;
    private JPanel currentPanel;

    public BodyPanel() {

        emptyPanel = new JPanel();
        passwordPanel = new PasswordPanel();
        downloaderPanel = new DownloaderPanel();
        taskTimerPanel = new TaskTimerPanel();

        setLayout(new CardLayout());

        add(emptyPanel, "empty");
        add(passwordPanel, ApplicationOptions.Password_Manager.toString().replace("_", " "));
        add(downloaderPanel, ApplicationOptions.Mass_Downloader.toString().replace("_", " "));
        add(taskTimerPanel, ApplicationOptions.Task_Timer.toString().replace("_", " "));

        currentPanel = emptyPanel;

    }

    public PasswordPanel getPasswordPanel() {
        return this.passwordPanel;
    }

    public DownloaderPanel getDownloaderPanel() {
        return this.downloaderPanel;
    }

    public TaskTimerPanel getTaskTimerPanel() {
        return this.taskTimerPanel;
    }

    public void setSelectedVisible(int index) {
        // set the appropriate panel visible
        CardLayout cl = (CardLayout) this.getLayout();
        cl.show(this, MainFrame.options[index]);

    }
}
