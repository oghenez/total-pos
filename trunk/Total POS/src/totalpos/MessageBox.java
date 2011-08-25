package totalpos;

import java.awt.*;
import javax.swing.*;

public class MessageBox {

    // SIGNAL_WORD'S
    public final static int SGN_DANGER = 0xFF000000;
    public final static int SGN_WARNING = 0xFE000000;
    public final static int SGN_CAUTION = 0xFD000000;
    public final static int SGN_NOTICE = 0xFC000000;
    public final static int SGN_IMPORTANT = 0xFA000000;
    public final static int SGN_SUCCESS = 0xFB000000;

    // ERROR_CLASS'ES
    public final static int CLS_GENERIC = 0x00000000;

    // ERROR_CODE'S

    // VARIABLES
    private int m_iMsgNumber; // = SIGNAL_WORD (0xFF000000) | ERROR_CLASS (0x00FF0000) | ERROR_CODE (0x0000FFFF)
    private String m_sHazard;
    private String m_sConsequences;
    private String m_sAvoiding;

    // CAUSE
    private Object m_eCause;

    /** Creates a new instance of MessageInf */
    public MessageBox(int iSignalWord, String sHazard, Object e) {
        m_iMsgNumber = iSignalWord | CLS_GENERIC;
        m_sHazard = sHazard;
        m_sConsequences = "";
        m_sAvoiding = "";
        m_eCause = e;
    }
    /** Creates a new instance of MessageInf */
    public MessageBox(int iSignalWord, String sHazard) {
        this (iSignalWord, sHazard, null);
    }

    public MessageBox(Throwable e) {
        this(SGN_WARNING, e.getLocalizedMessage(), e);
    }

    public void show(Component parent) {
        JMessageDialog.showMessage(parent, this);
    }

    public Object getCause() {
        return m_eCause;
    }

    public int getSignalWord() {
        return m_iMsgNumber & 0xFF000000;
    }

    public Icon getSignalWordIcon() {
        int iSignalWord = getSignalWord();
        if (iSignalWord == SGN_DANGER) {
            return UIManager.getIcon("OptionPane.errorIcon");
        } else if (iSignalWord == SGN_WARNING) {
            return UIManager.getIcon("OptionPane.warningIcon");
       } else if (iSignalWord == SGN_CAUTION) {
            return UIManager.getIcon("OptionPane.warningIcon");
        } else if (iSignalWord == SGN_NOTICE) {
            return UIManager.getIcon("OptionPane.informationIcon");
        } else if (iSignalWord == SGN_IMPORTANT) {
            return UIManager.getIcon("OptionPane.informationIcon");
        } else if (iSignalWord == SGN_SUCCESS) {
            return UIManager.getIcon("OptionPane.informationIcon");
        } else {
            return UIManager.getIcon("OptionPane.questionIcon");
        }
    }

    public String getErrorCodeMsg() {

        StringBuilder sb = new StringBuilder();
        int iSignalWord = getSignalWord();
        if (iSignalWord == SGN_DANGER) {
            sb.append("DNG_");
        } else if (iSignalWord == SGN_WARNING) {
            sb.append("WRN_");
        } else if (iSignalWord == SGN_CAUTION) {
            sb.append("CAU_");
        } else if (iSignalWord == SGN_NOTICE) {
            sb.append("NOT_");
        } else if (iSignalWord == SGN_IMPORTANT) {
            sb.append("IMP_");
        } else if (iSignalWord == SGN_SUCCESS) {
            sb.append("INF_");
        } else {
            sb.append("UNK_");
        }
        sb.append(toHex((m_iMsgNumber & 0x00FF0000) >> 16, 2));
        sb.append('_');
        sb.append(toHex(m_iMsgNumber & 0x0000FFFF, 4));
        return sb.toString();
    }

    private String toHex(int i, int iChars) {
        String s = Integer.toHexString(i);
        return s.length() >= iChars ? s : fillString(iChars - s.length()) + s;
    }

    private String fillString(int iChars) {
        char[] aStr = new char[iChars];
        for (int i = 0; i < aStr.length; i++) {
            aStr[i] = '0';
        }
        return new String(aStr);
    }


    public String getMessageMsg() {

        StringBuilder sb = new StringBuilder();
        int iSignalWord = getSignalWord();
        if (iSignalWord == SGN_DANGER) {
            sb.append("Error: ");
        } else if (iSignalWord == SGN_WARNING) {
            sb.append("Advertencia: ");
        } else if (iSignalWord == SGN_CAUTION) {
            sb.append("Error: ");
        } else if (iSignalWord == SGN_NOTICE) {
            sb.append("Noticia: ");
        } else if (iSignalWord == SGN_IMPORTANT) {
            sb.append("Importante: ");
        } else if (iSignalWord == SGN_SUCCESS) {
            sb.append("Éxito: ");
        } else {
            sb.append("Desconocido");
        }
        sb.append(m_sHazard);
        sb.append(m_sConsequences);
        sb.append(m_sAvoiding);
        return sb.toString();
    }
}
