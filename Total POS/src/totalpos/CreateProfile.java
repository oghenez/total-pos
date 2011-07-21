/*
 * CreateProfile.java
 *
 * Created on 15-jul-2011, 12:24:23
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 *
 * @author shidalgo
 */
public class CreateProfile extends javax.swing.JDialog {

    /** Creates new form CreateProfile */
    public CreateProfile(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        descriptionTextField = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Constants.appName);

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        titleLabel.setText("Crear Perfil");
        titleLabel.setName("titleLabel"); // NOI18N

        idLabel.setText("ID");
        idLabel.setName("idLabel"); // NOI18N

        idTextField.setName("idTextField"); // NOI18N
        idTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTextFieldActionPerformed(evt);
            }
        });
        idTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idTextFieldKeyPressed(evt);
            }
        });

        descriptionTextField.setName("descriptionTextField"); // NOI18N
        descriptionTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descriptionTextFieldActionPerformed(evt);
            }
        });
        descriptionTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descriptionTextFieldKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descriptionTextFieldKeyTyped(evt);
            }
        });

        descriptionLabel.setText("Descripción");
        descriptionLabel.setName("descriptionLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(idLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(idTextField)
                            .addComponent(descriptionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idTextFieldActionPerformed

    private void descriptionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionTextFieldActionPerformed
        
    }//GEN-LAST:event_descriptionTextFieldActionPerformed

    private void descriptionTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descriptionTextFieldKeyTyped
        
    }//GEN-LAST:event_descriptionTextFieldKeyTyped

    private void descriptionTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descriptionTextFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            try {
                if ( !idTextField.getText().matches("\\A[a-zA-Z0-9 ]+$") ){
                    throw new Exception("El ID es inválido. No se pueden utilizar caracteres especiales.");
                }

                ConnectionDrivers.createProfile(idTextField.getText(), descriptionTextField.getText());
                this.setVisible(false);
                MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado satisfactoriamente.");
                msb.show(this);
            } catch (SQLException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Error con la base de datos.",ex);
                msb.show(this);
            } catch (Exception ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Error al crear perfil.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_descriptionTextFieldKeyPressed

    private void idTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idTextFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_idTextFieldKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField descriptionTextField;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}
