package com.example.scso.school_social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by CYH on 2017/7/3.
 */

public class Goods_infoActivity extends Activity {
    private String user_id;
    private String goods_id;
    private String goods_user_id;
    private TextView goods_name;
    private TextView goods_price;
    private TextView goods_intro;
    private TextView goods_counts;
    private Handler handler;
    private ViewFlipper img_view;
    private int img_counts;
    //private ArrayList<String> img_url;
    private GetImage getImage;
    private GestureDetector gestureDetector;
    private Button connect_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_information);
        Intent intent=getIntent();
        goods_id=intent.getStringExtra("id");
        user_id=intent.getStringExtra("user_id");
        goods_name=(TextView)findViewById(R.id.goods_name);
        goods_price=(TextView)findViewById(R.id.goods_price);
        goods_intro=(TextView)findViewById(R.id.goods_intro);
        goods_counts=(TextView)findViewById(R.id.goods_counts);
        img_view=(ViewFlipper)findViewById(R.id.img_view);
        connect_button=(Button)findViewById(R.id.connect_button);
        //sql="select * from commodity_table where id='"+goods_id+"'";

        imgGesture imggesture=new imgGesture();
        gestureDetector=new GestureDetector(this,imggesture);
        //img_view.setInAnimation(this,android.R.anim.fade_in);
        //img_view.setOutAnimation(this,android.R.anim.fade_out);
        connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("connect_user_id",goods_user_id);
                intent.putExtra("user_id",user_id);
                intent.setClass(Goods_infoActivity.this,LiaotianActivity.class);
                startActivity(intent);
            }
        });

        handler=new Handler(){
            public void handleMessage(Message msg){
                JSONObject goods_data=(JSONObject) msg.obj;
                try {
                    img_counts=goods_data.getInt("img_counts");
                    for(int i=0;i<img_counts;i++){
                        String imgurl="img_url"+(i+1);
                        String url="http://118.89.199.187/school_social/img/commodity/"+goods_data.getString(imgurl);
                        //img_url.add(url);
                        getImage=new GetImage();
                        ImageView img=new ImageView(Goods_infoActivity.this);
                        img.setImageResource(R.drawable.loading);
                        img.setTag(url);
                        getImage.set_img(url,img);
                        img_view.addView(img);
                    }
                    goods_user_id=goods_data.getString("user_id");
                    goods_name.setText("名称："+goods_data.getString("goods_name"));
                    goods_price.setText("价格："+goods_data.getString("goods_price")+"元");
                    goods_counts.setText("数量："+goods_data.getString("goods_counts"));
                    goods_intro.setText("介绍："+goods_data.getString("goods_intro"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        getdata();
    }

    public boolean onTouchEvent(MotionEvent e){
        gestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    public void getdata() {
        Thread thread = new Thread() {
            public void run() {
                String myurl = "http://118.89.199.187/school_social/jsp/get_goods.jsp?id="+goods_id;
                try {
                    URL url = new URL(myurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10 * 1000);
                    //OutputStream outputStream=conn.getOutputStream();
                    //outputStream.write(URLEncoder.encode(object.toString(),"UTF-8").getBytes());
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        try {
                            String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                            final JSONObject jsonObject = new JSONObject(jsonstr);
                            JSONArray data = jsonObject.getJSONArray("Data");
                            JSONObject json_data=data.getJSONObject(0);
                            Log.d("tag", "run: "+json_data);
                            Message msg = Message.obtain(handler, 3);
                            msg.obj = json_data;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    public void back(View v){
        finish();
    }

    public class imgGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1,MotionEvent e2,float x,float y){
            if(e1.getX()>e2.getX()){
                img_view.showNext();
            }
            if(e1.getX()<e2.getX()){
                img_view.showPrevious();
            }
            return super.onFling(e1,e2,x,y);
        }
    }
}
