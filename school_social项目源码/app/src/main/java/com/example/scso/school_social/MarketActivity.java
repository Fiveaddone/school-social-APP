package com.example.scso.school_social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.Objects;

/**
 * Created by CYH on 2017/6/28.
 */


public class MarketActivity extends Activity {
    private String user_id;
    private Button all;
    private Button book;
    private Button shuma;
    private Button fuzhuang;
    private Button jiaocai;
    private Button wenxue;
    private Button shouji;
    private Button yifu;
    private Button zhuanye;
    private Button xiangji;
    private Button gongju;
    private Button xiezi;
    private Button other;
    private Button math;
    private Button computer;
    private Button kuzi;
    private String kind;
    private ImageView search;
    private EditText edit_name;
    private LinearLayout kind_show;
    private ListView listview;
    private TextView notice;
    private ProgressDialog pg;
    @Override
    protected void onCreate(Bundle onSaveInstanceState){
        user_id=getIntent().getStringExtra("user_id");
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_market);
        all=(Button)findViewById(R.id.all);
        book=(Button)findViewById(R.id.book);
        shuma=(Button)findViewById(R.id.shuma);
        fuzhuang=(Button)findViewById(R.id.fuzhuang);
        jiaocai=(Button)findViewById(R.id.jiaocai);
        wenxue=(Button)findViewById(R.id.wenxue);
        shouji=(Button)findViewById(R.id.shouji);
        yifu=(Button)findViewById(R.id.yifu);
        zhuanye=(Button)findViewById(R.id.zhuanye);
        xiangji=(Button)findViewById(R.id.xiangji);
        gongju=(Button)findViewById(R.id.gongju);
        xiezi=(Button)findViewById(R.id.xiezi);
        other=(Button)findViewById(R.id.other);
        math=(Button)findViewById(R.id.math);
        computer=(Button)findViewById(R.id.computer);
        kuzi=(Button)findViewById(R.id.kuzi);
        search=(ImageView)findViewById(R.id.search);
        edit_name=(EditText)findViewById(R.id.edit_name);
        kind_show=(LinearLayout)findViewById(R.id.kind_show);
        listview=(ListView)findViewById(R.id.listview);
        notice=(TextView)findViewById(R.id.notice);

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
                intent.setClass(MarketActivity.this,Goods_infoActivity.class);
                startActivity(intent);
            }
        });


        ButtonClickListener buttonClickListener=new ButtonClickListener();
        all.setOnClickListener(buttonClickListener);
        book.setOnClickListener(buttonClickListener);
        shuma.setOnClickListener(buttonClickListener);
        fuzhuang.setOnClickListener(buttonClickListener);
        jiaocai.setOnClickListener(buttonClickListener);
        wenxue.setOnClickListener(buttonClickListener);
        shouji.setOnClickListener(buttonClickListener);
        yifu.setOnClickListener(buttonClickListener);
        zhuanye.setOnClickListener(buttonClickListener);
        xiangji.setOnClickListener(buttonClickListener);
        gongju.setOnClickListener(buttonClickListener);
        xiezi.setOnClickListener(buttonClickListener);
        other.setOnClickListener(buttonClickListener);
        math.setOnClickListener(buttonClickListener);
        computer.setOnClickListener(buttonClickListener);
        kuzi.setOnClickListener(buttonClickListener);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                if(TextUtils.isEmpty(edit_name.getText())){
                    kind="";
                }
                else {
                    kind = edit_name.getText().toString();
                }
                query();
            }
        });


    }

    public void query(){
        pg=ProgressDialog.show(MarketActivity.this,"正在加载","加载中...");
        kind_show.setVisibility(View.GONE);
        edit_name.setText(kind);
//        String sql;
//        if(kind.equals("")){
//            sql="select id,img_url1,brand_model,counts,price from commodity_table order by heat desc";
//        }
//        else{
//            sql="select id,img_url1,brand_model,counts,price from commodity_table where name like '%"
//                    + kind + "%' order by heat desc";
//        }
        JSONObject json=new JSONObject();
        try {
            json.put("name",kind);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Handler handler=new Handler(){
            public void handleMessage(Message msg){
                pg.dismiss();
                if(msg.what==3) {
                    ArrayList<HashMap<String,Object>> list =new ArrayList<>();
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
                    if (list.size() == 0) {
                        notice.setText("暂无商品");
                        notice.setVisibility(View.VISIBLE);
                    }
                    MyAdapter adapter = new MyAdapter(MarketActivity.this, list);
                    listview.setAdapter(adapter);
                }
                else {
                    notice.setText("访问服务器失败");
                    notice.setVisibility(View.VISIBLE);
                }
            }
        };
        HttpPost httpPost=new HttpPost(json,"get_goods_list.jsp",handler);
        httpPost.doPost();
    }
    public void back(View v){
        finish();
    }


    public class ButtonClickListener implements View.OnClickListener{
        public void onClick(View view){
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            switch (view.getId()){
                case R.id.all:
                    kind="";
                    break;
                case R.id.book:
                    kind="书籍";
                    break;
                case R.id.shuma:
                    kind="数码产品";
                    break;
                case R.id.fuzhuang:
                    kind="服装";
                    break;
                case R.id.jiaocai:
                    kind="教材";
                    break;
                case R.id.wenxue:
                    kind="文学";
                    break;
                case R.id.gongju:
                    kind="工具书";
                    break;
                case R.id.shouji:
                    kind="手机";
                    break;
                case R.id.xiangji:
                    kind="相机";
                    break;
                case R.id.computer:
                    kind="电脑";
                    break;
                case R.id.yifu:
                    kind="衣服";
                    break;
                case R.id.kuzi:
                    kind="裤子";
                    break;
                case R.id.xiezi:
                    kind="鞋子";
                    break;
                case R.id.other:
                    kind="其它";
                    break;
                case R.id.math:
                    kind="数学";
                    break;
                case R.id.zhuanye:
                    kind="专业书籍";
                    break;
            }
            query();
        }
    }
}
