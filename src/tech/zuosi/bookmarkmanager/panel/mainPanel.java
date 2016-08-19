package tech.zuosi.bookmarkmanager.panel;

import com.google.gson.Gson;
import tech.zuosi.bookmarkmanager.data.BookmarkInfo;
import tech.zuosi.bookmarkmanager.data.DataManager;
import tech.zuosi.bookmarkmanager.listener.TextMenuListener;
import tech.zuosi.bookmarkmanager.operator.PanelOperator;
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
    public static JLabel titleLab,urlLab,infoLab;
    public static JTextField title,url;
    public static JTextArea info;
    public static JButton back,add,delete, data,save,reload;
    public static JScrollPane scrollInfoPane;
    public static JLabel messageLab;
    public static GridBagLayout gridBagLayout;
    public static GridBagConstraints constraints;
    public static JTextArea content;
    public static JScrollPane scrollContentPane;
    public static TextMenuListener tml;
    public static Font textFont;
    public static String defaultInfo;
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
        back = new JButton("返回");
        add = new JButton("添加");
        delete = new JButton("移除");
        data = new JButton("数据");
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
        //FIXME 单独Jar运行时，查看中文内容会产生乱码
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
            }
        });

        this.reload.addActionListener((ActionEvent e) -> {
            if ("".equals(url.getText()) || "".equals(title.getText()) || "".equals(info.getText())) {
                messageLab.setText("内容为空,无需重置");
                return;
            }
            title.setText("");
            url.setText("");
            info.setText("");
            if ("".equals(url.getText()) || "".equals(title.getText()) || "".equals(info.getText()))
                messageLab.setText("已经重置所有待录入的内容！");
        });

        this.data.addActionListener(e -> {
            this.remove(data);
            instanceConstraints(delete,2,6,3,2);
            this.remove(add);
            instanceConstraints(back,0,6,3,2);

            this.updateUI();

            messageLab.setText("点击保存即可切换为新增模式");

            content.setText(new DataManager().listDataIndex());
            currentMode = ModeType.LIST;

            this.back.addActionListener(e2 -> {
                if (currentMode == ModeType.INFO) {
                    if (!new PanelOperator().backToList()) {
                        saveOperator();
                    }
                } else if (currentMode == ModeType.LIST) {
                    saveOperator();
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
                    title.setText("");
                    url.setText("");
                    info.setText("");
                    content.setText(new DataManager().listDataIndex());
                    currentMode = ModeType.LIST;
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
                    showTitle = TextUtil.shortString(showTitle);

                    if (hasWritten) {
                        messageLab.setText("数据[" + showTitle + "]存储完毕~");

                        content.setText(new DataManager().listDataIndex());
                        currentMode = ModeType.LIST;
                    } else {
                        messageLab.setText("内容相同，无需更新~");
                    }
                }
            } else if (ModeType.LIST == currentMode) {
                messageLab.setText("双击数据即可读取");
            }
        });
    }

    public void saveOperator() {
        title.setText("");
        url.setText("");
        info.setText("");
        this.remove(delete);
        this.remove(back);
        instanceConstraints(data,2,6,3,2);
        instanceConstraints(add,0,6,3,2);
        this.updateUI();
        content.setText(defaultInfo);
        currentMode = ModeType.NEW;
    }

    private void initComponent() {
        instanceConstraints(titleLab,0,0,1,2);
        instanceConstraints(title,2,0,2,2);
        instanceConstraints(urlLab,0,2,1,2);
        instanceConstraints(url,2,2,2,2);
        instanceConstraints(infoLab,0,4,1,2);
        instanceConstraints(scrollInfoPane,2,4,2,2);
        instanceConstraints(add,0,6,3,2);
        instanceConstraints(data,2,6,3,2);
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
