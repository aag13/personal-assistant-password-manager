package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author aag-pc
 */
public class DownloaderPanel extends JPanel {

    private JPanel urlpanel;
    private JPanel directoryPanel;

    private JTextField urlField;
    private JTextField directoryField;

    private JCheckBox pdfBox;
    private JCheckBox zipBox;
    private JCheckBox mp3Box;
    private JCheckBox pptBox;
    private JCheckBox txtBox;

    private JFileChooser fileChooser;
    private JButton browseButton;
    private JButton downloadButton;

    private final int TEXTFIELD_SIZE = 40;

    private DownloadStarter downloadStarter;

    public DownloaderPanel() {
        setLayout(new BorderLayout());
        urlpanel = createURLPanel();
        add(urlpanel, BorderLayout.NORTH);

        directoryPanel = createDirectoryPanel();
        add(directoryPanel, BorderLayout.CENTER);

    }

    private JPanel createURLPanel() {
        JPanel panel = new JPanel();

        JLabel urlLabel = new JLabel("Enter URL to download from : ");
        urlField = new JTextField(TEXTFIELD_SIZE);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;

        // url label
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(urlLabel, gc);

        // url field
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 4;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(urlField, gc);

        pdfBox = new JCheckBox(".pdf");
        zipBox = new JCheckBox(".zip");
        mp3Box = new JCheckBox(".mp3");
        pptBox = new JCheckBox(".ppt");
        txtBox = new JCheckBox(".txt");

        // pdfBox
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(pdfBox, gc);

        // zipBox
        gc.gridx = 1;
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(zipBox, gc);

        // pptBox
        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(pptBox, gc);

        // txtBox
        gc.gridx = 1;
        gc.gridy = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(txtBox, gc);

        // mp3Box
        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(mp3Box, gc);

        return panel;
    }

    private JPanel createDirectoryPanel() {
        JPanel panel = new JPanel();

        directoryField = new JTextField(TEXTFIELD_SIZE);
        directoryField.setText("");
        directoryField.setEditable(false);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        browseButton = new JButton("Save in..");
        downloadButton = new JButton("Start Download");

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(directoryPanel) == JFileChooser.APPROVE_OPTION) {
                    String dir = fileChooser.getSelectedFile().toString();
                    directoryField.setText(dir);
                }

            }
        });

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String tempurl = urlField.getText().trim();
                String tempdirectory = directoryField.getText();
                LinkedList ll = getExtensions();
                String notifyMessage = "";
                boolean flag = true;
                if (tempurl.equals("")) {
                    notifyMessage = "No URL provided";
                    flag = false;
                } else if (tempdirectory.equals("")) {
                    notifyMessage = "No Folder to Save is provided";
                    flag = false;
                } else if (ll.size() == 0) {
                    notifyMessage = "No File Type is provided";
                    flag = false;
                }
                if (flag) {
                    //everything is fine, so start download

                    downloadStarter.startDownload(tempurl, tempdirectory, ll);

                } else {
                    JOptionPane.showMessageDialog(directoryPanel, notifyMessage);
                }
            }
        });

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;

        // directory field
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(directoryField, gc);

        // browse button
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(browseButton, gc);

        // download button
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(downloadButton, gc);

        return panel;
    }

    private LinkedList<String> getExtensions() {
        LinkedList<String> ll = new LinkedList<String>();
        if (pdfBox.isSelected()) {
            ll.add(".pdf");
        }
        if (zipBox.isSelected()) {
            ll.add(".zip");
        }
        if (mp3Box.isSelected()) {
            ll.add(".mp3");
        }
        if (pptBox.isSelected()) {
            ll.add(".ppt");
        }
        if (txtBox.isSelected()) {
            ll.add(".txt");
        }

        return ll;
    }

    public void setDownloadStarter(DownloadStarter ds) {
        this.downloadStarter = ds;
    }

    public void disableDownloadControls() {
        downloadButton.setEnabled(false);
        browseButton.setEnabled(false);
    }

    public void enableDownloadControls() {
        downloadButton.setEnabled(true);
        browseButton.setEnabled(true);
    }

}
