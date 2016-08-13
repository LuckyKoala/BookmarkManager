package tech.zuosi.bookmarkmanager.listener;

import tech.zuosi.bookmarkmanager.SelectedText;
import tech.zuosi.bookmarkmanager.util.DataManager;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by iwar on 2016/3/19.
 */
public class textMenuListener extends MouseAdapter implements CaretListener {
    private boolean isEditMode;
    private JTextArea textArea;
    private int line;

    public textMenuListener(JTextArea textArea) {
        this.textArea = textArea;
        this.isEditMode = false;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

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
        if (isEditMode) {
            String tarTitle;
            if (me.getClickCount() == 2) {
                String str = textArea.getText();
                String[] strArray = str.split("\n");
                tarTitle = strArray[line].trim().split("\\.")[1];  //To Skip Index
                SelectedText.text = tarTitle;
            }
            // FIXME 暂时无法导出为可执行jar
        }
    }
}
