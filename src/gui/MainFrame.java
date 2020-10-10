package gui;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import model.MessageDispatcher;
import model.Password;

public class MainFrame extends JFrame {

    public static String[] ids;
    public static Map<Integer,String> tasks;

    private HeadPanel headPanel;
    private BodyPanel bodyPanel;
    private FooterPanel footerPanel;

    private PasswordPanel bodyPanel_passwordPanel;
    private DownloaderPanel bodyPanel_downDownloaderPanel;
    private TaskTimerPanel bodyPanel_taskTimerPanel;

    public static String[] options = {ApplicationOptions.Password_Manager.toString().replace("_", " "),
        ApplicationOptions.Mass_Downloader.toString().replace("_", " "),
        ApplicationOptions.Task_Timer.toString().replace("_", " ")
    };

    private Controller controller;

    public MainFrame() {

        setLayout(new BorderLayout());
        controller = new Controller();

        // ======================  controller ======================
        //this ordering is important..
        try {
            controller.setupDatabase();
            MainFrame.ids = controller.getPasswords();
            MainFrame.tasks = controller.getTasks();
        }catch(Exception exp){
            exp.printStackTrace();
        }

        controller.setDispatcher(new MessageDispatcher() {
            @Override
            public void notifyMessage(String message) {
                footerPanel.setNotification(message);
            }
        });

        // ======================  main panels ======================
        headPanel = new HeadPanel();
        bodyPanel = new BodyPanel();
        footerPanel = new FooterPanel();

        headPanel.setOptionListener(new OptionListener() {
            public void optionChanged(int index) {
                bodyPanel.setSelectedVisible(index);
            }
        });

        // ======================  password panel ======================
        bodyPanel_passwordPanel = bodyPanel.getPasswordPanel();
        bodyPanel_passwordPanel.setRetrievePassword(new RetrievePassword() {
            public void retrievePasswordForName(String name, PanelName panelName) {
                try {
                    Password pass = controller.getPassword(name);
                    //sets the pass to appropriate field depending on panel name
                    if (panelName == PanelName.MASTER) {
                        bodyPanel_passwordPanel.compareMaster(pass.getPassword());
                    } else if (panelName == PanelName.EDIT) {
                        bodyPanel_passwordPanel.setCurrentUsername(pass.getUsername());
                        bodyPanel_passwordPanel.setCurrentPassword(pass.getPassword());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        bodyPanel_passwordPanel.setAddEditDeletePassword(new AddEditDeletePassword() {
            @Override
            public void passwordAddEditDelete(PasswordAction pa, String name, String username, String pass) {

                try {
                    if (pa == PasswordAction.ADD) {
                        String query = String.format("insert into PASSWORD( NAME, USERNAME, PASS)"
                                + " values('%s','%s','%s')", name, username, pass);

                        controller.addEditDeletePassword(query);
                        MainFrame.ids = controller.getPasswords();
                    } else if (pa == PasswordAction.EDIT) {
                        String query = String.format("update PASSWORD set PASS='%s'"
                                + " where NAME='%s'", pass, name);
                        controller.addEditDeletePassword(query);
                    } else if (pa == PasswordAction.DELETE) {
                        String query = String.format("delete from PASSWORD"
                                + " where NAME='%s'", name);
                        controller.addEditDeletePassword(query);
                        MainFrame.ids = controller.getPasswords();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        

        // ======================  downloader panel ======================
        bodyPanel_downDownloaderPanel = bodyPanel.getDownloaderPanel();
        bodyPanel_downDownloaderPanel.setDownloadStarter(new DownloadStarter() {
            @Override
            public void startDownload(String url, String directory, LinkedList listlll) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            bodyPanel_downDownloaderPanel.disableDownloadControls();
                            System.out.println("Inside thread..starting download");
                            controller.download(url, directory, listlll);
                            bodyPanel_downDownloaderPanel.enableDownloadControls();

                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        // ======================  task timer panel ======================
        bodyPanel_taskTimerPanel = bodyPanel.getTaskTimerPanel();
        bodyPanel_taskTimerPanel.setAddDeleteItem(new AddDeleteItem() {
            @Override
            public void updateListAdd(String item) {
                
                try {
                    controller.addListItem(item);
                    
                    //for testing purpose, to see if it automatically handles the change
                    MainFrame.tasks = controller.getTasks();
                    
                    bodyPanel_taskTimerPanel.addItemToList(item);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void updateListDelete(String item) {
                try {
                    controller.deleteListItem(item);
                    
                    MainFrame.tasks = controller.getTasks();
                    
                    bodyPanel_taskTimerPanel.deleteItemFromList(item);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
                
            }
        });

        add(headPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
        setJMenuBar(createMenuBar());
        setTitle("Personal Assistant");
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setSize(800,500);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitMenu = new JMenuItem("Quit");
        fileMenu.add(quitMenu);

        quitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Are you sure you want to quit?",
                        "Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }

            }
        });

        JMenu helpMenu = new JMenu("Help");
        JMenuItem faqMenu = new JMenuItem("FAQ...");
        helpMenu.add(faqMenu);

        faqMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "help page will be shown here");

            }
        });

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }
}
