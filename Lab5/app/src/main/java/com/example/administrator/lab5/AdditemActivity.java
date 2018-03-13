package com.example.administrator.lab5;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/11.
 */

public class AdditemActivity extends AppCompatActivity {

    EditText editText_name;
    EditText editText_birth;
    EditText editText_gift;
    Button button_add_done;

    private static final String TABLE_NAME = "Person_Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editText_name = (EditText)findViewById(R.id.editText_name);
        editText_birth = (EditText)findViewById(R.id.editText_birth);
        editText_gift = (EditText)findViewById(R.id.editText_gift);
        button_add_done = (Button)findViewById(R.id.button_add_done);

        set_Button();
    }

    public void set_Button()
    {
        button_add_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name;
                String new_birth;
                String new_gift;

                if(TextUtils.isEmpty(editText_name.getText()))
                {
                    Toast.makeText(AdditemActivity.this,"Name is empty,please enter sth",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new_name = editText_name.getText().toString();
                    new_birth = editText_birth.getText().toString();
                    new_gift = editText_gift.getText().toString();

                    myDB db = new myDB(getBaseContext());
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where name like ?", new String[]{new_name});

                    if(cursor.moveToFirst())
                    {
                        Toast.makeText(AdditemActivity.this,"repeated name",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        db.insert(new_name,new_birth,new_gift);
                        sqLiteDatabase.close();
                        setResult(100,new Intent());
                        finish();
                    }
                    cursor.close();
                }
            }
        });
    }
}
