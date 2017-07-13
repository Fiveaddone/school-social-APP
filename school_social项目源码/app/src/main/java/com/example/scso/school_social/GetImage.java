package com.example.scso.school_social;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by CYH on 2017/6/27.
 */

public class GetImage {
    ExecutorService executorservice;
    ArrayList<String> hasgoturl=new ArrayList<>();
    private LruCache<String,Bitmap> memCache;
    public GetImage(){
        super();
        memCache=new LruCache<>(20*1024*1024);
        executorservice=Executors.newFixedThreadPool(5);
    }
    public void set_img(final String url, final ImageView imageview){

        Handler handler=new Handler(){
            public void handleMessage(Message msg){
                Bitmap bitmap=(Bitmap) msg.obj;
                if(imageview.getTag()!=null&&imageview.getTag().equals(url)) {
                    if (bitmap == null) {
                        imageview.setImageResource(R.drawable.loading);
                    } else {
                        imageview.setImageBitmap(bitmap);
                        //imageview.setTag("");
                        hasgoturl.add(url);
                        memCache.put(url,bitmap);

                    }
                }
            }
        };
        if(memCache.get(url)==null){
            imgLoad imgload=new imgLoad(url,imageview);
            executorservice.submit(new GetImageThread(imgload,handler));
        }
        else{
            if(imageview.getTag()!=null&&imageview.getTag().equals(url)) {
                imageview.setImageBitmap(memCache.get(url));
            }
        }

    }

    public class imgLoad{
        public String url;
        public ImageView imageView;
        public imgLoad(String url,ImageView imageView){
            this.url=url;
            this.imageView=imageView;
        }
    }




    public class GetImageThread extends Thread{
        private imgLoad imgload;
        private Handler handler;
        private Bitmap bitmap;
        public GetImageThread(imgLoad imgload,Handler handler){
            super();
            this.imgload=imgload;
            this.handler=handler;
        }
        @Override
        public void run() {
            //String url = "http://118.89.199.187/school_social/img/commodity/" + url;
            if(hasgoturl.contains(imgload.url)){
                return;
            }
            URL img = null;
            try {
                img = new URL(imgload.url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) img.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        Message msg = Message.obtain(handler, 3);
        msg.obj = bitmap;
        handler.sendMessage(msg);
        }
    }
}
