package com.example.seunghyun.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;

import static android.speech.tts.TextToSpeech.ERROR;


import java.util.Locale;


public class ResultActivity extends AppCompatActivity {

    private TextToSpeech tts;   // TTS 변수 선언
    private Button BtnListen, BtnReturn;
    private final String replayMessage = "다시듣고 싶으면 휴대폰의 윗부분을 누르시고, 촬영을 다시 하고 싶으시면 휴대폰의 아랫부분을 눌러주세요";
    private String resultMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        BtnListen = (Button) findViewById(R.id.btn_listen);
        BtnReturn = (Button) findViewById(R.id.btn_return);

        resultMessage = getIntent().getStringExtra("result");

        // TTS를 생성하고 OnInitListener로 초기화 한다.
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                    tts.speak(resultMessage, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                tts.speak(replayMessage, TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void onError(String utteranceId) {

            }
        });

        BtnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Text에 있는 문장을 읽는다.
                tts.speak(replayMessage, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        BtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, CaptureActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}