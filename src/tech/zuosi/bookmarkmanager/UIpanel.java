package tech.zuosi.bookmarkmanager;


import tech.zuosi.bookmarkmanager.panel.tabPane;
import tech.zuosi.bookmarkmanager.util.DataManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by iwar on 2016/3/4.
 */
public class UIpanel {
    private JFrame jFrame = new JFrame("书签管理器");
    private Dimension dimension = new Dimension();
    private Point point = new Point(300,200);
    private JLabel jLabel = new JLabel("书签内容包括url、编号、标题与内容",JLabel.CENTER);
    private Font topLabelFont = new Font("Serief",Font.BOLD + Font.ITALIC,40);
    private Font buttonFont = new Font("Serief",Font.PLAIN,22);
    private JButton addBookmark = new JButton("添加书签");
    public static void main(String[] args) {
        new UIpanel();
    }
    public UIpanel() {
        addBookmark.setFont(buttonFont);
        addBookmark.setBounds(2,500,200,50);
        dimension.setSize(1100,768);
        jLabel.setFont(topLabelFont);
        jLabel.setBackground(Color.GREEN);
        jLabel.setBounds(50,20,750,50);
        jFrame.setSize(dimension);
        jFrame.setBackground(Color.WHITE);
        jFrame.setLocation(point);
        jFrame.setVisible(true);
        jFrame.add(new tabPane());
        jFrame.pack();
        jFrame.setResizable(false);
        // TODO 加入右键菜单（非必要）
        // TODO 加入快捷键
        // TODO 重构，性能优化
        //TODO 将各种功能封装
    }
}
