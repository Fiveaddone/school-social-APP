package com.example.scso.school_social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CYH on 2017/6/28.
 */

public class AddcommodityActivity extends Activity {
    private String username;
    private Spinner first_kind;
    private Spinner second_kind;
    private Spinner third_kind;
    private List<String> second_kind_data;
    private List<String> third_kind_data;
    private GridView img_grid;
    private ArrayList<HashMap<String,Object>> img_list;
    private ArrayList<String> img_url;
    private SimpleAdapter simpleAdapter;
    private Button submit;
    private ProgressDialog progress;
    private TextView first_kind_notice;
    private TextView second_kind_notice;
    private TextView third_kind_notice;
    private TextView brand_model_notice;
    private TextView price_notice;
    private TextView img_notice;
    private TextView counts_notice;
    private TextView intro_notice;
    private EditText brand_model_edit;
    private EditText price_edit;
    private EditText counts_edit;
    private EditText intro_edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        username=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcommodity);
        first_kind=(Spinner)findViewById(R.id.first_kind);
        second_kind=(Spinner)findViewById(R.id.second_kind);
        third_kind=(Spinner)findViewById(R.id.third_kind);
        img_grid=(GridView) findViewById(R.id.img_grid);
        submit=(Button)findViewById(R.id.submit);
        first_kind_notice=(TextView)findViewById(R.id.first_kind_notice);
        second_kind_notice=(TextView)findViewById(R.id.second_kind_notice);
        third_kind_notice=(TextView)findViewById(R.id.third_kind_notice);
        brand_model_notice=(TextView)findViewById(R.id.brand_model_notice);
        price_notice=(TextView)findViewById(R.id.price_notice);
        img_notice=(TextView)findViewById(R.id.img_notice);
        brand_model_edit=(EditText) findViewById(R.id.brand_model_edit);
        price_edit=(EditText) findViewById(R.id.price_edit);
        counts_edit=(EditText) findViewById(R.id.counts_edit);
        intro_edit=(EditText)findViewById(R.id.intro_edit);
        intro_notice=(TextView)findViewById(R.id.intro_notice);
        counts_notice=(TextView)findViewById(R.id.counts_notice);


        img_list=new ArrayList<>();
        img_url=new ArrayList<>();
        mySpinnerListener myspinnerListener=new mySpinnerListener();
        first_kind.setOnItemSelectedListener(myspinnerListener);
        second_kind.setOnItemSelectedListener(myspinnerListener);
        img_grid.setOnItemClickListener(new gridViewListener());
        final Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.add);
        HashMap<String,Object> img=new HashMap<>();
        img.put("img",bitmap);
        img_list.add(img);
        simpleAdapter=new SimpleAdapter(this,img_list,R.layout.grid_layout,new String[]{"img"},new int[]{R.id.grid_img});
//        onSpinnerClickListener onspinnerClickListener=new onSpinnerClickListener();
//        first_kind.setOnClickListener(onspinnerClickListener);
//        second_kind.setOnClickListener(onspinnerClickListener);
//        third_kind.setOnClickListener(onspinnerClickListener);
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i=(ImageView)view;
                    i.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        img_grid.setAdapter(simpleAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first_kind.getSelectedItemPosition()==0){
                    first_kind_notice.setText("请选择第一大类");
                    first_kind_notice.setVisibility(View.VISIBLE);
                }
                else if(second_kind.getSelectedItemPosition()==0){
                    second_kind_notice.setText("请选择第二大类");
                    second_kind_notice.setVisibility(View.VISIBLE);
                }
                else if(third_kind.getSelectedItemPosition()==0){
                    third_kind_notice.setText("请选择第三大类");
                    third_kind_notice.setVisibility(View.VISIBLE);
                }
                else if(TextUtils.isEmpty(brand_model_edit.getText())){
                    brand_model_notice.setText("请输入商品品牌及型号");
                    brand_model_notice.setVisibility(View.VISIBLE);
                }
                else if(TextUtils.isEmpty(counts_edit.getText())){
                    counts_notice.setText("请输入商品数量");
                    counts_notice.setVisibility(View.VISIBLE);
                }
                else if(TextUtils.isEmpty(price_edit.getText())){
                    price_notice.setText("请输入商品价格");
                    price_notice.setVisibility(View.VISIBLE);
                }
                else if(img_url.size()==0){
                    img_notice.setText("至少需要一张图片");
                    img_notice.setVisibility(View.VISIBLE);
                }
                else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date(System.currentTimeMillis());
                    final String date_time = format.format(date);
                    final ArrayList<String> filename = new ArrayList<String>();
                    ArrayList<InputStream> input = new ArrayList<InputStream>();
                    for (int i = 0; i < img_url.size(); i++) {
                        if (img_url.get(i).endsWith(".jpg")) {
                            filename.add((i + 1) + ".jpg");
                        } else if (img_url.get(i).endsWith(".png")) {
                            filename.add((i + 1) + ".png");
                        }
                        InputStream img = null;
                        try {
                        int permission= ActivityCompat.checkSelfPermission(AddcommodityActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if(permission != PackageManager.PERMISSION_GRANTED){
                            Log.d("tag", "onClick: permission_denied");

                            ActivityCompat.requestPermissions(AddcommodityActivity.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        }
                            //File file=new File(img_url.get(i));
                            img = new FileInputStream(img_url.get(i));
                            input.add(img);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    final Handler handler = new Handler() {
                        public void handleMessage(Message msg) {

                            if ((boolean) msg.obj == true) {
                                String first_kind_name=first_kind.getSelectedItem().toString();
                                Log.d("tag", "handleMessage: "+first_kind_name);
                                String second_kind_name=second_kind.getSelectedItem().toString();
                                String third_kind_name=third_kind.getSelectedItem().toString();
                                String brand_model=brand_model_edit.getText().toString();
                                String counts=counts_edit.getText().toString();
                                String intro=intro_edit.getText().toString();
                                final String price=price_edit.getText().toString();
                                if(second_kind_name.equals("无")){
                                    second_kind_name="";
                                }
                                if(third_kind_name.equals("无")){
                                    third_kind_name="";
                                }
                                String name=first_kind_name+","+second_kind_name+","+third_kind_name+","+brand_model;
                                String img_url1=null,img_url2=null,img_url3=null,img_url4=null,img_url5=null;
                                for(int i=0;i<filename.size();i++) {
                                    switch(i){
                                        case 0:
                                            img_url1 = username + date_time + "/" + filename.get(0);
                                            break;
                                        case 1:
                                            img_url2 = username + date_time + "/" + filename.get(1);
                                            break;
                                        case 2:
                                            img_url3 = username + date_time + "/" + filename.get(2);
                                            break;
                                        case 3:
                                            img_url4 = username + date_time + "/" + filename.get(3);
                                            break;
                                        case 4:
                                            img_url5 = username + date_time + "/" + filename.get(4);
                                            break;
                                    }

                                }
                                JSONObject json=new JSONObject();
                                try {
                                    json.put("first_kind_name",first_kind_name);
                                    json.put("second_kind_name",second_kind_name);
                                    json.put("third_kind_name",third_kind_name);
                                    json.put("brand_model",brand_model);
                                    json.put("name",name);
                                    json.put("counts",counts);
                                    json.put("price",price);
                                    json.put("username",username);
                                    json.put("intro",intro);
                                    json.put("filename_size",filename.size());
                                    json.put("img_url1",img_url1);
                                    json.put("img_url2",img_url2);
                                    json.put("img_url3",img_url3);
                                    json.put("img_url4",img_url4);
                                    json.put("img_url5",img_url5);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                String sql="insert into commodity_table (first_kind_name,second_kind_name,third_kind_name,brand_model,name,counts," +
//                                        "price,user_id,intro,heat,img_counts,img_url1,img_url2,img_url3,img_url4,img_url5) values('"+first_kind_name+"','"+second_kind_name
//                                        +"','"+third_kind_name+"','"+brand_model+"','"+name+"','"+counts+"','"+price+"','"+username+"','"+intro
//                                        +"',0,'"+filename.size()+"','"+img_url1+"','"+img_url2+"','"+img_url3+"','"+img_url4+"','"+img_url5+"')";
                                Handler handler1=new Handler(){
                                    @Override
                                    public void handleMessage(Message msg) {
                                        progress.dismiss();
                                        if(msg.what==3) {
                                            new AlertDialog.Builder(AddcommodityActivity.this).setTitle("上传结果")
                                                    .setMessage("上传成功！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dg, int which) {
                                                    finish();
                                                }
                                            }).show();
                                        }
                                        else{
                                            new AlertDialog.Builder(AddcommodityActivity.this).setTitle("上传结果")
                                                    .setMessage("上传失败，请稍后重试").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dg, int which) {
                                                    finish();
                                                }
                                            }).show();
                                        }
                                    }
                                };
                                HttpPost httpPost=new HttpPost(json,"add_goods.jsp",handler1);
                                httpPost.doPost();
                            }
                            else {
                                progress.dismiss();
                                new AlertDialog.Builder(AddcommodityActivity.this).setTitle("上传结果")
                                        .setMessage("上传失败，请稍后重试").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dg, int which) {
                                        finish();
                                    }
                                }).show();
                            }
                        }
                    };
                    String path = "/opt/tomcat/webapps/ROOT/school_social/img/commodity/";
                    FtpThread ftpThread = new FtpThread(username + date_time, filename, path, input, handler);
                    progress = ProgressDialog.show(AddcommodityActivity.this, "正在发送数据", "请稍后...");
                    ftpThread.start();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        if(resultCode== Activity.RESULT_OK){
            Uri uri=intent.getData();
            Log.d("tag", "onActivityResult: --->"+uri);
            try{
                String[] pojo={MediaStore.Images.Media.DATA};
                Cursor cursor=managedQuery(uri,pojo,null,null,null);
                if(cursor!=null){
                    ContentResolver cr=this.getContentResolver();
                    Bitmap bitmap= BitmapFactory.decodeStream(cr.openInputStream(uri));
                    HashMap<String,Object> img=new HashMap<>();
                    img.put("img",bitmap);
                    img_list.add(img);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    img_url.add(path);
                }
                else{
                    Log.d("tag", "onActivityResult: 路径有误");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    protected void onResume(){
        super.onResume();
        simpleAdapter=new SimpleAdapter(this,img_list,R.layout.grid_layout,new String[]{"img"},new int[]{R.id.grid_img});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i=(ImageView)view;
                    i.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        img_grid.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

//    public class onSpinnerClickListener implements View.OnClickListener{
//        public void onClick(View view){
//            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
//        }
//    }

    public void back(View view){
        finish();
    }


    public class gridViewListener implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
            if(img_list.size()==6){
                Toast.makeText(AddcommodityActivity.this,"最多选择5张图片",Toast.LENGTH_SHORT).show();
            }
            else if(position==0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
            else{
                AlertDialog.Builder builder=new AlertDialog.Builder(AddcommodityActivity.this);
                builder.setMessage("确认移除已添加图片？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        img_list.remove(position);
                        img_url.remove(position-1);
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        }
    }

    public class mySpinnerListener implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> parent){

        }
        public void onItemSelected(AdapterView<?> parent, View view,int position,long id){
            switch (parent.getId()){
                case R.id.first_kind:
                    second_kind_data=new ArrayList<>();
                    switch (position){
                        case 0:
                            second_kind_data.add("---请先选择第一大类---");
                            break;
                        case 1:
                            second_kind_data.add("---请选择第二大类---");
                            second_kind_data.add("文学");
                            second_kind_data.add("教材");
                            second_kind_data.add("工具书");
                            second_kind_data.add("其它");
                            break;
                        case 2:
                            second_kind_data.add("---请选择第二大类---");
                            second_kind_data.add("手机");
                            second_kind_data.add("相机");
                            second_kind_data.add("电脑");
                            second_kind_data.add("其它");
                            break;
                        case 3:
                            second_kind_data.add("---请选择第二大类---");
                            second_kind_data.add("衣服");
                            second_kind_data.add("裤子");
                            second_kind_data.add("鞋子");
                            second_kind_data.add("其它");
                            break;
                        case 4:
                            second_kind_data.add("---请选择第二大类---");
                            second_kind_data.add("无");
                            break;
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(AddcommodityActivity.this,android.R.layout.simple_spinner_item,second_kind_data);
                    second_kind.setAdapter(adapter);
                    break;
                case R.id.second_kind:
                    third_kind_data=new ArrayList<>();
                    switch (first_kind.getSelectedItemPosition()){
                        case 0:
                            third_kind_data.add("---请先选择第一大类---");
                            break;
                        case 1:
                            switch (position){
                                case 0:
                                    third_kind_data.add("---请先选择第二大类---");
                                    break;
                                case 1:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("历史");
                                    third_kind_data.add("小说");
                                    third_kind_data.add("科普");
                                    third_kind_data.add("其它");
                                    break;
                                case 2:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("数学");
                                    third_kind_data.add("政治");
                                    third_kind_data.add("英语");
                                    third_kind_data.add("专业书籍");
                                    third_kind_data.add("其它");
                                    break;
                                case 3:case 4:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("无");
                                    break;
                            }
                            break;
                        case 2:
                            switch (position){
                                case 0:
                                    third_kind_data.add("---请先选择第二大类---");
                                    break;
                                case 1:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("智能机");
                                    third_kind_data.add("棒棒机");
                                    third_kind_data.add("其它");
                                    break;
                                case 2:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("数码相机");
                                    third_kind_data.add("胶片相机");
                                    third_kind_data.add("其它");
                                    break;
                                case 3:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("笔记本");
                                    third_kind_data.add("台式机");
                                    third_kind_data.add("其它");
                                    break;
                                case 4:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("无");
                                    break;
                            }
                            break;
                        case 3:
                            switch (position){
                                case 0:
                                    third_kind_data.add("---请先选择第二大类---");
                                    break;
                                case 1:case 2:case 3:case 4:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("无");
                                    break;
                            }
                            break;
                        case 4:
                            switch (position){
                                case 0:
                                    third_kind_data.add("---请先选择第二大类---");
                                    break;
                                case 1:
                                    third_kind_data.add("---请选择第三大类---");
                                    third_kind_data.add("无");
                                    break;
                            }
                            break;
                    }
                    ArrayAdapter<String> adapter1=new ArrayAdapter<String>(AddcommodityActivity.this,android.R.layout.simple_spinner_item,third_kind_data);
                    third_kind.setAdapter(adapter1);
            }
        }
    }
}
