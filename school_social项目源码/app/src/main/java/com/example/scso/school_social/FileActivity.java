package com.example.scso.school_social;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileActivity extends Activity {
    private String user_id;
    private ImageView iv_back;
    private TextView tv_back;
    private LinearLayout titleLin;
    private ImageView iv_add;
    private SearchView mSearchView;
    private ListView mListView;
    private TextView tv_title;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private List<FileList> filelists;
    private int mOriginiv_addTop;
    private GestureDetectorCompat mDetectorCompat;
    private Myhandler handler=new Myhandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user_id=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        /*initFiles();*/

        initView();
        search();
        huadongyingcang();
        back();



        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMemu(iv_add);

            }
        });

    }
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if (msg.arg1==1){
               Toast.makeText(FileActivity.this,"没有网络，请连接网络",Toast.LENGTH_SHORT).show();
           }
        }
    };
    private void back()
    {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        search();

    }

    public class Myhandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            FileAdapter adapter=new FileAdapter(FileActivity.this,R.layout.file_item,filelists);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FileList list=filelists.get(position);
                    String file_id=list.getId();
                    Intent intent=new Intent(FileActivity.this,FileInformationActivity.class);
                    intent.putExtra("file_id",file_id);
                    intent.putExtra("user_id",user_id);
                    startActivity(intent);
                }
            });

        }
    }
    private void ShowMemu(View view)//菜单
    {
        PopupMenu pm=new PopupMenu(this,view);
        pm.getMenuInflater().inflate(R.menu.search,pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().toString().equals("发布上传文件"))
                {
                    Intent intent=new Intent(FileActivity.this,StoreFileActivity.class);
                    intent.putExtra("user_id",user_id);
                    startActivity(intent);
                }
                else if (item.getTitle().toString().equals("下载文件管理"))startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                else if (item.getTitle().toString().equals("已上传文件管理")){
                    Intent intent=new Intent(FileActivity.this,FileStoreManagerActivity.class);
                    intent.putExtra("user_id",user_id);
                    startActivity(intent);
                }
               else Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        pm.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
               // Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        pm.show();

    }


    private void search(){
        if(mSearchView!=null)
        {
            try{
                Class<?> argClass = mSearchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
                int id=mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
                TextView tv=(TextView)mSearchView.findViewById(id);
                //tv.setHint("请输入内容");
               // tv.setText("输入内容");
               tv.setHintTextColor(Color.parseColor("#a6a6a6"));
               // tv.setTextColor(Color.parseColor("#00ff00"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//去下标

        /*mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));*/
        /*FileAdapter adapter=new FileAdapter(FileActivity.this,R.layout.file_item,filelists);
        mListView.setAdapter(adapter);*/
        post("",handler1);
      mSearchView.onActionViewExpanded();
        //mSearchView.setBackgroundColor(0xffeeeff3);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
               /* if (!TextUtils.isEmpty(newText)){
                    *//*mListView.setFilterText(newText);*//*
                    post(newText);

                }else{
                    mListView.clearTextFilter();
                }*/
                post(newText,handler1);

                return false;
            }

        });


    }//搜索
    private void huadongyingcang(){
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到顶部
                        if (mListView.getLastVisiblePosition() == 0) {
                            tv_title.setVisibility(View.VISIBLE);
                            titleLin.setVisibility(View.VISIBLE);
                        }
                        // 判断滚动到底部
                      /*  if (mListView.getLastVisiblePosition() == (mListView
                                .getCount() - 1)) {
                            tv_title.setVisibility(View.VISIBLE);
                            titleLin.setVisibility(View.VISIBLE);
                        }*/
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = false;
                        break;
                }
            }
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮

                if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                    tv_title.setVisibility(View.GONE);
                    titleLin.setVisibility(View.GONE);
                } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                    tv_title.setVisibility(View.VISIBLE);
                    titleLin.setVisibility(View.VISIBLE);
                } else {
                    return;
                }
                lastVisibleItemPosition = firstVisibleItem;
            }



        });
    }//控件效果
    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {
        iv_back=(ImageView) findViewById(R.id.tab_back);
        tv_back=(TextView)findViewById(R.id.tab_back1);
        iv_add=(ImageView)findViewById(R.id.add);
        tv_title= (TextView) findViewById(R.id.file_title);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.clearFocus();
        mListView = (ListView) findViewById(R.id.listView);
        titleLin = (LinearLayout) findViewById(R.id.title);


    }
    private void initFiles(ArrayList<HashMap<String,String>> service){
        filelists=new ArrayList<FileList>();
        for(int i=0;i<service.size();i++) {
            String name=service.get(i).get("name");
            String type=service.get(i).get("type");
            String date=service.get(i).get("time");
            String size=service.get(i).get("size");
            String id=service.get(i).get("id");
            String likes=service.get(i).get("likes");
            String nickname=service.get(i).get("nickname");
            if (type.equals("txt"))filelists.add(new FileList(name,R.drawable.txt,date,size,id,likes,nickname));
            else  if (type.equals("docx")||type.equals("doc")||type.equals("docm")||type.equals("dotx")||type.equals("dotm"))filelists.add(new FileList(name,R.drawable.docx,date,size,id,likes,nickname));
            else  if (type.equals("pptx")||type.equals("pptm")||type.equals("ppt")||type.equals("ppsx")||type.equals("potx")||type.equals("potm"))filelists.add(new FileList(name,R.drawable.ppt,date,size,id,likes,nickname));
            else  if (type.equals("xls")||type.equals("xlt")||type.equals("xlsx")||type.equals("xlsm")||type.equals("xltx")||type.equals("xltm")||type.equals("xlsb"))filelists.add(new FileList(name,R.drawable.excel,date,size,id,likes,nickname));
            else  if (type.equals("rar")||type.equals("zip"))filelists.add(new FileList(name,R.drawable.rar,date,size,id,likes,nickname));
            else  if (type.equals("pdf"))filelists.add(new FileList(name,R.drawable.pdf,date,size,id,likes,nickname));
            else  if (type.equals("bmp")||type.equals("jpg")||type.equals("png")||type.equals("tiff")||type.equals("gif")||type.equals("pcx")
                    ||type.equals("tga")||type.equals("exif")||type.equals("fpx") ||type.equals("svg")||type.equals("psd")||type.equals("cdr")
                    ||type.equals("pcd")||type.equals("dxf")||type.equals("ufo")||type.equals("eps")||type.equals("ai")||type.equals("raw")
                    ||type.equals("WMF"))filelists.add(new FileList(name,R.drawable.picture,date,size,id,likes,nickname));
            else  filelists.add(new FileList(name,R.drawable.file_else,date,size,id,likes,nickname));
        }
        Message msg=Message.obtain(handler,3);
        handler.sendMessage(msg);

    }
    public void post(final String name, final Handler handler)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uriAPI = null;
                ArrayList<HashMap<String,String>> service_add=new ArrayList<HashMap<String, String>>();
                try {
                    uriAPI = "http://118.89.199.187/school_social/jsp/file_find.jsp?name=" + URLEncoder.encode(name,"utf-8");
/*                    Log.w("fff",URLEncoder.encode(name,"utf-8"));*/
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Message msg=Message.obtain(handler,3);
                    msg.arg1=1;
                    handler.sendMessage(msg);
                }

                try {
                    URL url = new URL(uriAPI);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Charset","GBK");
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10 * 1000);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        Log.w("fff","123");
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        try {
                            String jsonstr = URLDecoder.decode(in.readLine(), "utf-8");
                            final JSONObject jsonObject = new JSONObject(jsonstr);
                            JSONArray data = jsonObject.getJSONArray("Data");
                            Log.e("666",data.toString());
                            for(int i=0;i<data.length();i++){
                                HashMap<String,String> service=new HashMap<>();
                                service.put("name",data.getJSONObject(i).getString("name"));
                                service.put("type",data.getJSONObject(i).getString("type"));
                                service.put("size",data.getJSONObject(i).getString("size"));
                                service.put("time",data.getJSONObject(i).getString("time"));
                                service.put("id",data.getJSONObject(i).getString("id"));
                                service.put("likes",data.getJSONObject(i).getString("likes"));
                                service.put("nickname",data.getJSONObject(i).getString("nickname"));
                                service_add.add(service);




                            }
                            initFiles(service_add);


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }






}
