package tech.zuosi.bookmarkmanager;


import tech.zuosi.bookmarkmanager.panel.TabPane;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by iwar on 2016/3/4.
 */
public class UIpanel {
    private JFrame jFrame;
    private Point point;
    private JLabel jLabel;
    private Font topLabelFont,buttonFont;
    private JButton addBookmark;

    public static void main(String[] args) {
        new UIpanel();
    }

    public UIpanel() {
        init();

        addBookmark.setFont(buttonFont);
        addBookmark.setBounds(2,500,200,50);
        jLabel.setFont(topLabelFont);
        jLabel.setBackground(Color.GREEN);
        jLabel.setBounds(50,20,750,50);
        jFrame.setBackground(Color.WHITE);
        jFrame.setLocation(point);
        jFrame.add(new TabPane());
        jFrame.pack();
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    private void init() {
        jFrame = new JFrame("书签管理器");
        point = new Point(300,200);
        jLabel = new JLabel("书签内容包括url、编号、标题与内容",JLabel.CENTER);
        topLabelFont = new Font("Serief",Font.BOLD + Font.ITALIC,40);
        buttonFont = new Font("Serief",Font.PLAIN,22);
        addBookmark = new JButton("添加书签");
    }
}
