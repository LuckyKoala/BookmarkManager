package tech.zuosi.bookmarkmanager.panel;

import com.google.gson.Gson;
import tech.zuosi.bookmarkmanager.data.BookmarkInfo;
import tech.zuosi.bookmarkmanager.data.DataManager;
import tech.zuosi.bookmarkmanager.listener.TextMenuListener;
import tech.zuosi.bookmarkmanager.type.ModeType;
import tech.zuosi.bookmarkmanager.util.TextUtil;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by iwar on 2016/3/12.
 */
public class MainPanel extends JPanel {
    private JLabel titleLab,urlLab,infoLab;
    private JTextField title,url;
    private JTextArea info;
    private JButton readAndLoad,add,delete,edit,save,reload;
    private JScrollPane scrollInfoPane;
    private JLabel messageLab;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints constraints;
    private JTextArea content;
    private JScrollPane scrollContentPane;
    private TextMenuListener tml;
    private Font textFont;
    private String defaultInfo;
    public static ModeType currentMode;

    public MainPanel() {
        init();

        this.setLayout(gridBagLayout);
        initComponent();
        initActionListener();
    }

    private void init() {
        titleLab = new JLabel("标题");
        urlLab = new JLabel("URL");
        infoLab = new JLabel("备注");
        title = new JTextField(15);
        url = new JTextField(15);
        info = new JTextArea(5,15);
        readAndLoad = new JButton("读取");
        add = new JButton("添加");
        delete = new JButton("移除");
        edit = new JButton("编辑");
        save = new JButton("保存");
        reload = new JButton("重置");
        scrollInfoPane = new JScrollPane(info,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageLab = new JLabel("点击编辑按钮查看已有内容");
        gridBagLayout = new GridBagLayout();
        constraints = new GridBagConstraints();
        content = new JTextArea(18,30);
        scrollContentPane = new JScrollPane(content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tml = new TextMenuListener(content);
        textFont = new Font("Serief",Font.BOLD,13);
        currentMode = ModeType.NEW;
        defaultInfo = "Tips:\n" +
                " 1.填写内容之后点击添加即可增加并保存\n" +
                " 2.点击编辑即可进入编辑模式，查看已有数据\n" +
                "  2.1 双击选中条目\n" +
                "  2.2 点击读取则可以显示该条目的完整内容,\n" +
                "      并在左侧对内容进行编辑操作\n" +
                "  2.3 点击保存则可以保存该条目\n" +
                "  2.4 在编辑模式点击保存即可切换为新增模式\n" +
                " 3.点击重置即可清空输入区的所有内容\n" +
                " 还有什么问题？想不想问问隔壁的神奇海螺呢？";

        limitMaxSize(22,30,120);
    }

    private void limitMaxSize(int titleMaxLength,int urlMaxLength,int infoMaxLength) {
        title.setDocument(new PlainDocument() {
            public void insertString(int offset, String s,
                                     AttributeSet attributeSet) throws BadLocationException {
                if (s == null || offset < 0) {
                    return;
                }
                for (int i = 0; i < s.length(); i++) {
                    if (getLength() > titleMaxLength - 1) {
                        break;
                    }
                    super.insertString(offset + i, s.substring(i, i + 1),
                            attributeSet);
                }
            }
        });
        url.setDocument(new PlainDocument() {
            public void insertString(int offset, String s,
                                     AttributeSet attributeSet) throws BadLocationException {
                if (s == null || offset < 0) {
                    return;
                }
                for (int i = 0; i < s.length(); i++) {
                    if (getLength() > urlMaxLength - 1) {
                        break;
                    }
                    super.insertString(offset + i, s.substring(i, i + 1),
                            attributeSet);
                }
            }
        });
        info.setDocument(new PlainDocument() {
            public void insertString(int offset, String s,
                                     AttributeSet attributeSet) throws BadLocationException {
                if (s == null || offset < 0) {
                    return;
                }
                for (int i = 0; i < s.length(); i++) {
                    if (getLength() > infoMaxLength - 1) {
                        break;
                    }
                    super.insertString(offset + i, s.substring(i, i + 1),
                            attributeSet);
                }
            }
        });
    }

    private void initActionListener() {
        //TODO 读取后，右边有内容的时候，增加一种可以保存该次修改而不直接退出编辑模式的按钮，最好更新更多按钮，将frame显示区域扩大
        //? 多线程更新text
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
                messageLab.setText("数据[" + TextUtil.shortString(title.getText()) + "]存储完毕~");

                title.setText("");
                url.setText("");
                info.setText("");
                String alertMessage = "可以添加，移除，查看书签哦_(:з」∠)_\r\n内容已重置";
                JOptionPane.showMessageDialog(this, alertMessage, "书签精灵QAQ", -1);
            }
        });

        this.reload.addActionListener((ActionEvent e) -> {
            title.setText("");
            url.setText("");
            info.setText("");
            String alertMessage = "可以添加，移除，查看书签哦_(:з」∠)_\r\n内容已重置";
            JOptionPane.showMessageDialog(this, alertMessage, "书签精灵QAQ", -1);
            if ("".equals(url.getText()) || "".equals(title.getText()) || "".equals(info.getText()))
                messageLab.setText("已经重置所有待录入的内容！");
        });

        this.edit.addActionListener(e -> {
            this.remove(edit);
            instanceConstraints(delete,2,6,3,2);
            this.remove(add);
            instanceConstraints(readAndLoad,0,6,3,2);

            this.updateUI();

            messageLab.setText("点击保存即可切换为新增模式");
            String alertMessage = "双击选中然后选中读取或删除即可\r\n编辑完点击保存即可切换为新增模式，\r\n" +
                    "在编辑模式下将无法新增书签\r\n么么扎_(:з」∠)_";
            JOptionPane.showMessageDialog(this, alertMessage, "书签精灵0A0", -1);

            content.setText(new DataManager().listDataIndex());
            currentMode = ModeType.LIST;

            this.readAndLoad.addActionListener(e2 -> {
                String jsonString = DataManager.dataIndex.get(TextUtil.text);
                if (jsonString == null) {
                    messageLab.setText("请先双击选中一项数据");
                    return;
                }
                content.setText(TextUtil.formatJson(jsonString));
                currentMode = ModeType.INFO;
                BookmarkInfo bookmarkInfo = new Gson().fromJson(jsonString,BookmarkInfo.class);
                if (bookmarkInfo != null) {
                    title.setText(bookmarkInfo.getTitle());
                    url.setText(bookmarkInfo.getUrl());
                    info.setText(bookmarkInfo.getContentInfo());
                }
            });

            this.delete.addActionListener(e1 -> {
                String selectedJson = DataManager.dataIndex.get(TextUtil.text);
                if (TextUtil.text == null) {
                    messageLab.setText("请先双击选中一项数据");
                    return;
                }
                if (selectedJson == null) {
                    messageLab.setText("数据[" + TextUtil.shortString(TextUtil.text) + "]不存在");
                    return;
                }
                try {
                    new DataManager(new Gson().fromJson(DataManager.dataIndex.get(TextUtil.text),BookmarkInfo.class))
                            .removeBMI();
                    messageLab.setText("成功删除数据[" + TextUtil.shortString(TextUtil.text) + "]");
                    content.setText(new DataManager().listDataIndex());
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
                    System.out.println(info.getText());
                    System.out.println(info.getSelectedText());
                    try {
                        hasWritten = new DataManager(new BookmarkInfo(url.getText(),
                                title.getText(), info.getText())).writeData(true);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                    String showTitle = title.getText();
                    showTitle = TextUtil.shortString(showTitle);

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
            content.setText(defaultInfo);
            this.remove(delete);
            instanceConstraints(edit,2,6,3,2);
            this.remove(readAndLoad);
            instanceConstraints(add,0,6,3,2);
            this.updateUI();
            currentMode = ModeType.NEW;
            title.setText("");
            url.setText("");
            info.setText("");
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
        content.setText(defaultInfo);
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
