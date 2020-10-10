
package gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NotePanel extends JPanel{
    
    public NotePanel(){
        
        setLayout(new BorderLayout());
        add(new JLabel("Note Panel Selected"), BorderLayout.NORTH);
        
//        add(new JLabel("EAST"), BorderLayout.EAST);
//        add(new JLabel("WEST"), BorderLayout.WEST);
//        add(new JLabel("SOUTH"), BorderLayout.SOUTH);
//        JButton jbt = new JButton("Click ME");
//        jbt.setFont(new Font("Serif", 3, 40));
//        jbt.setPreferredSize(new Dimension(50, 50));
//        add(jbt, BorderLayout.CENTER);
        
        
    }
    
}
