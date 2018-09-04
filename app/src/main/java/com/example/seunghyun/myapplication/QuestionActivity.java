package com.example.seunghyun.myapplication;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.seunghyun.myapplication.requests.FunctionRequest;
import com.example.seunghyun.myapplication.responses.SuccessResponse;
import com.example.seunghyun.myapplication.util.APIClient;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.speech.tts.TextToSpeech.ERROR;

public class QuestionActivity extends AppCompatActivity {
    private static String questionMessage = "유통기한 인식을 원하시면 휴대폰의 윗부분을 눌러주시고, 물체인식을 원하시면 아랫부분을 눌러주세요";
    private static String recognitionMessage = "찾으시는 물건을 말해주세요";
    private TextToSpeech tts;
    private Button btnExpiration;
    private Button btnRecognition;
    private RecognitionListener listener;
    private SpeechRecognizer mRecognizer;
    private Intent recogIntent;
    private final int OPTION_RECOGNITION = 10;
    private final int OPTION_EXPIRATION = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        btnExpiration = (Button)findViewById(R.id.btn_select_expiration);
        btnRecognition = (Button)findViewById(R.id.btn_select_recognition);

        // TTS를 생성하고 OnInitListener로 초기화 한다.
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                    tts.speak(questionMessage, TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });

        btnExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIClient.getInstance().create(FunctionRequest.class).expiration().enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                        if(response.isSuccessful()) {
                            Intent intent = new Intent(QuestionActivity.this, CaptureActivity.class);
                            intent.putExtra("option", "expiration");
                            intent.putExtra("id",response.body().getId());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessResponse> call, Throwable t) {

                    }
                });
            }
        });

        btnRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this,CaptureActivity.class);
                intent.putExtra("option","recognition");
                if(tts.isSpeaking())
                    tts.stop();
                tts.speak(recognitionMessage, TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                mRecognizer.startListening(recogIntent);

            }

            @Override
            public void onError(String utteranceId) {

            }
        });

        listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                String key = SpeechRecognizer.RESULTS_RECOGNITION;
                ArrayList<String> result = results.getStringArrayList(key);
                String sendingWord = selectWord(result);
                APIClient.getInstance().create(FunctionRequest.class).recognition(sendingWord).enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                        if(response.isSuccessful()) {
                            Intent intent = new Intent(QuestionActivity.this, CaptureActivity.class);
                            intent.putExtra("option", "recognition");
                            intent.putExtra("id",response.body().getId());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        };
        recogIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recogIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        recogIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);

    }

    public String selectWord(ArrayList<String> wordList) {
        int index = 0;
        for (int i=0; i<wordList.size(); i++) {
            if(wordList.get(i).length()>wordList.get(index).length())
                index=i;
        }
        return wordList.get(index);
    }





}
