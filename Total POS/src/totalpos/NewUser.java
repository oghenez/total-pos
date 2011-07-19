/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewUser.java
 *
 * Created on 19-jul-2011, 9:44:40
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
public class NewUser extends javax.swing.JDialog {

    /** Creates new form NewUser */
    public NewUser(java.awt.Frame parent, boolean modal) {
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

        jLabel1 = new javax.swing.JLabel();
        loginText = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Introduzca el nuevo login");
        jLabel1.setName("jLabel1"); // NOI18N

        loginText.setName("loginText"); // NOI18N
        loginText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                loginTextKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(loginText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loginTextKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            try {
                if (loginText.getText().isEmpty()) {
                    MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "El login no puede ser vacío.");
                    msg.show(this);
                    return;
                }
                ConnectionDrivers.createUser(loginText.getText());
                MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Usuario creado satisfactoriamente.");
                msg.show(this);
                closeWindows();
            } catch (SQLException ex) {
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error con la base de datos.", ex);
                msg.show(this);
            }
        }else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            closeWindows();
        }
    }//GEN-LAST:event_loginTextKeyPressed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField loginText;
    // End of variables declaration//GEN-END:variables

    private void closeWindows() {
        this.setVisible(false);
        dispose();
    }

}
