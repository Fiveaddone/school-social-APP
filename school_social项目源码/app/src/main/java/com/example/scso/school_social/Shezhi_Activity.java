package com.example.scso.school_social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
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
import java.util.ArrayList;

public class Shezhi_Activity extends Activity {
    private Button aihaoBaochun,aihaoQvxiao;
    private CheckBox kaoYan,baoYan,liuXue,xueShu,chuangYe,xueKe,dianJing,geChang,yingShi,wenXue,keJi,wanHui,geWu,xiangSheng,
            zhiJiao,zhiYuanZhe,yiGong,qiuLei,tianJing,diaoYan,diaoCa,sheJiao,gongZuo,zhaoXin,qiTa;
    private int like_number;
    private ArrayList<String> aihao = new ArrayList<>();
    private String username;
    private Handler shezhi_messeage = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shezhi_);
        aihaoBaochun = (Button) findViewById(R.id.aihao_baochun);
        aihaoQvxiao = (Button) findViewById(R.id.aihao_qvxiao);
        like_number = 0;

        kaoYan = (CheckBox) findViewById(R.id.kaoyan);
        baoYan = (CheckBox) findViewById(R.id.baoyan);
        liuXue = (CheckBox) findViewById(R.id.liuxue);
        xueShu = (CheckBox) findViewById(R.id.xueshu);
        chuangYe = (CheckBox) findViewById(R.id.chuangye);
        xueKe = (CheckBox) findViewById(R.id.xueke);
        dianJing = (CheckBox) findViewById(R.id.dianjing);
        geChang = (CheckBox) findViewById(R.id.gechang);
        yingShi = (CheckBox) findViewById(R.id.yingshi);
        wenXue = (CheckBox) findViewById(R.id.wenxue);
        keJi = (CheckBox) findViewById(R.id.keji);
        wanHui = (CheckBox) findViewById(R.id.wanhui);
        geWu = (CheckBox) findViewById(R.id.gewu);
        xiangSheng = (CheckBox) findViewById(R.id.xiangsheng);
        zhiJiao = (CheckBox) findViewById(R.id.zhijiao);
        zhiYuanZhe = (CheckBox) findViewById(R.id.zhiyuanzhe);
        yiGong = (CheckBox) findViewById(R.id.yigong);
        qiuLei = (CheckBox) findViewById(R.id.qiulei);
        tianJing = (CheckBox) findViewById(R.id.tianjing);
        diaoYan = (CheckBox) findViewById(R.id.diaoyan);
        diaoCa = (CheckBox) findViewById(R.id.diaoca);
        sheJiao = (CheckBox) findViewById(R.id.shejiao);
        gongZuo = (CheckBox) findViewById(R.id.gongzuo);
        zhaoXin = (CheckBox) findViewById(R.id.zhaoxin);
        qiTa = (CheckBox) findViewById(R.id.qita);

        kaoYan.setOnCheckedChangeListener(cheackBox_lisener);
        baoYan.setOnCheckedChangeListener(cheackBox_lisener);
        liuXue.setOnCheckedChangeListener(cheackBox_lisener);
        xueShu.setOnCheckedChangeListener(cheackBox_lisener);
        chuangYe.setOnCheckedChangeListener(cheackBox_lisener);
        xueKe.setOnCheckedChangeListener(cheackBox_lisener);
        dianJing.setOnCheckedChangeListener(cheackBox_lisener);
        geChang.setOnCheckedChangeListener(cheackBox_lisener);
        yingShi.setOnCheckedChangeListener(cheackBox_lisener);
        wenXue.setOnCheckedChangeListener(cheackBox_lisener);
        keJi.setOnCheckedChangeListener(cheackBox_lisener);
        wanHui.setOnCheckedChangeListener(cheackBox_lisener);
        geWu.setOnCheckedChangeListener(cheackBox_lisener);
        xiangSheng.setOnCheckedChangeListener(cheackBox_lisener);
        zhiJiao.setOnCheckedChangeListener(cheackBox_lisener);
        zhiYuanZhe.setOnCheckedChangeListener(cheackBox_lisener);
        yiGong.setOnCheckedChangeListener(cheackBox_lisener);
        qiuLei.setOnCheckedChangeListener(cheackBox_lisener);
        tianJing.setOnCheckedChangeListener(cheackBox_lisener);
        diaoYan.setOnCheckedChangeListener(cheackBox_lisener);
        diaoCa.setOnCheckedChangeListener(cheackBox_lisener);
        sheJiao.setOnCheckedChangeListener(cheackBox_lisener);
        gongZuo.setOnCheckedChangeListener(cheackBox_lisener);
        zhaoXin.setOnCheckedChangeListener(cheackBox_lisener);
        qiTa.setOnCheckedChangeListener(cheackBox_lisener);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String token = bundle.getString("token");
        if (token.equals("0")) {
            aihaoQvxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            aihaoBaochun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (like_number <= 0) {
                        Toast.makeText(Shezhi_Activity.this, "你没有选择爱好", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject main_json = new JSONObject();
                                    main_json.put("username", username);
                                    int number = 0;
                                    while (number < aihao.size()) {
                                        int i;
                                        i = number + 1;
                                        main_json.put("type" + i, aihao.get(number));
                                        number++;
                                    }
                                    main_json.put("number", number);
                                    String mainUrl = "http://118.89.199.187/school_social/jsp/hd_shezhi.jsp";
                                    URL main_url = new URL(mainUrl);
                                    HttpURLConnection conn = (HttpURLConnection) main_url.openConnection();
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
                                    printWriter.write(main_json.toString().getBytes());//post的参数 xx=xx&yy=yy
                                    // flush输出流的缓冲
                                    printWriter.flush();
                                    printWriter.close();
                                    //开始获取数据
                                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                        try {
                                            String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                                            final JSONObject jsonObject = new JSONObject(jsonstr);
                                            Message msg = Message.obtain(shezhi_messeage, 3);
                                            msg.obj = jsonObject;
                                            shezhi_messeage.sendMessage(msg);
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        shezhi_messeage = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                JSONObject back_json = (JSONObject) msg.obj;
                                try {
                                    if (back_json.getInt("result_code") == 1)
                                        Toast.makeText(Shezhi_Activity.this, "设置成功", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(Shezhi_Activity.this, "设置失败", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                    }
                }
            });
        }
        else if (token.equals("1")){
            TextView aihaoText = (TextView)findViewById(R.id.aihao_text);
            aihaoText.setText("类型选择");
            aihaoBaochun.setText("确认");
            aihaoBaochun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (like_number <= 0) {
                        Toast.makeText(Shezhi_Activity.this, "你没有选择活动类型", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        intent.putExtra("aihao", aihao);
                        setResult(1,intent);
                        finish();
                    }
                }
            });
            aihaoQvxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("aihao",aihao);
                    setResult(2,intent);
                    finish();
                }
            });
        }
    }

    private CompoundButton.OnCheckedChangeListener cheackBox_lisener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(compoundButton.isChecked()) {
                like_number++;
                aihao.add(compoundButton.getText().toString());
                if(like_number > 3){
                    Toast.makeText(Shezhi_Activity.this,"爱好选择超过3个",Toast.LENGTH_SHORT).show();
                    compoundButton.setChecked(false);
                    like_number--;
                    aihao.remove(like_number);
                }
            }
            else {
                like_number--;
                aihao.remove(compoundButton.getText().toString());
            }
            //Toast.makeText(Shezhi_Activity.this,aihao.toString(),Toast.LENGTH_SHORT).show();
        }
    };
}
