package totalpos;

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author  adrian
 */
public class JMessageDialog extends javax.swing.JDialog {
    
    /** Creates new form JMessageDialog */
    private JMessageDialog(java.awt.Frame parent, boolean modal) {        
        super(parent, modal);       
    }
    /** Creates new form JMessageDialog */
    private JMessageDialog(java.awt.Dialog parent, boolean modal) {        
        super(parent, modal);       
    }
    
    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }
    
    public static void showMessage(Component parent, MessageBox inf) {
        
        Window window = getWindow(parent);

        JMessageDialog myMsg;
        if (window instanceof Frame) { 
            myMsg = new JMessageDialog((Frame) window, true);
        } else {
            myMsg = new JMessageDialog((Dialog) window, true);
        }

        myMsg.initComponents();
        myMsg.setVisibleCmdMore(inf.getCause() != null);
//        myMsg.applyComponentOrientation(parent.getComponentOrientation());
        myMsg.jscrException.setVisible(false);        
        myMsg.getRootPane().setDefaultButton(myMsg.jcmdOK);
        
        myMsg.jlblIcon.setIcon(inf.getSignalWordIcon());
        myMsg.jlblMessage.setText("<html>" + inf.getMessageMsg());
        if ( inf.getCause() != null ){
            String txt = ((Exception)inf.getCause()).getMessage();
            if ( txt.length() < 15 ){
                myMsg.mainMsgLabel.setFont(Constants.font24);
            }else if ( txt.length() < 30 ){
                myMsg.mainMsgLabel.setFont(Constants.font15);
            }else{
                myMsg.mainMsgLabel.setFont(Constants.font12);
            }
            myMsg.mainMsgLabel.setText("<html>" + txt + "</html>");
        }
        
        // Capturamos el texto de la excepcion...
        if (inf.getCause() == null) {
            myMsg.jtxtException.setText(null);
        } else {            
            StringBuffer sb = new StringBuffer(); 
            
            if (inf.getCause() instanceof Throwable) {
                Throwable t = (Throwable) inf.getCause();
                while (t != null) {
                    sb.append(t.getClass().getName());
                    sb.append(": \n");
                    sb.append(t.getMessage());
                    sb.append("\n\n");
                    t = t.getCause();
                }
            } else if (inf.getCause() instanceof Throwable[]) {
                Throwable[] m_aExceptions = (Throwable[]) inf.getCause();
                for (int i = 0; i < m_aExceptions.length; i++) {
                    sb.append(m_aExceptions[i].getClass().getName());
                    sb.append(": \n");
                    sb.append(m_aExceptions[i].getMessage());
                    sb.append("\n\n");
                }             
            } else if (inf.getCause() instanceof Object[]) {
                Object [] m_aObjects = (Object []) inf.getCause();
                for (int i = 0; i < m_aObjects.length; i++) {
                    sb.append(m_aObjects[i].toString());
                    sb.append("\n\n");
                }             
            } else if (inf.getCause() instanceof String) {
                sb.append(inf.getCause().toString());
            } else {
                sb.append(inf.getCause().getClass().getName());
                sb.append(": \n");
                sb.append(inf.getCause().toString());
            }
            // TODO HERE
            
            for (StackTraceElement object : ((Exception)inf.getCause()).getStackTrace()) {
                sb.append(object.getMethodName() + " @ " + object.getLineNumber()+ " @ " + object.getFileName() + "\n");
            }
            myMsg.jtxtException.setText(sb.toString());  
        }       
        myMsg.jtxtException.setCaretPosition(0);            
        
        //myMsg.show();
        myMsg.jcmdOK.requestFocus();
        myMsg.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        mainMsgLabel = new javax.swing.JLabel();
        jlblMessage = new javax.swing.JLabel();
        jscrException = new javax.swing.JScrollPane();
        jtxtException = new javax.swing.JTextArea();
        jlblIcon = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jcmdMore = new javax.swing.JButton();

        setTitle(Constants.appName);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel4MouseMoved(evt);
            }
        });
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        mainMsgLabel.setFont(new java.awt.Font("Courier New", 1, 24));
        jPanel4.add(mainMsgLabel);

        jlblMessage.setFont(new java.awt.Font("Courier New", 0, 14));
        jlblMessage.setText("jlblMessage");
        jlblMessage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jlblMessage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jlblMessage.setMinimumSize(new java.awt.Dimension(200, 100));
        jlblMessage.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanel4.add(jlblMessage);

        jscrException.setAlignmentX(0.0F);

        jtxtException.setEditable(false);
        jscrException.setViewportView(jtxtException);

        jPanel4.add(jscrException);

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        jlblIcon.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jlblIcon.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(jlblIcon, java.awt.BorderLayout.LINE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jcmdOK.setText("OK");
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdOK);

        jcmdMore.setText("Informacion"); // NOI18N
        jcmdMore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdMoreActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdMore);

        jPanel3.add(jPanel2, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-481)/2, (screenSize.height-163)/2, 481, 163);
    }// </editor-fold>//GEN-END:initComponents

    private void jcmdMoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdMoreActionPerformed
        
        // Add your handling code here:
        jcmdMore.setEnabled(false);
        jscrException.setVisible(true);
        setSize(getWidth(), 310);
        validateTree();
        
    }//GEN-LAST:event_jcmdMoreActionPerformed

    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdOKActionPerformed
        // Add your handling code here:
        setVisible(false);
        dispose();
    }//GEN-LAST:event_jcmdOKActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void jPanel4MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseMoved
        
    }//GEN-LAST:event_jPanel4MouseMoved

    private void setVisibleCmdMore(boolean b){
        jcmdMore.setVisible(b);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jcmdMore;
    private javax.swing.JButton jcmdOK;
    private javax.swing.JLabel jlblIcon;
    private javax.swing.JLabel jlblMessage;
    private javax.swing.JScrollPane jscrException;
    private javax.swing.JTextArea jtxtException;
    private javax.swing.JLabel mainMsgLabel;
    // End of variables declaration//GEN-END:variables
    
}
