package com.example.administrator.lab5;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/12/11.
 */

public class myDB extends SQLiteOpenHelper{

    private static final String DB_NAME = "mydb.db";
    private static final String TABLE_NAME = "Person_Info";
    private static final int DB_VERSION = 1;

    public myDB(Context context,String name,SQLiteDatabase.CursorFactory factory,int version)
    {
        super(context, name , factory , version);
    }

    public myDB(Context context)
    {
        this(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                + "(name TEXT PRIMARY KEY,"
                + "birth TEXT,"
                + "gift TEXT)";
        try
        {
            db.execSQL(CREATE_TABLE);
        }
        catch (SQLException e)
        {
            //handle
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String name,String birth,String gift)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void update(String name, String birth, String gift)
    {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArges = {name};
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.update(TABLE_NAME,values,whereClause,whereArges);
        db.close();
    }

    public void delete(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArges = {name};

        db.delete(TABLE_NAME,whereClause,whereArges);
        db.close();
    }


}
