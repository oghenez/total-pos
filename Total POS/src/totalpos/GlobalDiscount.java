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
    private double subtotal;

    /** Creates new form GlobalDiscount
     * @param parent
     * @param modal 
     */
    public GlobalDiscount(Frame parent, boolean modal, double subT) {
        super(parent, modal);
        initComponents();
        this.parent = (MainRetailWindows) parent;
        subtotal = subT;
        calculate.setMnemonic('l');
        acceptButton.setMnemonic('a');
        cancelButton.setMnemonic('c');
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
        titleLabel = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        percentLabel1 = new javax.swing.JLabel();
        finalMoney = new javax.swing.JTextField();
        percentLabel = new javax.swing.JLabel();
        percentField = new javax.swing.JTextField();
        percentLabelxD1 = new javax.swing.JLabel();
        percentLabelxD = new javax.swing.JLabel();
        calculate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        userLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        userLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        userLabel.setText("Usuario");
        userLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        userLabel.setName("userLabel"); // NOI18N

        passwordLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        passwordLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        passwordLabel.setText("Contraseña");
        passwordLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        passwordLabel.setName("passwordLabel"); // NOI18N

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

        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        acceptButton.setText("Aceptar");
        acceptButton.setFocusable(false);
        acceptButton.setName("acceptButton"); // NOI18N
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("* = Obligatorio");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("*");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText("*");
        jLabel3.setName("jLabel3"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cantidad de Descuento *"));
        jPanel1.setName("jPanel1"); // NOI18N

        percentLabel1.setFont(new java.awt.Font("Courier New", 0, 12));
        percentLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        percentLabel1.setText("Monto Final");
        percentLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        percentLabel1.setName("percentLabel1"); // NOI18N

        finalMoney.setName("finalMoney"); // NOI18N
        finalMoney.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                finalMoneyKeyPressed(evt);
            }
        });

        percentLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        percentLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        percentLabel.setText("Descuento");
        percentLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        percentLabel.setName("percentLabel"); // NOI18N

        percentField.setName("percentField"); // NOI18N
        percentField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                percentFieldKeyPressed(evt);
            }
        });

        percentLabelxD1.setText("Bsf");
        percentLabelxD1.setName("percentLabelxD1"); // NOI18N

        percentLabelxD.setText("%");
        percentLabelxD.setName("percentLabelxD"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(percentLabel)
                    .addComponent(percentLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(percentField, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                    .addComponent(finalMoney, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(percentLabelxD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(percentLabelxD1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(percentField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(percentLabelxD)
                    .addComponent(percentLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(finalMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(percentLabel1)
                    .addComponent(percentLabelxD1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        calculate.setText("Calcular");
        calculate.setFocusable(false);
        calculate.setName("calculate"); // NOI18N
        calculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titleLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addComponent(calculate, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userLabel)
                            .addComponent(passwordLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                            .addComponent(userField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))))
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
                    .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(acceptButton)
                    .addComponent(jLabel1)
                    .addComponent(calculate))
                .addContainerGap())
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
        }else if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            doIt();
        }else{
            calculate();
        }
    }//GEN-LAST:event_percentFieldKeyPressed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        doIt();
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void calculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateActionPerformed
        calculate();
    }//GEN-LAST:event_calculateActionPerformed

    private void finalMoneyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_finalMoneyKeyPressed
        calculate();
    }//GEN-LAST:event_finalMoneyKeyPressed

    private void calculate(){
        try{
            if ( !percentField.getText().isEmpty() ){
                double p = Double.parseDouble(percentField.getText().replace(',', '.'));
                if ( (100-p)*subtotal/100.0 < 1.0 || p < .0){
                    MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Descuento incorrecto.");
                    msg.show(this);
                }else{
                    finalMoney.setText(Constants.df.format((new Price(null,(100-p)*subtotal/100.0)).plusIva().getQuant()));
                }
            }else if ( !finalMoney.getText().isEmpty() ){
                double p = Double.parseDouble(finalMoney.getText().replace(',', '.'));
                double totalPlusIva = new Price(null, subtotal).plusIva().getQuant();
                if ( p < 1.0 || p > totalPlusIva){
                    MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Descuento incorrecto.");
                    msg.show(this);
                }else{
                    percentField.setText(Constants.df.format((100.0-(p/totalPlusIva)*100.0)));
                }
            }else{
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Debe especificar el descuento.");
                msg.show(this);
            }
        }catch(NumberFormatException ex){
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Descuento incorrecto.");
            msg.show(this);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton calculate;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField finalMoney;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField percentField;
    private javax.swing.JLabel percentLabel;
    private javax.swing.JLabel percentLabel1;
    private javax.swing.JLabel percentLabelxD;
    private javax.swing.JLabel percentLabelxD1;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField userField;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables

    private void doIt() {
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
            double p = Double.parseDouble(percentField.getText().replace(',', '.'));
            if ( p >= 100.0 || p < .0){
                throw new NumberFormatException("");
            }

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
            if ( ConnectionDrivers.isAllowed(u.getPerfil(), "globalDiscount") ){
                Shared.userInsertedPasswordOk(userField.getText());
                parent.setGlobalDiscount(p/100.0);
            }else{
                MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "El usuario no tiene permisos para hacer el descuento global");
                msg.show(this);
            }
            this.dispose();

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
    }

}
