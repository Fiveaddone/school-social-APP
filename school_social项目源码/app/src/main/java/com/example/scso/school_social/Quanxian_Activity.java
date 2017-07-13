package com.example.scso.school_social;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class Quanxian_Activity extends Activity {
    private Button quanxianQvxiao, quanxianQueren;
    private JSONObject shenqing = new JSONObject();
    private String shenqing_name;
    private Handler shenqing_messeage = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shenqing_name=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanxian);

        quanxianQvxiao = (Button)findViewById(R.id.quanxian_qvxiao);
        quanxianQueren = (Button)findViewById(R.id.quanxian_shenqing);

        quanxianQvxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        quanxianQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText zhengshiName = (EditText)findViewById(R.id.zhenshi_name);
                EditText lianxiPhone = (EditText)findViewById(R.id.lianxi_phone);
                EditText xiaoneiSite = (EditText)findViewById(R.id.xiaonei_site);
                EditText danWei = (EditText)findViewById(R.id.danwei);
                EditText zhiWei = (EditText)findViewById(R.id.zhiwei);
                String zhenshi_name = zhengshiName.getText().toString();
                String lianxi_phone = lianxiPhone.getText().toString();
                String xiaonei_site = xiaoneiSite.getText().toString();
                String danwei = danWei.getText().toString();
                String zhiwei = zhiWei.getText().toString();
                if(TextUtils.isEmpty(zhengshiName.getText()) || TextUtils.isEmpty(lianxi_phone) || TextUtils.isEmpty(xiaonei_site)||
                        TextUtils.isEmpty(danwei) || TextUtils.isEmpty(zhiwei)) {
                    Toast.makeText(Quanxian_Activity.this, "信息填写不完善", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        shenqing.put("zhenshi_name",zhenshi_name);
                        shenqing.put("lianxi_phone",lianxi_phone);
                        shenqing.put("xiaonei_site",xiaonei_site);
                        shenqing.put("danwei",danwei);
                        shenqing.put("zhiwei",zhiwei);
                        shenqing.put("shenqing_name",shenqing_name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String mainUrl = "http://118.89.199.187/school_social/jsp/hd_shenqing.jsp";
                                URL main_url = null;
                                main_url = new URL(mainUrl);
                                HttpURLConnection conn = null;
                                conn = (HttpURLConnection) main_url.openConnection();

                                conn.setRequestMethod("POST");// 提交模式
                                // conn.setConnectTimeout(10000);//连接超时 单位毫秒
                                // conn.setReadTimeout(2000);//读取超时 单位毫秒
                                // 发送POST请求必须设置如下两行
                                conn.setDoOutput(true);
                                conn.setDoInput(true);
                                conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
                                // 获取URLConnection对象对应的输出流
                                OutputStream printWriter = conn.getOutputStream();
                                // 发送请求参数
                                printWriter.write(shenqing.toString().getBytes());//post的参数 xx=xx&yy=yy
                                // flush输出流的缓冲
                                printWriter.flush();
                                printWriter.close();
                                //开始获取数据
                                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                    try {
                                        String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                                        final JSONObject jsonObject = new JSONObject(jsonstr);
                                        Message msg=Message.obtain(shenqing_messeage,3);
                                        msg.obj= jsonObject;
                                        shenqing_messeage.sendMessage(msg);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (ProtocolException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    shenqing_messeage = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            JSONObject back_json = (JSONObject)msg.obj;
                            try {
                                if(back_json.getInt("result_code")==1)
                                    Toast.makeText(Quanxian_Activity.this,"申请提交成功",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Quanxian_Activity.this,"申请提交失败",Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                }
            }
        });
    }
}
