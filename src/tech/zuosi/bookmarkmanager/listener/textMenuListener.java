package tech.zuosi.bookmarkmanager.listener;

import tech.zuosi.bookmarkmanager.operator.PanelOperator;
import tech.zuosi.bookmarkmanager.panel.MainPanel;
import tech.zuosi.bookmarkmanager.type.ModeType;
import tech.zuosi.bookmarkmanager.util.TextUtil;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by iwar on 2016/3/19.
 */
public class TextMenuListener extends MouseAdapter implements CaretListener {
    //private boolean isEditMode;
    private JTextArea textArea;
    private int line;

    public TextMenuListener(JTextArea textArea) {
        this.textArea = textArea;
        //this.isEditMode = false;
    }

    //public boolean isEditMode() {
        //return isEditMode;
    //}

    //public void setEditMode(boolean editMode) {
        //isEditMode = editMode;
    //}

    public void caretUpdate(CaretEvent ce) {
        if (textArea.getText().trim().length() == 0)
            return;
        int offset = ce.getDot();
        try {
            line = textArea.getLineOfOffset(offset);
        } catch (BadLocationException be) {
            be.printStackTrace();
        }
    }

    //public String selectedTitle() {}

    @Override
    public void mouseClicked(MouseEvent me) {
        if (ModeType.LIST == MainPanel.currentMode) {
            String tarTitle;
            if (me.getClickCount() == 2) {
                String str = textArea.getText()==null?"":textArea.getText();
                String[] strArray = str.split("\n");
                if (line >= strArray.length) {
                    tarTitle = "";
                } else {
                    tarTitle = strArray[line].trim().split("\\.")[1];
                }
                TextUtil.text = tarTitle;
                new PanelOperator().loadBMI();
            }
        }
    }
}
