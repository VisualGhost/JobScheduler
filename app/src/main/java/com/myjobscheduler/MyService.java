package com.myjobscheduler;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyService extends JobService {

    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                PersistableBundle bundle = jobParameters.getExtras();
                String data = bundle.getString(MainActivity.JOB_DATA_KEY);
                SystemClock.sleep(1000 * 60);
                sendNotification(data);
            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        mExecutor.shutdown();
        sendNotification("On stop Job");
        return true;
    }

    private void sendNotification(String message) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_chat_black_24dp)
                        .setContentTitle("This is a data")
                        .setContentText(message);

        NotificationManager nm = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        nm.notify(message.hashCode(), builder.build());
    }
}
