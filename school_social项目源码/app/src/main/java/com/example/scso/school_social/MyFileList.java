package com.example.scso.school_social;

/**
 * Created by Fiveaddone on 2017/7/8.
 */
public class MyFileList
{
    private String name;
    private int imageId;
    private String size;
    private String date;
    private String id;
    private String cherk;
    public MyFileList(String name,int imageId,String date,String size,String id,String cherk){
        this.name=name;
        this.imageId=imageId;
        this.date=date;
        this.size=size;
        this.id=id;
        this.cherk=cherk;
    }
    public String getName(){
        return name;
    }
    public String getSize(){
        return size;
    }
    public String getDate(){
        return date;
    }
    public String getId(){return id;}
    public String getCherk(){
    return cherk;
}


    public int getImageId(){
        return imageId;
    }
}
