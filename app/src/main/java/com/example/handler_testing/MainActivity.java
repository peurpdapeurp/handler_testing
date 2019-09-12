package com.example.handler_testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Messages
    private static final int MSG_WORK_THREAD_INITIALIZED = 0;

    // Thread objects
    private WorkThread workThread_;
    private WorkThread.Info workThreadInfo_;
    boolean workThreadInitialized_ = false;

    // Back-end modules
    MockPlaybackQueue mockPlaybackQueue_;

    // UI Objects
    Button sendTestMessageButton_;

    Handler handler_;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler_ = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what) {
                    case MSG_WORK_THREAD_INITIALIZED: {
                        Log.d(TAG, "Work thread initialized.");
                        workThreadInfo_ = (WorkThread.Info) msg.obj;
                        workThreadInitialized_ = true;
                        mockPlaybackQueue_ = new MockPlaybackQueue(workThreadInfo_.looper);
                    }
                }
            }
        };

        workThread_ = new WorkThread(new WorkThread.Callbacks() {
            @Override
            public void onInitialized(WorkThread.Info info) {
                handler_.obtainMessage(MSG_WORK_THREAD_INITIALIZED, info).sendToTarget();
            }
        });
        workThread_.start();

        sendTestMessageButton_ = (Button) findViewById(R.id.send_msg_test);
        sendTestMessageButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workThreadInitialized_) {
                    mockPlaybackQueue_.sendTestMessage();
                }
            }
        });

    }
}
