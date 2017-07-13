package com.example.scso.school_social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CYH on 2017/7/9.
 */

public class Myservice_Activity extends Activity {
    private String user_id;
    private ListView service_list;
    private ArrayList<HashMap<String,String>> service_data;
    private Handler handler;
    private ProgressDialog pg;
    private TextView my_service_notice;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        user_id=getIntent().getStringExtra("user_id");
        //System.out.println(user_id);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myservice);

        service_list=(ListView)findViewById(R.id.service_list);
        my_service_notice=(TextView)findViewById(R.id.my_service_notice);


        handler=new Handler(){
            public void handleMessage(Message msg){
                pg.dismiss();
                if(msg.what==3) {
//                    SimpleAdapter simpleAdapter = new SimpleAdapter(ServiceActivity.this,service_data,R.layout.service_list,
//                            new String[] {"id","name","dianzan_counts","pinglun_counts","time","intro"},new int[] {R.id.service_id,R.id.service_name,R.id.dianzan_counts
//                            ,R.id.pinglun_counts,R.id.service_time,R.id.service_intro});
//                    service_list.setAdapter(simpleAdapter);
                    if(service_data.size()>0){
                        my_service_notice.setVisibility(View.GONE);
                    }
                    ServiceListAdapter serviceListAdapter=new ServiceListAdapter(Myservice_Activity.this,service_data,user_id,true);
                    service_list.setAdapter(serviceListAdapter);
                }
                else{
                    new AlertDialog.Builder(Myservice_Activity.this).setTitle("请求结果")
                            .setMessage("请求失败，请稍后重试").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dg, int which) {
                        }
                    }).show();
                }
            }
        };
        get_service_list();
        pg=ProgressDialog.show(Myservice_Activity.this,"请求数据","正在发送请求...");
    }

    public void get_service_list(){
        service_data=new ArrayList<>();
        Thread thread = new Thread() {
            public void run() {
                String myurl = "http://118.89.199.187/school_social/jsp/get_my_service.jsp?id="+user_id;
                try {
                    URL url = new URL(myurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10 * 1000);
                    //OutputStream outputStream=conn.getOutputStream();
                    //outputStream.write(URLEncoder.encode(object.toString(),"UTF-8").getBytes());
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        try {
                            String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                            final JSONObject jsonObject = new JSONObject(jsonstr);
                            JSONArray data = jsonObject.getJSONArray("Data");
                            for(int i=0;i<data.length();i++){
                                HashMap<String,String> service=new HashMap<>();
                                service.put("user_id",data.getJSONObject(i).getString("user_id"));
                                service.put("id",data.getJSONObject(i).getString("service_id"));
                                service.put("name",data.getJSONObject(i).getString("service_name"));
                                service.put("dianzan_counts",data.getJSONObject(i).getString("dianzan_counts"));
                                service.put("pinglun_counts",data.getJSONObject(i).getString("pinglun_counts"));
                                service.put("time",data.getJSONObject(i).getString("start_time")+"——"+data.getJSONObject(i).getString("end_time"));
                                service.put("intro",data.getJSONObject(i).getString("service_intro"));
                                service.put("is_dianzan",data.getJSONObject(i).getString("is_dianzan"));
                                service_data.add(service);
                            }
                            Log.d("tag", "run: "+data);
                            Message msg = Message.obtain(handler, 3);
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Message msg = Message.obtain(handler, 0);
                            handler.sendMessage(msg);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Message msg = Message.obtain(handler, 0);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = Message.obtain(handler, 0);
                    handler.sendMessage(msg);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onRestart() {
        get_service_list();
        super.onRestart();
    }

    public void back(View v){
        finish();
    }
}

