package tech.zuosi.bookmarkmanager.panel;

import javax.swing.*;

/**
 * Created by iwar on 2016/3/12.
 */
public class TabPane extends JTabbedPane {
    public TabPane() {
        this.setTabPlacement(JTabbedPane.TOP);
        this.addTab("内容",new MainPanel());
        this.addTab("设置",new SettingPanel());
    }
}
