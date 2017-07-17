package com.suitepad.httpproxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

public class ProxyService extends Service {

    private static final String TAG = "ProxyService";
    private HttpProxyServer server;

    public ProxyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        server = DefaultHttpProxyServer.bootstrap()
                .withPort(8081)
                .withFiltersSource(new HttpFiltersSource() {
                    @Override
                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                        Log.d(TAG, "filterRequest: " + originalRequest.getUri());
                        // by default cache only GET requests
                        if (originalRequest.getMethod() == HttpMethod.GET) {
                            return new ProxyFilter(getContentResolver(), originalRequest, ctx);
                        }
                        return null;
                    }

                    @Override
                    public int getMaximumRequestBufferSizeInBytes() {
                        return 0;
                    }

                    @Override
                    public int getMaximumResponseBufferSizeInBytes() {
                        return 10 * 1024 * 1024;
                    }
                })
                .start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        server.stop();
        super.onDestroy();
    }
}
