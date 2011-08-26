/*
 * ManageBank.java
 *
 * Created on 22-ago-2011, 14:31:13
 */

package totalpos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Saúl Hidalgo
 */
public class ManageBank extends JInternalFrame {

    public boolean isOk = false;
    List<BankPOS> bpos = new ArrayList<BankPOS>();

    /** Creates new form ManageBank */
    public ManageBank() {
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
        DefaultTableModel model = (DefaultTableModel) bposTable.getModel();
        model.setRowCount(0);
        bpos = ConnectionDrivers.listBPos();
        for (BankPOS bankPOS : bpos) {
            String[] s = {bankPOS.getId(), bankPOS.getDescripcion(), bankPOS.getLot(), bankPOS.getPosId() , bankPOS.getKind()};
            model.addRow(s);
        }
        JComboBox comboBox = new JComboBox();
        for (String kbpos : Constants.kindOfBPOS) {
           comboBox.addItem(kbpos);
        }
        TableColumn kbposColumn = bposTable.getColumnModel().getColumn(4);
        kbposColumn.setCellEditor(new DefaultCellEditor(comboBox));
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click para ver las opciones");
        kbposColumn.setCellRenderer(renderer);

        

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        bposTable = new javax.swing.JTable();
        deleteButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Punto de Venta de Banco");

        title.setFont(new java.awt.Font("Tahoma", 1, 24));
        title.setText("Punto de Venta de Banco");
        title.setName("title"); // NOI18N

        cancelButton.setText("Cerrar");
        cancelButton.setFocusable(false);
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Guardar");
        saveButton.setFocusable(false);
        saveButton.setName("saveButton"); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        bposTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Descripción", "Lote", "Caja", "Tipo"
            }
        ));
        bposTable.setName("bposTable"); // NOI18N
        bposTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(bposTable);

        deleteButton.setText("Eliminar");
        deleteButton.setFocusable(false);
        deleteButton.setName("deleteButton"); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(saveButton)
                    .addComponent(deleteButton)
                    .addComponent(addButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) bposTable.getModel();
        model.setNumRows(model.getRowCount()+1);
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int n = bposTable.getSelectedRow();
        if ( n != -1){
            DefaultTableModel model = (DefaultTableModel) bposTable.getModel();
            model.removeRow(bposTable.getSelectedRow());
        }else{
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Debe seleccionar un punto de venta de banco!");
            msg.show(this);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) bposTable.getModel();
        Set<String> s = new TreeSet<String>();
        for (int i = 0; i < model.getRowCount(); i++) {
            if ( model.getValueAt(i, 0) == null || model.getValueAt(i, 2) == null ||
                    ((String)model.getValueAt(i, 0)).isEmpty() || ((String)model.getValueAt(i, 2)).isEmpty()){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Todos los campos son obligatorios. No pueden haber puntos de ventas con campos vacíos.");
                msg.show(this);
                return;
            }
            if ( s.contains(model.getValueAt(i, 0)) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Los códigos no se pueden repetir");
                msg.show(this);
                return;
            }
            s.add((String) model.getValueAt(i, 0));
        }
        try {
            ConnectionDrivers.deleteAllBPos();
            ConnectionDrivers.createBPOS(model);
            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado correctamente");
            msg.show(this);
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTable bposTable;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

}
