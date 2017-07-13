package com.example.scso.school_social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
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

public class fabu_Activity extends Activity {
    private EditText fabuHdtime,fabuHddate,fabuHdname,fabuHdsite,fabuHdjianjie,fabuHdleixing,baomingSite,
            baominghdDate,baominghdTime;
    private DatePicker fabuDate,baomingDate;
    private TimePicker fabuTime,baomingTime;
    private Button fabuQvxiao,fabuQueren,dateQueren,dateQvxiao,timeQvxiao,timeQueren,baomingDateQvxiao,baomingDateQueren
            ,baomingTimeQvxiao,baomingTimeQueren;
    private LinearLayout beijingLinear2,dateLinear,timeLinear,beijingLinear3,beijingLinear4,beijingLinear5,baomingDateLinear
            ,baomingTimeLinear;
    private ArrayList<String> aihao = new ArrayList<>();
    private String username;
    private String[] type = new String[3];
    private JSONObject fabu_hd_message = new JSONObject();
    Handler fabu_messeage = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        fabuHddate = (EditText) findViewById(R.id.fabu_hddate);
        fabuHdtime = (EditText) findViewById(R.id.fabu_hdtime);
        fabuHdname = (EditText) findViewById(R.id.fabu_hdname);
        fabuHdsite = (EditText) findViewById(R.id.fabu_hdsite);
        fabuHdjianjie = (EditText) findViewById(R.id.fabu_hdjianjie);
        fabuHdleixing = (EditText) findViewById(R.id.fabu_hdleixing);
        baominghdDate = (EditText) findViewById(R.id.baoming_hddate);
        baominghdTime = (EditText) findViewById(R.id.baoming_hdtime);
        baomingSite = (EditText) findViewById(R.id.baoming_hdsite);

        fabuDate = (DatePicker) findViewById(R.id.fabu_date);
        baomingDate = (DatePicker) findViewById(R.id.baoming_date);

        fabuTime = (TimePicker) findViewById(R.id.fabu_time);
        baomingTime = (TimePicker) findViewById(R.id.baoming_time);

        fabuQvxiao = (Button)findViewById(R.id.fabu_qvxiao);
        fabuQueren = (Button)findViewById(R.id.fabu_queren);
        dateQueren = (Button)findViewById(R.id.date_queren);
        dateQvxiao = (Button)findViewById(R.id.date_qvxiao);
        timeQueren = (Button)findViewById(R.id.time_queren);
        timeQvxiao = (Button)findViewById(R.id.time_qvxiao);
        baomingDateQueren = (Button)findViewById(R.id.baoming_date_queren);
        baomingDateQvxiao = (Button)findViewById(R.id.baoming_date_qvxiao);
        baomingTimeQueren = (Button)findViewById(R.id.baoming_time_queren);
        baomingTimeQvxiao = (Button)findViewById(R.id.baoming_time_qvxiao);

        beijingLinear2 = (LinearLayout)findViewById(R.id.beijing_linear2);
        dateLinear = (LinearLayout)findViewById(R.id.date_linear);
        beijingLinear3 = (LinearLayout)findViewById(R.id.beijing_linear3);
        timeLinear = (LinearLayout)findViewById(R.id.time_linear);
        beijingLinear4 = (LinearLayout)findViewById(R.id.beijing_linear4);
        baomingDateLinear = (LinearLayout)findViewById(R.id.baoming_date_linear);
        beijingLinear5 = (LinearLayout)findViewById(R.id.beijing_linear5);
        baomingTimeLinear = (LinearLayout)findViewById(R.id.baoming_time_linear);

        fabuTime.setIs24HourView(true);
        baomingTime.setIs24HourView(true);

        beijingLinear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateLinear.setVisibility(View.GONE);
                beijingLinear2.setVisibility(View.GONE);
            }
        });
        beijingLinear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLinear.setVisibility(View.GONE);
                beijingLinear3.setVisibility(View.GONE);
            }
        });
        beijingLinear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baomingDateLinear.setVisibility(View.GONE);
                beijingLinear4.setVisibility(View.GONE);
            }
        });
        beijingLinear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baomingTimeLinear.setVisibility(View.GONE);
                beijingLinear5.setVisibility(View.GONE);
            }
        });

        fabuHddate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (beijingLinear2.getVisibility() == View.GONE) {
                                beijingLinear2.setVisibility(View.VISIBLE);
                                beijingLinear2.bringToFront();
                            } else
                                beijingLinear2.setVisibility(View.GONE);
                            if (dateLinear.getVisibility() == View.GONE) {
                                dateLinear.setVisibility(View.VISIBLE);
                                dateLinear.bringToFront();
                            } else
                                dateLinear.setVisibility(View.GONE);
                        }return true;}
        });

        fabuHdtime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(beijingLinear3.getVisibility()==View.GONE)
                    {
                        beijingLinear3.setVisibility(View.VISIBLE);
                        beijingLinear3.bringToFront();
                    }
                    else
                        beijingLinear3.setVisibility(View.GONE);
                    if(timeLinear.getVisibility()==View.GONE)
                    {
                        timeLinear.setVisibility(View.VISIBLE);
                        timeLinear.bringToFront();
                    }
                    else
                        timeLinear.setVisibility(View.GONE);
                }
                return true;
            }
        });

        fabuHdleixing.setInputType(InputType.TYPE_NULL);
        fabuHdleixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("token", "1");
                intent.setClass(fabu_Activity.this, Shezhi_Activity.class);
                startActivityForResult(intent, 1);
            }
        });



        baominghdDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(beijingLinear4.getVisibility()==View.GONE)
                    {
                        beijingLinear4.setVisibility(View.VISIBLE);
                        beijingLinear4.bringToFront();
                    }
                    else
                        beijingLinear4.setVisibility(View.GONE);
                    if(baomingDateLinear.getVisibility()==View.GONE)
                    {
                        baomingDateLinear.setVisibility(View.VISIBLE);
                        baomingDateLinear.bringToFront();
                    }
                    else
                        baomingDateLinear.setVisibility(View.GONE);
                }
                return true;
            }
        });

        baominghdTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(beijingLinear5.getVisibility()==View.GONE)
                    {
                        beijingLinear5.setVisibility(View.VISIBLE);
                        beijingLinear5.bringToFront();
                    }
                    else
                        beijingLinear5.setVisibility(View.GONE);
                    if(baomingTimeLinear.getVisibility()==View.GONE)
                    {
                        baomingTimeLinear.setVisibility(View.VISIBLE);
                        baomingTimeLinear.bringToFront();
                    }
                    else
                        baomingTimeLinear.setVisibility(View.GONE);
                }
                return true;
            }
        });

        fabuQvxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fabuQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hd_name = fabuHdname.getText().toString();
                String hd_site = fabuHdsite.getText().toString();
                String hd_jianjie = fabuHdjianjie.getText().toString();
                String hd_date = fabuHddate.getText().toString();
                String hd_time = fabuHdtime.getText().toString();
                ArrayList<String> hd_leixing = aihao;
                String hd_baoming_date = baominghdDate.getText().toString();
                String hd_baoming_time = baominghdTime.getText().toString();
                String hd_baoming_sie = baomingSite.getText().toString();
                if(TextUtils.isEmpty(hd_name) ||TextUtils.isEmpty(hd_site) ||TextUtils.isEmpty(hd_date)||TextUtils.isEmpty(hd_jianjie)||
                        TextUtils.isEmpty(hd_time)|| TextUtils.isEmpty(fabuHdleixing.getText().toString())){
                    Toast.makeText(fabu_Activity.this, "信息填写不完善", Toast.LENGTH_SHORT).show();
                }
                else{
                    fabu_hd_message = new JSONObject();
                    try {
                        fabu_hd_message.put("hd_name",hd_name);
                        fabu_hd_message.put("hd_site",hd_site);
                        fabu_hd_message.put("hd_date",hd_date);
                        fabu_hd_message.put("hd_time",hd_time);
                        fabu_hd_message.put("hd_jianjie",hd_jianjie);
                        fabu_hd_message.put("type1",type[0]);
                        fabu_hd_message.put("type2","");
                        fabu_hd_message.put("type3","");
                        fabu_hd_message.put("hd_baoming_date","");
                        fabu_hd_message.put("hd_baoming_site","");
                        fabu_hd_message.put("hd_baoming_time","");
                        if(type[1] != null)
                            fabu_hd_message.put("type2",type[1]);
                        if(type[2] != null)
                            fabu_hd_message.put("type3",type[2]);
                        if(hd_baoming_date != null)
                            fabu_hd_message.put("hd_baoming_date",hd_baoming_date);
                        if(hd_baoming_sie != null)
                            fabu_hd_message.put("hd_baoming_site",hd_baoming_sie);
                        if(hd_baoming_time != null)
                            fabu_hd_message.put("hd_baoming_time",hd_baoming_time);
                        fabu_hd_message.put("hd_user_name",username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(fabu_hd_message);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String mainUrl = "http://118.89.199.187/school_social/jsp/hd_fabu.jsp";
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
                                printWriter.write(fabu_hd_message.toString().getBytes());//post的参数 xx=xx&yy=yy
                                // flush输出流的缓冲
                                printWriter.flush();
                                printWriter.close();
                                System.out.print("111111111111111111111111");
                                //开始获取数据
                                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                    try {
                                        System.out.print("111111111111111111111111");
                                        String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                                        final JSONObject jsonObject = new JSONObject(jsonstr);
                                        Message msg = Message.obtain(fabu_messeage, 3);
                                        msg.obj = jsonObject;
                                        fabu_messeage.sendMessage(msg);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    System.out.print("错误");
                                }
                            } catch (ProtocolException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    fabu_messeage = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            JSONObject back_json = (JSONObject) msg.obj;
                            try {
                                if (back_json.getInt("result_code") == 1)
                                    Toast.makeText(fabu_Activity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(fabu_Activity.this, "发布失败", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                }
            }
        });
        dateQvxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateLinear.setVisibility(View.GONE);
                beijingLinear2.setVisibility(View.GONE);
            }
        });
        timeQvxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeLinear.setVisibility(View.GONE);
                beijingLinear3.setVisibility(View.GONE);
            }
        });
        baomingDateQvxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baomingDateLinear.setVisibility(View.GONE);
                beijingLinear4.setVisibility(View.GONE);
            }
        });
        baomingTimeQvxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baomingTimeLinear.setVisibility(View.GONE);
                beijingLinear5.setVisibility(View.GONE);
            }
        });

        dateQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = fabuDate.getYear();
                String month="";
                if((fabuDate.getMonth() +1)<10){
                    month="0"+(fabuDate.getMonth() +1);
                }
                else{
                    month=(fabuDate.getMonth() +1)+"";
                }
                String day="";

                if(fabuDate.getDayOfMonth()<10){
                    day="0"+fabuDate.getDayOfMonth();
                }
                else{
                    day=fabuDate.getDayOfMonth()+"";
                }
                fabuHddate.setText(year + "-" + month +"-"+ day);
                dateLinear.setVisibility(View.GONE);
                beijingLinear2.setVisibility(View.GONE);
            }
        });
        baomingDateQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = baomingDate.getYear();
                int month = baomingDate.getMonth() +1;
                int day =baomingDate.getDayOfMonth();
                baominghdDate.setText(year + "-" + month +"-"+ day);
                baomingDateLinear.setVisibility(View.GONE);
                beijingLinear4.setVisibility(View.GONE);
            }
        });
        timeQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int hour = fabuTime.getCurrentHour();
                    int min = fabuTime.getCurrentMinute();
                    String hour_String="";
                    if(hour<10){
                        hour_String="0"+hour;
                    }
                    else{
                        hour_String=hour+"";
                    }
                    String min_String="";

                    if(min<10){
                        min_String="0"+min;
                    }
                    else{
                        min_String=min+"";
                    }
                    fabuHdtime.setText(hour_String + ":" + min_String);
                    timeLinear.setVisibility(View.GONE);
                    beijingLinear3.setVisibility(View.GONE);
                }catch (Exception e){
                    Log.e("-----",e.getMessage());
                }
               /* final TimePickerDialog dialog = new TimePickerDialog(fabu_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Toast.makeText(fabu_Activity.this,timePicker.getCurrentHour(),Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                    }
                },12,12,true);
                dialog.show();*/

            }
        });
        baomingTimeQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = baomingTime.getCurrentHour();
                int min = baomingTime.getCurrentMinute();
                baominghdTime.setText(hour + ":" + min);
                baomingTimeLinear.setVisibility(View.GONE);
                beijingLinear5.setVisibility(View.GONE);
            }
        });
    }
    protected  void onActivityResult(int requestCode,int resultCode,Intent date){
        super.onActivityResult(requestCode,resultCode,date);
        if(resultCode == 1){
            Bundle bundle = new Bundle();
            bundle = date.getExtras();
            aihao = bundle.getStringArrayList("aihao");
            int i = 0;
            while (i < aihao.size()) {
                type[i] = aihao.get(i);
                i++;
            }
            fabuHdleixing.setText(aihao.toString());
        }
    }
}
