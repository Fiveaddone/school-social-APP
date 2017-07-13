package com.example.scso.school_social;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class FileInformationActivity extends Activity {
    private ImageView iv_back;
    private TextView tv_back;
    private TextView file_name;
    private  BootstrapButton file_load;
    private ImageView file_img;
    private TextView file_describe;
    private ImageView file_like;
    private String file_id;
    private String load_url;
    private String likes;
    private String username;
    private String target_user_id;
    private String target_user_name;
    private ListView pinglun_list;
    private JSONObject json;
    private BootstrapEditText pinglun_edit;
    private BootstrapButton pinglun_button;
    private BootstrapButton huifu_button;
    private ArrayList<HashMap<String,String>> pinglun_data_list;
    private String user_id;
    private String type="file";
    private Handler handler1;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_information);
        username=getIntent().getStringExtra("user_id");
        initview();
        back();
        Intent intent=getIntent();
        file_id=intent.getStringExtra("file_id");
        post(file_id,posthandler);
        pinglun();
        like();
        load();




    }
    public void pinglun(){
         handler1=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    Toast.makeText(FileInformationActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                    pinglun_edit.setText("");
                    get_data(handler);
                }
                else{
                    Toast.makeText(FileInformationActivity.this,"评论失败，请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        };
        pinglun_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pinglun_edit.getText())) {
                    Toast.makeText(FileInformationActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    json = new JSONObject();
                    try {
                        json.put("type", type);
                        json.put("id", file_id);
                        json.put("user_id", user_id);
                        json.put("content", pinglun_edit.getText().toString());
                        json.put("is_huifu", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpPost httpPost = new HttpPost(json, "pinglun.jsp", handler1);
                    httpPost.doPost();
                }
            }

        });
        huifu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PinglunActivity.this,"huifu",Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(pinglun_edit.getText())) {
                    Toast.makeText(FileInformationActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    json = new JSONObject();
                    try {
                        json.put("type", type);
                        json.put("id", file_id);
                        json.put("user_id", user_id);
                        json.put("is_huifu", 1);
                        json.put("target_user_id", target_user_id);
                        json.put("content", "回复 @" + target_user_name + " :" + pinglun_edit.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpPost httpPost = new HttpPost(json, "pinglun.jsp", handler1);
                    httpPost.doPost();
                }
            }
        });
        pinglun_list.setOnItemClickListener(new ListListener());

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    pinglun_data_list=new ArrayList<>();
                    JSONArray pinglunJSON=(JSONArray) msg.obj;
                    Log.e("a",pinglunJSON.toString());

                    try {
                        for (int i = 0; i < pinglunJSON.length(); i++) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("user_name", pinglunJSON.getJSONObject(i).getString("user_name"));
                            data.put("user_id",pinglunJSON.getJSONObject(i).getString("user_id"));
                            data.put("date_time",pinglunJSON.getJSONObject(i).getString("date_time"));
                            data.put("content",pinglunJSON.getJSONObject(i).getString("content"));
                            Log.e("666",data.toString());
                            pinglun_data_list.add(data);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    SimpleAdapter adapter=new SimpleAdapter(FileInformationActivity.this,pinglun_data_list,R.layout.pinglun_list_layout,
                            new String[] {"user_name","user_id","date_time","content"},new int[] {R.id.user_name,R.id.user_id,
                            R.id.date_time,R.id.content});
                    pinglun_list.setAdapter(adapter);
                }
                else{
                    Toast.makeText(FileInformationActivity.this,"获取评论失败",Toast.LENGTH_SHORT);
                }
            }
        };
        get_data(handler);

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK && huifu_button.getVisibility()==View.VISIBLE){
            huifu_button.setVisibility(View.GONE);
            pinglun_button.setVisibility(View.VISIBLE);
        }
        else{
            finish();
        }
        return false;
    }
    public class ListListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int now_position=position-parent.getFirstVisiblePosition();
            View v=(View)parent.getChildAt(now_position);
            TextView user_id_text=(TextView) v.findViewById(R.id.user_id);
            TextView user_name_text=(TextView) v.findViewById(R.id.user_name);

            //TextView id_text=(TextView)layout.findViewById(R.id.goods_id);
            target_user_id=user_id_text.getText().toString();
            target_user_name=user_name_text.getText().toString();
            pinglun_edit.clearFocus();
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0,InputMethodManager.SHOW_IMPLICIT);
            pinglun_button.setVisibility(View.GONE);
            huifu_button.setVisibility(View.VISIBLE);
        }
    }
    public void get_data(Handler handler){
        Log.d("tag", "get_data: ");
        JSONObject json=new JSONObject();
        try {
            json.put("type",type);
            json.put("id",file_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost=new HttpPost(json,"get_pinglun.jsp",handler);
        httpPost.doPost();
    }


    public void load() {
        file_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(FileInformationActivity.this);
                dialog.setTitle(file_name.getText().toString());
                dialog.setIcon(file_img.getDrawable());
                dialog.setMessage("是否确认下载");
                dialog.setCancelable(true);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://118.89.199.187/school_social/file" + load_url;
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setDestinationInExternalPublicDir("/school_social/"+username+"/", file_name.getText().toString());
                        Log.e("aa","http://118.89.199.187/school_social/file" + load_url);
                        Log.e("aa",file_name.getText().toString());
                        DownloadManager downloadManager = (DownloadManager) FileInformationActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                        long reference = downloadManager.enqueue(request);
                ////加入下载队列后会给该任务返回一个long型的id，
                //通过该id可以取消任务，重启任务等等
                //在通知栏中显示，默认就是显示的
                         request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.allowScanningByMediaScanner();//允许被扫描
                        request.setTitle("文件下载");
                        initFinishRecicever(reference);
                        }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }







        });
    }
    private void initFinishRecicever(final long reference) {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long references = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (references == reference) {
                    Toast.makeText(FileInformationActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }
    private Handler postlikehandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==4){
                file_like.setImageResource(R.drawable.like1);
                file_like.setTag("1");
            }
            else file_like.setTag("0");
        }
    };


    private Handler posthandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            setview((HashMap<String, String>) msg.obj);

        }

    };
    public void like()
    {
        file_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                if (file_like.getTag().equals("1")) {
                    file_like.setImageResource(R.drawable.like);
                    file_like.setTag("0");
                    try {
                        json.put("do", "quxiao");
                        json.put("user_id", username);
                        json.put("file_id", file_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    file_like.setImageResource(R.drawable.like1);
                    file_like.setTag("1");
                    try {
                        json.put("do", "dianzan");
                        json.put("user_id", username);
                        json.put("file_id", file_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                HttpPost httpPost = new HttpPost(json, "file_dianzan.jsp", new Handler());
                httpPost.doPost();
            }
        });


    }
    private void back()
    {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        finish();
                    }

        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        finish();


            }
        });
    }
    private void initview()
    {
        pinglun_list =(ListView) findViewById(R.id.pinglunlist);
        pinglun_edit = (BootstrapEditText) findViewById(R.id.pinglun_edit);
        pinglun_edit.clearFocus();
        pinglun_button= (BootstrapButton) findViewById(R.id.pinglun_button);
        huifu_button= (BootstrapButton) findViewById(R.id.huifu_button);
        file_describe= (TextView) findViewById(R.id.file_describe);
        file_img= (ImageView) findViewById(R.id.file_type);
        file_like= (ImageView) findViewById(R.id.like);
        file_name= (TextView) findViewById(R.id.file_name);
        file_load= (BootstrapButton) findViewById(R.id.file_load);
        iv_back=(ImageView) findViewById(R.id.tab_back);
        tv_back=(TextView)findViewById(R.id.tab_back1);

    }
    public void post(final String file_id, final Handler posthandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> service=new HashMap<>();
                String uriAPI = null;
                try {
                    uriAPI = "http://118.89.199.187/school_social/jsp/get_fileinformation.jsp?id=" + URLEncoder.encode(file_id,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    URL url=new URL(uriAPI);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10 * 1000);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                        final JSONObject jsonObject = new JSONObject(jsonstr);
                        JSONArray data = jsonObject.getJSONArray("Data");
                        for(int i=0;i<data.length();i++){

                            service.put("name",data.getJSONObject(i).getString("name"));
                            service.put("type",data.getJSONObject(i).getString("type"));
                            service.put("size",data.getJSONObject(i).getString("size"));
                            service.put("url",data.getJSONObject(i).getString("url"));
                            service.put("describe",data.getJSONObject(i).getString("describe"));
                            service.put("likes",data.getJSONObject(i).getString("likes"));
                        }
                        Message msg=Message.obtain(posthandler,3);
                        msg.obj=service;
                        posthandler.sendMessage(msg);


                        }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public void setview(HashMap<String,String> service){
        file_name.setText(service.get("name"));
        file_describe.setText("文件描述:\n"+service.get("describe"));
        file_load.setText("下载文件("+service.get("size")+")");
        likes=service.get("likes");
        load_url=service.get("url");
        user_id=username;
        String type=service.get("type");
        if (type.equals("txt"))file_img.setImageResource(R.drawable.txt);
        else  if (type.equals("docx")||type.equals("doc")||type.equals("docm")||type.equals("dotx")||type.equals("dotm"))file_img.setImageResource(R.drawable.docx);
        else  if (type.equals("pptx")||type.equals("pptm")||type.equals("ppt")||type.equals("ppsx")||type.equals("potx")||type.equals("potm"))file_img.setImageResource(R.drawable.ppt);
        else  if (type.equals("xls")||type.equals("xlt")||type.equals("xlsx")||type.equals("xlsm")||type.equals("xltx")||type.equals("xltm")||type.equals("xlsb"))file_img.setImageResource(R.drawable.excel);
        else  if (type.equals("rar")||type.equals("zip"))file_img.setImageResource(R.drawable.rar);
        else  if (type.equals("pdf"))file_img.setImageResource(R.drawable.pdf);
        else  if (type.equals("bmp")||type.equals("jpg")||type.equals("png")||type.equals("tiff")||type.equals("gif")||type.equals("pcx")
                ||type.equals("tga")||type.equals("exif")||type.equals("fpx") ||type.equals("svg")||type.equals("psd")||type.equals("cdr")
                ||type.equals("pcd")||type.equals("dxf")||type.equals("ufo")||type.equals("eps")||type.equals("ai")||type.equals("raw")
                ||type.equals("WMF"))file_img.setImageResource(R.drawable.picture);
        else  file_img.setImageResource(R.drawable.file_else);
        postlike(file_id,user_id,postlikehandler);


    }
    public void postlike(final String file_id, final String user_id,final Handler postlikehandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> service=new HashMap<>();
                String uriAPI = null;
                    uriAPI = "http://118.89.199.187/school_social/jsp/get_wholike.jsp?file_id=" + file_id+"&user_id="+user_id;
                try {
                    URL url=new URL(uriAPI);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10 * 1000);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                        final JSONObject jsonObject = new JSONObject(jsonstr);
                        JSONArray data = jsonObject.getJSONArray("Data");
                        if(String.valueOf(data).equals("[]")){
                            Message msg=Message.obtain(postlikehandler,3);
                            msg.arg1=8;
                            Log.w("asd", String.valueOf(msg.arg1));
                            postlikehandler.sendMessage(msg);

                        }
                        else {
                            Message msg=Message.obtain(postlikehandler,3);
                            msg.arg1=4;
                            Log.w("asd", String.valueOf(msg.arg1));
                            postlikehandler.sendMessage(msg);
                        }
                        Log.w("qwe", String.valueOf(String.valueOf(data).equals("[]")));
                        Log.w("asd", String.valueOf(data));




                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
