package com.example.scso.school_social;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class guanli_Activity extends Activity {
    private ListView guanli_listView ;
    private ArrayList<HashMap<String,Object>> hd_list_message = new ArrayList<>();
    private JSONObject guanli_json;
    private String username;
    private FragmentBottomGuanli f_guanli ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanli);
        get_data();
        startFragment();
    }

    public void get_data(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    guanli_json = new JSONObject();
                    guanli_json.put("username",username);
                    guanli_json.put("leibie","guanli");
                    String mainUrl = "http://118.89.199.187/school_social/jsp/hd_main.jsp";
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
                    printWriter.write(guanli_json.toString().getBytes());//post的参数 xx=xx&yy=yy
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
                            int number = 0;
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

    public void startFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        f_guanli = new FragmentBottomGuanli();//利用以上步骤实例化之前建立好的对象
        transaction.replace(R.id.framLayout2, f_guanli);//把这个对象放到我们的Framelayout里
        transaction.commit();                              //提交事务执行操作
    }

    public class FragmentBottomGuanli extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View v = View.inflate(getActivity(),R.layout.tuijianlayout,null);
            guanli_listView = (ListView) v.findViewById(R.id.listView);
            SimpleAdapter adapter = new SimpleAdapter( guanli_Activity.this,hd_list_message,R.layout.hd_list,
                    new String[] {"hd_id","hd_name","hd_date","hd_time","hd_site","hd_like_number","hd_collect_number"},
                    new int[] {R.id.hd_id,R.id.hd_name,R.id.hd_date,R.id.hd_time,R.id.hd_site,R.id.hd_like,R.id.hd_collect});
            guanli_listView.setAdapter(adapter);
            guanli_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = i-adapterView.getFirstVisiblePosition();
                    View v=adapterView.getChildAt(position);
                    TextView hdId = (TextView) v.findViewById(R.id.hd_id);
                    Intent intent = new Intent();
                    intent.putExtra("hd_id",hdId.getText().toString());
                    username=getIntent().getStringExtra("user_id");
                    intent.putExtra("user_id",username);
                    intent.setClass(guanli_Activity.this,cakan_Activity.class);
                    startActivity(intent);
                }
            });
            return v;
        }
    }
}
