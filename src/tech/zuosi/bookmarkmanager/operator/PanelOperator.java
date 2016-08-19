package tech.zuosi.bookmarkmanager.operator;

import com.google.gson.Gson;
import tech.zuosi.bookmarkmanager.data.BookmarkInfo;
import tech.zuosi.bookmarkmanager.data.DataManager;
import tech.zuosi.bookmarkmanager.panel.MainPanel;
import tech.zuosi.bookmarkmanager.type.ModeType;
import tech.zuosi.bookmarkmanager.util.TextUtil;

/**
 * Created by iwar on 2016/8/15.
 */
public class PanelOperator {

    public PanelOperator() {}

    public void loadBMI() {
        String jsonString = DataManager.dataIndex.get(TextUtil.text);
        if (jsonString == null) {
            MainPanel.messageLab.setText("请先双击选中一项数据");
            return;
        }
        MainPanel.content.setText(TextUtil.formatJson(jsonString));
        MainPanel.currentMode = ModeType.INFO;
        BookmarkInfo bookmarkInfo = new Gson().fromJson(jsonString,BookmarkInfo.class);
        if (bookmarkInfo != null) {
            MainPanel.title.setText(bookmarkInfo.getTitle());
            MainPanel.url.setText(bookmarkInfo.getUrl());
            MainPanel.info.setText(bookmarkInfo.getContentInfo());
        }
    }

    public boolean backToList() {
        //MainPanel.title.setText("");
        //MainPanel.url.setText("");
        //MainPanel.info.setText("");
        if (DataManager.dataIndex.keySet().size() == 0) {
            return false;
        }
        MainPanel.content.setText(new DataManager().listDataIndex());
        MainPanel.currentMode = ModeType.LIST;
        return true;
    }
}
