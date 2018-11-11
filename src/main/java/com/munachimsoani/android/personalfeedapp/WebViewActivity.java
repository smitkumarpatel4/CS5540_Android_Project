package com.munachimsoani.android.personalfeedapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = (WebView) findViewById(R.id.webview);

        Intent intent = getIntent();
        String urlString = null;
        if(intent.hasExtra("urlString")){

            urlString = intent.getStringExtra("urlString");
            Log.d("mycode",urlString);

            mWebView.loadUrl(urlString);


        }
    }
}
