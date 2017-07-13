package com.example.scso.school_social;

/**
 * Created by Administrator on 2017/7/8.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/5.
 */

public class MyAdapter1 extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String,Object>> list;
    private static LayoutInflater inflater=null;

    public MyAdapter1(Activity a, ArrayList<HashMap<String,Object>> list){
        this.activity=a;
        this.list=list;
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
            convertView=inflater.inflate(R.layout.powercheck_item,null);
        }
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView power = (TextView) convertView.findViewById(R.id.power);
        HashMap<String,Object> info=new HashMap<>();
        info=list.get(position);
//        Log.d("tag", "getView: "+position);
//        Log.d("tag", "getView: ------>"+convertView.getId());

        name.setText((String)info.get("name"));
        phone.setText((String)info.get("phone"));
        power.setText((String)info.get("power"));
        return convertView;
    }
}
