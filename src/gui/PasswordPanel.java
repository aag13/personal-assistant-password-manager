
package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PasswordPanel extends JPanel{
    
    
    private JPanel passwordHeadPanel;
    private JPanel passwordBodyPanel;
    
    private RetrievePassword retrievePassword;
    private AddEditDeletePassword aedp;
    
    // add part
    private JTextField addNameField;
    private JTextField addUsernameField;
    private JPasswordField addPasswordField;
    private JPasswordField addRePasswordField;
    
    private JCheckBox editEnabled;
    
    //edit part
    private JTextField currentUsernameField;
    private JTextField currentPasswordField;
    private JPasswordField editPasswordField;
    private JPasswordField editRePasswordField;
    
    private JPasswordField masterPasswordField;
    private JButton showPanelButton;
    private JButton  logoutButton;
    private JLabel masterPasswordLabel;
    private DefaultComboBoxModel defaultComboboxModel;
    private JComboBox nameComboBox;
    
    
    private final int TEXTFIELD_SIZE = 40;
    
    public PasswordPanel(){
        setLayout(new BorderLayout());
        
        
        passwordHeadPanel = createPasswordHeadPanel();
        add(passwordHeadPanel, BorderLayout.NORTH);
        
        passwordBodyPanel = createPasswordBodyPanel();
        add(passwordBodyPanel, BorderLayout.CENTER);
        passwordBodyPanel.setVisible(false);
    }
    
    
    public void setRetrievePassword(RetrievePassword rp){
        this.retrievePassword = rp;
    }
    
    public void setAddEditDeletePassword(AddEditDeletePassword aedp){
        this.aedp = aedp;
    }
    
    private void setMasterPasswordField(String str){
        masterPasswordField.setText(str);
    }
    
    public void compareMaster(String pass){
        // either show the body panel or show dialog stating wrong password
        if(pass.equals(new String(masterPasswordField.getPassword()))){
            // valid master password, so make the panels visible
            masterPasswordLabel.setVisible(false);
            masterPasswordField.setVisible(false);
            showPanelButton.setVisible(false);
            
            logoutButton.setVisible(true);
            passwordBodyPanel.setVisible(true);
        }else{
            // show invalid password dialog and also clear password field
            setMasterPasswordField("");
        }
    }
    
    public void setCurrentUsername(String un){
        //set edit current password field
        currentUsernameField.setText(un);
    }
    
    public void setCurrentPassword(String pass){
        //set edit current password field
        currentPasswordField.setText(pass);
    }
    
    private void cleanAddPanel() {
        addNameField.setText("");
        addUsernameField.setText("");
        addPasswordField.setText("");
        addRePasswordField.setText("");
    }
    
    private void cleanEditPanel() {
        currentUsernameField.setText("");
        currentPasswordField.setText("");
        
    }
    
    private JPanel createPasswordHeadPanel(){
        JPanel headPanel = new JPanel();
        JLabel passwordHeadLabel = new JLabel("Password Manager");
        masterPasswordLabel = new JLabel("Master Password");
        masterPasswordField = new JPasswordField(15);
        
        showPanelButton = new JButton("Show");
        logoutButton = new JButton("Log Out");
        logoutButton.setVisible(false);
        
        
        showPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show logout button and hide this button
                if(retrievePassword != null){
                    retrievePassword.retrievePasswordForName("MASTER", 
                            PanelName.MASTER);
                }
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show panelButton button and hide this button
                masterPasswordField.setVisible(true);
                masterPasswordLabel.setVisible(true);
                showPanelButton.setVisible(true);

                logoutButton.setVisible(false);
                passwordBodyPanel.setVisible(false);

                setMasterPasswordField("");

                // clean everything
                cleanAddPanel();
                cleanEditPanel();
                editEnabled.setSelected(false);
                editPasswordField.setEditable(false);
                editRePasswordField.setEditable(false);
                
            }
        });
        
        
        headPanel.setBorder(BorderFactory.createEtchedBorder());
        headPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;
        
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        headPanel.add(passwordHeadLabel, gc);
        
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        headPanel.add(masterPasswordLabel, gc);
        
        gc.gridy = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        headPanel.add(masterPasswordField, gc);
        
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        headPanel.add(logoutButton, gc);
        
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        headPanel.add(showPanelButton,gc);
        
        
        return headPanel;
    }
    
    private JPanel createPasswordBodyPanel(){
        JPanel rightPanel = new JPanel();
        JTabbedPane jTabbedPane = new JTabbedPane();
        
        JPanel editPanel = createViewEditDeletePanel();
        JPanel addPanel = createAddPanel();
        
        jTabbedPane.add("View / Edit / Delete Password",editPanel);
        jTabbedPane.add("Add Password",addPanel);
        
        rightPanel.add(jTabbedPane);
        
        return rightPanel;
    }
    
    private JPanel createAddPanel(){
        JPanel panel = new JPanel();
        JLabel nameLabel = new JLabel("Enter Name : ");
        addNameField = new JTextField(TEXTFIELD_SIZE);
        JLabel usernameLabel = new JLabel("Enter Username/Email : ");
        addUsernameField = new JTextField(TEXTFIELD_SIZE);
        
        JLabel passwordLabel = new JLabel("Enter Password : ");
        addPasswordField = new JPasswordField(TEXTFIELD_SIZE);
        JLabel rePasswordLabel = new JLabel("Re-Enter Password : ");
        addRePasswordField = new JPasswordField(TEXTFIELD_SIZE);
        JButton addButton = new JButton("Add");
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pass1 = String.valueOf(addPasswordField.getPassword());
                String pass2 = String.valueOf(addRePasswordField.getPassword());
                
                if(!pass1.equals(pass2) || pass1.equals("")){
                    //JComponent.getTopLevelAncestor()
                    JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(panel),
                            "Passwords DO NOT match",
                            "Password mismatch error",
                            JOptionPane.ERROR_MESSAGE);
                }else if(aedp != null){
                    aedp.passwordAddEditDelete(PasswordAction.ADD, addNameField.getText(), 
                            addUsernameField.getText(), String.valueOf(addPasswordField.getPassword()));
                    defaultComboboxModel.addElement(addNameField.getText());
                    cleanAddPanel();
                }
                
            }
        });
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;
        
        // Name field label
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(nameLabel, gc);
        
        // Name text field 
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(addNameField, gc);
        
        // username field label
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(usernameLabel, gc);
        
        // username text field 
        gc.gridx = 1;
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(addUsernameField, gc);
        
        // password field label
        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(passwordLabel, gc);
        
        // password field
        gc.gridx = 1;
        gc.gridy = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(addPasswordField, gc);
        
        // re-password field label
        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(rePasswordLabel, gc);
        
        // re-password field
        gc.gridx = 1;
        gc.gridy = 3;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(addRePasswordField, gc);
        
        //add button
        gc.gridx = 1;
        gc.gridy = 4;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(addButton, gc);
        
        return panel;
    }
    
    private JPanel createViewEditDeletePanel(){
        JPanel panel = new JPanel();
        JLabel nameLabel = new JLabel("Select Name : ");
        
        defaultComboboxModel = new DefaultComboBoxModel(MainFrame.ids);
        nameComboBox = new JComboBox();
        nameComboBox.setModel(defaultComboboxModel);
        
        JLabel idUsernameLabel = new JLabel("Current Username : ");
        currentUsernameField = new JTextField(TEXTFIELD_SIZE);
        currentUsernameField.setEditable(false);
        
        JLabel idPasswordLabel = new JLabel("Current Password : ");
        currentPasswordField = new JTextField(TEXTFIELD_SIZE);
        currentPasswordField.setEditable(false);
        
        JLabel passwordLabel = new JLabel("Enter Password : ");
        editPasswordField = new JPasswordField(TEXTFIELD_SIZE);
        editPasswordField.setEditable(false);
        
        JLabel rePasswordLabel = new JLabel("Re-Enter Password : ");
        editRePasswordField = new JPasswordField(TEXTFIELD_SIZE);
        editRePasswordField.setEditable(false);
        
        // at the beginning, edit is unchecked, therefore edit button disabled and delete button enabled
        editEnabled = new JCheckBox("Edit");
        JButton editButton = new JButton("Edit");
        editButton.setEnabled(false);
        JButton deleteButton = new JButton("Delete");
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pass1 = String.valueOf(editPasswordField.getPassword());
                String pass2 = String.valueOf(editRePasswordField.getPassword());

                if (!pass1.equals(pass2) || pass1.equals("")) {
                    //JComponent.getTopLevelAncestor()
                    JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(panel),
                            "Passwords DO NOT match",
                            "Password mismatch error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (aedp != null) {
                    aedp.passwordAddEditDelete(PasswordAction.EDIT, (String) nameComboBox.getSelectedItem(),
                            currentUsernameField.getText(), String.valueOf(editPasswordField.getPassword()));
                    if (retrievePassword != null) {
                        retrievePassword.retrievePasswordForName((String) nameComboBox.getSelectedItem(),
                                PanelName.EDIT);
                    }
                    editPasswordField.setText("");
                    editRePasswordField.setText("");

                }
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = (String) nameComboBox.getSelectedItem();
                if (selectedName.equals("MASTER")) {
                    JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(panel),
                            "cannot delete MASTER password dude...WTF!!!",
                            "MASTER password deletion error",
                            JOptionPane.ERROR_MESSAGE);
                }else if (aedp != null) {
                    aedp.passwordAddEditDelete(PasswordAction.DELETE, selectedName,
                            currentUsernameField.getText(), String.valueOf(editPasswordField.getPassword()));
                    defaultComboboxModel.removeElement(selectedName);
                }
                
            }
        });
        
        editEnabled.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(editEnabled.isSelected()){
                    editPasswordField.setEditable(true);
                    editRePasswordField.setEditable(true);
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(false);
                }else{
                    editPasswordField.setText("");
                    editRePasswordField.setText("");
                    editPasswordField.setEditable(false);
                    editRePasswordField.setEditable(false);
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(true);
                }
            }
        });
        
        nameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 if(retrievePassword != null){
                     retrievePassword.retrievePasswordForName((String) nameComboBox.getSelectedItem(), 
                             PanelName.EDIT);
                 }
            }
        });
        
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;
        
        // name label
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(nameLabel, gc);
        
        // name combobox
        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(nameComboBox, gc);
        
        // username label
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(idUsernameLabel, gc);
        
        // username field
        gc.gridx = 1;
        gc.gridy = 1;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(currentUsernameField, gc);
        
        
        // name password label
        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(idPasswordLabel, gc);
        
        // name password field
        gc.gridx = 1;
        gc.gridy = 2;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(currentPasswordField, gc);
        
        // password label
        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(passwordLabel, gc);
        
        // password field
        gc.gridx = 1;
        gc.gridy = 3;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(editPasswordField, gc);
        
        // re password label
        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(rePasswordLabel, gc);
        
        // re password field
        gc.gridx = 1;
        gc.gridy = 4;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(editRePasswordField, gc);
        
        //editEnabled checkbox
        gc.gridx = 0;
        gc.gridy = 5;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(editEnabled, gc);
        
        //edit button
        gc.gridx = 1;
        gc.gridy = 5;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(editButton, gc);
        
        //delete button
        gc.gridx = 2;
        gc.gridy = 5;
        gc.insets = new Insets(5, 5, 5, 5);
        panel.add(deleteButton, gc);
        
        return panel;
    }
    
    
}
