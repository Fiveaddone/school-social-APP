package com.example.scso.school_social;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        Button button1=(Button)findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ManagerActivity.this,PowerCheck.class);
                startActivity(intent);
            }
        });
        Button button2=(Button)findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ManagerActivity.this,FileCheck.class);
                startActivity(intent);
            }
        });
        Button button3=(Button)findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ManagerActivity.this,ServerCheck.class);
                startActivity(intent);
            }
        });
        //Button button4=(Button)findViewById(R.id.button_4);
        //button4.setOnClickListener(new View.OnClickListener(){
          //  @Override
           // public void onClick(View v){
         //       Intent intent=new Intent(ManagerActivity.this,HdCheck.class);
            //    startActivity(intent);
          //  }
       // });
    }
}
