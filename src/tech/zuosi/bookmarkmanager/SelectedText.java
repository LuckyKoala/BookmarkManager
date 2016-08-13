package tech.zuosi.bookmarkmanager;

/**
 * Created by iwar on 2016/8/13.
 */
public class SelectedText {
    public static String text;

    //TODO 获取数据不直观，不好读取
    public static String formatJson(String src) {
        if (src == null) return "";
        return src.replaceAll("\\\\n","\n     ").replace("{","").replace("}","")
                .replaceAll(",","\n").replaceAll("\":\"","->\n     ").replaceAll("\"","");
    }
}
