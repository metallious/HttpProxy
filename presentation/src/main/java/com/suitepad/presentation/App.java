package com.suitepad.presentation;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by metal on 7/16/2017.
 */

public class App extends Application {

    /**
     * a dummy {@link ServiceConnection} object to bind the service to the presentation application
     * to prevent it from being destroyed
     */
    private static final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // need to make sure that the proxy server is running
        Intent proxyIntent = new Intent();
        proxyIntent.setComponent(new ComponentName(
                "com.suitepad.httpproxy", "com.suitepad.httpproxy.ProxyService"
        ));
        bindService(proxyIntent, connection, BIND_AUTO_CREATE|BIND_IMPORTANT);

        // need to make sure that the proxy settings are configured correctly
        // TODO: 7/17/2017 setup the proxy

    }


}
