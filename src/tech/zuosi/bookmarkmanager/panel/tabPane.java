package tech.zuosi.bookmarkmanager.panel;

import javax.swing.*;

/**
 * Created by iwar on 2016/3/12.
 */
public class tabPane extends JTabbedPane {
    public tabPane() {
        this.setTabPlacement(JTabbedPane.TOP);
        this.addTab("内容",new mainPanel());
        this.addTab("设置",new settingPanel());
    }
}
