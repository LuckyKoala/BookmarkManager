package tech.zuosi.bookmarkmanager.panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by iwar on 2016/3/12.
 */
public class SettingPanel extends JPanel {
    private JLabel pathLab = new JLabel("存储格式");
    private JTextField path = new JTextField("./bookmarkinfo.json",15);
    private JButton apply = new JButton("应用");
    private JButton saveAndLoad = new JButton("保存并迁移");
    private GridBagLayout gridBagLayout = new GridBagLayout();
    private GridBagConstraints gridBagConstraints = new GridBagConstraints();

    public SettingPanel() {
        // TODO 例如加入文件浏览器，方便选择数据文件存储路径
        this.setLayout(gridBagLayout);
        this.path.setEditable(false);
        instanceConstraints(pathLab,0,0,1);
        instanceConstraints(path,1,0,2);
        instanceConstraints(apply,0,8,1);
        instanceConstraints(saveAndLoad,2,8,2);
    }

    void instanceConstraints(Component var,int x,int y,int width) {
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.gridwidth = width;
        gridBagLayout.setConstraints(var,gridBagConstraints);
        this.add(var);
    }
}
