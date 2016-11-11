package com.example.mytasks.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TasksSQLiteHelp extends SQLiteOpenHelper {
    public TasksSQLiteHelp(Context context){
        super(context, "tasks.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL
        ("CREATE TABLE TASKS(ID integer primary key autoincrement, TASK varchar NOT NULL);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TASKS");
        onCreate(sqLiteDatabase);
    }
}