package totalpos;

import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Saúl Hidalgo
 */
public class UpdateClock extends Thread{

    private long lastOperationTime;

    public UpdateClock() {
        lastOperationTime = Calendar.getInstance().getTimeInMillis();
    }

    public void actioned(){
        lastOperationTime = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void run(){
        int checking = 1;

        // This delay might some an unknown problem. =(
        try {
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problema desconocido",ex);
            msb.show(null);
            Shared.reload();
        }

        while(Shared.getUser() != null ){
            ++checking;

            if ( !Shared.isOffline && checking % Constants.secondsToChangeMsg2Pos == 0
                    && Shared.getMyMainWindows() instanceof MainRetailWindows ){
                MainRetailWindows m = (MainRetailWindows) Shared.getMyMainWindows();

                if ( m.msg2pos == null ) break; //little patch. List has not been loaded yet!

                m.setMsgIndex( (m.getMsgIndex()+1) % m.msg2pos.size());
                m.updateMsg();
            }

            if ( !Shared.isOffline && checking % Constants.secondsToShiftMsg == 0 && Shared.getMyMainWindows() instanceof MainRetailWindows ){
                MainRetailWindows m = (MainRetailWindows) Shared.getMyMainWindows();
                m.increaseShiftValue();
            }

            if ( !Shared.isOffline && checking % Constants.secondsToCheckTurn == 0 && Shared.getMyMainWindows() instanceof MainRetailWindows ){
                try {
                    List<Assign> as = ConnectionDrivers.listAssignsTurnPosRightNow();
                    boolean toContinue = false;
                    for (Assign assign : as) {
                        if (assign.getPos().equals(Constants.myId) && assign.isOpen()) {
                            toContinue = true;
                            break; // for performance ...  =D!
                        }
                    }
                    if (!toContinue) {
                        MessageBox msg = new MessageBox(MessageBox.SGN_IMPORTANT, "El turno de venta se ha vencido.");
                        msg.show(null);
                        Shared.reload();
                        break;
                    }
                } catch (SQLException ex) {
                    MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos");
                    msg.show(null);
                    Shared.reload();
                    break;
                }
            }

            if ( !Shared.isOffline && checking % Constants.secondsToUpdateCountdown == 0 && Shared.getMyMainWindows() instanceof MainRetailWindows ){
                try {
                    List<Assign> as = ConnectionDrivers.listAssignsTurnPosRightNow();
                    for (Assign assign : as) {
                        if (assign.getPos().equals(Constants.myId) && assign.isOpen()) {
                            Turn cur = Shared.getTurn(ConnectionDrivers.listTurns(), assign.getTurn());
                            Time diff = ConnectionDrivers.getDiff( cur.getFin() );
                            int leaving = (diff.getMinutes()+diff.getHours()*60);
                            if ( leaving < 5 ){
                                MainRetailWindows mrw = (MainRetailWindows) Shared.getMyMainWindows();
                                if ( leaving > 0 ){
                                    mrw.yourTurnIsFinishingLabel.setText("Su turno se va a vencer en " + leaving + " minuto" + (leaving>1?"s":"") + ".");
                                }else{
                                    mrw.yourTurnIsFinishingLabel.setText("Su turno se va a vencer en pocos segundos.");
                                }
                                mrw.yourTurnIsFinishingLabel.setVisible(true);
                            }
                            break; // for performance ...  =D!
                        }
                    }
                } catch (SQLException ex) {
                    MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos",ex);
                    msg.show(null);
                    Shared.reload();
                    break;
                } catch (ParseException ex){
                    MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos",ex);
                    msg.show(null);
                    Shared.reload();
                    break;
                }
            }

            if ( !Shared.isOffline && checking % Constants.secondsToUpdateMirror == 0 ){
                if ( !Shared.isOffline ){
                    try {
                        for (String table : Constants.tablesToMirrorAtDay) {
                            ConnectionDrivers.mirrorTable(table);
                        }
                    } catch (Exception ex) {
                        MessageBox msg = new MessageBox(MessageBox.SGN_DANGER, "Problemas con la base de datos");
                        msg.show(null);
                        Shared.reload();
                        break;
                    }
                }

            }

            if ( Calendar.getInstance().getTimeInMillis() - lastOperationTime > Long.valueOf(Shared.getConfig("idleTime"))){

                MessageBox msg = new MessageBox(MessageBox.SGN_WARNING, "El sistema ha permanecido mucho tiempo sin uso. Requiere contraseña.");
                msg.show(Shared.getMyMainWindows());
                PasswordNeeded pn = new PasswordNeeded((JFrame)Shared.getMyMainWindows(), true, Shared.getUser());
                Shared.centerFrame(pn);
                pn.setVisible(true);
                if ( pn.isPasswordOk() ){
                    lastOperationTime = Calendar.getInstance().getTimeInMillis();
                }else{
                    Shared.reload();
                    break;
                }
            }

            Thread thisThread = Thread.currentThread();

            try {
                thisThread.sleep(1000);
            } catch (InterruptedException ex) {
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Problema desconocido",ex);
                msb.show(null);
                Shared.reload();
                break;
            }

            Date d = new Date(Calendar.getInstance().getTimeInMillis());

            if ( Shared.getMyMainWindows() instanceof MainWindows ){
                MainWindows m = (MainWindows)Shared.getMyMainWindows();
                m.whatTimeIsIt.setText(Constants.sdfDateHour.format(d));
            } else if ( Shared.getMyMainWindows() instanceof MainRetailWindows ){
                MainRetailWindows m = (MainRetailWindows)Shared.getMyMainWindows();
                m.whatTimeIsIt.setText(Constants.sdfDateHour.format(d));
            }

        }
    }

}
