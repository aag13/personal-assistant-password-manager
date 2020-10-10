
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeadPanel extends JPanel{
    
    private JPanel leftPanel;
    private JPanel rightPanel;
    Dimension dim;
    private OptionListener optionListener;
    
    public HeadPanel(){
        dim = getPreferredSize();
//        dim.height = dim.height/4;
//        setPreferredSize(dim);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        
        // LEFT PANEL
        leftPanel = createLeftPanel();
        // RIGHT PANEL
        rightPanel = createRightPanel();
        
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        
    }
    
    private JPanel createLeftPanel(){
        JPanel left = new JPanel();
        left.add(new JLabel("What's Cooking??"));
        
        JComboBox optionBox = new JComboBox();
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        for(String s:MainFrame.options){
            comboModel.addElement(s);;
        }
        optionBox.setModel(comboModel);
        optionBox.setSelectedIndex(-1);
        left.add(optionBox);
        
        optionBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 if(optionListener != null){
                     optionListener.optionChanged(optionBox.getSelectedIndex());
                     System.out.println("selected index is : "+optionBox.getSelectedIndex());
                 }else{
                     System.out.println("something wrong with the option listener!!!");
                 }
            }
        });
        
        left.setBorder(BorderFactory.createEtchedBorder());
        return left;
    }
    
    private JPanel createRightPanel(){
        JPanel right = new JPanel();
        right.add(new JLabel("Right Panel : Stuff to add later"));
        right.setBorder(BorderFactory.createEtchedBorder());
        return right;
    }
    
    public void setOptionListener(OptionListener ol){
        this.optionListener = ol;
    }
}
