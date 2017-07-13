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

public class MyAdapter3 extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String,Object>> list;
    private static LayoutInflater inflater=null;

    public MyAdapter3(Activity a, ArrayList<HashMap<String,Object>> list){
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
            convertView=inflater.inflate(R.layout.servercheck_item,null);
        }
        TextView server_id = (TextView) convertView.findViewById(R.id.server_id);
        TextView servername = (TextView) convertView.findViewById(R.id.servername);
        TextView state = (TextView) convertView.findViewById(R.id.state);
        TextView intro = (TextView) convertView.findViewById(R.id.intro);
        HashMap<String,Object> info=new HashMap<>();
        info=list.get(position);
//        Log.d("tag", "getView: "+position);
//        Log.d("tag", "getView: ------>"+convertView.getId());

        server_id.setText((String)info.get("server_id"));
        servername.setText((String)info.get("servername"));
        state.setText((String)info.get("state"));
        intro.setText((String)info.get("intro"));
        return convertView;
    }
}
