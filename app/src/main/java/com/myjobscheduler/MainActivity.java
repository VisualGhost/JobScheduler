package com.myjobscheduler;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final int JOB_ID = MyService.class.getSimpleName().hashCode();
    public static final String JOB_DATA_KEY = "job_data_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.job_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(21)
            public void onClick(View view) {
                JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

                ComponentName componentName = new ComponentName(getApplicationContext(),
                        MyService.class);

                PersistableBundle bundle = new PersistableBundle();
                bundle.putString(JOB_DATA_KEY, "Foo");

                JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                        .setRequiresCharging(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        .setRequiresDeviceIdle(true)
                        .setPersisted(true)
                        .setOverrideDeadline(TimeUnit.MINUTES.toMillis(1L))
                        .setExtras(bundle)
                        .build();
                int result = scheduler.schedule(jobInfo);
                if (result == JobScheduler.RESULT_FAILURE) {
                    textView.setText("Fail");
                } else {
                    textView.setText("Success");
                }
            }
        });
    }
}
