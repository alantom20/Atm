package com.home.atm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FinanceActivity extends AppCompatActivity {

    private ExpenseAdapter adapter;
    private RecyclerView recyclerView;
    private ExpenseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinanceActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.recylers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper = new ExpenseHelper(this);
        Cursor cursor = helper.getReadableDatabase()
                .query("expense",
                        null,null,null,
                        null,null,null);
        adapter = new ExpenseAdapter(cursor);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = helper.getReadableDatabase()
                .query("expense",
                        null,null,null,
                        null,null,null);
        adapter = new ExpenseAdapter(cursor);
        recyclerView.setAdapter(adapter);
    }

    public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.expenseHolder>{
        Cursor cursor;
        public ExpenseAdapter(Cursor cursor) {
             this.cursor = cursor;
        }

        @NonNull
        @Override
        public expenseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.expense_item,viewGroup,false);
            return new expenseHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull expenseHolder expenseHolder, int i) {
            cursor.moveToPosition(i);
            String date = cursor.getString(cursor.getColumnIndex("cdata"));
            String info = cursor.getString(cursor.getColumnIndex("info"));
            int amount =  cursor.getInt(cursor.getColumnIndex("amount"));
            expenseHolder.dateText.setText(date);
            expenseHolder.infoText.setText(info);
            expenseHolder.amountText.setText(String.valueOf(amount));
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        public class expenseHolder extends RecyclerView.ViewHolder{
            TextView dateText;
            TextView infoText;
            TextView amountText;

            public expenseHolder(@NonNull View itemView) {
                super(itemView);
                dateText = itemView.findViewById(R.id.item_data);
                infoText = itemView.findViewById(R.id.item_info);
                amountText = itemView.findViewById(R.id.item_amount);
            }
        }
    }
}