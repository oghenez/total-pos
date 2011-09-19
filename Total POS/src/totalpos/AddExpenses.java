/*
 * AddExpenses.java
 *
 * Created on 26-ago-2011, 16:33:20
 */

package totalpos;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Saúl Hidalgo
 */
public class AddExpenses extends javax.swing.JInternalFrame {

    List<Expense> expenses;
    public boolean isOk = false;

    /** Creates new form AddExpenses */
    public AddExpenses() {
        try {
            initComponents();
            updateAll();
            isOk = true;
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    private void updateAll() throws SQLException{
        expenses = ConnectionDrivers.listExpensesToday();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String ex = Shared.getConfig("expenses");
        String allConcepts = ex.substring(1, ex.length()-1);
        Scanner sc = new Scanner(allConcepts);
        sc.useDelimiter("\\}\\{");

        JComboBox jcb = new JComboBox();
        while(sc.hasNext()){
            jcb.addItem(sc.next());
        }
        
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click para ver las opciones");
        TableColumn conceptColumn = table.getColumnModel().getColumn(0);
        conceptColumn.setCellRenderer(renderer);
        conceptColumn.setCellEditor(new DefaultCellEditor(jcb));

        for (Expense e : expenses) {
            String[] s = {e.getConcept(),Constants.df.format(e.getQuant()),e.getDescription()};
            model.addRow(s);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        acceptButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Gastos");

        acceptButton.setText("Guardar");
        acceptButton.setFocusable(false);
        acceptButton.setName("acceptButton"); // NOI18N
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cerrar");
        cancelButton.setFocusable(false);
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        addButton.setText("Agregar");
        addButton.setFocusable(false);
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Eliminar");
        deleteButton.setFocusable(false);
        deleteButton.setName("deleteButton"); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Concepto", "Monto", "Descripción"
            }
        ));
        table.setName("table"); // NOI18N
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(20);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);

        jLabel1.setFont(new java.awt.Font("Courier New", 1, 18));
        jLabel1.setText("Gastos de hoy");
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acceptButton, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(addButton)
                    .addComponent(acceptButton)
                    .addComponent(deleteButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(model.getRowCount()+1);
    }//GEN-LAST:event_addButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int n = table.getSelectedRow();
        if ( n != -1 ){
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(n);
        }else{
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Debe seleccionar un gasto!");
            msg.show(this);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            try{
                if ( model.getValueAt(i, 0) == null || model.getValueAt(i, 1) == null ||
                        ((String)model.getValueAt(i, 0)).isEmpty() || ((String)model.getValueAt(i, 1)).isEmpty()){
                    MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Todos los campos son obligatorios. No pueden haber gastos con campos vacíos.");
                    msg.show(this);
                    return;
                }
                Double m = Double.parseDouble(((String) model.getValueAt(i, 1)).replace(',', '.'));
                if ( m <= .0 ){
                    throw new NumberFormatException();
                }
            }catch (NumberFormatException ex){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El monto es inválido. Debe ser positivo");
                msg.show(this);
                return;
            }
        }
        try {
            ConnectionDrivers.deleteAllExpensesToday();
            ConnectionDrivers.createExpensesToday(model);
            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado correctamente");
            msg.show(this);
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }//GEN-LAST:event_acceptButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

}
