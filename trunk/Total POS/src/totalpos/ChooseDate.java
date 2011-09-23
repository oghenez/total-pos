/*
 * ChooseDate.java
 *
 * Created on 01-sep-2011, 13:25:55
 */

package totalpos;

import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JTextField;

/**
 *
 * @author Saúl Hidalgo
 */
public class ChooseDate extends javax.swing.JInternalFrame {

    /** Creates new form ChooseDate */
    public ChooseDate(String title, JTextField jtf, boolean isClosingDay) {
        initComponents();
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(new Cal(jtf,this,isClosingDay));
        setTitle(title);
        pack();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setClosable(true);
        setIconifiable(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
