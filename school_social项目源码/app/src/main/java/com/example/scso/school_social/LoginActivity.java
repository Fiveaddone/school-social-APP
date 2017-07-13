package com.example.scso.school_social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URL;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    private com.beardedhen.androidbootstrap.BootstrapEditText username;
    private com.beardedhen.androidbootstrap.BootstrapEditText password;
    private com.beardedhen.androidbootstrap.BootstrapButton bt_login;
    private TextView tv_Register;
    private TextView tv_FindPassword;
    private CheckBox remember;
    private CheckBox auto;
    private String phone;
    private String mima;
    private int json_power;
    private SharedPreferences sp;
    private Handler mHandler;
    private ProgressDialog pg;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        flag = getIntent().getIntExtra("flag",0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        sp = this.getSharedPreferences("userinfo",MODE_PRIVATE);
        if(flag==0) {
            if (sp.getBoolean("REMEMBER", false)) {
                //设置默认是记录密码状态
                remember.setChecked(true);
                username.setText(sp.getString("USER_NAME", ""));
                password.setText(sp.getString("PASSWORD", ""));
                //判断自动登陆多选框状态
                if (sp.getBoolean("AUTO", false)) {
                    //设置默认是自动登录状态
                    auto.setChecked(true);
                    //跳转界面
                    Toast.makeText(LoginActivity.this, "自动登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("phone", sp.getString("USER_NAME", ""));
                    intent.putExtra("power", sp.getInt("POWER", 0));
                    LoginActivity.this.startActivity(intent);
                }
            }
        }
        mHandler = new Handler(){
            public void handleMessage(Message msg) {
                pg.dismiss();
                switch (msg.arg1) {
                    case 0:
                        if (!mima.equals(msg.obj)) {
                            Toast.makeText(LoginActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            if(remember.isChecked()){
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("USER_NAME", phone);
                                editor.putString("PASSWORD",mima);
                                editor.putInt("POWER",json_power);
                                editor.commit();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("phone", phone);
                            intent.putExtra("power",json_power);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case 1:
                        Toast.makeText(LoginActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
        /*if(Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        tv_Register=(TextView)findViewById(R.id.xingyonghuzhuce);
        tv_FindPassword=(TextView)findViewById(R.id.wangjimima);
        tv_FindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,FindPasswordActivity1.class);
                startActivity(intent);
            }
        });
        tv_Register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent home=new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }else
            return super.onKeyDown(keyCode, event);
    }

    private void init() {
        username = (BootstrapEditText) findViewById(R.id.zhanghao);
        password = (BootstrapEditText) findViewById(R.id.mima);
        remember = (CheckBox)findViewById(R.id.remember);
        auto = (CheckBox)findViewById(R.id.auto);
        bt_login = (BootstrapButton)findViewById(R.id.denglu);
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(remember.isChecked()){
                    sp.edit().putBoolean("REMEMBER",true).commit();
                }
                else{
                    sp.edit().putBoolean("REMEMBER",false).commit();
                }
            }
        });
        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(auto.isChecked()){
                    sp.edit().putBoolean("AUTO",true).commit();
                    remember.setChecked(true);
                }
                else{
                    sp.edit().putBoolean("AUTO",false).commit();
                }
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                phone=username.getText().toString();
                mima=password.getText().toString();
                if(phone.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (mima.equals("")) {
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        pg = ProgressDialog.show(LoginActivity.this, "正在登录", "请稍后...");
                        Thread thread = new Thread(){
                            public void run(){
                                String myurl = "http://118.89.199.187/school_social/jsp/get_password.jsp?phone="+phone;
                                // Log.d("", "run:"+myurl);
                                try {
                                    URL url = new URL(myurl);
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.setConnectTimeout(10 * 1000);
                                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                        try {
                                            String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                                            final JSONObject jsonObject = new JSONObject(jsonstr);
                                            int json_code = jsonObject.getInt("result_code");
                                            if(json_code==1){
                                                String json_password=jsonObject.getString("password");
                                                json_power = jsonObject.getInt("power");
                                                Message msg = new Message();
                                                msg.arg1 = 0;
                                                msg.obj = json_password;
                                                mHandler.sendMessage(msg);
                                            }
                                           else{
                                                Message msg = new Message();
                                                msg.arg1 = 1;
                                                msg.obj = "用户不存在请注册";
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
                    }
                }
            }

        });
    }




}







