package tech.zuosi.bookmarkmanager.util;

/**
 * Created by iwar on 2016/8/13.
 */
public class TextUtil {
    public static String text;

    public static String shortString(String src) {
        if (src.length() > 6) {
            return src.substring(0,5) + "...";
        }
        return src;
    }

    public static String formatJson(String src) {
        //JTextPane 设置字体颜色
        if (src == null) return "";

        String var1[] = src.replace("{","").replace("}","").replace("\"","")
                .split(",");
        StringBuilder sb = new StringBuilder();

        for (String var2 : var1) {
            String var3[] = var2.split(":");
            String key,value;

            key = var3[0];
            value = var3[1];
            value = formatString(value,32);

            switch (key) {
                case "BIID":
                    key = "MD5";
                    break;
                case "url":
                    key = "网址";
                    break;
                case "title":
                    key = "标题";
                    break;
                case "contentInfo":
                    key = "备注";
                    break;
            }

            sb.append(key + "->\n     "
                    + value + "\n");
        }

        return sb.toString();
    }

    private static String formatString(String src,int limit) {
        if (src.length() <= limit) return src;

        final int length = src.length();
        int sectionNumer = length/limit + (length%limit>0?1:0);
        StringBuilder sb = new StringBuilder();

        for (int var=0;var < sectionNumer-1;var++) {
            int substract = length-(var+1)*limit;
            sb.append(src.substring(substract)).append("\n" + "     ");
            src= src.substring(0, substract - 1);
        }

        return sb.append(src).append("     ").toString();
    }
}
