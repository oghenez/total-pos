/*
 * ChangeProfileDetails.java
 *
 * Created on 20-jul-2011, 9:25:57
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shidalgo
 */
public class ChangeProfileDetails extends javax.swing.JDialog {

    private String prevId;
    /** Creates new form ChangeProfileDetails */
    public ChangeProfileDetails(java.awt.Frame parent, boolean modal, String prevId, String description) {
        super(parent, modal);
        initComponents();

        changeId.setText(prevId);
        changeDescription.setText(description);
        this.prevId = prevId;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        changeId = new javax.swing.JTextField();
        changeDescription = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Constants.appName);
        setMinimumSize(new java.awt.Dimension(400, 96));
        setResizable(false);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel1.setText("ID");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel2.setText("Descripción");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        changeId.setName("changeId"); // NOI18N
        changeId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                changeIdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                changeIdKeyReleased(evt);
            }
        });

        changeDescription.setName("changeDescription"); // NOI18N
        changeDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeDescriptionActionPerformed(evt);
            }
        });
        changeDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                changeDescriptionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                changeDescriptionKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Courier New", 1, 18));
        jLabel3.setText("Cambiar detalles");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(changeDescription)
                            .addComponent(changeId, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(changeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(changeDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void changeDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_changeDescriptionActionPerformed

    private void changeDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_changeDescriptionKeyReleased
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            try {
                if (changeDescription.getText().isEmpty()) {
                    MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "No puede ser vacío el ID del perfil.");
                    msb.show(this);
                    return;
                }
                ConnectionDrivers.changeProfileDetails(prevId, changeId.getText(), changeDescription.getText());
                MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Cambiado satisfactoriamente");
                msb.show(this);
                closeWindows();
            } catch (SQLException ex) {
                if ( ex.getMessage().matches(Constants.isDataRepeated) ){
                    MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Perfil ya existente. Intente otro.");
                    msb.show(this);
                }else{
                    MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
                    msb.show(this);
                }
            } catch (Exception ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, ex.getMessage(), ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            closeWindows();
        }
    }//GEN-LAST:event_changeDescriptionKeyReleased

    private void changeIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_changeIdKeyReleased
        Shared.getScreenSaver().actioned();
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            closeWindows();
        }
    }//GEN-LAST:event_changeIdKeyReleased

    private void changeIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_changeIdKeyPressed
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_changeIdKeyPressed

    private void changeDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_changeDescriptionKeyPressed
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_changeDescriptionKeyPressed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_formMouseMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField changeDescription;
    private javax.swing.JTextField changeId;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables

    private void closeWindows() {
        this.setVisible(false);
        dispose();
    }

}
