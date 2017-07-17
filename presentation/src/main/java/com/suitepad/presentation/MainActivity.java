package com.suitepad.presentation;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String PAGE_URL = "file:///android_asset/index.html";

    @SuppressWarnings("FieldCanBeLocal")
    private WebView webView;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.my_web_view);
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(PAGE_URL);

    }

}
