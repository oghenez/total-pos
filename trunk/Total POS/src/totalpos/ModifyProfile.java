package totalpos;

/* (swing1.1) */

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.tree.*;


/**
 * @version 1.1 01/15/99
 */
public class ModifyProfile extends JDialog {

    String profile;
  
  public ModifyProfile(String profileId) {
    super(new JDialog(),true);

    this.profile = profileId;
    
    JTree tree = new JTree( exploreTree("/" , "root") );

    tree.addKeyListener(new KeyAdapter() {
            @Override
        public void keyReleased(java.awt.event.KeyEvent evt) {
            setVisible(false);
            dispose();
        }
    });

    tree.setCellRenderer(new CheckRenderer());
    tree.getSelectionModel().setSelectionMode(
      TreeSelectionModel.SINGLE_TREE_SELECTION
    );
    tree.putClientProperty("JTree.lineStyle", "Angled");
    tree.addMouseListener(new NodeSelectionListener(tree));
    JScrollPane sp = new JScrollPane(tree);

    this.setTitle("Ver/Modificar permisos de Perfil");
    this.setSize(500, 300);
    
    getContentPane().add(sp,    BorderLayout.CENTER);
  }

  class NodeSelectionListener extends MouseAdapter {
    JTree tree;
    
    NodeSelectionListener(JTree tree) {
      this.tree = tree;
    }
    
        @Override
    public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      int row = tree.getRowForLocation(x, y);
      TreePath  path = tree.getPathForRow(row);
      //TreePath  path = tree.getSelectionPath();
      if (path != null) {
        CheckNode node = (CheckNode)path.getLastPathComponent();
        boolean isSelected = ! (node.isSelected());
        node.setSelected(isSelected);
        if ( isSelected ) {
            //tree.expandPath(path);
        } else {
            //tree.collapsePath(path);
        }
        ((DefaultTreeModel)tree.getModel()).nodeChanged(node);
        tree.revalidate();
        tree.repaint();
      }
    }
  }

      private CheckNode exploreTree(String realName , String id){
        try {
            CheckNode ans = new CheckNode(realName + " (" + id + ") " , profile);

            for (Edge edge : ConnectionDrivers.listEdges(id)) {
                ans.add(exploreTree(edge.getNombre(),edge.getId()));
            }

            return ans;
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error", ex);
            msg.show(this);
            return null;
        } catch (Exception ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Error al listar menu.",ex);
            msb.show(this);
            return null;
        }
    }
}
