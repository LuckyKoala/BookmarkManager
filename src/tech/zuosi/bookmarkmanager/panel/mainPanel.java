package tech.zuosi.bookmarkmanager.panel;

import com.google.gson.Gson;
import tech.zuosi.bookmarkmanager.SelectedText;
import tech.zuosi.bookmarkmanager.data.BookmarkInfo;
import tech.zuosi.bookmarkmanager.data.DataManager;
import tech.zuosi.bookmarkmanager.listener.textMenuListener;
import tech.zuosi.bookmarkmanager.type.ModeType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private JButton readAndLoad = new JButton("读取");
    private JButton add = new JButton("添加");
    private JButton delete = new JButton("移除");
    private JButton edit = new JButton("编辑");
    private JButton save = new JButton("保存");
    private JButton reload = new JButton("重置");
    private JButton exit = new JButton("退出");
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
    public static ModeType currentMode = ModeType.NEW;

    public mainPanel() {
        this.setLayout(gridBagLayout);
        initComponent();
        initActionListener();
    }

    private void initActionListener() {
        //TODO 读取后，右边有内容的时候，增加一种可以保存该次修改而不直接退出编辑模式的按钮，最好更新更多按钮，将frame显示区域扩大
        // TODO 监听事件，info.getText().getBytes().length <= 119/或是增加横向滚动栏
        //TODO 多线程更新text
        //TODO 目前程序似乎直接关闭窗口，进程仍存在
        //FIXME 导出的jar直接运行似乎无法正常读取数据
        this.add.addActionListener(e -> {
            if (ModeType.NEW != currentMode)
                return;
            if ("".equals(url.getText()) || "".equals(title.getText()) || "".equals(info.getText())) {
                messageLab.setText("内容不能为空");
            } else {
                try {
                    new DataManager(new BookmarkInfo(url.getText(), title.getText(), info.getText())).writeData(false);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
                messageLab.setText("数据["+title.getText()+"]存储完毕~");

                title.setText("");
                url.setText("");
                info.setText("");
                String alertMessage = "可以添加，移除，查看书签哦\r\n目前还没有完成_(:з」∠)_\r\n内容已重置";
                JOptionPane.showMessageDialog(this, alertMessage, "书签精灵QAQ", -1);
            }
        });

        this.reload.addActionListener((ActionEvent e) -> {
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
            this.remove(add);
            instanceConstraints(readAndLoad,0,6,3,2);

            messageLab.setText("点击保存即可切换为新增模式");
            String alertMessage = "双击选中然后选中读取或删除即可\r\n编辑完点击保存即可切换为新增模式，\r\n" +
                    "在编辑模式下将无法新增书签\r\n么么扎_(:з」∠)_";
            JOptionPane.showMessageDialog(this, alertMessage, "书签精灵0A0", -1);

            content.setText(new DataManager().listDataIndex());
            currentMode = ModeType.LIST;
            //tml.setEditMode(true);

            this.readAndLoad.addActionListener(e2 -> {
                String jsonString = DataManager.dataIndex.get(SelectedText.text);
                content.setText(SelectedText.formatJson(jsonString));
                //tml.setEditMode(false);
                currentMode = ModeType.INFO;
                BookmarkInfo bookmarkInfo = new Gson().fromJson(jsonString,BookmarkInfo.class);
                if (bookmarkInfo != null) {
                    title.setText(bookmarkInfo.getTitle());
                    url.setText(bookmarkInfo.getUrl());
                    info.setText(bookmarkInfo.getContentInfo());
                }
            });
            this.delete.addActionListener(e1 -> {
                try {
                    new DataManager(new Gson().fromJson(DataManager.dataIndex.get(SelectedText.text),BookmarkInfo.class)).removeBMI();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            });
        });

        this.save.addActionListener(e -> {
            if (ModeType.INFO == currentMode) {
                if ("".equals(url.getText()) || "".equals(title.getText()) || "".equals(info.getText())) {
                    messageLab.setText("内容为空,保持原有内容");
                } else {
                    boolean hasWritten = false;
                    try {
                        hasWritten = new DataManager(new BookmarkInfo(url.getText(),
                                title.getText(), info.getText())).writeData(true);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                    String showTitle = title.getText();
                    int showTitleLength = showTitle.length();
                    if (showTitleLength > 6) {
                        showTitle = showTitle.substring(0,5) + "...";
                    }

                    if (hasWritten) {
                        messageLab.setText("数据[" + showTitle + "]存储完毕~");
                    } else {
                        messageLab.setText("内容相同，无需更新~");
                    }
                }
            } else if (ModeType.LIST == currentMode) {
                messageLab.setText("点击编辑按钮查看已有内容");
            } else {
                return;
            }

            content.setEditable(false);
            content.setText("默认文本\n 如你所见\n  想不想问问隔壁的神奇海螺呢?");
            this.remove(delete);
            instanceConstraints(edit,2,6,3,2);
            this.remove(readAndLoad);
            instanceConstraints(add,0,6,3,2);
            currentMode = ModeType.NEW;
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
