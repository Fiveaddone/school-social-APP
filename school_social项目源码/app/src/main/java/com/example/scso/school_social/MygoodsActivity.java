package com.example.scso.school_social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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

/**
 * Created by CYH on 2017/7/9.
 */

public class MygoodsActivity extends Activity {
    private String user_id;
    private Handler handler;
    private ListView listview;
    private ArrayList<HashMap<String,Object>> list;
    private TextView goods_list_notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user_id=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygoods);



        listview=(ListView)findViewById(R.id.goods_list);
        goods_list_notice=(TextView)findViewById(R.id.my_goods_notice);

        handler = new myHandler();
        get_goods_list();

    }
    public void back(View v){
        finish();
    }


    public void get_goods_list(){
        JSONObject json=new JSONObject();
        try {
            json.put("id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost=new HttpPost(json,"get_my_goods.jsp",handler);
        httpPost.doPost();
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
                if (list.size() > 0) {

                    goods_list_notice.setVisibility(View.GONE);
                }
                MyAdapter adapter = new MyAdapter(MygoodsActivity.this, list);
                listview.setAdapter(adapter);
            }
            else {
                goods_list_notice.setText("访问服务器失败");
                goods_list_notice.setVisibility(View.VISIBLE);
            }
        }
    }
}
