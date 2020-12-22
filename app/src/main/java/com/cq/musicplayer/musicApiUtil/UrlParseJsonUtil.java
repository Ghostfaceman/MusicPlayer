package com.cq.musicplayer.musicApiUtil;


import com.cq.musicplayer.musicApiUtil.model.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlParseJsonUtil {
    /**
     * 该方法拿到网页字符串数据
     */
    public static String getWebString(String url) {
        String result = null;
        try {
            URL uRl = new URL(url);
            //打开url连接
            HttpURLConnection connection = (HttpURLConnection) uRl.openConnection();
            //设置请求的方式 要求大写
            connection.setRequestMethod("GET");
            //设置连接超时的时间
            connection.setConnectTimeout(10000);
            //获取响应码
            int code = connection.getResponseCode();
            //判断响应码
            if (code == 200) {
                InputStream inputStream = connection.getInputStream();
                result = getStringFromStream(inputStream);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     *  该方法拿到网页的json字符串
     * @param inputStream
     * @return
     */
    public static String getStringFromStream(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        byte[] buffer = new byte[1024];
        try {
            while (((len = inputStream.read(buffer)) != -1)) {
                baos.write(buffer, 0, len);
            }
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 解析随机音乐对象
     * @param jsonString
     * @return
     */
    public static Song paseJsonObject(String jsonString){
        Song song = null;
        try {
            //json字符串转json对象
            JSONObject jsonObject = new JSONObject(jsonString);
            //拿到json对象data数据的json字符串
            String jsondata = jsonObject.getString("data");
            //data字段的json字符串转json对象
            JSONObject dataJsonObject = new JSONObject(jsondata);

            //解析它
            String name = dataJsonObject.getString("name");
            String url = dataJsonObject.getString("url");
            String picurl = dataJsonObject.getString("picurl");
            String artistsname = dataJsonObject.getString("artistsname");
            song = new Song(name,url,picurl,artistsname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return song;
    }
}
