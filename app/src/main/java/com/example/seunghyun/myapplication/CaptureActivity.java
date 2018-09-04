package com.example.seunghyun.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.example.seunghyun.myapplication.requests.ResultRequest;
import com.example.seunghyun.myapplication.requests.UploadImageRequest;
import com.example.seunghyun.myapplication.responses.ResultResponse;
import com.example.seunghyun.myapplication.responses.SuccessResponse;
import com.example.seunghyun.myapplication.util.APIClient;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CaptureActivity extends Activity
        implements SurfaceHolder.Callback {

    @SuppressWarnings("deprecation")
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private String str;

    private AutoCaptureTask autoCaptureTask;
    private int id;
    private String option;
    private final int OPTION_RECOGNITION = 10;
    private final int OPTION_EXPIRATION = 11;

    @SuppressWarnings("deprecation")
    Camera.PictureCallback jpegCallback;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        option = intent.getStringExtra("option");
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        autoCaptureTask = new AutoCaptureTask();
        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
//                uploadImage(data);
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    str = String.format("%d.jpg", System.currentTimeMillis());
//
//                    File file = new File(Environment.getExternalStorageDirectory() + "/develop", str);
//                    FileOutputStream outputStream = null;
//                    try {
//                        outputStream = new FileOutputStream(file);
//                        outputStream.write(data);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), data);
                MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", "test", requestBody);
                APIClient.getInstance().create(UploadImageRequest.class).uploadImage(body,"test").enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                       if(!response.isSuccessful()) {
                           Toast.makeText(getApplicationContext(),"업로드에 실패했습니다.", Toast.LENGTH_LONG).show();
                       }
                    }

                    @Override
                    public void onFailure(Call<SuccessResponse> call, Throwable t) {

                    }
                });

                Toast.makeText(getApplicationContext(),
                        "Picture Saved", Toast.LENGTH_LONG).show();
                refreshCamera();


            }
        };

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @SuppressWarnings("deprecation")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startCapture();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        autoCaptureTask.cancel(true);

    }




    public void startCapture() {
        camera = Camera.open();
        camera.stopPreview();
        Camera.Parameters param = camera.getParameters();
        camera.setDisplayOrientation(90);
        camera.setParameters(param);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            autoCaptureTask.execute();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    class AutoCaptureTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(3000);
                    camera.takePicture(null, null, jpegCallback);
                    APIClient.getInstance().create(ResultRequest.class).requestResult(option, String.valueOf(id)).enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                            if(response.body().getIsComplete()=="true") {
                                Intent intent = new Intent(CaptureActivity.this,ResultActivity.class);
                                intent.putExtra("result",response.body().getProduct());
                                startActivity(intent);
                                cancel(true);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultResponse> call, Throwable t) {

                        }
                    });

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(CaptureActivity.this, ResultActivity.class);
            startActivity(intent);
            finish();


        }
    }
}