package totalpos;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.List;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Saul Hidalgo
 */
public class Shared {


    protected static void centerFrame(javax.swing.JFrame frame){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        frame.setLocation(x, y);
    }

    protected static void maximize(JFrame frame){
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    protected static String hashPassword(String x){
        return x.hashCode() + "";
    }

    public static void centerFrame(JDialog dialog) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = dialog.getSize().width;
        int h = dialog.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        dialog.setLocation(x, y);
    }

    public static String booleanToAllowed(boolean b){
        return b?"Habilitado":"Deshabilitado";
    }

    public static User giveUser(List<User> l, String u){
        for (User user : l)
            if ( user.getLogin().equals(u) )
                return user;

        return null;
        
    }

    public static List<Profile> updateProfiles(JComboBox rCombo, boolean withEmpty) throws SQLException, Exception {

        List<Profile> profiles = ConnectionDrivers.listProfile("");

        DefaultComboBoxModel dfcbm = (DefaultComboBoxModel) rCombo.getModel();
        dfcbm.removeAllElements();

        if (withEmpty){
            profiles.add(0, new Profile("", ""));
        }

        for (Profile profile : profiles) {
            rCombo.addItem(profile.getId());
        }

        return profiles;
    }

    public static void userTrying(String l) throws Exception{
        TreeMap<String,Integer> tries = Login.tries;
        if ( !tries.containsKey(l) ){
            tries.put(l, new Integer(1));
        }else if ( tries.get(l).compareTo(new Integer(1)) > 0 ){
            try {
                ConnectionDrivers.lockUser(l);
            } catch (SQLException ex1) {
                MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Error con la base de datos.", ex1);
                msg.show(null);
            }
            throw new Exception(Constants.userLocked);
        }else{
            tries.put(l, new Integer(tries.get(l) + 1));
        }
    }

    public static void userInsertedPasswordOk(String username){
        TreeMap<String,Integer> tries = Login.tries;
        tries.put(username, 0);
    }

}