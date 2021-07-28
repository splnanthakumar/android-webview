package com.activeapps.tamilchat4u;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private WebView mWebview;
    private static final String WEB_URL = "https://systemxchat.com/";
    private static final String REQ_URL = "https://meet.jit.si/";
    private Activity activity = null;
    private ValueCallback<Uri[]> mUploadMessage;
    private GifImageView progressDialog;
    private GifImageView gifImageView;
    private Callback runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        progressDialog = findViewById(R.id.image);
        progressDialog.setVisibility(View.VISIBLE);

        mWebview = findViewById(R.id.webView);

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setSaveFormData(true);
        mWebview.getSettings().setAllowContentAccess(true);
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                progressDialog.setVisibility(View.GONE);
            }
        });

        activity = this;
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                // asegurar que no existan callbacks
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }

                mUploadMessage = filePathCallback;

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*"); // set MIME type to filter

                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FILECHOOSER_RESULTCODE);

                return true;
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {


                Log.d(TAG, "onPermissionRequest");
                activity.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        if (request.getOrigin().toString().equals(WEB_URL) || request.getOrigin().toString().equals(REQ_URL)) {
                            if (checkPermission()) {
                                request.grant(request.getResources());
                            } else {
                                requestPermission(new Callback() {
                                    @Override
                                    public void run(boolean result) {
                                        if (result) {
                                            request.grant(request.getResources());
                                        } else {
                                            request.deny();
                                        }
                                    }
                                });
                            }
                        } else {
                            request.deny();
                        }
                    }
                });
            }

        });
        mWebview.loadUrl(WEB_URL);
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

    private boolean checkPermission() {
        int audio = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        int camera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        return audio == PackageManager.PERMISSION_GRANTED && camera == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // manejo de seleccion de archivo
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {

            if (intent == null || resultCode != RESULT_OK) {
                if (null != mUploadMessage) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                }
                return;
            }

            Uri[] result = null;
            String dataString = intent.getDataString();

            if (dataString != null) {
                result = new Uri[]{Uri.parse(dataString)};
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }


    private void requestPermission(Callback runnable) {
        this.runnable = runnable;
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean result = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (runnable != null) {
                            runnable.run(result);
                        }
                    }
                }
        }
        runnable = null;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    static interface Callback {
        void run(boolean result);
    }
}

