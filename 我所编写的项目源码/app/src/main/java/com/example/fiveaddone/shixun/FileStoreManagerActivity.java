package com.example.fiveaddone.shixun;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileStoreManagerActivity extends Activity {
    private ImageView iv_back;
    private TextView tv_back;
    private ImageView iv_add;
    private List<MyFileList> filelists;
    private ListView mListView;
    private Button bt_delete;
    private TextView gang;
    private boolean delete_state=false;
    MyFileAdapter adapter;
    private ArrayList<String> id;
    private int data_state;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_store_manager);
        initView();
        back();
        choose_delete();
        delete();
        post("18107498983",handler);



    }
    public void delete()
    {
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=adapter.getId();
                AlertDialog.Builder dialog=new AlertDialog.Builder(FileStoreManagerActivity.this);
                dialog.setTitle("删除确认");
                dialog.setMessage("是否删除你所上传的文件("+id.size()+"个文件)");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Handler handler1=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                data_state=msg.what;
                                if (data_state==3){
                                    AlertDialog.Builder dialog=new AlertDialog.Builder(FileStoreManagerActivity.this);
                                    dialog.setTitle("成功！");
                                    dialog.setMessage("删除成功");
                                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {



                                        }
                                    });
                                    dialog.show();
                                }
                                else {

                                    AlertDialog.Builder dialog=new AlertDialog.Builder(FileStoreManagerActivity.this);
                                    dialog.setTitle("数据库连接失败");
                                    dialog.setMessage("删除失败");
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
                        post("18107498983",handler);
                        adapter.notifyDataSetChanged();
                        iv_add.setImageResource(R.drawable.add);
                        bt_delete.setVisibility(View.GONE);
                        gang.setVisibility(View.GONE);
                        delete_state=false;
                        adapter.setTrue(false);


                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });
    }
    public void choose_delete(){
        iv_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(delete_state==false) {
                    iv_add.setImageResource(R.drawable.add1);
                    bt_delete.setVisibility(View.VISIBLE);
                    gang.setVisibility(View.VISIBLE);
                    delete_state=true;
                    adapter.setTrue(true);


                }
                else if(delete_state==true){
                    iv_add.setImageResource(R.drawable.add);
                    bt_delete.setVisibility(View.GONE);
                    gang.setVisibility(View.GONE);
                    delete_state=false;
                    adapter.setTrue(false);
                }
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            adapter=new MyFileAdapter(FileStoreManagerActivity.this,R.layout.myfile_item,filelists,false,bt_delete);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MyFileList list=filelists.get(position);
                    String file_id=list.getId();
                    Intent intent=new Intent(FileStoreManagerActivity.this,FileInformationActivity.class);
                    intent.putExtra("file_id",file_id);
                    startActivity(intent);
                }
            });

        }
    };
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.tab_back);
        tv_back = (TextView) findViewById(R.id.tab_back1);
        iv_add = (ImageView) findViewById(R.id.add);
        mListView = (ListView) findViewById(R.id.listView);
        bt_delete= (Button) findViewById(R.id.file_delete);
        gang= (TextView) findViewById(R.id.gang);
    }
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
    public void post(final String owner, final Handler handler)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uriAPI = null;
                ArrayList<HashMap<String,String>> service_add=new ArrayList<HashMap<String, String>>();
                try {
                    uriAPI = "http://118.89.199.187/school_social/jsp/get_mystorefile.jsp?owner=" + URLEncoder.encode(owner,"utf-8");
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
                                service.put("check",data.getJSONObject(i).getString("check"));
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
    private void initFiles(ArrayList<HashMap<String,String>> service){
        filelists=new ArrayList<MyFileList>();
        for(int i=0;i<service.size();i++) {
            String name=service.get(i).get("name");
            String type=service.get(i).get("type");
            String date=service.get(i).get("time");
            String size=service.get(i).get("size");
            String id=service.get(i).get("id");
            String check_state=service.get(i).get("check");
            if (type.equals("txt"))filelists.add(new MyFileList(name,R.drawable.txt,date,size,id,check_state));
            else  if (type.equals("docx")||type.equals("doc")||type.equals("docm")||type.equals("dotx")||type.equals("dotm"))filelists.add(new MyFileList(name,R.drawable.docx,date,size,id,check_state));
            else  if (type.equals("pptx")||type.equals("pptm")||type.equals("ppt")||type.equals("ppsx")||type.equals("potx")||type.equals("potm"))filelists.add(new MyFileList(name,R.drawable.ppt,date,size,id,check_state));
            else  if (type.equals("xls")||type.equals("xlt")||type.equals("xlsx")||type.equals("xlsm")||type.equals("xltx")||type.equals("xltm")||type.equals("xlsb"))filelists.add(new MyFileList(name,R.drawable.excel,date,size,id,check_state));
            else  if (type.equals("rar")||type.equals("zip"))filelists.add(new MyFileList(name,R.drawable.rar,date,size,id,check_state));
            else  if (type.equals("pdf"))filelists.add(new MyFileList(name,R.drawable.pdf,date,size,id,check_state));
            else  if (type.equals("bmp")||type.equals("jpg")||type.equals("png")||type.equals("tiff")||type.equals("gif")||type.equals("pcx")
                    ||type.equals("tga")||type.equals("exif")||type.equals("fpx") ||type.equals("svg")||type.equals("psd")||type.equals("cdr")
                    ||type.equals("pcd")||type.equals("dxf")||type.equals("ufo")||type.equals("eps")||type.equals("ai")||type.equals("raw")
                    ||type.equals("WMF"))filelists.add(new MyFileList(name,R.drawable.picture,date,size,id,check_state));
            else  filelists.add(new MyFileList(name,R.drawable.file_else,date,size,id,check_state));
        }
        Message msg=Message.obtain(handler,3);
        handler.sendMessage(msg);

    }
    public void postdata(Handler handler)
    {
        try {
            JSONObject file = new JSONObject();
            for (int i=0;i<id.size();i++) {
                String i_d="id"+i;
                file.put(i_d, id.get(i));
            }
            file.put("size",id.size());
            HttpPost postdate=new HttpPost(file,"delete_myfile.jsp",handler);
            postdate.doPost();
        }
        catch (JSONException e){
            throw new RuntimeException(e);
        }
    }
}
