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
import android.widget.ImageButton;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    private ImageButton btnStartCapture;
    private final static int PERMISSION_OK = 100;
    private final static int STORAGE_PERMISSION_OK = 101;
    private final static int AUDIO_PERMISSION_OK = 102;
    private boolean isCameraGranted = false;
    private boolean isStorageGranted = false;
    private boolean isAudioGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btnStartCapture = (ImageButton) findViewById(R.id.btn_start_capture);
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
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO},
                    PERMISSION_OK);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_OK:
                isCameraGranted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                isStorageGranted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);
                isAudioGranted = (grantResults[2] == PackageManager.PERMISSION_GRANTED);
                if (isCameraGranted && isStorageGranted && isAudioGranted) {
                    Intent intent = new Intent(StartActivity.this,QuestionActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this,"권한을 승인하지 않아 앱을 사용할 수 없습니다.",Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
