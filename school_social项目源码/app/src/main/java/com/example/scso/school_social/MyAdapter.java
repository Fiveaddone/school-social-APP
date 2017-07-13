package com.example.scso.school_social;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CYH on 2017/6/27.
 */

public class MyAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String,Object>> list;
    private static LayoutInflater inflater=null;
    public GetImage getImage;
    public MyAdapter(Activity a, ArrayList<HashMap<String,Object>> list){
        this.activity=a;
        this.list=list;
        inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getImage=new GetImage();
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
        Holder holder=null;
        HashMap<String,Object> info=new HashMap<>();
        info=list.get(position);
        String url="http://118.89.199.187/school_social/img/commodity/"+info.get("img");
        if(convertView==null){
            convertView=inflater.inflate(R.layout.list_layout,null);
            holder=new Holder();
            convertView.setTag(holder);
            //System.out.println("new"+position);
            holder.id = (TextView) convertView.findViewById(R.id.goods_id);
            holder.name= (TextView) convertView.findViewById(R.id.list_name);
            holder.price = (TextView) convertView.findViewById(R.id.list_price);
            holder.counts = (TextView) convertView.findViewById(R.id.counts);
            holder.img = (ImageView) convertView.findViewById(R.id.list_img);

        }
        else{
            holder=(Holder) convertView.getTag();
            //System.out.println("old"+position);
        }

//        Log.d("tag", "getView: "+position);
//        Log.d("tag", "getView: ------>"+convertView.getId());
        holder.img.setTag(url);
        holder.img.setImageResource(R.drawable.loading);
        getImage.set_img(url,holder.img);
        holder.id.setText((String)info.get("id"));
        holder.name.setText((String)info.get("name"));
        holder.price.setText((String)info.get("price"));
        holder.counts.setText((String)info.get("counts"));
        return convertView;
    }
    public class Holder{
        private ImageView img;
        private TextView id;
        private TextView name;
        private TextView price;
        private TextView counts;
    }
}
