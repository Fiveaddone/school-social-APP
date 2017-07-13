package com.example.scso.school_social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;


import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class FindPasswordActivity extends Activity {

    private BootstrapEditText et_newpassword;
    private BootstrapEditText et_renewpassword;
    private BootstrapButton bt_enter;
    private ImageView iv_back;
    private TextView tv_back;
    private String newpassword;
    private String renwepassword;
    private String phone;
    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.arg1) {
                    case 0:
                        Toast.makeText(FindPasswordActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(FindPasswordActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FindPasswordActivity.this, LoginActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };
        Intent intent=getIntent();
        phone=(intent.getStringExtra("phone"));
        et_newpassword=(BootstrapEditText)findViewById(R.id.wangjimima_password);
        et_renewpassword=(BootstrapEditText)findViewById(R.id.wangjimima_repassword);
        bt_enter=(BootstrapButton)findViewById(R.id.wangjimima_queren);
        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newpassword=et_newpassword.getText().toString().replaceAll(" ","");
                renwepassword=et_renewpassword.getText().toString().replaceAll(" ","");
                if(newpassword.equals("")) Toast.makeText(FindPasswordActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                else if(newpassword.length()<6) Toast.makeText(FindPasswordActivity.this,"新密码不足6位",Toast.LENGTH_SHORT).show();
                else if(renwepassword.equals(""))Toast.makeText(FindPasswordActivity.this,"请填重复密码",Toast.LENGTH_SHORT).show();
                else if(!newpassword.equals(renwepassword))Toast.makeText(FindPasswordActivity.this,"密码不一致", Toast.LENGTH_SHORT).show();
                else {
                    Thread thread = new Thread(){
                        public void run(){
                            String myurl = "http://118.89.199.187/school_social/jsp/set_newpassword.jsp?phone="+phone+"&newpassword="+newpassword;
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
                                        int json_code = jsonObject.getInt("result_code");
                                        if(json_code==1){
                                            Message msg = new Message();
                                            msg.arg1 = 1;
                                            msg.obj = "修改密码成功";
                                            mHandler.sendMessage(msg);
                                        }
                                        else{
                                            Message msg = new Message();
                                            msg.arg1 = 0;
                                            msg.obj = "修改密码失败";
                                            mHandler.sendMessage(msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                    //Toast.makeText(FindPasswordActivity.this,"更改密码成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back();
    }
    private void back() {

        iv_back = (ImageView) findViewById(R.id.tab_back);
        tv_back = (TextView) findViewById(R.id.tab_back1);
        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FindPasswordActivity.this, FindPasswordActivity1.class);
                startActivity(intent);
                finish();
            }

        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FindPasswordActivity.this, FindPasswordActivity1.class);
                startActivity(intent);
                finish();
            }
        });
    };
}


