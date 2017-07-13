package com.example.scso.school_social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CYH on 2017/7/5.
 */

public class ServiceListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String,String>> list;
    private static LayoutInflater inflater=null;
    private String dianzan_user_id;
    private boolean flag=false;
    public ServiceListAdapter(Activity a, ArrayList<HashMap<String,String>> list,String user_id,boolean flag){
        this.flag=flag;
        this.activity=a;
        this.list=list;
        this.dianzan_user_id=user_id;
        Log.d("tag", "ServiceListAdapter: "+dianzan_user_id);
        inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.service_list,null);
        }
        final TextView service_id = (TextView) convertView.findViewById(R.id.service_id);
        TextView service_name = (TextView) convertView.findViewById(R.id.service_name);
        TextView service_intro = (TextView) convertView.findViewById(R.id.service_intro);
        TextView service_time = (TextView) convertView.findViewById(R.id.service_time);
        final TextView dianzan_counts = (TextView) convertView.findViewById(R.id.dianzan_counts);
        final TextView pinglun_counts=(TextView)convertView.findViewById(R.id.pinglun_counts);
        final ImageView dianzan=(ImageView)convertView.findViewById(R.id.dianzan);
        ImageView pinglun=(ImageView)convertView.findViewById(R.id.pinglun);
        final Button connect_button=(Button)convertView.findViewById(R.id.connect_button);
        if(flag){
            connect_button.setVisibility(View.GONE);
        }
        final HashMap<String,String> info=list.get(position);
        //info=list.get(position);
        service_id.setText("服务编号："+info.get("id"));
        service_name.setText("服务名称："+info.get("name"));
        service_intro.setText("服务介绍："+info.get("intro"));
        service_time.setText("服务时段："+info.get("time"));
        dianzan_counts.setText(info.get("dianzan_counts"));
        pinglun_counts.setText(info.get("pinglun_counts"));
        Log.d("tag", "getView: "+info.get("user_id"));
        connect_button.setTag(info.get("user_id"));
        if(info.get("is_dianzan").equals("1")){
            dianzan.setImageResource(R.drawable.dianzan1);
            dianzan.setTag("1");
        }
        else{
            dianzan.setImageResource(R.drawable.dianzan);
            dianzan.setTag("0");
        }
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json=new JSONObject();
                if(dianzan.getTag().equals("1")){
                    dianzan.setImageResource(R.drawable.dianzan);
                    dianzan.setTag("0");
                    int number=Integer.parseInt(dianzan_counts.getText().toString());
                    number--;
                    dianzan_counts.setText(number+"");
                    try {
                        json.put("do", "quxiao");
                        json.put("user_id",dianzan_user_id);
                        json.put("service_id",info.get("id"));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
                else{
                    dianzan.setImageResource(R.drawable.dianzan1);
                    dianzan.setTag("1");
                    int number=Integer.parseInt(dianzan_counts.getText().toString());
                    number++;
                    dianzan_counts.setText(number+"");
                    try {
                        json.put("do", "dianzan");
                        json.put("user_id",dianzan_user_id);
                        json.put("service_id",info.get("id"));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                HttpPost httpPost=new HttpPost(json,"dianzan.jsp",new Handler());
                httpPost.doPost();
            }
        });
        pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(activity,PinglunActivity.class);
                intent.putExtra("type","service");
                intent.putExtra("id",info.get("id"));
                intent.putExtra("user_id",dianzan_user_id);
                activity.startActivity(intent);
            }
        });
        connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(activity,LiaotianActivity.class);
                intent.putExtra("connect_user_id",(String)v.getTag());
                intent.putExtra("user_id",dianzan_user_id);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}
