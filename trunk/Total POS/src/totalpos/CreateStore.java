/*
 * CreateStore.java
 *
 * Created on 03-ago-2011, 16:00:17
 */

package totalpos;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shidalgo
 */
public class CreateStore extends javax.swing.JInternalFrame {

    public boolean isOk = false;

    /** Creates new form CreateStore */
    public CreateStore() {
        initComponents();

        if ( !Shared.getConfig().containsKey("storeName") ){
            titleLabel.setText("Crear Tienda");
            this.setTitle("Crear Tienda");
        }else{
            titleLabel.setText("Modificar Tienda");
            this.setTitle("Modificar Tienda");

            nameFieldText.setText(Shared.getConfig("storeName"));
            descriptionFieldText.setText(Shared.getConfig("storeDescription"));
        }

        isOk = true;
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nameFieldText = new javax.swing.JTextField();
        descriptionFieldText = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();

        setClosable(true);
        setTitle("Crear/Modificar Tienda");
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setName("titleLabel"); // NOI18N

        jLabel2.setText("Código");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText("Descripción");
        jLabel3.setName("jLabel3"); // NOI18N

        nameFieldText.setName("nameFieldText"); // NOI18N
        nameFieldText.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                nameFieldTextMouseMoved(evt);
            }
        });

        descriptionFieldText.setName("descriptionFieldText"); // NOI18N
        descriptionFieldText.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                descriptionFieldTextMouseMoved(evt);
            }
        });

        cancelButton.setText("Cancelar");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                cancelButtonMouseMoved(evt);
            }
        });
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        acceptButton.setText("Aceptar");
        acceptButton.setName("acceptButton"); // NOI18N
        acceptButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                acceptButtonMouseMoved(evt);
            }
        });
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(descriptionFieldText)
                            .addComponent(nameFieldText, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(acceptButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameFieldText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionFieldText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        try {
            ConnectionDrivers.saveConfig("storeName", nameFieldText.getText());
            ConnectionDrivers.saveConfig("storeDescription", descriptionFieldText.getText());
            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado correctamente");
            msg.show(this);
            ConnectionDrivers.initializeConfig();
            this.dispose();
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
            msb.show(this);
        }
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_formMouseMoved

    private void descriptionFieldTextMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descriptionFieldTextMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_descriptionFieldTextMouseMoved

    private void nameFieldTextMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameFieldTextMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_nameFieldTextMouseMoved

    private void acceptButtonMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_acceptButtonMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_acceptButtonMouseMoved

    private void cancelButtonMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelButtonMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_cancelButtonMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField descriptionFieldText;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField nameFieldText;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}