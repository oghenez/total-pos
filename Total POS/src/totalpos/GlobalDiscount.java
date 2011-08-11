/*
 * GlobalDiscount.java
 *
 * Created on 10-ago-2011, 16:10:56
 */

package totalpos;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 *
 * @author shidalgo
 */
public class GlobalDiscount extends javax.swing.JDialog {

    public boolean isOk = false;
    private MainRetailWindows parent;

    /** Creates new form GlobalDiscount
     * @param parent
     * @param modal 
     */
    public GlobalDiscount(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent = (MainRetailWindows) parent;
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

        userLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        percentLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        percentField = new javax.swing.JTextField();
        percentLabelxD = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        userLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        userLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        userLabel.setText("Usuario    *");
        userLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        userLabel.setName("userLabel"); // NOI18N

        passwordLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        passwordLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        passwordLabel.setText("Contraseña *");
        passwordLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        passwordLabel.setName("passwordLabel"); // NOI18N

        percentLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        percentLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        percentLabel.setText("Descuento  *");
        percentLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        percentLabel.setName("percentLabel"); // NOI18N

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setText("Descuento Global");
        titleLabel.setName("titleLabel"); // NOI18N

        userField.setName("userField"); // NOI18N
        userField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                userFieldKeyPressed(evt);
            }
        });

        passwordField.setName("passwordField"); // NOI18N
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordFieldKeyPressed(evt);
            }
        });

        percentField.setName("percentField"); // NOI18N
        percentField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                percentFieldKeyPressed(evt);
            }
        });

        percentLabelxD.setText("%");
        percentLabelxD.setName("percentLabelxD"); // NOI18N

        cancelButton.setText("Cancelar");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        acceptButton.setText("Aceptar");
        acceptButton.setName("acceptButton"); // NOI18N
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
                    .addComponent(titleLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(percentLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(percentField, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(percentLabelxD))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userLabel)
                            .addComponent(passwordLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                            .addComponent(userField, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel)
                    .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(percentLabel)
                    .addComponent(percentField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(percentLabelxD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void userFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_userFieldKeyPressed

    private void passwordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_passwordFieldKeyPressed

    private void percentFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_percentFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_percentFieldKeyPressed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        if ( userField.getText().isEmpty() ){
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El usuario es obligatorio");
            msg.show(this);
            return;
        }else if ( percentField.getText().isEmpty() ) {
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El descuento no puede ser vacío");
            msg.show(this);
            return;
        }
        try{
            double p = Double.parseDouble(percentField.getText());
            if ( p >= 100.0 || p <= .0){
                throw new NumberFormatException("");
            }

            if ( !ConnectionDrivers.existsUser(userField.getText().trim()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Usuario no existe");
                msg.show(this);
                passwordField.setEnabled(true);
                return;
            }

            if ( ConnectionDrivers.isLocked(userField.getText().trim()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Usuario bloqueado");
                msg.show(this);
                passwordField.setEnabled(true);
                return;
            }

            ConnectionDrivers.login(userField.getText(), passwordField.getPassword());

            User u = Shared.giveUser(ConnectionDrivers.listUsers(), userField.getText());
            if ( ConnectionDrivers.isAllowed(u.getPerfil(), "globalDiscount") ){
                Shared.userInsertedPasswordOk(userField.getText());
                
            }

        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Problemas con la base de datos",ex);
            msg.show(this);
        } catch ( NumberFormatException ex ){
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El descuento es inválido");
            msg.show(this);
        } catch (Exception ex) {
            String kindErr = "";

            if ( Constants.wrongPasswordMsg.equals(ex.getMessage()) ) {
                kindErr = Constants.wrongPasswordMsg;
            }

            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, kindErr);
            msg.show(this);

            if ( ex.getMessage().equals(Constants.wrongPasswordMsg) ){
                try {
                    Shared.userTrying(userField.getText());
                } catch (Exception ex1) {
                    msg = new MessageBox(MessageBox.SGN_DANGER,
                                (ex1.getMessage().equals(Constants.userLocked)? Constants.userLocked :"Error."),
                                ex1);
                    msg.show(null);
                    this.dispose();
                    Shared.reload();
                }
            }

        }

    }//GEN-LAST:event_acceptButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField percentField;
    private javax.swing.JLabel percentLabel;
    private javax.swing.JLabel percentLabelxD;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField userField;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables

}
