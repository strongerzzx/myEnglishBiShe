package com.example.englishapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import utils.LogUtil;

/*
 词库详情页 + 搜索点击后的详情页
 */
public class CiKuDetailActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "CiKuDetailActivity";
    private TextView mDetailEng;
    private TextView mDetailFayin;
    private ImageView mDetailSound;
    private String mHeadWord;
    private String mTranCn;
    private String mTranCn1;
    private String mUkphone;
    private String mNewUkPhone;
    private TextToSpeech mSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ci_ku_detail);

        getData();

        initView();

        initEvent();
    }

    private void initEvent() {
        mDetailEng.setText(mHeadWord);
        mDetailFayin.setText(mUkphone);


        mDetailSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpeech != null && !mSpeech.isSpeaking()) {
                    mSpeech.speak(mHeadWord.trim(),TextToSpeech.QUEUE_FLUSH, null,1+"");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSpeech == null) {
            mSpeech = new TextToSpeech(CiKuDetailActivity.this,this);
            mSpeech.setPitch(1.2f);//音调
            mSpeech.setSpeechRate(1.3f);//语速
        }
    }

    private void initView() {
        mDetailEng = findViewById(R.id.ci_ku_detail_english);
        mDetailFayin = findViewById(R.id.ci_ku_detail_fayin);
        mDetailSound = findViewById(R.id.ci_ku_detail_sound);
    }

    private void getData() {
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        mHeadWord = bundle.getString("headWord");
        mTranCn = bundle.getString("tranCn");
        mTranCn1 = bundle.getString("tranCn1");
        mUkphone = bundle.getString("ukphone");
        mNewUkPhone = "["+ mUkphone +"]";

        LogUtil.d(TAG,mHeadWord +":"+ mTranCn +":"+ mTranCn1 +":"+ mNewUkPhone);
    }

    @Override
    public void onInit(int status) {
        //语音初始化
        if (status== TextToSpeech.SUCCESS) {
            mSpeech.setLanguage(Locale.ENGLISH);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSpeech != null) {
            mSpeech.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSpeech != null) {
            mSpeech.shutdown();
            mSpeech=null;
        }
    }
}
