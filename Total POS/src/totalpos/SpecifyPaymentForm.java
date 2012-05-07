/*
 * SpecifyPaymentForm.java
 *
 * Created on 22-ago-2011, 10:38:01
 */

package totalpos;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Saúl Hidalgo
 */
public class SpecifyPaymentForm extends javax.swing.JDialog implements Doer{

    public Working workingFrame;
    public double total;
    private List<PayForm> payForms = new ArrayList<PayForm>();
    private String receiptID;
    private MainRetailWindows myParent;
    private Double sum;
    private Double change;
    
    /** Creates new form SpecifyPaymentForm */
    SpecifyPaymentForm(MainRetailWindows aThis, boolean b, double subtotal, String receipt) {
        super(aThis, b);
        initComponents();
        total = Math.round((new Price(null, subtotal).plusIva()).getQuant())+.0;
        receiptID = receipt;
        myParent = aThis;

        //payForms.add(new PayForm(receipt, "Efectivo", "", "", total.getQuant()));
        updateAll();
        table.requestFocus();
    }

    private void updateAll(){
        sum = .0;
        change = .0;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (PayForm payForm : payForms) {
            String[] s = {payForm.getFormWay(),Constants.df.format(payForm.getQuant()),payForm.getbPos(),payForm.getLot()};
            model.addRow(s);
            sum += payForm.getQuant();
        }
        if ( payForms.size() > 0 ){
            table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
        }
        change = sum-total;
        moneyResult.setText(Constants.df.format(sum));
        ChangeResult.setText(Constants.df.format(change));
        Shared.msgWithEffect("Tot. + IVA = " + Shared.format4Display(total), "Recibido = " + Shared.format4Display(sum));
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        moneyLabel = new javax.swing.JLabel();
        changeLabel = new javax.swing.JLabel();
        ChangeResult = new javax.swing.JLabel();
        moneyResult = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Formas de Pago");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Courier New", 1, 18));
        jLabel1.setText("Formas de Cobro");
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Forma de Pago", "Monto", "Id", "Lote"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        jLabel2.setFont(new java.awt.Font("Courier New", 1, 12));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel2.setText("E / Efectivo");
        jLabel2.setFocusable(false);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Courier New", 1, 12));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel3.setText("N / Nota de Crédito");
        jLabel3.setFocusable(false);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Courier New", 1, 12));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel4.setText("D / Débito");
        jLabel4.setFocusable(false);
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel5.setText("C / Crédito");
        jLabel5.setFocusable(false);
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel6.setText("Esc / Atras");
        jLabel6.setFocusable(false);
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel7.setText("Enter / OK");
        jLabel7.setFocusable(false);
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setFont(new java.awt.Font("Courier New", 1, 12));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas.jpg"))); // NOI18N
        jLabel8.setText("F12 / Eliminar");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel8.setName("jLabel8"); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setName("jPanel1"); // NOI18N

        moneyLabel.setBackground(new java.awt.Color(255, 255, 255));
        moneyLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        moneyLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        moneyLabel.setText("Dinero:");
        moneyLabel.setName("moneyLabel"); // NOI18N

        changeLabel.setBackground(new java.awt.Color(255, 255, 255));
        changeLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        changeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        changeLabel.setText("Cambio:");
        changeLabel.setName("changeLabel"); // NOI18N

        ChangeResult.setBackground(new java.awt.Color(255, 255, 255));
        ChangeResult.setFont(new java.awt.Font("Courier New", 1, 18));
        ChangeResult.setText("jLabel12");
        ChangeResult.setName("ChangeResult"); // NOI18N

        moneyResult.setBackground(new java.awt.Color(255, 255, 255));
        moneyResult.setFont(new java.awt.Font("Courier New", 1, 18));
        moneyResult.setText("jLabel11");
        moneyResult.setName("moneyResult"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(changeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChangeResult, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(moneyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moneyResult, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {changeLabel, moneyLabel});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(moneyLabel)
                    .addComponent(moneyResult, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChangeResult, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ChangeResult, changeLabel, moneyLabel, moneyResult});

        jLabel9.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/totalpos/resources/Etiquetas 2x.jpg"))); // NOI18N
        jLabel9.setText("A / American Express");
        jLabel9.setFocusable(false);
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel9.setName("jLabel9"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, 0, 438, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableKeyPressed
        if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE){
            this.dispose();
        }else if ( evt.getKeyCode() == KeyEvent.VK_F12 ){
            payForms.remove(table.getSelectedRow());
            updateAll();
        }else if ( evt.getKeyCode() == KeyEvent.VK_N ){
            AddMoney2Pay am2p = new AddMoney2Pay(this, true, Constants.CNPaymentName,total);
            Shared.centerFrame(am2p);
            am2p.setVisible(true);
        } else if ( evt.getKeyCode() == KeyEvent.VK_E ){
            AddMoney2Pay am2p = new AddMoney2Pay(this, true, Constants.cashPaymentName,total);
            Shared.centerFrame(am2p);
            am2p.setVisible(true);
        } else if ( evt.getKeyCode() == KeyEvent.VK_C ){
            AddCard2Pay ac2p = new AddCard2Pay(this, true, Constants.creditPaymentName,total);
            if ( ac2p.isOk ){
                Shared.centerFrame(ac2p);
                ac2p.setVisible(true);
            }
        } else if ( evt.getKeyCode() == KeyEvent.VK_D ){
            AddCard2Pay ac2p = new AddCard2Pay(this, true, Constants.debitPaymentName,total);
            if ( ac2p.isOk ){
                Shared.centerFrame(ac2p);
                ac2p.setVisible(true);
            }
        } else if ( evt.getKeyCode() == KeyEvent.VK_A ){
            AddCard2Pay ac2p = new AddCard2Pay(this, true, Constants.americanExpressPaymentName,total);
            if ( ac2p.isOk ){
                Shared.centerFrame(ac2p);
                ac2p.setVisible(true);
            }
        } else if ( evt.getKeyCode() == KeyEvent.VK_O || evt.getKeyCode() == KeyEvent.VK_ENTER){
            if ( change < 0 ){
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Monto insuficiente.");
                msb.show(Shared.getMyMainWindows());
            }else{
                workingFrame = new Working(this);
                
                WaitSplash ws = new WaitSplash(this);

                Shared.centerFrame(workingFrame);
                workingFrame.setVisible(true);

                ws.execute();
            }
        }
    }//GEN-LAST:event_tableKeyPressed

    public void close(){
        workingFrame.setVisible(false);
    }

    public void doIt(){
        try {
            if ( change < 0 ){
                MessageBox msb = new MessageBox(MessageBox.SGN_CAUTION, "Monto insuficiente.");
                msb.show(null);
            }else{
                Shared.msgWithEffect( "Cambio = " + Shared.format4Display(change), "Gracias :)!");
                myParent.printer.isReceipt = true;
                myParent.printer.printerSerial = null;
                if (!myParent.printer.checkPrinter()) {
                    MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "La impresora no coincide con la registrada en el sistema. No se puede continuar");
                    msb.show(null);
                    return;
                }

                myParent.print(payForms);
                ConnectionDrivers.updateLastReceipt(myParent.printer.lastReceipt);
                payForms.add(new PayForm(receiptID, Constants.cashPaymentName, "" , "", -1*change));
                ConnectionDrivers.savePayForm(payForms);
                this.dispose();
            }
        } catch (SQLException ex) {
            workingFrame.setVisible(false);
            MessageBox msb = new MessageBox(MessageBox.SGN_DANGER, "Error en la base de datos!",ex);
            msb.show(null);
            this.dispose();
            Shared.reload();
        } catch (FileNotFoundException ex) {
            workingFrame.setVisible(false);
            Shared.what2DoWithReceipt(myParent, ex);
            this.dispose();
            myParent.printer.forceClose();
        } catch (Exception ex) {
            workingFrame.setVisible(false);
            Shared.what2DoWithReceipt(myParent , ex);
            this.dispose();
            myParent.printer.forceClose();
        }
    }

    public void add(String reason , Double money){
        payForms.add(new PayForm(receiptID, reason, "" , "", money));
        updateAll();
    }

    public void add(String reason , Double money, BankPOS bpos){
        payForms.add(new PayForm(receiptID, reason, bpos.getId() , bpos.getLot(), money));
        updateAll();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ChangeResult;
    private javax.swing.JLabel changeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel moneyLabel;
    private javax.swing.JLabel moneyResult;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

}
