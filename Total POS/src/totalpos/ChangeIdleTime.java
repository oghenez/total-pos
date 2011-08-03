/*
 * ChangeIdleTime.java
 *
 * Created on 21-jul-2011, 12:53:57
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.JInternalFrame;


/**
 *
 * @author shidalgo
 */
public class ChangeIdleTime extends JInternalFrame {

    public boolean isOk = false;
    private double currentTime;
    /** Creates new form ChangeIdleTime
     * @param parent
     * @param modal
     */
    public ChangeIdleTime() {
        initComponents();

        String c = Shared.getConfig("idleTime");

        double t = 0;
        try{
            t = Double.valueOf(c);
        } catch ( NumberFormatException ex ){
            t = 0;
        }
        t /= 60*1000;

        currentTime = t;
        this.idleTimeTextField.setText( t + "" );

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
        idleTimeTextField = new javax.swing.JTextField();
        secondsLabel = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Cambiar tiempo de bloqueo");
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setText("Cambiar tiempo de autobloqueo");
        titleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        titleLabel.setName("titleLabel"); // NOI18N

        idleTimeTextField.setName("idleTimeTextField"); // NOI18N
        idleTimeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idleTimeTextFieldActionPerformed(evt);
            }
        });
        idleTimeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idleTimeTextFieldKeyPressed(evt);
            }
        });

        secondsLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        secondsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        secondsLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        secondsLabel.setText("Minutos");
        secondsLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        secondsLabel.setName("secondsLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(idleTimeTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(secondsLabel))
                    .addComponent(titleLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idleTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secondsLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idleTimeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idleTimeTextFieldKeyPressed
        Shared.getScreenSaver().actioned();
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            try{
                double tt = Double.valueOf(idleTimeTextField.getText());
                long t = (long) (tt * 60 * 1000);
                if ( t <= 0 || t >= 1000*60*1000){
                    MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "El tiempo debe ser un entero positivo menor a 1000.");
                    msb.show(Shared.getMyMainWindows());
                    this.idleTimeTextField.setText( currentTime + "" );
                }else{
                    try {
                        ConnectionDrivers.saveConfig("idleTime", t + "");
                        MessageBox msg = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado correctamente");
                        msg.show(this);
                        ConnectionDrivers.initializeConfig();
                        this.dispose();
                    } catch (SQLException ex) {
                        if ( ex.getMessage().matches(Constants.isDataRepeated) ){
                            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Perfil ya existente. Intente otro.");
                            msb.show(this);
                        }else{
                            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
                            msb.show(this);
                        }
                    } catch (Exception ex) {
                        MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, ex.getMessage(), ex);
                        msg.show(this);
                        Shared.reload();
                    }
                }
            } catch ( NumberFormatException ex ){
                MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "El tiempo debe tener un formato válido.",ex);
                msb.show(Shared.getMyMainWindows());
                this.idleTimeTextField.setText( currentTime + "" );
            }
        }
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_idleTimeTextFieldKeyPressed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_formMouseMoved

    private void idleTimeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idleTimeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idleTimeTextFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField idleTimeTextField;
    private javax.swing.JLabel secondsLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

}
