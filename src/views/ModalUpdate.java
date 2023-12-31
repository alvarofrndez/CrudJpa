/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author alvaro
 */
public class ModalUpdate extends javax.swing.JPanel {
    private List<JTextField> list_fields;

    /**
     * Creates new form ModalUpdate
     */
    public ModalUpdate(List<String> fields, String id, List<Object> values) {
        initComponents();
        start(fields, id, values);
    }
    
    public void start(List<String> fields, String id, List<Object> values){
        list_fields = new ArrayList<>();
        setLayout(new GridLayout(0, 2));

        for(int i = 0; i<fields.size(); i++){
            addField(fields.get(i), values.get(i + 1));
        }
    }
    
    public void addField(String field, Object value){
        if(!field.equals("familias") && !field.equals("clientes") && !field.equals("lineas") && !field.equals("fecha")){
            add(new JLabel(field));
            JTextField text_field = new JTextField();
            text_field.setText(String.valueOf(value));
            add(text_field);
            list_fields.add(text_field);
        }
    }
    
    public List<String> getValues() {
        List<String> values = new ArrayList<>();

        for (JTextField textField : list_fields) {
            values.add(textField.getText());
        }

        return values;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
