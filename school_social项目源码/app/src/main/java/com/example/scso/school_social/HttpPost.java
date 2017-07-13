package com.example.scso.school_social;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class HttpPost {
    private JSONObject post_data;
    private String jsp_file;
    private Handler handler;
    public HttpPost(JSONObject json, String jsp_file, Handler handler){
        this.post_data=json;
        this.jsp_file=jsp_file;
        this.handler=handler;
    }


    public void doPost(){
        new Thread(){
            public void run(){
                String url="http://118.89.199.187/school_social/jsp/"+jsp_file;
                Log.d("tag", "run: "+url);
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection conn=(HttpURLConnection)url1.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection","keep-Alive");
                    //conn.setRequestProperty("Charset","UTF-8");
                    byte [] data=post_data.toString().getBytes();
                    conn.setRequestProperty("Content-Length",String.valueOf(data.length));
                    //conn.setRequestProperty("contentType","application/json");
                    conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
                    conn.connect();
                    OutputStream out=conn.getOutputStream();
                    out.write(data);
                    out.flush();
                    out.close();
                    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        try {
                            String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                            final JSONObject jsonObject = new JSONObject(jsonstr);
                            JSONArray json_data = jsonObject.getJSONArray("Data");
                            int result_code=jsonObject.getInt("result_code");
                            Log.d("tag", "run: "+jsonObject.getString("result_code"));
                            Log.d("tag", "run: "+json_data);
                            Message msg = Message.obtain(handler);
                            if(result_code==1){
                                msg.what=3;
                            }
                            else{
                                msg.what=1;
                            }
                            msg.obj = json_data;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Message msg = Message.obtain(handler,1);
                        handler.sendMessage(msg);
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
