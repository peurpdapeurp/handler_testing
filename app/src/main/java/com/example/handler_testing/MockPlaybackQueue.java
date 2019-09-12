package com.example.handler_testing;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

public class MockPlaybackQueue {

    private static final String TAG = "MockPlaybackQueue";

    // Private constants
    private static final int PROCESSING_INTERVAL_MS = 1000;

    // Messages
    public static final int MSG_DO_SOME_WORK = 0;
    public static final int MSG_TEST = 1;

    private Handler progressEventsHandler_;
    private Handler moduleMessageHandler_;
    private Handler workHandler_;

    public MockPlaybackQueue(Looper workThreadLooper) {

        progressEventsHandler_ = new Handler(workThreadLooper) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };

        moduleMessageHandler_ = new Handler(workThreadLooper) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_TEST: {
                        Log.d(TAG, "moduleMessageHandler_ got MSG_TEST");
                        break;
                    }
                    default: {
                        throw new IllegalStateException("unexpected msg.what " + msg.what);
                    }
                }
            }
        };

        workHandler_ = new Handler(workThreadLooper) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_DO_SOME_WORK: {
                        doSomeWork();
                        break;
                    }
                    default: {
                        throw new IllegalStateException("unexpected msg.what " + msg.what);
                    }
                }
            }
        };

        doSomeWork(); // start the workHandler's work cycle

    }

    public void sendTestMessage() {
        moduleMessageHandler_.obtainMessage(MSG_TEST).sendToTarget();
    }

    public void doSomeWork() {
        Log.d(TAG, "doSomeWork called");
        scheduleNextWork(SystemClock.uptimeMillis() + PROCESSING_INTERVAL_MS);
    }

    private void scheduleNextWork(long thisOperationStartTimeMs) {
        workHandler_.removeMessages(MSG_DO_SOME_WORK);
        workHandler_.sendEmptyMessageAtTime(MSG_DO_SOME_WORK, thisOperationStartTimeMs + PROCESSING_INTERVAL_MS);
    }

}
