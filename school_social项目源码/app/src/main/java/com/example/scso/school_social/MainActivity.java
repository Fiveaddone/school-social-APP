package com.example.scso.school_social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Button deal;
    private Button file;
    private Button activity;
    private Button manager;
    private int power;
    private String user_id;
    private ImageView user_info_img;
    private TextView user_info_name;
    private ListView liaotian_list;
    private Thread get_mesg_thread;
    private boolean flag=true;
    private Handler handler;
    private ArrayList<HashMap<String,String>> liao_list;
    private int time=3000;
    private TextView liaotian_notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent1=getIntent();
        user_id=intent1.getStringExtra("phone");
        power=intent1.getIntExtra("power",0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        deal=(Button)findViewById(R.id.deal);
        file=(Button)findViewById(R.id.file);
        activity=(Button)findViewById(R.id.activity);
        manager=(Button)findViewById(R.id.manager);
        liaotian_list=(ListView)findViewById(R.id.liaotian_list);
        liaotian_notice=(TextView)findViewById(R.id.liao_tian_notice);

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(power==1){
            manager.setVisibility(View.VISIBLE);
        }

        ButtonListener buttonListener=new ButtonListener();
        deal.setOnClickListener(buttonListener);
        file.setOnClickListener(buttonListener);
        activity.setOnClickListener(buttonListener);
        manager.setOnClickListener(buttonListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        user_info_img=(ImageView) v.findViewById(R.id.user_info_img);
        user_info_name=(TextView) v.findViewById(R.id.user_info_name);
        navigationView.setNavigationItemSelectedListener(this);

        user_info_img.setOnClickListener(new ImageOnListener());
        liaotian_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int now_position=position-parent.getFirstVisiblePosition();
                View v=(View) parent.getChildAt(now_position);
                TextView user_id_text=(TextView) v.findViewById(R.id.liao_tian_user_id);
                String connect_user_id=user_id_text.getText().toString();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,LiaotianActivity.class);
                intent.putExtra("connect_user_id",connect_user_id);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    liao_list=new ArrayList<>();
                    JSONArray json_list=(JSONArray) msg.obj;
                    Log.d("tag", "handleMessage: "+json_list);
                    try {
                        if(json_list.length()==0){
                            liaotian_notice.setText("最近没有聊天");
                        }
                        else {
                            Log.d("tag", "handleMessage: 1");
                            for (int i = 0; i < json_list.length(); i++) {
                                HashMap<String, String> liao_user = new HashMap<>();
                                liao_user.put("user_name", json_list.getJSONObject(i).getString("user_name"));
                                liao_user.put("target_user_id", json_list.getJSONObject(i).getString("user_id"));
                                liao_list.add(liao_user);
                            }
                            liaotian_notice.setText("以下是最近聊天对象");
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    SimpleAdapter simpleAdapter=new SimpleAdapter(MainActivity.this,liao_list,R.layout.liaotian_list,
                            new String[] {"user_name","target_user_id"},new int[] {R.id.liao_tian_user_name,R.id.liao_tian_user_id});
                    liaotian_list.setAdapter(simpleAdapter);
                }
            }
        };

        get_user_info();
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


    public void get_mesg(){
        JSONObject json=new JSONObject();
        try {
            json.put("user_id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost =new HttpPost(json,"get_mesg_list.jsp",handler);
        httpPost.doPost();
    }

    public class ImageOnListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,PersonInfoActivity.class);
            intent.putExtra("user_id",user_id);
            Log.d("tag", "onClick: "+user_id);
            startActivity(intent);
        }
    }


    public void get_user_info(){
        Handler hand_info=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    JSONArray user=(JSONArray) msg.obj;
                    try {
                        user_info_name.setText(user.getJSONObject(0).getString("user_name").toString());
                        Log.d("tag", "handleMessage: "+user_info_name.getText().toString());
                        String url="http://118.89.199.187/school_social/img/account/"+user.getJSONObject(0).getString("img_url").toString();
                        Log.d("tag", "handleMessage: "+url);
                        user_info_img.setTag(url);
                        GetImage getImage=new GetImage();
                        getImage.set_img(url,user_info_img);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        JSONObject json=new JSONObject();
        try {
            json.put("user_id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost =new HttpPost(json,"get_user_info.jsp",hand_info);
        httpPost.doPost();
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


    public class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            switch (v.getId()){
                case R.id.deal:
                    intent.setClass(MainActivity.this,DealActivity.class);
                    break;
                case R.id.activity:
                    intent.setClass(MainActivity.this,ActivActivity.class);
                    break;
                case R.id.file:
                    intent.setClass(MainActivity.this,FileActivity.class);
                    break;
                case R.id.manager:
                    intent.setClass(MainActivity.this,ManagerActivity.class);
                    break;
            }
            intent.putExtra("user_id",user_id);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent=new Intent();
        switch (id){
            case R.id.my_shoucang:
                intent.setClass(MainActivity.this, Shouchang_Activity.class);
                break;
            case R.id.my_goods:
                intent.setClass(MainActivity.this,MygoodsActivity.class);
                break;
            case R.id.my_file:
                intent.setClass(MainActivity.this,FileStoreManagerActivity.class);
                break;
            case R.id.my_service:
                intent.setClass(MainActivity.this, Myservice_Activity.class);
                break;
            case R.id.my_act:
                intent.setClass(MainActivity.this, guanli_Activity.class);
                break;
        }
        intent.putExtra("user_id",user_id);
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
