/*
 * CreateProfile.java
 *
 * Created on 15-jul-2011, 12:24:23
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.JInternalFrame;

/**
 *
 * @author shidalgo
 */
public class CreateProfile extends JInternalFrame {

    /** Creates new form CreateProfile */
    public CreateProfile() {
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

        setClosable(true);
        setIconifiable(true);
        setTitle("Crear Perfil");
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setText("Crear Perfil");
        titleLabel.setName("titleLabel"); // NOI18N

        idLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        idLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        idLabel.setText("ID");
        idLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                idTextFieldKeyReleased(evt);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                descriptionTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descriptionTextFieldKeyTyped(evt);
            }
        });

        descriptionLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        descriptionLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        descriptionLabel.setText("Descripción");
        descriptionLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(idLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                            .addComponent(idTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))))
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
                    .addComponent(descriptionLabel)
                    .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        Shared.getScreenSaver().actioned();
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
                if ( ex.getMessage().matches(Constants.isDataRepeated) ){
                    MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Perfil ya existente. Intente otro.");
                    msb.show(this);
                }else{
                    MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
                    msb.show(this);
                }
            } catch (Exception ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al crear perfil.",ex);
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
        Shared.getScreenSaver().actioned();
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_idTextFieldKeyPressed

    private void idTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idTextFieldKeyReleased
        if ( idTextField.getText().length() > 20 ){
            idTextField.setText(idTextField.getText().substring(0, 20));
        }
    }//GEN-LAST:event_idTextFieldKeyReleased

    private void descriptionTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descriptionTextFieldKeyReleased
        if ( descriptionTextField.getText().length() > 200 ){
            descriptionTextField.setText(descriptionTextField.getText().substring(0, 200));
        }
    }//GEN-LAST:event_descriptionTextFieldKeyReleased

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_formMouseMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField descriptionTextField;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}
