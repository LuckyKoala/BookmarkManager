package tech.zuosi.bookmarkmanager.type;

/**
 * Created by iwar on 2016/8/14.
 */
public enum ModeType {
    NEW("添加"),
    LIST("列表"),
    INFO("信息");

    private String modeType;

    ModeType(String modeType) {
        this.modeType = modeType;
    }
}
