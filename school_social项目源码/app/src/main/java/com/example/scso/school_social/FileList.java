package com.example.scso.school_social;

/**
 * Created by Fiveaddone on 2017/7/6.
 */
public class FileList
{
    private String name;
    private int imageId;
    private String size;
    private String date;
    private String id;
    private String likes;
    private String nickname;
    public FileList(String name,int imageId,String date,String size,String id,String likes,String nickname){
        this.name=name;
        this.imageId=imageId;
        this.date=date;
        this.size=size;
        this.id=id;
        this.likes=likes;
        this.nickname=nickname;
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
    public String getNickname(){return nickname;}

    public int getImageId(){
        return imageId;
    }
    public String getLikes(){return likes;}
}
