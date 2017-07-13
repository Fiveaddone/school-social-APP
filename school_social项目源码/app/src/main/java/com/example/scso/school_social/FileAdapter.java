package com.example.scso.school_social;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fiveaddone on 2017/7/6.
 */
public class FileAdapter extends ArrayAdapter<FileList> {
    private int resourceId;
    public FileAdapter(Context context, int textViewResourceId, List<FileList> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        FileList fileList=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView fileImage= (ImageView) view.findViewById(R.id.image_show);
        TextView filename= (TextView) view.findViewById(R.id.name_show);
        TextView date= (TextView) view.findViewById(R.id.date_show);
        TextView size= (TextView) view.findViewById(R.id.size_show);
        TextView likes= (TextView) view.findViewById(R.id.likes);
        TextView nickname= (TextView) view.findViewById(R.id.owner_show);
        likes.setText(fileList.getLikes()+" 个点赞");
        nickname.setText("by "+fileList.getNickname());
        nickname.setTextColor(0xff2a8bde);
        fileImage.setImageResource(fileList.getImageId());
        filename.setText(fileList.getName());
        date.setText(fileList.getDate());
        size.setText(fileList.getSize());
        return view;
    }

}