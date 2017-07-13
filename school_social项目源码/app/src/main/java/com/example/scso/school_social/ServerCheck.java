package com.example.scso.school_social;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class ServerCheck extends AppCompatActivity {
    //TextView message=null;
    //private Handler handler1;
    private Handler handler;
    private ListView mlistview;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_check);
        mlistview= (ListView) findViewById(R.id.listView3);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ArrayList<HashMap<String,Object>> list;
                list=( ArrayList<HashMap<String,Object>>)msg.obj;

                MyAdapter3 adapter=new MyAdapter3(ServerCheck.this,list);
                mlistview.setAdapter(adapter);
            }
        };
        // Button btn=(Button) findViewById(R.id.btn);
        // btn.setOnClickListener(new View.OnClickListener() {
        //public void run() {
        //在android中操作数据库最好在子线程中执行，否则可能会报异常
        new Thread() {
            public void run() {
                Connection conn = null;
                Statement stmt = null;
                // ArrayList<HashMap<String,String>> list=new ArrayList<>();

                try {
                    //注册驱动
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://118.89.199.187:3306/school_social";
                    conn = (Connection) DriverManager.getConnection(url, "root", "root");
                    stmt = (Statement) conn.createStatement();
                    String sql = "select * from service_apply where state='"+0+"'";
                    ResultSet rs = stmt.executeQuery(sql);

                    ArrayList<HashMap<String,Object>> commodity_list=new ArrayList<>();
                    while (rs.next()) {
                        //Log.v("yzy", "field1-->" + rs.getString(1) + "  field2-->" + rs.getString(2));
                        System.out.println(rs.getString("id"));
                        System.out.println(rs.getString("name"));
                        System.out.println(rs.getString("state"));
                        //message.append(rs.getString("check") + "\n");
                        HashMap<String,Object> commodity_info=new HashMap<>();
                        commodity_info.put("server_id",rs.getString("id"));
                        commodity_info.put("servername",rs.getString("name"));
                        commodity_info.put("state",rs.getString("state"));
                        commodity_info.put("intro",rs.getString("intro"));
                        commodity_list.add(commodity_info);
                    }


                    rs.close();
                    stmt.close();
                    conn.close();
                    Message msg=Message.obtain(handler,3);
                    msg.obj=commodity_list;
                    handler.sendMessage(msg);
                    Log.v("yzy", "success to connect!");
                }
                catch (ClassNotFoundException e) {
                    Log.v("yzy", "fail to connect!" + "  " + e.getMessage());
                }
                catch (SQLException e) {
                    Log.v("yzy", "fail to connect!" + "  " + e.getMessage());
                }
            }
        }.start();
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, final long id) {

                AlertDialog.Builder dialog =new AlertDialog.Builder(ServerCheck.this);
                dialog.setTitle("注意");
                dialog.setMessage("是否审核通过该服务");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        int position_now=position-parent.getFirstVisiblePosition();
                        View v=(View)parent.getChildAt(position_now);
                        TextView id_text=(TextView)v.findViewById(R.id.server_id);
                        final String id=id_text.getText().toString();

                        //System.out.println(id);

                        new Thread() {
                            public void run() {
                                Connection conn = null;
                                Statement stmt = null;
                                // ArrayList<HashMap<String,String>> list=new ArrayList<>();
                                System.out.println(id);
                                try {
                                    //注册驱动
                                    //System.out.println("11111");
                                    Class.forName("com.mysql.jdbc.Driver");
                                    String url = "jdbc:mysql://118.89.199.187:3306/school_social";
                                    conn = (Connection) DriverManager.getConnection(url, "root", "root");
                                    stmt = (Statement) conn.createStatement();
                                    String sql = "update service_apply set state=1 where id='"+id+"'";
                                    //System.out.println(sql);
                                    int rs = stmt.executeUpdate(sql);

                                    System.out.println(id);

                                    if(rs==1) {
                                        System.out.println(id);
                                        //System.out.println(rs.getString("power"));
                                    }
                                    else{
                                        System.out.println("dddd");
                                    }


                                    stmt.close();
                                    conn.close();
                                    Log.v("yzy", "success to connect!");
                                }
                                catch (ClassNotFoundException e) {
                                    Log.v("yzy", "fail to connect!" + "  " + e.getMessage());
                                }
                                catch (SQLException e) {
                                    Log.v("yzy", "fail to connect!" + "  " + e.getMessage());
                                }
                            }


                        }.start();


                        finish();
                        Intent intent=new Intent(ServerCheck.this,ServerCheck.class);
                        startActivity(intent);

                    }
                });
                dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){

                    }
                });
                dialog.show();
                //System.out.println(position);


                //Toast.makeText(ManagerActivity.this,"1111111111"+position,Toast.LENGTH_SHORT).show();
            }

        });
        //}
        //}//);





    }
}

