package com.suitepad.httpproxy;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.littleshoot.proxy.HttpFiltersAdapter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Created by metal on 7/17/2017.
 */

class ProxyFilter extends HttpFiltersAdapter {

    private static final String TAG = "ProxyFilter";
    private ContentResolver contentResolver;

    ProxyFilter(ContentResolver cr,
                       HttpRequest originalRequest,
                       ChannelHandlerContext ctx) {
        super(originalRequest, ctx);
        contentResolver = cr;
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        Log.d(TAG, "clientToProxyRequest: " + httpObject.toString());
        Log.d(TAG, "clientToProxyRequest: hash: " + getHashedUri());
        Uri uri = new Uri.Builder()
                .scheme("content")
                .authority("com.suitepad.datasource")
                .path(getHashedUri())
                .build();
        Cursor qResponse = contentResolver.query(uri, null, null, null, null);
        if (qResponse != null && qResponse.moveToFirst()) {
            Log.d(TAG, "clientToProxyRequest: cache hit");
            byte[] blob = qResponse.getBlob(1);
            qResponse.close();
            contentResolver = null;
            ByteBuf buffer = Unpooled.wrappedBuffer(blob);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    buffer);
            HttpHeaders.setContentLength(response, buffer.readableBytes());
            HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, "application/json");
            HttpHeaders.setHeader(response, HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            HttpHeaders.setHeader(response, HttpHeaders.Names.ACCESS_CONTROL_ALLOW_HEADERS,
                    "Origin, X-Requested-With, Content-Type, Accept");
            return response;
        }
        Log.d(TAG, "clientToProxyRequest: cache miss");
        return super.clientToProxyRequest(httpObject);
    }

    private String getHashedUri() {
        String uri = originalRequest.getUri();
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(uri.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
