package com.home.atm;

import android.content.ContentValues;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = AddActivity.class.getSimpleName();
    private EditText edAmount;
    private EditText edInfo;
    private EditText edDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        edDate = findViewById(R.id.ed_date);
        edInfo = findViewById(R.id.ed_info);
        edAmount = findViewById(R.id.ed_amount);
    }
    public void add(View view){
        String date = edDate.getText().toString();
        String info = edInfo.getText().toString();
        int amount = Integer.parseInt(edAmount.getText().toString());
        ExpenseHelper helper = new ExpenseHelper(this);
        ContentValues values = new ContentValues();
        values.put("cdata",date);
        values.put("info",info);
        values.put("amount",amount);
        long id = helper.getWritableDatabase()
                .insert("expense",null,values);
        Log.d(TAG, "add: " + id);
        if (id > -1) {
            Toast.makeText(this, "新增成功", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"新增失敗",Toast.LENGTH_LONG).show();
            }
        }
    }
