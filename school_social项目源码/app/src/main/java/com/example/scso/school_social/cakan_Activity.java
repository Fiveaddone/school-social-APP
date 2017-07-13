package com.example.scso.school_social;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashMap;

public class cakan_Activity extends Activity {
    private Handler hd_messeage,hd_messeage1,hd_messeage2;
    private String username,id;
    Drawable drawable;
    Drawable drawable2;
    Drawable drawable1;
    Drawable drawable3;
    private  ImageView like;
    private ImageView collect;
    private JSONObject main_json1;
    private TextView collect_text,like_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cakan);
        drawable =getDrawable(R.drawable.dianzhan1);
        drawable2 =getDrawable(R.drawable.dianzhan2);
        drawable1 = getDrawable(R.drawable.shouchang1);
        drawable3 = getDrawable(R.drawable.shouchang2);

        Intent intent = getIntent();
        id = intent.getStringExtra("hd_id");
        username=intent.getStringExtra("user_id");
        like = (ImageView)findViewById(R.id.like_image);
        collect = (ImageView)findViewById(R.id.collect_image);
        collect_text = (TextView)findViewById(R.id.hd_cakan_collect);
        like_text =(TextView)findViewById(R.id.hd_cakan_like);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        try {
                            main_json1 = new JSONObject();
                            if (like.getDrawable() == drawable) {
                                main_json1.put("like", "1");
                                like.setImageDrawable(drawable2);
                                String i = like_text.getText().toString();
                                int j = 0;
                                j = Integer.parseInt(i) + 1;
                                like_text.setText(j+"");
                                //like.setImageResource(R.drawable.dianzhan2);
                            } else {
                                main_json1.put("like", "0");
                                like.setImageDrawable(drawable);
                                String i = like_text.getText().toString();
                                int j = 0;
                                j = Integer.parseInt(i) - 1;
                                like_text.setText(j+"");
                                //like.setImageResource(R.drawable.dianzhan1);
                            }
                            Log.d("tag", "run: 111");
                            main_json1.put("hd_id", id);
                            main_json1.put("username", username);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                            final String mainUrl1 = "http://118.89.199.187/school_social/jsp/hd_like.jsp";
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                            URL main_url1 = new URL(mainUrl1);
                            HttpURLConnection conn = (HttpURLConnection) main_url1.openConnection();
                            conn.setRequestMethod("POST");// 提交模式
                            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
                            // conn.setReadTimeout(2000);//读取超时 单位毫秒
                            // 发送POST请求必须设置如下两行
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setRequestProperty("Connection","keep-Alive");
                            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
                            // 获取URLConnection对象对应的输出流
                            OutputStream printWriter = conn.getOutputStream();
                            // 发送请求参数
                            printWriter.write(main_json1.toString().getBytes());//post的参数 xx=xx&yy=yy
                            // flush输出流的缓冲
                            printWriter.flush();
                            printWriter.close();
                            //开始获取数据
                            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                try {
                                    String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                                    final JSONObject jsonObject = new JSONObject(jsonstr);
                                    //JSONObject back_json = jsonObject.getJSONObject("hd_data");
                                    System.out.println(jsonObject);
                                    //String collect_token = back_json.getString("collect_token");
                                    String like_token = jsonObject.getString("result_code");

                                    HashMap add_shujv = new HashMap();
                                    //add_shujv.put("hd_collect_token",back_json.getString("hd_collect_token"));
                                    add_shujv.put("like_token",like_token);
                                    System.out.println(add_shujv);
                                    Message msg=Message.obtain(hd_messeage1,3);
                                    msg.obj= add_shujv;
                                    hd_messeage1.sendMessage(msg);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (JSONException e){
                                        e.printStackTrace();
                        }
                    }
                }).start();
                hd_messeage1 = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        HashMap messeage = new HashMap();
                        messeage = (HashMap) msg.obj;
                        //Calendar calendar = Calendar.getInstance();
                        ImageView likeImage = (ImageView)findViewById(R.id.like_image);
                        if(messeage.get("like_token").equals("1"))
                        {
                            ;
                        }
                    }
                };
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        try {
                            main_json1 = new JSONObject();
                            if (collect.getDrawable() == drawable1) {
                                main_json1.put("collect", "1");
                                collect.setImageDrawable(drawable3);
                                String i = collect_text.getText().toString();
                                int j = 0;
                                j = Integer.parseInt(i) + 1;
                                collect_text.setText(j+"");
                            }
                            else{
                                main_json1.put("collect","0");
                                collect.setImageDrawable(drawable1);
                                String i = collect_text.getText().toString();
                                int j = 0;
                                j = Integer.parseInt(i) - 1;
                                collect_text.setText(j+"");
                            }
                            main_json1.put("hd_id", id);
                            main_json1.put("username", username);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                            String mainUrl1 = "http://118.89.199.187/school_social/jsp/hd_collect.jsp";
                            URL main_url1 = new URL(mainUrl1);
                            HttpURLConnection conn = (HttpURLConnection) main_url1.openConnection();
                            conn.setRequestMethod("POST");// 提交模式
                            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
                            // conn.setReadTimeout(2000);//读取超时 单位毫秒
                            // 发送POST请求必须设置如下两行
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setRequestProperty("Connection","keep-Alive");
                            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
                            // 获取URLConnection对象对应的输出流
                            OutputStream printWriter = conn.getOutputStream();
                            // 发送请求参数
                            printWriter.write(main_json1.toString().getBytes());//post的参数 xx=xx&yy=yy
                            // flush输出流的缓冲
                            printWriter.flush();
                            printWriter.close();
                            //开始获取数据
                            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                try {
                                    String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                                    final JSONObject jsonObject = new JSONObject(jsonstr);
                                    //JSONObject back_json = jsonObject.getJSONObject("hd_data");
                                    System.out.println(jsonObject);
                                    //String collect_token = back_json.getString("collect_token");
                                    String collect_token = jsonObject.getString("result_code");

                                    HashMap add_shujv = new HashMap();
                                    //add_shujv.put("hd_collect_token",back_json.getString("hd_collect_token"));
                                    add_shujv.put("collect_token",collect_token);
                                    System.out.println(add_shujv);
                                    Message msg=Message.obtain(hd_messeage2,3);
                                    msg.obj= add_shujv;
                                    hd_messeage2.sendMessage(msg);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                hd_messeage2 = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        HashMap messeage = new HashMap();
                        messeage = (HashMap) msg.obj;
                        //Calendar calendar = Calendar.getInstance();
                        //ImageView collectImage = (ImageView)findViewById(R.id.collect_image);
                        if(messeage.get("collect_token")=="1")
                        {
                            ;
                        }
                    }
                };
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject main_json = new JSONObject();
                    main_json.put("hd_id",id);
                    main_json.put("username",username);
                    String mainUrl = "http://118.89.199.187/school_social/jsp/hd_cakan.jsp";
                    URL main_url = new URL(mainUrl);
                    HttpURLConnection conn = (HttpURLConnection) main_url.openConnection();
                    conn.setRequestMethod("POST");// 提交模式
                    // conn.setConnectTimeout(10000);//连接超时 单位毫秒
                    // conn.setReadTimeout(2000);//读取超时 单位毫秒
                    // 发送POST请求必须设置如下两行
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Connection","keep-Alive");
                    conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
                    // 获取URLConnection对象对应的输出流
                    OutputStream printWriter = conn.getOutputStream();
                    // 发送请求参数
                    printWriter.write(main_json.toString().getBytes());//post的参数 xx=xx&yy=yy
                    // flush输出流的缓冲
                    printWriter.flush();
                    printWriter.close();
                    //开始获取数据
                    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        try {
                            String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                            final JSONObject jsonObject = new JSONObject(jsonstr);
                            JSONObject back_json = jsonObject.getJSONObject("hd_data");
                            System.out.println(back_json);

                            String id =  back_json.getString("hd_id");
                            String promulgator = back_json.getString("hd_fabuzhe");
                            String name = back_json.getString("hd_name");
                            String date = back_json.getString("hd_date");
                            String time =  back_json.getString("hd_time");
                            String site = back_json.getString("hd_site");
                            String neirong = back_json.getString("hd_neirong");
                            String like_number =  back_json.getString("hd_like_number");
                            String collect_number =  back_json.getString("hd_collect_number");
                            String type1 =  back_json.getString("hd_type1");
                            String collect_token = back_json.getString("collect_token");
                            String like_token = back_json.getString("like_token");
//                            String type2 = null, type3 = null, baoming_site = null, baoming_date = null, baoming_time = null;
//                            if(back_json.getString("hd_type2") != null) {
//                                type2 = back_json.getString("hd_type2");
//                            }
//                            if(back_json.getString("hd_type3") != null) {
//                                type3 = back_json.getString("hd_type3");
//                            }
//                            if(back_json.getString("hd_baoming_site") != null) {
//                                baoming_site = back_json.getString("hd_baoming_site");
//                            }
//                            if(back_json.getString("hd_baoming_date") != null) {
//                                baoming_date = back_json.getString("hd_baoming_date");
//                            }
//                            if(back_json.getString("hd_baoming_time") != null) {
//                                baoming_time = back_json.getString("hd_baoming_time");
//                            }
                            HashMap add_shujv = new HashMap();
                            add_shujv.put("hd_id",id);
                            add_shujv.put("hd_fabuzhe",promulgator);
                            add_shujv.put("hd_name",name);
                            add_shujv.put("hd_date",date);
                            add_shujv.put("hd_time",time);
                            add_shujv.put("hd_site",site);
                            add_shujv.put("hd_neirong",neirong);
                            add_shujv.put("hd_like_number",like_number);
                            add_shujv.put("hd_collect_number",collect_number);
                            add_shujv.put("hd_type1",type1);
                            add_shujv.put("hd_type2",back_json.getString("hd_type2"));
                            add_shujv.put("hd_type3",back_json.getString("hd_type3"));
                            add_shujv.put("hd_baoming_site",back_json.getString("hd_baoming_site"));
                            add_shujv.put("hd_baoming_date",back_json.getString("hd_baoming_date"));
                            add_shujv.put("hd_baoming_time",back_json.getString("hd_baoming_time"));
                            add_shujv.put("collect_token",back_json.getString("collect_token"));
                            add_shujv.put("like_token",back_json.getString("like_token"));
                            System.out.println(add_shujv);
                            Message msg=Message.obtain(hd_messeage,3);
                            msg.obj= add_shujv;
                            hd_messeage.sendMessage(msg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        hd_messeage = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                TextView cakanHdId = (TextView)findViewById(R.id.cakan_hd_id);
                TextView cakanBiaoti = (TextView)findViewById(R.id.cakan_biaoti);
                TextView cakanDatetime = (TextView)findViewById(R.id.cakan_datetime);
                TextView cakanSite = (TextView)findViewById(R.id.cakan_site);
                TextView cakanNeirong = (TextView)findViewById(R.id.cakan_neirong);
                TextView cakanLeixing = (TextView)findViewById(R.id.cakan_leixing);
                TextView cakanBaomingDatetime = (TextView)findViewById(R.id.cakan_baoming_datetime);
                TextView cakanBaomingSite = (TextView)findViewById(R.id.cakan_baoming_site);
                TextView cakanFabuzhe = (TextView)findViewById(R.id.cakan_fabuzhe);
                TextView cakanLikeNumber = (TextView)findViewById(R.id.hd_cakan_like);
                TextView cakanCollectNumber = (TextView)findViewById(R.id.hd_cakan_collect);

                ImageView likeImage = (ImageView)findViewById(R.id.like_image);
                ImageView collectImage = (ImageView)findViewById(R.id.collect_image);

                HashMap messeage = new HashMap();
                messeage = (HashMap) msg.obj;
                Log.d("tag", "handleMessage: "+messeage);
                Calendar calendar = Calendar.getInstance();
                cakanHdId.setText((String)messeage.get("hd_id"));
                cakanBiaoti.setText((String)messeage.get("hd_name"));
                cakanDatetime.setText("时间：" + messeage.get("hd_date") +"  "+ messeage.get("hd_time"));
                cakanSite.setText("地点：" + messeage.get("hd_site"));
                cakanNeirong.setText("内容：" + messeage.get("hd_neirong"));
                cakanLeixing.setText("类型：" + messeage.get("hd_type1")+"  "+messeage.get("hd_type2")+"  "+messeage.get("hd_type3"));
                cakanBaomingDatetime.setText("报名时间：" + messeage.get("hd_baoming_date")+"  "+messeage.get("hd_baoming_time"));
                cakanBaomingSite.setText("报名地点：" + messeage.get("hd_baoming_site"));
                cakanFabuzhe.setText("发布者：" + messeage.get("hd_fabuzhe"));
                cakanLikeNumber.setText((String)messeage.get("hd_like_number"));
                cakanCollectNumber.setText((String)messeage.get("hd_collect_number"));
                if(messeage.get("collect_token").equals("1"))
                {
                    collect.setImageDrawable(drawable3);
                }else {
                    collect.setImageDrawable(drawable1);
                }
                if(messeage.get("like_token").equals("1"))
                {
                    like.setImageDrawable(drawable2);
                }
                else
                    like.setImageDrawable(drawable);

            }
        };
    }
}
