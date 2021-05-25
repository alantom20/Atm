package com.home.atm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOGIN = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    boolean logon = false;
    private List<Function> functions;
    //    String[] functions = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode != RESULT_OK) {
                finish();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!logon){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent,REQUEST_LOGIN);
            //            startActivity(intent);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              makeNotification();
            }
        });
        //Recycler
        setupFunctions();
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
          recyclerView.setLayoutManager((new GridLayoutManager(this,3)));
        //Adapter
//       FunctionAdapter adapter = new FunctionAdapter(this);
        IconAdapter adapter = new IconAdapter();
        recyclerView.setAdapter(adapter);


    }

    private void makeNotification() {
        String channelId = "love";
        String channelName = "我的最愛";
        NotificationManager manager = getNotificationManager(channelId, channelName);
        Intent intent = new Intent(this,TransActivity.class);
        PendingIntent pIntent = PendingIntent.getActivities(this,0, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.heart1)
                .setContentTitle("This is Title")
                .setContentText("This is Text")
                .setSubText("This is info ")
                .setWhen(System.currentTimeMillis())
                .setChannelId(channelId)
                .setContentIntent(pIntent);
        manager.notify(1,builder.build());

    }

    private NotificationManager getNotificationManager(String channelId, String channelName) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        return manager;
    }


    private void setupFunctions() {
        functions = new ArrayList<>();
        String[] funcs = getResources().getStringArray(R.array.functions);
        functions.add(new Function(funcs[0],R.drawable.func_transaction));
        functions.add(new Function(funcs[1],R.drawable.func_balance));
        functions.add(new Function(funcs[2],R.drawable.func_finance));
        functions.add(new Function(funcs[3],R.drawable.func_contacts));
        functions.add(new Function(funcs[4],R.drawable.func_exit));
    }

    public class IconAdapter extends RecyclerView.Adapter<IconAdapter.iconHolder>{
        @NonNull
        @Override
        public iconHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_icon,viewGroup,false);
            return new iconHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull iconHolder iconHolder, int i) {
            final Function function = functions.get(i);
            iconHolder.nameText.setText(function.getName());
            iconHolder.iconImage.setImageResource(function.getIcon());
            iconHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClicked(function);
                }
            });
        }

        @Override
        public int getItemCount() {
            return functions.size();
        }

        public class iconHolder extends RecyclerView.ViewHolder{
            ImageView iconImage;
            TextView nameText;
            public iconHolder(@NonNull View itemView) {
                super(itemView);
                iconImage = itemView.findViewById(R.id.item_icon);
                nameText = itemView.findViewById(R.id.item_name);
            }
        }

    }

    private void itemClicked(Function function) {
        Log.d(TAG, "itemClicked: " + function.getName());
        switch (function.getIcon()){
            case R.drawable.func_transaction:
                startActivity(new Intent(this,TransActivity.class));
                break;
            case R.drawable.func_balance:
                break;
            case R.drawable.func_finance:
                Intent finance = new Intent(this,FinanceActivity.class);
                startActivity(finance);
                break;
            case  R.drawable.func_contacts:
                Intent contacts = new Intent(this, ContactActivity.class);
                startActivity(contacts);
                break;
            case R.drawable.func_exit:
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings : {
                return true;
            }
            case R.id.action_work : {
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class).setInitialDelay(30, TimeUnit.SECONDS).build();
                WorkManager.getInstance(this).enqueue(workRequest);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Log.d(TAG, "onOptionsItemSelected: "  + sdf.format(new Date()));


            }
        }




        return super.onOptionsItemSelected(item);
    }


}