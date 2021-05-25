package com.home.atm;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyWorker extends Worker {
    private static final String TAG = MyWorker.class.getSimpleName();

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: ");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Log.d(TAG, "onOptionsItemSelected: "  + sdf.format(new Date()));
        return Result.success();
    }
}
