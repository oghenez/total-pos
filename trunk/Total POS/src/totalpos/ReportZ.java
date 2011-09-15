/*
 * ReportZ.java
 *
 * Created on 26-ago-2011, 15:00:06
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 *
 * @author Saúl Hidalgo
 */
public class ReportZ extends javax.swing.JDialog implements Doer{

    private String kindOfReport;
    MainRetailWindows myParent;
    private Working workingFrame;

    /** Creates new form ReportZ */
    public ReportZ(java.awt.Frame parent, boolean modal, String kind) {
        super(parent, modal);
        initComponents();
        kindOfReport = kind;
        myParent = (MainRetailWindows) parent;
        titleLabel.setText(titleLabel.getText() + " " + kind);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report Z");

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setText("Imprimir Reporte");
        titleLabel.setName("titleLabel"); // NOI18N

        jLabel1.setText("Usuario");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("Contraseña");
        jLabel2.setName("jLabel2"); // NOI18N

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addComponent(userField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passwordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            workingFrame = new Working(this);

            WaitSplash ws = new WaitSplash(this);

            Shared.centerFrame(workingFrame);
            workingFrame.setVisible(true);

            ws.execute();
        }else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_passwordFieldKeyPressed

    private void userFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userFieldKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_userFieldKeyPressed

    public void doIt(){
        if ( userField.getText().isEmpty() ){
            workingFrame.setVisible(false);
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El usuario es obligatorio");
            msg.show(this);
            return;
        }
        try{
            if ( !ConnectionDrivers.existsUser(userField.getText().trim()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Usuario no existe");
                msg.show(this);
                passwordField.setEnabled(true);
                return;
            }

            if ( ConnectionDrivers.isLocked(userField.getText().trim()) ){
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Usuario bloqueado");
                msg.show(this);
                passwordField.setEnabled(true);
                return;
            }

            ConnectionDrivers.login(userField.getText(), passwordField.getPassword());

            User u = Shared.giveUser(ConnectionDrivers.listUsers(), userField.getText());
            if ( ConnectionDrivers.isAllowed(u.getPerfil(), "report" + kindOfReport) ){
                myParent.printer.report(kindOfReport);
            }else{
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El usuario no tiene permisos para calcular el reporte " + kindOfReport);
                msg.show(this);
            }
            this.dispose();

        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Problemas con la base de datos",ex);
            msg.show(this);
        } catch ( NumberFormatException ex ){
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "La cantidad es inválida");
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
    }

    public void close(){
        workingFrame.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField passwordField;
    public javax.swing.JLabel titleLabel;
    private javax.swing.JTextField userField;
    // End of variables declaration//GEN-END:variables

}
