package com.example.scso.school_social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CYH on 2017/6/28.
 */

public class ApplyserviceActivity extends Activity {
    private Spinner time1;
    private Spinner time2;
    ArrayList<String> time_list1;
    ArrayList<String> time_list2;
    private Button submit;
    private EditText phone_edit;
    private EditText id_edit;
    private TextView phone_notice;
    private TextView id_notice;
    private TextView time_notice;
    private TextView intro_edit;
    private ProgressDialog progress;
    private EditText name_edit;
    private TextView name_notice;
    private String user_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        user_id=getIntent().getStringExtra("user_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyservice);
        time1=(Spinner)findViewById(R.id.time1);
        time2=(Spinner)findViewById(R.id.time2);
        submit=(Button)findViewById(R.id.submit);
        phone_edit=(EditText)findViewById(R.id.phone_edit);
        id_edit=(EditText)findViewById(R.id.id_edit);
        phone_notice=(TextView)findViewById(R.id.phone_notice);
        id_notice=(TextView)findViewById(R.id.id_notice);
        time_notice=(TextView)findViewById(R.id.time_notice);
        intro_edit=(TextView)findViewById(R.id.intro_edit);
        name_edit=(EditText)findViewById(R.id.name_edit);
        name_notice=(TextView) findViewById(R.id.name_notice);
        time_list1=new ArrayList<>();
        time_list1.add("请选择开始时间");
        for(int i=0;i<23;i++){
            time_list1.add(i+":00");
        }
        ArrayAdapter<String> adapter=new ArrayAdapter(ApplyserviceActivity.this,android.R.layout.simple_spinner_item,time_list1);
        time1.setAdapter(adapter);
        time1.setOnItemSelectedListener(new onSpinnerListenner());
        submit.setOnClickListener(new onSubmitClick());

    }

    public void back(View view){
        finish();
    }

    public class onSubmitClick implements View.OnClickListener{
        public void onClick(View view){
            if(TextUtils.isEmpty(phone_edit.getText())){
                Log.d("tag", "onClick: empty");
                phone_notice.setText("电话号码不能为空！");
                phone_notice.setVisibility(View.VISIBLE);
            }
            else if(TextUtils.isEmpty(id_edit.getText())){
                id_notice.setText("学号不能为空！");
                phone_notice.setVisibility(View.VISIBLE);
            }
            else if(time1.getSelectedItemPosition()==0||time2.getSelectedItemPosition()==0){
                time_notice.setText("请选择开始和结束时间！");
                phone_notice.setVisibility(View.VISIBLE);
            }
            else if(TextUtils.isEmpty(name_edit.getText())){
                name_notice.setText("名称不能为空");
                name_notice.setVisibility(View.VISIBLE);
            }
            else{
//                String sql="insert into service_apply (phone,student_id,start_time,end_time,name,intro,state) values('"+phone_edit.getText()+"','"+id_edit.getText()
//                        +"','"+time1.getSelectedItem()+"','"+time2.getSelectedItem()+"','"+name_edit.getText()+"','"+intro_edit.getText()
//                        +"',0)";
                JSONObject json =new JSONObject();
                try {
                    json.put("user_id",user_id);
                    json.put("phone",phone_edit.getText().toString());
                    json.put("student_id",id_edit.getText().toString());
                    json.put("start_time",time1.getSelectedItem().toString());
                    json.put("end_time",time2.getSelectedItem().toString());
                    json.put("name",name_edit.getText().toString());
                    json.put("intro",intro_edit.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        progress.dismiss();
                        if(msg.what==3){
                            new AlertDialog.Builder(ApplyserviceActivity.this).setTitle("申请结果")
                                    .setMessage("申请成功，请等待管理员审批").setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dg,int which){
                                    finish();
                                }
                            }).show();
                        }
                        else{
                            new AlertDialog.Builder(ApplyserviceActivity.this).setTitle("申请结果")
                                    .setMessage("申请失败，请稍后重试").setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dg,int which){
                                    finish();
                                }
                            }).show();
                        }
                    }
                };
                HttpPost httpPost=new HttpPost(json,"apply_service.jsp",handler);
                httpPost.doPost();
                progress=ProgressDialog.show(ApplyserviceActivity.this,"正在发送申请","请稍后...");
            }
        }
    }

    public class onSpinnerListenner implements AdapterView.OnItemSelectedListener{
        public void onNothingSelected(AdapterView<?> parent){

        }
        public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
            time_list2=new ArrayList<>();
            if(position==0){
                time_list2.add("请先选择开始时间");
            }
            else {
                time_list2.add("请选择结束时间");
                for (; position < 24; position++) {
                    time_list2.add(position + ":00");
                }
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(ApplyserviceActivity.this,android.R.layout.simple_spinner_item,time_list2);
            time2.setAdapter(adapter);
        }
    }
}
