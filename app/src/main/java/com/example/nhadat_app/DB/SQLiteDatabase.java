package com.example.nhadat_app.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.nhadat_app.Model.UsSave;

public class SQLiteDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="nhadat.db";
    private static  final int DATABASE_VERSION=1;
    public SQLiteDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String sql="create table trangthai(id integer primary key autoincrement, category text)";
        db.execSQL(sql);
    }

    @Override
    public void onOpen(android.database.sqlite.SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int themTK(String a){
        ContentValues contentValues=new ContentValues();
        contentValues.put("category", a);
        android.database.sqlite.SQLiteDatabase st=getWritableDatabase();
        return (int) st.insert("trangthai", null, contentValues);
    }

    public String getTK(){
        String as = null;
        android.database.sqlite.SQLiteDatabase st=getReadableDatabase();
        Cursor re=st.query("trangthai", null,null,null,null,null,null);
        if ((re.moveToNext())){
            as=(re.getString(1));
        }
        else{
            as="null";
        };
        return as;
    }

    public int xoaTK(String id) {
        String whereClause = "category = ?";
        String[] whereArgs = {id};
        android.database.sqlite.SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("trangthai",
                whereClause, whereArgs);
    }
}
