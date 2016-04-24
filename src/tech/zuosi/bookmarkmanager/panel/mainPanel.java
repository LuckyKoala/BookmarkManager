package tech.zuosi.bookmarkmanager.panel;

import tech.zuosi.bookmarkmanager.listener.limitTextListener;
import tech.zuosi.bookmarkmanager.listener.textMenuListener;
import tech.zuosi.bookmarkmanager.util.BookmarkInfo;
import tech.zuosi.bookmarkmanager.util.DataManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by iwar on 2016/3/12.
 */
public class mainPanel extends JPanel {
    private JLabel titleLab = new JLabel("标题");
    private JLabel urlLab = new JLabel("URL");
    private JLabel infoLab = new JLabel("备注");
    private JTextField title = new JTextField(15);
    private JTextField url = new JTextField(15);
    private JTextArea info = new JTextArea(5,15);
    private JButton add = new JButton("添加");
    private JButton delete = new JButton("移除");
    private JButton edit = new JButton("编辑");
    private JButton save = new JButton("保存");
    private JButton reload = new JButton("重置");
    private JScrollPane scrollInfoPane = new JScrollPane(info,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JLabel messageLab = new JLabel("点击编辑按钮查看已有内容");
    private GridBagLayout gridBagLayout = new GridBagLayout();
    private GridBagConstraints constraints = new GridBagConstraints();
    private JTextArea content = new JTextArea(18,30);
    private JScrollPane scrollContentPane = new JScrollPane(content,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private textMenuListener tml = new textMenuListener(content);
    private Font textFont = new Font("Serief",Font.BOLD,13);

    public mainPanel() {
        this.setLayout(gridBagLayout);
        initComponent();
        initActionListener();
    }

    private void initActionListener() {
        // TODO 监听事件，info.getText().getBytes().length <= 119/或是增加横向滚动栏
        this.add.addActionListener(e -> {
            if (tml.isEditMode())
                return;
            if ("".equals(url.getText()) || "".equals(title.getText()) || "".equals(info.getText())) {
                messageLab.setText("内容不能为空");
            } else {
                try {
                    new DataManager(new BookmarkInfo(url.getText(), title.getText(), info.getText())).writeData();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
                messageLab.setText("数据["+title.getText()+"]存储完毕~");
            }
        });
        this.reload.addActionListener(e -> {
            title.setText("");
            url.setText("");
            info.setText("");
            String alertMessage = "可以添加，移除，查看书签哦\r\n目前还没有完成_(:з」∠)_\r\n内容已重置";
            JOptionPane.showMessageDialog(this, alertMessage, "书签精灵QAQ", -1);
            if ("".equals(url.getText()) || "".equals(title.getText()) || "".equals(info.getText()))
                messageLab.setText("已经重置所有待录入的内容！");
        });
        this.edit.addActionListener(e -> {
            this.remove(edit);
            instanceConstraints(delete,2,6,3,2);
            messageLab.setText("编辑程式正在运行.");
            String alertMessage = "编辑按钮已经替换为移除按钮，\r\n编辑完点击保存即可切换为新增模式，\r\n" +
                    "在编辑模式下将无法新增书签\r\n么么扎_(:з」∠)_";
            JOptionPane.showMessageDialog(this, alertMessage, "书签精灵0A0", -1);
            title.setEditable(false);
            url.setEditable(false);
            info.setEditable(false);
            content.setText(new DataManager().listDataIndex());
            tml.setEditMode(true);
            // TODO 点击移除，弹出窗口，输入书签序号即可移除
        });
        this.save.addActionListener(e -> {
            this.remove(delete);
            instanceConstraints(edit,2,6,3,2);
            title.setEditable(true);
            url.setEditable(true);
            info.setEditable(true);
            messageLab.setText("点击编辑按钮查看已有内容");
            // FIXME 过长的内容会使得面板变形
            content.setText("默认文本\n 如你所见\n  想不想问问隔壁的神奇海螺呢?");
            // TODO 点击保存，重载数据文件
        });
    }

    private void initComponent() {
        instanceConstraints(titleLab,0,0,1,2);
        instanceConstraints(title,2,0,2,2);
        instanceConstraints(urlLab,0,2,1,2);
        instanceConstraints(url,2,2,2,2);
        instanceConstraints(infoLab,0,4,1,2);
        instanceConstraints(scrollInfoPane,2,4,2,2);
        instanceConstraints(add,0,6,3,2);
        instanceConstraints(edit,2,6,3,2);
        instanceConstraints(save,0,8,3,2);
        instanceConstraints(reload,2,8,3,2);
        instanceConstraints(messageLab,0,10,5,3);
        instanceConstraints(scrollContentPane,6,0,3,13);
        this.info.setLineWrap(true);
        content.setEditable(false);
        content.setText("默认文本\n" +
                " 如你所见\n" +
                "  想不想问问隔壁的神奇海螺呢?");
        content.addCaretListener(tml);
        content.addMouseListener(tml);
        content.setFont(textFont);
    }

    private void instanceConstraints(Component var,int x,int y,int width,int height) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.ipady = 4;
        gridBagLayout.setConstraints(var,constraints);
        this.add(var);
    }

}
