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

public class MyAdapter2 extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String,Object>> list;
    private static LayoutInflater inflater=null;

    public MyAdapter2(Activity a, ArrayList<HashMap<String,Object>> list){
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
            convertView=inflater.inflate(R.layout.filecheck_item,null);
        }
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView filename = (TextView) convertView.findViewById(R.id.filename);
        TextView check = (TextView) convertView.findViewById(R.id.check);
        TextView file_describe = (TextView) convertView.findViewById(R.id.file_describe);
        HashMap<String,Object> info=new HashMap<>();
        info=list.get(position);
//        Log.d("tag", "getView: "+position);
//        Log.d("tag", "getView: ------>"+convertView.getId());

        id.setText((String)info.get("id"));
        filename.setText((String)info.get("filename"));
        check.setText((String)info.get("check"));
        file_describe.setText((String)info.get("file_describe"));
        return convertView;
    }
}