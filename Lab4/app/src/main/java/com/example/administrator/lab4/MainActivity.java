package com.example.administrator.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Boolean is_Registered;
    public EditText editText_newpassword;
    public EditText editText_confirmpassword;
    public Button button_ok;
    public Button button_clear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_newpassword = (EditText)findViewById(R.id.editText_newpassword);
        editText_confirmpassword = (EditText)findViewById(R.id.editText_confirmpassword);
        button_ok = (Button)findViewById(R.id.button_ok);
        button_clear = (Button)findViewById(R.id.button_clear);

        sharedPreferences = getSharedPreferences("Password",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        SysApplication.getInstance().addActivity(this);
        Check_PassWord();
        Set_Buttons();
    }

    public void Check_PassWord()
    {
        if(TextUtils.isEmpty(sharedPreferences.getString("Password",null)))
        {
            is_Registered=false;
            editText_newpassword.setVisibility(View.VISIBLE);
            editText_newpassword.setHint("New Password");
            editText_confirmpassword.setVisibility(View.VISIBLE);
            editText_confirmpassword.setHint("Confirm Password");
        }
        else
        {
            is_Registered=true;
            editText_newpassword.setVisibility(View.GONE);
            editText_confirmpassword.setVisibility(View.VISIBLE);
            editText_confirmpassword.setHint("Password");
        }
    }

    public void Set_Buttons()
    {

        button_ok.setOnClickListener(new View.OnClickListener() {
            String newPassword = "";
            String confirmPassword = "";
            String inputPassword="";
            Intent intent = new Intent(MainActivity.this, FileActivity.class);
            @Override
            public void onClick(View v) {
                newPassword = editText_newpassword.getText().toString();
                confirmPassword = editText_confirmpassword.getText().toString();
                if(!is_Registered)//没有注册
                {
                    if(newPassword.equals("")||confirmPassword.equals(""))//判断是否输入为空
                    {
                        Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(!newPassword.equals(confirmPassword))//判断两次是否相等
                    {
                        Toast.makeText(MainActivity.this,"Password is not equals to confirmPassword",Toast.LENGTH_SHORT).show();
                    }
                    else//无错误，将输入设成密码
                    {
                        editor.putString("Password",newPassword);
                        editor.commit();
                        startActivity(intent);//设完后跳转
                    }
                }
                else if(is_Registered)//已经注册
                {
                    inputPassword = editText_confirmpassword.getText().toString();
                    if(inputPassword.equals(sharedPreferences.getString("Password",null)))
                    {
                        startActivity(intent);//输入正确直接跳转
                    }
                    else//输错了弹出信息
                    {
                        Toast.makeText(MainActivity.this,"Password Mismatch!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_newpassword.setText("");
                editText_confirmpassword.setText("");
            }
        });
    }


}
