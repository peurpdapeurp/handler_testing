package com.example.handler_testing;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class MockPlaybackQueue {

    private static final String TAG = "MockPlaybackQueue";

    // Messages
    public static final int MSG_TEST = 0;

    private Handler moduleMessageHandler_;

    public MockPlaybackQueue(Looper workThreadLooper) {
        moduleMessageHandler_ = new Handler(workThreadLooper) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_TEST: {
                        Log.d(TAG, "moduleMessageHandler_ got MSG_TEST");
                    }
                    default: {
                        throw new IllegalStateException("unexpected msg.what " + msg.what);
                    }
                }
            }
        };
    }

    public void sendTestMessage() {
        moduleMessageHandler_.obtainMessage(MSG_TEST).sendToTarget();
    }

}
