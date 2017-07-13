package com.example.scso.school_social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

/**
 * Created by CYH on 2017/7/5.
 */

public class LiaotianActivity extends Activity {
    private String connect_user_id;
    private String connect_user_name;
    private String user_id;
    private Handler handler;
    private TextView user_name_text;
    private ProgressDialog ps;
    private LinearLayout content;
    private Drawable drawabel1;
    private Drawable drawable2;
    private Button go;
    private EditText mesg_edit;
    private Handler msg_handler;
    private Timer timer;
    private int time=500;
    private Thread get_mesg_thread;
    private boolean flag=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        connect_user_id=getIntent().getStringExtra("connect_user_id");
        user_id=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liaotian);
        //Toast.makeText(LiaotianActivity.this,"联系"+connect_user_id, Toast.LENGTH_SHORT).show();
        user_name_text=(TextView)findViewById(R.id.user_name);
        content=(LinearLayout) findViewById(R.id.content);
        go=(Button)findViewById(R.id.go_button);
        mesg_edit=(EditText)findViewById(R.id.mesg_edit);

        go.setOnClickListener(new goListener());


        drawabel1=getResources().getDrawable(R.drawable.edit_text_normal);
        drawabel1.setBounds(0,0,drawabel1.getMinimumWidth(),drawabel1.getMinimumHeight());
        drawable2=getResources().getDrawable(R.color.colorAccent);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    JSONArray jsonObject=(JSONArray) msg.obj;
                    if(jsonObject.length()==0){
                        if(time<10*1000){
                            time=time*2;
                        }
                        else{
                            time=10*1000;
                        }
                    }
                    else{
                        time=500;
                    }
                    try {
                        for (int i = 0; i < jsonObject.length(); i++) {
                            String mesg_id = jsonObject.getJSONObject(i).getString("id");
                            String mesg_content=jsonObject.getJSONObject(i).getString("content");
                            TextView mesg_view=new TextView(LiaotianActivity.this);
                            LinearLayout liner1=new LinearLayout(LiaotianActivity.this);
                            liner1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                            mesg_view.setCompoundDrawables(drawabel1,drawabel1,drawabel1,drawabel1);
                            mesg_view.setBackgroundColor(Color.parseColor("#0000ff"));
                            mesg_view.setPadding(5,5,5,5);
                            mesg_view.setCompoundDrawablePadding(30);
                            mesg_view.setTextSize(20);
                            //mesg_view.setTextColor(Color.parseColor("#ffffff"));
                            liner1.setGravity(Gravity.LEFT);
                            liner1.setPadding(10,10,150,10);
                            mesg_view.setText(mesg_content);
                            liner1.addView(mesg_view);
                            content.addView(liner1);
                            set_liaotian_state(mesg_id);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(LiaotianActivity.this,"获取失败,请稍后重试",Toast.LENGTH_SHORT);
                }

            }
        };
        msg_handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    Toast.makeText(LiaotianActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LiaotianActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                }
            }
        };

        set_user_name();

        get_mesg_thread=new Thread(){
            @Override
            public void run() {
                while(flag){
                    get_mesg();
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        get_mesg_thread.start();

    }

    public class goListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(mesg_edit.getText())) {
                Toast.makeText(LiaotianActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
            }
            else{
                JSONObject json = new JSONObject();
                String mesg_content=mesg_edit.getText().toString();
                try {
                    json.put("connect_user_id", connect_user_id);
                    json.put("user_id", user_id);
                    json.put("content",mesg_content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("tag", "onClick: "+json);
                HttpPost httpPost = new HttpPost(json, "send_mesg.jsp", msg_handler);
                httpPost.doPost();
                LinearLayout liner1=new LinearLayout(LiaotianActivity.this);
                TextView mesg_view=new TextView(LiaotianActivity.this);
                mesg_view.setCompoundDrawables(drawabel1,drawabel1,drawabel1,drawabel1);
                liner1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                mesg_view.setBackgroundColor(Color.parseColor("#ff0000"));
                mesg_view.setPadding(5,5,5,5);
                mesg_view.setCompoundDrawablePadding(30);
                mesg_view.setTextSize(20);
                mesg_view.setTextColor(Color.parseColor("#ffffff"));
                liner1.setGravity(Gravity.RIGHT);
                liner1.setPadding(150,10,10,10);
                mesg_view.setText(mesg_content);
                liner1.addView(mesg_view);
                content.addView(liner1);
                mesg_edit.setText("");
            }
        }
    }


    public void set_user_name(){
        Handler handler1=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ps.dismiss();
                if(msg.what==3){
                    JSONArray jsonObject=(JSONArray) msg.obj;
                    try {
                        for (int i = 0; i < jsonObject.length(); i++) {
                            connect_user_name = jsonObject.getJSONObject(i).getString("user_name");
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    user_name_text.setText(connect_user_name);
                }
                else{
                    Toast.makeText(LiaotianActivity.this,"获取失败,请稍后重试",Toast.LENGTH_SHORT);
                }
            }
        };
        ps=ProgressDialog.show(LiaotianActivity.this,"正在加载数据","加载中...");
        JSONObject json=new JSONObject();
        try {
            json.put("id",connect_user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost =new HttpPost(json,"get_user_name.jsp",handler1);
        httpPost.doPost();
    }

    public void get_mesg(){
            System.out.println(time);
            JSONObject json=new JSONObject();
            try {
                json.put("connect_user_id",connect_user_id);
                json.put("user_id",user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpPost httpPost =new HttpPost(json,"get_mesg.jsp",handler);
            httpPost.doPost();
    }

    public void set_liaotian_state(String id){
        JSONObject json=new JSONObject();
        try {
            json.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost =new HttpPost(json,"set_mesg_state.jsp",new Handler());
        httpPost.doPost();
    }
    public void back(View v){
        finish();
    }

    @Override
    protected void onDestroy() {
        flag=false;
        super.onDestroy();
    }
}
