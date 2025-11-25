package com.gegosoft.yourappname.Helper;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gegosoft.yourappname.Services.ScreenCapturerService;

@TargetApi(29)
public class ScreenCapturerManager {
    private ScreenCapturerService mService;
    private Context mContext;
    private State currentState = State.UNBIND_SERVICE;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            ScreenCapturerService.LocalBinder binder = (ScreenCapturerService.LocalBinder) service;
            mService = binder.getService();
            currentState = State.BIND_SERVICE;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    public enum State {
        BIND_SERVICE,
        START_FOREGROUND,
        END_FOREGROUND,
        UNBIND_SERVICE
    }

    public ScreenCapturerManager(Context context) {
        mContext = context;
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent(mContext, ScreenCapturerService.class);
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void startForeground() {
        mService.startForeground();
        currentState = State.START_FOREGROUND;
    }

    public void endForeground() {
        mService.endForeground();
        currentState = State.END_FOREGROUND;
    }

    public void unbindService() {
        mContext.unbindService(connection);
        currentState = State.UNBIND_SERVICE;
    }
}