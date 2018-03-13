package com.example.administrator.lab5;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.DialogPreference;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TABLE_NAME = "Person_Info";

    public Button button_add_new;
    public ListView listView_person_list;
    public SimpleAdapter adapter;
    public TextView textView_name;
    public EditText editText_birth;
    public EditText editText_gift;
    public TextView editText_phone;

    public List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_add_new = (Button)findViewById(R.id.button_add_new);
        listView_person_list = (ListView)findViewById(R.id.listView_personList);


        onDataBaseUpdate();//从数据库中导出数据来更新页面
        set_DeleteContacts();//设置删除联系人动作
        set_button();//设置按钮动作
        set_editDataBase();//设置编辑动作

    }

    public void set_button()
    {
        button_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AdditemActivity.class);
                startActivityForResult(intent,100);
            }
        });
    }

    public void onDataBaseUpdate()
    {
        myDB db = new myDB(getBaseContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        datas.clear();
        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                String t_name = cursor.getString(0);
                String t_birth = cursor.getString(1);
                String t_gift = cursor.getString(2);
                Map<String, String> map = new HashMap<String, String>();
                map.put("name",t_name);
                map.put("birth",t_birth);
                map.put("gift",t_gift);
                datas.add(map);
            }
            adapter = new SimpleAdapter(MainActivity.this,datas,R.layout.layout_item,new String[]{"name","birth","gift"},new int[]{R.id.textView_name_L,R.id.textView_birth_L,R.id.textView_gift_L});
            listView_person_list.setAdapter(adapter);
        }
    }

    public void set_DeleteContacts()
    {
        listView_person_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder message = new AlertDialog.Builder(MainActivity.this);
                message.setTitle("Delete Contacts");
                message.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB db = new myDB(getBaseContext());
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.delete(datas.get(position).get("name"));
                        sqLiteDatabase.close();
                        datas.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                message.setNegativeButton("No",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //do nothing
                    }
                });
                message.create().show();
                return true;
            }
        });
    }

    public void set_editDataBase()
    {
        listView_person_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View newView = layoutInflater.inflate(R.layout.activity_dialog,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},0);
                }

                textView_name = (TextView)newView.findViewById(R.id.textView_name_D);
                editText_birth = (EditText)newView.findViewById(R.id.editText_birth_D);
                editText_gift = (EditText)newView.findViewById(R.id.editText_gift_D);
                editText_phone = (EditText)newView.findViewById(R.id.editText_phone_D);

                textView_name.setText(datas.get(position).get("name"));
                editText_birth.setText(datas.get(position).get("birth"));
                editText_gift.setText(datas.get(position).get("gift"));

                String phone_number = "";
                Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                while(cursor.moveToNext())
                {
                    String contacts_id = cursor.getString(cursor.getColumnIndex("_id"));
                    if(cursor.getString(cursor.getColumnIndex("display_name")).equals(textView_name.getText().toString()))
                    {
                        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex("has_phone_number")))>0)
                        {
                            Cursor cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,"contact_id = " + contacts_id, null , null);
                            while(cursor2.moveToNext())
                            {
                                phone_number = phone_number + cursor2.getString(cursor2.getColumnIndex("data1"));
                            }
                            cursor2.close();
                        }
                    }

                }
                cursor.close();

                if(phone_number.isEmpty())
                {
                    phone_number = "none";
                }

                editText_phone.setText(phone_number);

                builder.setView(newView);
                builder.setTitle("Motify information");

                builder.setPositiveButton("save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB db = new myDB(getBaseContext());
                        db.update(textView_name.getText().toString(),editText_birth.getText().toString(),editText_gift.getText().toString());
                        Toast.makeText(MainActivity.this,"Modify done",Toast.LENGTH_SHORT).show();
                        onDataBaseUpdate();
                    }
                });
                builder.setNegativeButton("back", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        //do nothing
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 100 && resultCode == 100)
        {
            onDataBaseUpdate();
        }
    }
}
