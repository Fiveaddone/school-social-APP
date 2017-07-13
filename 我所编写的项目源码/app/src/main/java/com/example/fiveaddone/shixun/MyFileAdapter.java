package com.example.fiveaddone.shixun;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fiveaddone on 2017/7/6.
 */
public class MyFileAdapter extends ArrayAdapter<MyFileList> {
    private int resourceId;
    private boolean isTrue;
    private Button button;
    private Map<Integer,Boolean> map=new HashMap<>();
    private ArrayList<String> id=new ArrayList<>();
    public MyFileAdapter(Context context, int textViewResourceId, List<MyFileList> objects,boolean isTrue,Button button){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        this.isTrue = isTrue;
        this.button=button;
        this.id=id;
    }


    public ArrayList<String> getId() {
        return id;
    }

    public void setId(ArrayList<String> id) {
        this.id = id;
        notifyDataSetChanged();
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        final MyFileList fileList=getItem(position);
        View view;
        ViewHolder viewHolder ;
        if(convertView==null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.fileImage= (ImageView) view.findViewById(R.id.image_show);
            viewHolder.filename= (TextView) view.findViewById(R.id.name_show);
            viewHolder.date= (TextView) view.findViewById(R.id.date_show);
            viewHolder.size= (TextView) view.findViewById(R.id.size_show);
            viewHolder.check_state= (TextView) view.findViewById(R.id.check_state);
            view.setTag(viewHolder);
        }
        else {
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }

        /**/
        viewHolder.checkBox=(CheckBox)view.findViewById(R.id.check_box);
        viewHolder.fileImage.setImageResource(fileList.getImageId());
        viewHolder.filename.setText(fileList.getName());
        viewHolder.date.setText(fileList.getDate());
        viewHolder.size.setText(fileList.getSize());
        if (fileList.getCherk().equals("0")) {
            viewHolder.check_state.setText("未审核");
            viewHolder. check_state.setTextColor(0xffff6600);
        }
        else if (fileList.getCherk().equals("1")) {
            viewHolder.check_state.setText("审核通过");
            viewHolder.check_state.setTextColor(0xff2a8bde);
        }
        if (isTrue){
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   if(map.get(position)==null)id.add(fileList.getId());
                   map.put(position,true);
                   button.setText("删除(已选择"+map.size()+")");
                   Log.e("a", String.valueOf(position));

               }
               else {
                   map.remove(position);
                   button.setText("删除(已选择"+map.size()+")");
                   id.remove(fileList.getId());
               }
           }
       });
        if(map!=null&&map.containsKey(position)){
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);
            id.remove(fileList.getId());
            //id.remove(fileList.getId());
        }

        return view;
    }
    class ViewHolder{
        ImageView fileImage;
        TextView filename;
        TextView date;
        TextView size;
        TextView check_state;
        CheckBox checkBox;

    }




}