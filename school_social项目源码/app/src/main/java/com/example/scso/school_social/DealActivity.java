package com.example.scso.school_social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DealActivity extends Activity {
    private String user_id;
    private Handler handler;
    private ListView listview;
    private ArrayList<HashMap<String,Object>> list=new ArrayList<>();
    private String sql;
    private LinearLayout market_linear;
    private LinearLayout service_linear;
    private LinearLayout add_function;
    private LinearLayout zhedang;
    private ImageView search;
    private EditText edit_name;
    private TextView title;
    private  TextView notice;
    private String name="";
    //    private TextView add_commodity;
//    private TextView apply_service;
    private ImageView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user_id=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);



        listview=(ListView)findViewById(R.id.listview);
        market_linear=(LinearLayout)findViewById(R.id.market_linear);
        service_linear=(LinearLayout)findViewById(R.id.service_linear);
        add_function=(LinearLayout)findViewById(R.id.add_funtion);
        zhedang=(LinearLayout)findViewById(R.id.zhedang);
        add=(ImageView)findViewById(R.id.add);
        search=(ImageView)findViewById(R.id.search);
        edit_name=(EditText)findViewById(R.id.edit_name);
        title=(TextView)findViewById(R.id.title);
        notice=(TextView)findViewById(R.id.notice);
//        add_commodity=(TextView)findViewById(R.id.add_commodity);
//        apply_service=(TextView)findViewById(R.id.apply_service);


        LinearListener linearListener=new LinearListener();
        market_linear.setOnClickListener(linearListener);
        service_linear.setOnClickListener(linearListener);
        ImageListener imageListener=new ImageListener();
        add.setOnClickListener(imageListener);
        zhedangListener zhedanglistener=new zhedangListener();
        zhedang.setOnClickListener(zhedanglistener);
        searchListener searchlistener=new searchListener();
        search.setOnClickListener(searchlistener);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //HashMap<String,String> map=(HashMap<String, String>) parent.getItemAtPosition(position);
                Log.d("tag", "onItemClick: position"+position);
                Log.d("tag", "onItemClick: id"+id);
                int now_position=position-parent.getFirstVisiblePosition();
                TextView id_text=(TextView) parent.getChildAt(now_position).findViewById(R.id.goods_id);
                //TextView id_text=(TextView)layout.findViewById(R.id.goods_id);
                String goods_id=id_text.getText().toString();
                Log.d("tag", "onItemClick: id"+goods_id);
                Intent intent=new Intent();
                intent.putExtra("id",goods_id);
                intent.putExtra("user_id",user_id);
                intent.setClass(DealActivity.this,Goods_infoActivity.class);
                startActivity(intent);
            }
        });



        //sql="select id,img_url1,brand_model,counts,price from commodity_table order by heat desc";
        handler = new myHandler();
        get_goods_list();

    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        get_goods_list();
//    }

    public void get_goods_list(){
        JSONObject json=new JSONObject();
        try {
            json.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost=new HttpPost(json,"get_goods_list.jsp",handler);
        httpPost.doPost();
    }

    public void add_commodity(View v){
        Intent intent=new Intent();
        intent.putExtra("user_id",user_id);
        intent.setClass(DealActivity.this,AddcommodityActivity.class);
        startActivity(intent);
    }
    public void apply_service(View v){
        Intent intent=new Intent();
        intent.putExtra("user_id",user_id);
        intent.setClass(DealActivity.this,ApplyserviceActivity.class);
        startActivity(intent);
    }

    public class searchListener implements View.OnClickListener{
        public void onClick(View view){
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            name=edit_name.getText().toString();
            if(!name.equals("")) {
                get_goods_list();
            }
        }
    }

    public class ImageListener implements View.OnClickListener{
        public void onClick(View view){
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            if(add_function.getVisibility()==view.GONE) {
                zhedang.setVisibility(view.VISIBLE);
                zhedang.bringToFront();
                add_function.setVisibility(view.VISIBLE);
                add_function.bringToFront();

//                add.setBackgroundResource(R.drawable.image_bg_hover);
            }
            else {
                add_function.setVisibility(view.GONE);
//                add.setBackgroundResource(R.drawable.image_bg);
                zhedang.setVisibility(view.GONE);
            }
        }
    }

    public class zhedangListener implements View.OnClickListener{
        public void onClick(View view){
            add_function.setVisibility(view.GONE);
//            add.setBackgroundResource(R.drawable.image_bg);
            zhedang.setVisibility(view.GONE);
        }
    }


    public class LinearListener implements View.OnClickListener{
        public void onClick(View view){
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            Intent intent=new Intent();
            switch (view.getId()){
                case R.id.market_linear:
                    intent.setClass(DealActivity.this,MarketActivity.class);
                    break;
                case R.id.service_linear:
                    intent.setClass(DealActivity.this,ServiceActivity.class);
            }
            intent.putExtra("user_id",user_id);
            startActivity(intent);
        }
    }

    public class myHandler extends Handler{
        public void handleMessage(Message msg){
            if(msg.what==3) {
                list =new ArrayList<>();
                JSONArray json_list=(JSONArray) msg.obj;
                try {
                    for (int i = 0; i < json_list.length(); i++) {
                        HashMap<String, Object> commodity_info = new HashMap<>();
                        commodity_info.put("id", json_list.getJSONObject(i).getString("id"));
                        commodity_info.put("img", json_list.getJSONObject(i).getString("img_url1"));
                        commodity_info.put("name", "名称：" + json_list.getJSONObject(i).getString("brand_model"));
                        commodity_info.put("price", "￥" + json_list.getJSONObject(i).getString("price"));
                        commodity_info.put("counts", "数量：" + json_list.getJSONObject(i).getString("counts"));
                        list.add(commodity_info);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                //SimpleAdapter adapter =new SimpleAdapter(DealActivity.this,list,R.layout.list_layout,new String[]{"name","price"},new int[]{R.id.list_name,R.id.list_price});
                if (list.size() == 0) {
                    notice.setText("暂无商品");
                    notice.setVisibility(View.VISIBLE);
                }
                MyAdapter adapter = new MyAdapter(DealActivity.this, list);
                listview.setAdapter(adapter);
            }
            else {
                notice.setText("访问服务器失败");
                notice.setVisibility(View.VISIBLE);
            }
        }
    }
}
