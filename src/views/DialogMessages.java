/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author alvaro
 */
public class DialogMessages{
    private String title;
    private String text;
    private int type;
    private String[] options;
    
    public DialogMessages(String title, String text, String[] options){
        this.title = title;
        this.text = text;
        this.options = options;
    }
    
    public DialogMessages(String title, String text, int type){
        this.title = title;
        this.text = text;
        this.type = type;
    }
    
    public void showMessage(){
        if (this.type == 0)
            JOptionPane.showMessageDialog(null, this.text, this.title, JOptionPane.ERROR_MESSAGE);
        else if (this.type == 1)
            JOptionPane.showMessageDialog(null, this.text, this.title, JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, this.text, this.title, JOptionPane.WARNING_MESSAGE);
    }
    
    public int showMessageConfirm(){
        if (this.type == 0)
            return JOptionPane.showConfirmDialog(null, this.text, this.title, JOptionPane.ERROR_MESSAGE);
        else if (this.type == 1)
            return JOptionPane.showConfirmDialog(null, this.text, this.title, JOptionPane.INFORMATION_MESSAGE);
        else
            return JOptionPane.showConfirmDialog(null, this.text, this.title, JOptionPane.WARNING_MESSAGE);
    }
    
    public String showMessageOption(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
        JComboBox<String> dorpdown = new JComboBox<>(this.options);
        JLabel label = new JLabel(this.text);

        panel.add(label);
        panel.add(dorpdown);
        
        int result = JOptionPane.showConfirmDialog(frame, panel, this.title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION)
            return (String) dorpdown.getSelectedItem();
        else
            return null;
    }
    
    public String showMessageSelection(){
        int result = JOptionPane.showConfirmDialog(null, this.text, this.title, JOptionPane.INFORMATION_MESSAGE);

        if (result == JOptionPane.OK_OPTION)
            return "other";
        else if(result == JOptionPane.NO_OPTION)
            return "this";
        else
            return "";
    }
}
