package com.example.seunghyun.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    private Button btnStartCapture;
    private final static int CAMERA_PERMISSION_OK = 100;
    private final static int STORAGE_PERMISSION_OK = 101;
    private final static int AUDIO_PERMISSION_OK = 102;
    private boolean isCameraGranted = false;
    private boolean isStorageGranted = false;
    private boolean isAudioGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btnStartCapture = (Button) findViewById(R.id.btn_start_capture);
        btnStartCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()) {
                    Intent intent = new Intent(StartActivity.this, QuestionActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean isPermissionGranted() {
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int storagePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int audioPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED
                && audioPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_OK);
        } else if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_OK);
        } else if (audioPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_OK);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_OK:
                isCameraGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                if (isCameraGranted) {
                    int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_OK);
                    }
                }
                break;
            case STORAGE_PERMISSION_OK:
                isStorageGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                if (isStorageGranted) {
                    int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
                    if (permission == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_OK);
                    }
                }
                break;
            case AUDIO_PERMISSION_OK:
                isAudioGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                if(isAudioGranted) {
                    Intent intent = new Intent(StartActivity.this,QuestionActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
