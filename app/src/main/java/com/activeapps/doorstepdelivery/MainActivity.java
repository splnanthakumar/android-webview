package com.activeapps.doorstepdelivery;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mWebview = findViewById(R.id.webView);
        mWebview.loadUrl("https://app.doorstepbuy.in/delivery-boy/");
        mWebview.setWebViewClient(new MyWebViewClient());
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        boolean canGoBack = null != mWebview && mWebview.canGoBack();
        if (canGoBack) {
            mWebview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}