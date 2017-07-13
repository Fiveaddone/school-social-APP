package com.example.scso.school_social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CYH on 2017/7/9.
 */

public class PersonInfoActivity extends Activity {
    private String user_id;
    private TextView user_name_text;
    private TextView school_text;
    private TextView realname_text;
    private TextView student_id_text;
    private TextView sex_text;
    private Button logout;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        user_id=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        user_name_text=(TextView) findViewById(R.id.user_name_text);
        school_text=(TextView)findViewById(R.id.school_text);
        realname_text=(TextView)findViewById(R.id.realname_text);
        student_id_text=(TextView)findViewById(R.id.student_id_text);
        sex_text=(TextView)findViewById(R.id.sex_text);
        logout=(Button)findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(PersonInfoActivity.this,LoginActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
                finish();
            }
        });




        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    JSONArray json=(JSONArray) msg.obj;
                    try {
                        user_name_text.setText("用户名："+json.getJSONObject(0).getString("user_name"));
                        school_text.setText("学校："+json.getJSONObject(0).getString("school"));
                        realname_text.setText("真实姓名："+json.getJSONObject(0).getString("realname"));
                        student_id_text.setText("学号："+json.getJSONObject(0).getString("studentID"));
                        sex_text.setText("性别："+json.getJSONObject(0).getString("sex"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        get_user_info();
    }
    public void get_user_info(){
        JSONObject jsonObject=new JSONObject();
        Log.d("tag", "get_user_info: "+user_id);
        try {
            jsonObject.put("user_id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost=new HttpPost(jsonObject,"get_user_info.jsp",handler);
        httpPost.doPost();
    }
    public void back(View v){
        finish();
    }
}
