package com.example.mytasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mytasks.db.TasksSQLiteHelp;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TasksSQLiteHelp dbHelper = null;
    SQLiteDatabase db = null;
    ArrayAdapter<String> tasksAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<String> taskList = new ArrayList<>();
        tasksAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskList);
        ListView listView = (ListView) findViewById(R.id.tasksListView);
        listView.setAdapter(tasksAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {deleteTask(tasksAdapter.getItem(i));}
        });
        try{
            dbHelper = new TasksSQLiteHelp(getApplicationContext()); refreshTasks();
        }catch(Exception ex){
            new AlertDialog.Builder(this)
                    .setMessage(ex.getLocalizedMessage()).setPositiveButton("Ok", null).show();
        }
    }


    public void addTask(View view){
        try{
            EditText edit = (EditText)findViewById(R.id.newTaskEditText);
            String task = edit.getText().toString();
            db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO TASKS(TASK) VALUES('"+task+"')");
            db.close();
            dbHelper.close();
            edit.setText("");
            tasksAdapter.add(task);
        }catch(Exception ex){
            new AlertDialog.Builder(this)
                    .setMessage(ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }
    public void deleteTask(String task){
        try{
            db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM TASKS WHERE TASK = '"+task+"'");
            db.close();
            dbHelper.close();
            tasksAdapter.remove(task);
        }catch(Exception ex){
            new AlertDialog.Builder(this)
                    .setMessage(ex.getLocalizedMessage())
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    protected void refreshTasks(){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TASKS", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String nome = cursor.getString(1);
            tasksAdapter.add(nome);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        dbHelper.close();
    }
}