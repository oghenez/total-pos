/*
 * ListTurnsForm.java
 *
 * Created on 02-ago-2011, 9:29:03
 */

package totalpos;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author shidalgo
 */
public class ListTurnsForm extends JInternalFrame {

    private List<Turn> turns;
    public boolean isOk = false;
    /** Creates new form ListTurnsForm */
    public ListTurnsForm() {
        initComponents();
        updateAll();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        newTurn = new javax.swing.JButton();
        changeTurn = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Turnos");
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Hora de Inicio", "Hora de Fin"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tableMouseMoved(evt);
            }
        });
        table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tableFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        newTurn.setText("Nuevo Turno");
        newTurn.setFocusable(false);
        newTurn.setName("newTurn"); // NOI18N
        newTurn.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                newTurnMouseMoved(evt);
            }
        });
        newTurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTurnActionPerformed(evt);
            }
        });

        changeTurn.setText("Modificar Turno");
        changeTurn.setFocusable(false);
        changeTurn.setName("changeTurn"); // NOI18N
        changeTurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeTurnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(changeTurn, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newTurn)
                    .addComponent(changeTurn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newTurnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTurnActionPerformed
        CreateTurn ct = new CreateTurn();
        getParent().add(ct);
        ct.setVisible(true);
        ct.requestFocus();
        updateAll();
    }//GEN-LAST:event_newTurnActionPerformed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_formMouseMoved

    private void tableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_tableMouseMoved

    private void newTurnMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newTurnMouseMoved
        Shared.getScreenSaver().actioned();
    }//GEN-LAST:event_newTurnMouseMoved

    private void tableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tableFocusGained
        updateAll();
    }//GEN-LAST:event_tableFocusGained

    private void changeTurnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeTurnActionPerformed
        if ( table.getSelectedRow() != -1 ){
            CreateTurn cp = new CreateTurn(turns.get(table.getSelectedRow()));
            this.getParent().add(cp);
            cp.setVisible(true);
        }else{
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "Debe seleccionar el turno");
            msg.show(this);
        }
    }//GEN-LAST:event_changeTurnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeTurn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newTurn;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    private void updateAll() {
        try {
            turns = ConnectionDrivers.listTurns();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            for (Turn p : turns) {
                Object[] s = {p.getIdentificador(), p.getNombre(), p.getInicio(), p.getFin()};
                model.addRow(s);
            }
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
            msg.show(this);
            this.dispose();
            Shared.reload();
        }
    }

}
