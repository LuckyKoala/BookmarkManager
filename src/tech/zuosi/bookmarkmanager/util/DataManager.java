package tech.zuosi.bookmarkmanager.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/3/12.
 */
public class DataManager {
    private Gson gson = new Gson();
    private BookmarkInfo bookmarkInfo;
    public static Map<String,String> dataIndex = new HashMap<>();
    private File dataFile = new File("e:"+File.separator+"io"+File.separator+"bookmarkmanager.json");

    public DataManager() {}

    public DataManager(BookmarkInfo bookmarkInfo) {
        this.bookmarkInfo = bookmarkInfo;
    }

    public boolean writeData() throws IOException {
        if (dataIndex.containsKey(bookmarkInfo.getTitle())) {
            return false;
        } else if (null == this.bookmarkInfo) {
            return false;
        }
        if (!dataFile.exists())
            dataFile.createNewFile();
        // TODO 文件不存在会自动创建，但是目录不存在则会报错
        OutputStream outputStream = new FileOutputStream(dataFile,true);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.setIndent("    ");
        writer.beginObject();
        writer.name("BIID").value(bookmarkInfo.getBIID());
        writer.name("url").value(bookmarkInfo.getUrl());
        writer.name("title").value(bookmarkInfo.getTitle());
        writer.name("contentinfo").value(bookmarkInfo.getContentInfo());
        writer.endObject();
        writer.close();
        outputStream.close();
        return true;
    }

    public void readData() throws IOException {
        Reader reader = new FileReader(dataFile);
        String title = null;
        String url = null;
        String info = null;
        String BIID = null;
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true);
        while (JsonToken.BEGIN_OBJECT.equals(jsonReader.peek())) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if ("title".equals(name)) {
                    title = jsonReader.nextString();
                } else if ("url".equals(name)) {
                    url = jsonReader.nextString();
                } else if ("contentinfo".equals(name)) {
                    info = jsonReader.nextString();
                } else if ("BIID".equals(name)) {
                    BIID = jsonReader.nextString();
                    // TODO 利用BIID(MD5)来检查内容是否更改过
                }

            }
            jsonReader.endObject();
            if (null == dataIndex) {
                dataIndex.put(title,gson.toJson(new BookmarkInfo(url,title,info)));
            } else if (!dataIndex.containsKey(title)) {
                dataIndex.put(title,gson.toJson(new BookmarkInfo(url,title,info)));
            }
        }
        jsonReader.close();
        reader.close();
    }

    public String listDataIndex() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            readData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object[] di = dataIndex.keySet().toArray();
        for (int i= 0;i < di.length;i++) {
            stringBuilder.append(di[i]);
            if (i < di.length-1) {
                stringBuilder.append("\r\n");
            }
        }
        return stringBuilder.toString();
    }
}
