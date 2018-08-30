package com.example.seunghyun.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import static android.speech.tts.TextToSpeech.ERROR;


import java.util.Locale;



public class ResultActivity extends AppCompatActivity {

    private TextToSpeech tts;   // TTS 변수 선언
    private Button BtnListen, BtnReturn;
    private final String testMessage = "hello";
    public ResultActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        BtnListen = (Button) findViewById(R.id.btn_listen);
        BtnReturn = (Button) findViewById(R.id.btn_return);

        // TTS를 생성하고 OnInitListener로 초기화 한다.
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        tts.speak(testMessage, TextToSpeech.QUEUE_FLUSH, null);

/*
        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("strParamName");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        matrix.preRotate(90);
        Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);

        ImageView img = (ImageView) findViewById(R.id.imageView1);
        img.setImageBitmap(adjustedBitmap); */

        BtnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Text에 있는 문장을 읽는다.
                tts.speak(testMessage, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        BtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

        @Override
        public void onDestroy () {
            super.onDestroy();
            // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
            if (tts != null) {
                tts.stop();
                tts.shutdown();
                tts = null;
            }
        }
}