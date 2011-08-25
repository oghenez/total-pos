/*
 * CreditNoteForm.java
 *
 * Created on 23-ago-2011, 8:44:40
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Saúl Hidalgo
 */
public class CreditNoteForm extends javax.swing.JDialog {

    private Receipt receipt;
    private String actualId;
    private MainRetailWindows myParent;
    public boolean isOk = false;

    /** Creates new form CreditNoteForm
     * @param parent
     * @param modal
     * @param r 
     */
    public CreditNoteForm(MainRetailWindows parent, boolean modal, Receipt r) {
        super(parent, modal);
        initComponents();
        try {
            receipt = r;
            myParent = parent;
            internIdField.setText(receipt.getInternId());
            fiscalNumberField.setText(receipt.getFiscalNumber());
            fiscalPrinterField.setText(receipt.getFiscalPrinter());
            totalField.setText(Constants.df.format(receipt.getTotalWithIva()));
            zReportField.setText(receipt.getzReportId());
            dateField.setText(Constants.sdfDateHour.format(receipt.getCreationDate()));
            List<Client> l = ConnectionDrivers.listClients(receipt.getClientId());

            // Client MUST be registred
            assert( l.size() >= 1);
            Client c = l.get(0);
            clientIDField.setText(c.getId());
            clientDescriptionField.setText(c.getAddress());
            clientNameField.setText(c.getName());
            clientPhoneField.setText(c.getPhone());
            updateAll();
            isOk = true;
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
        }
    }

    public void updateAll(){
        //TODO Set status in a Constant File
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        if ( !receipt.getItems().isEmpty() && receipt.getStatus().equals("Facturada")){
            actualId = receipt.getInternId();
            model = (DefaultTableModel) table.getModel();

            for (Item2Receipt item2r : receipt.getItems()) {
                Item item = item2r.getItem();
                Object[] s = {false,item.getDescription(), item.getDescuento()+"", item.getLastPrice().toString(), item.getLastPrice().getIva().toString(), item.getLastPrice().plusIva().toString()};
                model.addRow(s);
            }
            table.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
        }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        codeLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        clientIDField = new javax.swing.JTextField();
        clientNameField = new javax.swing.JTextField();
        clientPhoneField = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();
        clientDescription = new javax.swing.JScrollPane();
        clientDescriptionField = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        internCode = new javax.swing.JLabel();
        internIdField = new javax.swing.JTextField();
        fiscalPrinter = new javax.swing.JLabel();
        fiscalPrinterField = new javax.swing.JTextField();
        fiscalNumber = new javax.swing.JLabel();
        fiscalNumberField = new javax.swing.JTextField();
        zReportField = new javax.swing.JTextField();
        zReport = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        zReport1 = new javax.swing.JLabel();
        totalField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nota de Crédito");

        jLabel1.setFont(new java.awt.Font("Courier New", 1, 18));
        jLabel1.setText("Crear Nota de Crédito");
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Devolver", "Cantidad", "Descripción", "Descuento", "Precio", "IVA", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);

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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));
        jPanel1.setFocusable(false);
        jPanel1.setName("jPanel1"); // NOI18N

        codeLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        codeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        codeLabel.setText("Código");
        codeLabel.setFocusable(false);
        codeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        codeLabel.setName("codeLabel"); // NOI18N

        nameLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        nameLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        nameLabel.setText("Nombre");
        nameLabel.setFocusable(false);
        nameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nameLabel.setName("nameLabel"); // NOI18N

        phoneLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        phoneLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        phoneLabel.setText("Teléfono");
        phoneLabel.setFocusable(false);
        phoneLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        phoneLabel.setName("phoneLabel"); // NOI18N

        clientIDField.setEditable(false);
        clientIDField.setText("jTextField1");
        clientIDField.setFocusable(false);
        clientIDField.setName("clientIDField"); // NOI18N

        clientNameField.setEditable(false);
        clientNameField.setText("jTextField2");
        clientNameField.setFocusable(false);
        clientNameField.setName("clientNameField"); // NOI18N

        clientPhoneField.setEditable(false);
        clientPhoneField.setText("jTextField3");
        clientPhoneField.setFocusable(false);
        clientPhoneField.setName("clientPhoneField"); // NOI18N

        descriptionLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 3x.jpg"))); // NOI18N
        descriptionLabel.setText("Dirección");
        descriptionLabel.setFocusable(false);
        descriptionLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setName("descriptionLabel"); // NOI18N

        clientDescription.setName("clientDescription"); // NOI18N

        clientDescriptionField.setColumns(20);
        clientDescriptionField.setEditable(false);
        clientDescriptionField.setRows(2);
        clientDescriptionField.setFocusable(false);
        clientDescriptionField.setName("clientDescriptionField"); // NOI18N
        clientDescription.setViewportView(clientDescriptionField);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(phoneLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(codeLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(clientPhoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    .addComponent(clientNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    .addComponent(clientIDField, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clientDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(codeLabel)
                    .addComponent(clientIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameLabel)
                            .addComponent(clientNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(phoneLabel)
                            .addComponent(clientPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(clientDescription, 0, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de Factura"));
        jPanel2.setFocusable(false);
        jPanel2.setName("jPanel2"); // NOI18N

        internCode.setFont(new java.awt.Font("Courier New", 1, 12));
        internCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        internCode.setText("Correlativo");
        internCode.setFocusable(false);
        internCode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        internCode.setName("internCode"); // NOI18N

        internIdField.setEditable(false);
        internIdField.setText("jTextField1");
        internIdField.setFocusable(false);
        internIdField.setName("internIdField"); // NOI18N

        fiscalPrinter.setFont(new java.awt.Font("Courier New", 1, 12));
        fiscalPrinter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        fiscalPrinter.setText("Impresora");
        fiscalPrinter.setFocusable(false);
        fiscalPrinter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fiscalPrinter.setName("fiscalPrinter"); // NOI18N

        fiscalPrinterField.setEditable(false);
        fiscalPrinterField.setText("jTextField1");
        fiscalPrinterField.setFocusable(false);
        fiscalPrinterField.setName("fiscalPrinterField"); // NOI18N

        fiscalNumber.setFont(new java.awt.Font("Courier New", 1, 12));
        fiscalNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        fiscalNumber.setText("Nro Fiscal");
        fiscalNumber.setFocusable(false);
        fiscalNumber.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fiscalNumber.setName("fiscalNumber"); // NOI18N

        fiscalNumberField.setEditable(false);
        fiscalNumberField.setText("jTextField1");
        fiscalNumberField.setFocusable(false);
        fiscalNumberField.setName("fiscalNumberField"); // NOI18N

        zReportField.setEditable(false);
        zReportField.setText("jTextField1");
        zReportField.setFocusable(false);
        zReportField.setName("zReportField"); // NOI18N

        zReport.setFont(new java.awt.Font("Courier New", 1, 12));
        zReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        zReport.setText("Reporte Z");
        zReport.setFocusable(false);
        zReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zReport.setName("zReport"); // NOI18N

        dateLabel.setFont(new java.awt.Font("Courier New", 1, 12));
        dateLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        dateLabel.setText("Momento");
        dateLabel.setFocusable(false);
        dateLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        dateLabel.setName("dateLabel"); // NOI18N

        dateField.setEditable(false);
        dateField.setText("jTextField1");
        dateField.setFocusable(false);
        dateField.setName("dateField"); // NOI18N

        zReport1.setFont(new java.awt.Font("Courier New", 1, 12));
        zReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        zReport1.setText("Total");
        zReport1.setFocusable(false);
        zReport1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zReport1.setName("zReport1"); // NOI18N

        totalField.setEditable(false);
        totalField.setText("jTextField1");
        totalField.setFocusable(false);
        totalField.setName("totalField"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(fiscalNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(internCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(internIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fiscalNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(fiscalPrinter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(zReport1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(zReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(totalField, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(fiscalPrinterField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(zReportField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateField, fiscalNumberField, internIdField});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(internCode)
                            .addComponent(internIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fiscalPrinter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(dateLabel)
                                        .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(zReport1)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fiscalNumber)
                                .addComponent(fiscalNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(fiscalPrinterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zReportField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zReport))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
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
        doIt();
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ){
            doIt();
        } else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ){
            this.dispose();
        }
    }//GEN-LAST:event_tableKeyPressed

    private String nextId(){
        try {
            int rightNow = ConnectionDrivers.lastCreditNoteToday();

            Date d = ConnectionDrivers.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(d) + Constants.myId + String.format("%04d", rightNow);
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
            return "";
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Problemas al listar calcular el siguiente código de factura.",ex);
            msb.show(this);
            this.dispose();
            Shared.reload();
            return "";
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane clientDescription;
    private javax.swing.JTextArea clientDescriptionField;
    private javax.swing.JTextField clientIDField;
    private javax.swing.JTextField clientNameField;
    private javax.swing.JTextField clientPhoneField;
    private javax.swing.JLabel codeLabel;
    private javax.swing.JTextField dateField;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel fiscalNumber;
    private javax.swing.JTextField fiscalNumberField;
    private javax.swing.JLabel fiscalPrinter;
    private javax.swing.JTextField fiscalPrinterField;
    private javax.swing.JLabel internCode;
    private javax.swing.JTextField internIdField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JTable table;
    private javax.swing.JTextField totalField;
    private javax.swing.JLabel zReport;
    private javax.swing.JLabel zReport1;
    private javax.swing.JTextField zReportField;
    // End of variables declaration//GEN-END:variables

    private void doIt() {

        try {
            List<Item> items = new ArrayList<Item>();
            for (int i = 0; i < table.getRowCount(); ++i) {
                if ((Boolean) table.getValueAt(i, 0)) {
                    items.add(receipt.getItems().get(i).getItem());
                }
            }
            if (items.isEmpty()) {
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Debe seleccionar al menos un (1) artículo para devolver.");
                msb.show(this);
                return;
            }
            myParent.printer.printerSerial = null;
            if (!myParent.printer.checkPrinter()) {
                MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "La impresora no coincide con la registrada en el sistema. No se puede continuar");
                msb.show(null);
                return;
            }
            actualId = nextId();
            ConnectionDrivers.createCreditNote(actualId, receipt.getInternId(), myParent.getUser().getLogin(), myParent.getAssign(), items);
            myParent.printer.printCreditNote(items, receipt.getInternId(), actualId, myParent.getUser());
            this.dispose();
        } catch (SQLException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Problemas con la base de datos",ex);
            msb.show(this);
            myParent.dispose();
            Shared.reload();
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Problemas al imprimir",ex);
            msb.show(this);
            myParent.dispose();
            Shared.reload();
        } 
    }

}
