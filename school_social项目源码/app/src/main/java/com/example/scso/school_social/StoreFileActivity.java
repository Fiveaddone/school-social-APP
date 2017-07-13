package com.example.scso.school_social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StoreFileActivity extends Activity {
    private ImageView iv_back;
    private TextView tv_back;
    private BootstrapButton bt_quren;
    private TextView file_address;
    private BootstrapEditText file_name;
    private TextView file_size;
    private ImageView file_type;
    private String type;
    private String filename;
    private ImageView iv_store;
    private BufferedInputStream inputStream;
    private String  path;
    private long size;
    private long storesize;
    private ProgressDialog storeprogress;
    private Date date;
    private String describe;
    private boolean file_state;
    private int data_state;
    private String fileuri="/opt/tomcat/webapps/ROOT/school_social/file";
    private BootstrapEditText ed_describe;
    private String date_time;
    private String user;
    int REQUESTCODE_FROM_ACTIVITY = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_file);
        initView();
        back();
        choosefile();
        setQueren();

    }
   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {

                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                List<String> list = data.getStringArrayListExtra("paths");
                //Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();

            }
        }
    }*/
    private void choosefile()
    {
        iv_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*LFilePicker filePicker=new LFilePicker();
                filePicker.withActivity(StoreFileActivity.this);
                filePicker.withTitle("选择上传文件");
                filePicker.withMutilyMode(false);
                filePicker.withRequestCode(REQUESTCODE_FROM_ACTIVITY);
                filePicker.withNotFoundBooks("请选择一个文件");
                filePicker.start();*/
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("txt/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                ProgressDialog progressDialog=new ProgressDialog(StoreFileActivity.this);
                progressDialog.setTitle("");

                Uri uri = data.getData();

                String filePath=uri.getPath().toString();
                String b=getRealFilePath(StoreFileActivity.this,uri);
                path=b;
                String a[]=new String[2];
                File mFile;
                /*if(filePath.indexOf(String.valueOf(Environment.getExternalStorageDirectory()))!=-1)//判断是否在sd卡中
                {
                    a = filePath.split(String.valueOf(Environment.getExternalStorageDirectory()));//切分Uri
                    mFile = new File(Environment.getExternalStorageDirectory(),a[1]);
                }
                else if(filePath.indexOf(String.valueOf(Environment.getDataDirectory()))!=-1){ //判断文件是否在手机内存中
                    //对Uri进行切割
                    a =filePath.split(String.valueOf(Environment.getDataDirectory()));
                    //获取到file
                    mFile = new File(Environment.getDataDirectory(),a[1]);
                }else{
                    //出现其他没有考虑到的情况
                    Toast.makeText(this,"文件路径解析失败！", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                mFile=new File(b);
                try//获取文件大小
                {
                    size =getFileSize(mFile);
                    String filesize=FormetFileSize(size);
                    file_size.setText("文件大小:"+filesize);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                file_address.setText("文件位置:\n\r\r\r\r"+b);
                type= b.substring(b.lastIndexOf(".") + 1,b.length());
                setFile_type(type);
                filename =mFile.getName();
                file_name.setText(filename);
                //Toast.makeText(this, "文件路径："+uri.getPath().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }//上传文件
    private void back()
    {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StoreFileActivity.this);
                dialog.setTitle("请确认");
                dialog.setMessage("是否放弃上传");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setNegativeButton("否",null);
                dialog.show();
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StoreFileActivity.this);
                dialog.setTitle("请确认");
                dialog.setMessage("是否放弃上传");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setNegativeButton("否",null);
                dialog.show();
            }
        });
    }
    private void initView() {
        iv_back=(ImageView) findViewById(R.id.tab_back);
        tv_back=(TextView)findViewById(R.id.tab_back1);
        bt_quren=(BootstrapButton)findViewById(R.id.choosefile);
        file_address= (TextView) findViewById(R.id.file_address);
        file_name= (BootstrapEditText) findViewById(R.id.file_name);
        file_size= (TextView) findViewById(R.id.file_size);
        file_type= (ImageView) findViewById(R.id.file_type);
        iv_store= (ImageView) findViewById(R.id.store);
        ed_describe= (BootstrapEditText) findViewById(R.id.describe);



    }
    private void setQueren()//确认按钮
    {
        bt_quren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StoreFileActivity.this);
                dialog.setTitle("请确认");
                dialog.setMessage("是否确认上传");
                dialog.setCancelable(true);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String fileuri="/opt/tomcat/webapps/ROOT/school_social/file";
                        if(path!=null) {


                            try {
                                int permission = ActivityCompat.checkSelfPermission(StoreFileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    Log.d("tag", "onClick: permission_denied");

                                    ActivityCompat.requestPermissions(StoreFileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                }
                                File f = new File(path);
                                inputStream = new BufferedInputStream(
                                        new FileInputStream(f));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                            date=new Date(System.currentTimeMillis());//获取当前时间
                            String str=formatter.format(date);
                            date_time=str;
                            str+=".";
                            str+=type;
                            describe=ed_describe.getText().toString();
                            filename=file_name.getText().toString();
                            setStoreprogress();


                            FileFtp n = new FileFtp(user, str, fileuri, inputStream, handler,handlerstate);
                            n.start();



                        }
                        else{
                           AlertDialog.Builder dialog2=new AlertDialog.Builder(StoreFileActivity.this);
                            dialog2.setTitle("提示");
                            dialog2.setMessage("未选择上传文件");
                            dialog2.setCancelable(true);
                            dialog2.setPositiveButton("选择上传文件", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("txt//*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    startActivityForResult(intent,1);

                                }
                            });
                            dialog2.show();
                            /*Toast.makeText(getApplicationContext(), "未选择文件，请选择文件", Toast.LENGTH_SHORT).show();*/
                        }
                       /* Intent intent=new Intent(StoreFileActivity.this,FileActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                });
                dialog.setNegativeButton("否",null);
                dialog.show();
            }
        });
    }
    public void setStoreprogress()
    {
        storeprogress=new ProgressDialog(StoreFileActivity.this);
        storeprogress.setMessage("正在上传中......");
        storeprogress.setTitle(filename);
        storeprogress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        storeprogress.setIcon(file_type.getDrawable());
        storeprogress.show();
    }

    public void Storeprogress()
    {

            final String filesize=FormetFileSize(size);

                    double a ;
                    double b=0;

                    if (filesize.endsWith("KB")) {
                        a = (double) size / 1024;
                        b=(double)storesize/1024;
                        storeprogress.setProgressNumberFormat(String.format("%.2f KB/%.2f KB",b,a));
                    } else if (filesize.endsWith("MB")) {
                        a = (double) size / 1048576;
                        b=(double)storesize/1048576;
                        storeprogress.setProgressNumberFormat(String.format("%.2f MB/%.2f MB",b,a));
                    } else if (filesize.endsWith("GB")) {
                        a = (double) size / 1073741824;
                        b=(double)storesize/1073741824;
                        storeprogress.setProgressNumberFormat(String.format("%.2f MB/%.2f MB",b,a));
                    }
                    else   {
                        a = (double) size ;
                        b=(double)storesize;

                        storeprogress.setProgressNumberFormat(String.format("%.2f MB/%.2f MB",b,a));
                    }
        double c=b/a*100;
        storeprogress.setProgress((int)c);

    }

    public static long getFileSize(File file) throws Exception {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    public void setFile_type(String type)
    {
        if (type.equals("txt"))file_type.setImageResource(R.drawable.txt);
        else  if (type.equals("docx")||type.equals("doc")||type.equals("docm")||type.equals("dotx")||type.equals("dotm"))file_type.setImageResource(R.drawable.docx);
        else  if (type.equals("pptx")||type.equals("pptm")||type.equals("ppt")||type.equals("ppsx")||type.equals("potx")||type.equals("potm"))file_type.setImageResource(R.drawable.ppt);
        else  if (type.equals("xls")||type.equals("xlt")||type.equals("xlsx")||type.equals("xlsm")||type.equals("xltx")||type.equals("xltm")||type.equals("xlsb"))file_type.setImageResource(R.drawable.excel);
        else  if (type.equals("rar")||type.equals("zip"))file_type.setImageResource(R.drawable.rar);
        else  if (type.equals("pdf"))file_type.setImageResource(R.drawable.pdf);
        else  if (type.equals("bmp")||type.equals("jpg")||type.equals("png")||type.equals("tiff")||type.equals("gif")||type.equals("pcx")
                ||type.equals("tga")||type.equals("exif")||type.equals("fpx") ||type.equals("svg")||type.equals("psd")||type.equals("cdr")
                ||type.equals("pcd")||type.equals("dxf")||type.equals("ufo")||type.equals("eps")||type.equals("ai")||type.equals("raw")
                ||type.equals("WMF"))file_type.setImageResource(R.drawable.picture);
        else  file_type.setImageResource(R.drawable.file_else);

    }
    private Handler handlerstate=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            file_state=(boolean)msg.obj;
            if(file_state==false){
                storeprogress.dismiss();
                AlertDialog.Builder dialog=new AlertDialog.Builder(StoreFileActivity.this);
                dialog.setTitle(filename);
                dialog.setIcon(file_type.getDrawable());
                dialog.setMessage("上传失败");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
            else if(file_state=true){
                Handler handler1=new Handler()
                {
                    @Override
                    public void handleMessage(Message msg) {
                        data_state=msg.what;
                        if (data_state==3){
                            storeprogress.dismiss();
                            AlertDialog.Builder dialog=new AlertDialog.Builder(StoreFileActivity.this);
                            dialog.setTitle(filename);
                            dialog.setIcon(file_type.getDrawable());
                            dialog.setMessage("上传成功");
                            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            });
                            dialog.show();
                        }
                        else {
                            storeprogress.dismiss();
                            AlertDialog.Builder dialog=new AlertDialog.Builder(StoreFileActivity.this);
                            dialog.setTitle(filename);
                            dialog.setIcon(file_type.getDrawable());
                            dialog.setMessage("上传失败");
                            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.show();

                        }
                    }

                };
                postdata(handler1);
            }
        }
    };
    private Handler handler=new Handler()
    {
        public void handleMessage(Message msg) {
            storesize= (long) msg.obj;
            //file_state=msg.arg1;
            Storeprogress();


        }




    };
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }//绝对路径
    public void postdata(Handler handler)
    {
        try {
            JSONObject file = new JSONObject();
            file.put("owner", user);
            file.put("name", filename);
            file.put("url","/"+user+"/"+date_time+"."+type);
            file.put("size",FormetFileSize(size));
            file.put("type",type);
            file.put("describe",describe);
            HttpPost postdate=new HttpPost(file,"set_file.jsp",handler);
            postdate.doPost();
        }
        catch (JSONException e){
            throw new RuntimeException(e);
        }
    }



}
