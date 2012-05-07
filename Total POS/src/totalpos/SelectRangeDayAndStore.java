/*
 * SelectRangeDayAndStore.java
 *
 * Created on 08-mar-2012, 14:43:10
 */

package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class SelectRangeDayAndStore extends javax.swing.JInternalFrame {

    boolean isCestatickets;

    /** Creates new form SelectRangeDayAndStore */
    public SelectRangeDayAndStore(boolean cesta) {
        initComponents();
        isCestatickets = cesta;
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
        jLabel4 = new javax.swing.JLabel();
        seeButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        fromTextButton = new javax.swing.JTextField();
        untilText = new javax.swing.JTextField();
        departamentoText = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Elegir Tienda y Fechas");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel2.setText("Desde");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel3.setText("Hasta");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel4.setText("Departamento");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setName("jLabel4"); // NOI18N

        seeButton.setText("Ver");
        seeButton.setName("seeButton"); // NOI18N
        seeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seeButtonActionPerformed(evt);
            }
        });

        closeButton.setText("Cerrar");
        closeButton.setName("closeButton"); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        fromTextButton.setEditable(false);
        fromTextButton.setName("fromTextButton"); // NOI18N
        fromTextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fromTextButtonMouseClicked(evt);
            }
        });

        untilText.setEditable(false);
        untilText.setName("untilText"); // NOI18N
        untilText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                untilTextMouseClicked(evt);
            }
        });

        departamentoText.setName("departamentoText"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(seeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(untilText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(fromTextButton, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(departamentoText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fromTextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(untilText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(departamentoText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(seeButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void fromTextButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fromTextButtonMouseClicked
        ChooseDate cal = new ChooseDate(Constants.appName,fromTextButton,0);
        ((MainWindows)Shared.getMyMainWindows()).mdiPanel.add(cal);
        cal.setVisible(true);
    }//GEN-LAST:event_fromTextButtonMouseClicked

    private void untilTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_untilTextMouseClicked
        ChooseDate cal = new ChooseDate(Constants.appName,untilText,0);
        ((MainWindows)Shared.getMyMainWindows()).mdiPanel.add(cal);
        cal.setVisible(true);
    }//GEN-LAST:event_untilTextMouseClicked

    private void seeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seeButtonActionPerformed
        if ( fromTextButton.getText().isEmpty() || untilText.getText().isEmpty() ){
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Todos los campos son obligatorios");
            msg.show(null);
        }else{
            AnalizePresence ap = new AnalizePresence("%"+ departamentoText.getText() +"%",fromTextButton.getText(), untilText.getText(), isCestatickets);
            if ( ap.isOk ){
                ((MainWindows)Shared.getMyMainWindows()).mdiPanel.add(ap);
                ap.setVisible(true);
            }
            this.dispose();
        }
    }//GEN-LAST:event_seeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JTextField departamentoText;
    private javax.swing.JTextField fromTextButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton seeButton;
    private javax.swing.JTextField untilText;
    // End of variables declaration//GEN-END:variables

}
