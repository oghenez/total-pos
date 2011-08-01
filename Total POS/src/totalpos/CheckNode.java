package totalpos;

/*  (swing1.1) */

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.tree.*;


/**
 * @version 1.0 01/11/99
 */
public class CheckNode extends DefaultMutableTreeNode {

  public final static int SINGLE_SELECTION = 0;
  public final static int DIG_IN_SELECTION = 4;
  protected int selectionMode;
  protected boolean isSelected;
  private String profile;
  public boolean isOk = false;

  public CheckNode(String profile) {
    this(null,profile);
  }

  public CheckNode(Object userObject, String profile) {
    this(userObject, true, profile);
  }

  public CheckNode(Object userObject, boolean allowsChildren
                                    , String profile) {
    super(userObject, allowsChildren);
    try {
        String name = ConnectionDrivers.getIdProfile(this.toString());
        this.isSelected = ConnectionDrivers.isAllowed(profile, name);
        isOk = true;
    } catch (SQLException ex) {
        MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
        msg.show(null);
    } catch (Exception ex) {
        MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al listar permisos de perfil.",ex);
        msb.show(MainWindows.mw);
        Shared.reload();
    }
    setSelectionMode(DIG_IN_SELECTION);
    this.profile = profile;

  }


  public void setSelectionMode(int mode) {
    selectionMode = mode;
  }

  public int getSelectionMode() {
    return selectionMode;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;

    // DO NOT MODIFY!! IT WORKS OK!!
    try {
        String name = ConnectionDrivers.getIdProfile(this.toString());
        if ( isSelected ){
            ConnectionDrivers.disableMenuProfile(profile, name);
            ConnectionDrivers.enableMenuProfile(profile, name);
        }else{
            ConnectionDrivers.disableMenuProfile(profile, name);
        }
    } catch (SQLException ex) {
        MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos.", ex);
        msg.show(new JDialog());
    } catch (Exception ex) {
        MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problemas al cambiar permisos de perfil.",ex);
        msb.show(null);
        Shared.reload();
    }
    

    if ((selectionMode == DIG_IN_SELECTION)
        && (children != null)) {

        for (Object o : children) {
            CheckNode cn = (CheckNode) o;
            cn.setSelected(isSelected);
        }
    }
  }

  public boolean isSelected() {
    return isSelected;
  }

}

