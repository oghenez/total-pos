/*
 * ParameteringReport.java
 *
 * Created on Aug 29, 2011, 1:14:24 AM
 */

package totalpos;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Saul Hidalgo
 */
public class ParameteringReport extends javax.swing.JInternalFrame {

    private String title = null;
    private List<TextColumnBuilder> columns = new ArrayList<TextColumnBuilder>();
    private List<Column> columnsTD = new ArrayList<Column>();
    private List<Parameter> parameters = new ArrayList<Parameter>();
    private List<TextColumnBuilder> subtotals = new ArrayList<TextColumnBuilder>();
    private String groupBy = null;
    private boolean showNumbers = true;
    private String sql = null;
    private String fileAddr = "";
    public boolean isOk = false;
    public boolean vertical = false;
    JasperReportBuilder jrb;
    JRDataSource jrds;
    JasperViewer jv;
    public boolean empty = false;

    /** Creates new form ParameteringReport */
    public ParameteringReport(File f) {
        try {
            initComponents();
            fileAddr = f.getPath();
            parseFile();
            creatingParametersFields();
            isOk = true;
            if ( parameters.isEmpty() ){
                titleLabel.setText("No hay parámetros para especificar");
                empty = true;
                doIt();
            }
            titleLabel.setText(title);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParameteringReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseFile() throws FileNotFoundException{
        Scanner sc = new Scanner(new File(fileAddr));
        int curLine = 1;
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String[] tokens = line.split("==");
            if ( tokens.length != 2 ){
                System.err.println("Err parsing line " + curLine);
                continue;
            }
            if ( tokens[0].equals("Title") ){
                title = tokens[1];
            }else if ( tokens[0].equals("Columns") ){
                String[] subtoks = tokens[1].split("\\|");
                for (String subsubtok : subtoks) {
                    String[] comp = subsubtok.split("\\,");
                    if ( comp.length != 3 && comp.length != 4 && comp.length != 5){
                        System.err.println("Err parsing component \"" + subsubtok + "\"");
                        continue;
                    }
                    Column nc = new Column(comp[1], comp[0], comp[2]);
                    DRIDataType dridt = type.stringType();
                    if ( comp[2].equals("bigDecimalType") ){
                        dridt = type.bigDecimalType();
                    }
                    TextColumnBuilder tcb = col.column(nc.getFieldName(), nc.getName(), dridt);
                    columnsTD.add(nc);
                    //TODO Just read String
                    columns.add(tcb);
                    if ( comp.length >= 4 && comp[3].equals("1")){
                        subtotals.add(tcb);
                    }
                }
            }else if ( tokens[0].equals("ShowNumbers") ){
                showNumbers = tokens[1].equals("True");
            }else if ( tokens[0].equals("Vertical") ){
                vertical = tokens[1].equals("True");
            }else if ( tokens[0].equals("Parameters") ){
                String[] subtoks = tokens[1].split("\\|");
                for (String subsubtok : subtoks) {
                    String[] comp = subsubtok.split("\\,");
                    if ( comp.length != 3 ){
                        System.err.println("Err parsing component \"" + subsubtok + "\"");
                        continue;
                    }
                    parameters.add(new Parameter(comp[2], comp[0], comp[1],null,null));
                }
            }else if ( tokens[0].equals("SQL") ){
                sql = tokens[1];
            }else if ( tokens[0].equals("GroupBy") ){
                //TODO Decide what to do here
                groupBy = tokens[1];
            }
            ++curLine;
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

        titleLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Especifique los parametros");

        titleLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        titleLabel.setText("Especifique los parametros");
        titleLabel.setName("titleLabel"); // NOI18N

        mainPanel.setName("mainPanel"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 339, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 357, Short.MAX_VALUE)
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void doIt(){
        try {
            jrb = report();
            if ( vertical ){
                jrb = jrb.setPageFormat(PageType.LETTER, PageOrientation.LANDSCAPE);
            }else{
                jrb = jrb.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
            }
            jrb = jrb.setColumnTitleStyle(Constants.columnTitleStyle);
            for (Parameter parameter : parameters) {
                //TODO Check the syntaxis.
                if (parameter.getTextField().getText().isEmpty()) {
                    MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "No se pueden dejar parámetros vacíos.");
                    msg.show(this);
                    return;
                }
            }
            jrb = jrb.addColumn(createColumns(columns));
            jrb = jrb.setDataSource(ConnectionDrivers.createDataSource(parameters, sql, columnsTD));
            if ( title != null ){/*
                jrb = jrb.addTitle(cmp.horizontalList().add(
                        cmp.text(title).setStyle(Constants.titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT))
                        .newRow()
                        .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)));*/
                jrb = jrb.title(Templates.createTitleComponent(title));
            }
            if ( showNumbers ){
                jrb = jrb.pageFooter(cmp.pageXofY());
            }
            if ( !subtotals.isEmpty() ){
                for (int i = 0 ; i < subtotals.size() ; i++ ) {
                    jrb = jrb.subtotalsAtSummary((AggregationSubtotalBuilder<BigDecimal>)sbt.sum((TextColumnBuilder<BigDecimal>)subtotals.get(i)).setLabel("Total"));
                }
                //jrb = jrb.subtotalsAtFirstGroupFooter(semiSubTotal);
            }
            jrb = jrb.highlightDetailEvenRows();
            jv = new JasperViewer(jrb.toJasperPrint(), false);
            jv.setTitle(Constants.appName);
            jv.setVisible(true);
        } catch (DRException ex) {
            Logger.getLogger(ParameteringReport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ParameteringReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        doIt();
    }//GEN-LAST:event_acceptButtonActionPerformed

    private TextColumnBuilder[] createColumns(List<TextColumnBuilder> lc){
        TextColumnBuilder[] ans = new TextColumnBuilder[lc.size()];
        for (int i = 0; i < lc.size(); i++) {
            ans[i] = lc.get(i);
        }
        return ans;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    private void creatingParametersFields() {
        mainPanel.setLayout(new GridLayout(parameters.size(), 2));
        for (Parameter name : parameters) {
            JLabel label = new JLabel(name.getFormName());
            final JTextField textField = new JTextField();
            mainPanel.add(label);
            if ( name.getType().equals("Date") ){
                textField.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        ChooseDate cal = new ChooseDate(Constants.appName,textField);
                        ((MainWindows)Shared.getMyMainWindows()).mdiPanel.add(cal);
                        cal.setVisible(true);
                    }
                });
                textField.setEditable(false);
            }
            mainPanel.add(textField);
            name.setLabel(label);
            name.setTextField(textField);
        }
        setSize(450, 130+parameters.size()*22);
    }
}
