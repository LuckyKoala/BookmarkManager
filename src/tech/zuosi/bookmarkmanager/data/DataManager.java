package tech.zuosi.bookmarkmanager.data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/3/12.
 */
public class DataManager {
    private Gson gson = new Gson();
    private BookmarkInfo bookmarkInfo;
    public static Map<String,String> dataIndex = new HashMap<>();
    private String s = "e:"+File.separator+"io"+File.separator+"bookmarkmanager.json";
    private File dataFile = new File(s);

    public DataManager() {}

    public DataManager(BookmarkInfo bookmarkInfo) {
        this.defineBMI(bookmarkInfo);
    }

    private void defineBMI(BookmarkInfo bookmarkInfo) {
        this.bookmarkInfo = bookmarkInfo;
    }

    public boolean removeBMI() throws IOException {
        boolean result = dataIndex.remove(bookmarkInfo.getTitle(),dataIndex.get(bookmarkInfo.getTitle()));
        writeData(true);

        return result;
    }

    public boolean writeData(boolean rewrite) throws IOException {
        boolean append = !rewrite;
        if (dataIndex.containsKey(bookmarkInfo.getTitle())) {
            String preJson = dataIndex.get(bookmarkInfo.getTitle());
            BookmarkInfo pre = gson.fromJson(preJson,BookmarkInfo.class);
            if (pre.getBIID().equals(bookmarkInfo.getBIID())) {
                return false;
            }
            dataIndex.replace(bookmarkInfo.getTitle(),preJson,gson.toJson(bookmarkInfo));
        } else {
            dataIndex.put(bookmarkInfo.getTitle(),gson.toJson(bookmarkInfo));
        }
        if (!dataFile.getParentFile().exists()) {
            dataFile.getParentFile().mkdir();
        }
        if (!dataFile.exists()) {
            dataFile.createNewFile();
        }

        writeBMI(append);
        return true;
    }

    private void writeBMI(boolean append) throws IOException {
        List<BookmarkInfo> bookmarkInfoList = new ArrayList<>();
        OutputStream outputStream = new FileOutputStream(dataFile,append);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));

        if (append) {
            bookmarkInfoList.add(bookmarkInfo);
        } else {
            for (String json : dataIndex.values()) {
                bookmarkInfoList.add(gson.fromJson(json,BookmarkInfo.class));
            }
        }

        writer.beginArray();
        for (BookmarkInfo bmi : bookmarkInfoList) {
            writer.setIndent("    ");
            writer.beginObject();
            writer.name("BIID").value(bmi.getBIID());
            writer.name("url").value(bmi.getUrl());
            writer.name("title").value(bmi.getTitle());
            writer.name("contentinfo").value(bmi.getContentInfo());
            writer.endObject();
        }
        writer.endArray();

        writer.close();
        outputStream.close();
    }

    public void readData() {
        if (!dataFile.exists()) return;
        try (Reader reader = new FileReader(dataFile);JsonReader jsonReader = new JsonReader(reader)) {
            String title = null;
            String url = null;
            String info = null;
            String BIID = null;

            jsonReader.setLenient(true);
            while (JsonToken.BEGIN_ARRAY.equals(jsonReader.peek())) {
                jsonReader.beginArray();
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
                            BIID = jsonReader.nextString(); //读取了但是没有用，可以考虑不写入这一信息
                        }

                    }
                    jsonReader.endObject();
                    if (null == dataIndex) {
                        dataIndex.put(title,gson.toJson(new BookmarkInfo(url,title,info)));
                    } else if (!dataIndex.containsKey(title)) {
                        dataIndex.put(title,gson.toJson(new BookmarkInfo(url,title,info)));
                    }
                }
                jsonReader.endArray();
            }

            jsonReader.close();
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String listDataIndex() {
        StringBuilder stringBuilder = new StringBuilder();
        readData();
        Object[] di = dataIndex.keySet().toArray();
        for (int i= 0;i < di.length;i++) {
            stringBuilder.append("【" + i + "】." + di[i]);
            if (i < di.length-1) {
                stringBuilder.append("\r\n");
            }
        }
        return stringBuilder.toString();
    }
}
