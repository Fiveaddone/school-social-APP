package com.example.scso.school_social;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ActivActivity extends Activity implements View.OnClickListener{
    private Button tuijian,remen,jiangzuo,bishai,biaoyan,gongyi,tiyv,shijian,qita,fabu,guanli,shenqing;
    //我们之前定义的三个Fragment，分别声明三个这样的类的对象

    public FragmentBottomTuijian f_tuijian;

    private ImageButton jiahaoButton,shezhiButton,shouchangButton;

    private ListView hd_listView;

    private LinearLayout jiahaoLinear,beijingLinear;

    private ArrayList<HashMap<String,Object>> hd_list_message ;

    private String leibie , userName , mainUrl;

    private ResultSet rs ;
    private Connection conn ;

    private JSONObject main_json;

    private int number;
    private Handler power_messeage= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userName=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);

        mainUrl = "http://118.89.199.187/school_social/jsp/hd_main.jsp";
        leibie = "tuijian";

        hd_list_message = new ArrayList<>();

        //实例化layout并配置监听器
        tuijian = (Button) findViewById(R.id.tuijian);
        remen = (Button) findViewById(R.id.remen);
        jiangzuo = (Button) findViewById(R.id.jiangzuo);
        bishai = (Button) findViewById(R.id.bishai);
        biaoyan = (Button) findViewById(R.id.biaoyan);
        gongyi = (Button) findViewById(R.id.gongyi);
        tiyv = (Button) findViewById(R.id.tiyv);
        shijian = (Button) findViewById(R.id.shijian);
        qita = (Button) findViewById(R.id.qita);
        fabu = (Button) findViewById(R.id.fabu_button);
        guanli = (Button) findViewById(R.id.guanli_button);
        shenqing = (Button) findViewById(R.id.quanxian_button);

        jiahaoButton = (ImageButton)findViewById(R.id.imageButton1);
        shezhiButton = (ImageButton)findViewById(R.id.shezhi_button);
        shouchangButton = (ImageButton)findViewById(R.id.shouchang_button);

        jiahaoLinear = (LinearLayout)findViewById(R.id.jiahao_linear);
        beijingLinear = (LinearLayout)findViewById(R.id.beijing_linear);

        tuijian.setOnClickListener(this);
        remen.setOnClickListener(this);
        jiangzuo.setOnClickListener(this);
        bishai.setOnClickListener(this);
        biaoyan.setOnClickListener(this);
        gongyi.setOnClickListener(this);
        tiyv.setOnClickListener(this);
        shijian.setOnClickListener(this);
        qita.setOnClickListener(this);

        shouchangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivActivity.this, Shouchang_Activity.class);
                intent.putExtra("user_id",userName);
                startActivity(intent);
            }
        });

        beijingLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beijingLinear.setVisibility(View.GONE);
                jiahaoLinear.setVisibility(View.GONE);
            }
        });
        jiahaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(beijingLinear.getVisibility() == View.GONE){
                    beijingLinear.setVisibility(View.VISIBLE);
                    beijingLinear.bringToFront();
                }
                else {
                    beijingLinear.setVisibility(View.GONE);
                }

                if(jiahaoLinear.getVisibility() == View.GONE){
                    jiahaoLinear.setVisibility(View.VISIBLE);
                    jiahaoLinear.bringToFront();
                }
                else
                    jiahaoLinear.setVisibility(View.GONE);
            }
        });
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject username = new JSONObject();
                try {
                    username.put("username",userName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String mainUrl = "http://118.89.199.187/school_social/jsp/hd_power.jsp";
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
                            printWriter.write(username.toString().getBytes());//post的参数 xx=xx&yy=yy
                            // flush输出流的缓冲
                            printWriter.flush();
                            printWriter.close();
                            //开始获取数据
                            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                try {
                                    String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                                    final JSONObject jsonObject = new JSONObject(jsonstr);
                                    Message msg = Message.obtain(power_messeage, 3);
                                    msg.obj = jsonObject;
                                    power_messeage.sendMessage(msg);
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
                power_messeage = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        JSONObject back_json = (JSONObject) msg.obj;
                        try {
                            if (back_json.getInt("power") == 1) {
                                Intent intent = new Intent(ActivActivity.this, fabu_Activity.class);
                                intent.putExtra("user_id",userName);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(ActivActivity.this, "权限不足，无法发布，请先申请权限", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        });
        guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject username = new JSONObject();
                try {
                    username.put("username",userName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String mainUrl = "http://118.89.199.187/school_social/jsp/hd_power.jsp";
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
                            printWriter.write(username.toString().getBytes());//post的参数 xx=xx&yy=yy
                            // flush输出流的缓冲
                            printWriter.flush();
                            printWriter.close();
                            //开始获取数据
                            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                                try {
                                    String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                                    final JSONObject jsonObject = new JSONObject(jsonstr);
                                    Message msg = Message.obtain(power_messeage, 3);
                                    msg.obj = jsonObject;
                                    power_messeage.sendMessage(msg);
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
                power_messeage = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        JSONObject back_json = (JSONObject) msg.obj;
                        try {
                            if (back_json.getInt("power") == 1) {
                                Intent intent = new Intent(ActivActivity.this,guanli_Activity.class);
                                intent.putExtra("user_id",userName);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(ActivActivity.this, "没有管理活动权限，无法进行活动管理", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        });
        shenqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivActivity.this,Quanxian_Activity.class);
                intent.putExtra("user_id",userName);
                startActivity(intent);
            }
        });
        shezhiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("token","0");
                intent.putExtra("user_id",userName);
                intent.setClass(ActivActivity.this,Shezhi_Activity.class);
                startActivity(intent);
            }
        });

        get_data();
        startFragment();
    }

    public void get_data(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    main_json = new JSONObject();
                    main_json.put("username",userName);
                    main_json.put("leibie",leibie);
                    URL main_url = new URL(mainUrl);
                    HttpURLConnection conn = (HttpURLConnection) main_url.openConnection();
                    conn.setRequestMethod("POST");// 提交模式
                    // conn.setConnectTimeout(10000);//连接超时 单位毫秒
                    // conn.setReadTimeout(2000);//读取超时 单位毫秒
                    // 发送POST请求必须设置如下两行
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Connection","keep-Alive");
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
                            JSONArray back_json = jsonObject.getJSONArray("hd_Data");
                            System.out.println(back_json);
                            number = 0;
                            for (;number < back_json.length();number++){
                                int id =  back_json.getJSONObject(number).getInt("hd_id");
                                //String promulgator = (String) back_json.getJSONObject(number).get("hd_fabuzhe");
                                String name = (String) back_json.getJSONObject(number).get("hd_name");
                                String date = back_json.getJSONObject(number).getString("hd_date");
                                String time =  back_json.getJSONObject(number).getString("hd_time");
                                String site = (String) back_json.getJSONObject(number).get("hd_site");
                                int like_number =  back_json.getJSONObject(number).getInt("hd_like_number");
                                int collect_number =  back_json.getJSONObject(number).getInt("hd_collect_number");
                                HashMap add_shujv = new HashMap<String,Objects>();
                                add_shujv.put("hd_id",id);
                                //add_shujv.put("hd_fabuzhe",promulgator);
                                add_shujv.put("hd_name",name);
                                add_shujv.put("hd_date",date);
                                add_shujv.put("hd_time",time);
                                add_shujv.put("hd_site",site);
                                add_shujv.put("hd_like_number",like_number);
                                add_shujv.put("hd_collect_number",collect_number);
                                hd_list_message.add(add_shujv);
                            }
                            //System.out.println(hd_list_message.get(0));
                            startFragment();
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
    }

    public void link_sql(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://118.89.199.187:3306/school_social", "root", "root");
                    getHashMap("SELECT hd_promulgator,hd_name,hd_date,hd_time,hd_site,hd_like_number,hd_collect_number FROM hd");
                    Log.e("-----------","success");
                } catch (SQLException e) {
                    Log.e("-----------",e.getMessage());
                }
            }
        }).start();

    }

    public void getHashMap(String sql_yvjv){
        int number=0;
        String yvjv = sql_yvjv;
        try {
            //rs = conn.createStatement().executeQuery("SELECT hd_promulgator,hd_name,hd_date,hd_time,hd_site,hd_like_number,hd_collect_number FROM hd");
            rs = conn.createStatement().executeQuery(yvjv);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (rs.next()) {
                String promulgator = rs.getString("hd_promulgator");
                String name = rs.getString("hd_name");
                Date date = rs.getDate("hd_date");
                Time time = rs.getTime("hd_time");
                String site = rs.getString("hd_site");
                int like_number = rs.getInt("hd_like_number");
                int collect_number = rs.getInt("hd_collect_number");
                HashMap add_shujv = new HashMap<String,Objects>();
                add_shujv.put("hd_fabuzhe",promulgator);
                add_shujv.put("hd_name",name);
                add_shujv.put("hd_date",date);
                add_shujv.put("hd_time",time);
                add_shujv.put("hd_site",site);
                add_shujv.put("hd_like_number",like_number);
                add_shujv.put("hd_collect_number",collect_number);
                hd_list_message.add(add_shujv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        f_tuijian = new FragmentBottomTuijian();//利用以上步骤实例化之前建立好的对象
        transaction.replace(R.id.framLayout1, f_tuijian);//把这个对象放到我们的Framelayout里
        transaction.commit();                              //提交事务执行操作
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.tuijian: {
                leibie="tuijian";
                hd_list_message.clear();
                get_data();
                //f_tuijian = new FragmentBottomTuijian();
                //transaction.replace(R.id.framLayout1, f_tuijian);
                break;
            }
            case R.id.remen:{
                leibie="remen";
                hd_list_message.clear();
                get_data();
                //FragmentBottomTuijian f_remen = new FragmentBottomTuijian();
                //transaction.replace(R.id.framLayout1, f_remen);
                break;
            }
            case R.id.jiangzuo: {
                leibie= "jiangzuo";
                hd_list_message.clear();
                get_data();
                //FragmentBottomTuijian f_jiangzuo = new FragmentBottomTuijian();
                //transaction.replace(R.id.framLayout1, f_jiangzuo);
                break;
            }
            case R.id.bishai: {
                leibie = "bishai";
                hd_list_message.clear();
                get_data();
                //f_bishai = new FragmentBottomBishai();
                //transaction.replace(R.id.framLayout1, f_bishai);
                break;
            }
            case R.id.biaoyan: {
                leibie = "biaoyan";
                hd_list_message.clear();
                get_data();
                //f_biaoyan = new FragmentBottomBiaoyan();
                //transaction.replace(R.id.framLayout1, f_biaoyan);
                break;
            }
            case R.id.gongyi: {
                leibie = "gongyi";
                hd_list_message.clear();
                get_data();
                //f_gongyi = new FragmentBottomGongyi();
                //transaction.replace(R.id.framLayout1, f_gongyi);
                break;
            }
            case R.id.tiyv: {
                leibie = "tiyv";
                hd_list_message.clear();
                get_data();
                //f_tiyv = new FragmentBottomTiyv();
                //transaction.replace(R.id.framLayout1, f_tiyv);
                break;
            }
            case R.id.shijian: {
                leibie = "shijian";
                hd_list_message.clear();
                get_data();
                //f_shijian = new FragmentBottomShijian();
                //transaction.replace(R.id.framLayout1, f_shijian);
                break;
            }
            case R.id.qita: {
                leibie = "qita";
                hd_list_message.clear();
                get_data();
                //f_qita = new FragmentBottomQita();
                //transaction.replace(R.id.framLayout1, f_qita);
                break;
            }
        }
        transaction.commit();
    }

    public class FragmentBottomTuijian extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View v = View.inflate(getActivity(),R.layout.tuijianlayout,null);
            hd_listView = (ListView) v.findViewById(R.id.listView);
            SimpleAdapter adapter = new SimpleAdapter( ActivActivity.this,hd_list_message,R.layout.hd_list,
                    new String[] {"hd_id","hd_name","hd_date","hd_time","hd_site","hd_like_number","hd_collect_number"},
                    new int[] {R.id.hd_id,R.id.hd_name,R.id.hd_date,R.id.hd_time,R.id.hd_site,R.id.hd_like,R.id.hd_collect});
            hd_listView.setAdapter(adapter);
            hd_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = i-adapterView.getFirstVisiblePosition();
                    View v=adapterView.getChildAt(position);
                    TextView hdId = (TextView) v.findViewById(R.id.hd_id);
                    Intent intent = new Intent();
                    intent.putExtra("hd_id",hdId.getText().toString());
                    intent.putExtra("user_id",userName);
                    intent.setClass(ActivActivity.this,cakan_Activity.class);
                    startActivity(intent);
                }
            });
            return v;
        }
    }
}

