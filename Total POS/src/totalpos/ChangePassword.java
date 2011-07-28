/*
 * ChangePassword.java
 *
 * Created on 19-jul-2011, 8:56:00
 */

package totalpos;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saúl Hidalgo
 */
public class ChangePassword extends javax.swing.JDialog {

    private User user;
    private Login parent;

    /** Creates new form ChangePassword */
    public ChangePassword(Login parent, boolean modal , User u) {
        super(parent, modal);
        initComponents();

        this.user = u;
        this.parent = parent;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        newPasswordText = new javax.swing.JPasswordField();
        newPasswordText2 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Constants.appName);

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel2.setText("Contraseña Nueva");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel3.setText("Contraseña nueva (Repetición)");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N

        newPasswordText.setName("newPasswordText"); // NOI18N

        newPasswordText2.setName("newPasswordText2"); // NOI18N
        newPasswordText2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPasswordText2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(newPasswordText2, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(newPasswordText, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(newPasswordText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(newPasswordText2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newPasswordText2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPasswordText2ActionPerformed
        changePasswordNow();
    }//GEN-LAST:event_newPasswordText2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField newPasswordText;
    private javax.swing.JPasswordField newPasswordText2;
    // End of variables declaration//GEN-END:variables

    private void changePasswordNow() {
        try {
            if (!newPasswordText.getText().trim().equals(newPasswordText2.getText().trim())) {
                MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "Las contraseñas no son iguales.");
                msb.show(this);
                return;
            }
            ConnectionDrivers.setPassword(user.getLogin(), Shared.hashPassword(newPasswordText.getText()));
            ConnectionDrivers.mustntChangePassword(user.getLogin());
            parent.userChangedHerPass = true;
            MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Contraseña cambiada satisfactoriamente");
            msg.show(this);
            this.setVisible(false);
            dispose();
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, ex.getMessage() ,ex);
            msb.show(this);
        }
    }

}
