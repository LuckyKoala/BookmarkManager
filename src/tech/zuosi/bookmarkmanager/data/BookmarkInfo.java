package tech.zuosi.bookmarkmanager.data;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by iwar on 2016/3/4.
 */
public class BookmarkInfo {
    private String BIID;
    private String url;
    private String title;
    private String contentInfo;

    public BookmarkInfo(String url,String title,String contentInfo) {
        this.setUrl(url);
        this.setTitle(title);
        this.setContentInfo(contentInfo);
        try {
            this.setBIID(convertBIID());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.setBIID("null");
        }
    }

    public String getBIID() {
        return BIID;
    }

    public void setBIID(String BIID) {
        this.BIID = BIID;
    }

    private String convertBIID() throws UnsupportedEncodingException {
        MessageDigest md5;
        String src = url + title + contentInfo;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = src.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = (int) md5Byte & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentInfo() {
        return contentInfo;
    }

    public void setContentInfo(String contentInfo) {
        this.contentInfo = contentInfo;
    }
}
