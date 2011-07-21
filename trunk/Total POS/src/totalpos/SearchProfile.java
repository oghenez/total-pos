/*
 * SearchProfile.java
 *
 * Created on 15-jul-2011, 16:27:12
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author shidalgo
 */
public class SearchProfile extends javax.swing.JDialog {

    public boolean isOk = false;

    /** Creates new form SearchProfile */
    public SearchProfile(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        update();
    }

    private void update(){
        try {
            List<Profile> profiles = ConnectionDrivers.listProfile(searchTextField.getText());
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            model.setRowCount(0);

            for (Profile p : profiles) {
                String s[] = {p.getId() , p.getDescription()};
                model.addRow(s);
            }

            isOk = true;
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Error con la base de datos.",ex);
            msb.show(this);
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Error al listar perfiles.",ex);
            msb.show(this);
            Shared.reload();
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

        searchTitle = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        changePermits = new javax.swing.JButton();
        changeDetails = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Constants.appName);

        searchTitle.setFont(new java.awt.Font("Tahoma", 1, 18));
        searchTitle.setText("Buscar Perfil");
        searchTitle.setName("searchTitle"); // NOI18N

        searchTextField.setName("searchTextField"); // NOI18N
        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });
        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyReleased(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "id", "Descripcion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        changePermits.setText("Permisos");
        changePermits.setName("changePermits"); // NOI18N
        changePermits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePermitsActionPerformed(evt);
            }
        });

        changeDetails.setText("Detalles");
        changeDetails.setName("changeDetails"); // NOI18N
        changeDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeDetailsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                    .addComponent(searchTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                    .addComponent(searchTitle, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(changeDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(changePermits, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changePermits)
                    .addComponent(changeDetails))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextFieldActionPerformed

    private void searchTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.setVisible(false);
            dispose();
            return;
        }

        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
            update();
    }//GEN-LAST:event_searchTextFieldKeyPressed

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            closeWindows();
        }
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void tableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableKeyReleased
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.setVisible(false);
            dispose();
            return;
        }

        if ( evt.getKeyCode() == KeyEvent.VK_SPACE ){
            showThisProfile((String)table.getValueAt(table.getSelectedRow(),0));
        }
    }//GEN-LAST:event_tableKeyReleased

    private void changePermitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePermitsActionPerformed
        if ( table.getSelectedRow() != -1 ){
            showThisProfile((String)table.getValueAt(table.getSelectedRow(),0));
        }else{
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Debe seleccionar un perfil.");
            msb.show(this);
        }
    }//GEN-LAST:event_changePermitsActionPerformed

    private void changeDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeDetailsActionPerformed
        if ( table.getSelectedRow() != -1 ){
            ChangeProfileDetails cpd = new ChangeProfileDetails(null, true, (String)table.getValueAt(table.getSelectedRow(),0), (String)table.getValueAt(table.getSelectedRow(),1));
            Shared.centerFrame(cpd);
            cpd.setVisible(true);
            update();
        }else{
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Debe seleccionar un perfil.");
            msb.show(this);
        }
        
    }//GEN-LAST:event_changeDetailsActionPerformed

    private void showThisProfile(String p){
        ModifyProfile cnte = new ModifyProfile(p);
        Shared.centerFrame(cnte);
        cnte.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeDetails;
    private javax.swing.JButton changePermits;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JLabel searchTitle;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    private void closeWindows() {
        this.setVisible(false);
        dispose();
    }

}
