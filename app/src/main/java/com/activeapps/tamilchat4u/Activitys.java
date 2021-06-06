//package com.activeapps.tamilchat4u;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.hardware.Camera;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.webkit.GeolocationPermissions;
//import android.webkit.ValueCallback;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.widget.Toast;
//
//import java.io.File;
//import java.io.IOException;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class Activitys extends AppCompatActivity {
//    /** Check if this device has a camera */
//    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
//    private boolean checkCameraHardware(Context context) {
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//// this device has a camera
//            return true;
//        } else {
//// no camera on this device
//            return false;
//        }
//    }
//
//    /** A safe way to get an instance of the Camera object. */
//    public static Camera getCameraInstance(){
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//        }
//        catch (Exception e){
//// Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
//    }
//
//    private static final int INPUT_FILE_REQUEST_CODE = 1;
//    private static final int FILECHOOSER_RESULTCODE = 1;
//    private static final String TAG = MainActivity.class.getSimpleName();
//    Context context;
//    WebView webView;
//    private WebSettings webSettings;
//    private ValueCallback<Uri> mUploadMessage;
//    private Uri mCapturedImageURI = null;
//    private ValueCallback<Uri[]> mFilePathCallback;
//    public String mCameraPhotoPath;
//    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
//    private int webViewPreviousState;
//    private final int PAGE_STARTED = 0x1;
//    private final int PAGE_REDIRECTED = 0x2;
//    private static final int REQUEST_FINE_LOCATION = 1;
//    private String geolocationOrigin;
//    private GeolocationPermissions.Callback geolocationCallback;
//    private static final int CAMERA_REQUEST_CODE = 100;
//    private static final int MY_CAMERA_REQUEST_CODE = 100;
//    public static final int MY_PERMISSIONS_REQUEST = 0;
//    private boolean isError = false;
//    boolean isPageError = false;
//    private void processPickImage() {
//        if(hasCameraPermission()) {
//            pickImage();
//        }
//        else
//        {
//            requestCameraPermission();
//        }
//    }
//
//    private void pickImage() {
//    }
//
//    private boolean hasCameraPermission() {
//        return ContextCompat.checkSelfPermission(context,
//                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    private void requestCameraPermission() {
//        requestPermissions(new String[]{Manifest.permission.CAMERA},
//                MY_CAMERA_REQUEST_CODE );
//    }
//
//    private File createImageFile() throws IOException {
//// Create an image file name
//
//        String imageFileName = "pharmacy-photo-cheack-123-";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName, /* prefix */
//                ".jpg", /* suffix */
//                storageDir /* directory */
//        );
//
//// Save a file: path for use with ACTION_VIEW intents
//        mCameraPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState)
//    {
//        super.onSaveInstanceState(outState);
//        webView.saveState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState)
//    {
//        super.onRestoreInstanceState(savedInstanceState);
//        webView.restoreState(savedInstanceState);
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//        int MY_PERMISSIONS_REQUEST_CAMERA=0;
//// Here, this is the current activity
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//        {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
//            {
//
//            }
//            else
//            {
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
//// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//// app-defined int constant. The callback method gets the
//// result of the request.
//            }
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
//                super.onActivityResult(requestCode, resultCode, data);
//                return;
//            }
//            Uri[] results = null;
//// Check that the response is a good one
//            if (resultCode == Activity.RESULT_OK) {
//                if (data == null) {
//// If there is not data, then we may have taken a photo
//                    if (mCameraPhotoPath != null) {
//                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
//                    }
//                } else {
//                    String dataString = data.getDataString();
//                    if (dataString != null) {
//                        results = new Uri[]{Uri.parse(dataString)};
//                    }
//                }
//            }
//            mFilePathCallback.onReceiveValue(results);
//            mFilePathCallback = null;
//        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
//                super.onActivityResult(requestCode, resultCode, data);
//                return;
//            }
//            if (requestCode == FILECHOOSER_RESULTCODE) {
//                if (null == this.mUploadMessage) {
//                    return;
//                }
//                Uri result = null;
//                try {
//                    if (resultCode != RESULT_OK) {
//                        result = null;
//                    } else {
//// retrieve from the private variable if the intent is null
//                        result = data == null ? mCapturedImageURI : data.getData();
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "activity :" + e,
//                            Toast.LENGTH_LONG).show();
//                }
//                mUploadMessage.onReceiveValue(result);
//                mUploadMessage = null;
//            }
//        }
//        return;
//    }
