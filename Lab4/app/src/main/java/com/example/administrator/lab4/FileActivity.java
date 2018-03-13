package com.example.administrator.lab4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FileActivity extends AppCompatActivity {

    public EditText editText_content_name;
    public EditText editText_content;
    public Button button_save;
    public Button button_load;
    public Button button_clear;
    public Button button_delete;
    public FileOutputStream fileOutputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        editText_content_name = (EditText)findViewById(R.id.editText_content_name);
        editText_content = (EditText)findViewById(R.id.editText_content);
        button_save = (Button)findViewById(R.id.button_save);
        button_load = (Button)findViewById(R.id.button_load);
        button_clear = (Button)findViewById(R.id.button_clear);
        button_delete = (Button)findViewById(R.id.button_delete);

        SysApplication.getInstance().addActivity(this);
        Set_Buttons();
    }

    public void Set_Buttons()
    {
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_content.setText("");
                //editText_content_name.setText("");
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = editText_content_name.getText().toString();
                String fileContent = editText_content.getText().toString();
                try
                {
                    FileOutputStream fileOutputStream = openFileOutput(fileName,MODE_PRIVATE);
                    fileOutputStream.write(fileContent.getBytes());
                    Toast.makeText(FileActivity.this,"Successfully saved file.",Toast.LENGTH_SHORT).show();
                    fileOutputStream.close();
                }
                catch(IOException ex)
                {
                    Toast.makeText(FileActivity.this,"Fail to save file.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = editText_content_name.getText().toString();
                String fileContent = editText_content.getText().toString();
                try
                {
                    FileInputStream fileInputStream = openFileInput(fileName);
                    byte[] contents = new byte[fileInputStream.available()];
                    fileInputStream.read(contents);
                    fileContent = new String(contents);
                    editText_content.setText(fileContent);
                    fileInputStream.close();

                    Toast.makeText(FileActivity.this,"Successfully load file.",Toast.LENGTH_SHORT).show();
                }
                catch(IOException ex)
                {
                    Toast.makeText(FileActivity.this,"Fail to load file.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = editText_content_name.getText().toString();
                deleteFile(fileName);
                Toast.makeText(FileActivity.this,"Delete successfully.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent keyEvent) {
        if(KeyCode == KeyEvent.KEYCODE_BACK) {
            SysApplication.getInstance().exit();
        }
        return true;
    }



}
