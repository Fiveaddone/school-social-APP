package com.example.scso.school_social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;



public class PersonalInformationActivity extends Activity {
    private ImageView iv_back;
    private TextView tv_back;
    private BootstrapButton bt_next;
    private BootstrapEditText et_mima;
    private BootstrapEditText et_remima;
    private BootstrapEditText et_nichen;
    private BootstrapEditText et_realname;
    private BootstrapEditText et_personalID;
    private BootstrapEditText et_schoolID;
    private BootstrapDropDown s_school;
    private BootstrapDropDown s_xinbie;
    private BootstrapCircleThumbnail mImage;
    private AwesomeTextView tv_phone;
    private ImageView revise;
    private String phone;
    private String mima;
    private String remima;
    private String nickname;
    private String realname;
    private String ID;
    private String studentID;
    private InputStream img_is;
    private String img_name;
    private String school="请选择学校";
    private String sex="请选择性别";
    private Handler mHandler;
    private Handler http_handler;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private Bitmap mBitmap;
    private ProgressDialog pg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        mHandler = new Handler(){
            public void handleMessage(Message msg) {
                if((boolean)msg.obj==true){
                    //数据库操作
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("phone", phone);
                        jsonObject.put("mima",mima);
                        jsonObject.put("nickname",nickname);
                        jsonObject.put("realname",realname);
                        jsonObject.put("ID",ID);
                        jsonObject.put("studentID",studentID);
                        jsonObject.put("school",school);
                        jsonObject.put("sex",sex);
                        jsonObject.put("headpicture",img_name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpPost httppost_personalinformation = new HttpPost(jsonObject,"set_information.jsp",http_handler);
                    httppost_personalinformation.doPost();
                }
                else {
                    Toast.makeText(PersonalInformationActivity.this,"上传头像失败", Toast.LENGTH_SHORT).show();
                }
                /*switch (msg.obj){
                    case 1:
                        Toast.makeText(PersonalInformationActivity.this,"注册失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(PersonalInformationActivity.this,"注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PersonalInformationActivity.this, LoginActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                        break;
                }*/
            }
        };
        http_handler = new Handler(){
            public void handleMessage(Message msg){
                pg.dismiss();
                switch (msg.what){
                    case 1:
                        Toast.makeText(PersonalInformationActivity.this,"注册失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(PersonalInformationActivity.this,"注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PersonalInformationActivity.this, LoginActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };
        initView();
        Intent intent=getIntent();
        tv_phone.setText(intent.getStringExtra("zhuce_phone"));
        s_school.setOnDropDownItemClickListener(new BootstrapDropDown.OnDropDownItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int id) {
                s_school.setText(getResources().getStringArray(R.array.school)[id]);
            }
        });
        s_xinbie.setOnDropDownItemClickListener(new BootstrapDropDown.OnDropDownItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int id) {
                s_xinbie.setText(getResources().getStringArray(R.array.xinbie)[id]);
            }
        });

        mImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showChoosePicDialog();
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mima=et_mima.getText().toString().replaceAll(" ","");
                remima=et_remima.getText().toString().replaceAll(" ","");
                nickname=et_nichen.getText().toString().replaceAll(" ","");
                realname=et_realname.getText().toString().replaceAll(" ","");
                ID=et_personalID.getText().toString().replaceAll(" ","");
                studentID=et_schoolID.getText().toString().replaceAll(" ","");
                school=s_school.getText().toString();
                sex=s_xinbie.getText().toString();
                phone=tv_phone.getText().toString();
                //phone = "13238813725";

                img_name = phone+".jpg";

                if(mima.equals("")) {
                    Toast.makeText(PersonalInformationActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                }
                else if(mima.length()<6){
                        Toast.makeText(PersonalInformationActivity.this,"密码不足6位",Toast.LENGTH_SHORT).show();
                    }
                else if(remima.equals("")) {
                    Toast.makeText(PersonalInformationActivity.this, "请重复密码填写", Toast.LENGTH_SHORT).show();
                }
                else if(!remima.equals(mima)) {
                    Toast.makeText(PersonalInformationActivity.this,"两次密码输入不同",Toast.LENGTH_SHORT).show();
                }
                else if(nickname.equals("")){
                    Toast.makeText(PersonalInformationActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(realname.equals("")){
                    Toast.makeText(PersonalInformationActivity.this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(sex.equals("请选择性别")){
                    Toast.makeText(PersonalInformationActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
                }
                else if(ID.equals("")){
                    Toast.makeText(PersonalInformationActivity.this, "身份证号不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(studentID.equals("")){
                    Toast.makeText(PersonalInformationActivity.this, "学生证号不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(school.equals("请选择学校")){
                    Toast.makeText(PersonalInformationActivity.this, "请选择学校", Toast.LENGTH_SHORT).show();
                }
                else if(mBitmap==null||mBitmap.isRecycled()){
                    Toast.makeText(PersonalInformationActivity.this, "请选择头像", Toast.LENGTH_SHORT).show();
                }
                else {
                    pg=ProgressDialog.show(PersonalInformationActivity.this,"正在注册","注册中...");
                    Thread ftpthread = new Thread(){
                        public void run() {
                            String url ="118.89.199.187";
                            String username ="ubuntu";
                            String password ="cyh962014";
                            String path = "/opt/tomcat/webapps/ROOT/school_social/img/account/";
                            img_is = Bitmap2InputStream(mBitmap);
                            FTPClient ftpClient = new FTPClient();
                            boolean state=false;
                            try {
                                ftpClient.connect(url);
                                ftpClient.login(username,password);
                                int reply=ftpClient.getReplyCode();
                                if(FTPReply.isPositiveCompletion(reply)) {
                                    ftpClient.changeWorkingDirectory(path);
                                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                    ftpClient.storeFile(img_name,img_is);
                                    img_is.close();
                                    state = true;
                                }
                                ftpClient.logout();
                                Message msg=Message.obtain(mHandler,3);
                                msg.obj=state;
                                mHandler.sendMessage(msg);
                            }catch (IOException e){
                                e.printStackTrace();
                                Message msg=Message.obtain(mHandler,3);
                                msg.obj=state;
                                mHandler.sendMessage(msg);
                            }
                        }
                    };
                    ftpthread.start();
                }
            }
        });

    }


    void initView() {
        bt_next = (BootstrapButton) findViewById(R.id.bt_next);
        et_mima=(BootstrapEditText)findViewById(R.id.password) ;
        et_remima=(BootstrapEditText)findViewById(R.id.re_password) ;
        et_nichen=(BootstrapEditText)findViewById(R.id.name) ;
        et_realname=(BootstrapEditText)findViewById(R.id.real_name) ;
        et_personalID=(BootstrapEditText)findViewById(R.id.personalID) ;
        et_schoolID=(BootstrapEditText)findViewById(R.id.schoolID) ;
        s_school=(BootstrapDropDown) findViewById(R.id.s_school);
        s_xinbie=(BootstrapDropDown)findViewById(R.id.s_xinbie);
        tv_phone=(AwesomeTextView)findViewById(R.id.xingxi_phone);
        revise=(ImageView)findViewById(R.id.revise);
        mImage=(BootstrapCircleThumbnail) findViewById(R.id.bcircle_border_change_example);
    }
    private void back() {

        iv_back = (ImageView) findViewById(R.id.tab_back);
        tv_back = (TextView) findViewById(R.id.tab_back1);
        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PersonalInformationActivity.this);
                dialog.setTitle("请确认");
                dialog.setMessage("是否放弃注册");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PersonalInformationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("否",null);
                dialog.show();
            }

        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PersonalInformationActivity.this);
                dialog.setTitle("请确认");
                dialog.setMessage("是否放弃注册");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PersonalInformationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("否",null);
                dialog.show();
            }
        });

    };
    public void onBackPressed(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(PersonalInformationActivity.this);
        dialog.setTitle("请确认");
        dialog.setMessage("是否放弃注册");
        dialog.setCancelable(false);
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PersonalInformationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNegativeButton("否",null);
        dialog.show();
    }
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;

                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PersonalInformationActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.d("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            mImage.setImageBitmap(mBitmap);//显示图片
            //在这个地方可以写上上传该图片到服务器的代码
        }
    }

    public InputStream Bitmap2InputStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }




}

