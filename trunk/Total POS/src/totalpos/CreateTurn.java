package totalpos;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import javax.swing.JInternalFrame;

/**
 *
 * @author Saul Hidalgo
 */
public class CreateTurn extends JInternalFrame {

    private List<User> modelUserList;
    private List<String> listPos;
    public boolean isOk = false;
    private boolean modify = false;

    /** Creates new form CreateTurn
     * @param parent
     * @param modal
     */
    public CreateTurn() {
        initComponents();
        fillForms();
    }

    public CreateTurn(Turn t) {
        initComponents();
        fillForms();
        idField.setText(t.getIdentificador());
        nameField.setText(t.getNombre()==null?"":t.getNombre());
        hourInit.select(t.getInicio().getHours()%12);
        minuteInit2.select(t.getInicio().getMinutes());
        secondInit2.select(t.getInicio().getSeconds());
        amOrPm2.select(t.getInicio().getHours() > 12 ? 1 : 0);

        hourEnd2.select(t.getFin().getHours()%12);
        minuteEnd2.select(t.getFin().getMinutes());
        secondEnd2.select(t.getFin().getSeconds());
        amOrPmEnd2.select(t.getFin().getHours() > 12 ? 1 : 0);

        modify = true;

        titleLabel.setText("Modificar Turno");
        setTitle("Modificar Turno");
        idField.setEditable(false);
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
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cashLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        idField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        hourInit = new java.awt.Choice();
        minuteInit2 = new java.awt.Choice();
        secondInit2 = new java.awt.Choice();
        amOrPm2 = new java.awt.Choice();
        hourEnd2 = new java.awt.Choice();
        minuteEnd2 = new java.awt.Choice();
        secondEnd2 = new java.awt.Choice();
        amOrPmEnd2 = new java.awt.Choice();

        setClosable(true);
        setIconifiable(true);
        setTitle("Crear Turno");
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 24));
        titleLabel.setText("Crear Turno");
        titleLabel.setName("titleLabel"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel2.setText("Identificador *");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel3.setText("Nombre");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N

        cancelButton.setFont(new java.awt.Font("Courier New", 0, 12));
        cancelButton.setText("Cancelar");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setFont(new java.awt.Font("Courier New", 0, 12));
        okButton.setText("Aceptar");
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cashLabel.setFont(new java.awt.Font("Courier New", 0, 12));
        cashLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        cashLabel.setText("Hora Inicial  *");
        cashLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cashLabel.setName("cashLabel"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Courier New", 0, 12));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel4.setText("Hora Final    *");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(":");
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(":");
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(":");
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(":");
        jLabel8.setName("jLabel8"); // NOI18N

        nameField.setName("nameField"); // NOI18N
        nameField.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                nameFieldMouseMoved(evt);
            }
        });
        nameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameFieldKeyPressed(evt);
            }
        });

        idField.setName("idField"); // NOI18N
        idField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idFieldKeyPressed(evt);
            }
        });

        jLabel11.setText("* = Campo Obligatorio");
        jLabel11.setName("jLabel11"); // NOI18N

        hourInit.setName("hourInit"); // NOI18N

        minuteInit2.setName("minuteInit2"); // NOI18N

        secondInit2.setName("secondInit2"); // NOI18N

        amOrPm2.setName("amOrPm2"); // NOI18N

        hourEnd2.setName("hourEnd2"); // NOI18N

        minuteEnd2.setName("minuteEnd2"); // NOI18N

        secondEnd2.setName("secondEnd2"); // NOI18N

        amOrPmEnd2.setName("amOrPmEnd2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addGap(280, 280, 280))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cashLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(hourInit, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(minuteInit2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(hourEnd2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(minuteEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(secondInit2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(secondEnd2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(amOrPmEnd2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(amOrPm2, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)))
                            .addComponent(nameField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(idField, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                        .addGap(76, 76, 76)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(secondInit2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(amOrPm2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minuteInit2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cashLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(hourInit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hourEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(minuteEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amOrPmEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(secondEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton)
                    .addComponent(jLabel11))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doIt();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_formMouseMoved

    private void idFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idFieldKeyPressed
        Shared.getScreenSaver().actioned();
        if ( idField.getText().length() > 20 ){
            idField.setText(idField.getText().substring(0, 20));
        }
    }//GEN-LAST:event_idFieldKeyPressed

    private void nameFieldMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameFieldMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_nameFieldMouseMoved

    private void nameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameFieldKeyPressed
        Shared.getScreenSaver().actioned();
        if ( nameField.getText().length() > 20 ){
            nameField.setText(nameField.getText().substring(0, 20));
        }
    }//GEN-LAST:event_nameFieldKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Choice amOrPm2;
    private java.awt.Choice amOrPmEnd2;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel cashLabel;
    private java.awt.Choice hourEnd2;
    private java.awt.Choice hourInit;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private java.awt.Choice minuteEnd2;
    private java.awt.Choice minuteInit2;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton okButton;
    private java.awt.Choice secondEnd2;
    private java.awt.Choice secondInit2;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    private void doIt() {
        try {
            if (idField.getText().isEmpty()) {
                MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "El Identificador no puede ser vacío.");
                msb.show(this);
                return;
            }
            Time a = new Time(amOrPm2.getSelectedIndex() * 12 + hourInit.getSelectedIndex(), minuteInit2.getSelectedIndex(), secondInit2.getSelectedIndex());
            Time b = new Time(amOrPmEnd2.getSelectedIndex() * 12 + hourEnd2.getSelectedIndex(), minuteEnd2.getSelectedIndex(), secondEnd2.getSelectedIndex());
            if (a.after(b)) {
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "El tiempo final debe ser luego del tiempo inicial");
                msb.show(this);
                return;
            }

            if ( modify ){
                ConnectionDrivers.modifyTurn(idField.getText(), nameField.getText(), a, b);
            }else{
                ConnectionDrivers.createTurn(idField.getText(), nameField.getText(), a, b);
            }

            MessageBox msb = new MessageBox(MessageBox.SGN_SUCCESS, "Guardado satisfactoriamente");
            msb.show(this);
            setVisible(false);
            dispose();
        } catch (SQLException ex) {
            if ( ex.getMessage().matches(Constants.isDataRepeated) ){
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Turno ya existente. Intente otro.");
                msb.show(this);
            }else{
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas con la base de datos.",ex);
                msb.show(this);
                this.dispose();
                Shared.reload();
            }
        }
        
    }

    private void fillForms() {
        hourEnd2.add("12");
        hourInit.add("12");

        for (int i = 1; i < 12; i++) {
            hourEnd2.add(i + "");
            hourInit.add(i + "");
        }
        for (int i = 0; i < 60; i++) {
            minuteInit2.add(i+"");
            minuteEnd2.add(i+"");
        }
        for (int i = 0; i < 60; i++) {
            secondEnd2.add(i+"");
            secondInit2.add(i+"");
        }

        amOrPm2.addItem("am");
        amOrPm2.addItem("pm");

        amOrPmEnd2.addItem("am");
        amOrPmEnd2.addItem("pm");
    }

}
