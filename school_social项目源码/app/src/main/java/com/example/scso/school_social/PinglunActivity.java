package com.example.scso.school_social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CYH on 2017/7/5.
 */

public class PinglunActivity extends Activity {
    private String type;
    private String id;
    private String user_id;
    private ListView pinglun_list;
    private Handler handler;
    private Handler handler1;
    private ArrayList<HashMap<String,String>> pinglun_data_list;
    private String target_user_id;
    private String target_user_name;
    private EditText pinglun_edit;
    private Button pinglun_button;
    private Button huifu_button;
    private JSONObject json;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        id=intent.getStringExtra("id");
        user_id=intent.getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinglun);
        //Toast.makeText(PinglunActivity.this,type+id, Toast.LENGTH_SHORT).show();
        pinglun_list =(ListView) findViewById(R.id.pinglun_list);
        pinglun_edit =(EditText) findViewById(R.id.pinglun_edit);
        pinglun_button=(Button) findViewById(R.id.pinglun_button);
        huifu_button=(Button) findViewById(R.id.huifu_button);

        handler1=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    Toast.makeText(PinglunActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                    pinglun_edit.setText("");
                    get_data();
                }
                else{
                    Toast.makeText(PinglunActivity.this,"评论失败，请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        };

        pinglun_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pinglun_edit.getText())) {
                    Toast.makeText(PinglunActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    json = new JSONObject();
                    try {
                        json.put("type", type);
                        json.put("id", id);
                        json.put("user_id", user_id);
                        json.put("content", pinglun_edit.getText().toString());
                        json.put("is_huifu", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpPost httpPost = new HttpPost(json, "pinglun.jsp", handler1);
                    httpPost.doPost();
                }
            }

        });

        huifu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PinglunActivity.this,"huifu",Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(pinglun_edit.getText())) {
                    Toast.makeText(PinglunActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    json = new JSONObject();
                    try {
                        json.put("type", type);
                        json.put("id", id);
                        json.put("user_id", user_id);
                        json.put("is_huifu", 1);
                        json.put("target_user_id", target_user_id);
                        json.put("content", "回复 @" + target_user_name + " :" + pinglun_edit.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpPost httpPost = new HttpPost(json, "pinglun.jsp", handler1);
                    httpPost.doPost();
                }
            }
        });

        pinglun_list.setOnItemClickListener(new ListListener());


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    pinglun_data_list=new ArrayList<>();
                    JSONArray pinglunJSON=(JSONArray) msg.obj;
                    try {
                        for (int i = 0; i < pinglunJSON.length(); i++) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("user_name", pinglunJSON.getJSONObject(i).getString("user_name"));
                            data.put("user_id",pinglunJSON.getJSONObject(i).getString("user_id"));
                            data.put("date_time",pinglunJSON.getJSONObject(i).getString("date_time"));
                            data.put("content",pinglunJSON.getJSONObject(i).getString("content"));
                            pinglun_data_list.add(data);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    SimpleAdapter adapter=new SimpleAdapter(PinglunActivity.this,pinglun_data_list,R.layout.pinglun_list_layout,
                            new String[] {"user_name","user_id","date_time","content"},new int[] {R.id.user_name,R.id.user_id,
                                            R.id.date_time,R.id.content});
                    pinglun_list.setAdapter(adapter);
                }
                else{
                    Toast.makeText(PinglunActivity.this,"获取评论失败",Toast.LENGTH_SHORT);
                }
            }
        };

        get_data();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && huifu_button.getVisibility()==View.VISIBLE){
            huifu_button.setVisibility(View.GONE);
            pinglun_button.setVisibility(View.VISIBLE);
        }
        else{
            finish();
        }
        return false;
    }

    public class ListListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int now_position=position-parent.getFirstVisiblePosition();
            View v=(View)parent.getChildAt(now_position);
            TextView user_id_text=(TextView) v.findViewById(R.id.user_id);
            TextView user_name_text=(TextView) v.findViewById(R.id.user_name);

            //TextView id_text=(TextView)layout.findViewById(R.id.goods_id);
            target_user_id=user_id_text.getText().toString();
            target_user_name=user_name_text.getText().toString();
            pinglun_edit.requestFocus();
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0,InputMethodManager.SHOW_IMPLICIT);
            pinglun_button.setVisibility(View.GONE);
            huifu_button.setVisibility(View.VISIBLE);
        }
    }

    public void get_data(){
        Log.d("tag", "get_data: ");
        JSONObject json=new JSONObject();
        try {
            json.put("type",type);
            json.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPost=new HttpPost(json,"get_pinglun.jsp",handler);
        httpPost.doPost();
    }

    public void back(View v){
        finish();
    }
}
