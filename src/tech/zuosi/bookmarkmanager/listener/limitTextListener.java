package tech.zuosi.bookmarkmanager.listener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Administrator on 2016/3/21.
 */
public class limitTextListener implements ActionListener {
    private int textLimit;
    private JTextField jTextField;
    private JTextArea jTextArea;
    public limitTextListener(JTextArea textArea,int limit) {
        this.jTextArea = textArea;
        this.textLimit = limit;
    }
    public limitTextListener(JTextField textField,int limit) {
        this.jTextField = textField;
        this.textLimit = limit;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (null == jTextArea && null != jTextField) {
            //
        } else if (null == jTextField && null!= jTextArea) {
            //
        } else {
            return;
        }
    }
}
